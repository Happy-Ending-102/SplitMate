package com.splitmate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.splitmate.model.FilterType;

import com.splitmate.model.Currency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.model.Partition;
import com.splitmate.model.Group;
import com.splitmate.model.Currency;

public interface PaymentService {
    void initializeTransaction(String payerId, String receiverId, BigDecimal amount, Currency currency); // it must be accepted
}
