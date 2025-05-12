package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;   
import com.splitmate.model.Debt;      
import com.splitmate.model.Transaction;      

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
