package speek;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser — reads the List&lt;Token&gt; from the Tokenizer and builds a List&lt;Instruction&gt;.
 *
 * Operator precedence is enforced by the three-level call chain:
 *   parseExpression()  handles + and -  (lowest priority)
 *   parseTerm()        handles * and /  (higher priority)
 *   parsePrimary()     handles a single value (highest priority)
 *
 * Nested blocks are supported using indentation (line numbers):
 * body instructions must appear on lines strictly after the header line.
 */
public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens  = tokens;
        this.current = 0;
    }

    // ── Public entry point ────────────────────────────────────────────────────
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();
        while (!isAtEnd()) {
            skipNewlines();
            if (isAtEnd()) break;
            Instruction instr = parseInstruction();
            if (instr != null) instructions.add(instr);
        }
        return instructions;
    }

    // ── Instruction dispatch ──────────────────────────────────────────────────
    private Instruction parseInstruction() {
        Token t = peek();
        switch (t.getType()) {
            case LET:    return parseLet();
            case SAY:    return parseSay();
            case IF:     return parseIf();
            case REPEAT: return parseRepeat();
            default:
                throw new RuntimeException(
                    "Unexpected token '" + t.getValue() +
                    "' (type " + t.getType() + ") on line " + t.getLine());
        }
    }

    // ── let <name> be <expr> ──────────────────────────────────────────────────
    private AssignInstruction parseLet() {
        consume(TokenType.LET);
        String name = consume(TokenType.IDENTIFIER).getValue();
        consume(TokenType.BE);
        Expression expr = parseExpression();
        expectNewlineOrEOF();
        return new AssignInstruction(name, expr);
    }

    // ── say <expr> ────────────────────────────────────────────────────────────
    private PrintInstruction parseSay() {
        consume(TokenType.SAY);
        Expression expr = parseExpression();
        expectNewlineOrEOF();
        return new PrintInstruction(expr);
    }

    // ── if <left> is greater than <right> then  /  if <left> > <right> then ──
    private IfInstruction parseIf() {
        consume(TokenType.IF);
        Expression left = parseExpression();
        Expression condition;

        if (check(TokenType.IS)) {
            consume(TokenType.IS);
            consume(TokenType.GREATER);
            consume(TokenType.THAN);
            Expression right = parseExpression();
            condition = new BinaryOpNode(left, ">", right);
        } else if (check(TokenType.GREATER_THAN)) {
            advance();
            Expression right = parseExpression();
            condition = new BinaryOpNode(left, ">", right);
        } else if (check(TokenType.LESS_THAN)) {
            advance();
            Expression right = parseExpression();
            condition = new BinaryOpNode(left, "<", right);
        } else if (check(TokenType.EQUALS_EQUALS)) {
            advance();
            Expression right = parseExpression();
            condition = new BinaryOpNode(left, "==", right);
        } else {
            Token t = peek();
            throw new RuntimeException(
                "Expected comparison operator after expression on line " + t.getLine() +
                ", but found: '" + t.getValue() + "'");
        }

        consume(TokenType.THEN);
        expectNewlineOrEOF();
        List<Instruction> body = parseBody();
        return new IfInstruction(condition, body);
    }

    // ── repeat <n> times ──────────────────────────────────────────────────────
    private RepeatInstruction parseRepeat() {
        consume(TokenType.REPEAT);
        Token countToken = consume(TokenType.NUMBER);
        int count = (int) Double.parseDouble(countToken.getValue());
        consume(TokenType.TIMES);
        expectNewlineOrEOF();
        List<Instruction> body = parseBody();
        return new RepeatInstruction(count, body);
    }

    /**
     * Parse the indented body of an if/repeat block.
     *
     * Strategy (using INDENT/DEDENT tokens):
     *  - Expect an INDENT token.
     *  - Collect instructions until DEDENT or EOF.
     *  - Consume DEDENT token.
     */
    private List<Instruction> parseBody() {
        List<Instruction> body = new ArrayList<>();

        skipNewlines();
        if (isAtEnd()) return body;

        if (check(TokenType.INDENT)) {
            consume(TokenType.INDENT);
            while (!isAtEnd() && !check(TokenType.DEDENT)) {
                skipNewlines();
                if (isAtEnd() || check(TokenType.DEDENT)) break;

                Instruction instr = parseInstruction();
                if (instr != null) body.add(instr);
            }
            if (check(TokenType.DEDENT)) consume(TokenType.DEDENT);
        }

        return body;
    }


    // ── Expression parsing (three-level precedence) ───────────────────────────

    /** Handles + and - (lowest precedence). */
    private Expression parseExpression() {
        Expression expr = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = advance().getValue();
            Expression right = parseTerm();
            expr = new BinaryOpNode(expr, op, right);
        }
        return expr;
    }

    /** Handles * and / (higher than + and -). */
    private Expression parseTerm() {
        Expression expr = parsePrimary();
        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            String op = advance().getValue();
            Expression right = parsePrimary();
            expr = new BinaryOpNode(expr, op, right);
        }
        return expr;
    }

    /** Handles a single literal or variable (highest precedence). */
    private Expression parsePrimary() {
        Token t = peek();

        if (t.getType() == TokenType.NUMBER) {
            advance();
            return new NumberNode(Double.parseDouble(t.getValue()));
        }
        if (t.getType() == TokenType.STRING) {
            advance();
            return new StringNode(t.getValue());
        }
        if (t.getType() == TokenType.IDENTIFIER) {
            advance();
            return new VariableNode(t.getValue());
        }

        throw new RuntimeException(
            "Expected a number, string, or variable name, " +
            "but found '" + t.getValue() + "' on line " + t.getLine());
    }

    // ── Utility helpers ───────────────────────────────────────────────────────

    private Token peek()              { return tokens.get(current); }
    private Token advance()           { return tokens.get(current++); }
    private boolean check(TokenType t){ return !isAtEnd() && peek().getType() == t; }
    private boolean isAtEnd()         { return peek().getType() == TokenType.EOF; }

    private Token consume(TokenType expected) {
        if (check(expected)) return advance();
        Token t = peek();
        throw new RuntimeException(
            "Expected " + expected + " but found '" + t.getValue() +
            "' (type " + t.getType() + ") on line " + t.getLine());
    }

    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) advance();
    }

    private void expectNewlineOrEOF() {
        if (check(TokenType.NEWLINE)) advance();
    }
}
