package speek;

import java.util.List;

// ─────────────────────────────────────────────────────────────
//  Instruction  — one complete executable statement
// ─────────────────────────────────────────────────────────────
interface Instruction {
    void execute(Environment env);
}

// ─────────────────────────────────────────────────────────────
//  AssignInstruction  — let x be <expr>
//  Evaluates the expression and stores the result in the Environment.
// ─────────────────────────────────────────────────────────────
class AssignInstruction implements Instruction {
    private final String name;       // variable name to assign to
    private final Expression expr;   // expression that produces the value

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

// ─────────────────────────────────────────────────────────────
//  PrintInstruction  — say <expr>
//  Evaluates the expression and prints the result.
//  Whole-number Doubles are printed without the trailing ".0"
//  so that  say 16  prints  16  not  16.0.
// ─────────────────────────────────────────────────────────────
class PrintInstruction implements Instruction {
    private final Expression expr;

    public PrintInstruction(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.evaluate(env);

        if (value instanceof Double) {
            double d = (Double) value;
            // Print as integer when there is no fractional part
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

// ─────────────────────────────────────────────────────────────
//  IfInstruction  — if <condition> then
//                       <body>
//  Executes the body only when the condition evaluates to true.
// ─────────────────────────────────────────────────────────────
class IfInstruction implements Instruction {
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

// ─────────────────────────────────────────────────────────────
//  RepeatInstruction  — repeat <count> times
//                           <body>
//  Runs the body exactly count times.
// ─────────────────────────────────────────────────────────────
class RepeatInstruction implements Instruction {
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
