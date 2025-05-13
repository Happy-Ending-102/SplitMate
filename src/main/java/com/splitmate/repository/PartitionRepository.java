package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.splitmate.model.Notification;
import com.splitmate.model.Partition;
import com.splitmate.model.User;
import com.splitmate.model.Expense;
import com.splitmate.model.Payment;
import com.splitmate.model.Currency;

@Repository
public interface PartitionRepository extends MongoRepository<Partition, String> {
}

