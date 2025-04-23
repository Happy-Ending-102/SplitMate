// File: src/main/java/service/Service.java
package service;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import model.*;
import repository.*;

public interface CurrencyConverter {
    BigDecimal convert(BigDecimal amount, Currency from, Currency to);
}

@Service
public class ApiCurrencyConverter implements CurrencyConverter {
    private final String apiKey;
    @Autowired
    public ApiCurrencyConverter(@Value("${fx.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }
    @Override
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        // call external FX API…
        throw new UnsupportedOperationException("Not implemented");
    }
}

public interface UserService {
    User getUser(String id);
    User registerUser(User u);
    User updateUser(User u);
    void removeUser(String id);
}

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepo;
    @Override public User getUser(String id) { return userRepo.findById(id).orElse(null); }
    @Override public User registerUser(User u) { return userRepo.save(u); }
    @Override public User updateUser(User u) { return userRepo.save(u); }
    @Override public void removeUser(String id) { userRepo.deleteById(id); }
}

public interface GroupService {
    Group getGroup(String id);
    Group createGroup(Group g);
    Group addUserToGroup(String groupId, String userId);
    Group removeUserFromGroup(String groupId, String userId);
}

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired private GroupRepository groupRepo;
    @Autowired private UserRepository userRepo;

    @Override public Group getGroup(String id) { return groupRepo.findById(id).orElse(null); }
    @Override public Group createGroup(Group g) { return groupRepo.save(g); }

    @Override
    public Group addUserToGroup(String groupId, String userId) {
        Group g = getGroup(groupId);
        User u = userRepo.findById(userId).orElseThrow();
        g.getMembers().add(u);
        return groupRepo.save(g);
    }

    @Override
    public Group removeUserFromGroup(String groupId, String userId) {
        Group g = getGroup(groupId);
        g.getMembers().removeIf(u -> u.getId().equals(userId));
        return groupRepo.save(g);
    }
}

public interface ExpenseService {
    Expense addExpense(String groupId, Expense e);
    List<Expense> listExpenses(String groupId);
}

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired private ExpenseRepository expRepo;
    @Autowired private GroupRepository groupRepo;
    @Autowired private CurrencyConverter converter;

    @Override
    public Expense addExpense(String groupId, Expense e) {
        Group g = groupRepo.findById(groupId).orElseThrow();
        if (g.getConversionPolicy() == ConversionPolicy.INSTANT) {
            BigDecimal convAmt = converter.convert(
                e.getAmount(), e.getCurrency(), g.getDefaultCurrency());
            e.setAmount(convAmt);
            e.setCurrency(g.getDefaultCurrency());
        }
        g.getExpenses().add(e);
        expRepo.save(e);
        groupRepo.save(g);
        return e;
    }

    @Override
    public List<Expense> listExpenses(String groupId) {
        return expRepo.findByGroupId(groupId);
    }
}

public interface PaymentCalculator {
    List<Payment> calculate(Group group, List<Friendship> friendships);
}

@Service
public class SimplePaymentCalculator implements PaymentCalculator {
    private final CurrencyConverter converter;
    @Autowired
    public SimplePaymentCalculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<Payment> calculate(Group group, List<Friendship> friendships) {
        Currency target = group.getDefaultCurrency();
        Map<User, BigDecimal> net = new HashMap<>();
        for (User u : group.getMembers()) {
            BigDecimal bal = u.getBalance();
            BigDecimal conv = converter.convert(bal, u.getBaseCurrency(), target);
            net.put(u, conv);
        }
        // TODO: settlement algorithm using net & friendships
        return Collections.emptyList();
    }
}
