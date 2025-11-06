# Requirements Document: Interpreter Pattern (Pattern #20)

## Pattern Information
- **Pattern Name**: Interpreter Pattern
- **Category**: Behavioral Design Pattern
- **Complexity**: ⭐⭐⭐⭐ (High - involves grammar, parsing, AST)
- **Popularity**: ⭐⭐ (Low - specialized use cases, often replaced by parser generators)

## Business Context: StreamFlix Video Platform

### Domain
**Advanced Video Search Query Language**

Users need to search for videos using complex criteria beyond simple keyword search. Instead of creating dozens of filter dropdowns and checkboxes, StreamFlix provides a powerful query language where users can type expressions like:

```
duration > 10 AND category = 'tutorial'
views > 1000 OR (likes > 100 AND comments > 50)
uploaded_date > '2024-01-01' AND resolution = '4K'
```

Content creators, moderators, and power users use this query language to find specific videos quickly. The system must parse and evaluate these expressions against video metadata.

### Current Problem (Without Interpreter Pattern)

**Hardcoded Search Logic:**
```java
public class VideoSearchService {
    public List<Video> search(String query) {
        // ❌ Cannot handle complex queries!
        // Only supports simple keyword search

        if (query.contains("duration")) {
            // Try to extract duration... how?
            // What if combined with AND/OR?
            // This quickly becomes unmaintainable!
        }

        // Trying to parse manually:
        if (query.contains("AND")) {
            String[] parts = query.split("AND");
            // What about nested expressions?
            // What about operator precedence?
            // What about parentheses?
        }

        // ❌ Impossible to maintain as queries become complex!
        return new ArrayList<>();
    }
}
```

**Issues:**
1. ❌ **No Grammar**: Cannot parse structured queries
2. ❌ **String Manipulation Hell**: Split/contains/regex is unmaintainable
3. ❌ **No Operator Precedence**: AND vs OR evaluation order unclear
4. ❌ **No Nested Expressions**: Cannot handle `(A AND B) OR C`
5. ❌ **No Validation**: Syntax errors not caught
6. ❌ **Not Extensible**: Adding new operators requires code changes everywhere
7. ❌ **Hard to Test**: Cannot test expression evaluation in isolation

### Real-World Problem Scenario

**Power User Journey:**
```
User: Content moderator needs to find videos for review
Goal: Find all tutorial videos longer than 10 minutes with low engagement

Attempt 1: Use basic search filters
  → 5 dropdown menus
  → 10 checkboxes
  → 3 date range pickers
  → Still cannot express "low engagement" = (views > 1000 AND likes < 50)
  → 😞 Frustrated

Attempt 2: Contact support
  → "Can you find videos where duration > 10 AND category = 'tutorial' AND (views > 1000 AND likes < 50)?"
  → Support: "Sorry, our system doesn't support complex queries"
  → Manual review of 5,000 videos
  → ⏱️ Takes 8 hours

Attempt 3: Export to CSV, filter in Excel
  → Download 50,000 video records
  → Complex Excel formulas
  → Error-prone, time-consuming
  → 😫 Not repeatable
```

**Business Impact:**
- ⏱️ **Lost Productivity**: 8 hours for task that should take 10 seconds
- 😞 **Poor UX**: Power users frustrated by limited search
- 🐛 **Manual Errors**: Humans miss videos during manual review
- 💰 **Competitive Disadvantage**: Competitors have advanced search
- 📉 **Lower Quality**: Cannot efficiently moderate content

### Why This Occurs

1. **No Domain-Specific Language (DSL)**: Search is limited to UI controls
   - UI cannot express all possible combinations
   - Power users need programmatic query language

2. **String Parsing is Hard**: Manual parsing is error-prone
   - Operator precedence
   - Parentheses matching
   - Tokenization
   - Syntax validation

3. **Tightly Coupled Evaluation**: Search logic mixed with parsing
   - Cannot reuse expressions
   - Cannot optimize queries
   - Cannot test components independently

4. **No Abstract Syntax Tree (AST)**: Expressions not structured
   - Cannot analyze queries
   - Cannot optimize before execution
   - Cannot explain results

## Requirements

### Functional Requirements

#### FR-1: Query Language Grammar
The system must support a query language with the following grammar:

