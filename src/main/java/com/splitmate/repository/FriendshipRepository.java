// File: FriendshipRepository.java
package com.splitmate.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.splitmate.model.Friendship;
import com.splitmate.model.User;

@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    List<Friendship> findByUserA_IdAndUserB_IdOrUserA_IdAndUserB_Id(
        String userAId, String userBId,
        String userBIdAlt, String userAIdAlt
    );


}
