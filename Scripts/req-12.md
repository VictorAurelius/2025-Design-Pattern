# REQ-12: Factory Method Pattern Implementation

## Pattern Information
- **Pattern Name**: Factory Method Pattern
- **Category**: Creational Pattern
- **Difficulty**: ⭐⭐⭐ (Medium)
- **Folder**: `12-Factory-Method-DP/`

---

## Context Linking Strategy (Memory Optimization)

### Current Video Platform Cluster (5 patterns):
1. ✅ **Adapter** - MediaPlayer (mp3, mp4, vlc compatibility)
2. ✅ **Observer** - YouTube Channel (notification system)
3. ✅ **Proxy** - StreamFlix (lazy video loading)
4. ✅ **Flyweight** - Video Player UI Icons (9,000x memory savings)
5. ✅ **Builder** - Video Upload Configuration (12 params without telescoping hell)

### Adding Pattern #6:
6. **Factory Method** → **Video Export System** 🎬

**Why this context?**
- Natural extension: After uploading (Builder), users need to export videos
- Links to: Video Upload (Builder), Video Platform (Proxy, Flyweight)
- Real-world scenario: Export videos in different formats (MP4, AVI, MOV, WebM)
- Easy to remember: "StreamFlix platform needs video export feature"

---

## Recommended Context: Video Export System

### Scenario:
StreamFlix (video streaming platform) needs to export videos in different formats for various devices and purposes:
- **MP4**: Most compatible, web streaming, mobile devices
- **AVI**: High quality, video editing
- **MOV**: Apple devices, professional editing
- **WebM**: Web optimization, smaller file size

### Current Problem (WITHOUT Factory Method):

```java
// ❌ BAD: Client code has switch/if-else everywhere
public class VideoProcessor {
    public void exportVideo(String format, String videoPath) {
        if (format.equals("MP4")) {
            // MP4 export logic
            System.out.println("Exporting to MP4...");
            System.out.println("Using H.264 codec");
            System.out.println("Setting bitrate: 5000kbps");
            // ... 50 lines of MP4-specific code
        } else if (format.equals("AVI")) {
            // AVI export logic
            System.out.println("Exporting to AVI...");
            System.out.println("Using XVID codec");
            System.out.println("Setting bitrate: 8000kbps");
            // ... 50 lines of AVI-specific code
        } else if (format.equals("MOV")) {
            // MOV export logic
            System.out.println("Exporting to MOV...");
            System.out.println("Using Apple ProRes codec");
            System.out.println("Setting bitrate: 12000kbps");
            // ... 50 lines of MOV-specific code
        } else if (format.equals("WEBM")) {
            // WebM export logic
            System.out.println("Exporting to WebM...");
            System.out.println("Using VP9 codec");
            System.out.println("Setting bitrate: 3000kbps");
            // ... 50 lines of WebM-specific code
        }
        // ... more formats
    }
}
```

### Problems with this approach:

1. **Violation of Open/Closed Principle**
   - Adding new format = Modifying existing code
   - Need to change VideoProcessor class every time

2. **Code Duplication**
   - Similar export logic scattered across if-else blocks
   - Hard to maintain consistency

3. **Tight Coupling**
   - Client code knows too much about export details
   - Cannot reuse export logic

4. **Not Extensible**
   - Cannot add new formats without modifying existing code
   - Cannot create format-specific variations easily

5. **Testing Nightmare**
   - Must test entire switch statement for each format
   - Cannot test formats in isolation

**Example of the mess:**
```java
// Client code is ugly and inflexible
if (userDevice.equals("iPhone")) {
    processor.exportVideo("MOV", video);
} else if (userDevice.equals("Android")) {
    processor.exportVideo("MP4", video);
} else if (userDevice.equals("Web")) {
    processor.exportVideo("WEBM", video);
}
// What if we need GIF export? Must modify VideoProcessor!
// What if we need format-specific settings? Must add more parameters!
```

---

## Solution: Factory Method Pattern

### Key Idea:
- **Define an interface** for creating objects (VideoExporter)
- **Let subclasses decide** which class to instantiate
- **Defer instantiation** to creator subclasses (ExporterFactory)

### Structure:

