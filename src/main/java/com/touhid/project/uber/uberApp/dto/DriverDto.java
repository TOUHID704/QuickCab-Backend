package com.touhid.project.uber.uberApp.dto;

import com.touhid.project.uber.uberApp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

    private Long driverId;
    private Boolean available;
    private User user;
    private Double rating;
    private String vehicleId;


}
