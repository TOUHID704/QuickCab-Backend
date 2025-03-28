package com.touhid.project.uber.uberApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideRequestId; // Unique identifier for the ride request.

    @Column(columnDefinition = "Geometry(Point,4326)", nullable = false)
    private Point pickupLocation; // Pickup location (geographic point).

    @Column(columnDefinition = "Geometry(Point,4326)", nullable = false)
    private Point dropOffLocation; // Drop-off location (geographic point).

    @CreationTimestamp
    private LocalDateTime rideRequestedTime; // Timestamp of when the ride was requested.

    @ManyToOne
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // Enum for payment methods (CASH, CARD, etc.).

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus; // Enum for ride request status (PENDING, ACCEPTED, etc.).

    private double fare; // Fare for the ride.

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id")
    private Ride ride;


}
