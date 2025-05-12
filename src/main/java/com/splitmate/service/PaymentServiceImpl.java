package com.splitmate.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.splitmate.model.Currency;
import com.splitmate.model.Transaction;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;

import com.splitmate.repository.DebtRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.repository.TransactionRepository;
import com.splitmate.repository.NotificationRepository;

import com.splitmate.service.*;
import com.splitmate.repository.*;
import com.splitmate.model.User;
import com.splitmate.model.Group;
import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.Debt;
import com.splitmate.model.Transaction;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.model.Currency;
import com.splitmate.model.FilterType;
import com.splitmate.model.Payment;


@Service
public class PaymentServiceImpl implements PaymentService {
    private final CurrencyConverter converter;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired private DebtRepository debtRepo;
    @Autowired private UserService userService;

    @Autowired private UserRepository userRepo;
    @Autowired private FriendshipRepository friendshipRepo;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private NotificationRepository notificationRepository;


    public PaymentServiceImpl(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override
    public void initializeTransaction(String payerId, String receiverId, BigDecimal amount, Currency currency) {
        // Implementation of transaction initialization
        // This is where you would handle the logic for initializing a payment transaction
        Transaction transaction = new Transaction();
        User to = userService.getUser(receiverId);
        User from = userService.getUser(payerId);
        transaction.setTo(to);
        transaction.setFrom(from);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setPaymentDate(LocalDateTime.now());
        transaction.setPaid(false);

        transactionRepository.save(transaction);

        Notification notification = new Notification();
        notification.setUser(to);
        notification.setTransaction(transaction);
        notification.setRead(false);
        notification.setMessage("You have received a payment of " + amount + " " + currency + " from " + from.getName());
        notification.setType(NotificationType.TRANSACTION_RECEIVED);
        notification.setCreatedAt(LocalDateTime.now());
        // Save the transaction and notification to the database
        notificationRepository.save(notification);
    }
}
