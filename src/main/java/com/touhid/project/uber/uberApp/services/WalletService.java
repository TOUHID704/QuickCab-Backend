package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.WalletDto;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.entities.Wallet;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;

public interface WalletService {

    WalletDto createNewWallet(User user);

    WalletDto getWalletByUser(User user);

    WalletDto updateWallet(Long userId, Double amount);

    void deleteWallet(User user);

    WalletDto addFundsToWallet(User user, Double amount,Ride ride, Long  transactionId, TransactionMethod transactionMethod);

    WalletDto deductFundsFromWallet(User user, Double amount,Ride ride,Long  transactionId, TransactionMethod transactionMethod);

    Wallet findWalletByUser(User user);
}
