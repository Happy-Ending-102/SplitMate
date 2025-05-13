// File: SimplePaymentCalculator.java
package com.splitmate.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import com.splitmate.model.Currency;
import com.splitmate.model.Debt;
import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.splitmate.repository.DebtRepository;
import com.fasterxml.jackson.databind.JsonNode;                   // ← for JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode;            // ← if you use ObjectNode
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.splitmate.model.*;
import com.splitmate.repository.UserRepository; 
import com.splitmate.repository.FriendshipRepository;

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
        System.err.println("Calculating payments with ojAlgo…");

        // 1) Load users & friendships
        List<User> users           = userRepo.findAll();
        List<Friendship> friends   = friendshipRepo.findAll();

        // 2) Clear out existing debts in DB & memory
        debtRepo.deleteAll();
        users.forEach(u -> u.setDebts(new ArrayList<>()));

        // 3) Build nodes, edges, balances
        List<String> nodes = users.stream()
                                  .map(User::getId)
                                  .collect(Collectors.toList());

        List<OjAlgoDebtSettlementSolver.Edge> edges = new ArrayList<>();
        for (Friendship f : friends) {
            // adjust these getters to match your Friendship model:
            String a = f.getUserA().getId();
            String b = f.getUserB().getId();
            edges.add(new OjAlgoDebtSettlementSolver.Edge(a, b));
            edges.add(new OjAlgoDebtSettlementSolver.Edge(b, a));
        }

        Map<String,Double> balances = new LinkedHashMap<>();
        for (User u : users) {
            Balance bal = u.getBalanceByCurrency(u.getBaseCurrency());
            balances.put(u.getId(), bal.getAmount().doubleValue());
        }

        // 4) Solve in-Java
        var solver = new OjAlgoDebtSettlementSolver();
        List<OjAlgoDebtSettlementSolver.Semih> txns =
            solver.settle(nodes, edges, balances);

        // 5) Persist results as Debt objects
        List<Payment> newDebts = new ArrayList<>();
        for (var t : txns) {
            Debt d = new Debt();
            User from = findUser(t.from, users);
            User to   = findUser(t.to,   users);
            d.setFrom(from);
            d.setTo(to);
            d.setAmount(BigDecimal.valueOf(t.amount));
            debtRepo.save(d);

            from.getDebts().add(d);
            to.getDebts().add(d);
            newDebts.add(d);
        }

        // 6) Update users (so their DBRef debts list is correct)
        users.forEach(userService::updateUser);

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
