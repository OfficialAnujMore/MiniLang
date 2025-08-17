/**
 * Canonical set of token kinds recognized by the MiniLang lexer.
 * Organized by category for fast scanning and easier future expansion.
 */
enum Kind {

    // Delimiters and punctuation
    LBRACE,   
    RBRACE, 
    LPAREN, 
    RPAREN, 
    SEMI,   
    COMMA,  

    // Single-char operators
    PLUS,     
    MINUS,  
    STAR,   
    SLASH, 
    PERCENT,
    BANG,  
    EQ,     
    LT,     
    GT,     

    // Double-char operators
    EQEQ,  
    BANGEQ,
    LE,    
    GE,     
    ANDAND, 
    OROR,   

    // Literals, identifiers, and keywords
    NUMBER,
    IDENT, 
    TRUE,  
    FALSE, 
    VAR,   
    IF,    
    ELSE,   
    WHILE,  
    PRINT,  
    STRING,

    // Sentinel marking end of input
    EOF
}
