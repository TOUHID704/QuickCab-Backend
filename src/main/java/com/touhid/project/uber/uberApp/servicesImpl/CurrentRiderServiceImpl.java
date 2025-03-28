package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.RiderRepository;
import com.touhid.project.uber.uberApp.services.CurrentRiderService;
import com.touhid.project.uber.uberApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentRiderServiceImpl implements CurrentRiderService {

    private final RiderRepository riderRepository;

    @Override
    public Rider getCurrentRider() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        // Get the current authenticated user
        Object principal = auth.getPrincipal();



        // Ensure the principal is of type User
        if (!(principal instanceof User)) {
            throw new IllegalStateException("Authenticated principal is not a valid User.");
        }

        User user = (User) principal;

        // Fetch and return the Rider, or throw an exception if not found
        return riderRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No Rider found for the user ID: " + user.getUserId()));
    }
}
