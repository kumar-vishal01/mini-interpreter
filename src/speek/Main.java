package speek;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main — the command-line entry point for the SPEEK interpreter.
 *
 * Usage:
 *   java speek.Main &lt;path-to-file.speek&gt;
 *
 * Example:
 *   java speek.Main src/samples/program1.speek
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java speek.Main <file.speek>");
            System.err.println("Example: java speek.Main src/samples/program1.speek");
            System.exit(1);
        }

        String filePath = args[0];

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
            Interpreter interpreter = new Interpreter();
            interpreter.run(sourceCode);

        } catch (java.io.IOException e) {
            System.err.println("Error: Could not read file '" + filePath + "'");
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Runtime Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
