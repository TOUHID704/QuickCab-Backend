package com.touhid.project.uber.uberApp.strategiesImpl;

import com.touhid.project.uber.uberApp.strategies.DriverMatchingStrategy;
import com.touhid.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Service to manage and select strategies for driver matching and fare calculation.
 */
@Service
@RequiredArgsConstructor
public class RideStrategyManager {

    // Injected dependencies for driver matching strategies
    private final HighestRatedDriverMatchingStrategy highestRatedDriverMatchingStrategy;
    private final NearestDriverMatchingStrategy nearestDriverMatchingStrategy;

    // Injected dependencies for fare calculation strategies
    private final DefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final SurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    /**
     * Selects the appropriate driver matching strategy based on the rider's rating.
     *
     * @param riderRating The rating of the rider, which determines the driver matching strategy.
     * @return The selected strategy for matching a driver to the rider.
     */
    public DriverMatchingStrategy selectDriverMatchingStrategy(double riderRating) {
        // Use the highest rated driver strategy if the rider's rating is 4.8 or higher.
        if (riderRating >= 4.8) {
            return highestRatedDriverMatchingStrategy;
        }
        // Otherwise, use the nearest driver strategy.
        return nearestDriverMatchingStrategy;
    }

    /**
     * Selects the appropriate fare calculation strategy based on the time of day.
     *
     * @return The selected fare calculation strategy (default or surge pricing).
     */
    public RideFareCalculationStrategy selectFareCalculationStrategy() {
        // Define the surge pricing time window (6:00 PM to 9:00 PM).
        LocalTime surgeStartTime = LocalTime.of(18, 0); // Start of surge time
        LocalTime surgeEndTime = LocalTime.of(21, 0);   // End of surge time
        LocalTime currentTime = LocalTime.now();         // Get the current system time

        // Determine if the current time is within the surge pricing window.
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        // Return the appropriate fare calculation strategy based on the time of day.
        if (isSurgeTime) {
            return surgePricingFareCalculationStrategy; // Use surge pricing during surge hours
        }
        return defaultFareCalculationStrategy; // Use default pricing at other times
    }
}
