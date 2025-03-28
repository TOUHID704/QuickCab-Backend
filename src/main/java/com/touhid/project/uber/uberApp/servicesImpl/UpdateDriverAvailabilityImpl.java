package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.repositories.DriverRepository;
import com.touhid.project.uber.uberApp.services.DriverAvailabilityService;
import com.touhid.project.uber.uberApp.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDriverAvailabilityImpl implements DriverAvailabilityService {

     private final DriverRepository driverRepository;

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }
}
