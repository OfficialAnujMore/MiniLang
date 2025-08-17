import java.util.*;

/**
 * Lexer for MiniLang.
 * Transforms a source string into a list of tokens.
 * Each token carries its kind, lexeme, and source coordinates (line, col).
 */
final class Lexer {
    /** Entire source program as a single string. */
    private final String src;
    private int i = 0, line = 1, col = 1;

    
    private final Map<String,Kind> keywords = Map.of(
        "var", Kind.VAR, "if", Kind.IF, "else", Kind.ELSE,
        "while", Kind.WHILE, "print", Kind.PRINT,
        "true", Kind.TRUE, "false", Kind.FALSE
    );

    Lexer(String s){ this.src = s; }

    List<Token> lex() {
        List<Token> out = new ArrayList<>();

        while (!eof()) {
            char c = peek();

            // Skip spaces, tabs, carriage returns
            if (c == ' ' || c == '\t' || c == '\r') { advance(); continue; }

            // Newline: advance line counter and reset column
            if (c == '\n') { advanceLine(); continue; }

            // Line comment: consume until end of line
            if (c == '/' && peek2() == '/') {
                while(!eof() && peek() != '\n') advance();
                continue;
            }

            // Snapshot current position for the token we’re about to emit
            int l = line, c0 = col;

            // Single and double character punctuation/operators
            switch (c) {
                case '{': out.add(tok(Kind.LBRACE, "{", l, c0)); advance(); break;
                case '}': out.add(tok(Kind.RBRACE, "}", l, c0)); advance(); break;
                case '(': out.add(tok(Kind.LPAREN, "(", l, c0)); advance(); break;
                case ')': out.add(tok(Kind.RPAREN, ")", l, c0)); advance(); break;
                case ';': out.add(tok(Kind.SEMI,   ";", l, c0)); advance(); break;
                case ',': out.add(tok(Kind.COMMA,  ",", l, c0)); advance(); break;
                case '+': out.add(tok(Kind.PLUS,   "+", l, c0)); advance(); break;
                case '-': out.add(tok(Kind.MINUS,  "-", l, c0)); advance(); break;
                case '*': out.add(tok(Kind.STAR,   "*", l, c0)); advance(); break;
                case '/': out.add(tok(Kind.SLASH,  "/", l, c0)); advance(); break;
                case '%': out.add(tok(Kind.PERCENT,"%", l, c0)); advance(); break;

                // '!' or '!='
                case '!':
                    if (peek2() == '=') { out.add(tok(Kind.BANGEQ, "!=", l, c0)); advance(); advance(); }
                    else { out.add(tok(Kind.BANG, "!", l, c0)); advance(); }
                    break;

                // '=' or '=='
                case '=':
                    if (peek2() == '=') { out.add(tok(Kind.EQEQ, "==", l, c0)); advance(); advance(); }
                    else { out.add(tok(Kind.EQ, "=", l, c0)); advance(); }
                    break;

                // '<' or '<='
                case '<':
                    if (peek2() == '=') { out.add(tok(Kind.LE, "<=", l, c0)); advance(); advance(); }
                    else { out.add(tok(Kind.LT, "<", l, c0)); advance(); }
                    break;

                // '>' or '>='
                case '>':
                    if (peek2() == '=') { out.add(tok(Kind.GE, ">=", l, c0)); advance(); advance(); }
                    else { out.add(tok(Kind.GT, ">", l, c0)); advance(); }
                    break;

                // '&&'
                case '&':
                    if (peek2() == '&') { out.add(tok(Kind.ANDAND, "&&", l, c0)); advance(); advance(); }
                    else throw error("Unexpected '&'");
                    break;

                // '||'
                case '|':
                    if (peek2() == '|') { out.add(tok(Kind.OROR, "||", l, c0)); advance(); advance(); }
                    else throw error("Unexpected '|'");
                    break;

                default:
                    if (c == '\'' || c == '\"') {
                        char quote = c;
                        advance();                 
                        int start = i;
                        while (!eof() && peek() != quote) {
                            if (peek() == '\n') throw error("Unterminated string");
                            advance();
                        }
                        if (eof()) throw error("Unterminated string");
                        String content = src.substring(start, i);
                        advance();
                        out.add(tok(Kind.STRING, content, l, c0));
                        break;
                    }

                    if (Character.isDigit(c)) {
                        int start = i;
                        while(!eof() && Character.isDigit(peek())) advance();
                        out.add(tok(Kind.NUMBER, src.substring(start, i), l, c0));
                    }
                    else if (Character.isLetter(c) || c == '_') {
                        int start = i;
                        while(!eof() && (Character.isLetterOrDigit(peek()) || peek() == '_')) advance();
                        String w = src.substring(start, i);
                        Kind k = keywords.getOrDefault(w, Kind.IDENT);
                        out.add(tok(k, w, l, c0));
                    }
                    else {
                        throw error("Unexpected char " + c);
                    }
            }
        }

        // Always terminate the stream with EOF
        out.add(tok(Kind.EOF, "", line, col));
        return out;
    }

    
    private boolean eof(){ return i >= src.length(); }
    
    private char peek(){ return src.charAt(i); }

    private char peek2(){ return i + 1 < src.length() ? src.charAt(i + 1) : '\0'; }
    
    private void advance(){ i++; col++; }

    private void advanceLine(){ i++; line++; col = 1; }

    private Token tok(Kind k, String s, int l, int c){ return new Token(k, s, l, c); }

    private RuntimeException error(String msg){
        return new RuntimeException("Lex error " + msg + " at " + line + ":" + col);
    }
}
