// File: ExpenseService.java
package com.splitmate.service;

import java.util.List;
import com.splitmate.model.Expense;

public interface ExpenseService {
    Expense addExpense(String groupId, Expense e);
    List<Expense> listExpenses(String groupId);
}
