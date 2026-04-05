package speek;

public class VariableNode implements Expression {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        // Delegate to Environment — it will throw if the variable is undefined
        return env.get(name);
    }
    

    
}
