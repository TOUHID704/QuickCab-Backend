package com.touhid.project.uber.uberApp.controllers;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.services.DriverService;
import com.touhid.project.uber.uberApp.services.RatingService;
import com.touhid.project.uber.uberApp.services.RideService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/driver")
@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;
    private final RideService rideService;
    private final RatingService ratingService;

    @PostMapping("/ride/{rideRequestId}/accept")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/ride/{rideId}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId, @RequestBody RideStartDto rideStartDto) {
        return ResponseEntity.ok(driverService.startRide(rideId, rideStartDto.getOtp()));
    }

    @PostMapping("/ride/{rideId}/end")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/ride/{rideId}/cancel")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @GetMapping("/profile")
    public ResponseEntity<DriverDto> getDriverProfile() {
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @PostMapping("/rating/rider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {
        Ride ride = rideService.getRideById(ratingDto.getRideId());
        return ResponseEntity.ok(ratingService.rateRider(ride, ratingDto.getRiderRating()));
    }

    @GetMapping("/rides")
    public ResponseEntity<Page<RideDto>> getDriverRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize);
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }

    @GetMapping("/ride/requests")
    public ResponseEntity<List<RideRequestDto>> getAllRideRequests() {
        return ResponseEntity.ok(driverService.getAllRideRequests());
    }

    @GetMapping("/ride/active-ride")
    public ResponseEntity<RideDto> getActiveRide(){
       return ResponseEntity.ok(rideService.getDriverActiveRide());
    }

    @GetMapping("/rides/history")
    public ResponseEntity<Page<RideDto>> getRideHistory(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize);
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }


    @GetMapping("wallet/getBalance")
    public ResponseEntity<WalletDto> getWalletBalance(){
        return ResponseEntity.ok(driverService.getWalletBalance());
    }

    @PutMapping("wallet/addBalance/{amount}")
    public ResponseEntity<WalletDto> addBalance(@PathVariable Double amount){
        return ResponseEntity.ok(driverService.addWalletBalance(amount));
    }

    @GetMapping("wallet/transactionHistory")
    public ResponseEntity<List<WalletTransactionDto>> getTransactionHistory(){
        return ResponseEntity.ok(driverService.getTransactionHistory());
    }

}
