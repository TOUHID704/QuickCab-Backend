package com.touhid.project.uber.uberApp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletDto {

    private Long walletId;
    private Long userId;
    private Double balance;
    private LocalDateTime lastUpdated;


}