**Terminals (Leaf Expressions):**
1. **Comparison Expressions**
   - `duration > 10` (duration in minutes)
   - `views > 1000` (view count)
   - `likes > 100` (like count)
   - `comments > 50` (comment count)
   - `category = 'tutorial'` (exact match)
   - `resolution = '4K'` (720p, 1080p, 4K, 8K)
   - `uploaded_date > '2024-01-01'` (date comparison)

   Supported operators: `>`, `<`, `>=`, `<=`, `=`, `!=`

**Non-Terminals (Composite Expressions):**
1. **AND Expression**
   - `expr1 AND expr2`
   - Both expressions must be true
   - Example: `duration > 10 AND views > 1000`

2. **OR Expression**
   - `expr1 OR expr2`
   - At least one expression must be true
   - Example: `category = 'tutorial' OR category = 'review'`

3. **NOT Expression**
   - `NOT expr`
   - Negates the expression
   - Example: `NOT (views < 100)`

4. **Parenthesized Expression**
   - `(expr)`
   - Groups expressions for precedence
   - Example: `(duration > 10 AND views > 1000) OR likes > 500`

**Operator Precedence (highest to lowest):**
1. Parentheses `()`
2. NOT
3. AND
4. OR

**Example Queries:**
```
# Simple comparison
duration > 10

# AND combination
duration > 10 AND views > 1000

# OR combination
category = 'tutorial' OR category = 'review'

# Complex nested
(duration > 10 AND category = 'tutorial') OR (views > 10000 AND likes > 500)

# With NOT
duration > 5 AND NOT (views < 100)

# Multiple conditions
duration > 10 AND views > 1000 AND likes > 100 AND category = 'tutorial'
```

#### FR-2: Expression Parsing
The system must parse query strings into Abstract Syntax Tree (AST):

1. **Tokenization**
   - Split query into tokens (identifiers, operators, literals, parentheses)
   - Recognize keywords: AND, OR, NOT
   - Recognize operators: >, <, >=, <=, =, !=
   - Recognize literals: numbers, strings (quoted)

2. **Syntax Validation**
   - Detect syntax errors (mismatched parentheses, invalid operators)
   - Report error position and helpful message
   - Example errors:
     - `duration > ` → "Expected value after >"
     - `duration > 10 AND` → "Expected expression after AND"
     - `(duration > 10` → "Unmatched parenthesis"

3. **AST Construction**
   - Build tree structure from tokens
   - Respect operator precedence
   - Example: `A AND B OR C` → `(A AND B) OR C` (AND has higher precedence)

#### FR-3: Expression Evaluation
The system must evaluate expressions against video objects:

1. **Terminal Expression Evaluation**
   - Compare video properties against literal values
   - Support different data types: int, string, date
   - Return boolean result

2. **Composite Expression Evaluation**
   - AND: Return true if both children true
   - OR: Return true if at least one child true
   - NOT: Return opposite of child

3. **Context (Video Object)**
   - Expression receives Video object as context
   - Extracts properties: duration, views, likes, category, etc.
   - Evaluates comparison and returns result

4. **Result**
   - Each expression evaluates to boolean (true/false)
   - Videos matching query return true
   - Videos not matching return false

#### FR-4: Video Search Integration
The system must integrate expression evaluation with video search:

1. **Search Method**
   ```java
   List<Video> search(String query)
   ```
   - Parse query string to expression tree
   - Filter video collection using expression
   - Return matching videos

2. **Performance Optimization**
   - Parse query once, reuse for multiple videos
   - Short-circuit evaluation (AND/OR early exit)
   - Index-aware query optimization (if possible)

3. **Error Handling**
   - Catch parse errors and return helpful message
   - Handle invalid property names
   - Handle type mismatches (comparing string to number)

### Non-Functional Requirements

#### NFR-1: Performance
- **Parse Time**: < 10ms for typical query (< 50 characters)
- **Evaluate Time**: < 1ms per video
- **Search Time**: < 100ms for 10,000 videos
- **Memory**: AST size < 1KB for typical query

#### NFR-2: Usability
- **Clear Syntax Errors**: Show error position and suggestion
- **Query Examples**: Provide common query templates
- **Auto-complete**: Suggest property names and operators (bonus)

#### NFR-3: Maintainability
- **Grammar Extensibility**: Adding new operator = add one class
- **Clean Separation**: Parsing separate from evaluation
- **Testability**: Each expression type independently testable

