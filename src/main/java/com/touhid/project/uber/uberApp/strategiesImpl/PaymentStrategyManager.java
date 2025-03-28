package com.touhid.project.uber.uberApp.strategiesImpl;

import com.touhid.project.uber.uberApp.enums.PaymentMethod;
import com.touhid.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy selectPaymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING -> throw new UnsupportedOperationException(
                    "Payment method currently not implemented: " + paymentMethod);
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }
}
