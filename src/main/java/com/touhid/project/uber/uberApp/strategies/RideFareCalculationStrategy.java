package com.touhid.project.uber.uberApp.strategies;

import com.touhid.project.uber.uberApp.entities.RideRequest;

public interface RideFareCalculationStrategy {

    static final double RIDE_FARE_MULTIPLIER = 20.67;

    double calculateFare(RideRequest rideRequest);
}
