// File: PaymentCalculator.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Payment;
import com.splitmate.model.User;
import com.splitmate.model.Friendship;

public interface PaymentCalculator {
    public List<Payment> calculate();
}
