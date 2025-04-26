// File: src/main/java/model/Model.java
package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

public enum Currency {
    USD, EUR, TRY, GBP, JPY
}

public enum ConversionPolicy {
    INSTANT, DEFERRED
}

public enum Frequency {
    DAILY, WEEKLY, MONTHLY
}

public enum NotificationType {
    FRIEND_REQUEST,
    GROUP_INVITE,
    PAYMENT_RECEIVED,
    PAYMENT_REJECTED,
    DEBT_REMINDER,
    RECURRING_EXPENSE
}

public abstract class BaseEntity {
    @Id protected String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}

@Document("users")
public class User extends BaseEntity {
    private String name;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private String profileImageUrl;
    private BigDecimal balance;
    private Currency baseCurrency;
    @DBRef private List<Friendship> friends;
    @DBRef private List<Group> groups;
    @DBRef private List<Notification> notifications;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Currency getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(Currency baseCurrency) { this.baseCurrency = baseCurrency; }

    public List<Friendship> getFriends() { return friends; }
    public void addFriend(Friendship f) { this.friends.add(f); }

    public List<Group> getGroups() { return groups; }
    public void joinGroup(Group g) { this.groups.add(g); }

    public List<Notification> getNotifications() { return notifications; }
    public void addNotification(Notification n) { this.notifications.add(n); }

    public String getProfileImageUrl() { return profileImageUrl;}
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl;}

    
}

@Document("friendships")
public class Friendship extends BaseEntity {
    @DBRef private User userA;
    @DBRef private User userB;
    private LocalDate since;

    public User getUserA() { return userA; }
    public void setUserA(User userA) { this.userA = userA; }

    public User getUserB() { return userB; }
    public void setUserB(User userB) { this.userB = userB; }

    public LocalDate getSince() { return since; }
    public void setSince(LocalDate since) { this.since = since; }
}

@Document("groups")
public class Group extends BaseEntity {
    private String name;
    private String profileImageUrl;
    private Currency defaultCurrency;
    private ConversionPolicy conversionPolicy;
    @DBRef private List<User> members;
    @DBRef private List<User> frozenMembers;
    @DBRef private List<Expense> expenses;
    @DBRef private List<Notification> notifications;
    

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Currency getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(Currency defaultCurrency) { this.defaultCurrency = defaultCurrency; }

    public ConversionPolicy getConversionPolicy() { return conversionPolicy; }
    public void setConversionPolicy(ConversionPolicy conversionPolicy) { this.conversionPolicy = conversionPolicy; }

    public List<User> getMembers() { return members; }
    public void addMember(User u) { this.members.add(u); }

    public List<Expense> getExpenses() { return expenses; }
    public void addExpense(Expense e) { this.expenses.add(e); }

    public List<Notification> getNotifications() { return notifications; }
    public void addNotification(Notification n) { this.notifications.add(n); }
    
    public String getProfileImageUrl() { return profileImageUrl;}
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl;}

    public List<User> getFrozenMembers() { return frozenMembers;}
    public void setFrozenMembers(List<User> frozenMembers) {this.frozenMembers = frozenMembers;}

    

    
}

@Document("expenses")
public abstract class Expense extends BaseEntity {
    @DBRef protected User owner;
    protected BigDecimal amount;
    protected Currency currency;
    protected String description;
    protected String billImageUrl;
    protected LocalDate date;
    @DBRef protected List<User> divisionUsers;

    public abstract Map<User, BigDecimal> calculateShares();

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getBillImageUrl() { return billImageUrl;}

    public List<User> getDivisionUsers() { return divisionUsers;}

    public void setBillImageUrl(String billImageUrl) { this.billImageUrl = billImageUrl;}

    public void setDivisionUsers(List<User> divisionUsers) { this.divisionUsers = divisionUsers;}

    
}

public class OneTimeExpense extends Expense {
    @Override
    public Map<User, BigDecimal> calculateShares() { throw new UnsupportedOperationException(); }
}

public class RecurringExpense extends Expense {

    private Frequency frequency;

    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }

    @Override
    public Map<User, BigDecimal> calculateShares() { throw new UnsupportedOperationException(); }

}

@Document("payments")
public class Payment extends BaseEntity {
    @DBRef private User from;
    @DBRef private User to;
    private BigDecimal amount;
    private Currency currency;
    private boolean isPaid;

    public User getFrom() { return from; }
    public void setFrom(User from) { this.from = from; }

    public User getTo() { return to; }
    public void setTo(User to) { this.to = to; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public boolean isPaid() { return isPaid;}
    public void setPaid(boolean isPaid) { this.isPaid = isPaid;}

    
}

@Document("notifications")
public class Notification extends BaseEntity {
    @DBRef private User user;
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
