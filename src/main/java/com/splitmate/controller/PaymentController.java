// PaymentController.java
package com.splitmate.controller;

import java.util.List;
import com.splitmate.model.*;
import com.splitmate.service.*;
import com.splitmate.repository.FriendshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles payment calculation actions.
 */
@Component
public class PaymentController {
    private final PaymentCalculator calculator;
    private final GroupService groupService;
    private final FriendshipRepository friendshipRepo;
    @Autowired private PaymentService paymentService;

    public PaymentController(PaymentCalculator calculator,
                             GroupService groupService,
                             FriendshipRepository friendshipRepo) {
        this.calculator = calculator;
        this.groupService = groupService;
        this.friendshipRepo = friendshipRepo;
    }

    public List<Payment> calculatePayments(String groupId) {
        throw new UnsupportedOperationException();
    }

    public void acceptTransaction(String paymentId) {
        // Logic to accept a payment
        paymentService.acceptTransaction(paymentId);
    }
}