```
Product (Interface)
    ├─ VideoExporter
    │
Concrete Products (4 classes)
    ├─ MP4Exporter
    ├─ AVIExporter
    ├─ MOVExporter
    └─ WebMExporter

Creator (Abstract/Interface)
    ├─ ExporterFactory (abstract class)
    │
Concrete Creators (4 classes)
    ├─ MP4ExporterFactory
    ├─ AVIExporterFactory
    ├─ MOVExporterFactory
    └─ WebMExporterFactory

Client
    └─ FactoryMethodDemo
```

### Benefits:

1. **Open/Closed Principle** ✅
   - Add new format = Create new factory (no modification!)
   - Closed for modification, open for extension

2. **Single Responsibility** ✅
   - Each exporter handles ONE format
   - Each factory creates ONE type of exporter

3. **Loose Coupling** ✅
   - Client depends on interface, not concrete classes
   - Easy to swap implementations

4. **Easy Testing** ✅
   - Test each exporter independently
   - Mock factories easily

5. **Extensibility** ✅
   - Add GIF exporter? Just create GIFExporter + GIFExporterFactory
   - No changes to existing code!

### Code with Factory Method:

```java
// ✅ GOOD: Client code is clean and extensible
ExporterFactory factory;

if (format.equals("MP4")) {
    factory = new MP4ExporterFactory();
} else if (format.equals("AVI")) {
    factory = new AVIExporterFactory();
}

VideoExporter exporter = factory.createExporter();
exporter.export(videoPath);
```

**Even better with enum:**
```java
// ✅ BEST: Type-safe factory selection
ExporterFactory factory = ExporterFactory.getFactory(ExportFormat.MP4);
VideoExporter exporter = factory.createExporter();
exporter.export(videoPath);
```

---

## Implementation Requirements

### Files to Create (9 files):

1. **VideoExporter.java** (Product Interface)
   - `void export(String videoPath)`
   - `String getFormat()`
   - `int getBitrate()`
   - `String getCodec()`

2. **MP4Exporter.java** (Concrete Product)
   - Implements VideoExporter
   - MP4-specific export logic (H.264 codec, 5000kbps)
   - `.mp4` file extension

3. **AVIExporter.java** (Concrete Product)
   - Implements VideoExporter
   - AVI-specific export logic (XVID codec, 8000kbps)
   - `.avi` file extension

4. **MOVExporter.java** (Concrete Product)
   - Implements VideoExporter
   - MOV-specific export logic (Apple ProRes, 12000kbps)
   - `.mov` file extension

5. **WebMExporter.java** (Concrete Product)
   - Implements VideoExporter
   - WebM-specific export logic (VP9 codec, 3000kbps)
   - `.webm` file extension

6. **ExporterFactory.java** (Creator Abstract Class)
   - `abstract VideoExporter createExporter()` (Factory Method)
   - `void exportVideo(String videoPath)` (Template Method using factory method)
   - Static method: `getFactory(String format)` for convenience

7. **MP4ExporterFactory.java** (Concrete Creator)
   - Extends ExporterFactory
   - Returns `new MP4Exporter()`

8. **AVIExporterFactory.java** (Concrete Creator)
   - Extends ExporterFactory
   - Returns `new AVIExporter()`

9. **MOVExporterFactory.java** (Concrete Creator)
   - Extends ExporterFactory
   - Returns `new MOVExporter()`

10. **WebMExporterFactory.java** (Concrete Creator)
    - Extends ExporterFactory
    - Returns `new WebMExporter()`

11. **FactoryMethodDemo.java** (Client)
    - Demonstrates Factory Method pattern
    - Shows extensibility (adding new format)
    - Shows polymorphism (treating all exporters uniformly)
    - Compares with/without Factory Method

12. **package.bluej** (UML Diagram)
    - Shows Product hierarchy
    - Shows Creator hierarchy
    - Shows dependencies

---

## Expected Output

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
✓ MP4Exporter instance created

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
✓ AVIExporter instance created

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
✓ MOVExporter instance created

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
✓ WebMExporter instance created

[WebMExporter] Starting export...
  📁 Input: /videos/tutorial.mp4
  📦 Output: /videos/tutorial_exported.webm
  🎞️  Codec: VP9 (Web Optimized)
  📊 Bitrate: 3000 kbps
  🎯 Use case: Web optimization, smaller file size
  ⏱️  Processing... [████████████████████] 100%
  ✓ WebM export completed!


