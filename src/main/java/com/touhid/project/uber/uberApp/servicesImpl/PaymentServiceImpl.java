package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.PaymentDto;
import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.PaymentRepository;
import com.touhid.project.uber.uberApp.services.PaymentService;
import com.touhid.project.uber.uberApp.strategies.PaymentStrategy;
import com.touhid.project.uber.uberApp.strategiesImpl.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentTime(LocalDateTime.now())
                .wallet(ride.getRider().getUser().getWallet())
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    @Override
    public PaymentDto processPayment(Ride ride) {
        // Validate that the ride exists and has a valid payment method
        if (ride == null || ride.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Invalid ride or payment method.");
        }

        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride: " + ride));

        // Select the appropriate payment strategy
        PaymentStrategy paymentStrategy = paymentStrategyManager.selectPaymentStrategy(payment.getPaymentMethod());
        if (paymentStrategy == null) {
            throw new UnsupportedOperationException("Payment method not supported: " + payment.getPaymentMethod());
        }

        // Process the payment using the selected strategy
        PaymentDto processedPayment = paymentStrategy.processPayment(payment);

        return processedPayment;
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        return modelMapper.map(payment, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        return null; // To be implemented
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
