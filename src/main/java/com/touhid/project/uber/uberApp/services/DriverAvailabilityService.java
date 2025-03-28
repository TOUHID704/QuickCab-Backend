package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.entities.Driver;

public interface DriverAvailabilityService {

    Driver updateDriverAvailability(Driver driver, boolean available);
}
