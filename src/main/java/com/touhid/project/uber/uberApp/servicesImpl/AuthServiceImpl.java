package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.DriverDto;
import com.touhid.project.uber.uberApp.dto.SignUpDto;
import com.touhid.project.uber.uberApp.dto.UserDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.enums.Role;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.touhid.project.uber.uberApp.repositories.UserRepository;
import com.touhid.project.uber.uberApp.services.AuthService;
import com.touhid.project.uber.uberApp.services.DriverService;
import com.touhid.project.uber.uberApp.services.RiderService;
import com.touhid.project.uber.uberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    // Injecting AuthenticationManager
    private final AuthenticationManager authenticationManager;

        @Override
        public String[] login(String email, String password) {
            // Authenticate the user with email and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Get the authenticated user
            User user = (User) authentication.getPrincipal();

            // Generate access and refresh tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return new String[]{accessToken, refreshToken};
        }

    @Override
    @Transactional
    public UserDto signUp(SignUpDto signUpDto) {
        // Check if user already exists with the provided email
        userRepository.findByEmail(signUpDto.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeConflictException("Cannot sign up, user already exists with email " + signUpDto.getEmail());
                });

        // Map the SignUpDto to a User entity
        User mappedUser = modelMapper.map(signUpDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        // Save the new user and create a new rider and wallet
        User savedUser = userRepository.save(mappedUser);
        Rider rider = riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);

        // Return the saved user as a UserDto
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the ID " + userId));

        // Check if user is already a driver
        if (user.getRoles().contains(Role.DRIVER)) {
            throw new RuntimeConflictException("User with ID " + userId + " is already a driver");
        }

        // Create and save the new driver
        Driver driver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();

        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(driver);

        // Return the saved driver as a DriverDto
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the id"+userId));

        return jwtService.generateAccessToken(user);

    }

    // Method to get user roles by email
    public Set<Role> getUserRolesByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getRoles).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
