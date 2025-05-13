package com.splitmate.service;

import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

import java.math.BigDecimal;
import java.util.*;

/**
 * Solves the standard “minimize number of edges with positive flow”
 * settlement problem as a MILP in pure Java using ojAlgo.
 */
public class OjAlgoDebtSettlementSolver {

    public static class Edge {
        public final String from, to;
        public Edge(String from, String to) {
            this.from = from;
            this.to   = to;
        }
    }

    public static class Semih {
        public final String from, to;
        public final double amount;
        public Semih(String from, String to, double amount) {
            this.from   = from;
            this.to     = to;
            this.amount = amount;
        }
    }

    public List<Semih> settle(
            List<String> nodes,
            List<Edge>   edges,
            Map<String,Double> balances
    ) {
        // Big-M = sum of absolute balances
        double M = balances.values().stream()
                           .mapToDouble(Math::abs)
                           .sum();

        ExpressionsBasedModel model = new ExpressionsBasedModel();
        Map<Edge,Variable> fVar = new HashMap<>();
        Map<Edge,Variable> xVar = new HashMap<>();

        // create flow vars f_e ≥ 0 and binary use vars x_e
        for (Edge e : edges) {
            String fname = "f_" + e.from + "_" + e.to;
            String xname = "x_" + e.from + "_" + e.to;

            // Flow variable: continuous between 0 and M
            Variable f = model.addVariable(fname)
                               .lower(0)
                               .upper(M)
                               .weight(0);

            // Binary indicator: 0 or 1
            Variable x = model.addVariable(xname)
                               .binary()
                               .weight(1);

            fVar.put(e, f);
            xVar.put(e, x);

            // capacity constraint: f_e - M*x_e ≤ 0
            model.addExpression("cap_" + e.from + "_" + e.to)
     .upper(0)
     .set(f,  1)
     .set(x, -M);
        }

        // flow-balance at each node
        for (String v : nodes) {
            double bal = balances.getOrDefault(v, 0.0);
            Expression balanceExpr = model.addExpression("bal_" + v)
                                          .level(bal);
            for (Edge e : edges) {
                if (e.to.equals(v))   {
                    balanceExpr.set(fVar.get(e),  1);
                }
                if (e.from.equals(v)) {
                    balanceExpr.set(fVar.get(e), -1);
                }
            }
        }

        // solve the MILP
        Optimisation.Result result = model.minimise();
        if (!result.getState().isFeasible()) {
            throw new IllegalStateException("No feasible settlement: " + result.getState());
        }

                // extract positive flows as transactions
        List<Semih> settlements = new ArrayList<>();
        for (Edge e : edges) {
            Variable flowVar = fVar.get(e);
            // Find variable's index in the model
            long idx = model.indexOf(flowVar);
            // Retrieve its value from the solution
            double amt = result.doubleValue(idx);
            if (amt > 1e-8) {
                settlements.add(new Semih(e.from, e.to, amt));
            }
        }
        return settlements;
    }
}
