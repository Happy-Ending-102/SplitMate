// Exposes user-related endpoints
public class UserController {
    private UserService userService;
    public UserController(UserService svc);
    public User getUser(String userId);
    public void createUser(User dto);
    public void updateUser(String userId, User dto);
    public void deleteUser(String userId);
}

// Manages group endpoints
public class GroupController {
    private GroupService groupService;
    public GroupController(GroupService svc);
    public Group getGroup(String groupId);
    public void createGroup(Group dto);
    public void addMember(String groupId, String userId);
    public void removeMember(String groupId, String userId);
}

// Handles expense endpoints
public class ExpenseController {
    private ExpenseService expenseService;
    public ExpenseController(ExpenseService svc);
    public void addExpense(String groupId, Expense dto);
    public List<Expense> getExpenses(String groupId);
}

// Triggers payment calculations
public class PaymentController {
    private PaymentCalculator calculator;
    private UserService userService;
    private FriendshipRepository friendshipRepo;

    public PaymentController(PaymentCalculator calc,
                             UserService uSvc,
                             FriendshipRepository fRepo);

    public List<Payment> calculatePayments(String groupId);
}
