package speek;

/**
 * Handles printing output to the screen.
 * SPEEK syntax:  say x   or   say "hello"
 */
public class PrintInstruction implements Instruction {
    private final Expression expression;

    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment env) {
        Object result = expression.evaluate(env);
        // Print integers without a decimal point for cleaner output
        if (result instanceof Double) {
            double d = (Double) result;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                System.out.println((long) d);
                return;
            }
        }
        System.out.println(result);
    }
}
