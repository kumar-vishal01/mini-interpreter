package speek;

public class Token {

    private final TokenType type;
    private final String value;
    private final int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getToken() { return type; }
    public String getValue() { return value;}
    public int getline() {return line;}

}