// ExpenseController.java
package com.splitmate.controller;

import java.util.List;
import org.springframework.stereotype.Component;
import com.splitmate.model.*;
import com.splitmate.service.*;

/**
 * Handles expense-related UI actions.
 */
@Component
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public Expense addExpense(String groupId, Expense dto) {
        throw new UnsupportedOperationException();
    }

    public List<Expense> listExpenses(String groupId) {
        throw new UnsupportedOperationException();
    }
}