#### NFR-4: Correctness
- **Operator Precedence**: AND before OR, NOT before AND
- **Parentheses**: Correctly override precedence
- **Boolean Logic**: Standard truth tables for AND/OR/NOT

### Acceptance Criteria

✅ **Implementation Complete When:**

1. Grammar supports comparison, AND, OR, NOT, parentheses
2. Parser builds correct AST from query string
3. Evaluator correctly evaluates expressions against Video objects
4. At least 10 example queries work correctly
5. Syntax errors detected and reported with helpful messages
6. Operator precedence correctly handled
7. Parentheses correctly override precedence
8. Demo shows 5+ realistic search scenarios
9. UML diagram shows expression hierarchy
10. Code compiles without errors

## Interpreter Pattern Structure

### Components

#### 1. Expression Interface (Abstract Expression)
```java
public interface Expression {
    /**
     * Interpret (evaluate) this expression in given context
     *
     * @param context Video object to evaluate against
     * @return true if video matches this expression
     */
    boolean interpret(Video context);

    /**
     * Get string representation of this expression
     *
     * @return Human-readable expression string
     */
    String toString();
}
```

#### 2. Terminal Expressions (Leaf Nodes)
```java
// Example: duration > 10
public class GreaterThanExpression implements Expression {
    private String property;  // "duration"
    private int value;        // 10

    public boolean interpret(Video video) {
        int actualValue = video.getProperty(property);
        return actualValue > value;
    }
}

// Example: category = 'tutorial'
public class EqualsExpression implements Expression {
    private String property;  // "category"
    private String value;     // "tutorial"

    public boolean interpret(Video video) {
        String actualValue = video.getProperty(property);
        return actualValue.equals(value);
    }
}
```

#### 3. Non-Terminal Expressions (Composite Nodes)
```java
// Example: expr1 AND expr2
public class AndExpression implements Expression {
    private Expression left;
    private Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public boolean interpret(Video video) {
        // Short-circuit: if left is false, don't evaluate right
        return left.interpret(video) && right.interpret(video);
    }
}

// Example: expr1 OR expr2
public class OrExpression implements Expression {
    private Expression left;
    private Expression right;

    public boolean interpret(Video video) {
        // Short-circuit: if left is true, don't evaluate right
        return left.interpret(video) || right.interpret(video);
    }
}

// Example: NOT expr
public class NotExpression implements Expression {
    private Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean interpret(Video video) {
        return !expression.interpret(video);
    }
}
```

#### 4. Context (Video)
```java
public class Video {
    private String title;
    private int duration;       // in minutes
    private int views;
    private int likes;
    private int comments;
    private String category;
    private String resolution;
    private String uploadedDate;

    // Getters for all properties

    // Generic property accessor for interpreter
    public Object getProperty(String propertyName) {
        switch (propertyName) {
            case "duration": return duration;
            case "views": return views;
            case "likes": return likes;
            // ... etc
        }
    }
}
```

#### 5. Parser (Query Parser)
```java
public class QueryParser {
    /**
     * Parse query string into Expression tree
     *
     * @param query Query string (e.g., "duration > 10 AND views > 1000")
     * @return Root of expression tree
     * @throws ParseException if syntax error
     */
    public Expression parse(String query) throws ParseException {
        // Tokenize
        List<Token> tokens = tokenize(query);

        // Build AST with operator precedence
        return parseExpression(tokens);
    }

    private List<Token> tokenize(String query) {
        // Split into tokens
    }

    private Expression parseExpression(List<Token> tokens) {
        // Recursive descent parsing with precedence
    }
}
```

#### 6. Video Search Service (Client)
```java
public class VideoSearchService {
    private List<Video> videoDatabase;
    private QueryParser parser;

    public List<Video> search(String query) {
        // Parse query to expression tree
        Expression expression = parser.parse(query);

        // Filter videos using expression
        List<Video> results = new ArrayList<>();
        for (Video video : videoDatabase) {
            if (expression.interpret(video)) {
                results.add(video);
            }
        }

        return results;
    }
}
```

