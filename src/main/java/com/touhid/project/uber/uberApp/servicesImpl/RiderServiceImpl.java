package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.*;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.RideRepository;
import com.touhid.project.uber.uberApp.repositories.RideRequestRepository;
import com.touhid.project.uber.uberApp.repositories.RiderRepository;
import com.touhid.project.uber.uberApp.services.*;
import com.touhid.project.uber.uberApp.strategiesImpl.RideStrategyManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Builder
@Slf4j
public class RiderServiceImpl implements RiderService {

    private static final Logger logger = LoggerFactory.getLogger(RiderServiceImpl.class);

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final DriverService driverService;
    private final RideService rideService;
    private final RatingService ratingService;

    private final RideRepository rideRepository;

    private final DriverAvailabilityService driverAvailabilityService;

    private final CurrentRiderService currentRiderService;

    private final WalletService walletService;

    private final PaymentService paymentService;

    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        log.info("New ride request received from Rider ID: {}", currentRiderService.getCurrentRider().getRiderId());

        Rider rider = currentRiderService.getCurrentRider();
        log.info("Fetching current rider details. Rider ID: {}, Rating: {}", rider.getRiderId(), rider.getRating());

        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRider(rider);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        log.info("RideRequest initialized with status PENDING for Rider ID: {}", rider.getRiderId());

