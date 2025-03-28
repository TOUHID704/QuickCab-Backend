package com.touhid.project.uber.uberApp.dto;

import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {

    private Long paymentId;
    private Long userId;
    private Long rideId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

}