### Class Diagram Structure
```
┌─────────────────────────────────────────────────────────────┐
│                      Expression (Interface)                 │
│                     <<interface>>                           │
├─────────────────────────────────────────────────────────────┤
│ + interpret(context: Video): boolean                        │
│ + toString(): String                                        │
└────────────┬────────────────────────────────────────────────┘
             │
             │ implements
             │
    ┌────────┴────────────────────────────┐
    │                                     │
    │                                     │
┌───▼─────────────────┐      ┌───────────▼──────────────────┐
│ TerminalExpression  │      │  CompositeExpression         │
│  (Leaf Nodes)       │      │   (Non-Terminal Nodes)       │
├─────────────────────┤      ├──────────────────────────────┤
│GreaterThanExpression│      │ AndExpression                │
│ LessThanExpression  │      │ OrExpression                 │
│ EqualsExpression    │      │ NotExpression                │
│ ...                 │      └──────────────────────────────┘
└─────────────────────┘
             │
             │ interprets
             ▼
      ┌────────────┐
      │   Video    │ (Context)
      ├────────────┤
      │ - duration │
      │ - views    │
      │ - likes    │
      │ + getProperty()│
      └────────────┘

┌──────────────────────────┐
│     QueryParser          │
├──────────────────────────┤
│ + parse(query): Expression│
│ - tokenize()             │
│ - parseExpression()      │
└──────────────────────────┘

┌──────────────────────────┐
│  VideoSearchService      │ (Client)
├──────────────────────────┤
│ - videoDatabase: List    │
│ - parser: QueryParser    │
├──────────────────────────┤
│ + search(query): List<Video>│
└──────────────────────────┘
```

## Use Cases

### Use Case 1: Simple Comparison Query
```
Query: "duration > 10"

Expected Result:
  Videos with duration > 10 minutes

Example Videos:
  ✓ "React Tutorial" (15 min) → MATCH
  ✗ "Quick Tip" (5 min) → NO MATCH
  ✓ "Complete Course" (120 min) → MATCH
```

### Use Case 2: AND Query
```
Query: "duration > 10 AND views > 1000"

Expected Result:
  Videos longer than 10 min AND more than 1000 views

Example Videos:
  ✓ "React Tutorial" (15 min, 5000 views) → MATCH
  ✗ "Quick Tip" (5 min, 2000 views) → NO MATCH (duration too short)
  ✗ "Long Video" (30 min, 500 views) → NO MATCH (views too low)
```

### Use Case 3: OR Query
```
Query: "category = 'tutorial' OR category = 'review'"

Expected Result:
  Videos in tutorial OR review category

Example Videos:
  ✓ "React Tutorial" (category: tutorial) → MATCH
  ✓ "Phone Review" (category: review) → MATCH
  ✗ "Vlog Day 1" (category: vlog) → NO MATCH
```

### Use Case 4: Complex Nested Query
```
Query: "(duration > 10 AND category = 'tutorial') OR (views > 10000 AND likes > 500)"

Expected Result:
  Either:
    - Long tutorial videos, OR
    - Very popular videos (regardless of category)

Example Videos:
  ✓ "React Course" (30 min, tutorial, 2000 views, 100 likes) → MATCH (left side)
  ✓ "Viral Vlog" (5 min, vlog, 50000 views, 2000 likes) → MATCH (right side)
  ✓ "Python Tutorial" (15 min, tutorial, 20000 views, 800 likes) → MATCH (both sides!)
  ✗ "Short Vlog" (3 min, vlog, 500 views, 20 likes) → NO MATCH
```

### Use Case 5: Query with NOT
```
Query: "duration > 5 AND NOT (views < 100)"

Expected Result:
  Videos longer than 5 min that are NOT low-view (views >= 100)

Example Videos:
  ✓ "Tutorial" (10 min, 1000 views) → MATCH
  ✗ "Unpopular" (8 min, 50 views) → NO MATCH (views < 100)
  ✗ "Short" (2 min, 500 views) → NO MATCH (duration <= 5)
```

## Expected Output (Demo Scenarios)

### Scenario 1: Simple Query
```
═══════════════════════════════════════════════════════════════
INTERPRETER PATTERN - Video Search Query Language
═══════════════════════════════════════════════════════════════

SCENARIO 1: Simple Comparison Query
─────────────────────────────────────────────────────────────
Query: "duration > 10"

Parsing query...
✓ Parsed successfully

Expression Tree:
  GreaterThanExpression
    property: duration
    operator: >
    value: 10

Searching 100 videos...

Results (15 videos):
  ✓ "React Tutorial" (duration: 15 min)
  ✓ "Complete Python Course" (duration: 120 min)
  ✓ "JavaScript Basics" (duration: 25 min)
  ... (12 more)

Execution time: 5ms
```

