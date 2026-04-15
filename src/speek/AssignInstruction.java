package speek;

/**
 * AssignInstruction — let x be &lt;expr&gt;
 * Evaluates the expression and stores the result in the Environment.
 */
public class AssignInstruction implements Instruction {
    private final String name;
    private final Expression expr;

    public AssignInstruction(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.evaluate(env);
        env.set(name, value);
    }

    @Override
    public String toString() {
        return "AssignInstruction(" + name + " = " + expr + ")";
    }
}
