package speek;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestToken {
    public static void main(String[] args) throws Exception {
        // A small Speek program covering various token types
        String code = "let x be 10\n"
                     + "let name be \"hello\"\n"
                     + "say x + 5\n"
                     + "if x > 3 then\n"
                     + "  say x - 1\n"
                     + "repeat 3 times\n"
                     + "  say x * 2 / 1\n"
                     + "if x == 10 then\n"
                     + "  say \"done\"\n";


        String code1 = new String(Files.readAllBytes(Paths.get("src/samples/program2.speek")));

        Tokenizer tokenizer = new Tokenizer(code1);
        List<Token> tokens = tokenizer.tokenize();

        // System.out.println("List of Tokens :\n"+tokens);

        // Print the full list of tokens
        System.out.println("=== Token List ===");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println("[" + i + "] " + tokens.get(i));
        }
        System.out.println("==================");
        System.out.println("Total tokens: " + tokens.size());
    }
}
