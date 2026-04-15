package speek;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Tokenizer — reads SPEEK source code character by character and produces
 * a flat List<Token>. The last token is always EOF.
 *
 * Modified to support INDENT and DEDENT tokens.
 */
public class Tokenizer {
    private final String source;
    private int pos;
    private int line;
    private Stack<Integer> indentStack;

    public Tokenizer(String source) {
        this.source = source;
        this.pos    = 0;
        this.line   = 1;
        this.indentStack = new Stack<>();
        this.indentStack.push(0);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        boolean atLineStart = true;

        while (pos < source.length()) {
            if (atLineStart) {
                int spaces = 0;
                while (pos < source.length() && (source.charAt(pos) == ' ' || source.charAt(pos) == '\t')) {
                    if (source.charAt(pos) == '\t') spaces += 4;
                    else spaces += 1;
                    pos++;
                }

                if (pos < source.length() && (source.charAt(pos) == '\n' || source.charAt(pos) == '\r')) {
                    // Blank line, ignore indentation
                    atLineStart = false; // It will hit \n or \r in the main loop
                    continue;
                }

                if (pos >= source.length()) break;

                if (spaces > indentStack.peek()) {
                    indentStack.push(spaces);
                    tokens.add(new Token(TokenType.INDENT, "", line));
                } else if (spaces < indentStack.peek()) {
                    while (!indentStack.isEmpty() && spaces < indentStack.peek()) {
                        indentStack.pop();
                        tokens.add(new Token(TokenType.DEDENT, "", line));
                    }
                    if (indentStack.isEmpty() || spaces != indentStack.peek()) {
                        System.err.println("Warning: Indentation error on line " + line);
                    }
                }
                atLineStart = false;
            }

            char c = source.charAt(pos);

            if (c == ' ' || c == '\t' || c == '\r') { pos++; continue; }

            if (c == '\n') {
                if (!tokens.isEmpty() &&
                    tokens.get(tokens.size() - 1).getType() != TokenType.NEWLINE) {
                    tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                }
                line++;
                pos++;
                atLineStart = true;
                continue;
            }

            if (c == '+') { tokens.add(new Token(TokenType.PLUS,  "+", line)); pos++; continue; }
            if (c == '-') { tokens.add(new Token(TokenType.MINUS, "-", line)); pos++; continue; }
            if (c == '*') { tokens.add(new Token(TokenType.STAR,  "*", line)); pos++; continue; }
            if (c == '/') { tokens.add(new Token(TokenType.SLASH, "/", line)); pos++; continue; }

            if (c == '>') { tokens.add(new Token(TokenType.GREATER_THAN, ">", line)); pos++; continue; }
            if (c == '<') { tokens.add(new Token(TokenType.LESS_THAN,    "<", line)); pos++; continue; }

            if (c == '=' && pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                tokens.add(new Token(TokenType.EQUALS_EQUALS, "==", line));
                pos += 2;
                continue;
            }

            if (c == '"') { tokens.add(readString()); continue; }
            if (Character.isDigit(c)) { tokens.add(readNumber()); continue; }
            if (Character.isLetter(c) || c == '_') { tokens.add(readWord()); continue; }

            System.err.println("Warning: unknown character '" + c + "' on line " + line + " — skipping.");
            pos++;
        }

        while (indentStack.size() > 1) {
            indentStack.pop();
            tokens.add(new Token(TokenType.DEDENT, "", line));
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private Token readString() {
        int startLine = line;
        pos++;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && source.charAt(pos) != '"') {
            sb.append(source.charAt(pos));
            pos++;
        }
        if (pos < source.length()) pos++;
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() &&
               (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    private Token readWord() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() &&
               (Character.isLetterOrDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        String word = sb.toString();
        return new Token(keyword(word), word, startLine);
    }

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
