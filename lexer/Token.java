/**
 * Token
 * Immutable lexical unit produced by the lexer. Acts as the contract between scanning and parsing.
 * Carries kind, exact source text, and 1-based coordinates for precise diagnostics.
 */
final class Token {
    /** Discriminator of the token’s role in the grammar, e.g., IDENT, NUMBER, PLUS. */
    final Kind kind;

    /** Exact substring from the source. Useful for error messages and re-printing. */
    final String lexeme;

    /** 1-based source coordinates enabling pinpoint error reporting. */
    final int line, col;

    /** Lightweight value constructor. No validation needed thanks to upstream lexer guarantees. */
    Token(Kind k, String s, int l, int c) {
        kind = k;
        lexeme = s;
        line = l;
        col = c;
    }

    /** Debug-friendly rendering used in traces and diagnostics. */
    @Override
    public String toString() {
        return kind + "(" + lexeme + ")@" + line + ":" + col;
    }
}
