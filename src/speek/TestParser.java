package speek;

import java.util.List;

/**
 * TestParser — manual test for the Tokenizer + Parser pipeline.
 */
public class TestParser {

    public static void main(String[] args) {
        String code = "let x be 10\n"
                    + "let name be \"hello\"\n"
                    + "say x + 5\n"
                    + "if x is greater than 3 then\n"
                    + "    say x - 1\n"
                    + "repeat 3 times\n"
                    + "    say x * 2 / 1\n"
                    + "if x == 10 then\n"
                    + "    say \"done\"\n";

        Tokenizer tokenizer = new Tokenizer(code);
        List<Token> tokens = tokenizer.tokenize();

        System.out.println("=== TOKENS ===");
        for (Token t : tokens) {
            System.out.println(t);
        }

        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        System.out.println("\n=== INSTRUCTIONS ===");
        for (Instruction instr : instructions) {
            System.out.println(instr);
        }
    }
}
