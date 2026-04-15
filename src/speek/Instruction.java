package speek;

/**
 * Instruction — one complete executable statement.
 */
public interface Instruction {
    void execute(Environment env);
}
