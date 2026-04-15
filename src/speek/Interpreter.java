package speek;

import java.util.List;

/**
 * Interpreter — the pipeline that connects Tokenizer → Parser → Execution.
 * Call run(sourceCode) with a SPEEK program as a String to execute it.
 */
public class Interpreter {

    public void run(String sourceCode) {
        // Step 1: Tokenize
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

        // Step 2: Parse
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // Step 3: Execute
        Environment env = new Environment();
        for (Instruction instr : instructions) {
            instr.execute(env);
        }
    }
}
