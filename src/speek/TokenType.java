package speek;

public enum TokenType {
    // Keywords
    LET,
    BE,
    SAY,
    IF,
    IS,
    GREATER,
    THAN,
    THEN,
    REPEAT,
    TIMES,

    // Literal types
    NUMBER,
    STRING,
    IDENTIFIER,

    // Arithmetic operators
    PLUS,
    MINUS,
    STAR,
    SLASH,

    // Comparison operators
    GREATER_THAN,   // >
    LESS_THAN,      // <
    EQUALS_EQUALS,  // ==

    // Structure
    NEWLINE,
    INDENT,
    DEDENT,
    EOF
}
