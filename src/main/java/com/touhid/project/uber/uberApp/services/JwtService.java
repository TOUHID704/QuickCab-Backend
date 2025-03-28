package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.entities.User;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Long getUserIdFromToken(String token);
}
