package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.DriverDto;
import com.touhid.project.uber.uberApp.dto.RiderDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Rating;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.DriverRepository;
import com.touhid.project.uber.uberApp.repositories.RatingRepository;
import com.touhid.project.uber.uberApp.repositories.RiderRepository;
import com.touhid.project.uber.uberApp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {


    private final DriverRepository driverRepository;
    private final RatingRepository ratingRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Rating createNewRating(Ride ride, Integer driverRating, Integer riderRating) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .driverRating(driverRating)
                .riderRating(riderRating)
                .build();

        return ratingRepository.save(rating);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer riderRating) {
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(() -> new ResourceNotFoundException("Rating not found with the ride Id" + ride.getRideId()));
        ratingObj.setRiderRating(riderRating);

        ratingRepository.save(ratingObj);

        Double averageRiderRating = ratingRepository.findAverageRiderRatingByRiderId(ride.getRider().getRiderId());
        Rider rider = ride.getRider();
        rider.setRating(averageRiderRating);

        riderRepository.save(rider);

        return modelMapper.map(rider, RiderDto.class);
    }

    @Override
    public DriverDto rateDriver(Ride ride, Integer driverRating) {
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(() -> new ResourceNotFoundException("Rating not found with the ride Id" + ride.getRideId()));
        ratingObj.setDriverRating(driverRating);

        ratingRepository.save(ratingObj);

        Double averageDriverRating = ratingRepository.findAverageDriverRatingByDriverId(ride.getDriver().getDriverId());
        Driver driver = ride.getDriver();
        driver.setRating(averageDriverRating);

        driverRepository.save(driver);

        return modelMapper.map(driver, DriverDto.class);
    }

    @Override
    public List<Rating> getRatingsByRider(Rider rider) {
        return ratingRepository.findByRider(rider);
    }

    @Override
    public List<Rating> getRatingsForDriver(Driver driver) {
        return ratingRepository.findByDriver(driver);
    }

    @Override
    public Double getAverageRatingForDriver(Driver driver) {
        return ratingRepository.findAverageDriverRatingByDriverId(driver.getDriverId());
    }

    @Override
    public Double getAverageRatingForRider(Rider rider) {
        return ratingRepository.findAverageRiderRatingByRiderId(rider.getRiderId());
    }

    @Override
    public Optional<Rating> getRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride);
    }


}
