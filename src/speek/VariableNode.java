package speek;

/**
 * VariableNode — a variable reference, e.g. x or total.
 * Looks up the variable's current value in the Environment.
 */
public class VariableNode implements Expression {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