### Scenario 2: AND Query
```
SCENARIO 2: AND Query
─────────────────────────────────────────────────────────────
Query: "duration > 10 AND views > 1000"

Parsing query...
✓ Parsed successfully

Expression Tree:
  AndExpression
    ├─ GreaterThanExpression (duration > 10)
    └─ GreaterThanExpression (views > 1000)

Searching 100 videos...

Results (8 videos):
  ✓ "React Tutorial" (duration: 15 min, views: 5000)
  ✓ "Python Course" (duration: 120 min, views: 15000)
  ... (6 more)

Execution time: 3ms
```

### Scenario 3: Complex Nested Query
```
SCENARIO 3: Complex Nested Query
─────────────────────────────────────────────────────────────
Query: "(duration > 10 AND category = 'tutorial') OR (views > 10000)"

Parsing query...
✓ Parsed successfully

Expression Tree:
  OrExpression
    ├─ AndExpression
    │   ├─ GreaterThanExpression (duration > 10)
    │   └─ EqualsExpression (category = 'tutorial')
    └─ GreaterThanExpression (views > 10000)

Searching 100 videos...

Results (22 videos):
  ✓ "React Course" (15 min, tutorial, 5000 views) → LEFT MATCH
  ✓ "Viral Vlog" (3 min, vlog, 50000 views) → RIGHT MATCH
  ✓ "Python Tutorial" (30 min, tutorial, 20000 views) → BOTH MATCH
  ... (19 more)

Execution time: 7ms
```

### Scenario 4: Syntax Error Handling
```
SCENARIO 4: Syntax Error Handling
─────────────────────────────────────────────────────────────
Query: "duration > 10 AND"

Parsing query...
✗ Parse Error at position 17:
  "duration > 10 AND"
                   ^
  Expected expression after AND operator
```

### Scenario 5: Power User Workflow
```
SCENARIO 5: Content Moderator Workflow
─────────────────────────────────────────────────────────────
Task: Find tutorial videos for manual review

Query 1: "category = 'tutorial' AND duration > 10"
  → Found 45 videos

Query 2: "category = 'tutorial' AND duration > 10 AND views < 1000"
  → Found 12 videos (low engagement, need review)

Query 3: "category = 'tutorial' AND duration > 10 AND views < 1000 AND NOT (likes > 50)"
  → Found 5 videos (low engagement, low quality)

Manual Review:
  1. "Beginner Tutorial" (12 min, 500 views, 15 likes)
  2. "Advanced Tips" (18 min, 300 views, 8 likes)
  ... (3 more)

✓ Task completed in 30 seconds (previously took 8 hours!)
```

## Design Considerations

### 1. Interpreter vs Other Approaches

**Interpreter Pattern (This requirement):**
- Pros: Flexible, extensible grammar
- Cons: Complex to implement, slower than compiled
- Use: Small, frequently-changing grammars
- Example: Search queries, config languages, scripting

**Regular Expression:**
- Pros: Fast, built-in
- Cons: Limited to text patterns, not hierarchical
- Use: Simple text matching
- Example: Email validation, log parsing

**Parser Generator (ANTLR, Yacc):**
- Pros: Handles complex grammars, generates parser code
- Cons: External dependency, learning curve
- Use: Full programming languages
- Example: SQL, JavaScript, Python

**Decision: Use Interpreter Pattern**
- Query grammar is simple (< 10 expression types)
- Need full control over evaluation
- Educational value (learning pattern)
- No external dependencies

### 2. Expression Tree vs Stack-Based Evaluation

**Options:**
- A) Build expression tree (AST), then evaluate
- B) Evaluate expressions directly while parsing (stack-based)

**Chosen: A (Expression Tree)**

**Reasoning:**
- Reusable: Parse once, evaluate many times
- Testable: Inspect tree structure
- Debuggable: Print tree for debugging
- Optimizable: Can transform tree before evaluation

**Trade-off:**
- More memory (store tree)
- Two-phase (parse then evaluate)

### 3. Operator Precedence Handling

**Implementation Options:**
- A) Recursive descent parser with precedence climbing
- B) Shunting-yard algorithm (postfix conversion)
- C) Precedence table with lookahead

**Chosen: A (Recursive descent with precedence)**

**Reasoning:**
- Simple grammar (only 3 operators)
- Easy to understand and maintain
- Natural mapping from grammar rules to code

