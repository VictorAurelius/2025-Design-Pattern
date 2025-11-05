# Factory Method Pattern Solution

## Pattern: Factory Method (Creational)
**Context**: Video Export System for StreamFlix Platform
**Date**: 2025-11-05

---

## 1. Pattern Description

### What is Factory Method Pattern?

The **Factory Method Pattern** is a creational design pattern that defines an interface for creating objects, but lets subclasses decide which class to instantiate. Factory Method lets a class defer instantiation to subclasses.

### Key Components:

1. **Product (Interface)**: Defines the interface of objects the factory method creates
   - Example: `VideoExporter` interface

2. **Concrete Products**: Implement the Product interface
   - Examples: `MP4Exporter`, `AVIExporter`, `MOVExporter`, `WebMExporter`

3. **Creator (Abstract Class)**: Declares the factory method
   - Returns a Product type object
   - May contain a template method that uses the factory method
   - Example: `ExporterFactory` abstract class

4. **Concrete Creators**: Override the factory method to return specific product instances
   - Examples: `MP4ExporterFactory`, `AVIExporterFactory`, etc.

### UML Structure:

```
┌─────────────────────┐
│      Product        │ ← Product Interface
│   <<interface>>     │
└─────────────────────┘
          △
          │ implements
     ┌────┴────┐
     │         │
┌────┴───┐ ┌──┴─────┐
│Product1│ │Product2│ ← Concrete Products
└────────┘ └────────┘


┌─────────────────────┐
│      Creator        │ ← Creator (Abstract)
├─────────────────────┤
│+ factoryMethod()    │ ← Factory Method (abstract)
│+ operation()        │ ← Template Method
└─────────────────────┘
          △
          │ extends
     ┌────┴────┐
     │         │
┌────┴─────┐ ┌┴────────┐
│Creator1  │ │Creator2 │ ← Concrete Creators
└──────────┘ └─────────┘
     │            │
     │ creates    │ creates
     ↓            ↓
┌────────┐   ┌────────┐
│Product1│   │Product2│
└────────┘   └────────┘
```

### Key Principle:

**"Define an interface for creating an object, but let subclasses decide which class to instantiate."**

- The Creator class is **open for extension** (add new creators) but **closed for modification**
- Each product type gets its own factory
- Client code depends on abstractions, not concrete classes

---

## 2. Problem Statement (with Context Linking)

### Real-World Context: StreamFlix Video Platform

**Previous patterns in this cluster:**
1. **Adapter** - MediaPlayer (play mp3, mp4, vlc files)
2. **Observer** - YouTube Channel (notify subscribers)
3. **Proxy** - StreamFlix (lazy load videos)
4. **Flyweight** - Video Player UI Icons (share icons to save memory)
5. **Builder** - Video Upload Configuration (12 parameters)
6. **Factory Method** - Video Export System ← **NEW!**

### The Problem:

StreamFlix needs to **export videos** in different formats for various devices and purposes:

- **MP4**: Most compatible, web streaming, mobile devices
- **AVI**: High quality, video editing, archival
- **MOV**: Apple devices, professional editing, Final Cut Pro
- **WebM**: Web optimization, smaller file size, YouTube

### Current Implementation (WITHOUT Factory Method):

```java
public class VideoProcessor {

    public void exportVideo(String format, String videoPath) {

        if (format.equals("MP4")) {
            // MP4 export logic
            System.out.println("Exporting to MP4...");
            System.out.println("Using H.264 codec");
            System.out.println("Setting bitrate: 5000kbps");
            System.out.println("Container: MPEG-4");
            System.out.println("File extension: .mp4");
            // ... 50 lines of MP4-specific code

        } else if (format.equals("AVI")) {
            // AVI export logic
            System.out.println("Exporting to AVI...");
            System.out.println("Using XVID codec");
            System.out.println("Setting bitrate: 8000kbps");
            System.out.println("Container: AVI");
            System.out.println("File extension: .avi");
            // ... 50 lines of AVI-specific code

        } else if (format.equals("MOV")) {
            // MOV export logic
            System.out.println("Exporting to MOV...");
            System.out.println("Using Apple ProRes codec");
            System.out.println("Setting bitrate: 12000kbps");
            System.out.println("Container: QuickTime");
            System.out.println("File extension: .mov");
            // ... 50 lines of MOV-specific code

        } else if (format.equals("WEBM")) {
            // WebM export logic
            System.out.println("Exporting to WebM...");
            System.out.println("Using VP9 codec");
            System.out.println("Setting bitrate: 3000kbps");
            System.out.println("Container: Matroska");
            System.out.println("File extension: .webm");
            // ... 50 lines of WebM-specific code
        }
        // Total: 200+ lines in ONE method!
    }
}
```

### Problems with This Approach:

#### 1. Violation of Open/Closed Principle
```java
// Want to add GIF export?
// Must MODIFY existing VideoProcessor class!
else if (format.equals("GIF")) {
    // ... 50 more lines
}
// This violates OCP: Should be open for extension, closed for modification
```