═══════════════════════════════════════════════════════════
TEST 2: Polymorphism (Treating All Exporters Uniformly)
═══════════════════════════════════════════════════════════

🎯 Processing batch export for: /videos/demo.mp4

Exporter 1: MP4 (H.264) - 5000 kbps
Exporter 2: AVI (XVID) - 8000 kbps
Exporter 3: MOV (Apple ProRes) - 12000 kbps
Exporter 4: WebM (VP9) - 3000 kbps

✓ All 4 formats exported successfully!


═══════════════════════════════════════════════════════════
TEST 3: Device-Specific Export (Real-World Use Case)
═══════════════════════════════════════════════════════════

📱 User Device: iPhone
   → Recommended format: MOV
   → Using MOVExporterFactory
   → Export completed!

📱 User Device: Android Phone
   → Recommended format: MP4
   → Using MP4ExporterFactory
   → Export completed!

💻 User Device: Web Browser
   → Recommended format: WebM
   → Using WebMExporterFactory
   → Export completed!

🎬 User Device: Video Editor
   → Recommended format: AVI
   → Using AVIExporterFactory
   → Export completed!


═══════════════════════════════════════════════════════════
TEST 4: Extensibility (Adding New Format)
═══════════════════════════════════════════════════════════

💡 Scenario: We need to add GIF export for social media

❌ WITHOUT Factory Method:
   1. Modify VideoProcessor class (violates OCP)
   2. Add another if-else branch
   3. Risk breaking existing code
   4. Must retest all formats

✅ WITH Factory Method:
   1. Create GIFExporter class (implements VideoExporter)
   2. Create GIFExporterFactory class (extends ExporterFactory)
   3. Done! No changes to existing code!
   4. Only test new GIF functionality

🎉 Result: Adding GIF took 10 minutes (not 2 hours!)


═══════════════════════════════════════════════════════════
BENEFITS SUMMARY
═══════════════════════════════════════════════════════════

✅ Advantages:
   1. OPEN/CLOSED: Add format without modifying existing code
   2. SINGLE RESPONSIBILITY: Each exporter handles one format
   3. LOOSE COUPLING: Client depends on interface, not concrete classes
   4. TESTABILITY: Test each format independently
   5. EXTENSIBILITY: New formats in 10 minutes
   6. POLYMORPHISM: Treat all exporters uniformly
   7. TYPE SAFETY: No string comparisons, use enum

⚠️  Trade-offs:
   1. More classes (but organized and maintainable)
   2. Initial setup time (saves 100x time later)
   3. May be overkill for 2-3 simple formats

📊 ROI Calculation:
   - Initial effort: 2 hours to set up Factory Method
   - Time saved per new format: 1.5 hours (no modifications needed)
   - Formats added per year: 4 formats
   - Total saved: 6 hours/year per developer
   - ROI: 300% in first year, 10,000% over 5 years!


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

💡 Complete Video Platform Features:
   📥 Upload videos (Builder)
   ▶️  Play videos (Adapter for compatibility)
   📺 Stream videos (Proxy for lazy loading)
   🎨 UI icons (Flyweight for memory savings)
   🔔 Notifications (Observer for subscribers)
   📤 Export videos (Factory Method for formats)

🧠 Memorization Strategy:
   All 6 patterns in ONE domain = Super easy to remember!
   Think: "Video Platform = 6 patterns" → Instant recall! ⚡

════════════════════════════════════════════════════════════
```

---

## UML Class Diagram (package.bluej)

```
┌─────────────────────┐
│   VideoExporter     │ (Interface)
│   <<interface>>     │
├─────────────────────┤
│ + export(path)      │
│ + getFormat()       │
│ + getBitrate()      │
│ + getCodec()        │
└─────────────────────┘
          △
          │ implements
          │
    ┌─────┴─────┬─────────┬─────────┐
    │           │         │         │
┌───┴────┐  ┌───┴────┐  ┌┴────┐  ┌─┴────┐
│MP4Exp  │  │AVIExp  │  │MOVExp│  │WebMExp│
└────────┘  └────────┘  └──────┘  └──────┘


┌─────────────────────┐
│  ExporterFactory    │ (Abstract)
├─────────────────────┤
│ + createExporter()  │ ← Factory Method
│ + exportVideo(path) │
│ + getFactory(fmt)   │ (static)
└─────────────────────┘
          △
          │ extends
          │
    ┌─────┴─────┬──────────┬──────────┐
    │           │          │          │
