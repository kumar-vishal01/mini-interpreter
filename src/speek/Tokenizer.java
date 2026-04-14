package speek;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int pos;        // current position in source
    private int line;       // current line number (for error messages)

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char c = source.charAt(pos);

            // --- Skip spaces and tabs (but NOT newlines) ---
            if (c == ' ' || c == '\t' || c == '\r') {
                pos++;
                continue;
            }

            // --- Newline ---
            if (c == '\n') {
                // Only add NEWLINE if the last token wasn't also a NEWLINE
                // (avoids blank-line noise)
                if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).getType() != TokenType.NEWLINE) {
                    tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                }
                line++;
                pos++;
                continue;
            }

            // --- Single-character operators ---
            if (c == '+') { tokens.add(new Token(TokenType.PLUS,  "+", line)); pos++; continue; }
            if (c == '-') { tokens.add(new Token(TokenType.MINUS, "-", line)); pos++; continue; }
            if (c == '*') { tokens.add(new Token(TokenType.STAR,  "*", line)); pos++; continue; }
            if (c == '/') { tokens.add(new Token(TokenType.SLASH, "/", line)); pos++; continue; }
            if (c == '>') { tokens.add(new Token(TokenType.GREATER_THAN, ">", line)); pos++; continue; }
            if (c == '<') { tokens.add(new Token(TokenType.LESS_THAN,    "<", line)); pos++; continue; }

            // --- == operator ---
            if (c == '=' && pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                tokens.add(new Token(TokenType.EQUALS_EQUALS, "==", line));
                pos += 2;
                continue;
            }

            // --- String literal: "hello" ---
            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            // --- Number literal: 10, 3.14 ---
            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            // --- Keyword or identifier ---
            if (Character.isLetter(c) || c == '_') {
                tokens.add(readWord());
                continue;
            }

            // --- Unknown character: skip with warning ---
            System.err.println("Warning: unknown character '" + c + "' on line " + line + " — skipping.");
            pos++;
        }

        // Always end with EOF
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    // -------------------------------------------------------------------------
    // Helper: read a quoted string token
    // -------------------------------------------------------------------------
    private Token readString() {
        int startLine = line;
        pos++; // skip opening "
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && source.charAt(pos) != '"') {
            sb.append(source.charAt(pos));
            pos++;
        }
        pos++; // skip closing "
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    // -------------------------------------------------------------------------
    // Helper: read a number token (integer or decimal)
    // -------------------------------------------------------------------------
    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    // -------------------------------------------------------------------------
    // Helper: read a keyword or identifier
    // SPEEK multi-word keyword: "is greater than" (3 separate tokens that the
    // Parser will combine). We tokenize each word individually.
    // -------------------------------------------------------------------------
    private Token readWord() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && (Character.isLetterOrDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        String word = sb.toString();
        TokenType type = keyword(word);
        return new Token(type, word, startLine);
    }

    // -------------------------------------------------------------------------
    // Map a word to its keyword type, or IDENTIFIER if it isn't a keyword
    // -------------------------------------------------------------------------
    private TokenType keyword(String word) {
        switch (word) {
            case "let":     return TokenType.LET;
            case "be":      return TokenType.BE;
            case "say":     return TokenType.SAY;
            case "if":      return TokenType.IF;
            case "is":      return TokenType.IS;
            case "greater": return TokenType.GREATER;
            case "than":    return TokenType.THAN;
            case "then":    return TokenType.THEN;
            case "repeat":  return TokenType.REPEAT;
            case "times":   return TokenType.TIMES;
            default:        return TokenType.IDENTIFIER;
        }
    }
}
