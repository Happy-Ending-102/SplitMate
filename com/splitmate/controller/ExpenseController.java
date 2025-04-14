package com.splitmate.controller;

import com.splitmate.model.Expense;
import com.splitmate.repository.MongoExpenseRepository;

public class ExpenseController {
    private MongoExpenseRepository expenseRepo;
    
    public ExpenseController() {
        expenseRepo = new MongoExpenseRepository();
    }
    
    public boolean addExpense(Expense expense) {
        return expenseRepo.insert(expense);
    }
    
    public boolean updateExpense(Expense expense) {
        return expenseRepo.update(expense);
    }
    
    public boolean deleteExpense(Expense expense) {
        return expenseRepo.delete(expense);
    }
}
