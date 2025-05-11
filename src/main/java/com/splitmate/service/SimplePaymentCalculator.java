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
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import com.splitmate.model.Currency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.User;

@Service
public class SimplePaymentCalculator implements PaymentCalculator {
    private final CurrencyConverter converter;
    private final ObjectMapper mapper = new ObjectMapper();

    public SimplePaymentCalculator(CurrencyConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<Payment> calculate(List<User> users, List<Friendship> friendships) {
        // 1) Compute net balances in TRY for each user
        Map<String, Double> balances = new LinkedHashMap<>();
        for (User u : users) {
            BigDecimal net = BigDecimal.ZERO;
            // Assume User.balances holds per-currency amounts
            if (u.getBalances() != null) {
                for (var b : u.getBalances()) {
                    BigDecimal inTry = converter.convert(b.getAmount(), b.getCurrency(), Currency.TRY);
                    net = net.add(inTry);
                }
            }
            balances.put(u.getId(), net.doubleValue());
        }

        // 2) Build JSON payload for Python solver
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode nodesNode = payload.putArray("nodes");
        balances.keySet().forEach(nodesNode::add);

        ArrayNode edgesNode = payload.putArray("edges");
        for (Friendship f : friendships) {
            ArrayNode edge = mapper.createArrayNode();
            edge.add(f.getUserA().getId());
            edge.add(f.getUserB().getId());
            edgesNode.add(edge);
        }

        ObjectNode balancesNode = payload.putObject("balances");
        balances.forEach(balancesNode::put);

        List<Payment> payments = new ArrayList<>();
        try {
            // 3) Launch Python solver (calculate.py must read JSON from stdin and output JSON)
            ProcessBuilder pb = new ProcessBuilder("python3", "calculate.py"); // todo check path
            Process process = pb.start();

            // send input
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                mapper.writeValue(writer, payload);
            }

            // read output
            List<Transaction> txs;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                txs = mapper.readValue(reader, new TypeReference<List<Transaction>>() {});
            }

            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("Python solver exited with code " + exit);
            }

            // 4) Convert to Payment objects
            for (Transaction tx : txs) {
                Payment p = new Payment();
                User from = findUser(tx.getFrom(), users);
                User to   = findUser(tx.getTo(), users);
                p.setFrom(from);
                p.setTo(to);
                p.setAmount(BigDecimal.valueOf(tx.getAmount()));
                p.setCurrency(Currency.TRY);
                p.setPaid(false);
                payments.add(p);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to calculate payments", e);
        }

        return payments;
    }

    /** Helper to find a User by id in the provided list */
    private User findUser(String id, List<User> users) {
        return users.stream()
                    .filter(u -> id.equals(u.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + id));
    }

    /** Internal DTO matching Python output */
    private static class Transaction {
        private String from;
        private String to;
        private double amount;

        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getTo()   { return to; }
        public void setTo(String to)     { this.to = to; }
        public double getAmount()        { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }
    
}
