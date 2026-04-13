package speek;

/**
 * Represents a binary operation between two expressions.
 * e.g. x + y * 2   or   score > 50
 *
 * Arithmetic operators (+, -, *, /) return a Double.
 * Comparison operators (>, <, ==)    return a Boolean.
 */
public class BinaryOpNode implements Expression {
    private final Expression left;
    private final String     operator;
    private final Expression right;

    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left     = left;
        this.operator = operator;
        this.right    = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal  = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        switch (operator) {
            // ── Arithmetic ──────────────────────────────────────────────────
            case "+": {
                // Support string concatenation
                if (leftVal instanceof String || rightVal instanceof String) {
                    return stringify(leftVal) + stringify(rightVal);
                }
                return toDouble(leftVal) + toDouble(rightVal);
            }
            case "-": return toDouble(leftVal) - toDouble(rightVal);
            case "*": return toDouble(leftVal) * toDouble(rightVal);
            case "/": {
                double divisor = toDouble(rightVal);
                if (divisor == 0) throw new RuntimeException("Division by zero");
                return toDouble(leftVal) / divisor;
            }

            // ── Comparisons ─────────────────────────────────────────────────
            case ">":  return toDouble(leftVal) >  toDouble(rightVal);
            case "<":  return toDouble(leftVal) <  toDouble(rightVal);
            case "==": return leftVal.equals(rightVal);

            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
        
    }
    // ── Helpers ────────────────────────────────────────────────────────────

    

    private String stringify(Object val) {
        if (val instanceof Double) {
            double d = (Double) val;
            // Print as integer when there is no fractional part
            if (d == Math.floor(d) && !Double.isInfinite(d)) return String.valueOf((long) d);
            return String.valueOf(d);
        }
        return String.valueOf(val);
    }
}
