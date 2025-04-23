public interface UserRepository {
    User findById(String id);
    List<User> findAll();
    void save(User user);
    void deleteById(String id);
}

public interface FriendshipRepository {
    Friendship findById(String id);
    List<Friendship> findByUser(User user);
    void save(Friendship f);
}

public interface GroupRepository {
    Group findById(String id);
    List<Group> findByMember(User user);
    void save(Group g);
}

public interface ExpenseRepository {
    Expense findById(String id);
    List<Expense> findByGroup(Group group);
    void save(Expense e);
}

public interface PaymentRepository {
    Payment findById(String id);
    List<Payment> findByUser(User user);
    void save(Payment p);
}
