package com.splitmate.controller;

import com.splitmate.model.Transaction;
import com.splitmate.repository.MongoTransactionRepository;

public class PaymentController {
    private MongoTransactionRepository transactionRepo;
    
    public PaymentController() {
        transactionRepo = new MongoTransactionRepository();
    }
    
    public boolean recordPayment(Transaction transaction) {
        return transactionRepo.insert(transaction);
    }
    
    public boolean updatePayment(Transaction transaction) {
        return transactionRepo.update(transaction);
    }
    
    public boolean deletePayment(Transaction transaction) {
        return transactionRepo.delete(transaction);
    }
}
