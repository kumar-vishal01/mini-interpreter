package speek;

import java.util.List;

// ─────────────────────────────────────────────
//  Instruction  — one executable statement
// ─────────────────────────────────────────────
interface Instruction {
    void execute(Environment env);
}

// ─────────────────────────────────────────────
//  AssignInstruction  — let x be <expr>
// ─────────────────────────────────────────────
class AssignInstruction implements Instruction {
    private final String name;        // variable name
    private final Expression expr;    // value to assign

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

// ─────────────────────────────────────────────
//  PrintInstruction  — say <expr>
// ─────────────────────────────────────────────
class PrintInstruction implements Instruction {
    private final Expression expr;

    public PrintInstruction(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.evaluate(env);

        // Print numbers without unnecessary ".0"  (16.0 → "16")
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

// ─────────────────────────────────────────────
//  IfInstruction  — if <condition> is greater than <expr> then
//                     <body>
// ─────────────────────────────────────────────
class IfInstruction implements Instruction {
    private final Expression condition;
    private final List<Instruction> body;

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = body;
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

        if (!body.isEmpty()) sb.append("\n");

        for (int i = 0; i < body.size(); i++) {
            sb.append("    ").append(body.get(i));
            if (i != body.size() - 1) sb.append("\n");
        }

        return sb.toString();
    }
}

// ─────────────────────────────────────────────
//  RepeatInstruction  — repeat <count> times
//                          <body>
// ─────────────────────────────────────────────
class RepeatInstruction implements Instruction {
    private final int count;
    private final List<Instruction> body;

    public RepeatInstruction(int count, List<Instruction> body) {
        this.count = count;
        this.body = body;
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

        if (!body.isEmpty()) sb.append("\n");

        for (int i = 0; i < body.size(); i++) {
            sb.append("    ").append(body.get(i));
            if (i != body.size() - 1) sb.append("\n");
        }

        return sb.toString();
    }
}