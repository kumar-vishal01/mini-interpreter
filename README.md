# SPEEK Interpreter
**Advanced OOP in Java — Sitare University Group Project**

A working interpreter for the SPEEK scripting language, built entirely in pure Java.
Write SPEEK code → run the interpreter → see real output on your terminal.

---

## Quick Start

```bash
cd speek/
mkdir out
javac -d out src/speek/*.java
java -cp out speek.Main samples/program1.speek
```

---

## File Structure

```
speek/
├── src/
│   └── speek/
│       ├── TokenType.java       ← enum of all token types
│       ├── Token.java           ← immutable token object (type + value + line)
│       ├── Tokenizer.java       ← Stage 1: text → List<Token>
│       ├── Expression.java      ← interface: evaluate(Environment env)
│       ├── Nodes.java           ← NumberNode, StringNode, VariableNode, BinaryOpNode
│       ├── Environment.java     ← variable store: HashMap<String, Object>
│       ├── Instructions.java    ← Instruction interface + 4 instruction classes
│       ├── Parser.java          ← Stage 2: List<Token> → List<Instruction>
│       ├── Interpreter.java     ← orchestrates all 3 stages
│       └── Main.java            ← entry point, reads .speek file
├── samples/
│   ├── program1.speek           ← arithmetic  → output: 16
│   ├── program2.speek           ← strings     → output: Sitare / Hello from SPEEK
│   ├── program3.speek           ← conditional → output: Pass
│   └── program4.speek           ← loop        → output: 1 2 3 4
├── out/                         ← created by javac (do not commit)
└── README.md
```

---

## How to Run

### Windows

**Step 1 — Install Java**
- Download JDK 11+ from https://adoptium.net
- Run the installer — make sure "Add to PATH" is checked
- Open Command Prompt and verify:
```
java -version
javac -version
```

**Step 2 — Compile**
```
cd C:\Users\YourName\speek
mkdir out
javac -d out src\speek\*.java
```

**Step 3 — Run**
```
java -cp out speek.Main samples\program1.speek
java -cp out speek.Main samples\program2.speek
java -cp out speek.Main samples\program3.speek
java -cp out speek.Main samples\program4.speek
```

---

### macOS

**Step 1 — Install Java**
```bash
# Check if Java is already installed
java -version

# If not installed, download from https://adoptium.net
# Or use Homebrew:
brew install openjdk@17
```

**Step 2 — Compile**
```bash
cd ~/Desktop/speek
mkdir out
javac -d out src/speek/*.java
```

**Step 3 — Run**
```bash
java -cp out speek.Main samples/program1.speek
```

---

### Linux (Ubuntu / Debian)

**Step 1 — Install Java**
```bash
sudo apt update
sudo apt install default-jdk
java -version
javac -version
```

For Fedora / RHEL:
```bash
sudo dnf install java-11-openjdk-devel
```

**Step 2 — Compile**
```bash
cd ~/speek
mkdir out
javac -d out src/speek/*.java
```

**Step 3 — Run**
```bash
java -cp out speek.Main samples/program1.speek
```

---

### IntelliJ IDEA (any OS)

1. File → Open → select the `speek/` folder
2. Right-click `src/speek/` → Mark Directory as → Sources Root
3. Right-click `Main.java` → Run 'Main.main()'
4. When prompted for a program argument, enter: `samples/program1.speek`

---

### VS Code (any OS)

1. Install the **Extension Pack for Java** from the marketplace
2. Open the `speek/` folder
3. Open `Main.java` and click the Run ▶ button
4. To pass the file path, edit `.vscode/launch.json`:
```json
"args": ["samples/program1.speek"]
```

---

## Sample Programs

| File | Code | Output |
|------|------|--------|
| program1.speek | `let x be 10` / `let y be 3` / `let result be x + y * 2` / `say result` | `16` |
| program2.speek | `let name be "Sitare"` / `say name` / `say "Hello from SPEEK"` | `Sitare` / `Hello from SPEEK` |
| program3.speek | `let score be 85` / `if score is greater than 50 then` / `say "Pass"` | `Pass` |
| program4.speek | `let i be 1` / `repeat 4 times` / `say i` / `let i be i + 1` | `1` `2` `3` `4` |

---

## Pipeline

```
Source code (.speek file)
        ↓
  [ Tokenizer ]  →  List<Token>
        ↓
   [ Parser ]    →  List<Instruction>
        ↓
  [ Evaluator ]  →  output printed to terminal
```

- **Stage 1 — Tokenizer:** Reads raw text character by character, emits a flat list of labelled tokens. Whitespace is discarded. Ends with EOF.
- **Stage 2 — Parser:** Reads tokens, builds an instruction tree. Operator precedence is handled by three chained methods: `parseExpression()` → `parseTerm()` → `parsePrimary()`.
- **Stage 3 — Evaluator:** Walks the instruction list, executes each one. All instructions share one `Environment` (the variable store).

---

## Common Errors

| Error | Fix |
|-------|-----|
| `javac: command not found` | Java not installed. Install JDK (not JRE) and restart terminal. |
| `error: package speek does not exist` | You're not in the `speek/` folder. Run `cd speek/` first. |
| `Variable not defined: x` | Used a variable before assigning it. Check your `let` statements. |
| `Expected BE but got '...'` | Syntax error. Check your SPEEK code matches the language spec. |
| `Could not read file: ...` | Wrong path to .speek file. Check for typos. |
| `Cannot find or load main class speek.Main` | Missing `-cp out` flag or `out/` doesn't exist. Run `javac` first. |

---

## Team Roles

| Member | Files |
|--------|-------|
| Member 1 — Tokenizer | `TokenType.java`, `Token.java`, `Tokenizer.java` |
| Member 2 — Data structures | `Expression.java`, `Nodes.java`, `Environment.java`, `Instructions.java` |
| Member 3 — Parser + entry point | `Parser.java`, `Interpreter.java`, `Main.java` |

