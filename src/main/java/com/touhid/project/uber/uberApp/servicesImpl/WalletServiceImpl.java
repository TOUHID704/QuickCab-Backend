package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.dto.WalletDto;
import com.touhid.project.uber.uberApp.entities.Ride;
import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.entities.Wallet;
import com.touhid.project.uber.uberApp.entities.WalletTransaction;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.enums.TransactionStatus;
import com.touhid.project.uber.uberApp.enums.TransactionType;
import com.touhid.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.touhid.project.uber.uberApp.repositories.WalletRepository;
import com.touhid.project.uber.uberApp.services.WalletService;
import com.touhid.project.uber.uberApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;
    private final ModelMapper modelMapper;

    @Override
    public WalletDto createNewWallet(User user) {
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(0.0) // Initial balance is 0
                .build();

        Wallet savedWallet = walletRepository.save(wallet);
        return modelMapper.map(savedWallet, WalletDto.class);
    }

    @Transactional
    @Override
    public WalletDto addFundsToWallet(User user, Double amount, Ride ride, Long transactionId, TransactionMethod transactionMethod) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with ID " + user.getUserId()));

        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionTime(LocalDateTime.now())
                .transactionId(transactionId)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(amount)
                .wallet(wallet)
                .description("Funds added to wallet")
                .build();

        wallet.getTransactions().add(walletTransaction);

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        Wallet updatedWallet = walletRepository.save(wallet);
        return modelMapper.map(updatedWallet, WalletDto.class);
    }

    @Override
    public WalletDto deductFundsFromWallet(User user, Double amount, Ride ride, Long transactionId, TransactionMethod transactionMethod) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with ID " + user.getUserId()));

        if (wallet.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in the wallet for user with ID " + user.getUserId());
        }

        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction transaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(amount)
                .wallet(wallet)
                .description("Funds deducted from wallet")
                .build();

        wallet.getTransactions().add(transaction);

        walletTransactionService.createNewWalletTransaction(transaction);

        Wallet updatedWallet = walletRepository.save(wallet);
        return modelMapper.map(updatedWallet, WalletDto.class);
    }

    @Override
    public Wallet findWalletByUser(User user) {
        Wallet wallet = walletRepository.findWalletByUser(user).orElseThrow(()-> new ResourceNotFoundException("No Wallet found with the user"+user));
        return wallet;
    }

    @Override
    public WalletDto getWalletByUser(User user) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with ID " + user.getUserId()));

        return modelMapper.map(wallet, WalletDto.class);
    }

    @Override
    public WalletDto updateWallet(Long walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID " + walletId));

        wallet.setBalance(wallet.getBalance() + amount);
        Wallet updatedWallet = walletRepository.save(wallet);
        return modelMapper.map(updatedWallet, WalletDto.class);
    }

    @Override
    public void deleteWallet(User user) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with ID " + user.getUserId()));

        walletRepository.delete(wallet);
    }
}
