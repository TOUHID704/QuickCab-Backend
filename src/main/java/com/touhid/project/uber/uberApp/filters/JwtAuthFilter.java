package com.touhid.project.uber.uberApp.filters;

import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.services.JwtService;
import com.touhid.project.uber.uberApp.servicesImpl.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;  // Service for managing JWT tokens
    private final UserDetailServiceImpl userDetailServiceImpl;  // Service to get user details

    @Autowired
    @Qualifier("handlerExceptionResolver")  // To handle any exceptions that occur
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Get the "Authorization" header from the request
            final String requestTokenHeader = request.getHeader("Authorization");

            // If the header is missing or doesn't start with "Bearer", continue without processing
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);  // Proceed to the next filter
                return;
            }

            // Get the actual token from the header
            String token = requestTokenHeader.split("Bearer ")[1];

            // Extract the user ID from the token
            Long userId = jwtService.getUserIdFromToken(token);

            // If the user ID is valid and the user is not already authenticated, set the user in the security context
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userDetailServiceImpl.getUserById(userId);

                // Create an authentication token for the user
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Add request details to the authentication token (e.g., IP address)
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set the user as authenticated in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            // Proceed with the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // If any exception occurs, resolve it and return an appropriate response
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
