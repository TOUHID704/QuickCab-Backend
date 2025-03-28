package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);
    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);
    Page<Ride> findByRiderAndRideStatusIn(Rider rider, List<RideStatus> statuses, Pageable pageable);
    Optional<Ride> findByDriverAndRideStatus(Driver driver, RideStatus rideStatus);
    Optional<Ride> findByDriverAndRideStatusIn(Driver driver, List<RideStatus> asList);
}
