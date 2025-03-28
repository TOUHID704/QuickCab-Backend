package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service interface defining operations for riders in the system.
 */
public interface RiderService {

    /**
     * Creates a new ride request for a rider.
     *
     * @param rideRequestDto The DTO containing the ride request details.
     * @return A DTO representing the created ride request.
     */
    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    /**
     * Cancels an ongoing or upcoming ride.
     *
     * @param rideId The unique ID of the ride to be canceled.
     * @return A DTO representing the canceled ride.
     */
    RideDto cancelRide(Long requestId);


    void cancelRideRequest(Long rideRequestId);

    /**
     * Rates a driver after completing a ride.
     *
     * @param rideId The unique ID of the ride for which the driver is being rated.
     * @param rating The rating to give the driver (e.g., 1 to 5).
     * @return A DTO representing the driver with the updated rating.
     */
    DriverDto rateDriver(Long rideId, Integer rating,Long riderId);

    /**
     * Retrieves the profile of the currently logged-in rider.
     *
     * @return A DTO representing the rider's profile.
     */
    RiderDto getMyProfile();

    /**
     * Retrieves all rides taken by the currently logged-in rider.
     *
     * @return A list of DTOs representing the rider's past rides.
     */

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);





    RideDto checkMyRideRequestStatus(Long rideRequestId);


    WalletDto getWalletBalance();

    WalletDto addWalletBalance(Double amount);

    List<WalletTransactionDto> getTransactionHistory();
}
