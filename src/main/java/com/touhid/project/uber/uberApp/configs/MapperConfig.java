package com.touhid.project.uber.uberApp.configs;

import com.touhid.project.uber.uberApp.dto.PointDto;
import com.touhid.project.uber.uberApp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up mappings between different data transfer objects (DTOs) and entities.
 *
 * <p>This class uses the ModelMapper library to convert objects between the following types:</p>
 * - `PointDto` (DTO representation of a geographical point) to `Point` (JTS geometry object).
 * - `Point` (JTS geometry object) to `PointDto` (DTO representation of a geographical point).
 */
@Configuration
public class MapperConfig {

    /**
     * Creates and configures a `ModelMapper` bean.
     *
     * <p>The `ModelMapper` is used to automate the conversion between DTOs and entities,
     * simplifying object transformations in the application.</p>
     *
     * @return A configured instance of `ModelMapper`.
     */
    @Bean
    public ModelMapper modelMapper() {
        // Instantiate a new ModelMapper object.
        ModelMapper modelMapper = new ModelMapper();

        // Define a mapping from PointDto to Point using a custom converter.
        modelMapper.typeMap(PointDto.class, Point.class).setConverter(context -> {
            // Retrieve the source object (PointDto) from the mapping context.
            PointDto pointDto = context.getSource();

            // Convert PointDto to a JTS Point object using the GeometryUtil class.
            return GeometryUtil.createPoint(pointDto);
        });

        // Define a mapping from Point to PointDto using a custom converter.
        modelMapper.typeMap(Point.class, PointDto.class).setConverter(context -> {
            // Retrieve the source object (Point) from the mapping context.
            Point point = context.getSource();

            // Extract the coordinates (X and Y values) from the Point object.
            double[] coordinates = {
                    point.getX(), // X represents the longitude.
                    point.getY()  // Y represents the latitude.
            };

            // Create a new PointDto object and populate it with extracted values.
            PointDto pointDto = new PointDto();
            pointDto.setCoordinates(coordinates);
            return pointDto;
        });

        // Return the fully configured ModelMapper instance.
        return modelMapper;
    }
}
