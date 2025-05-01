// File: SimplePaymentCalculator.java
package com.splitmate.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.splitmate.model.Payment;
import com.splitmate.model.Group;
import com.splitmate.model.Friendship;

@Service
public class SimplePaymentCalculator implements PaymentCalculator {
    private final CurrencyConverter converter;

    public SimplePaymentCalculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<Payment> calculate(Group group, List<Friendship> friendships) {
        throw new UnsupportedOperationException();
    }
}
