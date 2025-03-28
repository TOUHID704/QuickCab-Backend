package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Rating;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRide(Ride ride);
    List<Rating> findByDriver(Driver driver);
    List<Rating> findByRider(Rider rider);
    @Query("SELECT AVG(r.driverRating) FROM Rating r WHERE r.driver.id = :driverId")
    Double findAverageDriverRatingByDriverId(@Param("driverId") Long driverId);

    @Query("SELECT AVG(r.riderRating) FROM Rating r WHERE r.rider.id = :riderId")
    Double findAverageRiderRatingByRiderId(@Param("riderId") Long riderId);

}
