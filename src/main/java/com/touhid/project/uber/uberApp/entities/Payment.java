package com.touhid.project.uber.uberApp.entities;

import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Ride ride; // Assuming a ride is associated with the payment

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Wallet wallet;

    private String description; // Optional for additional context
}
