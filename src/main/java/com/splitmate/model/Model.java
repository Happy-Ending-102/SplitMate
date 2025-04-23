// File: src/main/java/model/Model.java
package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

public enum Currency {
    USD, EUR, TRY, GBP, JPY
}

public enum ConversionPolicy {
    INSTANT,    // convert on addExpense()
    DEFERRED    // convert only at settlement time
}

public enum Frequency {
    DAILY, WEEKLY, MONTHLY
}

public abstract class BaseEntity {
    @Id
    protected String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}

@Document(collection = "users")
public class User extends BaseEntity {
    private String name;
    private String email;
    private BigDecimal balance;      // stored in user.baseCurrency
    private Currency baseCurrency;
    @DBRef private List<Friendship> friends;
    @DBRef private List<Group> groups;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Currency getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(Currency baseCurrency) { this.baseCurrency = baseCurrency; }

    public List<Friendship> getFriends() { return friends; }
    public void setFriends(List<Friendship> friends) { this.friends = friends; }
    public void addFriend(Friendship f) { this.friends.add(f); }

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }
    public void joinGroup(Group g) { this.groups.add(g); }
}

@Document(collection = "friendships")
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

@Document(collection = "groups")
public class Group extends BaseEntity {
    private String name;
    private Currency defaultCurrency;
    private ConversionPolicy conversionPolicy;
    @DBRef private List<User> members;
    @DBRef private List<Expense> expenses;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Currency getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(Currency defaultCurrency) { this.defaultCurrency = defaultCurrency; }

    public ConversionPolicy getConversionPolicy() { return conversionPolicy; }
    public void setConversionPolicy(ConversionPolicy conversionPolicy) { this.conversionPolicy = conversionPolicy; }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }
    public void addMember(User u) { this.members.add(u); }

    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }
    public void addExpense(Expense e) { this.expenses.add(e); }
}

@Document(collection = "expenses")
public abstract class Expense extends BaseEntity {
    @DBRef protected User owner;
    protected BigDecimal amount;
    protected Currency currency;      // original currency
    protected String description;
    protected LocalDate date;

    /** Returns per-user shares in group's defaultCurrency */
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
}

public class OneTimeExpense extends Expense {
    @Override
    public Map<User, BigDecimal> calculateShares() {
        throw new UnsupportedOperationException("Not implemented");
    }
}

public class RecurringExpense extends Expense {
    private Recurrence recurrence;

    @Override
    public Map<User, BigDecimal> calculateShares() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Recurrence getRecurrence() { return recurrence; }
    public void setRecurrence(Recurrence recurrence) { this.recurrence = recurrence; }
}

public class Recurrence {
    private Frequency frequency;
    private int interval;

    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }

    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
}

@Document(collection = "payments")
public class Payment extends BaseEntity {
    @DBRef private User from;
    @DBRef private User to;
    private BigDecimal amount;
    private Currency currency;      // payment currency (group.defaultCurrency)

    public User getFrom() { return from; }
    public void setFrom(User from) { this.from = from; }

    public User getTo() { return to; }
    public void setTo(User to) { this.to = to; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
}
