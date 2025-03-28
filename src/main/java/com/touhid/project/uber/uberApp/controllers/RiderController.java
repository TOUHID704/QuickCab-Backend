package com.touhid.project.uber.uberApp.controllers;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.WalletTransaction;
import com.touhid.project.uber.uberApp.services.RatingService;
import com.touhid.project.uber.uberApp.services.RideRequestService;
import com.touhid.project.uber.uberApp.services.RideService;
import com.touhid.project.uber.uberApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER") // Only users with RIDER role can access these APIs
@Slf4j
public class RiderController {

    private final RiderService riderService;
    private final RideService rideService;
    private final RatingService ratingService;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    /**
     * Rider requests a new ride.
     */
    @PostMapping("/ride/request")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));
    }


    /**
     * Rider rates the driver after ride completion.
     */
    @PostMapping("/driver/rate")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto) {
        Ride ride = rideService.getRideById(ratingDto.getRideId());
        return ResponseEntity.ok(ratingService.rateDriver(ride, ratingDto.getDriverRating()));
    }

    /**
     * Gets the rider's profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<RiderDto> getMyProfile() {
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    /**
     * Retrieves all rides of the rider.
     */
    @GetMapping("/rides")
    public ResponseEntity<Page<RideRequestDto>> getAllRidesForRider(
            @RequestParam Long riderId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RideRequestDto> rideRequests = rideRequestService.getAllRidesForRider(riderId, pageable);
        return ResponseEntity.ok(rideRequests);
    }

    /**
     * Checks the status of a ride request.
     */
    @GetMapping("/ride/{rideRequestId}/status")
    public ResponseEntity<RideDto> checkRideRequestStatus(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(riderService.checkMyRideRequestStatus(rideRequestId));
    }

    /**
     * Retrieves details of a specific ride.
     */
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<RideDto> getRideDetails(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideService.getMyRide(rideId));
    }

    /**
     * Retrieves ride history.
     */
    @GetMapping("/rides/history")
    public ResponseEntity<Page<RideDto>> getRideHistory(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize);
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }

    /**
     * Retrieves all active (ongoing) rides of the rider.
     */
    @GetMapping("/rides/active-rides")
    public ResponseEntity<Page<RideDto>> getActiveRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize);
        Page<Ride> rides = rideService.getActiveRides(pageRequest);
        Page<RideDto> rideDtos = rides.map(ride -> modelMapper.map(ride, RideDto.class));

        return ResponseEntity.ok(rideDtos);
    }

    @DeleteMapping("ride/cancel-request/{rideRequestId}")
    public ResponseEntity<String> cancelRideRequest(@PathVariable Long rideRequestId) {
        log.info("Rider requested to cancel ride request with ID: {}", rideRequestId);
        riderService.cancelRideRequest(rideRequestId);
        return ResponseEntity.ok("Ride request canceled successfully.");
    }

    @PatchMapping("ride/cancel-ride/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        log.info("Rider requested to cancel ride with ID: {}", rideId);
        RideDto canceledRide = riderService.cancelRide(rideId);
        return ResponseEntity.ok(canceledRide);
    }

    @GetMapping("wallet/getBalance")
    public ResponseEntity<WalletDto> getWalletBalance(){
        return ResponseEntity.ok(riderService.getWalletBalance());
    }

    @PutMapping("wallet/addBalance/{amount}")
    public ResponseEntity<WalletDto> addBalance(@PathVariable Double amount){
        return ResponseEntity.ok(riderService.addWalletBalance(amount));
    }

    @GetMapping("wallet/transactionHistory")
    public ResponseEntity<List<WalletTransactionDto>> getTransactionHistory(){
        return ResponseEntity.ok(riderService.getTransactionHistory());
    }
}
