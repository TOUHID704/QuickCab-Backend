package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.*;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.DriverRepository;
import com.touhid.project.uber.uberApp.repositories.RatingRepository;
import com.touhid.project.uber.uberApp.repositories.RideRepository;
import com.touhid.project.uber.uberApp.repositories.RideRequestRepository;
import com.touhid.project.uber.uberApp.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;
    private final RatingRepository ratingRepository;
    private final RideRequestRepository rideRequestRepository;
    private final DriverAvailabilityService driverAvailabilityService;
    private final CurrentDriverService currentDriverService;

    private final WalletService walletService;

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = currentDriverService.getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("You are not authorized to cancel this ride.");
        }

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverAvailabilityService.updateDriverAvailability(driver, true);
        rideRepository.save(ride);

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        log.info("Driver attempting to accept ride request with ID: {}", rideRequestId);

        // Fetch the ride request
        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> {
                    log.error("RideRequest not found for ID: {}", rideRequestId);
                    return new RuntimeException("RideRequest not found for ID: " + rideRequestId);
                });

        // Ensure the ride request is still pending
        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            log.warn("RideRequest ID: {} cannot be accepted. Current status: {}", rideRequestId, rideRequest.getRideRequestStatus());
            throw new RuntimeException("RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatus());
        }

        // Fetch the current driver
        Driver currentDriver = currentDriverService.getCurrentDriver();
        log.info("Driver ID: {} attempting to accept the ride request", currentDriver.getDriverId());

        // Check if the driver is available
        if (!currentDriver.getAvailable()) {
            log.warn("Driver ID: {} is not available to accept rides", currentDriver.getDriverId());
            throw new RuntimeException("Driver is not available to accept this ride.");
        }

        // Update driver availability
        driverAvailabilityService.updateDriverAvailability(currentDriver, false);
        log.info("Driver ID: {} marked as unavailable", currentDriver.getDriverId());

        // Create ride and associate it with the ride request
        Ride ride = rideService.createNewRide(rideRequest, currentDriver);
        rideRequest.setRide(ride);
        log.info("Ride created successfully for RideRequest ID: {} with Ride ID: {}", rideRequestId, ride.getRideId());

        // Update ride request status to ACCEPTED
        rideRequest.setRideRequestStatus(RideRequestStatus.ACCEPTED);
        rideRequestRepository.save(rideRequest);
        log.info("RideRequest ID: {} status updated to ACCEPTED", rideRequestId);

        // Update ride status to ACCEPTED
        ride.setRideStatus(RideStatus.ACCEPTED);
        rideRepository.save(ride);
        log.info("Ride ID: {} status updated to ACCEPTED", ride.getRideId());

        return modelMapper.map(ride, RideDto.class);
    }




    @Override
    public RideDto startRide(Long rideId, String otp) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = currentDriverService.getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("You are not authorized to start this ride.");
        }

        if (!ride.getRideStatus().equals(RideStatus.ACCEPTED)) {
            throw new RuntimeException("Ride status is not ACCEPTED. Cannot start the ride.");
        }

        if (!otp.equals(ride.getOtp())) {
            throw new RuntimeException("Invalid OTP provided.");
        }

        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);


        paymentService.createPayment(savedRide);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = currentDriverService.getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("You are not authorized to end this ride.");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING)) {
            throw new RuntimeException("Ride status is not ONGOING. Cannot end the ride.");
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        driverAvailabilityService.updateDriverAvailability(driver, true);

        paymentService.processPayment(ride);

        if (ratingRepository.findByRide(ride).isEmpty()) {
            ratingService.createNewRating(savedRide, null, null);
        }

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = currentDriverService.getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("You are not authorized to rate this rider.");
        }

        if (!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Cannot rate the rider. Ride is not yet completed.");
        }

        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = currentDriverService.getCurrentDriver();
        log.info(currentDriver.toString());
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver driver = currentDriverService.getCurrentDriver();
        Page<Ride> rides = rideService.getAllRidesOfDriver(driver, pageRequest);
        return rides.map(ride -> modelMapper.map(ride, RideDto.class));
    }


    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public List<RideRequestDto> getAllRideRequests() {
        // Retrieve RideRequests with status PENDING
        List<RideRequest> pendingRequests = rideRequestRepository.findByRideRequestStatus(RideRequestStatus.PENDING);

        // Convert entities to DTOs using ModelMapper
        return pendingRequests.stream()
                .map(request -> modelMapper.map(request, RideRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WalletDto getWalletBalance() {
        Driver driver = currentDriverService.getCurrentDriver();
        return walletService.getWalletByUser(driver.getUser());
    }

    @Override
    public WalletDto addWalletBalance(Double amount) {
        Driver driver = currentDriverService.getCurrentDriver();
        return walletService.addFundsToWallet(driver.getUser(),amount,null,null, TransactionMethod.BANKING);

    }

    @Override
    public List<WalletTransactionDto> getTransactionHistory() {
        User user = currentDriverService.getCurrentDriver().getUser();
        Wallet wallet = walletService.findWalletByUser(user);
        List<WalletTransaction> walletTransactionList = wallet.getTransactions();

        return walletTransactionList.stream()
                .map(walletTransaction -> modelMapper.map(walletTransaction, WalletTransactionDto.class))
                .collect(Collectors.toList());
    }


}
