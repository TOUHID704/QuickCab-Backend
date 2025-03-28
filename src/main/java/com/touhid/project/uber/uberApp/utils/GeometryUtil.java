package com.touhid.project.uber.uberApp.utils;

import com.touhid.project.uber.uberApp.dto.PointDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Utility class for handling geometric operations.
 *
 * <p>This class provides helper methods for creating JTS geometry objects
 * (e.g., Point) from DTOs and vice versa.</p>
 */
public class GeometryUtil {

    /**
     * Converts a `PointDto` object to a JTS `Point` object.
     *
     * <p>This method uses a `GeometryFactory` to create a `Point` object based on
     * the provided latitude and longitude values in the `PointDto`.</p>
     *
     * @param pointDto The `PointDto` object containing geographic coordinates.
     *                 - `coordinates[0]`: X-coordinate (longitude).
     *                 - `coordinates[1]`: Y-coordinate (latitude).
     *
     * @return A JTS `Point` object created with the specified coordinates.
     */
    public static Point createPoint(PointDto pointDto) {
        // Step 1: Initialize a GeometryFactory.
        // The factory is responsible for creating geometric shapes like Point.
        // - PrecisionModel: Controls the precision of coordinates.
        // - SRID 4326: Indicates the WGS 84 coordinate system (used for GPS data).
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        // Step 2: Create a Coordinate object.
        // - X-coordinate: pointDto.getCoordinates()[0] (longitude).
        // - Y-coordinate: pointDto.getCoordinates()[1] (latitude).
        Coordinate coordinate = new Coordinate(
                pointDto.getCoordinates()[0],
                pointDto.getCoordinates()[1]
        );

        // Step 3: Use the GeometryFactory to create a Point with the specified Coordinate.
        return geometryFactory.createPoint(coordinate);
    }
}
