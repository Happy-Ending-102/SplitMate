// File: PaymentRepository.java
package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.splitmate.model.Payment;
import com.splitmate.model.User;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByFrom(User from);
    List<Payment> findByTo(User to);
}
