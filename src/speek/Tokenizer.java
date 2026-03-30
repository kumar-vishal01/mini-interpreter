package speek;

// This is the final Tokenizer class that will be used to convert the input source code into Tokens.

// First build skeleton 
public class Tokenizer {
    private final String source;
    private int pos;
    private int line;

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    // Now we will run loop on the source 
    // Do further things
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while ( pos < source.length()){
            char c = source.charAt(pos);

            if( c == ' ' || c == '\t' || c == '\r'){
                pos++;
                continue;
            }

            if (Character.isLetter(c) || c == '_'){
                tokens.add(readWord());
                continue;
            }

          pos++;  
            
        }
        return tokens;
    }


    private Token readWord(){
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && (Character.isLetterOrDigit(source.charAt(pos)) ||source.charAt(pos) == '_')){
            sb.append(source.charAt(pos));
            pos++;
        }

        String word = sb.toString();
        TokenType type = keyword(word);

        return new Token(type, word, startLine);
    }



    private TokenType keyword(String word){
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
