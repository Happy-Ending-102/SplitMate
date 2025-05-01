// File: PaymentCalculator.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Payment;
import com.splitmate.model.Group;
import com.splitmate.model.Friendship;

public interface PaymentCalculator {
    List<Payment> calculate(Group group, List<Friendship> friendships);
}
