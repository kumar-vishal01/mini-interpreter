package speek;

/**
 * Handles assignment.
 * SPEEK syntax:  let x be 10 + 5
 */
public class AssignInstruction implements Instruction {
    private final String     variableName;
    private final Expression expression;

    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression   = expression;
    }

    @Override
    public void execute(Environment env) {
        Object result = expression.evaluate(env);
        env.set(variableName, result);
    }
}
