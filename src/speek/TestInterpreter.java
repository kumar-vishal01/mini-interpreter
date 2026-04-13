package speek;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestInterpreter {

    public static void main(String[] args) throws Exception {

        System.out.println("=== RUNNING SPEEK PROGRAM ===\n");

        // Option 1: Read from file (recommended)
        String code = new String(Files.readAllBytes(Paths.get("src/samples/programX.speek")));

        String test = new String(Files.readAllBytes(Paths.get("src/samples/test4.speek")));

        // Option 2: Inline test (uncomment if needed)
        /*
        String code =
                "let x be 10\n" +
                "let y be 20\n" +
                "let z be x + y * 2\n" +
                "say z\n" +
                "if z is greater than 30 then\n" +
                "    say \"big number\"\n" +
                "repeat 3 times\n" +
                "    say \"hello\"\n";
        */

        System.out.println("=== SOURCE CODE ===");
        System.out.println(test);

        System.out.println("\n=== OUTPUT ===");

        Interpreter interpreter = new Interpreter();
        interpreter.run(test);

        System.out.println("\n=== END ===");
    }
}