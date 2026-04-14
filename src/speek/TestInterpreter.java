package speek;

/**
 * TestInterpreter — runs all four required SPEEK sample programs.
 * Use this to verify that your interpreter produces the correct output.
 *
 * Expected output for each program is shown in comments below.
 */
public class TestInterpreter {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        // ─────────────────────────────────────────────────────────────────
        //  Program 1 — Arithmetic and variables
        //  Expected output:  16
        // ─────────────────────────────────────────────────────────────────
        System.out.println("=== Program 1: Arithmetic ===");
        interpreter.run(
            "let x be 10\n" +
            "let y be 3\n" +
            "let result be x + y * 2\n" +
            "say result\n"
        );

        // ─────────────────────────────────────────────────────────────────
        //  Program 2 — String output
        //  Expected output:
        //    Sitare
        //    Hello from SPEEK
        // ─────────────────────────────────────────────────────────────────
        System.out.println("\n=== Program 2: Strings ===");
        interpreter.run(
            "let name be \"Sitare\"\n" +
            "say name\n" +
            "say \"Hello from SPEEK\"\n"
        );

        // ─────────────────────────────────────────────────────────────────
        //  Program 3 — Conditional
        //  Expected output:  Pass
        // ─────────────────────────────────────────────────────────────────
        System.out.println("\n=== Program 3: Conditional ===");
        interpreter.run(
            "let score be 85\n" +
            "if score is greater than 50 then\n" +
            "    say \"Pass\"\n"
        );

        // ─────────────────────────────────────────────────────────────────
        //  Program 4 — Loop with variable update
        //  Expected output:  1  2  3  4  (each on its own line)
        // ─────────────────────────────────────────────────────────────────
        System.out.println("\n=== Program 4: Loop ===");
        interpreter.run(
            "let i be 1\n" +
            "repeat 4 times\n" +
            "    say i\n" +
            "    let i be i + 1\n"
        );
    }
}
