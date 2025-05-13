package com.splitmate.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class OjAlgoDebtSettlementSolverPrintTest {

    @Test
    void printSettlementOutput() {
        // 1) Instantiate the solver
        OjAlgoDebtSettlementSolver solver = new OjAlgoDebtSettlementSolver();

        // 2) Define a small example (A owes 50, B owes 30, C is owed 80)
        List<String> nodes = List.of("A", "B", "C");
        List<OjAlgoDebtSettlementSolver.Edge> edges = List.of(
            new OjAlgoDebtSettlementSolver.Edge("A", "C"),
            new OjAlgoDebtSettlementSolver.Edge("C", "A"),
            new OjAlgoDebtSettlementSolver.Edge("B", "C"),
            new OjAlgoDebtSettlementSolver.Edge("C", "B")
        );
        Map<String, Double> balances = Map.of(
            "A", -50.0,
            "B", -30.0,
            "C",  80.0
        );

        // 3) Solve
        List<OjAlgoDebtSettlementSolver.Semih> txns =
            solver.settle(nodes, edges, balances);

        // 4) Print results
        System.out.println("Settlement transactions:");
        for (OjAlgoDebtSettlementSolver.Semih t : txns) {
            System.out.printf("%s -> %s: %.2f%n", t.from, t.to, t.amount);
        }
    }
}
