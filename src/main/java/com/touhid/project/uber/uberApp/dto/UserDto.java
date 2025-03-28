package com.touhid.project.uber.uberApp.dto;

import com.touhid.project.uber.uberApp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents the Data Transfer Object (DTO) for a user.
 * It holds the user's basic details and their roles.
 * The roles are represented by a set of Role enum values (e.g., DRIVER, RIDER).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String name;
    private String email;
    private Set<Role> roles;
}
