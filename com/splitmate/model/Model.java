// Base entity with common ID field
public abstract class BaseEntity {
    protected String id;
    public String getId();
    public void setId(String id);
}

// --- Domain Entities ---

// A person in the system
public class User extends BaseEntity {
    private String name;
    private String email;
    private BigDecimal balance;               // positive = owes money; negative = should receive
    private List<Friendship> friends;         // aggregation
    private List<Group> groups;               // aggregation

    public String getName();
    public void setName(String name);
    public String getEmail();
    public void setEmail(String email);
    public BigDecimal getBalance();
    public void setBalance(BigDecimal balance);
    public List<Friendship> getFriends();
    public void addFriend(Friendship f);
    public List<Group> getGroups();
    public void joinGroup(Group g);
}

// Represents a friendship between two users
public class Friendship extends BaseEntity {
    private User userA;
    private User userB;
    private LocalDate since;

    public User getUserA();
    public User getUserB();
    public LocalDate getSince();
}

// A collection of users splitting expenses
public class Group extends BaseEntity {
    private String name;
    private List<User> members;               // aggregation
    private List<Expense> expenses;           // aggregation

    public String getName();
    public void setName(String name);
    public List<User> getMembers();
    public void addMember(User u);
    public List<Expense> getExpenses();
    public void addExpense(Expense e);
}

// Base class for any expense
public abstract class Expense extends BaseEntity {
    protected User owner;
    protected BigDecimal amount;
    protected String description;
    protected LocalDate date;

    public abstract Map<User, BigDecimal> calculateShares();  // how much each owes
    public User getOwner();
    public BigDecimal getAmount();
    public String getDescription();
    public LocalDate getDate();
}

// One-off expense  
public class OneTimeExpense extends Expense {
    @Override
    public Map<User, BigDecimal> calculateShares();
}

// Recurring expense: extends base
public class RecurringExpense extends Expense {
    private Recurrence recurrence;

    @Override
    public Map<User, BigDecimal> calculateShares();
    public Recurrence getRecurrence();
}

// Details of recurrence
public class Recurrence {
    private Frequency frequency;
    private int interval;   // e.g., every 2 weeks

    public Frequency getFrequency();
    public int getInterval();
}

// Frequency enum
public enum Frequency {
    DAILY, WEEKLY, MONTHLY;
}

// A single payment transaction between two users  
public class Payment extends BaseEntity {
    private User from;
    private User to;
    private BigDecimal amount;

    public User getFrom();
    public User getTo();
    public BigDecimal getAmount();
}
