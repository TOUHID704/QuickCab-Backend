package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.DriverRepository;
import com.touhid.project.uber.uberApp.services.CurrentDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentDriverServiceImpl implements CurrentDriverService {

    private final DriverRepository driverRepository;
    @Override
    public Driver getCurrentDriver() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User)) {
            throw new IllegalStateException("Authenticated principal is not a valid User.");
        }

        User user = (User) principal;

        return driverRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No Driver found for user ID: " + user.getUserId()));
    }
}
