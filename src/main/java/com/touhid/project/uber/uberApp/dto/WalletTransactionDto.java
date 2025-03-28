package com.touhid.project.uber.uberApp.dto;

import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.enums.TransactionStatus;
import com.touhid.project.uber.uberApp.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransactionDto {
    private Long transactionId;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private Double amount;
    private LocalDateTime transactionTime;
    private Long walletId; // Only storing walletId instead of full Wallet entity
    private Long rideId;   // Only storing rideId instead of full Ride entity
    private String description;
}
