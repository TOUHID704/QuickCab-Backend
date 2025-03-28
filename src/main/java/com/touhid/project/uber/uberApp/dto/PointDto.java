package com.touhid.project.uber.uberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for representing a geographic point in GeoJSON format.
 * This is used to standardize location data, such as pickup and drop-off points.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {

    /**
     * Array of coordinates representing the geographic point.
     * - Format: [longitude, latitude].
     * - Example: [77.5946, 12.9716] for a location in Bangalore, India.
     */
    private double[] coordinates;

    /**
     * Type of geometry, fixed as "Point" to represent a single geographic point.
     * - This follows the GeoJSON standard for geographic data.
     */
    private String type = "Point";

}
