// File: ExpenseServiceImpl.java
package com.splitmate.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.splitmate.repository.ExpenseRepository;
import com.splitmate.repository.GroupRepository;
import com.splitmate.model.Expense;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expRepo;
    private final GroupRepository groupRepo;
    private final CurrencyConverter converter;

    public ExpenseServiceImpl(ExpenseRepository expRepo,
                              GroupRepository groupRepo,
                              CurrencyConverter converter) {
        this.expRepo = expRepo;
        this.groupRepo = groupRepo;
        this.converter = converter;
    }

    @Override
    public Expense addExpense(String groupId, Expense e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Expense> listExpenses(String groupId) {
        throw new UnsupportedOperationException();
    }
}
