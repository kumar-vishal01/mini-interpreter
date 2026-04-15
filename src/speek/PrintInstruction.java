package speek;

/**
 * PrintInstruction — say &lt;expr&gt;
 * Evaluates the expression and prints the result.
 * Whole-number Doubles are printed without the trailing ".0".
 */
public class PrintInstruction implements Instruction {
    private final Expression expr;

    public PrintInstruction(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.evaluate(env);

        if (value instanceof Double) {
            double d = (Double) value;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                System.out.println((long) d);
            } else {
                System.out.println(d);
            }
        } else {
            System.out.println(value);
        }
    }

    @Override
    public String toString() {
        return "PrintInstruction(" + expr + ")";
    }
}
