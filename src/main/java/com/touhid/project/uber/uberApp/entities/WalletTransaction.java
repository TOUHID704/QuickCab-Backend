package com.touhid.project.uber.uberApp.entities;

import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.enums.TransactionStatus;
import com.touhid.project.uber.uberApp.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_wallet_transactions",columnList = "wallet_id"),
        @Index(name = "idx_wallet_transactions_ride",columnList = "ride_id")
})
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    private Double amount;

    private LocalDateTime transactionTime;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "walletId") // Define the foreign key
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "ride_id",referencedColumnName = "rideId")
    private Ride ride;

    private String description;
}
