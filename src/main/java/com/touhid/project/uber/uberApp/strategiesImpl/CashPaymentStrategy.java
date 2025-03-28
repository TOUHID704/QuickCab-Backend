package com.touhid.project.uber.uberApp.strategiesImpl;

import com.touhid.project.uber.uberApp.dto.PaymentDto;
import com.touhid.project.uber.uberApp.entities.Driver;
import com.touhid.project.uber.uberApp.entities.Payment;
import com.touhid.project.uber.uberApp.enums.PaymentStatus;
import com.touhid.project.uber.uberApp.enums.TransactionMethod;
import com.touhid.project.uber.uberApp.services.PaymentService;
import com.touhid.project.uber.uberApp.services.WalletService;
import com.touhid.project.uber.uberApp.servicesImpl.PaymentUpdateService;
import com.touhid.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/*
suppose if a ride cost Rs 100
and rider has made the payment via cash 100 rupees to driver.
then the whatever commission needs to be deducted will be deducted from the
 */


@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final ModelMapper modelMapper;
    private final PaymentUpdateService paymentUpdateService;
    @Override
    public PaymentDto processPayment(Payment payment) {

        Driver driver = payment.getRide().getDriver();
        double platformCommission = payment.getAmount()*PLATFORM_COMMISSION;
      walletService.deductFundsFromWallet(driver.getUser(),platformCommission,payment.getRide(),null,TransactionMethod.RIDE);


     paymentUpdateService.updatePaymentStatus(payment, PaymentStatus.COMPLETED);

      return modelMapper.map(payment,PaymentDto.class);


    }
}
