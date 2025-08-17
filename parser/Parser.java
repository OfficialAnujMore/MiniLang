import java.util.*;

/**
 * Recursive-descent parser for MiniLang.
 * Consumes a token stream and produces an AST:
 * Implements operator precedence by factoring expression rules
 *   from low precedence (or) down to primaries.
 */
final class Parser {
    private final List<Token> t;  
    private int i = 0;           
    Parser(List<Token> tokens){ this.t = tokens; }

    /**
     * Entry point for parsing a complete program.
     */
    List<Stmt> parse(){
        List<Stmt> out = new ArrayList<>();
        while(!at(Kind.EOF)){
            out.add(statement());
        }
        return out;
    }

    private Stmt statement(){
        if (match(Kind.LBRACE)) {
            List<Stmt> stmts = new ArrayList<>();
            while(!at(Kind.RBRACE)) stmts.add(statement());
            expect(Kind.RBRACE, "Expected }");
            return new Block(stmts);
        }

        if (match(Kind.VAR)) return vardecl();

        if (match(Kind.IF)) return ifstmt();

        if (match(Kind.WHILE)) return whilestmt();

        if (match(Kind.PRINT)) return printstmt();

        
        Token name = expect(Kind.IDENT, "Expected identifier");
        expect(Kind.EQ, "Expected =");
        Expr v = expr();
        expect(Kind.SEMI, "Expected ;");
        return new Assign(name.lexeme, v);
    }

    private Stmt vardecl(){
        Token name = expect(Kind.IDENT, "Expected identifier");
        Expr init;
        if (match(Kind.EQ)) init = expr(); else init = new Literal(0);
        expect(Kind.SEMI, "Expected ;");
        return new VarDecl(name.lexeme, init);
    }

    private Stmt ifstmt(){
        expect(Kind.LPAREN, "Expected (");
        Expr c = expr();
        expect(Kind.RPAREN, "Expected )");
        Stmt t0 = statement();
        Stmt e0 = null;
        if (match(Kind.ELSE)) e0 = statement();
        return new If(c, t0, e0);
    }

    private Stmt whilestmt(){
        expect(Kind.LPAREN, "Expected (");
        Expr c = expr();
        expect(Kind.RPAREN, "Expected )");
        Stmt b = statement();
        return new While(c, b);
    }

    private Stmt printstmt(){
        expect(Kind.LPAREN, "Expected (");
        List<Expr> args = new ArrayList<>();
        if (!at(Kind.RPAREN)) {
            args.add(expr());
            while (match(Kind.COMMA)) args.add(expr());
        }
        expect(Kind.RPAREN, "Expected )");
        expect(Kind.SEMI, "Expected ;");
        return new Print(args);
    }

    private Expr expr(){ return or(); }

    private Expr or(){
        Expr e = and();
        while(match(Kind.OROR)) e = new Binary(e, "||", and());
        return e;
    }

    private Expr and(){
        Expr e = equality();
        while(match(Kind.ANDAND)) e = new Binary(e, "&&", equality());
        return e;
    }

    private Expr equality(){
        Expr e = comparison();
        while(match(Kind.EQEQ, Kind.BANGEQ)){
            String op = prev().lexeme;
            e = new Binary(e, op, comparison());
        }
        return e;
    }

    private Expr comparison(){
        Expr e = term();
        while(match(Kind.LT, Kind.LE, Kind.GT, Kind.GE)){
            String op = prev().lexeme;
            e = new Binary(e, op, term());
        }
        return e;
    }

    private Expr term(){
        Expr e = factor();
        while(match(Kind.PLUS, Kind.MINUS)){
            String op = prev().lexeme;
            e = new Binary(e, op, factor());
        }
        return e;
    }

    private Expr factor(){
        Expr e = unary();
        while(match(Kind.STAR, Kind.SLASH, Kind.PERCENT)){
            String op = prev().lexeme;
            e = new Binary(e, op, unary());
        }
        return e;
    }

    private Expr unary(){
        if (match(Kind.BANG) || match(Kind.MINUS)){
            String op = prev().lexeme;
            return new Unary(op, unary()); 
        }
        return primary();
    }

    private Expr primary(){
        if (match(Kind.NUMBER)) return new Literal(Integer.parseInt(prev().lexeme));
        if (match(Kind.TRUE))   return new Literal(true);
        if (match(Kind.FALSE))  return new Literal(false);
        if (match(Kind.IDENT))  return new Variable(prev().lexeme);
        if (match(Kind.STRING)) return new Literal(prev().lexeme);
        if (match(Kind.LPAREN)){
            Expr e = expr();
            expect(Kind.RPAREN, "Expected )");
            return e;
        }
        Token t0 = peek();
        throw new RuntimeException(err("Expected expression", t0));
    }

    private boolean at(Kind k){ return peek().kind == k; }

    private boolean match(Kind... kinds){
        for (Kind k: kinds){
            if (at(k)){ i++; return true; }
        }
        return false;
    }

    private Token expect(Kind k, String msg){
        if (!at(k)) throw new RuntimeException(err(msg, peek()));
        return t.get(i++);
    }

    private Token prev(){ return t.get(i - 1); }

    private Token peek(){ return t.get(i); }

    private String err(String msg, Token tok){
        return msg + " at " + tok.line + ":" + tok.col + " found " + tok.kind;
    }
}
