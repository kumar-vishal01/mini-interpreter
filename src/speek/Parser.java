package speek;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current;   // index of the token we're looking at right now

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Public entry point
    // ─────────────────────────────────────────────────────────────────────────
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        while (!isAtEnd()) {
            skipNewlines();
            if (isAtEnd()) break;

            Instruction instr = parseInstruction();
            if (instr != null) {
                instructions.add(instr);
            }
        }
        return instructions;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Instruction dispatch — looks at the current token to decide what to parse
    // ─────────────────────────────────────────────────────────────────────────
    private Instruction parseInstruction() {
        Token t = peek();

        switch (t.getType()) {
            case LET:    return parseLet();
            case SAY:    return parseSay();
            case IF:     return parseIf();
            case REPEAT: return parseRepeat();
            default:
                throw new RuntimeException(
                    "Unexpected token '" + t.getValue() + "' on line " + t.getLine());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  let x be <expr>
    // ─────────────────────────────────────────────────────────────────────────
    private AssignInstruction parseLet() {
        consume(TokenType.LET);                          // eat "let"
        String name = consume(TokenType.IDENTIFIER).getValue();  // variable name
        consume(TokenType.BE);                           // eat "be"
        Expression expr = parseExpression();             // right-hand side
        expectNewlineOrEOF();
        return new AssignInstruction(name, expr);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  say <expr>
    // ─────────────────────────────────────────────────────────────────────────
    private PrintInstruction parseSay() {
        consume(TokenType.SAY);
        Expression expr = parseExpression();
        expectNewlineOrEOF();
        return new PrintInstruction(expr);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  if <expr> is greater than <expr> then
    //      <body>
    //
    //  The body is every indented line that follows.
    //  We use a simple rule: lines after "then" that start with whitespace,
    //  OR we collect until we see a non-indented keyword or EOF.
    //  Actually — since we're working with tokens and newlines, we collect
    //  until we hit a top-level keyword (LET/SAY/IF/REPEAT) or EOF.
    // ─────────────────────────────────────────────────────────────────────────
    private IfInstruction parseIf() {
        consume(TokenType.IF);
        Expression left = parseExpression();

        // "is greater than"
        consume(TokenType.IS);
        consume(TokenType.GREATER);
        consume(TokenType.THAN);

        Expression right = parseExpression();
        Expression condition = new BinaryOpNode(left, ">", right);

        consume(TokenType.THEN);
        expectNewlineOrEOF();

        List<Instruction> body = parseBody();
        return new IfInstruction(condition, body);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  repeat <number> times
    //      <body>
    // ─────────────────────────────────────────────────────────────────────────
    private RepeatInstruction parseRepeat() {
        consume(TokenType.REPEAT);
        Token countToken = consume(TokenType.NUMBER);
        int count = (int) Double.parseDouble(countToken.getValue());
        consume(TokenType.TIMES);
        expectNewlineOrEOF();

        List<Instruction> body = parseBody();
        return new RepeatInstruction(count, body);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Parse the indented body of an if/repeat block.
    //  We read instructions until we see a top-level keyword or EOF.
    //  Top-level keywords are LET, SAY, IF, REPEAT.
    // ─────────────────────────────────────────────────────────────────────────
    private List<Instruction> parseBody() {
        List<Instruction> body = new ArrayList<>();

        skipNewlines();

        if (isAtEnd()) return body;

        int startLine = peek().getLine();  // line where body starts

        while (!isAtEnd()) {

            Token t = peek();

            // STOP if we go back to same or earlier line (block ended)
            if (t.getLine() < startLine) break;

            // Also stop if new top-level instruction starts on new line
            if ((t.getType() == TokenType.LET ||
                t.getType() == TokenType.IF ||
                t.getType() == TokenType.REPEAT) &&
                t.getLine() == startLine) {
                break;
            }


            body.add(parseInstruction());
            skipNewlines();
        }

        return body;
    }

    // A token is a "body" token if it's one of our statement starters
    private boolean isBodyToken(Token t) {
        switch (t.getType()) {
            case LET:
            case SAY:
            case IF:
            case REPEAT:
                return true;
            default:
                return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Expression parsing — 3-level chain enforces operator precedence:
    //
    //  parseExpression  →  handles + and -  (lowest priority)
    //  parseTerm        →  handles * and /  (higher priority)
    //  parsePrimary     →  handles a single value (highest priority)
    //
    //  Because parseTerm is called BEFORE + or - are checked,
    //  * and / always bind tighter.
    // ─────────────────────────────────────────────────────────────────────────

    private Expression parseExpression() {
        Expression expr = parseTerm();   // get left side (may consume * and /)

        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = advance().getValue();   // eat + or -
            Expression right = parseTerm();
            expr = new BinaryOpNode(expr, op, right);
        }
        return expr;
    }

    private Expression parseTerm() {
        Expression expr = parsePrimary();   // get left side

        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            String op = advance().getValue();   // eat * or /
            Expression right = parsePrimary();
            expr = new BinaryOpNode(expr, op, right);
        }
        return expr;
    }

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
            "Expected a number, string, or variable — but got '" +
            t.getValue() + "' on line " + t.getLine());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Utility helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Return the current token without consuming it. */
    private Token peek() {
        return tokens.get(current);
    }

    /** Consume and return the current token. */
    private Token advance() {
        Token t = tokens.get(current);
        current++;
        return t;
    }

    /** Check if the current token has the given type (without consuming). */
    private boolean check(TokenType type) {
        return !isAtEnd() && peek().getType() == type;
    }

    /** Consume the current token — throw a helpful error if it's the wrong type. */
    private Token consume(TokenType expected) {
        if (check(expected)) return advance();
        Token t = peek();
        throw new RuntimeException(
            "Expected " + expected + " but got '" + t.getValue() +
            "' (type " + t.getType() + ") on line " + t.getLine());
    }

    /** Skip over any NEWLINE tokens. */
    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) advance();
    }

    /** After a statement, we expect a NEWLINE or EOF. */
    private void expectNewlineOrEOF() {
        if (check(TokenType.NEWLINE)) {
            advance();
        } else if (!isAtEnd()) {
            // tolerate missing newline at end of file
        }
    }

    /** Are we at the end of the token list? */
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }
}