#### 2. Violation of Single Responsibility Principle
- VideoProcessor knows about ALL export formats
- Handles MP4 logic, AVI logic, MOV logic, WebM logic
- One class doing 4+ different jobs!

#### 3. Code Duplication
```java
// Similar code repeated in each branch
System.out.println("Exporting to " + format + "...");
System.out.println("Using " + codec + " codec");
System.out.println("Setting bitrate: " + bitrate + "kbps");
// This pattern repeats 4 times!
```

#### 4. Tight Coupling
```java
// Client code is tightly coupled to string constants
processor.exportVideo("MP4", video);  // What if typo? "Mp4"? "mp4"?
processor.exportVideo("WEBM", video); // No type safety!
```

#### 5. Difficult to Test
```java
// Must test entire switch statement
// Cannot test MP4 export in isolation
// Cannot mock specific exporters
// Must set up all 4 formats to test one!
```

#### 6. Not Extensible
```java
// Business requirement: "Support GIF export for social media"
// Developer must:
// 1. Open VideoProcessor.java (risk breaking existing code)
// 2. Add another if-else branch
// 3. Copy-paste 50 lines of export logic
// 4. Retest ALL existing formats (regression risk)
// 5. 2 hours of work + 2 hours of testing
```

#### 7. Poor Readability
```java
// 200+ lines of nested if-else
// Hard to find specific format logic
// Hard to understand what each format does
// Cognitive overload!
```

### Real-World Impact:

**Scenario**: Marketing team wants GIF export for Twitter/Instagram

**Without Factory Method:**
- Time to implement: 2 hours
- Risk of breaking existing exports: HIGH
- Code added to existing class: 50+ lines
- Testing effort: 4 hours (must retest all formats)
- **Total cost**: 6 hours

**With Factory Method:**
- Time to implement: 30 minutes
- Risk of breaking existing exports: ZERO
- New classes created: 2 (GIFExporter + GIFExporterFactory)
- Testing effort: 30 minutes (only test GIF)
- **Total cost**: 1 hour

**Savings**: 5 hours (500% faster!)

### Why Factory Method Solves This:

1. **Open/Closed Principle**: Add new format = Create new classes (NO modifications)
2. **Single Responsibility**: Each exporter handles ONE format
3. **No Duplication**: Common logic in abstract creator, specific logic in concrete products
4. **Loose Coupling**: Client depends on interfaces, not concrete classes
5. **Easy Testing**: Test each exporter independently
6. **Extensible**: Add formats in minutes, not hours
7. **Readable**: Each format has its own clean class

---

## 3. Requirements Analysis

### Functional Requirements:

1. **Export videos in multiple formats**
   - MP4 (H.264 codec, 5000 kbps, web streaming)
   - AVI (XVID codec, 8000 kbps, video editing)
   - MOV (Apple ProRes codec, 12000 kbps, Apple devices)
   - WebM (VP9 codec, 3000 kbps, web optimization)

2. **Format-specific configuration**
   - Each format has unique codec
   - Each format has unique bitrate
   - Each format has unique file extension
   - Each format has unique use case

3. **Polymorphic export**
   - Treat all exporters uniformly
   - Batch export to multiple formats
   - Device-specific format selection

4. **Extensibility**
   - Add new formats without modifying existing code
   - Easy to add GIF, FLV, MKV, etc.

### Non-Functional Requirements:

1. **Maintainability**
   - Each format in its own class
   - Easy to find and modify specific format logic
   - No giant switch statements

2. **Testability**
   - Test each format independently
   - Mock factories easily
   - No dependencies between formats

3. **Type Safety**
   - No string-based format selection
   - Compile-time checking
   - IDE autocomplete

4. **Performance**
   - Minimal overhead for factory creation
   - Lazy instantiation possible
   - No performance penalty vs if-else

### Design Requirements:

1. **Follow SOLID Principles**
   - Single Responsibility: One format per class
   - Open/Closed: Extensible without modification
   - Liskov Substitution: All exporters interchangeable
   - Interface Segregation: Minimal interface
   - Dependency Inversion: Depend on abstractions

2. **Use Factory Method Pattern Correctly**
   - Product interface: `VideoExporter`
   - Concrete products: 4 exporter classes
   - Abstract creator: `ExporterFactory`
   - Concrete creators: 4 factory classes
   - Factory method: `createExporter()`

3. **Provide Template Method**
   - Common export workflow in abstract creator
   - Specific instantiation in concrete creators
   - `exportVideo()` method uses factory method

---

## 4. Pattern Effectiveness Analysis

### How Factory Method Solves Each Problem:

#### Problem 1: Violation of Open/Closed Principle
**Before:**
```java
// Must modify VideoProcessor to add GIF
else if (format.equals("GIF")) {
    // New code here
}
```

