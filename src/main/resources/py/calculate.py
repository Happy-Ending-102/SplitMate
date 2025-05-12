import pulp

def solve_debt_settlement(nodes, edges, balances):
    if pulp is None:
        raise ImportError("PuLP is not installed.")
    
    # Big-M value
    M = sum(abs(b) for b in balances.values())
    
    # Create the LP problem
    prob = pulp.LpProblem("Minimize_Transactions", pulp.LpMinimize)
    
    # Variables: flow f_uv and usage x_uv for each undirected edge (u, v)
    f = {}
    x = {}
    for u, v in edges:
        f[(u, v)] = pulp.LpVariable(f"f_{u}_{v}", -M, M, cat="Continuous")
        x[(u, v)] = pulp.LpVariable(f"x_{u}_{v}", 0, 1, cat="Binary")
        # Link flow and usage variable
        prob += f[(u, v)] <=  M * x[(u, v)]
        prob += f[(u, v)] >= -M * x[(u, v)]
    
    # Flow conservation constraints for each node
    for v in nodes:
        inflow = []
        outflow = []
        for u, w in edges:
            if w == v:
                inflow.append(f[(u, w)])
            if u == v:
                outflow.append(f[(u, w)])
        prob += (pulp.lpSum(inflow) - pulp.lpSum(outflow) == balances[v]), f"balance_{v}"
    
    # Objective: minimize the total number of used edges (transactions)
    prob += pulp.lpSum(x[(u, v)] for (u, v) in edges), "Minimize_Transactions"
    
    # Solve the problem with no verbose output
    solver = pulp.PULP_CBC_CMD(msg=0)
    prob.solve(solver)
    
    # Extract and return the list of transactions
    transactions = []
    for u, v in edges:
        val = pulp.value(f[(u, v)])
        if val is None:
            continue
        if abs(val) > 1e-6:
            if val > 0:
                transactions.append((u, v, val))
            else:
                transactions.append((v, u, -val))
    return transactions

import sys, json
import pulp

def main():
    # Read JSON input from stdin
    data = json.load(sys.stdin)
    nodes = data["nodes"]
    edges = [tuple(e) for e in data["edges"]]
    balances = data["balances"]

    # Solve
    txns = solve_debt_settlement(nodes, edges, balances)

    # Emit JSON array of [payer, payee, amount]
    json.dump(txns, sys.stdout)

if __name__ == "__main__":
    main()
    # Example usage
    #nodes = ["A", "B", "C", "D"]
    #edges = [("A", "B"), ("A", "C"), ("B", "D"), ("C", "D")]
    #balances = {"A": -10, "B": 5, "C": -5, "D": 10}
    
    # txns = solve_debt_settlement(nodes, edges, balances)
    # print("Optimal transactions:")
    # for payer, payee, amount in txns:
    #     print(f"  {payer} -> {payee}: {amount:.2f}")
