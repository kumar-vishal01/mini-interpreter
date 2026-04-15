package speek;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment — the variable store shared across all instructions during a run.
 * Maps variable names to their current values (Double or String).
 */
public class Environment {
    private final Map<String, Object> store = new HashMap<>();

    public void set(String name, Object value) {
        store.put(name, value);
    }

    public Object get(String name) {
        if (!store.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return store.get(name);
    }
}
