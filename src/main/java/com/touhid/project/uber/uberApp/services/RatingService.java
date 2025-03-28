package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.DriverDto;
import com.touhid.project.uber.uberApp.dto.RiderDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Rating;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.Rider;

import java.util.List;
import java.util.Optional;
public interface RatingService {

    DriverDto rateDriver(Ride ride, Integer driverRating);

    RiderDto rateRider(Ride ride, Integer riderRating);

    Rating createNewRating(Ride ride,Integer driverRating, Integer riderRating);


    List<Rating> getRatingsByRider(Rider rider);

    List<Rating> getRatingsForDriver(Driver driver);

    Double getAverageRatingForDriver(Driver driver);

    Double getAverageRatingForRider(Rider rider);

    Optional<Rating> getRatingByRide(Ride ride);
}
