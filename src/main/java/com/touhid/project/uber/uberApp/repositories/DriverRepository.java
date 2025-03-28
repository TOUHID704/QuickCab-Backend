package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides methods to interact with the "drivers" table in the database.
 *
 * - It extends JpaRepository, so it inherits basic CRUD methods like save, delete, and find.
 * - It also contains a custom query to find nearby drivers based on the pickup location.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * Finds up to 10 nearby drivers who are available for a ride, sorted by distance.
     *
     * **How it works:**
     * 1. The query calculates the distance between the driver's current location and the pickup location.
     * 2. It filters drivers who are marked as "available."
     * 3. It only considers drivers within a 10,000-meter (10 km) radius.
     * 4. Finally, it returns the 10 closest drivers, ordered by their distance from the pickup location.
     *
     * @param pickupLocation The pickup point provided by the user.
     *                       It is a geographic point (latitude and longitude).
     * @return A list of up to 10 nearby available drivers.
     */
    @Query(
            value = """
            SELECT d.*, 
                   ST_Distance(d.current_location, :pickupLocation) AS distance
            FROM driver AS d
            WHERE d.available = true 
              AND ST_DWithin(d.current_location, :pickupLocation, 10000)
            ORDER BY distance
            LIMIT 10
            """,
            nativeQuery = true // This specifies that the query is written in plain SQL.
    )
    List<Driver> findTenNearestDrivers(Point pickupLocation);


    /**
     * Finds up to 10 nearby top-rated drivers within a 15,000-meter (15 km) radius.
     *
     * **How it works:**
     * 1. The query filters drivers who are available.
     * 2. It calculates the distance between the driver's current location and the pickup location.
     * 3. It considers only drivers within a 15,000-meter (15 km) radius.
     * 4. The list of drivers is then sorted by rating in descending order.
     * 5. Finally, it returns the top 10 drivers based on their rating.
     *
     * @param pickupLocation The pickup point provided by the user.
     *                       It is a geographic point (latitude and longitude).
     * @return A list of the top 10 rated drivers near the pickup location.
     */
    @Query(value = """
            SELECT d.*,
            FROM driver d
            WHERE d.available = true
            AND ST_DWithin(d.current_location, :pickupLocation, 15000)
            ORDER BY d.rating DESC
            LIMIT 10 
            """,
            nativeQuery = true // This specifies that the query is written in plain SQL.
    )
    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Optional<Driver> findByUser(User user);
}
