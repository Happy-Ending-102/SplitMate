// User operations
public interface UserService {
    User getUser(String id);
    void registerUser(User u);
    void updateUser(User u);
    void removeUser(String id);
}

// Group management
public interface GroupService {
    Group getGroup(String id);
    void createGroup(Group g);
    void addUserToGroup(String groupId, String userId);
    void removeUserFromGroup(String groupId, String userId);
}

// Expense handling
public interface ExpenseService {
    void addExpense(String groupId, Expense e);
    List<Expense> listExpenses(String groupId);
}

// Payment calculation strategy
public interface PaymentCalculator {
    List<Payment> calculate(List<User> users, List<Friendship> friendships);
}

// --- Implementations ---

public class UserServiceImpl implements UserService {
    private UserRepository userRepo;
    public UserServiceImpl(UserRepository repo);
    @Override public User getUser(String id);
    @Override public void registerUser(User u);
    @Override public void updateUser(User u);
    @Override public void removeUser(String id);
}

public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepo;
    private UserRepository userRepo;
    public GroupServiceImpl(GroupRepository gRepo, UserRepository uRepo);
    @Override public Group getGroup(String id);
    @Override public void createGroup(Group g);
    @Override public void addUserToGroup(String groupId, String userId);
    @Override public void removeUserFromGroup(String groupId, String userId);
}

public class ExpenseServiceImpl implements ExpenseService {
    private ExpenseRepository expRepo;
    private GroupRepository groupRepo;
    public ExpenseServiceImpl(ExpenseRepository eRepo, GroupRepository gRepo);
    @Override public void addExpense(String groupId, Expense e);
    @Override public List<Expense> listExpenses(String groupId);
}

// Simple payment calculator using balances and friendships
public class SimplePaymentCalculator implements PaymentCalculator {
    @Override
    public List<Payment> calculate(List<User> users, List<Friendship> friendships);
}
