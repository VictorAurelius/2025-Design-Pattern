# Strategy Design Pattern - Video Compression Algorithms

## Pattern Description

The **Strategy Pattern** is a behavioral design pattern that defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it. This pattern is all about **composition over inheritance** and enables **runtime algorithm selection**.

### Intent
- Define a family of algorithms (strategies)
- Encapsulate each algorithm in a separate class
- Make algorithms interchangeable
- Let clients select algorithm at runtime
- Eliminate conditional statements based on algorithm type

### Key Components
1. **Strategy Interface** - Defines common interface for all algorithms (CompressionStrategy)
2. **Concrete Strategies** - Implement specific algorithms (H264Strategy, H265Strategy, VP9Strategy, AV1Strategy)
3. **Context** - Uses a strategy, can swap strategies at runtime (VideoCompressor)

### Core Principle: Composition Over Inheritance

**Before Strategy (Inheritance):**
```java
public abstract class VideoCompressor {
    public abstract void compress();
}

public class H264Compressor extends VideoCompressor {
    public void compress() { /* H.264 logic */ }
}

public class H265Compressor extends VideoCompressor {
    public void compress() { /* H.265 logic */ }
}

// Problem: Cannot change compression algorithm at runtime!
// Must create new compressor object
H264Compressor compressor = new H264Compressor();
// To switch: compressor = new H265Compressor();  // New object!
```

**After Strategy (Composition):**
```java
public class VideoCompressor {
    private CompressionStrategy strategy;

    public void setStrategy(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void compress() {
        strategy.compress();  // Delegate to strategy
    }
}

// Can change algorithm on same object!
VideoCompressor compressor = new VideoCompressor(new H264Strategy());
compressor.setStrategy(new H265Strategy());  // Runtime switch!
```

### Strategy vs State Pattern (Critical Distinction)

Both patterns have similar structure but **completely different intent**:

**Strategy Pattern:**
```java
// CLIENT selects strategy explicitly
VideoCompressor compressor = new VideoCompressor(new H264Strategy());
compressor.setStrategy(new H265Strategy());  // Client decides

// Strategies are INDEPENDENT
public class H264Strategy {
    public void compress() {
        // H.264 compression logic
        // Does NOT change to another strategy
    }
}
```

**State Pattern:**
```java
// CONTEXT changes state internally
VideoPlayer player = new VideoPlayer();
player.play();  // State decides transition

// States are DEPENDENT (know about each other)
public class PlayingState {
    public void pause(VideoPlayer player) {
        player.setState(new PausedState());  // State changes itself
    }
}
```

**Summary:**
- **Strategy:** Client-driven selection, algorithms independent
- **State:** Context-driven transition, states know each other
- **Strategy:** "Let me choose the algorithm"
- **State:** "I change behavior based on my internal condition"

---

## Problem: Video Compression with Hardcoded Algorithms

### Context: StreamFlix Video Compression Workflow

**This is Pattern #12 in our StreamFlix cluster** (5th Behavioral pattern)

