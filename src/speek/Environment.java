package speek;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment — the variable store shared across all instructions during a run.
 * Maps variable names to their current values (Double or String).
 */
public class Environment {
    // Maps variable name → its current value (Double or String)
    private final Map<String, Object> store = new HashMap<>();

    /** Store or update a variable. */
    public void set(String name, Object value) {
        store.put(name, value);
    }

    /**
     * Retrieve a variable's value.
     * Throws a clear error if the variable was never defined.
     */
    public Object get(String name) {
        if (!store.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return store.get(name);
    }
}
