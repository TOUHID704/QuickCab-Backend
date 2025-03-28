package com.touhid.project.uber.uberApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the Data Transfer Object (DTO) for user sign-up.
 * It is used to collect information required for creating a new user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {


    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    private String password;
}
