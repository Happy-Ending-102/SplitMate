// File: GroupRepository.java
package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.splitmate.model.Group;
import com.splitmate.model.User;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByMembersContaining(User member);
}
