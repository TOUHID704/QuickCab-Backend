package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * This class implements the DistanceService interface and uses RestClient to interact with the OSRM API.
 */
@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String OSRM_API_BASE_URL = "http://router.project-osrm.org/route/v1/driving/";

    // Injecting RestClient from the configuration class

    @Autowired
    private RestClient restClient;  // This will use the RestClient configured in RestClientConfig.

    /**
     * This method calculates the driving distance between two points using the OSRM API.
     *
     * @param src The source location as a Point (latitude, longitude).
     * @param dest The destination location as a Point (latitude, longitude).
     * @return The driving distance between the source and destination in kilometers.
     */
    @Override
    public double calculateDistance(Point src, Point dest) {

        try {
            // Construct the URI by combining the coordinates of source and destination
            String uri = src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY();

            // Use the RestClient to make a GET request to the OSRM API
            OSRMResponseDto responseDto = restClient.get()  // Using GET method of RestClient
                    .uri(OSRM_API_BASE_URL + uri)  // Appending the URI with source and destination coordinates
                    .retrieve()  // Retrieve the response from the API
                    .body(OSRMResponseDto.class);  // Parse the response into OSRMResponseDto object

            // Return the distance in kilometers (OSRM provides the distance in meters)
            return responseDto.getRoutes().get(0).getDistance() / 1000.0;

        } catch (Exception e) {
            // Handle errors, e.g., network issues or invalid API responses
            throw new RuntimeException("Error getting data from OSRM: " + e.getMessage());
        }
    }
}

/**
 * DTO class representing the OSRM API response.
 * It contains a list of routes, each with a distance.
 */
@Data
class OSRMResponseDto {
    private List<OSRMRoute> routes;
}

/**
 * DTO class for a single route in the OSRM API response.
 * It contains the distance of the route in meters.
 */
@Data
class OSRMRoute {
    private Double distance;
}
