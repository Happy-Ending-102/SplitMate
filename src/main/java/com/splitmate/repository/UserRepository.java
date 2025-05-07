// File: UserRepository.java
package com.splitmate.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.splitmate.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /** Used in login */
    User findByEmail(String email);

    /** Quick existence check before registering */
    boolean existsByEmail(String email);

    // Sadece id getiren özel sorgu:
    @Query(value="{ 'email': ?0 }", fields="{ '_id': 1 }")
    String findIdByEmail(String email);
}
