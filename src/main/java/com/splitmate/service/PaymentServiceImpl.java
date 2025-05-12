package com.splitmate.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
import com.splitmate.model.Debt;


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
    @Autowired private PaymentCalculator paymentCalculator;

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

    @Override
    public void acceptTransaction(String transactionId) {
        // Implementation of accepting a transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setPaid(true);
        transactionRepository.save(transaction);

        // Update the user's balance
        User to = transaction.getTo();
        User from = transaction.getFrom();
        Currency currency = transaction.getCurrency();
        BigDecimal amount = transaction.getAmount();
        to.getBalanceByCurrency(currency).addAmount(amount);
        from.getBalanceByCurrency(currency).subtractAmount(amount);

        // Save the updated users
        userRepo.save(to);
        userRepo.save(from);

        //run the main algorithm
        paymentCalculator.calculate();
    }
    @Override
    public void rejectTransaction(String transactionId) {
        // Implementation of rejecting a transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionRepository.save(transaction);
        // Optionally, you can also notify the user about the rejection
        Notification notification = new Notification();
        notification.setUser(transaction.getFrom());
        notification.setTransaction(transaction);
        notification.setRead(false);
        notification.setMessage("Your payment of " + transaction.getAmount() + " " + transaction.getCurrency() + " to " + transaction.getTo().getName() + " has been rejected.");
        notification.setType(NotificationType.TRANSACTION_REJECTED);
        notification.setCreatedAt(LocalDateTime.now());
        // Save the notification to the database
        notificationRepository.save(notification);
    } 

    @Override
    public List<Debt> getUserDebts(String userId) {
        // Implementation of getting user debts
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getDebts();
    }
}
