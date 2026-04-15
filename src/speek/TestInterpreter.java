package speek;

/**
 * TestInterpreter — runs all four required SPEEK sample programs.
 *
 * Expected output:
 *   Program 1: 16
 *   Program 2: Sitare / Hello from SPEEK
 *   Program 3: Pass
 *   Program 4: 1 / 2 / 3 / 4
 */
public class TestInterpreter {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        System.out.println("=== Program 1: Arithmetic ===");
        interpreter.run(
            "let x be 10\n" +
            "let y be 3\n" +
            "let result be x + y * 2\n" +
            "say result\n"
        );

        System.out.println("\n=== Program 2: Strings ===");
        interpreter.run(
            "let name be \"Sitare\"\n" +
            "say name\n" +
            "say \"Hello from SPEEK\"\n"
        );

        System.out.println("\n=== Program 3: Conditional ===");
        interpreter.run(
            "let score be 85\n" +
            "if score is greater than 50 then\n" +
            "    say \"Pass\"\n"
        );

        System.out.println("\n=== Program 4: Loop ===");
        interpreter.run(
            "let i be 1\n" +
            "repeat 4 times\n" +
            "    say i\n" +
            "    let i be i + 1\n"
        );
    }
}
