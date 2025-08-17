import java.util.*;

/**
 * Marker interface for all statement nodes.
 * Enables polymorphic dispatch over the statement space without inheritance chains.
 */
interface Stmt { }

/**
 * Marker interface for all expression nodes.
 * Keeps the expression hierarchy open for future node types (calls, functions, etc.).
 */
interface Expr { }

/**
 * Introduces a new scope and holds an ordered list of statements.

 */
final class Block implements Stmt {
    final List<Stmt> stmts;
    Block(List<Stmt> s) { stmts = s; }
}

/**
 * Variable declaration with optional initialization.
 */
final class VarDecl implements Stmt {
    final String name;
    final Expr init;

    VarDecl(String n, Expr e) { name = n; init = e; }
}

/**
 * Assignment to an existing variable.
 */
final class Assign implements Stmt {
    final String name;
    final Expr value;

    Assign(String n, Expr v) { name = n; value = v; }
}

/**
 * Conditional control flow with optional else branch.
 */
final class If implements Stmt {
    final Expr cond;
    final Stmt thenB;
    final Stmt elseB;

    If(Expr c, Stmt t, Stmt e) { cond = c; thenB = t; elseB = e; }
}

/**
 * While loop with a condition checked before each iteration.
 */
final class While implements Stmt {
    final Expr cond;
    final Stmt body;

    While(Expr c, Stmt b) { cond = c; body = b; }
}

/**
 * Print side effect for debugging and user output.
 */
final class Print implements Stmt {
    final List<Expr> values;
    Print(List<Expr> v){ values = v; }
}

/**
 * Literal constant value: integers or booleans in the current language surface.
 */
final class Literal implements Expr {
    final Object value;

    Literal(Object v) { value = v; }
}

/**
 * Variable reference expression.
 */
final class Variable implements Expr {
    final String name;

    Variable(String n) { name = n; }
}

/**
 * Unary operator expression.
 */
final class Unary implements Expr {
    final String op;
    final Expr right;

    Unary(String o, Expr r) { op = o; right = r; }
}

/**
 * Binary operator expression with left and right operands.
 */
final class Binary implements Expr {
    final Expr left;
    final String op;
    final Expr right;

    Binary(Expr l, String o, Expr r) { left = l; op = o; right = r; }
}
