import java.nio.file.*;
import java.util.*;

/**
 * MiniLang
 * Orchestrates the end-to-end pipeline:
 * 1. Ingest source code from a file
 * 2. Lex into tokens
 * 3. Parse into an AST
 * 4. Execute via the interpreter
 */
public class MiniLang {

    /**
     * Main entry point.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.exit(1);
        }

        // Load the entire source file as a single string
        String source = Files.readString(Path.of(args[0]));

        // Convert raw characters into a typed token stream
        List<Token> tokens = new Lexer(source).lex();

        // Transform tokens into an abstract syntax tree (AST)
        Parser parser = new Parser(tokens);
        List<Stmt> program = parser.parse();

        // Evaluate the Abstract Syntax Tree (AST) using a scoped runtime environment
        Interpreter interp = new Interpreter();
        interp.execute(program);
    }
}
