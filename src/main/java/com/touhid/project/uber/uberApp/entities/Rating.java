package com.touhid.project.uber.uberApp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @OneToOne(optional = false)
    private Ride ride; // Ensures every rating is tied to a ride.

    @ManyToOne(optional = false)
    private Rider rider; // Rider involved in the ride.

    @ManyToOne(optional = false)
    private Driver driver; // Driver involved in the ride.

    @Min(1)
    @Max(5)
    private Integer driverRating; // Rating given to the driver by the rider.

    @Min(1)
    @Max(5)
    private Integer riderRating; // Rating given to the rider by the driver.
}