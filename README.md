# SPEEK Interpreter

**Simple Plain English Execution Kit** — a mini scripting language interpreter built in pure Java.

---

## Project Structure

```
speek_Project v3/
├── src/
│   ├── speek/
│   │   ├── TokenType.java          # Enum of all token types
│   │   ├── Token.java              # Immutable token (type, value, line)
│   │   ├── Expression.java         # Interface: evaluate(env) → Object
│   │   ├── NumberNode.java         # Literal number expression
│   │   ├── StringNode.java         # Literal string expression
│   │   ├── VariableNode.java       # Variable reference expression
│   │   ├── BinaryOpNode.java       # left OP right expression
│   │   ├── Environment.java        # Variable store (name → value map)
│   │   ├── Instruction.java        # Interface: execute(env)
│   │   ├── AssignInstruction.java  # let x be <expr>
│   │   ├── PrintInstruction.java   # say <expr>
│   │   ├── IfInstruction.java      # if <cond> then <body>
│   │   ├── RepeatInstruction.java  # repeat <n> times <body>
│   │   ├── Tokenizer.java          # Source → List<Token>
│   │   ├── Parser.java             # List<Token> → List<Instruction>
│   │   ├── Interpreter.java        # Runs the full pipeline
│   │   ├── Main.java               # CLI entry point
│   │   ├── TestToken.java          # Manual tokenizer test
│   │   ├── TestParser.java         # Manual parser test
│   │   └── TestInterpreter.java    # Runs all 4 sample programs
│   └── samples/
│       ├── program1.speek          # Arithmetic
│       ├── program2.speek          # Strings
│       ├── program3.speek          # Conditional
│       ├── program4.speek          # Loop
│       └── program5_nested.speek   # Nested blocks (extension)
├── compile.sh                      # Compile all sources
└── README.md
```

---

## The SPEEK Language

### Syntax

| Construct         | Syntax                                 |
|-------------------|----------------------------------------|
| Assign variable   | `let x be 10`                          |
| Print value       | `say x`                                |
| Conditional       | `if x is greater than 5 then`          |
| Symbolic compare  | `if x > 5 then` / `if x < 5 then` / `if x == 5 then` |
| Loop              | `repeat 3 times`                       |
| Number literal    | `42` or `3.14`                         |
| String literal    | `"hello world"`                        |
| Arithmetic        | `x + y * 2 - z / 1`                   |

### Block Indentation

Bodies of `if` and `repeat` blocks are indicated by **indentation** (any whitespace indent). The interpreter detects blocks using line numbers — indented lines after a header belong to that block's body.

Nested blocks are fully supported:
```
repeat 3 times
    if x is greater than 5 then
        say x
    let x be x - 1
```

---

## How the Interpreter Works

The interpreter is a three-step pipeline:

```
Source Code (String)
       │
       ▼
  [Tokenizer]  →  List<Token>
       │
       ▼
   [Parser]   →  List<Instruction>
       │
       ▼
 [Interpreter] → Output
```

**Step 1 — Tokenizer** (`Tokenizer.java`): Reads source character by character, recognises patterns, and emits a `Token` for each one. The last token is always `EOF`.

**Step 2 — Parser** (`Parser.java`): Reads tokens and builds a list of `Instruction` objects. Operator precedence is handled by the three-level chain:
- `parseExpression()` — handles `+` and `-` (lowest priority)
- `parseTerm()` — handles `*` and `/` (higher priority)
- `parsePrimary()` — handles a single number, string, or variable

**Step 3 — Execution**: Each `Instruction` is executed in order using a shared `Environment` (variable store).

---

## Sample Programs & Expected Output

### Program 1 — Arithmetic
```
let x be 10
let y be 3
let result be x + y * 2
say result
```
**Output:** `16`

### Program 2 — Strings
```
let name be "Sitare"
say name
say "Hello from SPEEK"
```
**Output:**
```
Sitare
Hello from SPEEK
```

### Program 3 — Conditional
```
let score be 85
if score is greater than 50 then
    say "Pass"
```
**Output:** `Pass`

### Program 4 — Loop
```
let i be 1
repeat 4 times
    say i
    let i be i + 1
```
**Output:**
```
1
2
3
4
```

---

## How to Compile and Run

### Compile

```bash
# From the project root
javac -d out src/speek/*.java
```

Or use the provided script:
```bash
bash compile.sh
```

### Run a .speek file

```bash
java -cp out speek.Main src/samples/program1.speek
```

### Run the test suite

```bash
java -cp out speek.TestInterpreter
```

### Run tokenizer / parser tests

```bash
java -cp out speek.TestToken
java -cp out speek.TestParser
```

---

## Extension: Nested Blocks

Nested `if` inside `repeat` (and vice versa) is supported. Indentation determines which instructions belong to which block. Example (`program5_nested.speek`):

```
let x be 10
let i be 1
repeat 3 times
    if x is greater than 5 then
        say i
    let i be i + 1
```

**Output:**
```
1
2
3
```

---

## Design Decisions

- **One class per file**: Every interface and class lives in its own `.java` file, following standard Java conventions.
- **Immutability**: `Token` fields are `final`; no setters.
- **Operator precedence via call chain**: `parseTerm` is called inside `parseExpression`, so `*` and `/` always bind tighter than `+` and `-` — no precedence table needed.
- **Indent detection via line numbers**: The tokenizer records line numbers on every token. The parser uses these to determine block membership without requiring an explicit `end` keyword.
- **Clean separation of concerns**: `Tokenizer` only lexes, `Parser` only parses, `Instruction`/`Expression` classes only execute/evaluate.
