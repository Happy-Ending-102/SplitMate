package com.splitmate.service;

import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.Optimisation;

import java.util.*;

/**
 * Solves the standard “minimize number of edges with positive flow”
 * settlement problem as a MILP in pure Java.
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
            Variable f = Variable.make("f_"+e.from+"_"+e.to)
                                 .lower(0.0).upper(M)
                                 .weight(0.0);
            Variable x = Variable.make("x_"+e.from+"_"+e.to)
                                 .binary()
                                 .weight(1.0);       // minimize ∑x
            model.addVariable(f);
            model.addVariable(x);
            fVar.put(e, f);
            xVar.put(e, x);
            // capacity constraint: f_e – M·x_e ≤ 0
            model.addExpression("cap_"+e.from+"_"+e.to)
                 .upper(0.0)
                 .set(f, 1.0)
                 .set(x, -M);
        }

        // flow‐balance at each node
        for (String v : nodes) {
            var expr = model.addExpression("bal_"+v)
                            .level(balances.getOrDefault(v, 0.0));
            for (Edge e : edges) {
                if (e.to.equals(v))   expr.set(fVar.get(e),  1.0);
                if (e.from.equals(v)) expr.set(fVar.get(e), -1.0);
            }
        }

        // solve the MILP
        Optimisation.Result r = model.minimise();
        if (!r.getState().isFeasible()) {
            throw new IllegalStateException("No feasible settlement: " + r.getState());
        }

        // extract positive flows as transactions
        List<Semih> result = new ArrayList<>();
        for (Edge e : edges) {
            double amt = r.get(fVar.get(e)).doubleValue();
            if (amt > 1e-8) {
                result.add(new Semih(e.from, e.to, amt));
            }
        }
        return result;
    }
}
