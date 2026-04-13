package speek;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestParser {

    public static void main(String[] args) throws Exception {

        // Step 1: Input program
        String source = "let x be 10\n"
                     + "let name be \"hello\"\n"
                     + "say x + 5\n"
                     + "if x > 3 then\n"
                     + "  say x - 1\n"
                     + "repeat 3 times\n"
                     + "  say x * 2 / 1\n"
                     + "if x == 10 then\n"
                     + "  say \"done\"\n";

        
        String code = new String(Files.readAllBytes(Paths.get("src/samples/programX.speek")));

        String test = new String(Files.readAllBytes(Paths.get("src/samples/test4.speek")));

        // Step 2: Tokenize
        Tokenizer tokenizer = new Tokenizer(test);
        List<Token> tokens = tokenizer.tokenize();

        System.out.println("=== TOKENS ===");
        for (Token t : tokens) {
            System.out.println(t);
        }

        // Step 3: Parse
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        System.out.println("\n=== INSTRUCTIONS ===");
        for (Instruction instr : instructions) {
            System.out.println(instr);
        }
    }
}