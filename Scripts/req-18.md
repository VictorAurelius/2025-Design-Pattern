# REQ-18: Strategy Pattern - Video Compression Algorithms

## 📋 Pattern Information

**Pattern Name:** Strategy Pattern
**Pattern Type:** Behavioral Design Pattern
**Pattern Number:** #12 in StreamFlix cluster (5th Behavioral)
**Difficulty Level:** ⭐⭐⭐ Medium
**Prerequisites:** Interfaces, Polymorphism, Composition

## 🎯 Context: Video Compression Strategy Selection

### StreamFlix Pattern Cluster Progress

**Completed Patterns (11/24):**
1. ✅ Adapter (#1) - MediaPlayer compatibility
2. ✅ Observer (#2) - YouTube channel subscriptions
3. ✅ Proxy (#3) - StreamFlix access control
4. ✅ Flyweight (#4) - Video player UI icons
5. ✅ Builder (#5) - Video upload configuration
6. ✅ Factory Method (#6) - Video export formats
7. ✅ Abstract Factory (#7) - Video player themes
8. ✅ Prototype (#8) - Clone upload templates
9. ✅ Memento (#9) - Video editor undo/redo
10. ✅ Template Method (#10) - Video processing pipeline
11. ✅ State (#11) - Video player state management

**Current Pattern:**
12. 🔄 **Strategy (#12)** - Video compression algorithms

**Upcoming Patterns:**
13. Decorator - Video enhancement filters
14. Command - Player control commands
15. ... (12 more patterns)

### Pattern Linkage in StreamFlix Workflow

**Complete Video Processing Journey:**
```
Creator Side:
1. Build upload config (Builder #5)
   ↓
2. Clone from template (Prototype #8)
   ↓
3. Edit video with undo/redo (Memento #9)
   ↓
4. Upload to StreamFlix
   ↓
5. Process through pipeline (Template Method #10)
   ↓
6. 🆕 SELECT COMPRESSION ALGORITHM (Strategy #12) ← YOU ARE HERE
   ├─ H.264: Fast, universal compatibility
   ├─ H.265: Better compression, 4K/HDR support
   ├─ VP9: Open-source, YouTube default
   └─ AV1: Next-gen, best compression
   ↓
7. Notify subscribers (Observer #2)

Viewer Side:
8. Access StreamFlix (Proxy #3)
9. Choose player theme (Abstract Factory #7)
10. Watch video (State #11)
```

**Key Insight:** During video processing (Template Method #10), the system needs to select a compression algorithm. Different videos have different requirements (file size, quality, compatibility, processing speed). The Strategy pattern allows runtime selection of the optimal compression algorithm.

---

## 🔴 The Problem: Hardcoded Compression with If-Else Mess

### Current Situation: VideoCompressor Without Strategy Pattern

Currently, StreamFlix's video compressor uses a primitive approach with hardcoded algorithms and giant if-else statements:

```java
public class VideoCompressor {

    private String compressionType;  // "h264", "h265", "vp9", "av1"

    public void compress(String inputFile, String outputFile) {
        System.out.println("Compressing: " + inputFile);

        // Giant if-else based on compression type
        if (compressionType.equals("h264")) {
            // H.264 compression
            System.out.println("Using H.264 codec...");
            System.out.println("Compression ratio: 50:1");
            System.out.println("Speed: Fast (100 fps)");
            System.out.println("Compatibility: Universal");
            // Complex H.264 compression logic here...
        }
        else if (compressionType.equals("h265")) {
            // H.265 compression
            System.out.println("Using H.265 codec...");
            System.out.println("Compression ratio: 100:1");
            System.out.println("Speed: Medium (50 fps)");
            System.out.println("Compatibility: Modern devices");
            System.out.println("HDR support: Yes");
            // Complex H.265 compression logic here...
        }
        else if (compressionType.equals("vp9")) {
            // VP9 compression
            System.out.println("Using VP9 codec...");
            System.out.println("Compression ratio: 80:1");
            System.out.println("Speed: Medium (60 fps)");
            System.out.println("Compatibility: Chrome, Firefox");
            System.out.println("Open-source: Yes");
            // Complex VP9 compression logic here...
        }
        else if (compressionType.equals("av1")) {
            // AV1 compression
            System.out.println("Using AV1 codec...");
            System.out.println("Compression ratio: 150:1");
            System.out.println("Speed: Slow (20 fps)");
            System.out.println("Compatibility: Latest browsers");
            System.out.println("Royalty-free: Yes");
            // Complex AV1 compression logic here...
        }
        else {
            System.out.println("Unknown compression type: " + compressionType);
        }

        System.out.println("Compression complete!");
    }

    public void setCompressionType(String type) {
        this.compressionType = type;
    }
}
```

### Quantified Problems with This Approach

**Problem 1: Algorithm Not Encapsulated**
- Compression logic scattered throughout compress() method
- Each algorithm's code mixed with others
- 200+ lines of compression code in one method
- **Violates Single Responsibility Principle**

**Problem 2: If-Else Spaghetti Code**
- 4 if-else branches for 4 algorithms
- Adding new algorithm: Add another else-if branch
- Complex nested logic for algorithm parameters
- **Cyclomatic Complexity: 8+** (should be < 5)

**Problem 3: Cannot Swap Algorithms at Runtime**
- Must call setCompressionType() before compress()
- Easy to forget setting type → Uses default or crashes
- No compile-time guarantee type is set
- **Runtime errors from invalid type strings**

**Example Bug:**
```java
VideoCompressor compressor = new VideoCompressor();
// Forgot to set compression type!
compressor.compress("video.mp4", "output.mp4");
// NullPointerException or "Unknown compression type: null"
```

**Problem 4: String-Based Selection (No Type Safety)**
- compressionType is String ("h264", "h265", etc.)
- Typos cause runtime errors: "h265" vs "h256"
- No auto-completion in IDE
- No compile-time validation
- **Fragile and error-prone**

**Problem 5: Difficult to Test**
- Must test all algorithms through single compress() method
- Cannot mock individual algorithms
- Cannot test algorithms in isolation
- **Test complexity: O(n) where n = number of algorithms**

**Problem 6: Difficult to Add New Algorithms**
- Want to add VP8 compression?
- Must modify compress() method (add else-if)
- Must update all switch/if-else statements
- **Violates Open/Closed Principle** (not open for extension, requires modification)

**Adding VP8 algorithm:**
```java
public void compress(String inputFile, String outputFile) {
    if (compressionType.equals("h264")) { /* ... */ }
    else if (compressionType.equals("h265")) { /* ... */ }
    else if (compressionType.equals("vp9")) { /* ... */ }
    else if (compressionType.equals("av1")) { /* ... */ }
    else if (compressionType.equals("vp8")) {  // NEW BRANCH - modifies existing code
        // VP8 compression logic
    }
    // Must also update: validation, documentation, tests, etc.
}
```

**Problem 7: Code Duplication**
- Common compression setup duplicated in each branch
- Error handling duplicated
- Logging duplicated
- **DRY principle violated**

**Problem 8: Poor Encapsulation**
- Compression algorithms not isolated
- Parameters hard to customize per algorithm
- Cannot pass algorithm-specific options
- **Tight coupling between compressor and algorithms**

### Real-World Impact

**Scenario 1: Typo in Compression Type**
- Developer types: `compressor.setCompressionType("h256")` (typo: h256 vs h265)
- Runtime: "Unknown compression type: h256"
- Video processed with wrong algorithm or not at all
- **User impact:** 1,000+ videos processed incorrectly (must reprocess)
- **Time wasted:** 40 hours reprocessing + 10 hours debugging

**Scenario 2: Adding AV1 Algorithm**
- New requirement: Support AV1 for better compression
- Developer modifies compress() method (adds else-if)
- 200-line method grows to 250 lines
- Accidentally breaks H.265 logic (merged conflict)
- **Time wasted:** 8 hours implementation + 4 hours bug fixing

**Scenario 3: Algorithm-Specific Parameters**
- H.265 needs HDR settings
- AV1 needs encoder preset (slow, medium, fast)
- VP9 needs target bitrate
- Current design: Add more if-else for each parameter
- **Result:** Nested if-else hell, unmaintainable

**Scenario 4: Testing Nightmare**
- QA needs to test all 4 algorithms
- Must test through VideoCompressor.compress()
- Cannot mock individual algorithms
- Cannot test algorithms in isolation
- **Testing time:** 2 hours per algorithm × 4 = 8 hours

### Time Waste Statistics

**For StreamFlix Development Team (10 developers):**

**Bug Fixes (Wrong Compression Type):**
- Bugs per month: 3 (typos, wrong type)
- Time per bug: 3 hours (debug + reprocess videos)
- **Monthly:** 9 hours
- **Annual:** 108 hours

**Adding New Algorithms:**
- New algorithms per year: 2 (VP8, AV2)
- Time per algorithm: 8 hours (modify + test)
- **Annual:** 16 hours

**Code Reviews:**
- Reviews per month: 3 (compression-related)
- Time per review: 1 hour (complex if-else logic)
- **Annual:** 36 hours

**Testing:**
- Test cycles per month: 4
- Time per cycle: 2 hours (test all algorithms)
- **Annual:** 96 hours

**Video Reprocessing (Bugs):**
- Reprocessing events: 6 per year
- Videos per event: 500
- Time per event: 8 hours
- **Annual:** 48 hours

**Total Annual Time Waste:** 108 + 16 + 36 + 96 + 48 = **304 hours**

### Why Not Just Use Switch Statements?

**Attempt 1: Switch Statement**
```java
public void compress(String inputFile, String outputFile) {
    switch (compressionType) {
        case "h264":
            // H.264 logic
            break;
        case "h265":
            // H.265 logic
            break;
        case "vp9":
            // VP9 logic
            break;
        case "av1":
            // AV1 logic
            break;
        default:
            System.out.println("Unknown type");
    }
}
```

**Problems:**
- Still string-based (typo-prone)
- Still in one giant method
- Still violates Open/Closed Principle
- **Not much better than if-else**

**Attempt 2: Enum with Switch**
```java
enum CompressionType { H264, H265, VP9, AV1 }

public void compress(String inputFile, String outputFile) {
    switch (compressionType) {
        case H264:
            // H.264 logic
            break;
        // ... still 200+ lines in one method
    }
}
```

**Problems:**
- Better type safety (no typos)
- But still giant method
- Still violates Open/Closed Principle
- Cannot swap algorithm at runtime easily
- **Improved, but not ideal**

---

## ✅ The Solution: Strategy Pattern

### Core Concept

**Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.**

**Key Components:**
1. **Strategy Interface** - Defines common interface for all algorithms
2. **Concrete Strategies** - Implement specific algorithms (H264Strategy, H265Strategy, etc.)
3. **Context (VideoCompressor)** - Uses a Strategy, can swap strategies at runtime

### Strategy Pattern Structure

```java
// Strategy interface
public interface CompressionStrategy {
    void compress(String inputFile, String outputFile);
    String getCodecName();
    String getCompressionRatio();
}

// Context
public class VideoCompressor {
    private CompressionStrategy strategy;

    public VideoCompressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void compressVideo(String inputFile, String outputFile) {
        System.out.println("Starting compression...");
        strategy.compress(inputFile, outputFile);  // Delegate to strategy
        System.out.println("Compression complete!");
    }
}

// Concrete Strategy 1: H.264
public class H264Strategy implements CompressionStrategy {
    @Override
    public void compress(String inputFile, String outputFile) {
        System.out.println("Using H.264 codec...");
        System.out.println("Compression ratio: " + getCompressionRatio());
        System.out.println("Speed: Fast (100 fps)");
        System.out.println("Compatibility: Universal");
        // H.264 compression logic here
    }

    @Override
    public String getCodecName() {
        return "H.264/AVC";
    }

    @Override
    public String getCompressionRatio() {
        return "50:1";
    }
}

// Concrete Strategy 2: H.265
public class H265Strategy implements CompressionStrategy {
    private boolean hdrEnabled;

    public H265Strategy(boolean hdrEnabled) {
        this.hdrEnabled = hdrEnabled;
    }

    @Override
    public void compress(String inputFile, String outputFile) {
        System.out.println("Using H.265 codec...");
        System.out.println("Compression ratio: " + getCompressionRatio());
        System.out.println("Speed: Medium (50 fps)");
        System.out.println("HDR: " + (hdrEnabled ? "Enabled" : "Disabled"));
        // H.265 compression logic here
    }

    @Override
    public String getCodecName() {
        return "H.265/HEVC";
    }

    @Override
    public String getCompressionRatio() {
        return "100:1";
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        // Create compressor with H.264 strategy
        VideoCompressor compressor = new VideoCompressor(new H264Strategy());
        compressor.compressVideo("video.mp4", "output_h264.mp4");

        // Switch to H.265 strategy at runtime
        compressor.setStrategy(new H265Strategy(true));  // HDR enabled
        compressor.compressVideo("video.mp4", "output_h265.mp4");

        // Switch to VP9 strategy
        compressor.setStrategy(new VP9Strategy());
        compressor.compressVideo("video.mp4", "output_vp9.webm");
    }
}
```

### Benefits of Strategy Pattern

**1. Algorithms Encapsulated**
- Each algorithm in separate class
- H264Strategy, H265Strategy, VP9Strategy, AV1Strategy
- Single Responsibility: One class = one algorithm
- **200+ lines → 50 lines per class**

**2. No If-Else Spaghetti**
- VideoCompressor.compressVideo() has ZERO if-else
- Simply delegates to strategy.compress()
- **Cyclomatic Complexity: 8 → 1** (87.5% reduction)

**3. Runtime Algorithm Switching**
- Can change algorithm on the fly: `compressor.setStrategy(new H265Strategy())`
- No need to create new compressor
- **Flexible and dynamic**

**4. Type Safety**
- No string-based selection
- Compile-time guarantee strategy is valid
- IDE auto-completion for strategies
- **No typos, no runtime errors**

**5. Easy to Add New Algorithms**
- Create new class implementing CompressionStrategy
- NO changes to VideoCompressor
- NO changes to existing strategies
- **Open/Closed Principle: Open for extension, closed for modification**

**Adding VP8:**
```java
public class VP8Strategy implements CompressionStrategy {
    @Override
    public void compress(String inputFile, String outputFile) {
        System.out.println("Using VP8 codec...");
        // VP8 logic
    }

    @Override
    public String getCodecName() {
        return "VP8";
    }

    @Override
    public String getCompressionRatio() {
        return "60:1";
    }
}

// NO changes to VideoCompressor or other strategies!
```

**6. Easy to Test**
- Test each strategy independently
- Mock strategies for VideoCompressor tests
- Test strategy selection logic separately
- **Test complexity: O(1) per algorithm**

**7. Algorithm-Specific Parameters**
- Each strategy can have its own constructor parameters
- H265Strategy(boolean hdrEnabled)
- AV1Strategy(String preset, int crf)
- **Flexible configuration per algorithm**

**8. Clean Separation of Concerns**
- VideoCompressor: Manages compression workflow
- Strategies: Implement specific algorithms
- **Clear responsibilities**

---

## 📊 Requirements Analysis

### Functional Requirements

**FR1: Compression Strategies**
- System shall support 4 compression algorithms: H.264, H.265, VP9, AV1
- Each algorithm shall be encapsulated in separate strategy class
- Algorithms shall be interchangeable at runtime

**FR2: H.264 Strategy (Fast, Universal)**
- Compression ratio: 50:1
- Processing speed: Fast (100 fps)
- Compatibility: Universal (all devices)
- Use case: Standard definition videos, mobile uploads

**FR3: H.265 Strategy (High Quality, Modern)**
- Compression ratio: 100:1
- Processing speed: Medium (50 fps)
- Compatibility: Modern devices (2015+)
- HDR support: Optional
- Use case: 4K videos, premium content

**FR4: VP9 Strategy (Open-Source, Web)**
- Compression ratio: 80:1
- Processing speed: Medium (60 fps)
- Compatibility: Chrome, Firefox, Opera
- Open-source: Yes
- Use case: YouTube, web streaming

**FR5: AV1 Strategy (Next-Gen, Best Compression)**
- Compression ratio: 150:1
- Processing speed: Slow (20 fps)
- Compatibility: Latest browsers (2020+)
- Royalty-free: Yes
- Use case: High-quality archival, bandwidth-constrained

**FR6: Runtime Strategy Selection**
- Client can select strategy at creation time
- Client can change strategy after creation (setStrategy)
- Strategy changes take effect immediately

**FR7: Compression Interface**
- All strategies shall implement common CompressionStrategy interface
- Methods: compress(), getCodecName(), getCompressionRatio()
- Ensure consistent behavior across strategies

### Non-Functional Requirements

**NFR1: Code Quality**
- Each strategy class: < 100 lines
- VideoCompressor: < 50 lines
- Cyclomatic complexity per method: < 3
- No if-else based on algorithm type

**NFR2: Type Safety**
- No string-based algorithm selection
- Compile-time validation of strategies
- IDE auto-completion support

**NFR3: Extensibility**
- Adding new strategy: < 1 hour
- No modification to existing code (Open/Closed Principle)
- Minimal impact on tests

**NFR4: Testability**
- Each strategy independently testable
- Mock strategies for VideoCompressor tests
- Test coverage: > 90%

**NFR5: Performance**
- Strategy selection overhead: < 1ms
- No performance degradation vs hardcoded approach
- Compression speed determined by algorithm, not pattern overhead

**NFR6: Maintainability**
- Clear separation of algorithm logic
- Self-documenting code (strategy names descriptive)
- Easy to understand for new developers

---

## 🎯 Success Metrics

### Time Savings

**Before Strategy Pattern:**
- Bug fixes: 108 hours/year
- Adding algorithms: 16 hours/year
- Code reviews: 36 hours/year
- Testing: 96 hours/year
- Video reprocessing: 48 hours/year
- **Total:** 304 hours/year

**After Strategy Pattern:**
- Bug fixes: 10 hours/year (90% reduction - type safety eliminates typos)
- Adding algorithms: 2 hours/year (8× faster - just create new class)
- Code reviews: 10 hours/year (cleaner, simpler code)
- Testing: 20 hours/year (test strategies independently)
- Video reprocessing: 5 hours/year (fewer bugs)
- **Total:** 47 hours/year

**Annual Time Saved:** 304 - 47 = **257 hours**

### Code Metrics

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Lines in compress() method | 200+ | 5 | 97.5% reduction |
| If-else statements | 4-8 | 0 | 100% elimination |
| Cyclomatic complexity | 8+ | 1 | 87.5% reduction |
| Time to add algorithm | 8 hrs | 1 hr | 87.5% faster |
| Type safety bugs | 3/month | 0/month | 100% elimination |
| Test time per algorithm | 2 hrs | 0.5 hrs | 75% faster |

### ROI Calculation

**Development Cost:**
- Refactor to Strategy Pattern: 6 hours
- Testing: 3 hours
- Documentation: 1 hour
- **Total cost:** 10 hours

**Annual Savings:**
- Development time: 257 hours
- **Net savings:** 257 - 10 = 247 hours

**ROI:**
```
ROI = [(257 - 10) / 10] × 100 = 2,470%
```

### 5-Year Projection

| Year | Algorithms | Hours Saved | Cumulative |
|------|------------|-------------|------------|
| 1    | 4 → 5      | 257         | 257        |
| 2    | 5 → 6      | 257         | 514        |
| 3    | 6 → 8      | 257         | 771        |
| 4    | 8 → 10     | 257         | 1,028      |
| 5    | 10 → 12    | 257         | 1,285      |

**Total 5-year savings:** 1,285 hours
**5-year ROI:** [(1,285 - 10) / 10] × 100 = **12,750%**

---

## 🏗️ Implementation Plan

### Class Structure

```
18-Strategy-DP/
├── CompressionStrategy.java      (Strategy interface)
├── VideoCompressor.java          (Context)
├── H264Strategy.java             (Concrete strategy - fast, universal)
├── H265Strategy.java             (Concrete strategy - 4K, HDR)
├── VP9Strategy.java              (Concrete strategy - open-source, web)
├── AV1Strategy.java              (Concrete strategy - next-gen, best compression)
├── StrategyPatternDemo.java      (Client demo)
└── package.bluej                 (UML diagram)
```

### Compression Algorithm Comparison

| Algorithm | Compression Ratio | Speed | Compatibility | Use Case |
|-----------|------------------|-------|---------------|----------|
| **H.264** | 50:1 | Fast (100 fps) | Universal (99%+ devices) | Standard videos, mobile |
| **H.265** | 100:1 | Medium (50 fps) | Modern (2015+) | 4K, HDR, premium |
| **VP9** | 80:1 | Medium (60 fps) | Chrome, Firefox | YouTube, web streaming |
| **AV1** | 150:1 | Slow (20 fps) | Latest (2020+) | Archival, low bandwidth |

### Strategy Selection Logic

**Decision Tree:**
```
File size > 10GB?
├─ Yes → Use AV1 (best compression)
└─ No
   ├─ Need 4K/HDR?
   │  ├─ Yes → Use H.265
   │  └─ No → Continue
   ├─ Target web browsers?
   │  ├─ Yes → Use VP9
   │  └─ No → Use H.264 (universal)
```

**Example Usage:**
```java
// Mobile video upload (prioritize compatibility)
VideoCompressor compressor = new VideoCompressor(new H264Strategy());

// 4K HDR movie (prioritize quality)
compressor.setStrategy(new H265Strategy(true));  // HDR enabled

// YouTube upload (open-source, web-optimized)
compressor.setStrategy(new VP9Strategy());

// Archival footage (prioritize compression)
compressor.setStrategy(new AV1Strategy("slow", 28));  // Slow preset, CRF 28
```

---

## 🎨 UML Class Diagram (Preview)

```
┌──────────────────────────────────┐
│     <<interface>>                │
│   CompressionStrategy            │
├──────────────────────────────────┤
│ + compress(in, out): void        │
│ + getCodecName(): String         │
│ + getCompressionRatio(): String  │
└──────────────────────────────────┘
            ▲
            │ implements
            │
    ┌───────┼────────┬─────────┬────────┐
    │       │        │         │        │
┌───┴───┐ ┌─┴─────┐ ┌┴──────┐ ┌┴──────┐
│ H264  │ │ H265  │ │  VP9  │ │  AV1  │
│Strategy│ │Strategy│ │Strategy│ │Strategy│
└───────┘ └───────┘ └───────┘ └───────┘
                                  │
                                  │ uses
                                  │
                    ┌─────────────┴──────────┐
                    │   VideoCompressor      │
                    ├────────────────────────┤
                    │ - strategy: Compression│
                    │            Strategy    │
                    ├────────────────────────┤
                    │ + setStrategy(s): void │
                    │ + compressVideo(): void│
                    └────────────────────────┘
```

---

## 📝 Expected Output (Preview)

```
========================================
🎬 STRATEGY PATTERN DEMO - Video Compression
========================================

Testing 4 compression strategies:
1. H.264 - Fast, universal compatibility
2. H.265 - High quality, 4K/HDR support
3. VP9 - Open-source, web-optimized
4. AV1 - Next-gen, best compression

========================================
📹 SCENARIO 1: H.264 Strategy (Fast)
========================================

Selected Strategy: H.264/AVC
Compression Ratio: 50:1
Use Case: Standard videos, mobile uploads

Starting compression...
Using H.264 codec...
Compression ratio: 50:1
Speed: Fast (100 fps)
Compatibility: Universal (99%+ devices)
Processing: video.mp4 → output_h264.mp4
Compression complete!

File size: 1000 MB → 20 MB (98% reduction)
Processing time: 10 seconds

✓ H.264 compression successful

========================================
📹 SCENARIO 2: H.265 Strategy (4K HDR)
========================================

Switching strategy to: H.265/HEVC
Compression Ratio: 100:1
HDR Enabled: Yes

Starting compression...
Using H.265 codec...
Compression ratio: 100:1
Speed: Medium (50 fps)
Compatibility: Modern devices (2015+)
HDR: Enabled (HDR10)
Processing: 4k_video.mp4 → output_h265.mp4
Compression complete!

File size: 5000 MB → 50 MB (99% reduction)
Processing time: 100 seconds

✓ H.265 compression successful

========================================
✅ STRATEGY PATTERN BENEFITS
========================================

1. ✓ No If-Else Statements
   - 200+ lines → 5 lines in VideoCompressor
   - Cyclomatic complexity: 8 → 1

2. ✓ Easy Algorithm Switching
   - Runtime: setStrategy(new H265Strategy())
   - No code modification needed

3. ✓ Easy to Add Algorithms
   - Create new strategy class
   - Implement CompressionStrategy interface
   - No changes to existing code

4. ✓ Type Safety
   - No string-based selection
   - Compile-time validation
   - IDE auto-completion

5. ✓ Independent Testing
   - Test each strategy separately
   - Mock strategies for unit tests

ROI: 2,470% (Year 1), 12,750% (5 years)
Pattern #12 in StreamFlix cluster - Complete! ✓
```

---

## 🎓 Learning Objectives

After implementing this pattern, you will understand:

1. **Strategy Pattern Core Concept**
   - Encapsulate algorithms in separate classes
   - Make algorithms interchangeable
   - Client selects strategy at runtime

2. **Strategy vs State Pattern**
   - Strategy: Client selects algorithm explicitly
   - State: Object changes behavior based on internal state
   - Strategy: Algorithms independent
   - State: States know about each other

3. **Composition Over Inheritance**
   - Strategy uses composition (HAS-A)
   - Not inheritance (IS-A)
   - More flexible, runtime swapping

4. **Open/Closed Principle**
   - Open for extension (add new strategies)
   - Closed for modification (context unchanged)

5. **Dependency Injection**
   - Strategy passed to context via constructor
   - Enables testing with mock strategies
   - Loose coupling

---

## 🔗 Pattern Relationships

### Links to Previous Patterns

**Template Method (#10) vs Strategy (#12)**
- Template Method: Fixed algorithm structure, variable steps (inheritance)
- Strategy: Variable entire algorithm (composition)
- Template Method: Subclass overrides steps
- Strategy: Client selects strategy
- **Both eliminate if-else, different approaches**

**State (#11) vs Strategy (#12)**
- State: Behavior changes based on internal state (context changes state)
- Strategy: Behavior selected by client (client sets strategy)
- State: States transition automatically
- Strategy: No automatic transitions
- **Similar structure, different intent**

### Links to Future Patterns

**Strategy (#12) + Decorator (#13)**
- Strategy: Select algorithm
- Decorator: Add features to algorithm
- Can combine: DecoratedH265Strategy wraps H265Strategy

**Strategy (#12) + Factory Method (#6)**
- Factory Method creates strategy objects
- Example: CompressionStrategyFactory.create("h265")

---

## ✅ Definition of Done

Implementation is complete when:

- [x] Requirements document created (req-18.md)
- [ ] Solution documentation created (Documents/Solutions/Strategy.md)
- [ ] CompressionStrategy.java implemented (interface)
- [ ] VideoCompressor.java implemented (context)
- [ ] H264Strategy.java implemented (fast, universal)
- [ ] H265Strategy.java implemented (4K, HDR)
- [ ] VP9Strategy.java implemented (open-source, web)
- [ ] AV1Strategy.java implemented (next-gen, best compression)
- [ ] StrategyPatternDemo.java implemented (comprehensive demo)
- [ ] package.bluej created (UML diagram)
- [ ] All files compile successfully
- [ ] Demo output matches expected output
- [ ] Code demonstrates all learning objectives

---

## 📚 References

**Pattern Catalog:**
- Gang of Four: Strategy (p. 315)
- Head First Design Patterns: Chapter 1

**Real-World Examples:**
- Java Collections: Comparator (sort strategy)
- Java IO: BufferedReader with different strategies
- Compression tools: 7-Zip, WinRAR (multiple algorithms)
- Video encoders: FFmpeg (supports 100+ codecs)

**Key Principle:**
- Composition over inheritance
- Open/Closed Principle: Open for extension, closed for modification

---

**Pattern #12 Requirements Complete!**
**Next Step:** Implement with `do req-18`
**Estimated Implementation Time:** 2 hours
**Estimated Learning Value:** ⭐⭐⭐⭐⭐ (Fundamental behavioral pattern, widely used)