**Precedence Rules:**
1. Parentheses `()` - highest
2. NOT
3. AND
4. OR - lowest

**Example:**
```
A OR B AND C      → A OR (B AND C)   (AND before OR)
NOT A OR B        → (NOT A) OR B     (NOT before OR)
(A OR B) AND C    → (A OR B) AND C   (parens override)
```

### 4. Short-Circuit Evaluation

**AND Expression:**
```java
public boolean interpret(Video video) {
    // If left is false, don't evaluate right
    return left.interpret(video) && right.interpret(video);
}
```

**Benefits:**
- Performance: Skip unnecessary evaluations
- Correctness: Standard boolean logic
- Example: `false AND (expensive_check)` → only evaluates `false`

**OR Expression:**
```java
public boolean interpret(Video video) {
    // If left is true, don't evaluate right
    return left.interpret(video) || right.interpret(video);
}
```

## Benefits

1. ✅ **Grammar Representation**: Formal representation of query language
2. ✅ **Extensibility**: Add new operators by adding classes
3. ✅ **Reusability**: Parse once, evaluate many times
4. ✅ **Composability**: Build complex expressions from simple ones
5. ✅ **Testability**: Test each expression type independently
6. ✅ **AST Benefits**: Inspect, optimize, transform expressions
7. ✅ **Decoupling**: Parsing separate from evaluation
8. ✅ **Type Safety**: Expression types enforced by compiler

## Drawbacks

1. ❌ **Complexity**: More complex than string matching
2. ❌ **Performance**: Slower than compiled queries
3. ❌ **Grammar Changes**: Changing grammar requires code changes
4. ❌ **Learning Curve**: Users must learn query syntax
5. ❌ **Error Messages**: Syntax errors can be cryptic

## When to Use Interpreter Pattern

**✅ Use When:**
- Grammar is simple and stable
- Performance is not critical
- Need full control over evaluation
- Grammar changes infrequently
- Educational/learning purposes

**❌ Don't Use When:**
- Grammar is complex (> 20 rules)
- Performance is critical (use compiled approach)
- Grammar changes frequently (use external DSL)
- Already have parser library available

## Implementation Checklist

### Phase 1: Core Expression Types
- [ ] Create `Expression` interface
- [ ] Create `Video` class (context)
- [ ] Create terminal expressions:
  - [ ] `GreaterThanExpression`
  - [ ] `LessThanExpression`
  - [ ] `EqualsExpression`
- [ ] Create composite expressions:
  - [ ] `AndExpression`
  - [ ] `OrExpression`
  - [ ] `NotExpression`

### Phase 2: Parser Implementation
- [ ] Create `Token` class (for tokenization)
- [ ] Create `QueryParser` class
- [ ] Implement tokenization
- [ ] Implement recursive descent parsing
- [ ] Implement operator precedence
- [ ] Implement parentheses handling

### Phase 3: Search Integration
- [ ] Create `VideoSearchService` class
- [ ] Integrate parser with search
- [ ] Implement error handling
- [ ] Add syntax error reporting

### Phase 4: Demo & Documentation
- [ ] Create `InterpreterPatternDemo.java`
- [ ] Demonstrate 5+ query scenarios
- [ ] Show syntax error handling
- [ ] Show operator precedence
- [ ] Create `package.bluej` with UML
- [ ] Create `Documents/Solutions/Interpreter.md`

## Success Criteria

✅ **Implementation Complete When:**
1. Grammar supports comparison, AND, OR, NOT, parentheses
2. Parser correctly tokenizes and parses queries
3. Expression tree correctly built with precedence
4. Evaluator correctly interprets expressions
5. At least 10 test queries work correctly
6. Syntax errors detected and reported
7. Operator precedence correctly handled
8. Parentheses correctly override precedence
9. Demo shows 5+ realistic scenarios
10. UML diagram shows complete expression hierarchy

## References

- **Gang of Four**: Interpreter pattern for language grammars
- **Use Cases**: Query languages, config files, scripting, rule engines
- **Related Patterns**:
  - Composite (expression tree structure)
  - Visitor (alternative to distributed interpret() method)
  - Iterator (for traversing AST)

---

**Pattern #20 of 24 - Interpreter Pattern**
**Video Platform Context: Advanced Video Search Query Language**
**Next Pattern: #21 - TBD**
