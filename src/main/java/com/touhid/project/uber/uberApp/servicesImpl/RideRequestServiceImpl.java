package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.RideRequestDto;
import com.touhid.project.uber.uberApp.entities.RideRequest;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.RideRequestStatus;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.RideRequestRepository;
import com.touhid.project.uber.uberApp.repositories.RiderRepository;
import com.touhid.project.uber.uberApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    private final ModelMapper modelMapper;

    @Override
    public RideRequestDto findRideRequestById(Long rideRequestId) {
        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with the id " + rideRequestId));
        return modelMapper.map(rideRequest, RideRequestDto.class);
    }

    @Override
    public Page<RideRequestDto> getAllRidesForRider(Long riderId, Pageable pageable) {
        Page<RideRequest> rideRequests = rideRequestRepository.findByRiderRiderId(riderId, pageable);

        // Convert the Page<RideRequest> to Page<RideRequestDto>
        List<RideRequestDto> rideRequestDtos = rideRequests.getContent().stream()
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDto.class))
                .collect(Collectors.toList());

        // Return as a Page<RideRequestDto>
        return new PageImpl<>(rideRequestDtos, pageable, rideRequests.getTotalElements());
    }

    @Override
    public Page<RideRequestDto> getRidesByStatus(Long riderId, RideRequestStatus rideRequestStatus, Pageable pageable) {
        Page<RideRequest> rideRequests = rideRequestRepository.findByRiderRiderIdAndRideRequestStatus(riderId, rideRequestStatus, pageable);

        List<RideRequestDto> rideRequestDtos = rideRequests.getContent().stream()
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(rideRequestDtos, pageable, rideRequests.getTotalElements());
    }

    @Override
    public Page<RideRequestDto> getRidesByStatuses(Long riderId, List<RideRequestStatus> rideRequestStatuses, Pageable pageable) {

        Page<RideRequest> rideRequests = rideRequestRepository.findByRiderRiderIdAndRideRequestStatusIn(riderId, rideRequestStatuses, pageable);

        List<RideRequestDto> rideRequestDtos = rideRequests.getContent().stream()
                .map(rideRequest -> modelMapper.map(rideRequest, RideRequestDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(rideRequestDtos, pageable, rideRequests.getTotalElements());
    }


}