**After:**
```java
// Just create 2 new classes - NO modifications!
public class GIFExporter implements VideoExporter { }
public class GIFExporterFactory extends ExporterFactory { }
```

**Benefit**: Open for extension, closed for modification ✅

#### Problem 2: Violation of Single Responsibility
**Before:**
```java
// One class handles all formats
class VideoProcessor {
    // MP4 logic
    // AVI logic
    // MOV logic
    // WebM logic
    // 4 responsibilities!
}
```

**After:**
```java
// Each class handles ONE format
class MP4Exporter { /* Only MP4 logic */ }
class AVIExporter { /* Only AVI logic */ }
class MOVExporter { /* Only MOV logic */ }
class WebMExporter { /* Only WebM logic */ }
```

**Benefit**: Single Responsibility Principle ✅

#### Problem 3: Code Duplication
**Before:**
```java
// Repeated in each if-else branch
System.out.println("Exporting to " + format + "...");
System.out.println("Using " + codec);
// Duplicated 4 times!
```

**After:**
```java
// Common logic in abstract class (Template Method)
public abstract class ExporterFactory {
    public void exportVideo(String path) {
        VideoExporter exporter = createExporter();  // Factory Method
        exporter.export(path);  // Polymorphism
    }
}
```

**Benefit**: DRY (Don't Repeat Yourself) ✅

#### Problem 4: Tight Coupling
**Before:**
```java
// Tightly coupled to strings
processor.exportVideo("MP4", video);  // Typo risk!
processor.exportVideo("mp4", video);  // Case sensitive?
```

**After:**
```java
// Loosely coupled to interfaces
ExporterFactory factory = new MP4ExporterFactory();  // Type safe!
VideoExporter exporter = factory.createExporter();
exporter.export(video);  // Polymorphic call
```

**Benefit**: Loose Coupling + Type Safety ✅

#### Problem 5: Difficult to Test
**Before:**
```java
// Must test entire switch statement
@Test
public void testExport() {
    processor.exportVideo("MP4", "test.mp4");
    // Also tests AVI, MOV, WebM logic paths!
}
```

**After:**
```java
// Test each format independently
@Test
public void testMP4Export() {
    VideoExporter exporter = new MP4Exporter();
    exporter.export("test.mp4");
    // Only tests MP4 logic!
}

@Test
public void testFactoryCreation() {
    ExporterFactory factory = new MP4ExporterFactory();
    VideoExporter exporter = factory.createExporter();
    assertTrue(exporter instanceof MP4Exporter);
}
```

**Benefit**: Easy Unit Testing ✅

#### Problem 6: Not Extensible
**Before:**
```java
// Adding GIF: 2 hours (modify existing code + retest all)
// Risk: Breaking existing exports
```

**After:**
```java
// Adding GIF: 30 minutes (create 2 new classes)
// Risk: Zero (no changes to existing code)

public class GIFExporter implements VideoExporter {
    public void export(String path) {
        System.out.println("Exporting to GIF (animated)");
        System.out.println("Using GIF89a encoding");
        System.out.println("Frame rate: 24 fps");
    }
}

public class GIFExporterFactory extends ExporterFactory {
    public VideoExporter createExporter() {
        return new GIFExporter();
    }
}
```

**Benefit**: Highly Extensible ✅

#### Problem 7: Poor Readability
**Before:**
```java
// 200+ lines of nested if-else
public void exportVideo(String format, String path) {
    if (format.equals("MP4")) {
        // 50 lines
    } else if (format.equals("AVI")) {
        // 50 lines
    } else if (format.equals("MOV")) {
        // 50 lines
    } else if (format.equals("WEBM")) {
        // 50 lines
    }
}
```

**After:**
```java
// Each format is self-contained and clear
public class MP4Exporter implements VideoExporter {
    public void export(String path) {
        // 15 clean lines of MP4 logic
    }
}

public class AVIExporter implements VideoExporter {
    public void export(String path) {
        // 15 clean lines of AVI logic
    }
}
// Easy to find, easy to read!
```

**Benefit**: High Readability ✅

### Quantitative Benefits:

| Metric | Without Factory Method | With Factory Method | Improvement |
|--------|------------------------|---------------------|-------------|
| Lines per format | 50 lines (in one file) | 15 lines (separate file) | 70% reduction |
| Time to add format | 2 hours | 30 minutes | 4x faster |
| Risk of breaking code | HIGH | ZERO | ∞ safer |
| Test coverage | Hard (coupled) | Easy (isolated) | 5x better |
| Code readability | Poor (nested if-else) | Excellent (clean classes) | 10x better |
| Maintainability | Low (modify existing) | High (add new) | 10x better |

### ROI Calculation:

**Initial Investment:**
- Time to set up Factory Method: 3 hours
- Number of initial formats: 4 formats
- Total setup: 3 hours

**Annual Returns:**
- New formats per year: 3 formats
- Time saved per format: 1.5 hours
- Annual time saved: 4.5 hours

**5-Year ROI:**
- Total time saved: 22.5 hours
- ROI: (22.5 - 3) / 3 = 650%
- **Result**: 6.5x return on investment!

**Plus intangible benefits:**
- Fewer bugs (no regression)
- Easier onboarding (clean code)
- Better team morale (no spaghetti code)

---

## 5. Implementation

### File Structure:

```
12-Factory-Method-DP/
│
├── VideoExporter.java              (Product Interface)
├── MP4Exporter.java                (Concrete Product)
├── AVIExporter.java                (Concrete Product)
├── MOVExporter.java                (Concrete Product)
├── WebMExporter.java               (Concrete Product)
├── ExporterFactory.java            (Abstract Creator)
├── MP4ExporterFactory.java         (Concrete Creator)
├── AVIExporterFactory.java         (Concrete Creator)
├── MOVExporterFactory.java         (Concrete Creator)
├── WebMExporterFactory.java        (Concrete Creator)
├── FactoryMethodDemo.java          (Client)
└── package.bluej                   (UML Diagram)
```

### Implementation Details:

#### 1. Product Interface: VideoExporter.java

```java
public interface VideoExporter {

    // Factory Method pattern: Product interface
    void export(String videoPath);

    // Additional methods for format info
    String getFormat();
    int getBitrate();
    String getCodec();
    String getFileExtension();
}
```

**Design decisions:**
- Simple interface with essential methods
- `export()` is the main operation
- Getter methods for format metadata
- No implementation details (deferred to concrete classes)

#### 2. Concrete Products: MP4Exporter.java, etc.

```java
public class MP4Exporter implements VideoExporter {

    @Override
    public void export(String videoPath) {
        System.out.println("\n[MP4Exporter] Starting export...");
        System.out.println("  📁 Input: " + videoPath);
        System.out.println("  📦 Output: " + videoPath.replace(".mp4", "_exported.mp4"));
        System.out.println("  🎞️  Codec: " + getCodec());
        System.out.println("  📊 Bitrate: " + getBitrate() + " kbps");
        System.out.println("  🎯 Use case: Web streaming, mobile devices");
        System.out.println("  ⏱️  Processing... [████████████████████] 100%");
        System.out.println("  ✓ MP4 export completed!");
    }

    @Override
    public String getFormat() {
        return "MP4";
    }

    @Override
    public int getBitrate() {
        return 5000;  // kbps
    }

    @Override
    public String getCodec() {
        return "H.264 (High Efficiency)";
    }

    @Override
    public String getFileExtension() {
        return ".mp4";
    }
}
```

**Design decisions:**
- Each exporter is self-contained
- Format-specific constants (codec, bitrate)
- Clear, descriptive output
- Simulated export process

**Similar implementation for:**
- `AVIExporter` (XVID, 8000 kbps)
- `MOVExporter` (Apple ProRes, 12000 kbps)
- `WebMExporter` (VP9, 3000 kbps)

#### 3. Abstract Creator: ExporterFactory.java

```java
public abstract class ExporterFactory {

    // Factory Method (abstract) - subclasses implement this
    public abstract VideoExporter createExporter();

    // Template Method - uses factory method
    public void exportVideo(String videoPath) {
        // 1. Create exporter using factory method
        VideoExporter exporter = createExporter();

        // 2. Use the exporter polymorphically
        exporter.export(videoPath);
    }

    // Convenience static method for factory selection
    public static ExporterFactory getFactory(String format) {
        switch (format.toUpperCase()) {
            case "MP4":
                return new MP4ExporterFactory();
            case "AVI":
                return new AVIExporterFactory();
            case "MOV":
                return new MOVExporterFactory();
            case "WEBM":
                return new WebMExporterFactory();
            default:
                throw new IllegalArgumentException("Unknown format: " + format);
        }
    }
}
```

**Design decisions:**
- Abstract factory method: `createExporter()`
- Template method: `exportVideo()` (common workflow)
- Static helper: `getFactory()` (convenience)
- Throws exception for unknown formats

#### 4. Concrete Creators: MP4ExporterFactory.java, etc.

```java
public class MP4ExporterFactory extends ExporterFactory {

    @Override
    public VideoExporter createExporter() {
        System.out.println("[Factory] Creating MP4 Exporter...");
        return new MP4Exporter();
    }
}
```

**Design decisions:**
- Simple factory method implementation
- Returns specific product type
- Logs factory creation (for demo)

**Similar implementation for:**
- `AVIExporterFactory`
- `MOVExporterFactory`
- `WebMExporterFactory`

#### 5. Client: FactoryMethodDemo.java

```java
public class FactoryMethodDemo {

    public static void main(String[] args) {

        // Show problem
        demonstrateProblem();

        // Show solution
        demonstrateSolution();

        // Show polymorphism
        demonstratePolymorphism();

        // Show extensibility
        demonstrateExtensibility();
    }

    private static void demonstrateProblem() {
        System.out.println("PROBLEM: Without Factory Method");
        System.out.println("- Switch/if-else everywhere");
        System.out.println("- Adding format = Modifying code");
        System.out.println("- Tight coupling");
    }

    private static void demonstrateSolution() {
        System.out.println("SOLUTION: With Factory Method");

        // Method 1: Direct factory creation
        ExporterFactory factory = new MP4ExporterFactory();
        VideoExporter exporter = factory.createExporter();
        exporter.export("/videos/tutorial.mp4");

        // Method 2: Using template method
        factory.exportVideo("/videos/tutorial.mp4");

        // Method 3: Using static helper
        factory = ExporterFactory.getFactory("MP4");
        factory.exportVideo("/videos/tutorial.mp4");
    }

    private static void demonstratePolymorphism() {
        System.out.println("POLYMORPHISM: Batch export");

        // Array of factories
        ExporterFactory[] factories = {
            new MP4ExporterFactory(),
            new AVIExporterFactory(),
            new MOVExporterFactory(),
            new WebMExporterFactory()
        };

        // Export to all formats polymorphically
        for (ExporterFactory factory : factories) {
            factory.exportVideo("/videos/demo.mp4");
        }
    }

    private static void demonstrateExtensibility() {
        System.out.println("EXTENSIBILITY: Adding GIF");
        System.out.println("1. Create GIFExporter (implements VideoExporter)");
        System.out.println("2. Create GIFExporterFactory (extends ExporterFactory)");
        System.out.println("3. Done! No changes to existing code!");
    }
}
```

**Design decisions:**
- Demonstrates problem first (context)
- Shows multiple usage methods
- Demonstrates polymorphism
- Shows extensibility benefit

### Key Implementation Patterns:

1. **Factory Method Pattern**:
   - Abstract `createExporter()` in ExporterFactory
   - Concrete implementation in each factory

2. **Template Method Pattern**:
   - `exportVideo()` defines the workflow
   - Uses factory method to get product

3. **Polymorphism**:
   - All exporters implement VideoExporter
   - Treat all exporters uniformly

4. **Open/Closed Principle**:
   - Abstract creator is closed for modification
   - Concrete creators extend for new formats

---

## 6. Expected Output

```
╔════════════════════════════════════════════════════════════╗
║           FACTORY METHOD PATTERN DEMO                      ║
║              Video Export System                           ║
║  (Linked: StreamFlix Video Platform patterns)             ║
╚════════════════════════════════════════════════════════════╝


═══════════════════════════════════════════════════════════
PROBLEM: Without Factory Method Pattern
═══════════════════════════════════════════════════════════

❌ Problems:
   1. Switch/if-else statements everywhere
   2. Adding new format = Modifying existing code (violates OCP)
   3. Cannot extend easily
   4. Tight coupling to concrete classes
   5. Code duplication

Example of ugly code:
   if (format.equals("MP4")) { /* 50 lines */ }
   else if (format.equals("AVI")) { /* 50 lines */ }
   else if (format.equals("MOV")) { /* 50 lines */ }
   // Want to add GIF? Must modify this code! 😢


═══════════════════════════════════════════════════════════
SOLUTION: Factory Method Pattern
═══════════════════════════════════════════════════════════

✅ Benefits:
   1. Each format has its own class (Single Responsibility)
   2. Adding new format = Create new factory (Open/Closed)
   3. Client depends on interface (Loose Coupling)
   4. Easy to test each format independently


═══════════════════════════════════════════════════════════
TEST 1: Exporting with Different Formats
═══════════════════════════════════════════════════════════

🎬 Exporting video: /videos/tutorial.mp4

--- Using MP4 Factory ---
[Factory] Creating MP4 Exporter...

[MP4Exporter] Starting export...
  📁 Input: /videos/tutorial.mp4
  📦 Output: /videos/tutorial_exported.mp4
  🎞️  Codec: H.264 (High Efficiency)
  📊 Bitrate: 5000 kbps
  🎯 Use case: Web streaming, mobile devices
  ⏱️  Processing... [████████████████████] 100%
  ✓ MP4 export completed!

--- Using AVI Factory ---
[Factory] Creating AVI Exporter...

[AVIExporter] Starting export...
  📁 Input: /videos/tutorial.mp4
  📦 Output: /videos/tutorial_exported.avi
  🎞️  Codec: XVID (High Quality)
  📊 Bitrate: 8000 kbps
  🎯 Use case: Video editing, archival
  ⏱️  Processing... [████████████████████] 100%
  ✓ AVI export completed!

--- Using MOV Factory ---
[Factory] Creating MOV Exporter...

[MOVExporter] Starting export...
  📁 Input: /videos/tutorial.mp4
  📦 Output: /videos/tutorial_exported.mov
  🎞️  Codec: Apple ProRes (Professional)
  📊 Bitrate: 12000 kbps
  🎯 Use case: Apple devices, professional editing
  ⏱️  Processing... [████████████████████] 100%
  ✓ MOV export completed!

--- Using WebM Factory ---
[Factory] Creating WebM Exporter...

[WebMExporter] Starting export...
  📁 Input: /videos/tutorial.mp4
  📦 Output: /videos/tutorial_exported.webm
  🎞️  Codec: VP9 (Web Optimized)
  📊 Bitrate: 3000 kbps
  🎯 Use case: Web optimization, smaller file size
  ⏱️  Processing... [████████████████████] 100%
  ✓ WebM export completed!


═══════════════════════════════════════════════════════════
TEST 2: Polymorphism (Batch Export)
═══════════════════════════════════════════════════════════

🎯 Processing batch export for: /videos/demo.mp4

Exporter 1: MP4 (H.264) - 5000 kbps
Exporter 2: AVI (XVID) - 8000 kbps
Exporter 3: MOV (Apple ProRes) - 12000 kbps
Exporter 4: WebM (VP9) - 3000 kbps

✓ All 4 formats exported successfully!


═══════════════════════════════════════════════════════════
TEST 3: Device-Specific Export
═══════════════════════════════════════════════════════════

📱 User Device: iPhone
   → Recommended format: MOV
   → Using MOVExporterFactory
   ✓ Export completed!

📱 User Device: Android Phone
   → Recommended format: MP4
   → Using MP4ExporterFactory
   ✓ Export completed!

💻 User Device: Web Browser
   → Recommended format: WebM
   → Using WebMExporterFactory
   ✓ Export completed!

🎬 User Device: Video Editor
   → Recommended format: AVI
   → Using AVIExporterFactory
   ✓ Export completed!


═══════════════════════════════════════════════════════════
TEST 4: Extensibility (Adding New Format)
═══════════════════════════════════════════════════════════

💡 Scenario: We need to add GIF export for social media

❌ WITHOUT Factory Method:
   1. Modify VideoProcessor class (violates OCP)
   2. Add another if-else branch
   3. Risk breaking existing code
   4. Must retest all formats
   ⏱️  Time: 2 hours

✅ WITH Factory Method:
   1. Create GIFExporter class (implements VideoExporter)
   2. Create GIFExporterFactory class (extends ExporterFactory)
   3. Done! No changes to existing code!
   4. Only test new GIF functionality
   ⏱️  Time: 30 minutes

🎉 Result: 4x faster! 500% improvement!


═══════════════════════════════════════════════════════════
BENEFITS SUMMARY
═══════════════════════════════════════════════════════════

✅ Advantages:
   1. OPEN/CLOSED: Add format without modifying existing code
   2. SINGLE RESPONSIBILITY: Each exporter handles one format
   3. LOOSE COUPLING: Client depends on interface, not concrete classes
   4. TESTABILITY: Test each format independently
   5. EXTENSIBILITY: New formats in 30 minutes
   6. POLYMORPHISM: Treat all exporters uniformly
   7. TYPE SAFETY: Compile-time checking

⚠️  Trade-offs:
   1. More classes (4 products + 4 creators = 8 classes)
   2. Initial setup time (3 hours)
   3. May be overkill for 2-3 simple formats

📊 ROI Calculation:
   - Initial effort: 3 hours to set up Factory Method
   - Time saved per new format: 1.5 hours
   - Formats added per year: 3 formats
   - Total saved: 4.5 hours/year per developer
   - ROI: 650% over 5 years!


╔════════════════════════════════════════════════════════════╗
║                 CONTEXT LINKING                            ║
╚════════════════════════════════════════════════════════════╝

🎬 Video Platform Design Pattern Cluster (6 patterns):
   1. ADAPTER: MediaPlayer (mp3, mp4, vlc compatibility)
   2. OBSERVER: YouTube Channel (notification system)
   3. PROXY: StreamFlix (lazy video loading)
   4. FLYWEIGHT: Video Player UI (share icons, save 9,000x memory)
   5. BUILDER: Video Upload (12 params without telescoping hell)
   6. FACTORY METHOD: Video Export (4 formats, extensible)

💡 Complete Video Platform Workflow:
   📥 1. Upload video (BUILDER with 12 parameters)
   💾 2. Store video (PROXY for lazy loading)
   ▶️  3. Play video (ADAPTER for format compatibility)
   🎨 4. Show UI icons (FLYWEIGHT for memory optimization)
   🔔 5. Notify subscribers (OBSERVER pattern)
   📤 6. Export video (FACTORY METHOD for formats)

🧠 Memorization Strategy:
   All 6 patterns in ONE domain = Super easy to remember!
   Think: "Video Platform" → Recall all 6 patterns instantly! ⚡

════════════════════════════════════════════════════════════
```

---

## 7. UML Class Diagram

```
┌─────────────────────────────────────────┐
│         VideoExporter                   │ ← Product (Interface)
│         <<interface>>                   │
├─────────────────────────────────────────┤
│ + export(videoPath: String): void      │
│ + getFormat(): String                   │
│ + getBitrate(): int                     │
│ + getCodec(): String                    │
│ + getFileExtension(): String            │
└─────────────────────────────────────────┘
                    △
                    │ implements
        ┌───────────┼───────────┬───────────┐
        │           │           │           │
┌───────┴────┐ ┌───┴────┐ ┌────┴───┐ ┌────┴───────┐
│MP4Exporter │ │AVIExp..│ │MOVExp..│ │WebMExporter│
├────────────┤ ├────────┤ ├────────┤ ├────────────┤
│+export()   │ │+export()│ │+export()│ │+export()   │
│+getFormat()│ │+get...()│ │+get...()│ │+get...()   │
└────────────┘ └────────┘ └────────┘ └────────────┘
   H.264         XVID      ProRes       VP9
  5000kbps     8000kbps   12000kbps   3000kbps



┌──────────────────────────────────────────────┐
│         ExporterFactory                      │ ← Creator (Abstract)
│         <<abstract>>                         │
├──────────────────────────────────────────────┤
│ + createExporter(): VideoExporter            │ ← Factory Method
│ + exportVideo(videoPath: String): void       │ ← Template Method
│ + {static} getFactory(format: String): ...   │ ← Helper
└──────────────────────────────────────────────┘
                    △
                    │ extends
        ┌───────────┼────────────┬────────────┐
        │           │            │            │
┌───────┴────────┐ ┌┴──────────┐ ┌┴──────────┐ ┌┴────────────┐
│MP4ExporterFac..│ │AVIExport..│ │MOVExport..│ │WebMExporter.│
├────────────────┤ ├───────────┤ ├───────────┤ ├─────────────┤
│+createExporter()│ │+create...()│ │+create...()│ │+create...() │
└────────────────┘ └───────────┘ └───────────┘ └─────────────┘
        │                │              │              │
        │ creates        │ creates      │ creates      │ creates
        ↓                ↓              ↓              ↓
┌───────────┐     ┌───────────┐  ┌───────────┐  ┌───────────┐
│MP4Exporter│     │AVIExporter│  │MOVExporter│  │WebMExpor..│
└───────────┘     └───────────┘  └───────────┘  └───────────┘



┌─────────────────────────────────────────┐
│      FactoryMethodDemo                  │ ← Client
├─────────────────────────────────────────┤
│ + main(args: String[]): void            │
│ - demonstrateProblem(): void            │
│ - demonstrateSolution(): void           │
│ - demonstratePolymorphism(): void       │
│ - demonstrateExtensibility(): void      │
│ - demonstrateDeviceSpecific(): void     │
└─────────────────────────────────────────┘
                    │
                    │ uses
                    ↓
┌─────────────────────────────────────────┐
│         ExporterFactory                 │
│         <<abstract>>                    │
└─────────────────────────────────────────┘
```

### Key Relationships:

1. **Inheritance (△)**:
   - MP4Exporter, AVIExporter, MOVExporter, WebMExporter **implement** VideoExporter
   - MP4ExporterFactory, etc. **extend** ExporterFactory

2. **Creation (→)**:
   - MP4ExporterFactory **creates** MP4Exporter
   - AVIExporterFactory **creates** AVIExporter
   - MOVExporterFactory **creates** MOVExporter
   - WebMExporterFactory **creates** WebMExporter

3. **Dependency (---)**:
   - FactoryMethodDemo **uses** ExporterFactory
   - ExporterFactory **uses** VideoExporter (return type)

4. **Template Method**:
   - `exportVideo()` in ExporterFactory calls `createExporter()` (factory method)

### BlueJ Notation:

```
target1.type=InterfaceTarget         (VideoExporter)
target2.type=ClassTarget              (MP4Exporter)
target3.type=ClassTarget              (AVIExporter)
target4.type=ClassTarget              (MOVExporter)
target5.type=ClassTarget              (WebMExporter)
target6.type=AbstractTarget           (ExporterFactory)
target7.type=ClassTarget              (MP4ExporterFactory)
target8.type=ClassTarget              (AVIExporterFactory)
target9.type=ClassTarget              (MOVExporterFactory)
target10.type=ClassTarget             (WebMExporterFactory)
target11.type=ClassTarget             (FactoryMethodDemo)

dependency1.type=UsesDependency       (Demo → Factory)
dependency2.type=Inheritance          (MP4Exp → VideoExporter)
dependency3.type=Inheritance          (AVIExp → VideoExporter)
dependency4.type=Inheritance          (MOVExp → VideoExporter)
dependency5.type=Inheritance          (WebMExp → VideoExporter)
dependency6.type=Inheritance          (MP4Fac → ExporterFactory)
dependency7.type=Inheritance          (AVIFac → ExporterFactory)
dependency8.type=Inheritance          (MOVFac → ExporterFactory)
dependency9.type=Inheritance          (WebMFac → ExporterFactory)
dependency10.type=CreatesDependency   (MP4Fac → MP4Exp)
dependency11.type=CreatesDependency   (AVIFac → AVIExp)
dependency12.type=CreatesDependency   (MOVFac → MOVExp)
dependency13.type=CreatesDependency   (WebMFac → WebMExp)
```

---

## 8. Conclusion

### Pattern Summary:

The **Factory Method Pattern** successfully solves the video export system problem by:

1. **Eliminating switch statements**: Each format has its own class
2. **Enabling extensibility**: Add new formats without modifying existing code
3. **Enforcing Single Responsibility**: Each class handles one format
4. **Promoting loose coupling**: Client depends on interfaces
5. **Improving testability**: Test each format independently

### When to Use Factory Method:

✅ **Use Factory Method when:**
- You have a family of related products (export formats)
- You want to add new products without modifying existing code (Open/Closed)
- Product creation logic is complex or varies
- You want to enforce SOLID principles
- You need polymorphic object creation

❌ **Don't use Factory Method when:**
- You only have 2-3 simple products (overkill)
- Product creation is trivial (simple `new` is fine)
- You won't add new products (no extensibility needed)
- Performance is critical (factory adds minimal overhead, but still overhead)

### Real-World Applications:

1. **Video Processing**:
   - FFmpeg: Different encoders (H.264, VP9, HEVC)
   - HandBrake: Different presets (iPhone, Android, Web)

2. **Document Generation**:
   - Export to PDF, Word, Excel, HTML
   - Each format has its own exporter

3. **Database Drivers**:
   - MySQL, PostgreSQL, Oracle, SQLite
   - Each database has its own driver factory

4. **UI Frameworks**:
   - Android: Different View types (Button, TextView, ImageView)
   - Java Swing: Different component factories

5. **Logging**:
   - Log4j: Different appenders (File, Console, Database)
   - Each appender has its own factory

### Factory Method vs Other Patterns:

| Pattern | Purpose | When to Use |
|---------|---------|-------------|
| **Factory Method** | Create ONE product type with subclass variation | Multiple related products, extensibility needed |
| **Abstract Factory** | Create FAMILIES of related products | Need multiple products that work together |
| **Builder** | Construct complex objects step-by-step | Complex object with many parameters |
| **Prototype** | Clone existing objects | Object creation is expensive |
| **Singleton** | Ensure only one instance | Global state or resource |

### Context Linking Success:

**Video Platform Cluster** now has **6 patterns**:

1. **Adapter** - MediaPlayer compatibility (mp3, mp4, vlc)
2. **Observer** - YouTube notifications (subscriber alerts)
3. **Proxy** - StreamFlix lazy loading (performance)
4. **Flyweight** - UI icons sharing (9,000x memory savings)
5. **Builder** - Video upload config (12 parameters)
6. **Factory Method** - Video export formats (extensible) ✅

**Complete workflow:**
1. Upload video using **Builder** (12 parameters)
2. Store video using **Proxy** (lazy loading)
3. Play video using **Adapter** (format compatibility)
4. Show UI using **Flyweight** (memory optimization)
5. Notify users using **Observer** (subscriber notifications)
6. Export video using **Factory Method** (format selection) ✅

**Memorization benefit:**
- 6 patterns in ONE domain
- Easy to remember: "Video Platform = 6 patterns"
- Instant recall of all patterns!

### Key Takeaways:

1. **Open/Closed Principle**: Factory Method is the poster child for OCP
   - Open for extension (add new factories)
   - Closed for modification (no changes to existing code)

2. **Single Responsibility**: Each class has ONE job
   - MP4Exporter only handles MP4
   - AVIExporter only handles AVI
   - No "god class" doing everything

3. **Polymorphism is powerful**: Treat all exporters uniformly
   - Batch export to all formats
   - Device-specific selection
   - No if-else in client code

4. **Extensibility = Competitive advantage**: Add features faster than competitors
   - GIF export: 30 minutes (not 2 hours)
   - 4x faster feature development
   - Less risk of breaking existing code

5. **ROI is real**: 650% return over 5 years
   - Initial investment: 3 hours
   - Annual savings: 4.5 hours
   - Intangible benefits: fewer bugs, happier developers

### Final Thoughts:

The Factory Method pattern transforms a messy, unmaintainable switch statement into a clean, extensible, and testable architecture. By following the Open/Closed Principle and Single Responsibility Principle, we create code that is easy to understand, easy to test, and easy to extend.

**The video export system** is now:
- ✅ Extensible: Add formats in minutes
- ✅ Testable: Test each format independently
- ✅ Maintainable: Each format in its own class
- ✅ Type-safe: No string-based selection
- ✅ Polymorphic: Treat all exporters uniformly
- ✅ SOLID: Follows all 5 principles

This is the power of design patterns! 🚀

---

**End of FactoryMethod.md**
