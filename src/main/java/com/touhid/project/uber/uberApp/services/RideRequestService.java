package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.RideRequestDto;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RideRequestService {

    // Method to find a RideRequest by its ID



    RideRequestDto findRideRequestById(Long rideRequestId);

    Page<RideRequestDto> getAllRidesForRider(Long riderId, Pageable pageable);



    Page<RideRequestDto> getRidesByStatus(Long riderId, RideRequestStatus rideRequestStatus, Pageable pageable);

    Page<RideRequestDto> getRidesByStatuses(Long riderId, List<RideRequestStatus> rideRequestStatuses, Pageable pageable);

}
