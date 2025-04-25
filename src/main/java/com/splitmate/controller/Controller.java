// File: src/main/java/controller/Controller.java
package controller;

import java.math.BigDecimal;
import java.util.List;
import model.*;
import service.*;
import repository.FriendshipRepository;

/**
 * Handles user-related UI actions (e.g. from Swing forms).
 */
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User registerUser(User dto, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    public boolean login(String email, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    public User findUserById(String id) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Handles group-related UI actions.
 */
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public Group createGroup(Group dto) {
        throw new UnsupportedOperationException();
    }

    public Group findGroupById(String id) {
        throw new UnsupportedOperationException();
    }

    public Group addMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    public Group removeMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    public Group frozeMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Handles expense-related UI actions.
 */
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public Expense addExpense(String groupId, Expense dto) {
        throw new UnsupportedOperationException();
    }

    public List<Expense> listExpenses(String groupId) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Handles payment calculation actions.
 */
public class PaymentController {
    private final PaymentCalculator calculator;
    private final GroupService groupService;
    private final FriendshipRepository friendshipRepo;

    public PaymentController(PaymentCalculator calculator,
                             GroupService groupService,
                             FriendshipRepository friendshipRepo) {
        this.calculator = calculator;
        this.groupService = groupService;
        this.friendshipRepo = friendshipRepo;
    }

    public List<Payment> calculatePayments(String groupId) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Handles manual currency conversion requests from the UI.
 */
public class CurrencyController {
    private final CurrencyConverter converter;

    public CurrencyController(CurrencyConverter converter) {
        this.converter = converter;
    }

    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        throw new UnsupportedOperationException();
    }
}

/**
 * Handles notification-related UI actions.
 */
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public List<Notification> listUnread(String userId) {
        throw new UnsupportedOperationException();
    }

    public void markAsRead(String notificationId) {
        throw new UnsupportedOperationException();
    }
}
