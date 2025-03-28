package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.RideDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.RideRepository;
import com.touhid.project.uber.uberApp.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;
    private final CurrentRiderService currentRiderService;

    private final CurrentDriverService currentDriverService;




    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(()-> new ResourceNotFoundException("Ride not found with the id:"+rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        log.info("Starting to create a new ride for RideRequest ID: {}", rideRequest.getRideRequestId());

        Ride ride = new Ride();
        ride.setRideId(rideRequest.getRide().getRideId());
        ride.setPickupLocation(rideRequest.getPickupLocation());
        ride.setDropOffLocation(rideRequest.getDropOffLocation());
        ride.setRideCreatedTime(rideRequest.getRideRequestedTime());
        ride.setRider(rideRequest.getRider());
        ride.setRideStatus(RideStatus.PENDING);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setPaymentMethod(rideRequest.getPaymentMethod());
        ride.setFare(rideRequest.getFare());

        Ride savedRide = rideRepository.save(ride);
        log.info("New ride created successfully with Ride ID: {}", savedRide.getRideId());

        return savedRide;
    }


    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesByRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public RideDto getMyRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new ResourceNotFoundException("No Ride is found with the id" + rideId));
   return modelMapper.map(ride,RideDto.class);
    }

    @Override
    public Page<Ride> getActiveRides(PageRequest pageRequest) {
        Rider rider = currentRiderService.getCurrentRider();

        // Define active ride statuses (excluding CANCELLED, ENDED, and PENDING)
        List<RideStatus> activeStatuses = Arrays.asList(
                RideStatus.ACCEPTED,
                RideStatus.CONFIRMED,
                RideStatus.ONGOING
        );

        // Fetch rides that match any of these statuses
        return rideRepository.findByRiderAndRideStatusIn(rider, activeStatuses, pageRequest);
    }

    @Override
    public RideDto getDriverActiveRide() {
        Driver driver = currentDriverService.getCurrentDriver();

        Ride ride = rideRepository.findByDriverAndRideStatusIn(
                driver,
                Arrays.asList(RideStatus.ACCEPTED, RideStatus.ONGOING)
        ).orElseThrow(() -> new ResourceNotFoundException(
                "No active ride found for the driver " + driver
        ));

        return modelMapper.map(ride, RideDto.class);
    }



    private String generateRandomOTP(){
        Random random = new Random();
        int otpInt = random.nextInt(10000); // 0 to 9999
        return String.format("%04d",otpInt);
    }
}
