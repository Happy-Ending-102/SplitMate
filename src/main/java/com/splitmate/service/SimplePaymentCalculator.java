// File: SimplePaymentCalculator.java
package com.splitmate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.splitmate.model.Currency;
import com.splitmate.model.Debt;
import com.splitmate.model.Payment;
import com.splitmate.model.User;
import com.splitmate.model.Friendship;
import com.splitmate.repository.DebtRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.service.UserService;
import com.splitmate.service.CurrencyConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SimplePaymentCalculator implements PaymentCalculator {
    private final CurrencyConverter converter;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired private DebtRepository debtRepo;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepo;
    @Autowired private FriendshipRepository friendshipRepo;

    public SimplePaymentCalculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<Payment> calculate() {
        System.err.println("=== STARTING PAYMENT CALCULATION ===");

        // 1) Load users & friendships
        List<User> users = userRepo.findAll();
        List<Friendship> friends = friendshipRepo.findAll();
        System.out.println("Loaded users: " + users.size());
        System.out.println("Loaded friendships: " + friends.size());

        // 2) Clear out existing debts in DB & memory
        debtRepo.deleteAll();
        users.forEach(u -> u.setDebts(new ArrayList<>()));
        System.out.println("Cleared all debts in repository and reset in-memory lists.");

        // 3) Build nodes
        List<String> nodes = users.stream()
                                  .map(User::getId)
                                  .collect(Collectors.toList());
        System.out.println("Node IDs: " + nodes);

        // 3b) Build edges
        List<OjAlgoDebtSettlementSolver.Edge> edges = new ArrayList<>();
        for (Friendship f : friends) {
            String a = f.getUserA().getId();
            String b = f.getUserB().getId();
            edges.add(new OjAlgoDebtSettlementSolver.Edge(a, b));
            edges.add(new OjAlgoDebtSettlementSolver.Edge(b, a));
        }
        System.out.println("Constructed edges: " + edges.size() + " directional links");

        // 3c) Build balances
        Map<String, Double> balances = new LinkedHashMap<>();
        for (User u : users) {
            // assume getBalanceByCurrency returns a wrapper with .getAmount()

            BigDecimal totalTL = u.getBalances().stream()
                .map(b -> b.getCurrency() == Currency.TRY
                    ? b.getAmount()
                    : converter.convert(b.getAmount(), b.getCurrency(), Currency.TRY))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            balances.put(u.getId(), totalTL.doubleValue());
            System.out.println("Balance of user " + u.getId() + " = " + totalTL);
        }

        // 4) Solve the settlement problem
        System.err.println("Running OjAlgoDebtSettlementSolver...");
        var solver = new OjAlgoDebtSettlementSolver();
        List<OjAlgoDebtSettlementSolver.Semih> txns = solver.settle(nodes, edges, balances);
        System.out.println("Solver returned " + txns.size() + " transactions:");
        txns.forEach(t -> System.out.println("  from=" + t.from + " to=" + t.to + " amount=" + t.amount));

        // 5) Persist results as Debt objects
        List<Payment> newDebts = new ArrayList<>();
        for (var t : txns) {
            Debt d = new Debt();
            User from = findUser(t.from, users);
            User to   = findUser(t.to, users);
            d.setFrom(from);
            d.setTo(to);
            d.setAmount(BigDecimal.valueOf(t.amount));
            debtRepo.save(d);
            System.out.println("Saved Debt: " + from.getId() + " → " + to.getId() + " : " + t.amount);

            from.getDebts().add(d);
            to.getDebts().add(d);
            newDebts.add(d);
        }

        // 6) Update users (so their DBRef debts list is correct)
        users.forEach(userService::updateUser);
        System.out.println("Updated user debt references in DB.");

        System.err.println("=== PAYMENT CALCULATION COMPLETE ===");
        return newDebts;
    }

    /** Helper to find a User by id in the provided list */
    private User findUser(String id, List<User> users) {
        return users.stream()
                    .filter(u -> id.equals(u.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + id));
    }
}
