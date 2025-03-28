package com.touhid.project.uber.uberApp.services;

import com.touhid.project.uber.uberApp.dto.WalletTransactionDto;
import com.touhid.project.uber.uberApp.entities.WalletTransaction;
import com.touhid.project.uber.uberApp.enums.TransactionStatus;

import java.util.List;

public interface WalletTransactionService {

    WalletTransactionDto createNewWalletTransaction(WalletTransaction walletTransaction);

    WalletTransactionDto getTransactionById(Long transactionId);

    List<WalletTransactionDto> getTransactionsByUserId(Long userId);

    List<WalletTransactionDto> getTransactionsByStatus(TransactionStatus transactionStatus);

    void updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);

    void deleteTransaction(Long transactionId);


}
