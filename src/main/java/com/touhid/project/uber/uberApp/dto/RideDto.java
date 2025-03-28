package com.touhid.project.uber.uberApp.dto;


import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.enums.RideStatus;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
public class RideDto {

   private Long rideId;
   private PointDto pickupLocation;
   private PointDto dropOffLocation;
   private LocalDateTime createdTime;
   private RiderDto rider;
   private DriverDto driver;
   private PaymentMethod paymentMethod;
   private RideStatus rideStatus;
   private String otp;
   private Double fare;
   private LocalDateTime startedAt;
   private LocalDateTime endedAt;

}
