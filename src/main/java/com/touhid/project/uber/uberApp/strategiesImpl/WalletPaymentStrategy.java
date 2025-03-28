package com.touhid.project.uber.uberApp.strategiesImpl;

import com.touhid.project.uber.uberApp.dto.PaymentDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.entities.Rider;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.services.WalletService;
import com.touhid.project.uber.uberApp.servicesImpl.PaymentUpdateService;
import com.touhid.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final ModelMapper modelMapper;
    private final PaymentUpdateService paymentUpdateService;



    @Override
    @Transactional
    public PaymentDto processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductFundsFromWallet(rider.getUser(), payment.getAmount(), payment.getRide(), null, TransactionMethod.RIDE);

        double driverEarnings = payment.getAmount() * (1 - PLATFORM_COMMISSION);
        walletService.addFundsToWallet(driver.getUser(), driverEarnings, payment.getRide(), null, TransactionMethod.RIDE);

       paymentUpdateService.updatePaymentStatus(payment, PaymentStatus.COMPLETED);

        return modelMapper.map(payment, PaymentDto.class);
    }
}
