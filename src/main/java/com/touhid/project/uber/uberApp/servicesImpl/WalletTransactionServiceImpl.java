package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.WalletTransactionDto;
import com.touhid.project.uber.uberApp.entities.WalletTransaction;
import com.touhid.project.uber.uberApp.enums.TransactionStatus;
import com.touhid.project.uber.uberApp.repositories.WalletTransactionRepository;
import com.touhid.project.uber.uberApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final ModelMapper modelMapper;
    private final WalletTransactionRepository walletTransactionRepository;


    @Override
    public WalletTransactionDto createNewWalletTransaction(WalletTransaction walletTransaction) {
        WalletTransactionDto walletTransactionDto = modelMapper.map(walletTransaction,WalletTransactionDto.class);
        walletTransactionRepository.save(walletTransaction);
        return walletTransactionDto;
    }

    @Override
    public WalletTransactionDto getTransactionById(Long transactionId) {
        return null;
    }

    @Override
    public List<WalletTransactionDto> getTransactionsByUserId(Long userId) {
        return null;
    }

    @Override
    public List<WalletTransactionDto> getTransactionsByStatus(TransactionStatus transactionStatus) {
        return null;
    }

    @Override
    public void updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {

    }

    @Override
    public void deleteTransaction(Long transactionId) {

    }
}
