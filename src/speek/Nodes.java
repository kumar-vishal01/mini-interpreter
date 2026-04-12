package speek;

// ─────────────────────────────────────────────
//  NumberNode  — literal number, e.g. 10 or 3.14
// ─────────────────────────────────────────────
class NumberNode implements Expression {
    private final double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;   // always a Double
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

// ─────────────────────────────────────────────
//  StringNode  — literal string, e.g. "Hello"
// ─────────────────────────────────────────────
class StringNode implements Expression {
    private final String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;   // always a String
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}

// ─────────────────────────────────────────────
//  VariableNode  — variable reference, e.g. x
//  Looks up the variable's current value in Environment
// ─────────────────────────────────────────────
class VariableNode implements Expression {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.get(name);   // throws RuntimeException if not defined
    }

    @Override
    public String toString() {
        return name;
    }
}

// ─────────────────────────────────────────────
//  BinaryOpNode  — left OP right
//  Arithmetic ops  → returns Double
//  Comparison ops  → returns Boolean
// ─────────────────────────────────────────────
class BinaryOpNode implements Expression {
    private final Expression left;
    private final String operator;   // "+", "-", "*", "/", ">", "<", "=="
    private final Expression right;

    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal  = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // ── Arithmetic ──────────────────────────────────────────────────
        if (operator.equals("+") || operator.equals("-") ||
            operator.equals("*") || operator.equals("/")) {

            double l = toDouble(leftVal, operator);
            double r = toDouble(rightVal, operator);

            switch (operator) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/":
                    if (r == 0) throw new RuntimeException("Division by zero.");
                    return l / r;
            }
        }

        // ── Comparison ──────────────────────────────────────────────────
        if (operator.equals(">") || operator.equals("<") || operator.equals("==")) {
            // == can compare strings too
            if (operator.equals("==")) {
                return leftVal.equals(rightVal);
            }
            double l = toDouble(leftVal, operator);
            double r = toDouble(rightVal, operator);
            switch (operator) {
                case ">": return l > r;
                case "<": return l < r;
            }
        }

        throw new RuntimeException("Unknown operator: " + operator);
    }

    // Helper: coerce Object to double (Numbers only)
    private double toDouble(Object val, String op) {
        if (val instanceof Double) return (Double) val;
        throw new RuntimeException(
            "Operator '" + op + "' requires numbers, but got: " + val);
    }


    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}