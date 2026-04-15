# SPEEK Interpreter

**Simple Plain English Execution Kit (SPEEK)** — A mini scripting language interpreter built completely from scratch in pure Java. 

This project demonstrates how a program reads source code, understands its structure, and executes it. Every time you run a SPEEK script, a three-step pipeline evaluates your instructions transparently via Lexical Analysis, Syntactic Parsing, and Evaluation.

---

## 🚀 How to Clone & Run

### 1. Clone the Repository
Begin by cloning the project and navigating into it:

```bash
git clone https://github.com/kumar-vishal01/speek-interpreter.git
cd speek-interpreter
```

### 2. Compile the Project
The interpreter relies purely on standard Java and doesn't require any external build tools. Create an output directory and compile all the `.java` files from the `src` folder:

```bash
mkdir -p out
javac -d out src/speek/*.java
```

### 3. Run Sample Programs
Once compiled, you can run any `.speek` script by passing the file to the main execution class. We have provided four standard sample programs to demonstrate the language capabilities.

```bash
java -cp out speek.Main src/samples/program1.speek
```
*(Replace `program1.speek` with `program2.speek`, `program3.speek`, or `program4.speek` to run the other tests).*

### 4. Run Your Own Custom Scripts
You can easily write and execute your own SPEEK scripts! Simply create a file ending in `.speek` anywhere on your computer (for example, `mycode.speek`), write your code maintaining the standard indentation rules, and pass the path of your completely new file to the interpreter:

```bash
java -cp out speek.Main path/to/your/mycode.speek
```

---

## 📂 Project Structure

```text
speek-interpreter/
├── src/
│   ├── speek/                 # Core interpreter source code
│   │   ├── AssignInstruction.java
│   │   ├── BinaryOpNode.java
│   │   ├── Environment.java   # Variable memory store for the runtime
│   │   ├── Expression.java    # Interface for evaluating numeric/string nodes
│   │   ├── IfInstruction.java
│   │   ├── Instruction.java   # Interface for executing statements 
│   │   ├── Interpreter.java   # Evaluator (walks the tree and executes instructions)
│   │   ├── Main.java          # CLI Entry Point
│   │   ├── NumberNode.java
│   │   ├── Parser.java        # Syntactic analysis (builds an AST)
│   │   ├── PrintInstruction.java
│   │   ├── RepeatInstruction.java
│   │   ├── StringNode.java
│   │   ├── Token.java
│   │   ├── TokenType.java     # Enum containing language parameters
│   │   ├── Tokenizer.java     # Lexical analysis
│   │   └── VariableNode.java
│   │
│   └── samples/               # Example programs to test your compiler
│       ├── program1.speek     # Tests Arithmetic & Variables
│       ├── program2.speek     # Tests String Output
│       ├── program3.speek     # Tests Conditionals
│       └── program4.speek     # Tests Loops
└── README.md
```

---

## 📖 Test Cases & Outputs

Below are the standard test cases demonstrating the fundamental capabilities of the SPEEK language. 
*(Note: Blocks of code associated with `if` and `repeat` are defined by the indentation of the following lines).*

### Program 1 — Arithmetic and Variables
**Code (`program1.speek`):**
```speek
let x be 10
let y be 3
let result be x + y * 2
say result
```
**Expected Output:**
```text
16.0
```

### Program 2 — String Output
**Code (`program2.speek`):**
```speek
let name be "Sitare"
say name
say "Hello from SPEEK"
```
**Expected Output:**
```text
Sitare
Hello from SPEEK
```

### Program 3 — Conditional
**Code (`program3.speek`):**
```speek
let score be 85
if score is greater than 50 then
    say "Pass"
```
**Expected Output:**
```text
Pass
```

### Program 4 — Loop
**Code (`program4.speek`):**
```speek
let i be 1
repeat 4 times
    say i
    let i be i + 1
```
**Expected Output:**
```text
1.0
2.0
3.0
4.0
```

---

## ⚙️ How the Interpreter Works
The execution pipeline is straightforward and runs your code through three specific steps:

1. **Step 1 (Tokenizer):** Reads the source code character by character and breaks it into a flat list of labeled pieces called tokens (e.g., words like `let`, `say`, matching operators). It also evaluates spacing to manage block Indentation. 
2. **Step 2 (Parser):** Reads the generated token list and uses a strict precedence hierarchy (managing priority for multiplication over addition, for example) to construct a localized, tree-based abstract data structure.
3. **Step 3 (Evaluator):** Processes and evaluates the generated syntax tree from the bottom-up, keeping track of persistent variables mapped within the `Environment`, effectively executing the program logic!
