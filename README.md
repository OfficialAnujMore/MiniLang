# MiniLang

A lean, **education-first interpreter** implemented in Java. MiniLang showcases the end-to-end language toolchain — **lexing → parsing → AST → interpretation** — with clean architecture and enterprise-ready separation of concerns. It’s purpose-built to demonstrate core concepts from **CPSC 323 – Compilers and Languages** in a way that’s easy to extend, reason about, and demo.

> TL;DR: high signal, low ceremony. Parse programs. Execute them. Teach the stack.

---

## 🚀 Value Proposition

* **Translation principles**: concrete walkthrough from raw source to tokens to AST to runtime.
* **Readable architecture**: modularized into `lexer/`, `parser/`, `ast/`, `runtime/`.
* **Deterministic semantics**: integers + booleans, block scoping, short-circuit logic.
* **Demo-friendly**: tiny surface area, clear diagnostics, runnable samples.

---

## ✨ Feature Matrix

| Capability           | Notes                                                                    |   |              |
| -------------------- | ------------------------------------------------------------------------ | - | ------------ |
| Statements           | `var` declarations, assignment, `if` / `else`, `while`, `print`, `{}`    |   |              |
| Expressions          | Literals, variables, unary `! -`, binary \`+ - \* / % < <= > >= == != && |   | \`           |
| Scoping              | Lexical block scoping via nested `Environment` frames                    |   |              |
| Type Discipline      | Runtime guards for `int` vs `bool` (clear error messages)                |   |              |
| Short-Circuit Logic  | Proper `&&` / \`                                                         |   | \` semantics |
| Developer Ergonomics | `build.sh` and `run.sh`; curated `samples/`                              |   |              |

---

## 🧠 Learning Outcomes (Course-aligned)

* **Lexical analysis** — scanning & token kinds
* **Top-down parsing** — recursive descent with operator precedence
* **AST design** — minimal, extensible node set
* **Semantic model** — lexical scope & environment chaining
* **Execution model** — tree-walking evaluator with type checks

---

## 🗣️ Language Surface (Mini Spec)

### Statements

```
program   := { stmt }
stmt      := vardecl | assign | if | while | print | block
vardecl   := "var" IDENT [ "=" expr ] ";"
assign    := IDENT "=" expr ";"
if        := "if" "(" expr ")" stmt [ "else" stmt ]
while     := "while" "(" expr ")" stmt
print     := "print" "(" expr ")" ";"
block     := "{" { stmt } "}"
```

### Expressions (descending precedence)

```
expr        := or
or          := and        { "||" and }
and         := equality   { "&&" equality }
equality    := comparison { ("==" | "!=") comparison }
comparison  := term       { ("<" | "<=" | ">" | ">=") term }
term        := factor     { ("+" | "-") factor }
factor      := unary      { ("*" | "/" | "%") unary }
unary       := ("!" | "-") unary | primary
primary     := NUMBER | "true" | "false" | IDENT | "(" expr ")"
```

---

## 📦 Project Structure

> You’re here:

```
minilang/
├── README.md
├── .gitignore
├── build.sh
├── run.sh
├── samples/
│   ├── program.ml
│   ├── arithmetic.ml
│   ├── factorial.ml
│   ├── fibonacci.ml
│   └── booleans.ml
├── MiniLang.java            # Entry point (requires a file arg)
├── ast/
│   └── Ast.java             # Stmt/Expr nodes
├── lexer/
│   ├── Kind.java
│   ├── Token.java
│   └── Lexer.java
├── parser/
│   └── Parser.java
└── runtime/
    ├── Environment.java
    └── Interpreter.java
```

> Compiled `.class` files are emitted to `out/` by the build script and ignored by git.

---

## 🛠️ Prerequisites

* **JDK 17+** (`java -version`, `javac -version`)
* Bash/Zsh (for the provided scripts)

  > Windows users: use Git Bash / WSL, or run the `javac`/`java` commands directly.

---

## ⚙️ Build & Run

### 1) Build

```bash
chmod +x build.sh run.sh
./build.sh
```

### 2) Execute a program

```bash
./run.sh samples/program.ml
```

The entry point enforces a single file argument. Direct `java` usage:

```bash
java -cp out MiniLang samples/factorial.ml
```

---

## 🧪 Sample Programs

* **Arithmetic**: `samples/arithmetic.ml` — operators & precedence
* **Factorial**: `samples/factorial.ml` — loops & state
* **Fibonacci**: `samples/fibonacci.ml` — iterative sequence
* **Booleans**: `samples/booleans.ml` — truth tables & `&&`/`||`/`!`

> Pro tip: tweak a program, re-run the same command, narrate the output. Great for TA demos.

---

## 🔍 Troubleshooting

* **`permission denied: ./build.sh`**
  `chmod +x build.sh run.sh`

* **`Could not find or load main class MiniLang`**
  Ensure you ran `./build.sh` successfully and are executing from the repo root:
  `java -cp out MiniLang samples/program.ml`

* **`Type error expected int/bool ...`**
  Intentional runtime guardrail. Check your expression types.

* **Parser errors** like `Expected ) at line:col ...`
  The parser reports the earliest failing token; scan nearby syntax.

---

## 🧭 Design at a Glance

* **Lexer**: single-pass scanner, emits `Token(kind, lexeme, line, col)`
* **Parser**: recursive descent, clear rule per precedence tier
* **AST**: minimalist sealed-by-convention node set (`Block`, `VarDecl`, `Assign`, `If`, `While`, `Print`, `Literal`, `Variable`, `Unary`, `Binary`)
* **Runtime**: `Environment` chain for lexical scope; interpreter walks AST with type guards

---

## 🛣️ Roadmap (stretch goals)

* Functions + call stack (return signaling)
* Static type checker (compile-time diagnostics)
* Source spans in AST for pinpoint errors
* REPL mode (interactive statements/expressions)
* Constant folding & small-step optimizations
* Packaging as a runnable JAR

---

## 📄 License

MIT — use freely for learning, demos, and portfolio work. Attribution appreciated.

---

## 🙌 Acknowledgments

Built to accelerate learning for **CPSC 323**. Feedback loops welcome — PRs, issues, and suggestions help the project (and your future students) get better.
