// File: ExpenseServiceImpl.java
package com.splitmate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import com.splitmate.repository.ExpenseRepository;
import com.splitmate.repository.GroupRepository;
import com.splitmate.model.ConversionPolicy;
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
        var group = groupRepo.findById(groupId).orElseThrow(() -> new NoSuchElementException("Group not found"));

        if (group.getConversionPolicy() == ConversionPolicy.INSTANT &&
            e.getCurrency() != group.getDefaultCurrency()) {

            BigDecimal converted = converter.convert(
                e.getAmount(), 
                e.getCurrency(), 
                group.getDefaultCurrency()
            );

            e.setAmount(converted);
            e.setCurrency(group.getDefaultCurrency());
        }

        e.setGroup(group);
        expRepo.save(e);
        group.addExpense(e);
        groupRepo.save(group);

        return e;
    }

    @Override
    public List<Expense> listExpenses(String groupId) {
        return expRepo.findByGroup_Id(groupId);
    }
}
