package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.DriverDto;
import com.touhid.project.uber.uberApp.dto.SignUpDto;
import com.touhid.project.uber.uberApp.dto.UserDto;
import com.touhid.project.uber.uberApp.enums.Role;

import java.util.Set;

public interface AuthService {

    String[]  login(String email,String password);
    UserDto signUp(SignUpDto signUpDto);
    DriverDto onboardNewDriver(Long userId,String vehicleId );


    String refreshToken(String refreshToken);

    Set<Role> getUserRolesByEmail(String email);
}