┌───┴───┐  ┌───┴───┐  ┌───┴──┐  ┌───┴───┐
│MP4Fac │  │AVIFac │  │MOVFac│  │WebMFac│
└───────┘  └───────┘  └──────┘  └───────┘
    │           │          │          │
    │ creates   │ creates  │ creates  │ creates
    ↓           ↓          ↓          ↓
┌───────┐  ┌───────┐  ┌──────┐  ┌───────┐
│MP4Exp │  │AVIExp │  │MOVExp│  │WebMExp│
└───────┘  └───────┘  └──────┘  └───────┘


┌─────────────────────┐
│  FactoryMethodDemo  │ (Client)
├─────────────────────┤
│ + main(args)        │
└─────────────────────┘
          │ uses
          ↓
┌─────────────────────┐
│  ExporterFactory    │
└─────────────────────┘
```

---

## Key Learning Points

### 1. Factory Method vs Simple Factory
- **Simple Factory**: One factory class with switch/if-else
- **Factory Method**: Each product type has its own factory (better!)

### 2. When to Use Factory Method
✅ Use when:
- You have a family of related products
- You want to add new products without modifying existing code
- Product creation logic is complex
- You want to enforce Open/Closed Principle

❌ Don't use when:
- You only have 2-3 simple products
- Product creation is trivial
- You won't add new products

### 3. Factory Method vs Abstract Factory
- **Factory Method**: Creates ONE product (VideoExporter)
- **Abstract Factory**: Creates FAMILIES of products (will learn later!)

### 4. Real-World Examples
- Java: `Calendar.getInstance()`, `NumberFormat.getInstance()`
- Android: `View.inflate()`, `Fragment.instantiate()`
- Spring: `BeanFactory`, `ApplicationContext`
- Video Export: FFmpeg, HandBrake (different format exporters)

---

## Testing Checklist

- [ ] All 4 exporters implement VideoExporter interface
- [ ] Each exporter has correct codec, bitrate, file extension
- [ ] All 4 factories extend ExporterFactory abstract class
- [ ] Factory method returns correct exporter type
- [ ] Static getFactory() method works correctly
- [ ] Demo shows before/after comparison
- [ ] Demo shows polymorphism (batch export)
- [ ] Demo shows extensibility (adding new format)
- [ ] Demo shows device-specific export use case
- [ ] UML diagram shows inheritance and creation relationships
- [ ] Output is formatted with emojis and clear sections

---

## Success Criteria

✅ Implementation is complete when:
1. All 11 Java files created (4 products + 4 creators + 1 abstract + 1 client + 1 bluej)
2. Client can create exporters without knowing concrete classes
3. Adding new format requires NO changes to existing code
4. Demo output is comprehensive and educational
5. UML diagram clearly shows Factory Method pattern structure
6. Pattern effectively links to Video Platform cluster (6 patterns total)

---

## Context Link to Existing Patterns

```
StreamFlix Video Platform
│
├─ 1. ADAPTER: MediaPlayer
│   └─ Problem: Incompatible media formats
│   └─ Solution: Adapter wraps media players
│
├─ 2. OBSERVER: YouTube Channel
│   └─ Problem: Notify subscribers of new videos
│   └─ Solution: Observer notifies all subscribers
│
├─ 3. PROXY: StreamFlix
│   └─ Problem: Loading all videos at once = slow
│   └─ Solution: Proxy loads videos lazily
│
├─ 4. FLYWEIGHT: Video Player UI Icons
│   └─ Problem: 10,000 videos × 4 icons = 20GB
│   └─ Solution: Share 4 icon objects = 2MB
│
├─ 5. BUILDER: Video Upload Config
│   └─ Problem: 12 parameters = 1,024 constructors
│   └─ Solution: Builder with fluent interface
│
└─ 6. FACTORY METHOD: Video Export System ⭐ NEW!
    └─ Problem: Switch/if-else for every format
    └─ Solution: Each format has its own factory
```

**Complete workflow:**
1. User uploads video (BUILDER)
2. Video is stored lazily (PROXY)
3. User watches video (ADAPTER for compatibility)
4. UI shows play/pause icons (FLYWEIGHT for memory)
5. Subscribers get notified (OBSERVER)
6. User exports video (FACTORY METHOD) ⭐

---

**End of req-12.md**
