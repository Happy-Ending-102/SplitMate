// File: src/main/java/repository/Repository.java
package repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import model.*;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}

@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    List<Friendship> findByUserAOrUserB(User userA, User userB);
}

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByMembersContaining(User member);
}

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByOwner(User owner);
    List<Expense> findByGroupId(String groupId);
}

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByFrom(User from);
    List<Payment> findByTo(User to);
}

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdAndReadFalse(String userId);
}
