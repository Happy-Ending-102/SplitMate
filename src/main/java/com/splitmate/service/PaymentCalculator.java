// File: PaymentCalculator.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Payment;
import com.splitmate.model.User;
import com.splitmate.model.Friendship;

public interface PaymentCalculator {
    List<Payment> calculate(List<User> users, List<Friendship> friendships);
}
