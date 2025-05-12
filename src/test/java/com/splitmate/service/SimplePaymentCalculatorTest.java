// File: src/test/java/com/splitmate/service/SimplePaymentCalculatorTest.java
package com.splitmate.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import com.splitmate.model.User;
import com.splitmate.repository.*;
import com.splitmate.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

class SimplePaymentCalculatorTest {

    @Mock private CurrencyConverter converter;
    @Mock private DebtRepository     debtRepo;
    @Mock private UserService        userService;
    @Mock private UserRepository     userRepo;
    @Mock private FriendshipRepository friendshipRepo;

    @InjectMocks private SimplePaymentCalculator calculator;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void callCalculate_only() {
        when(userRepo.findAll()).thenReturn(List.of());
        when(friendshipRepo.findAll()).thenReturn(List.of());

        calculator.calculate();
    }
}