package speek;

import java.util.List;

/**
 * IfInstruction — if &lt;condition&gt; then
 *                     &lt;body&gt;
 * Executes the body only when the condition evaluates to true.
 */
public class IfInstruction implements Instruction {
    private final Expression condition;
    private final List<Instruction> body;

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body      = body;
    }

    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);
        if (result instanceof Boolean && (Boolean) result) {
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IfInstruction(").append(condition).append(")");
        for (Instruction instr : body) {
            sb.append("\n    ").append(instr);
        }
        return sb.toString();
    }
}
