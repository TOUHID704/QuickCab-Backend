package com.touhid.project.uber.uberApp.strategies;

import com.touhid.project.uber.uberApp.dto.PaymentDto;
import com.touhid.project.uber.uberApp.entities.Payment;

public interface PaymentStrategy {

    static final Double PLATFORM_COMMISSION = 0.3;
    PaymentDto processPayment(Payment payment);

}
