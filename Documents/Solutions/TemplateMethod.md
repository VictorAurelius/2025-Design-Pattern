# Template Method Design Pattern - Video Processing Pipeline

## Pattern Description

The **Template Method Pattern** is a behavioral design pattern that defines the skeleton of an algorithm in a base class, but lets subclasses override specific steps of the algorithm without changing its structure. It's one of the most fundamental and widely-used patterns in object-oriented programming.

### Intent
- Define the skeleton of an algorithm in an operation, deferring some steps to subclasses
- Let subclasses redefine certain steps of an algorithm without changing the algorithm's structure
- Promote code reuse by extracting common behavior into a single location

### Key Components
1. **Abstract Class (Template)** - Defines the algorithm skeleton and common behavior
2. **Template Method** - Final method that defines the invariant algorithm structure
3. **Abstract Methods (Primitive Operations)** - Steps that subclasses MUST implement
4. **Hook Methods** - Optional steps with default implementation that subclasses CAN override

### Hollywood Principle
The Template Method pattern follows the **Hollywood Principle**: *"Don't call us, we'll call you"*

- The base class (Hollywood) controls the algorithm flow
- The base class calls subclass methods at appropriate times
- Subclasses don't control when their methods are called
- This is **Inversion of Control** - the framework controls the flow, not the client

### Method Types in Template Method Pattern

**1. Template Method (Final)**
```java
public final void processVideo(String videoId) {
    validate();     // Common
    preprocess();   // Common
    encode();       // Abstract - subclass implements
    optimize();     // Abstract - subclass implements
    save();         // Common
    notify();       // Common
}
```
- Marked `final` to prevent subclass override
- Defines the invariant algorithm structure
- Calls other methods in specific order

**2. Abstract Methods (Primitive Operations)**
```java
protected abstract void encode();
protected abstract void optimize();
```
- Subclasses MUST implement these
- Compiler enforces implementation
- Represents variable steps in algorithm

**3. Hook Methods (Optional Overrides)**
```java
protected boolean shouldAddWatermark() {
    return true;  // Default behavior
}

protected void addWatermark() {
    System.out.println("Adding watermark...");
}
```
- Provide default implementation
- Subclasses CAN override if needed
- Useful for optional algorithm steps

**4. Common Methods (Concrete Operations)**
```java
private void validate() {
    System.out.println("Validating video...");
}
```
- Implemented in base class
- Shared by all subclasses
- Usually private or protected
- Represents invariant steps

---

## Problem: Video Processing Code Duplication

### Context: StreamFlix Video Processing Workflow

**This is Pattern #10 in our StreamFlix cluster** (3rd Behavioral pattern)

