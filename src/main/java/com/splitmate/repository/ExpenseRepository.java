// File: ExpenseRepository.java
package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.splitmate.model.Expense;
import com.splitmate.model.User;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByOwner(User owner);
    List<Expense> findByGroup_Id(String groupId);
}
