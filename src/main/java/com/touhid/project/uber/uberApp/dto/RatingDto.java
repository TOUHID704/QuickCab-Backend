package com.touhid.project.uber.uberApp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
   private Long ratingId;

   private Long rideId;

   private Long riderId;

   private Long driverId;

   @Min(1)
   @Max(5)
   private Integer driverRating;

   @Min(1)
   @Max(5)
   private Integer riderRating;
}