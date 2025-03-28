package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.PaymentDto;
import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;

import java.util.List;


public interface PaymentService {

    PaymentDto createPayment(Ride ride);

    PaymentDto processPayment(Ride ride);

    PaymentDto getPaymentById(Long paymentId);

    List<PaymentDto> getPaymentsByUserId(Long userId);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);

    void deletePayment(Long paymentId);
}

