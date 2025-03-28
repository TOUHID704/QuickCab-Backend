package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ProblemDetail;

import java.util.List;

public interface DriverService {

    RideDto cancelRide(Long rideId);
    RideDto acceptRide(Long rideRequestId);
    RideDto startRide(Long rideId,String otp);
    RideDto endRide(Long rideId);
    RiderDto rateRider(Long rideId, Integer rating);
    DriverDto getMyProfile();
    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver createNewDriver(Driver driver);


    List<RideRequestDto> getAllRideRequests();


    WalletDto getWalletBalance();

    WalletDto addWalletBalance(Double amount);

    List<WalletTransactionDto> getTransactionHistory();
}
