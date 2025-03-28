package com.touhid.project.uber.uberApp.dto;

import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for handling ride request data.
 * Used to transfer ride request information between different layers of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    /**
     * Unique identifier for the ride request.
     */
    private Long rideRequestId;

    /**
     * Geographic point representing the pickup location of the ride request.
     * Stored using the WGS 84 coordinate system.
     */
    private PointDto pickupLocation;

    /**
     * Geographic point representing the drop-off location of the ride request.
     * Stored using the WGS 84 coordinate system.
     */
    private PointDto dropOffLocation;

    /**
     * Timestamp indicating when the ride request was created.
     * Typically populated automatically by Hibernate using @CreationTimestamp.
     */
    private LocalDateTime rideRequestedTime;

    /**
     * The rider who made the ride request.
     * This is associated with the Rider entity and represents a many-to-one relationship.
     */
    private RiderDto rider;

    /**
     * The payment method chosen for the ride (e.g., CASH, ).
     * This is represented by an enum, ensuring consistency in the accepted values.
     */
    private PaymentMethod paymentMethod;

    /**
     * The current status of the ride request (e.g., PENDING, ACCEPTED, COMPLETED).
     * Represented by an enum to enforce valid statuses.
     */

    private Double fare;
    private RideRequestStatus rideRequestStatus;
}
