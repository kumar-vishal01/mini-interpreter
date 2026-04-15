package speek;

import java.util.List;

/**
 * RepeatInstruction — repeat &lt;count&gt; times
 *                         &lt;body&gt;
 * Runs the body exactly count times.
 */
public class RepeatInstruction implements Instruction {
    private final int count;
    private final List<Instruction> body;

    public RepeatInstruction(int count, List<Instruction> body) {
        this.count = count;
        this.body  = body;
    }

    @Override
    public void execute(Environment env) {
        for (int i = 0; i < count; i++) {
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RepeatInstruction(").append(count).append(")");
        for (Instruction instr : body) {
            sb.append("\n    ").append(instr);
        }
        return sb.toString();
    }
}
