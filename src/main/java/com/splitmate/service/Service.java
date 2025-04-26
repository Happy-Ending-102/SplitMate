// File: src/main/java/service/Service.java
package service;

import java.math.BigDecimal;
import java.util.List;
import model.*;

/**
 * Converts between currencies.
 */
public interface CurrencyConverter {
    BigDecimal convert(BigDecimal amount, Currency from, Currency to);
}

/**
 * Implementation stub for currency conversion.
 */
@Service
public class ApiCurrencyConverter implements CurrencyConverter {
    private final String apiKey;

    public ApiCurrencyConverter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

/**
 * User-related operations, including registration and authentication.
 */
public interface UserService {
    User getUser(String id);
    User registerUser(User u, String rawPassword);
    boolean authenticate(String email, String rawPassword);
    User updateUser(User u);
    void removeUser(String id);
}

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final CurrencyConverter converter;  // example of injecting another service

    public UserServiceImpl(UserRepository userRepo,
                           CurrencyConverter converter) {
        this.userRepo = userRepo;
        this.converter = converter;
    }

    @Override public User getUser(String id) { throw new UnsupportedOperationException(); }
    @Override public User registerUser(User u, String rawPassword) { throw new UnsupportedOperationException(); }
    @Override public boolean authenticate(String email, String rawPassword) { throw new UnsupportedOperationException(); }
    @Override public User updateUser(User u) { throw new UnsupportedOperationException(); }
    @Override public void removeUser(String id) { throw new UnsupportedOperationException(); }
}

/**
 * Group management operations.
 */
public interface GroupService {
    Group getGroup(String id);
    Group createGroup(Group g);
    Group addUserToGroup(String groupId, String userId);
    Group removeUserFromGroup(String groupId, String userId);
    Group frozeUserInAGroup(String groupId, String userId);
}

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepo;
    private final UserRepository userRepo;

    public GroupServiceImpl(GroupRepository groupRepo, UserRepository userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    @Override public Group getGroup(String id) { throw new UnsupportedOperationException(); }
    @Override public Group createGroup(Group g) { throw new UnsupportedOperationException(); }
    @Override public Group addUserToGroup(String groupId, String userId) { throw new UnsupportedOperationException(); }
    @Override public Group removeUserFromGroup(String groupId, String userId) { throw new UnsupportedOperationException(); }
    @Override public Group frozeUserInAGroup(String groupId, String userId) { throw new UnsupportedOperationException(); }
}

/**
 * Expense handling operations.
 */
public interface ExpenseService {
    Expense addExpense(String groupId, Expense e);
    List<Expense> listExpenses(String groupId);
}

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expRepo;
    private final GroupRepository groupRepo;
    private final CurrencyConverter converter;

    public ExpenseServiceImpl(ExpenseRepository expRepo,
                              GroupRepository groupRepo,
                              CurrencyConverter converter) {
        this.expRepo = expRepo;
        this.groupRepo = groupRepo;
        this.converter = converter;
    }

    @Override public Expense addExpense(String groupId, Expense e) { throw new UnsupportedOperationException(); }
    @Override public List<Expense> listExpenses(String groupId) { throw new UnsupportedOperationException(); }
}

/**
 * Calculates settlement payments.
 */
public interface PaymentCalculator {
    List<Payment> calculate(Group group, List<Friendship> friendships);
}

@Service
public class SimplePaymentCalculator implements PaymentCalculator {
    private final CurrencyConverter converter;

    public SimplePaymentCalculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override public List<Payment> calculate(Group group, List<Friendship> friendships) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Notification operations.
 */
public interface NotificationService {
    void createNotification(String userId, NotificationType type, String message);
    List<Notification> listNotifications(String userId);
    void markAsRead(String notificationId);
}

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifRepo;
    private final UserRepository userRepo;

    public NotificationServiceImpl(NotificationRepository notifRepo,
                                   UserRepository userRepo) {
        this.notifRepo = notifRepo;
        this.userRepo = userRepo;
    }

    @Override public void createNotification(String userId, NotificationType type, String message) {
        throw new UnsupportedOperationException();
    }
    @Override public List<Notification> listNotifications(String userId) {
        throw new UnsupportedOperationException();
    }
    @Override public void markAsRead(String notificationId) {
        throw new UnsupportedOperationException();
    }
}
