package com.touhid.project.uber.uberApp.controllers;

import com.touhid.project.uber.uberApp.dto.*;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.enums.Role;
import com.touhid.project.uber.uberApp.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.util.Arrays;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    ResponseEntity<UserDto> signUp( @Valid @RequestBody SignUpDto signUpDto){
      return new ResponseEntity<>(authService.signUp(signUpDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("onBoardNewDriver/{userId}")
    ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId, @RequestBody OnboardDriverDto onboardDriverDto){
        return new ResponseEntity<>(authService.onboardNewDriver(userId,onboardDriverDto.getVehicleId()),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){

        String tokens[] = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // Set the access token cookie
        Cookie tokenCookie = new Cookie("token", tokens[0]);
        tokenCookie.setPath("/"); // Ensure the cookie is available for all endpoints
        tokenCookie.setDomain("localhost");
        tokenCookie.setHttpOnly(false); // Makes it inaccessible via JavaScript
        tokenCookie.setSecure(false); // Set to true if using HTTPS
        tokenCookie.setMaxAge(60 * 60); // Cookie expires in 1 hour
        httpServletResponse.addCookie(tokenCookie);

        // Set the refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens[1]);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain("localhost");
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setMaxAge(60 * 10); // Cookie expires in 10 minutes
        httpServletResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0])); // Return access token in response body

    }


    @PostMapping("/refresh")
    ResponseEntity<LoginResponseDto> refresh(HttpServletRequest httpServletRequest){

        String refreshToken = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found in cookies"));

        String accessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new LoginResponseDto(accessToken));

    }


    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletResponse httpServletResponse) {
        // Clear the token cookie
        Cookie accessTokenCookie = new Cookie("token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // Removes the cookie
        httpServletResponse.addCookie(accessTokenCookie);

        // Clear the refresh token cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // Removes the cookie
        httpServletResponse.addCookie(refreshTokenCookie);
        return ResponseEntity.ok("Logged out successfully");
    }



    @GetMapping("/roles")
    public Set<Role> getRolesByEmail(@RequestParam String email) {
        return authService.getUserRolesByEmail(email);
    }





}
