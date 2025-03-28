package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import com.touhid.project.uber.uberApp.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentUpdateService {

    private final PaymentRepository paymentRepository;

    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
