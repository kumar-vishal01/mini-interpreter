package speek;

/**
 * NumberNode — a literal number in source code, e.g. 10 or 3.14
 */
public class NumberNode implements Expression {
    private final double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
