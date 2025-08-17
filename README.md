# MiniLang

A compact, **education-first language interpreter** in Java. MiniLang demonstrates the full translation pipeline — **lexing → parsing → AST → interpretation** — with lexical scoping, short‑circuit logic, and clean modularity. Ideal for CPSC 323 labs, TA demos, and rapid learning.

> Owner: **Anuj More**  •  GitHub: **[https://github.com/OfficialAnujMore/MiniLang](https://github.com/OfficialAnujMore/MiniLang)**

---

## 🔎 Description

A compact Java interpreter that tokenizes source code, builds an AST, and executes it with lexical scoping. Perfect for teaching grammars, operator precedence, and runtime behavior.

---

## ✨ Features (short & simple)

* **Lexer with source spans** — turns text into tokens; includes line\:column; handles multi‑char ops, comments, and spaces.
* **Recursive‑descent parser** — builds the AST with rule‑per‑function; honors precedence (e.g., `||` lowest → primaries highest); clear “expected X” errors.
* **AST model** — small, extensible node set for statements & expressions; structure only.
* **Interpreter (tree‑walking)** — executes the AST step‑by‑step; nested scopes; supports `&&` and `||` short‑circuiting.
* **Runtime type guards** — checks `int` vs `bool` with helpful messages.
* **Deterministic semantics** — predictable math/comparison ops; standard control flow (`if/else`, `while`, `{ ... }`).
* **Developer ergonomics** — `build.sh` & `run.sh`, clean folders (`lexer/`, `parser/`, `ast/`, `runtime/`), works in any IDE.

---

## 🧠 Learning Outcomes & Syllabus Alignment

* **Language design & grammars** → Tiny imperative language (ints/bools, statements, expressions) with clear precedence.
* **Lexical analysis** → Tokenizer with keyword map and precise line/col diagnostics.
* **Parsing techniques** → Top‑down recursive descent; precedence; fast `expect(...)` failures.
* **Abstract syntax trees** → Clean split between statements and expressions; easy to extend.
* **Semantic checks** → Lexical scoping via nested environments; name resolution; decl vs assign; runtime type checks.
* **Execution model** → Tree‑walking interpreter, short‑circuit `&&`/`||`, visible output via `print`.
* **Error reporting** → Coordinated lexer/parser errors; clear runtime messages.
* **Tooling & testing** → Modular layout, build/run scripts, sample programs for quick validation.

---

## 🧰 Tools & Tech Stack

* **Java 17+** — Core implementation (lexer, parser, AST, interpreter).
* **Bash/Zsh scripts** — One‑touch build & run (`./build.sh`, `./run.sh`).
* **Text editor/IDE** — VS Code or IntelliJ.
* **Git & GitHub** — Version control, PRs, docs.

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

## ⚙️ Build & Run

**Prerequisites:** JDK 17+, Bash/Zsh (Windows: Git Bash/WSL or run `javac`/`java` directly.)

**Build**

```bash
chmod +x build.sh run.sh
./build.sh
```

**Run**

```bash
./run.sh samples/program.ml
# or directly
java -cp out MiniLang samples/factorial.ml
```

---

## ✅ Execution Proofs (Screenshots)

The following screenshots validate successful end‑to‑end runs across multiple programs. Store images under `screenshots/`.

* **Arithmetic:** `./run.sh samples/arithmetic.ml`
  `![Arithmetic program run](screenshots/arithmetic-run.png)`
* **Factorial:** `./run.sh samples/factorial.ml`
  `![Factorial program run](screenshots/factorial-run.png)`
* **Fibonacci:** `./run.sh samples/fibonacci.ml`
  `![Fibonacci program run](screenshots/fibonacci-run.png)`
* **Booleans:** `./run.sh samples/booleans.ml`
  `![Booleans program run](screenshots/booleans-run.png)`

> Optional hero image:
> `![Language Interpreter — MiniLang](screenshots/language-interpreter.png "lexing → parsing → AST → interpretation")`

---

## 📊 Outcomes

* See the whole pipeline — source → tokens → AST → execution.
* Understand grammars & precedence — how syntax maps to the AST.
* Master scope & lifetimes — nested blocks and variable shadowing.
* Build semantic discipline — clear type checks and actionable errors.
* Move fast — run and extend quickly; perfect for labs, workshops, and evaluations.

---

## 🔍 Troubleshooting

* `permission denied: ./build.sh` → `chmod +x build.sh run.sh`
* `Could not find or load main class MiniLang` → ensure `./build.sh` succeeded, run from repo root: `java -cp out MiniLang samples/program.ml`
* `Type error expected int/bool ...` → adjust expression types; guards are intentional.
* Parser errors like `Expected ) at line:col ...` → fix nearby syntax; parser reports earliest failing token.

---

## 🛣️ Roadmap (stretch)

* Functions + call stack (return signaling)
* Static type checker
* Source spans in AST
* REPL mode
* Constant folding / small‑step optimizations
* Runnable JAR packaging

---

## 📄 License

MIT — use freely for learning, demos, and portfolio work.

---

## 🙌 Acknowledgments

Built to accelerate learning for **CPSC 323**. PRs, issues, and suggestions welcome.
