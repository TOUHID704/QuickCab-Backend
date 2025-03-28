package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.RideDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing ride-related operations.
 */
public interface RideService {

    /**
     * Retrieves a ride by its unique ID.
     *
     * @param rideId The ID of the ride to retrieve.
     * @return The Ride entity corresponding to the provided ID.
     */
    Ride getRideById(Long rideId);


    /**
     * Creates a new ride and assigns it to a driver.
     *
     * @param rideRequestDto DTO containing the ride request details.
     * @param driverDto DTO containing details of the selected driver.
     * @return The newly created Ride entity.
     */
    Ride createNewRide(RideRequest rideRequest, Driver driver);

    /**
     * Updates the status of an existing ride.
     *
     * @param rideId The ID of the ride to update.
     * @param rideStatus The new status to assign to the ride.
     * @return The updated Ride entity.
     */
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    /**
     * Retrieves a paginated list of all rides associated with a specific rider.
     *
     * @param riderId The ID of the rider whose rides are to be fetched.
     * @param pageRequest Pagination details, such as page number and size.
     * @return A paginated list of rides for the specified rider.
     */


    /**
     * Retrieves a paginated list of all rides associated with a specific driver.
     *
     * @param driverId The ID of the driver whose rides are to be fetched.
     * @param pageRequest Pagination details, such as page number and size.
     * @return A paginated list of rides for the specified driver.
     */
    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);

    Page<Ride> getAllRidesByRider(Rider rider, PageRequest pageRequest);

    RideDto getMyRide(Long rideId);

    Page<Ride> getActiveRides(PageRequest pageRequest);


    RideDto getDriverActiveRide();
}
