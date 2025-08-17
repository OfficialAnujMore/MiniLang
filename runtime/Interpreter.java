import java.util.*;

/**
 * Interpreter
 * Executes MiniLang by walking the AST with a lexical-scope environment chain.
 */
final class Interpreter {
    
    private Environment env = new Environment();


    void execute(List<Stmt> stmts){
        for (Stmt s: stmts) exec(s);
    }

    
    private void exec(Stmt s){
        if (s instanceof Block b) {
            Environment prev = env;
            env = new Environment(prev);
            try {
                for (Stmt st: b.stmts) exec(st);
            } finally {
                env = prev;
            }
            return;
        }

        
        if (s instanceof VarDecl vd) {
            Object v = eval(vd.init);
            env.define(vd.name, v);
            return;
        }

        
        if (s instanceof Assign as) {
            Object v = eval(as.value);
            env.assign(as.name, v);
            return;
        }

        
        if (s instanceof If iff) {
            boolean c = asBool(eval(iff.cond), "if condition");
            if (c) exec(iff.thenB);
            else if (iff.elseB != null) exec(iff.elseB);
            return;
        }

        
        if (s instanceof While w) {
            while (asBool(eval(w.cond), "while condition")) {
                exec(w.body);
            }
            return;
        }

        if (s instanceof Print p) {
            boolean first = true;
            for (Expr ex : p.values) {
                if (!first) System.out.print(" ");
                System.out.print(stringify(eval(ex)));
                first = false;
            }
            System.out.println();
            return;
        }

        throw new RuntimeException("Unknown statement " + s.getClass());
    }

    
    private Object eval(Expr e){

        if (e instanceof Literal l) return l.value;

        if (e instanceof Variable v) return env.get(v.name);

        if (e instanceof Unary u) {
            Object r = eval(u.right);
            return switch(u.op){
                case "!" -> !asBool(r, "logical not");
                case "-" -> -asInt(r, "unary minus");
                default -> throw new RuntimeException("Unknown unary " + u.op);
            };
        }

        if (e instanceof Binary b) {
            if (b.op.equals("&&")) {
                boolean left = asBool(eval(b.left), "&& left");
                return left && asBool(eval(b.right), "&& right");
            }
            if (b.op.equals("||")) {
                boolean left = asBool(eval(b.left), "|| left");
                return left || asBool(eval(b.right), "|| right");
            }

            Object L = eval(b.left);
            Object R = eval(b.right);

            return switch(b.op){
                case "+"  -> asInt(L,"+ left")  + asInt(R,"+ right");
                case "-"  -> asInt(L,"- left")  - asInt(R,"- right");
                case "*"  -> asInt(L,"* left")  * asInt(R,"* right");
                case "/"  -> asInt(L,"/ left")  / asInt(R,"/ right");
                case "%"  -> asInt(L,"% left")  % asInt(R,"% right");

                case "<"  -> asInt(L,"< left")  <  asInt(R,"< right");
                case "<=" -> asInt(L,"<= left") <= asInt(R,"<= right");
                case ">"  -> asInt(L,"> left")  >  asInt(R,"> right");
                case ">=" -> asInt(L,">= left") >= asInt(R,">= right");

                case "==" -> Objects.equals(L, R);
                case "!=" -> !Objects.equals(L, R);

                default   -> throw new RuntimeException("Unknown operator " + b.op);
            };
        }

        throw new RuntimeException("Unknown expr " + e.getClass());
    }


    private int asInt(Object v, String ctx){
        if (v instanceof Integer i) return i;
        throw new RuntimeException("Type error expected int in " + ctx + " got " + v);
    }

    private boolean asBool(Object v, String ctx){
        if (v instanceof Boolean b) return b;
        throw new RuntimeException("Type error expected bool in " + ctx + " got " + v);
    }

    
    private String stringify(Object v){ return String.valueOf(v); }
}
