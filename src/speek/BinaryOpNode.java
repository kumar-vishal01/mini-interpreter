package speek;

/**
 * BinaryOpNode — left OP right
 *
 * Arithmetic ops (+, -, *, /) return Double.
 * Comparison ops (>, <, ==) return Boolean.
 */
public class BinaryOpNode implements Expression {
    private final Expression left;
    private final String operator;
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
            case "+": return toDouble(leftVal, operator) + toDouble(rightVal, operator);
            case "-": return toDouble(leftVal, operator) - toDouble(rightVal, operator);
            case "*": return toDouble(leftVal, operator) * toDouble(rightVal, operator);
            case "/": {
                double r = toDouble(rightVal, operator);
                if (r == 0) throw new RuntimeException("Division by zero.");
                return toDouble(leftVal, operator) / r;
            }
            case ">":  return toDouble(leftVal, operator) > toDouble(rightVal, operator);
            case "<":  return toDouble(leftVal, operator) < toDouble(rightVal, operator);
            case "==": return leftVal.equals(rightVal);
        }

        throw new RuntimeException("Unknown operator: " + operator);
    }

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
