// File: ExpenseRepository.java
package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;   
import com.splitmate.model.Debt;      

@Repository
public interface DebtRepository extends MongoRepository<Debt, String> {
    List<Debt> findByUserId(String userId);
    List<Debt> findByGroupId(String groupId);
    List<Debt> findByUserIdAndGroupId(String userId, String groupId);
}
