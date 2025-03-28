package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest,Long> {

    RideRequest findByRideRequestId(Long rideRequestId);

    // Fetch all RideRequests for a specific Rider
    List<RideRequest> findByRider(Rider rider);

    List<RideRequest> findByRideRequestStatus(RideRequestStatus rideRequestStatus   );

    // Alternatively, if you only need the rider's ID
    Page<RideRequest> findByRiderRiderId(Long riderId, Pageable pageable);


    // Method to filter by a single status
    Page<RideRequest> findByRiderRiderIdAndRideRequestStatus(Long riderId, RideRequestStatus rideRequestStatus, Pageable pageable);

    // Method to filter by multiple statuses
    Page<RideRequest> findByRiderRiderIdAndRideRequestStatusIn(Long riderId, List<RideRequestStatus> rideRequestStatuses, Pageable pageable);

    // Fetch RideRequest by Ride
    Optional<RideRequest> findByRide(Ride ride);

}