**Pattern Linkage:**
- Links to **Template Method (#10)**: Videos processed through pipeline
- Links to **State (#11)**: Player manages playback state
- **NEW: Compression Strategy (#12)**: Select optimal compression algorithm during processing

**Complete Processing Journey:**
```
Creator Side:
1. Build upload config (Builder #5)
2. Clone from template (Prototype #8)
3. Edit video with undo/redo (Memento #9)
4. Upload to StreamFlix
5. Process through pipeline (Template Method #10)
   ↓
6. SELECT COMPRESSION ALGORITHM (Strategy #12) ← YOU ARE HERE
   ├─ H.264: Fast, universal compatibility
   ├─ H.265: Better compression, 4K/HDR support
   ├─ VP9: Open-source, web-optimized
   └─ AV1: Next-gen, best compression
   ↓
7. Notify subscribers (Observer #2)

Viewer Side:
8. Access StreamFlix (Proxy #3)
9. Choose player theme (Abstract Factory #7)
10. Watch video (State #11)
```

### The Problem Without Strategy Pattern

**Current Implementation: Hardcoded If-Else Hell**

```java
public class VideoCompressor {

    private String compressionType;  // "h264", "h265", "vp9", "av1"

    public void compress(String inputFile, String outputFile) {
        System.out.println("Compressing: " + inputFile);

        // GIANT IF-ELSE BASED ON COMPRESSION TYPE
        if (compressionType.equals("h264")) {
            // H.264 compression (50+ lines)
            System.out.println("Using H.264 codec...");
            System.out.println("Compression ratio: 50:1");
            System.out.println("Speed: Fast (100 fps)");
            System.out.println("Compatibility: Universal");
            // Complex H.264 compression logic...
        }
        else if (compressionType.equals("h265")) {
            // H.265 compression (50+ lines)
            System.out.println("Using H.265 codec...");
            System.out.println("Compression ratio: 100:1");
            System.out.println("Speed: Medium (50 fps)");
            System.out.println("HDR support: Yes");
            // Complex H.265 compression logic...
        }
        else if (compressionType.equals("vp9")) {
            // VP9 compression (50+ lines)
            System.out.println("Using VP9 codec...");
            System.out.println("Compression ratio: 80:1");
            System.out.println("Open-source: Yes");
            // Complex VP9 compression logic...
        }
        else if (compressionType.equals("av1")) {
            // AV1 compression (50+ lines)
            System.out.println("Using AV1 codec...");
            System.out.println("Compression ratio: 150:1");
            System.out.println("Speed: Slow (20 fps)");
            // Complex AV1 compression logic...
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

### Quantified Problems

**Problem 1: If-Else Spaghetti Code**
- compress() method: 200+ lines
- 4-8 if-else branches
- Each branch: 50+ lines
- **Cyclomatic Complexity: 8+** (should be < 5)
- **Violates Single Responsibility Principle**

**Problem 2: String-Based Selection (No Type Safety)**
```java
compressor.setCompressionType("h264");   // OK
compressor.setCompressionType("h256");   // TYPO! Runtime error
compressor.setCompressionType("H264");   // Case-sensitive! Runtime error
```
- No compile-time validation
- No IDE auto-completion
- Typos discovered at runtime
- **Fragile and error-prone**

**Problem 3: Cannot Swap Algorithms Easily**
```java
VideoCompressor compressor = new VideoCompressor();
compressor.setCompressionType("h264");
compressor.compress("video1.mp4", "output1.mp4");

// Want to use H.265 for next video
compressor.setCompressionType("h265");
compressor.compress("video2.mp4", "output2.mp4");

// Easy to forget setCompressionType() call!
compressor.compress("video3.mp4", "output3.mp4");  // Still uses h265!
```

**Problem 4: Difficult to Add New Algorithms**
```java
// Want to add VP8 codec?
public void compress(String inputFile, String outputFile) {
    if (compressionType.equals("h264")) { /* ... */ }
    else if (compressionType.equals("h265")) { /* ... */ }
    else if (compressionType.equals("vp9")) { /* ... */ }
    else if (compressionType.equals("av1")) { /* ... */ }
    else if (compressionType.equals("vp8")) {  // MUST MODIFY compress()
        // VP8 logic
    }
}
// Violates Open/Closed Principle (not open for extension)
```

**Problem 5: Cannot Test Algorithms Independently**
- All algorithms in one compress() method
- Must test through VideoCompressor
- Cannot mock individual algorithms
- **Test time: 2 hours per algorithm**

**Problem 6: Algorithm-Specific Parameters Difficult**
```java
// H.265 needs HDR option, AV1 needs preset
if (compressionType.equals("h265")) {
    if (hdrEnabled) {  // Another nested if!
        // H.265 with HDR
    } else {
        // H.265 without HDR
    }
}
// Nested if-else hell grows exponentially!
```

### Real-World Impact

**Scenario 1: Typo in Compression Type**
- Developer: `compressor.setCompressionType("h256")`  (typo: h256 vs h265)
- Runtime: "Unknown compression type: h256"
- Result: 1,000 videos processed with default/wrong algorithm
- **Time wasted:** 40 hours reprocessing + 10 hours debugging

**Scenario 2: Adding AV1 Algorithm**
- Requirement: Support next-gen AV1 codec
- Must modify compress() method (add else-if)
- 200-line method grows to 250 lines
- Risk: Accidentally break existing algorithms
- **Time wasted:** 8 hours implementation + 4 hours bug fixing

**Scenario 3: Algorithm Selection Logic Error**
- Developer forgets to call setCompressionType()
- NullPointerException at runtime
- Videos processed incorrectly
- **Support tickets:** 50+ complaints

### Time Waste Analysis

**For StreamFlix Team (10 developers):**

**Bug Fixes (Wrong Compression Type):**
- Bugs per month: 3
- Time per bug: 3 hours
- **Annual:** 108 hours

**Adding New Algorithms:**
- New algorithms per year: 2
- Time per algorithm: 8 hours
- **Annual:** 16 hours

**Code Reviews:**
- Reviews per month: 3
- Time per review: 1 hour
- **Annual:** 36 hours

**Testing:**
- Test cycles per month: 4
- Time per cycle: 2 hours
- **Annual:** 96 hours

**Video Reprocessing:**
- Events per year: 6
- Time per event: 8 hours
- **Annual:** 48 hours

**Total Annual Time Waste:** 108 + 16 + 36 + 96 + 48 = **304 hours**

---

## Requirements Analysis

### Functional Requirements

**FR1: Compression Strategies**
- System shall support 4 compression algorithms: H.264, H.265, VP9, AV1
- Each algorithm shall be encapsulated in separate strategy class
- Algorithms shall implement common CompressionStrategy interface

**FR2: H.264 Strategy (Fast, Universal)**
- Codec: H.264/AVC
- Compression ratio: 50:1
- Processing speed: Fast (100 fps)
- Compatibility: Universal (99%+ devices)
- Use case: Standard definition videos, mobile uploads

**FR3: H.265 Strategy (High Quality, Modern)**
- Codec: H.265/HEVC
- Compression ratio: 100:1
- Processing speed: Medium (50 fps)
- Compatibility: Modern devices (2015+)
- HDR support: Optional (constructor parameter)
- Use case: 4K videos, premium content, HDR

**FR4: VP9 Strategy (Open-Source, Web)**
- Codec: VP9
- Compression ratio: 80:1
- Processing speed: Medium (60 fps)
- Compatibility: Chrome, Firefox, Opera
- Open-source: Yes
- Use case: YouTube, web streaming

**FR5: AV1 Strategy (Next-Gen, Best Compression)**
- Codec: AV1
- Compression ratio: 150:1
- Processing speed: Slow (20 fps)
- Compatibility: Latest browsers (2020+)
- Royalty-free: Yes
- Use case: High-quality archival, bandwidth-constrained

**FR6: Runtime Strategy Selection**
- VideoCompressor shall accept strategy via constructor
- VideoCompressor shall allow strategy change via setStrategy()
- Strategy changes shall take effect immediately

**FR7: Compression Interface**
- compress(inputFile, outputFile) - Perform compression
- getCodecName() - Get codec name for display
- getCompressionRatio() - Get compression ratio for display

### Non-Functional Requirements

**NFR1: Code Quality**
- VideoCompressor: < 50 lines
- Each strategy class: < 100 lines
- Cyclomatic complexity per method: < 3
- No if-else based on algorithm type

**NFR2: Type Safety**
- No string-based algorithm selection
- Compile-time validation of strategies
- IDE auto-completion for strategies

**NFR3: Extensibility**
- Adding new strategy: < 1 hour
- No modification to VideoCompressor
- No modification to existing strategies
- Open/Closed Principle compliance

**NFR4: Testability**
- Each strategy independently testable
- Mock strategies for VideoCompressor tests
- Test coverage: > 90%

**NFR5: Performance**
- Strategy selection overhead: < 1ms
- No performance degradation vs hardcoded approach

**NFR6: Maintainability**
- Clear separation of algorithm logic
- Self-documenting code
- Easy onboarding for new developers

---

## Effectiveness Analysis

### Time Savings Calculation

**Before Strategy Pattern:**
- Bug fixes: 108 hours/year
- Adding algorithms: 16 hours/year
- Code reviews: 36 hours/year
- Testing: 96 hours/year
- Video reprocessing: 48 hours/year
- **Total:** 304 hours/year

**After Strategy Pattern:**
- Bug fixes: 10 hours/year (90% reduction - type safety)
- Adding algorithms: 2 hours/year (8× faster - just create class)
- Code reviews: 10 hours/year (cleaner code)
- Testing: 20 hours/year (independent testing)
- Video reprocessing: 5 hours/year (fewer bugs)
- **Total:** 47 hours/year

**Annual Time Saved:** 304 - 47 = **257 hours**

### Code Metrics Comparison

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Lines in compress() | 200+ | 5 | 97.5% reduction |
| If-else statements | 4-8 | 0 | 100% elimination |
| Cyclomatic complexity | 8+ | 1 | 87.5% reduction |
| Time to add algorithm | 8 hrs | 1 hr | 87.5% faster |
| Type safety bugs | 3/month | 0/month | 100% elimination |
| Test time per algorithm | 2 hrs | 0.5 hrs | 75% faster |

### ROI Analysis

**Development Cost:**
- Refactor to Strategy Pattern: 6 hours
- Testing: 3 hours
- Documentation: 1 hour
- **Total cost:** 10 hours

**Annual Savings:**
- Development time: 257 hours
- **Net savings:** 257 - 10 = 247 hours

**ROI Calculation:**
```
ROI = [(257 - 10) / 10] × 100 = 2,470%
```

### 5-Year Projection

| Year | Algorithms | Hours Saved | Cumulative Savings |
|------|------------|-------------|--------------------|
| 1    | 4 → 5      | 257         | 257                |
| 2    | 5 → 6      | 257         | 514                |
| 3    | 6 → 8      | 257         | 771                |
| 4    | 8 → 10     | 257         | 1,028              |
| 5    | 10 → 12    | 257         | 1,285              |

**Total 5-year savings:** 1,285 hours
**5-year ROI:** [(1,285 - 10) / 10] × 100 = **12,750%**

### Qualitative Benefits

**1. Compile-Time Safety**
- No string typos ("h256" vs "h265")
- IDE auto-completion for strategies
- Errors caught during development, not in production

**2. Clean Code**
- Each algorithm in separate class (Single Responsibility)
- VideoCompressor is simple (delegates to strategy)
- Easy to read and understand

**3. Flexible Configuration**
- Each strategy can have its own parameters
- H265Strategy(boolean hdrEnabled)
- AV1Strategy(String preset, int crf)

**4. Independent Testing**
- Test H264Strategy without VideoCompressor
- Mock strategies for integration tests
- Faster test cycles

**5. Easier Debugging**
- Print current strategy: `strategy.getCodecName()`
- Trace which algorithm is being used
- No need to inspect string variables

---

## Implementation

### Project Structure
```
18-Strategy-DP/
├── CompressionStrategy.java      (Strategy interface)
├── VideoCompressor.java          (Context)
├── H264Strategy.java             (Concrete strategy)
├── H265Strategy.java             (Concrete strategy)
├── VP9Strategy.java              (Concrete strategy)
├── AV1Strategy.java              (Concrete strategy)
├── StrategyPatternDemo.java      (Client demo)
└── package.bluej                 (UML diagram)
```

### UML Class Diagram

```
┌──────────────────────────────────────────────┐
│          <<interface>>                       │
│        CompressionStrategy                   │
├──────────────────────────────────────────────┤
│ + compress(in, out): void                    │
│ + getCodecName(): String                     │
│ + getCompressionRatio(): String              │
│ + getDescription(): String                   │
└──────────────────────────────────────────────┘
            ▲
            │ implements
            │
    ┌───────┼────────┬─────────┬────────┐
    │       │        │         │        │
┌───┴───┐ ┌─┴─────┐ ┌┴──────┐ ┌┴──────┐
│ H264  │ │ H265  │ │  VP9  │ │  AV1  │
│Strategy│ │Strategy│ │Strategy│ │Strategy│
├───────┤ ├───────┤ ├───────┤ ├───────┤
│       │ │-hdr   │ │       │ │-preset│
│       │ │Enabled│ │       │ │-crf   │
└───────┘ └───────┘ └───────┘ └───────┘
                                  │
                                  │ uses
                                  │
                    ┌─────────────┴──────────────────┐
                    │      VideoCompressor           │
                    ├────────────────────────────────┤
                    │ - strategy: CompressionStrategy│
                    ├────────────────────────────────┤
                    │ + VideoCompressor(strategy)    │
                    │ + setStrategy(strategy): void  │
                    │ + compressVideo(in, out): void │
                    │ + showStrategyInfo(): void     │
                    └────────────────────────────────┘
```

### Compression Algorithm Comparison

| Algorithm | Compression Ratio | Speed | Compatibility | Best For |
|-----------|------------------|-------|---------------|----------|
| **H.264** | 50:1 | Fast (100 fps) | Universal (99%+) | Standard videos, mobile |
| **H.265** | 100:1 | Medium (50 fps) | Modern (2015+) | 4K, HDR, premium |
| **VP9** | 80:1 | Medium (60 fps) | Web browsers | YouTube, web streaming |
| **AV1** | 150:1 | Slow (20 fps) | Latest (2020+) | Archival, low bandwidth |

### Key Implementation Details

#### 1. CompressionStrategy Interface

```java
public interface CompressionStrategy {
    void compress(String inputFile, String outputFile);
    String getCodecName();
    String getCompressionRatio();
    String getDescription();
}
```

**Why interface instead of abstract class?**
- Java single inheritance limitation
- Strategies may need to extend other classes
- Interface provides pure contract definition
- Allows multiple interface implementation if needed

#### 2. VideoCompressor Context

```java
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
        strategy.compress(inputFile, outputFile);  // Delegate!
        System.out.println("Compression complete!");
    }
}
```

**Key points:**
- Composition (HAS-A strategy)
- Delegates to strategy (no if-else)
- Can swap strategy at runtime
- Simple and clean

#### 3. Concrete Strategy Example: H265Strategy

```java
public class H265Strategy implements CompressionStrategy {
    private boolean hdrEnabled;

    public H265Strategy(boolean hdrEnabled) {
        this.hdrEnabled = hdrEnabled;
    }

    @Override
    public void compress(String inputFile, String outputFile) {
        System.out.println("Using " + getCodecName() + "...");
        System.out.println("Compression ratio: " + getCompressionRatio());
        System.out.println("Speed: Medium (50 fps)");
        System.out.println("HDR: " + (hdrEnabled ? "Enabled" : "Disabled"));
        // H.265 compression logic
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
```

**Strategy-specific parameters:**
- Each strategy can have its own configuration
- H265Strategy has hdrEnabled parameter
- AV1Strategy could have preset, crf parameters
- Flexible and extensible

#### 4. Usage Example

```java
// Create compressor with H.264 strategy
VideoCompressor compressor = new VideoCompressor(new H264Strategy());
compressor.compressVideo("video.mp4", "output_h264.mp4");

// Switch to H.265 with HDR at runtime
compressor.setStrategy(new H265Strategy(true));
compressor.compressVideo("4k_video.mp4", "output_h265.mp4");

// Switch to VP9 for web
compressor.setStrategy(new VP9Strategy());
compressor.compressVideo("web_video.mp4", "output_vp9.webm");

// Switch to AV1 for archival
compressor.setStrategy(new AV1Strategy("slow", 28));
compressor.compressVideo("archive.mp4", "output_av1.mp4");
```

---

## Expected Output

```
========================================
🎬 STRATEGY PATTERN DEMO - Video Compression Algorithms
========================================

Available compression strategies:
1. H.264/AVC - Fast, universal compatibility
2. H.265/HEVC - High quality, 4K/HDR support
3. VP9 - Open-source, web-optimized
4. AV1 - Next-gen, best compression

========================================
📹 SCENARIO 1: H.264 Strategy (Fast, Universal)
========================================

Selected Strategy: H.264/AVC
Compression Ratio: 50:1
Description: Fast encoding, universal device compatibility

Compressing: video.mp4 → output_h264.mp4

Starting compression...
Using H.264/AVC codec...
Compression ratio: 50:1
Processing speed: Fast (100 fps)
Compatibility: Universal (99%+ devices)
Target bitrate: 5000 kbps
Profile: High
Level: 4.1

Processing video...
████████████████████████████████ 100%

File size: 1000 MB → 20 MB (98% reduction)
Processing time: 10 seconds

Compression complete!

✓ H.264 compression successful

========================================
📹 SCENARIO 2: H.265 Strategy (4K HDR)
========================================

Switching strategy to: H.265/HEVC
Compression Ratio: 100:1
Description: High-quality 4K/HDR encoding

Compressing: 4k_video.mp4 → output_h265.mp4

Starting compression...
Using H.265/HEVC codec...
Compression ratio: 100:1
Processing speed: Medium (50 fps)
Compatibility: Modern devices (2015+)
HDR: Enabled (HDR10)
Color space: BT.2020
Bit depth: 10-bit
Profile: Main 10

Processing video...
████████████████████████████████ 100%

File size: 5000 MB → 50 MB (99% reduction)
Processing time: 100 seconds

Compression complete!

✓ H.265 compression successful with HDR

========================================
📹 SCENARIO 3: VP9 Strategy (Open-Source, Web)
========================================

Switching strategy to: VP9
Compression Ratio: 80:1
Description: Open-source codec optimized for web streaming

Compressing: web_video.mp4 → output_vp9.webm

Starting compression...
Using VP9 codec...
Compression ratio: 80:1
Processing speed: Medium (60 fps)
Compatibility: Chrome, Firefox, Opera
Open-source: Yes
Target quality: CQ 30
Tile columns: 2
Threads: 4

Processing video...
████████████████████████████████ 100%

File size: 2000 MB → 25 MB (98.75% reduction)
Processing time: 50 seconds

Compression complete!

✓ VP9 compression successful

========================================
📹 SCENARIO 4: AV1 Strategy (Next-Gen)
========================================

Switching strategy to: AV1
Compression Ratio: 150:1
Description: Next-generation codec with best compression efficiency

Compressing: archive.mp4 → output_av1.mp4

Starting compression...
Using AV1 codec...
Compression ratio: 150:1
Processing speed: Slow (20 fps)
Compatibility: Latest browsers (2020+)
Royalty-free: Yes
Encoder preset: Slow (maximum quality)
CRF: 28
CPU threads: 8
Tile rows/columns: 2/2

Processing video...
████████████████████████████████ 100%

File size: 8000 MB → 53 MB (99.34% reduction)
Processing time: 400 seconds (6m 40s)

Compression complete!

✓ AV1 compression successful - best compression achieved!

========================================
📊 COMPRESSION COMPARISON
========================================

Video: test_video.mp4 (original: 1000 MB)

Algorithm    | Output Size | Reduction | Time   | Quality
-------------|-------------|-----------|--------|--------
H.264        | 20 MB       | 98.00%    | 10s    | Good
H.265 (HDR)  | 10 MB       | 99.00%    | 20s    | Excellent
VP9          | 12.5 MB     | 98.75%    | 16s    | Very Good
AV1          | 6.7 MB      | 99.33%    | 50s    | Excellent

Best compression: AV1 (99.33% reduction)
Fastest: H.264 (10 seconds)
Best quality/size: H.265 with HDR

========================================
✅ STRATEGY PATTERN BENEFITS DEMONSTRATED
========================================

1. ✓ No If-Else Statements
   - VideoCompressor: 200+ lines → 5 lines
   - Cyclomatic complexity: 8 → 1 (87.5% reduction)

2. ✓ Easy Algorithm Switching
   - Runtime: setStrategy(new H265Strategy())
   - No code modification needed
   - Same VideoCompressor object

3. ✓ Easy to Add Algorithms
   - Create new strategy class
   - Implement CompressionStrategy interface
   - Zero changes to VideoCompressor
   - Zero changes to existing strategies

4. ✓ Type Safety
   - No string-based selection
   - Compile-time validation
   - IDE auto-completion
   - No typos, no runtime errors

5. ✓ Algorithm-Specific Parameters
   - H265Strategy(boolean hdrEnabled)
   - AV1Strategy(String preset, int crf)
   - Flexible configuration per algorithm

6. ✓ Independent Testing
   - Test each strategy separately
   - Mock strategies for unit tests
   - 75% faster testing

7. ✓ Composition Over Inheritance
   - VideoCompressor HAS-A strategy (composition)
   - Can swap strategies at runtime
   - More flexible than inheritance

8. ✓ Open/Closed Principle
   - Open for extension (add strategies)
   - Closed for modification (VideoCompressor unchanged)

========================================
🎓 KEY LEARNING POINTS
========================================

Strategy Pattern teaches:

1. **Encapsulate Algorithms**
   - Each algorithm in separate class
   - Single Responsibility Principle
   - Clean separation of concerns

2. **Composition Over Inheritance**
   - HAS-A relationship (composition)
   - Runtime algorithm swapping
   - More flexible than IS-A (inheritance)

3. **Runtime Selection**
   - Client selects algorithm
   - Can change on same object
   - Dynamic behavior

4. **Strategy vs State**
   - Strategy: Client selects (explicit)
   - State: Context transitions (automatic)
   - Strategy: Algorithms independent
   - State: States know each other

5. **Open/Closed Principle**
   - Add strategies without modifying context
   - Extend through new classes
   - Minimal code changes

========================================
📈 ROI SUMMARY
========================================

Before Strategy Pattern:
  - If-else statements: 4-8
  - Lines in compress(): 200+
  - Cyclomatic complexity: 8+
  - Type safety bugs: 3 per month
  - Annual time waste: 304 hours

After Strategy Pattern:
  - If-else statements: 0
  - Lines in compressVideo(): 5
  - Cyclomatic complexity: 1
  - Type safety bugs: 0 per month
  - Annual time saved: 257 hours

ROI: 2,470% (Year 1), 12,750% (5 years)

Pattern #12 in StreamFlix cluster - Complete! ✓
```

---

## UML Class Diagram (Detailed)

```
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                 CompressionStrategy                         │
├─────────────────────────────────────────────────────────────┤
│ + compress(inputFile: String, outputFile: String): void    │
│ + getCodecName(): String                                    │
│ + getCompressionRatio(): String                             │
│ + getDescription(): String                                  │
└─────────────────────────────────────────────────────────────┘
                          ▲
                          │ implements
          ┌───────────────┼──────────────┬───────────┬────────────┐
          │               │              │           │            │
┌─────────┴─────────┐ ┌───┴────────────┐ ┌┴─────────┐ ┌──────────┴─────┐
│   H264Strategy    │ │  H265Strategy  │ │VP9Strategy│ │  AV1Strategy   │
├───────────────────┤ ├────────────────┤ ├───────────┤ ├────────────────┤
│                   │ │ - hdrEnabled   │ │           │ │ - preset: Str  │
│                   │ │                │ │           │ │ - crf: int     │
├───────────────────┤ ├────────────────┤ ├───────────┤ ├────────────────┤
│ + compress()      │ │ + compress()   │ │+ compress()│ │ + compress()   │
│ + getCodecName()  │ │ + getCodecName()│ │+ getCodec..│ │ + getCodecName()│
│ + getCompression..│ │ + getCompres...│ │+ getCompr..│ │ + getCompres...│
│ + getDescription()│ │ + getDescripti..│ │+ getDescr..│ │ + getDescription()│
└───────────────────┘ └────────────────┘ └───────────┘ └────────────────┘
                                                              │
                                                              │ uses
                                                              │
                                             ┌────────────────┴──────────────┐
                                             │      VideoCompressor          │
                                             ├───────────────────────────────┤
                                             │ - strategy: CompressionStrategy│
                                             ├───────────────────────────────┤
                                             │ + VideoCompressor(strategy)   │
                                             │ + setStrategy(strategy): void │
                                             │ + compressVideo(in, out): void│
                                             │ + showStrategyInfo(): void    │
                                             │ + getStrategy(): Compression..│
                                             └───────────────────────────────┘

RELATIONSHIPS:
═══════════════════════════════════════════════════════════════════════
1. All concrete strategies implement CompressionStrategy interface
2. VideoCompressor maintains reference to current strategy
3. Client creates strategies and passes to VideoCompressor
4. VideoCompressor delegates compression to current strategy
5. Strategies are independent (don't know about each other)
```

---

## Conclusion

### Pattern Summary

The **Strategy Pattern** is essential for eliminating conditional logic when selecting between multiple algorithms. It's one of the most widely-used patterns in software development because it provides **flexibility, extensibility, and clean code**.

**Core Achievement: Algorithm Interchangeability**
- Encapsulate algorithms in separate classes
- Select algorithm at runtime via composition
- No if-else statements based on algorithm type
- **Type-safe, flexible, and maintainable**

**Key Benefits:**
1. **No If-Else:** 200+ lines → 5 lines in context (97.5% reduction)
2. **Type Safety:** Compile-time validation (no string typos)
3. **Easy Extension:** Add strategies without modifying context (Open/Closed Principle)
4. **Runtime Switching:** Change algorithm on same object (composition)

**ROI: 2,470% Year 1, 12,750% over 5 years**

### When to Use Strategy Pattern

✅ **Use Strategy when:**
- Multiple algorithms for same task (compression, sorting, validation)
- Need to select algorithm at runtime
- Want to eliminate if-else based on algorithm type
- Algorithms should be interchangeable
- Need to add new algorithms frequently

❌ **Avoid Strategy when:**
- Only one algorithm exists (no need for pattern)
- Algorithm never changes (just use simple method)
- Algorithms are not independent (use State instead)
- Overhead not justified (very simple algorithms)

### Strategy vs State Pattern (Final Clarification)

**Strategy Pattern (This Implementation):**
```java
// CLIENT selects algorithm explicitly
VideoCompressor compressor = new VideoCompressor(new H264Strategy());
compressor.setStrategy(new H265Strategy());  // Client decides when to switch

// Use case: User chooses compression algorithm based on needs
```

**State Pattern (Previous - #11):**
```java
// CONTEXT changes state automatically based on conditions
VideoPlayer player = new VideoPlayer();
player.play();  // May transition to BufferingState if network slow

// Use case: Player automatically buffers when network conditions change
```

**Summary:**
- **Strategy:** "Let me choose the algorithm" (client-driven)
- **State:** "I change behavior based on my condition" (context-driven)
- **Same structure, completely different intent!**

### Pattern Linkage in StreamFlix Cluster

This is **Pattern #12** in our cluster (5th Behavioral pattern):

**Previous Patterns:**
1. Adapter (#1) - MediaPlayer compatibility
2. Observer (#2) - YouTube channel subscriptions
3. Proxy (#3) - StreamFlix access control
4. Flyweight (#4) - Video player UI icons
5. Builder (#5) - Video upload configuration
6. Factory Method (#6) - Video export formats
7. Abstract Factory (#7) - Video player themes
8. Prototype (#8) - Clone upload templates
9. Memento (#9) - Video editor undo/redo
10. Template Method (#10) - Video processing pipeline
11. State (#11) - Video player state management

**Current Pattern:**
12. **Strategy (#12)** - Video compression algorithms

**Context Links:**
- Videos processed through pipeline (Template Method #10)
- **During processing, select compression algorithm (Strategy #12)**
- After compression, notify subscribers (Observer #2)
- Complete processing workflow: Upload → Process → Compress → Notify

### Real-World Applications Beyond StreamFlix

**1. Java Collections:**
```java
Collections.sort(list, new Comparator<String>() {
    public int compare(String a, String b) {
        return a.length() - b.length();  // Sort by length
    }
});
// Comparator is a strategy for sorting algorithms
```

**2. Payment Processing:**
```java
public class PaymentProcessor {
    private PaymentStrategy strategy;

    public void processPayment(double amount) {
        strategy.pay(amount);
    }
}

// Strategies: CreditCardStrategy, PayPalStrategy, CryptoStrategy
```

**3. File Compression:**
```java
// 7-Zip, WinRAR support multiple algorithms
FileCompressor compressor = new FileCompressor();
compressor.setStrategy(new ZipStrategy());    // ZIP
compressor.setStrategy(new GZipStrategy());   // GZIP
compressor.setStrategy(new BZip2Strategy());  // BZIP2
```

**4. Route Planning:**
```java
// Google Maps route selection
RouteCalculator calculator = new RouteCalculator();
calculator.setStrategy(new FastestRouteStrategy());   // Fastest
calculator.setStrategy(new ShortestRouteStrategy());  // Shortest
calculator.setStrategy(new ScenicRouteStrategy());    // Scenic
```

**5. Validation:**
```java
// Form validation with different strategies
Validator validator = new Validator();
validator.setStrategy(new EmailValidationStrategy());
validator.setStrategy(new PhoneValidationStrategy());
validator.setStrategy(new CreditCardValidationStrategy());
```

### Implementation Checklist

When implementing Strategy pattern, ensure:

- [x] Strategy interface defines common contract
- [x] Each concrete strategy implements interface
- [x] Context maintains reference to strategy (composition)
- [x] Context delegates to strategy (no if-else)
- [x] Strategies are independent (don't know about each other)
- [x] Client can select/change strategy at runtime
- [x] Strategies can have their own parameters
- [x] No string-based selection (type-safe)

### Final Thoughts

The Strategy pattern is **one of the most important patterns** in software development. It embodies the **Open/Closed Principle** and **Composition Over Inheritance** principles perfectly.

For StreamFlix's video compression, the Strategy pattern ensures:
- **Zero if-else statements** (100% elimination)
- **Type-safe selection** (no string typos)
- **Easy algorithm addition** (create class, no modification)
- **Independent testing** (75% faster)

The pattern saved **257 hours annually** by eliminating type safety bugs, simplifying code reviews, speeding up testing, and making algorithm addition trivial. More importantly, it provides a **solid foundation** for future compression algorithms (VP8, AV2, etc.).

**Pattern #12 completed. The video compression system is now flexible, maintainable, and extensible!**

---

**Pattern Cluster Progress: 12/24 patterns implemented (50% - HALFWAY MILESTONE!)**

**Next patterns to implement:**
- Decorator (Structural) - Video enhancement filters
- Command (Behavioral) - Player control commands
- Chain of Responsibility (Behavioral) - Request handling chain
- ... 12 more patterns

**🎉 CONGRATULATIONS - 50% COMPLETE! 🎉**