**Pattern Linkage:**
- Links to **Builder (#5)**: Users build upload configurations
- Links to **Prototype (#8)**: Users clone templates for quick reuse
- Links to **Memento (#9)**: Users edit videos before uploading
- **NEW: Video Processing Pipeline (#10)**: After upload, videos must be processed

**The Complete Creator Workflow:**
```
1. Build upload config (Builder)
   ↓
2. Clone from template (Prototype)
   ↓
3. Edit video with undo/redo (Memento)
   ↓
4. Upload to StreamFlix
   ↓
5. VIDEO PROCESSING (Template Method) ← YOU ARE HERE
   ├─ Standard: 1080p, standard compression
   ├─ Premium: 4K, HDR, high quality
   └─ Live: Live encoding, chat overlay
   ↓
6. Notify subscribers (Observer)
   ↓
7. Users watch with themes (Abstract Factory)
```

### The Problem Without Template Method

StreamFlix processes three types of videos, each requiring slightly different processing:

**Type 1: Standard Videos (Regular uploads)**
- Resolution: 1080p
- Compression: Standard (CRF 23)
- Watermark: Standard bottom-right
- Target: Regular content creators

**Type 2: Premium Videos (High-quality uploads)**
- Resolution: 4K
- Compression: High-quality (CRF 18) with HDR
- Watermark: Subtle top-left
- Target: Professional creators

**Type 3: Live Stream Recordings**
- Resolution: 1080p with adaptive bitrate
- Special encoding for streaming
- Chat overlay embedded
- Target: Live streamers

**Current Implementation (Copy-Paste Nightmare):**

```java
public class StandardVideoProcessor {
    public void processVideo(String videoId) {
        // Step 1: Validate (DUPLICATED in all 3 classes)
        System.out.println("Validating format...");
        validateFormat();

        // Step 2: Preprocess (DUPLICATED in all 3 classes)
        System.out.println("Extracting metadata...");
        extractMetadata();
        System.out.println("Generating thumbnail...");
        generateThumbnail();

        // Step 3: Encode (DIFFERENT)
        System.out.println("Transcoding to 1080p...");
        transcodeTo1080p();

        // Step 4: Optimize (DIFFERENT)
        System.out.println("Standard compression...");
        applyStandardCompression();

        // Step 5: Watermark (DIFFERENT)
        System.out.println("Adding standard watermark...");
        addStandardWatermark();

        // Step 6: Save (DUPLICATED in all 3 classes)
        System.out.println("Uploading to CDN...");
        uploadToCDN();

        // Step 7: Notify (DUPLICATED in all 3 classes)
        System.out.println("Notifying user...");
        notifyUser();
    }
}

public class PremiumVideoProcessor {
    public void processVideo(String videoId) {
        // Steps 1, 2, 6, 7 are IDENTICAL to StandardVideoProcessor
        // Only steps 3, 4, 5 are different
        // BUT we have to duplicate ALL the code!
    }
}

public class LiveStreamProcessor {
    public void processVideo(String videoId) {
        // Steps 1, 2, 6, 7 are IDENTICAL to StandardVideoProcessor
        // Only steps 3, 4, 5 are different
        // BUT we have to duplicate ALL the code!
    }
}
```

### Quantified Problems

**1. Code Duplication**
- Total lines of code: 600 lines (3 classes × 200 lines each)
- Duplicated code: 400 lines (steps 1, 2, 6, 7 duplicated 3 times)
- Duplication percentage: 67%
- **Unique code:** Only 200 lines (steps 3, 4, 5 × 3 classes)

**2. Maintenance Nightmare**
- Bug in validation step → Must fix in 3 places
- Update CDN upload logic → Must modify 3 files
- Add new notification method → 3 files need changes
- **Risk:** Developer forgets to update one file → Production bug

**Example Bug Scenario:**
```
Bug found: Videos not properly tagged before CDN upload
Developer fixes: StandardVideoProcessor.java ✓
Developer fixes: PremiumVideoProcessor.java ✓
Developer forgets: LiveStreamProcessor.java ✗

Result: Live streams uploaded without proper tags
Impact: CDN serving wrong content, 500+ user complaints
Time to fix: 4 hours debugging + 2 hours emergency patch
```

**3. Inconsistent Pipelines**
- Developer adds new step to one processor
- Forgets to add it to others
- Pipelines diverge over time
- **Users experience:** Inconsistent video quality across types

**4. Cannot Enforce Workflow Order**
- No guarantee all processors follow same step order
- Developer might skip validation in one processor
- No compile-time checks for missing steps
- **Security risk:** Unvalidated videos reach CDN

**5. Time Waste Statistics**

**Scenario: GamingPro Team (10 developers)**
- Bug fixes requiring 3-file updates: 5 per month
- Time per bug fix: 30 minutes (10 min × 3 files)
- Monthly time waste: 5 bugs × 30 min = 2.5 hours
- **Annual time waste:** 2.5 hours × 12 = 30 hours

**Scenario: Adding New Video Type (Shorts)**
- Must copy-paste entire processor class
- Implementation time: 4 hours
- Testing: 2 hours
- Bug fixes from copy-paste errors: 3 hours
- **Total time:** 9 hours
- **With Template Method:** 1 hour (only implement variable steps)

### Why Not Just Use Helper Methods?

**Attempt 1: Shared Helper Class**
```java
public class VideoProcessorHelper {
    public static void validate() { }
    public static void preprocess() { }
    public static void save() { }
    public static void notify() { }
}

public class StandardVideoProcessor {
    public void processVideo(String videoId) {
        VideoProcessorHelper.validate();
        VideoProcessorHelper.preprocess();
        // ... custom encoding
        VideoProcessorHelper.save();
        VideoProcessorHelper.notify();
    }
}
```

**Problems:**
- No enforcement of workflow order (can skip steps)
- No guarantee all processors call same helper methods
- Cannot ensure template structure is followed
- Subclasses control flow instead of base class
- **Not an inversion of control**

**Attempt 2: Inheritance Without Template Method**
```java
public class BaseVideoProcessor {
    protected void validate() { }
    protected void preprocess() { }
    protected void save() { }
    protected void notify() { }
}

public class StandardVideoProcessor extends BaseVideoProcessor {
    public void processVideo(String videoId) {
        validate();      // Can skip this!
        preprocess();    // Can change order!
        encode();
        optimize();
        save();          // Can forget this!
        // Forgot notify()!
    }
}
```

**Problems:**
- Subclass controls workflow order (can be wrong)
- Can skip steps (no enforcement)
- Each subclass must remember to call all methods
- **Defeats the purpose of consistent pipeline**

---

## Requirements Analysis

### Functional Requirements

**FR1: Consistent Processing Pipeline**
- System shall define 7-step video processing pipeline
- All video types MUST follow exact same pipeline order
- Pipeline: validate → preprocess → encode → optimize → watermark → save → notify

**FR2: Common Steps Implementation**
- Base class shall implement steps 1, 2, 6, 7 (validate, preprocess, save, notify)
- Common steps shall be private/protected (not overridable)
- All subclasses automatically inherit common behavior

**FR3: Variable Steps (Abstract Methods)**
- Subclasses MUST implement encode() method
- Subclasses MUST implement optimize() method
- Compiler shall enforce implementation (abstract methods)

**FR4: Optional Steps (Hook Methods)**
- Base class shall provide shouldAddWatermark() hook
- Base class shall provide addWatermark() hook with default implementation
- Subclasses CAN override hooks to customize behavior

**FR5: Video Type Specific Processing**
- **StandardVideoProcessor:**
  - encode(): Transcode to 1080p H.264
  - optimize(): Standard compression (CRF 23)
  - addWatermark(): Bottom-right, semi-transparent

- **PremiumVideoProcessor:**
  - encode(): Transcode to 4K H.265 with HDR
  - optimize(): High-quality compression (CRF 18)
  - addWatermark(): Top-left, subtle branding

- **LiveStreamProcessor:**
  - encode(): Live encoding with chat overlay
  - optimize(): Adaptive bitrate (multiple quality levels)
  - addWatermark(): "LIVE" badge with timestamp

**FR6: Template Method Protection**
- processVideo() method shall be final (cannot be overridden)
- Ensures pipeline structure never changes
- Subclasses can only customize specific steps, not overall flow

**FR7: Metadata Extraction**
- Preprocess step shall extract: duration, resolution, framerate, codec
- Generate thumbnail at 10% position in video
- Store metadata for later use

**FR8: CDN Upload**
- Save step shall upload to CDN (CloudFront/Akamai)
- Generate HLS playlist for adaptive streaming
- Store CDN URL in database

### Non-Functional Requirements

**NFR1: Code Reusability**
- Common code shall be defined in ONE place (base class)
- Code duplication reduction: > 70%
- New video type requires < 50 lines of unique code

**NFR2: Maintainability**
- Bug fix in common step shall affect all processors (no manual sync)
- Time to fix pipeline bug: < 10 minutes (vs 30 minutes)
- Single source of truth for common behavior

**NFR3: Extensibility**
- Adding new video type shall take < 1 hour
- No modification to existing processors required
- Follows Open/Closed Principle (open for extension, closed for modification)

**NFR4: Type Safety**
- Abstract methods shall be enforced at compile-time
- Missing implementation shall cause compilation error
- No runtime errors due to missing steps

**NFR5: Performance**
- Template method call overhead: < 1ms
- No performance degradation vs hand-coded processors
- Pipeline execution time dominated by encoding/compression (minutes)

**NFR6: Readability**
- Pipeline workflow shall be visible in template method
- Each step shall have clear, descriptive name
- Code shall be self-documenting

---

## Effectiveness Analysis

### Time Savings Calculation

**Scenario: StreamFlix Development Team (10 developers)**

**Before Template Method:**
- Bug fix time: 3 files × 10 minutes = 30 minutes
- Bugs per month (inconsistency): 5 bugs
- Monthly time waste: 5 × 30 min = 2.5 hours
- Adding new video type: 9 hours
- **Annual time waste:** 2.5 hours × 12 = 30 hours

**After Template Method:**
- Bug fix time: 1 file × 5 minutes = 5 minutes
- Bugs per month: 1 (80% reduction due to consistency)
- Monthly time saved: 2.5 - 0.4 = 2.1 hours
- Adding new video type: 1 hour
- **Annual time saved:** 2.1 hours × 12 = 25.2 hours

### Code Metrics Comparison

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Total Lines of Code | 600 | 250 | 58% reduction |
| Duplicated Lines | 400 | 0 | 100% elimination |
| Files to Update (bug fix) | 3 | 1 | 67% reduction |
| Time to Add Video Type | 9 hrs | 1 hr | 89% faster |
| Inconsistency Bugs | 5/month | 1/month | 80% reduction |
| Code Coverage | 60% | 95% | 35% increase |

### ROI Analysis

**Development Cost:**
- Refactor to Template Method: 4 hours
- Testing all video types: 2 hours
- Documentation: 1 hour
- **Total cost:** 7 hours

**Annual Savings:**
- Bug fixes: 25.2 hours
- New video type (1 per year): 8 hours saved
- Code review time reduction: 5 hours
- **Total annual savings:** 38.2 hours

**ROI Calculation:**
```
ROI = [(38.2 - 7) / 7] × 100 = 446%
```

### 5-Year Projection

| Year | Video Types | Hours Saved | Cumulative Savings |
|------|-------------|-------------|--------------------|
| 1    | 3 → 4       | 38.2        | 38.2               |
| 2    | 4 → 5       | 46.0        | 84.2               |
| 3    | 5 → 7       | 61.6        | 145.8              |
| 4    | 7 → 9       | 77.2        | 223.0              |
| 5    | 9 → 12      | 100.4       | 323.4              |

**Total 5-year savings:** 323.4 hours
**5-year ROI:** [(323.4 - 7) / 7] × 100 = 4,520%

### Qualitative Benefits

**1. Consistency Guarantee**
- All video types follow EXACT same pipeline
- Impossible to skip steps or change order
- Template method is final (compile-time enforcement)

**2. Single Source of Truth**
- Common behavior defined once in base class
- Changes automatically propagate to all subclasses
- No manual synchronization needed

**3. Code Readability**
- Pipeline structure visible in template method
- Clear separation: what vs how
- Self-documenting code

**4. Easier Onboarding**
- New developers see algorithm structure immediately
- Only need to understand variable steps for new types
- Common steps already implemented

**5. Reduced Testing Burden**
- Common steps tested once in base class
- Only test variable steps in subclasses
- 95% code coverage (vs 60% before)

---

## Implementation

### Project Structure
```
16-Template-Method-DP/
├── VideoProcessor.java              (Abstract base class)
├── StandardVideoProcessor.java      (Concrete - 1080p)
├── PremiumVideoProcessor.java       (Concrete - 4K HDR)
├── LiveStreamProcessor.java         (Concrete - Live)
├── TemplateMethodDemo.java          (Client demo)
└── package.bluej                    (UML diagram)
```

### Class Diagram (UML)

```
┌──────────────────────────────────────────────────┐
│           VideoProcessor                         │ (Abstract)
├──────────────────────────────────────────────────┤
│ + processVideo(videoId): final void              │ ◄─ Template Method (final)
│ # validate(): void                               │ ◄─ Common (private)
│ # preprocess(): void                             │ ◄─ Common (private)
│ # encode(): abstract void                        │ ◄─ Must implement
│ # optimize(): abstract void                      │ ◄─ Must implement
│ # shouldAddWatermark(): boolean                  │ ◄─ Hook (can override)
│ # addWatermark(): void                           │ ◄─ Hook (can override)
│ # save(): void                                   │ ◄─ Common (private)
│ # notify(): void                                 │ ◄─ Common (private)
└──────────────────────────────────────────────────┘
                        △
                        │ extends
        ┌───────────────┼───────────────┐
        │               │               │
┌───────┴─────────┐ ┌──┴──────────┐ ┌──┴──────────────┐
│  Standard       │ │  Premium    │ │  LiveStream     │
│VideoProcessor   │ │VideoProcessor│ │  Processor      │
├─────────────────┤ ├─────────────┤ ├─────────────────┤
│ + encode()      │ │ + encode()  │ │ + encode()      │
│ + optimize()    │ │ + optimize()│ │ + optimize()    │
│ + addWatermark()│ │ + addWater..│ │ + addWatermark()│
└─────────────────┘ └─────────────┘ └─────────────────┘
```

### Sequence Diagram: Processing Standard Video

```
Client         VideoProcessor      StandardVideoProcessor
  |                  |                         |
  |─────────────────►|                         |
  | processVideo()   |                         |
  |                  |                         |
  |                  | validate()              |
  |                  |──────────┐              |
  |                  |          │              |
  |                  |◄─────────┘              |
  |                  |                         |
  |                  | preprocess()            |
  |                  |──────────┐              |
  |                  |          │              |
  |                  |◄─────────┘              |
  |                  |                         |
  |                  | encode()                |
  |                  |────────────────────────►|
  |                  |                         | transcodeTo1080p()
  |                  |◄────────────────────────|
  |                  |                         |
  |                  | optimize()              |
  |                  |────────────────────────►|
  |                  |                         | standardCompression()
  |                  |◄────────────────────────|
  |                  |                         |
  |                  | shouldAddWatermark()    |
  |                  |────────────────────────►|
  |                  |◄────────────────────────|
  |                  | return true             |
  |                  |                         |
  |                  | addWatermark()          |
  |                  |────────────────────────►|
  |                  |                         | addStandardWatermark()
  |                  |◄────────────────────────|
  |                  |                         |
  |                  | save()                  |
  |                  |──────────┐              |
  |                  |          │              |
  |                  |◄─────────┘              |
  |                  |                         |
  |                  | notify()                |
  |                  |──────────┐              |
  |                  |          │              |
  |                  |◄─────────┘              |
  |◄─────────────────|                         |
```

### Key Implementation Details

#### 1. VideoProcessor - Template Method (Final)

```java
public abstract class VideoProcessor {

    // TEMPLATE METHOD - Final, cannot be overridden
    public final void processVideo(String videoId) {
        System.out.println("\n[STEP 1: VALIDATE]");
        validate();

        System.out.println("\n[STEP 2: PREPROCESS]");
        preprocess();

        System.out.println("\n[STEP 3: ENCODE]");
        encode();  // Abstract - subclass implements

        System.out.println("\n[STEP 4: OPTIMIZE]");
        optimize();  // Abstract - subclass implements

        System.out.println("\n[STEP 5: WATERMARK]");
        if (shouldAddWatermark()) {  // Hook
            addWatermark();  // Hook
        }

        System.out.println("\n[STEP 6: SAVE]");
        save();

        System.out.println("\n[STEP 7: NOTIFY]");
        notify();
    }

    // Common methods - same for all video types
    private void validate() {
        System.out.println("✓ Validating video format and size...");
    }

    private void preprocess() {
        System.out.println("✓ Extracting metadata...");
        System.out.println("✓ Generating thumbnail...");
    }

    private void save() {
        System.out.println("✓ Uploading to CDN...");
    }

    private void notify() {
        System.out.println("✓ Notifying user...");
    }

    // Abstract methods - subclasses MUST implement
    protected abstract void encode();
    protected abstract void optimize();

    // Hook methods - subclasses CAN override
    protected boolean shouldAddWatermark() {
        return true;  // Default: add watermark
    }

    protected void addWatermark() {
        System.out.println("  → Adding watermark...");
    }
}
```

**Why final?**
- Prevents subclasses from changing algorithm structure
- Guarantees consistent pipeline for all video types
- Enforces template method pattern contract

#### 2. StandardVideoProcessor - Concrete Implementation

```java
public class StandardVideoProcessor extends VideoProcessor {

    @Override
    protected void encode() {
        System.out.println("  → Transcoding to 1080p H.264...");
        System.out.println("  → Progress: ████████████ 100%");
    }

    @Override
    protected void optimize() {
        System.out.println("  → Applying standard compression (CRF 23)...");
        System.out.println("  → Size reduced: 2.5GB → 850MB (66% savings)");
    }

    @Override
    protected void addWatermark() {
        System.out.println("  → Adding standard watermark (bottom-right)...");
    }
}
```

#### 3. Hook Method Usage

**Default Behavior (Base Class):**
```java
protected boolean shouldAddWatermark() {
    return true;  // Add watermark by default
}
```

**Override in Subclass (Premium - No Watermark):**
```java
public class PremiumVideoProcessor extends VideoProcessor {
    @Override
    protected boolean shouldAddWatermark() {
        return false;  // Premium users don't get watermark
    }

    // No need to override addWatermark() - it won't be called
}
```

**Override Both Hook Methods (Live):**
```java
public class LiveStreamProcessor extends VideoProcessor {
    @Override
    protected boolean shouldAddWatermark() {
        return true;  // Live streams get badge
    }

    @Override
    protected void addWatermark() {
        System.out.println("  → Adding 'LIVE' badge with timestamp...");
    }
}
```

---

## Expected Output

```
========================================
🎬 TEMPLATE METHOD PATTERN DEMO - Video Processing Pipeline
========================================

Processing 3 video types:
1. Standard Video (1080p)
2. Premium Video (4K HDR)
3. Live Stream Recording

Each follows the EXACT same 7-step pipeline:
  Step 1: Validate
  Step 2: Preprocess
  Step 3: Encode (variable)
  Step 4: Optimize (variable)
  Step 5: Watermark (hook)
  Step 6: Save
  Step 7: Notify

========================================
📹 PROCESSING: Standard Video (video001.mp4)
========================================

[STEP 1: VALIDATE]
✓ Validating video format and size...
✓ Format: MP4 (supported)
✓ Size: 2.5 GB (within limit)

[STEP 2: PREPROCESS]
✓ Extracting metadata...
  → Duration: 10:35
  → Resolution: 1920x1080
  → Framerate: 60fps
  → Codec: H.264
✓ Generating thumbnail...
  → Thumbnail saved: thumb_video001.jpg

[STEP 3: ENCODE]
  → Transcoding to 1080p H.264...
  → Progress: ████████████ 100%

[STEP 4: OPTIMIZE]
  → Applying standard compression (CRF 23)...
  → Size reduced: 2.5GB → 850MB (66% savings)

[STEP 5: WATERMARK]
  → Adding standard watermark (bottom-right)...

[STEP 6: SAVE]
✓ Uploading to CDN...
  → CDN URL: https://cdn.streamflix.com/video001.mp4
✓ Generating HLS playlist...

[STEP 7: NOTIFY]
✓ Notifying user...
  → Email sent to user@example.com
  → Push notification sent

✅ Standard video processed successfully!

========================================
📹 PROCESSING: Premium Video (premium001.mp4)
========================================

[STEP 1: VALIDATE]
✓ Validating video format and size...
✓ Format: MP4 (supported)
✓ Size: 15.3 GB (within premium limit)

[STEP 2: PREPROCESS]
✓ Extracting metadata...
  → Duration: 25:42
  → Resolution: 3840x2160
  → Framerate: 60fps
  → Codec: H.265
  → HDR: Yes (HDR10)
✓ Generating thumbnail...
  → High-res thumbnail saved: thumb_premium001.jpg

[STEP 3: ENCODE]
  → Transcoding to 4K H.265...
  → Processing HDR metadata (HDR10)...
  → Color space: BT.2020
  → Progress: ████████████ 100%

[STEP 4: OPTIMIZE]
  → Applying high-quality compression (CRF 18)...
  → Preserving HDR color space...
  → Size reduced: 15.3GB → 8.2GB (46% savings)

[STEP 5: WATERMARK]
  → Adding premium watermark (subtle, top-left)...

[STEP 6: SAVE]
✓ Uploading to CDN...
  → CDN URL: https://cdn.streamflix.com/premium001.mp4
✓ Generating HLS playlist (4K + HDR)...

[STEP 7: NOTIFY]
✓ Notifying user...
  → Email sent to premium@example.com
  → Push notification sent

✅ Premium video processed successfully!

========================================
📹 PROCESSING: Live Stream Recording (live001.mp4)
========================================

[STEP 1: VALIDATE]
✓ Validating video format and size...
✓ Format: MP4 (live recording)
✓ Size: 5.8 GB (within limit)

[STEP 2: PREPROCESS]
✓ Extracting metadata...
  → Duration: 2:15:33 (live stream)
  → Resolution: 1920x1080
  → Framerate: 60fps
  → Chat messages: 15,432
✓ Generating thumbnail...
  → Thumbnail with 'LIVE' badge saved

[STEP 3: ENCODE]
  → Encoding live stream recording...
  → Processing chat overlay...
  → Embedding 15,432 chat messages
  → Progress: ████████████ 100%

[STEP 4: OPTIMIZE]
  → Optimizing for streaming...
  → Generating adaptive bitrate versions:
    • 1080p60 (6000 kbps) - source
    • 720p60 (4500 kbps)
    • 480p30 (2500 kbps)
    • 360p30 (1000 kbps)

[STEP 5: WATERMARK]
  → Adding 'LIVE' badge with timestamp...
  → Badge position: top-right

[STEP 6: SAVE]
✓ Uploading to CDN...
  → CDN URL: https://cdn.streamflix.com/live001.mp4
✓ Generating HLS playlist (multi-bitrate)...

[STEP 7: NOTIFY]
✓ Notifying user...
  → Email sent to streamer@example.com
  → Push notification sent

✅ Live stream processed successfully!

========================================
📊 PIPELINE STATISTICS
========================================

Total videos processed: 3
Total processing time: 45 minutes
  Standard: 12 minutes
  Premium: 28 minutes (4K encoding)
  Live: 5 minutes (already encoded)

Pipeline consistency: 100%
  All videos followed exact same 7-step pipeline
  No steps skipped or reordered
  Template method enforced structure

========================================
✅ TEMPLATE METHOD BENEFITS DEMONSTRATED
========================================

1. ✓ Consistent Pipeline Enforced
   - All 3 video types follow EXACT same workflow
   - Impossible to skip steps or change order
   - Template method is final (compile-time guarantee)

2. ✓ Code Reusability Achieved
   - Common steps (validate, preprocess, save, notify) defined ONCE
   - Code duplication: 400 lines → 0 lines (100% elimination)
   - Single source of truth for common behavior

3. ✓ Easy Extension Demonstrated
   - Adding new video type requires < 50 lines
   - Only implement variable steps (encode, optimize)
   - Common steps inherited automatically

4. ✓ Maintainability Improved
   - Bug fix in common step affects all processors
   - Update in ONE place (base class)
   - No manual synchronization needed

5. ✓ Type Safety Enforced
   - Abstract methods enforced at compile-time
   - Missing implementation causes compilation error
   - No runtime surprises

6. ✓ Hollywood Principle Applied
   - Base class controls algorithm flow
   - Subclasses don't call base methods directly
   - "Don't call us, we'll call you"

========================================
🎓 KEY LEARNING POINTS
========================================

Template Method Pattern teaches:

1. **Inversion of Control**
   - Framework (base class) controls flow
   - Client (subclass) provides implementations
   - Opposite of typical control flow

2. **Method Types**
   - Template Method: Final, defines skeleton
   - Abstract Methods: Must implement
   - Hook Methods: Can override
   - Common Methods: Shared implementation

3. **Hollywood Principle**
   - "Don't call us, we'll call you"
   - Base class calls subclass methods
   - Not vice versa

4. **Open/Closed Principle**
   - Open for extension (new video types)
   - Closed for modification (base pipeline unchanged)

5. **Single Responsibility**
   - Base class: Algorithm structure
   - Subclasses: Variable implementations
   - Clear separation of concerns

========================================
📈 ROI SUMMARY
========================================

Before Template Method:
  - Code: 600 lines (400 duplicated)
  - Bug fix time: 30 minutes (3 files)
  - Add video type: 9 hours
  - Annual time waste: 30 hours

After Template Method:
  - Code: 250 lines (0 duplicated)
  - Bug fix time: 5 minutes (1 file)
  - Add video type: 1 hour
  - Annual time saved: 38.2 hours

ROI: 446% (Year 1), 4,520% (5 years)

Pattern #10 in StreamFlix cluster - Complete! ✓
```

---

## UML Class Diagram

```
                    ┌────────────────────────────────────────────────┐
                    │         <<abstract>>                           │
                    │         VideoProcessor                         │
                    ├────────────────────────────────────────────────┤
                    │ + processVideo(videoId): final void            │ ◄── Template Method (FINAL)
                    │                                                │
                    │ # validate(): void                             │ ◄── Common (implemented)
                    │ # preprocess(): void                           │ ◄── Common (implemented)
                    │ # save(): void                                 │ ◄── Common (implemented)
                    │ # notify(): void                               │ ◄── Common (implemented)
                    │                                                │
                    │ # encode(): abstract void                      │ ◄── Abstract (MUST implement)
                    │ # optimize(): abstract void                    │ ◄── Abstract (MUST implement)
                    │                                                │
                    │ # shouldAddWatermark(): boolean                │ ◄── Hook (CAN override)
                    │ # addWatermark(): void                         │ ◄── Hook (CAN override)
                    └────────────────────────────────────────────────┘
                                        △
                                        │
                                        │ extends
                    ┌───────────────────┼────────────────────┐
                    │                   │                    │
    ┌───────────────┴─────────┐  ┌─────┴────────────┐  ┌────┴──────────────────┐
    │  StandardVideoProcessor │  │ PremiumVideo     │  │  LiveStreamProcessor  │
    ├─────────────────────────┤  │ Processor        │  ├───────────────────────┤
    │                         │  ├──────────────────┤  │                       │
    │ + encode()              │  │                  │  │ + encode()            │
    │   → 1080p H.264         │  │ + encode()       │  │   → Live + chat       │
    │                         │  │   → 4K H.265 HDR │  │                       │
    │ + optimize()            │  │                  │  │ + optimize()          │
    │   → Standard CRF 23     │  │ + optimize()     │  │   → Adaptive bitrate  │
    │                         │  │   → HQ CRF 18    │  │                       │
    │ + addWatermark()        │  │                  │  │ + addWatermark()      │
    │   → Bottom-right        │  │ + addWatermark() │  │   → LIVE badge        │
    │                         │  │   → Top-left     │  │                       │
    └─────────────────────────┘  └──────────────────┘  └───────────────────────┘

RELATIONSHIP KEY:
═══════════════════════════════════════════════════════════════════════════════
△  Inheritance (extends)
   - Subclasses inherit common methods
   - Subclasses MUST implement abstract methods
   - Subclasses CAN override hook methods

processVideo() Flow:
═══════════════════════════════════════════════════════════════════════════════
1. validate()         [Base class - common]
2. preprocess()       [Base class - common]
3. encode()           [Subclass - variable implementation]
4. optimize()         [Subclass - variable implementation]
5. shouldAddWatermark() [Subclass - optional override]
6. addWatermark()     [Subclass - optional override]
7. save()             [Base class - common]
8. notify()           [Base class - common]
```

---

## Conclusion

### Pattern Summary

The **Template Method Pattern** is one of the most fundamental patterns in OOP, demonstrating the power of **inheritance** and **polymorphism** to eliminate code duplication while ensuring consistent behavior.

**Core Achievement: Inversion of Control**
- Base class defines algorithm skeleton (template method)
- Base class calls subclass methods at appropriate times
- Subclasses provide implementations but don't control flow
- **Hollywood Principle:** "Don't call us, we'll call you"

**Key Benefits:**
1. **Code Reuse:** Common behavior defined once (400 → 0 duplicated lines)
2. **Consistency:** All processors follow exact same pipeline (compile-time enforced)
3. **Maintainability:** Bug fix in one place affects all processors
4. **Extensibility:** New video type takes 1 hour (vs 9 hours)

**ROI: 446% Year 1, 4,520% over 5 years**

### When to Use Template Method Pattern

✅ **Use Template Method when:**
- Algorithm has invariant structure but variable steps
- Multiple classes share common behavior with minor variations
- You want to enforce a specific workflow order
- You want to prevent subclasses from changing algorithm structure
- Code duplication exists across similar classes

❌ **Avoid Template Method when:**
- Algorithm steps vary completely (no common structure) - use Strategy instead
- You need runtime algorithm switching - use Strategy instead
- Inheritance is not appropriate (composition preferred) - use Strategy instead
- Only one algorithm implementation exists - no need for pattern

### Template Method vs Strategy Pattern

Both patterns define algorithms, but with different approaches:

**Template Method (Inheritance-based):**
```java
public abstract class VideoProcessor {
    public final void process() {
        validate();
        encode();  // Subclass implements
        save();
    }
    protected abstract void encode();
}

public class StandardProcessor extends VideoProcessor {
    protected void encode() { /* 1080p */ }
}
```

**Strategy (Composition-based):**
```java
public class VideoProcessor {
    private EncodingStrategy strategy;

    public void process() {
        validate();
        strategy.encode();  // Delegate to strategy
        save();
    }
}

public class Standard1080pStrategy implements EncodingStrategy {
    public void encode() { /* 1080p */ }
}
```

**When to choose:**
- **Template Method:** When algorithm structure is fixed, variations are minor
- **Strategy:** When entire algorithm varies, need runtime switching

### Pattern Linkage in StreamFlix Cluster

This is **Pattern #10** in our cluster (3rd Behavioral pattern):

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

**Current Pattern:**
10. **Template Method (#10)** - Video processing pipeline

**Context Links:**
- Users build configs (Builder #5) → Clone templates (Prototype #8)
- Users edit videos (Memento #9) → Upload to StreamFlix
- **Videos processed through pipeline (Template Method #10)**
- Subscribers notified (Observer #2) → Users watch (Abstract Factory #7)

**Complete workflow:** Build → Clone → Edit → Upload → **Process** → Notify → Watch

### Real-World Applications Beyond StreamFlix

**Frameworks (Most Common Use Case):**
- **Java Collections:** AbstractList, AbstractSet, AbstractMap
  ```java
  public abstract class AbstractList<E> {
      public boolean add(E e) {  // Template method
          add(size(), e);  // Calls abstract method
      }
      public abstract void add(int index, E element);
  }
  ```

- **Spring Framework:** JdbcTemplate, RestTemplate, HibernateTemplate
  ```java
  public class JdbcTemplate {
      public <T> T query(String sql, ResultSetExtractor<T> rse) {
          Connection con = getConnection();  // Common
          PreparedStatement ps = createStatement(con, sql);  // Common
          ResultSet rs = ps.executeQuery();  // Common
          T result = rse.extractData(rs);  // Variable - client implements
          closeConnection(con);  // Common
          return result;
      }
  }
  ```

**UI Frameworks:**
- **Android Activity Lifecycle:**
  ```java
  public abstract class Activity {
      public final void performCreate() {  // Template method
          onCreate();  // Hook - subclass overrides
          onStart();   // Hook
          onResume();  // Hook
      }
      protected void onCreate() { }  // Default: do nothing
      protected void onStart() { }
      protected void onResume() { }
  }
  ```

- **React Component Lifecycle:**
  ```javascript
  class Component {
      // Template method (internal)
      _performMount() {
          this.componentWillMount();  // Hook
          this.render();              // Abstract
          this.componentDidMount();   // Hook
      }
  }
  ```

**Build Tools:**
- **Maven Build Lifecycle:** validate → compile → test → package → install
- **Gradle Task Execution:** Each phase is a template method

**Game Development:**
- **Game Loop:** init() → update() → render() → destroy()
- **Entity Behavior:** spawn() → update() → die()

### Implementation Checklist

When implementing Template Method pattern, ensure:

- [x] Template method is final (cannot be overridden)
- [x] Template method defines clear algorithm structure
- [x] Common steps implemented in base class (private/protected)
- [x] Variable steps declared as abstract (compiler enforced)
- [x] Hook methods have default implementation (optional override)
- [x] Method access modifiers correct (template: public, others: protected)
- [x] Subclasses cannot change algorithm structure
- [x] Documentation explains which methods to override

### Final Thoughts

The Template Method pattern is **essential for framework design** and **code reuse**. It's the pattern behind most modern frameworks (Spring, Android, React) because it provides the perfect balance between:
- **Control:** Framework controls workflow
- **Flexibility:** Clients customize specific steps
- **Consistency:** Same workflow for all clients

For StreamFlix, Template Method ensures all videos—regardless of type—go through the same rigorous processing pipeline. This consistency is critical for quality assurance and user experience.

The pattern saves 38.2 hours annually by eliminating code duplication and reducing maintenance overhead. More importantly, it makes the codebase more **understandable**, **testable**, and **extensible**.

**Pattern #10 completed. The video processing pipeline is now consistent, maintainable, and extensible!**

---

**Pattern Cluster Progress: 10/24 patterns implemented (41.7%)**

**Next patterns to implement:**
- Strategy (Behavioral) - Video compression algorithms
- Decorator (Structural) - Video enhancement filters
- State (Behavioral) - Video player states
- ... 14 more patterns
