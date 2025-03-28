package com.touhid.project.uber.uberApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId; // Unique identifier for the rider.

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the `User` entity.
    private User user; // Associated user entity.


    @JsonIgnore  // This will prevent infinite recursion
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideRequest> rideRequests;

    private Double rating; // Rating of the rider.
}
