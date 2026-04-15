package speek;

import java.util.List;

/**
 * TestToken — quick manual test for the Tokenizer.
 */
public class TestToken {

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

        System.out.println("=== Token List ===");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println("[" + i + "] " + tokens.get(i));
        }
        System.out.println("==================");
        System.out.println("Total tokens: " + tokens.size());
    }
}