        // Calculate fare
        Double fare = rideStrategyManager.selectFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);
        log.info("Fare calculated: {} for RideRequest", fare);

        // Create an initial Ride without a driver (driver is null initially)
        log.info("Creating initial ride without driver for RideRequest ID: {}", rideRequest.getRideRequestId());
        Ride ride = rideService.createNewRide(rideRequest, null);
        rideRequest.setRide(ride);
        log.info("Initial Ride created with Ride ID: {} for RideRequest ID: {}", ride.getRideId(), rideRequest.getRideRequestId());

        // Save ride request
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        log.info("RideRequest saved successfully with ID: {}", savedRideRequest.getRideRequestId());

        // Finding matching drivers
        log.info("Finding matching drivers for RideRequest ID: {} based on Rider rating: {}", savedRideRequest.getRideRequestId(), rider.getRating());
        List<Driver> drivers = rideStrategyManager
                .selectDriverMatchingStrategy(rider.getRating())
                .findMatchingDriver(rideRequest);

        log.info("Found {} potential drivers for RideRequest ID: {}", drivers.size(), savedRideRequest.getRideRequestId());

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }


    @Override
    @Transactional
    public void cancelRideRequest(Long rideRequestId) {
        log.info("Attempting to cancel ride request with ID: {}", rideRequestId);

        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> {
                    log.error("RideRequest not found for ID: {}", rideRequestId);
                    return new ResourceNotFoundException("RideRequest not found with ID: " + rideRequestId);
                });

        Rider currentRider = currentRiderService.getCurrentRider();
        if (!rideRequest.getRider().equals(currentRider)) {
            log.warn("Rider ID: {} attempted to cancel RideRequest ID: {} but is not the owner",
                    currentRider.getRiderId(), rideRequestId);
            throw new RuntimeException("You are not authorized to cancel this ride request.");
        }

        Ride ride = rideRequest.getRide();
        if (ride != null) {
            rideRepository.delete(ride);
            log.info("Deleted associated ride with ID: {}", ride.getRideId());
        }

        rideRequestRepository.delete(rideRequest);
        log.info("RideRequest ID: {} successfully canceled and deleted", rideRequestId);
    }

    @Override
    @Transactional
    public RideDto cancelRide(Long rideId) {
        log.info("Attempting to cancel ride with ID: {}", rideId);

        Ride ride = rideService.getRideById(rideId);
        RideRequest rideRequest = rideRequestRepository.findByRideRequestId(rideId);

        if (ride == null || rideRequest == null) {
            log.error("Ride or RideRequest not found for ID: {}", rideId);
            throw new ResourceNotFoundException("Ride or RideRequest not found.");
        }

        Rider currentRider = currentRiderService.getCurrentRider();
        if (!ride.getRider().equals(currentRider)) {
            log.warn("Rider ID: {} attempted to cancel Ride ID: {} but is not the owner",
                    currentRider.getRiderId(), rideId);
            throw new RuntimeException("You are not authorized to cancel this ride.");
        }

        if (ride.getRideStatus().equals(RideStatus.ONGOING)) {
            log.warn("Ride ID: {} is already ongoing and cannot be canceled", rideId);
            throw new RuntimeException("Cannot cancel an ongoing ride.");
        }

        ride.setRideStatus(RideStatus.CANCELLED);
        rideRequest.setRideRequestStatus(RideRequestStatus.CANCELLED);
        Ride savedRide = rideRepository.save(ride);
        rideRequestRepository.save(rideRequest);
        log.info("Ride ID: {} and associated RideRequest ID: {} marked as CANCELLED", rideId, rideRequest.getRideRequestId());

        // If a driver was assigned, make them available again
        if (ride.getDriver() != null) {
            driverAvailabilityService.updateDriverAvailability(ride.getDriver(), true);
            log.info("Driver ID: {} marked as available again", ride.getDriver().getDriverId());
        }

        return modelMapper.map(savedRide, RideDto.class);
    }




    @Override
    public DriverDto rateDriver(Long rideId, Integer rating,Long riderId) {

        Ride ride = rideService.getRideById(rideId);
        Rider rider = riderRepository.findById(rideId).orElseThrow(()-> new ResourceNotFoundException("Rider not found with the id"+riderId));

        if (!rider.equals(ride.getRider())) {
            throw new RuntimeException("You are not authorized" + ride.getRider().getRiderId() + "!=" + rider.getRiderId());
        }

        if (!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride Status is not ended hence cannot be rated"+ride.getRideStatus());

        }
        return ratingService.rateDriver(ride,rating);

    }

    @Override
    public RiderDto getMyProfile() {
        Rider rider = currentRiderService.getCurrentRider();
        return modelMapper.map(rider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider rider = currentRiderService.getCurrentRider();
        Page<Ride> rides = rideService.getAllRidesByRider(rider, pageRequest);
        return rides.map(ride -> modelMapper.map(ride, RideDto.class));
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = new Rider();
        rider.setUser(user);
        rider.setRating(0.0);
        return riderRepository.save(rider);
    }



    @Override
    public RideDto checkMyRideRequestStatus(Long rideRequestId) {
        RideRequest rideRequest = rideRequestRepository.findByRideRequestId(rideRequestId);
        if (rideRequest == null) {
            throw new ResourceNotFoundException("Ride request not found.");
        }

        Ride ride = rideRequest.getRide();

        if (rideRequest.getRideRequestStatus().equals(RideRequestStatus.ACCEPTED) && ride != null) {
            return modelMapper.map(ride, RideDto.class);
        } else {
            throw new ResourceNotFoundException("Ride request is not accepted yet.");
        }
    }

    @Override
    public WalletDto getWalletBalance() {
        Rider rider = currentRiderService.getCurrentRider();
        return walletService.getWalletByUser(rider.getUser());
    }

    @Override
    public WalletDto addWalletBalance(Double amount) {
        Rider rider = currentRiderService.getCurrentRider();
        return walletService.addFundsToWallet(rider.getUser(),amount,null,null, TransactionMethod.BANKING);

    }

    @Override
    public List<WalletTransactionDto> getTransactionHistory() {
        User user = currentRiderService.getCurrentRider().getUser();
        Wallet wallet = walletService.findWalletByUser(user);
        List<WalletTransaction> walletTransactionList = wallet.getTransactions();

        return walletTransactionList.stream()
                .map(walletTransaction -> modelMapper.map(walletTransaction, WalletTransactionDto.class))
                .collect(Collectors.toList());
    }


}
