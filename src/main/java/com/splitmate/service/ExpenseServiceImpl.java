// File: ExpenseServiceImpl.java
package com.splitmate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.splitmate.repository.ExpenseRepository;
import com.splitmate.repository.GroupRepository;
import com.splitmate.model.ConversionPolicy;
import com.splitmate.model.Currency;
import com.splitmate.model.Expense;
import com.splitmate.model.Partition;
import com.splitmate.model.User;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expRepo;
    private final GroupRepository groupRepo;
    private final CurrencyConverter converter;
    @Autowired private PaymentCalculator paymentCalculator;
    @Autowired private UserService userService;

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
                BigDecimal.valueOf(1), 
                e.getCurrency(), 
                group.getDefaultCurrency()
            );

            e.setAmount(converted*e.getAmount());
            e.setCurrency(group.getDefaultCurrency());

            for(Partition partition : e.getDivisionAmongUsers()) {
                User user = partition.getUser();
                partition.setAmount(converted.multiply(BigDecimal.valueOf(partition.getAmount())));
            }
        }
        // TODO handle budget. add balance to group

        e.setGroup(group);
        expRepo.save(e);
        group.addExpense(e);
        groupRepo.save(group);
        
        // Update the balances of the users involved in the expense
        for(Partition partition : e.getDivisionAmongUsers()) {
            User user = partition.getUser();
            BigDecimal amount = BigDecimal.valueOf( partition.getAmount() );
            Currency currency = e.getCurrency();
            user.getBalanceByCurrency(currency).addAmount(amount.negate());
            userService.updateUser(user);
        }
        // update the owner's balance
        User owner = e.getOwner();
        owner.getBalanceByCurrency(e.getCurrency()).addAmount(e.getAmount());
        userService.updateUser(owner);
        
        // run the main algorithm
        paymentCalculator.calculate();

        return e;
    }

    @Override
    public List<Expense> listExpenses(String groupId) {
        return expRepo.findByGroup_Id(groupId);
    }
}
