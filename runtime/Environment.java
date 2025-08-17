import java.util.*;

/**
 * Environment
 * Runtime symbol table with lexical scoping.
 */
final class Environment {
    final Environment parent;

    final Map<String,Object> vals = new HashMap<>();

    Environment(){ this.parent = null; }

    Environment(Environment p){ this.parent = p; }

    void define(String name, Object v){ vals.put(name, v); }


    void assign(String name, Object v){
        if (vals.containsKey(name)) { vals.put(name, v); return; }
        if (parent != null) { parent.assign(name, v); return; }
        throw new RuntimeException("Undefined variable " + name);
    }


    Object get(String name){
        if (vals.containsKey(name)) return vals.get(name);
        if (parent != null) return parent.get(name);
        throw new RuntimeException("Undefined variable " + name);
    }
}
