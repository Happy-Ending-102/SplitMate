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
        System.err.println("Calculating payments...");
        List<User> users           = userRepo.findAll();
        List<Friendship> friendships   = friendshipRepo.findAll();
        // 1) Clear out any existing debts
        users.forEach(u -> u.setDebts(new ArrayList<>()));

        // clear the debts in the database
        List<Debt> existingDebts = debtRepo.findAll();
        for (Debt d : existingDebts) {
            debtRepo.delete(d);
        }

        // 2) Build JSON payload
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode nodesArr = payload.putArray("nodes");
        users.forEach(u -> nodesArr.add(u.getId()));

        ArrayNode edgesArr = payload.putArray("edges");
        for (Friendship f : friendships) {
            edgesArr.add(mapper.createArrayNode()
                              .add(f.getUserA().getId())
                              .add(f.getUserB().getId()));
        }

        ObjectNode balancesObj = payload.putObject("balances");
        for (User u : users) {
            // Sum all of this user's balances into TRY
            BigDecimal totalTL = u.getBalances().stream()
                .map(b -> b.getCurrency() == Currency.TRY
                    ? b.getAmount()
                    : converter.convert(b.getAmount(), b.getCurrency(), Currency.TRY))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            balancesObj.put(u.getId(), totalTL.doubleValue());
        }
        // — PRINT TO CONSOLE FOR DEBUGGING —
        String prettyJson="semih";
        try {
            prettyJson = mapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.err.println("DEBUG: initial JSON payload:\n" + prettyJson);

        // 3) Call Python solver
        List<Payment> newDebts = new ArrayList<>();
                ProcessBuilder pb = new ProcessBuilder(
            "python3",           // Windows “py” launcher           // use Python 3
            "src/main/resources/py/calculate.py"  // relative to projectRoot
        );
        // pb.redirectErrorStream(true);

        try {
            Process proc = pb.start();
            // Send JSON to Python
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()))) {
                mapper.writeValue(w, payload);
            }
            int exit = proc.waitFor();
            if (exit != 0) {
                try (BufferedReader err = new BufferedReader(
                        new InputStreamReader(proc.getErrorStream()))) {
                    err.lines().forEach(System.err::println);
                }
                throw new RuntimeException("Python solver exited with code " + exit);
            }

            // JSON only if Python succeeded
            JsonNode result = mapper.readTree(proc.getInputStream());
            System.out.println("DEBUG: full JSON result = " + result.toString());

            // 4) Convert JSON back into Debt objects
            if (result.isArray()) {
                for (JsonNode txn : result) {
                    System.out.println("DEBUG: txn node = " + txn.toString());
                    String fromId = txn.get(0).asText();
                    String toId   = txn.get(1).asText();
                    BigDecimal amt = BigDecimal.valueOf(txn.get(2).asDouble());
                    // print parsed values
                    System.out.println("DEBUG: parsed fromId=" + fromId
                        + ", toId=" + toId
                        + ", amt=" + amt);


                    Debt debt = new Debt();
                    User from = findUser(fromId, users);
                    User to   = findUser(toId,   users);

                    debt.setFrom(from);
                    debt.setTo(to);
                    debt.setAmount(amt);
                    debtRepo.save(debt);

                    // Attach to both users
                    from.getDebts().add(debt);
                    to.getDebts().add(debt);

                    newDebts.add(debt);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Payment calculation failed", e);
        }
        for(User u : users) {
            // Save the updated user with debts
            userService.updateUser(u);
        }

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
