# REQ-16: Template Method Pattern - Video Processing Pipeline

## 📋 Pattern Information

**Pattern Name:** Template Method Pattern
**Pattern Type:** Behavioral Design Pattern
**Pattern Number:** #10 in StreamFlix cluster (3rd Behavioral)
**Difficulty Level:** ⭐⭐⭐ Medium
**Prerequisites:** Inheritance, Abstract classes, Method overriding

## 🎯 Context: Video Processing Pipeline

### StreamFlix Pattern Cluster Progress

**Completed Patterns (9/24):**
1. ✅ Adapter (#1) - MediaPlayer compatibility
2. ✅ Observer (#2) - YouTube channel subscriptions
3. ✅ Proxy (#3) - StreamFlix access control
4. ✅ Flyweight (#4) - Video player UI icons
5. ✅ Builder (#5) - Video upload configuration
6. ✅ Factory Method (#6) - Video export formats
7. ✅ Abstract Factory (#7) - Video player themes
8. ✅ Prototype (#8) - Clone upload templates
9. ✅ Memento (#9) - Video editor undo/redo

**Current Pattern:**
10. 🔄 **Template Method (#10)** - Video processing pipeline

**Upcoming Patterns:**
11. Strategy - Video compression algorithms
12. Decorator - Video enhancement filters
13. ... (14 more patterns)

### Pattern Linkage in StreamFlix Workflow

**Complete Content Creator Journey:**
```
1. Build upload config (Builder #5)
   ↓
2. Clone from template (Prototype #8)
   ↓
3. Edit video with undo/redo (Memento #9)
   ↓
4. Upload video to StreamFlix
   ↓
5. 🆕 VIDEO PROCESSING PIPELINE (Template Method #10) ← YOU ARE HERE
   ├─ Standard Video: 1080p, standard compression
   ├─ Premium Video: 4K, HDR, high quality
   └─ Live Stream Recording: special encoding, chat overlay
   ↓
6. Notify subscribers (Observer #2)
   ↓
7. Users watch with themes (Abstract Factory #7)
```

**Key Insight:** Every uploaded video must go through processing before it can be watched. The Template Method pattern defines this consistent pipeline while allowing different processing strategies for different video types.

---

## 🔴 The Problem: Code Duplication in Video Processing

### Current Situation: Copy-Paste Processors

StreamFlix processes three types of videos:

**1. Standard Video Processor**
```java
public class StandardVideoProcessor {
    public void processVideo(String videoId) {
        // Step 1: Validate
        System.out.println("Validating video format and size...");
        if (!isValidFormat()) return;

        // Step 2: Preprocess
        System.out.println("Extracting metadata...");
        extractMetadata();
        System.out.println("Generating thumbnail...");
        generateThumbnail();

        // Step 3: Process (STANDARD SPECIFIC)
        System.out.println("Transcoding to 1080p...");
        transcodeTo1080p();
        System.out.println("Standard compression...");
        applyStandardCompression();

        // Step 4: Postprocess (STANDARD SPECIFIC)
        System.out.println("Adding standard watermark...");
        addStandardWatermark();

        // Step 5: Save
        System.out.println("Uploading to CDN...");
        uploadToCDN();

        // Step 6: Notify
        System.out.println("Notifying user...");
        notifyUser();
    }
}
```

**2. Premium Video Processor**
```java
public class PremiumVideoProcessor {
    public void processVideo(String videoId) {
        // Step 1: Validate (DUPLICATED!)
        System.out.println("Validating video format and size...");
        if (!isValidFormat()) return;

        // Step 2: Preprocess (DUPLICATED!)
        System.out.println("Extracting metadata...");
        extractMetadata();
        System.out.println("Generating thumbnail...");
        generateThumbnail();

        // Step 3: Process (PREMIUM SPECIFIC)
        System.out.println("Transcoding to 4K...");
        transcodeTo4K();
        System.out.println("High-quality compression with HDR...");
        applyHDRProcessing();

        // Step 4: Postprocess (PREMIUM SPECIFIC)
        System.out.println("Adding premium watermark...");
        addPremiumWatermark();
        System.out.println("Generating high-res preview...");
        generateHighResPreview();

        // Step 5: Save (DUPLICATED!)
        System.out.println("Uploading to CDN...");
        uploadToCDN();

        // Step 6: Notify (DUPLICATED!)
        System.out.println("Notifying user...");
        notifyUser();
    }
}
```

**3. Live Stream Recording Processor**
```java
public class LiveStreamProcessor {
    public void processVideo(String videoId) {
        // Step 1: Validate (DUPLICATED!)
        System.out.println("Validating video format and size...");
        if (!isValidFormat()) return;

        // Step 2: Preprocess (DUPLICATED!)
        System.out.println("Extracting metadata...");
        extractMetadata();
        System.out.println("Generating thumbnail...");
        generateThumbnail();

        // Step 3: Process (LIVE SPECIFIC)
        System.out.println("Encoding live stream...");
        encodeLiveStream();
        System.out.println("Processing chat overlay...");
        processChatOverlay();

        // Step 4: Postprocess (LIVE SPECIFIC)
        System.out.println("Adding live badge...");
        addLiveBadge();
        System.out.println("Generating chapters...");
        generateChapters();

        // Step 5: Save (DUPLICATED!)
        System.out.println("Uploading to CDN...");
        uploadToCDN();

        // Step 6: Notify (DUPLICATED!)
        System.out.println("Notifying user...");
        notifyUser();
    }
}
```

### Problems with This Approach

**1. Massive Code Duplication (DRY Violation)**
- Steps 1, 2, 5, 6 are IDENTICAL across all three processors
- 67% of code duplicated (4 out of 6 steps)
- Total duplicated code: ~200 lines across 3 classes

**2. Maintenance Nightmare**
- Bug in validation step → Must fix in 3 places
- Change CDN upload logic → Must update 3 files
- Add new notification method → 3 files to modify
- **Forgot to update 1 file? Production bug!**

**3. Inconsistent Pipelines**
- Developer adds new step to StandardVideoProcessor
- Forgets to add it to PremiumVideoProcessor
- Premium videos missing critical processing step
- **Inconsistency causes user-facing bugs**

**4. Cannot Enforce Workflow**
- No guarantee all processors follow same order
- Developer might skip validation in one processor
- No compile-time checks for missing steps
- **Pipeline violations go undetected**

**5. Difficult to Extend**
- Want to add new video type (e.g., Shorts)?
- Must copy-paste entire pipeline again
- Manually ensure all steps included
- Risk of missing steps or wrong order

### Real-World Impact

**Scenario 1: CDN Upload Bug**
- Bug found: Videos not properly tagged before CDN upload
- Must fix in 3 separate files
- Developer fixes StandardVideoProcessor and PremiumVideoProcessor
- **FORGETS LiveStreamProcessor** ❌
- Live streams uploaded without proper tags
- CDN serving wrong content to users
- **Time wasted:** 4 hours debugging + 2 hours emergency fix

**Scenario 2: New Notification Requirement**
- Product requirement: Send email + push notification (not just email)
- Must update notifyUser() in 3 files
- Developer updates 2 files correctly
- **Misses 1 file** ❌
- Some users don't get notifications
- **Customer support tickets:** 50+ complaints

**Scenario 3: Adding New Video Type (Shorts)**
- Copy-paste from StandardVideoProcessor
- Forgets to update compression settings
- Shorts processed with wrong compression
- **Quality issues:** Videos look pixelated
- **Time wasted:** 6 hours reprocessing 10,000+ videos

### Quantified Pain Points

**For 3 Video Types:**
- Code duplication: 200 lines × 3 files = 600 lines (400 duplicated)
- Bug fix time: 3 files × 10 minutes = 30 minutes per bug
- Bugs per month due to inconsistency: 5 bugs
- **Monthly time waste:** 5 bugs × 30 min = 2.5 hours
- **Annual time waste:** 2.5 hours × 12 months = 30 hours

**Adding 4th Video Type (Shorts):**
- Implementation time: 4 hours (copy-paste + modifications)
- Testing all 4 types: 2 hours
- Bug fixes due to copy-paste errors: 3 bugs × 1 hour = 3 hours
- **Total time:** 9 hours (vs 1 hour with Template Method)

**5-Year Projection:**
- Number of video types grows: 3 → 5 → 8 → 12
- Code duplication grows exponentially
- Bug rate increases linearly with each new type
- **Estimated time waste:** 30 + 50 + 80 + 120 + 180 = 460 hours

---

## ✅ The Solution: Template Method Pattern

### Core Concept

**Define the skeleton of an algorithm in a base class, but let subclasses override specific steps without changing the algorithm's structure.**

**Key Components:**
1. **Abstract Class (Template)** - Defines pipeline structure
2. **Template Method** - Final method that defines algorithm skeleton (cannot be overridden)
3. **Abstract Methods** - Steps that subclasses MUST implement
4. **Hook Methods** - Optional steps that subclasses CAN override

### Template Method Pattern Structure

```java
public abstract class VideoProcessor {

    // TEMPLATE METHOD (final - cannot be overridden!)
    public final void processVideo(String videoId) {
        // Step 1: Common behavior
        validate();

        // Step 2: Common behavior
        preprocess();

        // Step 3: Subclass-specific behavior (MUST implement)
        encode();

        // Step 4: Subclass-specific behavior (MUST implement)
        optimize();

        // Step 5: Hook (optional - default implementation)
        if (shouldAddWatermark()) {
            addWatermark();
        }

        // Step 6: Common behavior
        save();

        // Step 7: Common behavior
        notify();
    }

    // COMMON METHODS (implemented in base class)
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

    // ABSTRACT METHODS (subclasses MUST implement)
    protected abstract void encode();
    protected abstract void optimize();

    // HOOK METHODS (optional - default implementation)
    protected boolean shouldAddWatermark() {
        return true;  // Default: add watermark
    }

    protected void addWatermark() {
        System.out.println("✓ Adding watermark...");
    }
}
```

**Subclass 1: Standard Video**
```java
public class StandardVideoProcessor extends VideoProcessor {

    @Override
    protected void encode() {
        System.out.println("  → Transcoding to 1080p...");
    }

    @Override
    protected void optimize() {
        System.out.println("  → Applying standard compression...");
    }

    @Override
    protected void addWatermark() {
        System.out.println("  → Adding standard watermark...");
    }
}
```

**Subclass 2: Premium Video**
```java
public class PremiumVideoProcessor extends VideoProcessor {

    @Override
    protected void encode() {
        System.out.println("  → Transcoding to 4K...");
        System.out.println("  → Processing HDR metadata...");
    }

    @Override
    protected void optimize() {
        System.out.println("  → Applying high-quality compression...");
    }

    @Override
    protected void addWatermark() {
        System.out.println("  → Adding premium watermark (subtle)...");
    }
}
```

**Subclass 3: Live Stream**
```java
public class LiveStreamProcessor extends VideoProcessor {

    @Override
    protected void encode() {
        System.out.println("  → Encoding live stream recording...");
        System.out.println("  → Processing chat overlay...");
    }

    @Override
    protected void optimize() {
        System.out.println("  → Optimizing for streaming...");
        System.out.println("  → Generating adaptive bitrate versions...");
    }

    @Override
    protected void addWatermark() {
        System.out.println("  → Adding 'LIVE' badge...");
    }
}
```

### Benefits of Template Method

**1. DRY Principle (Don't Repeat Yourself)**
- Common steps (validate, preprocess, save, notify) defined ONCE
- Duplicated code: 400 lines → 80 lines (80% reduction)
- Single source of truth for common behavior

**2. Consistent Pipeline Guaranteed**
- All processors follow EXACT same workflow
- Impossible to skip steps or change order
- Compile-time enforcement via final template method

**3. Easy Maintenance**
- Bug in validation? Fix in ONE place (base class)
- Change CDN logic? Update ONE method
- All subclasses automatically inherit fix

**4. Simple Extension**
- New video type (Shorts)? Extend base class
- Only implement variable steps (encode, optimize)
- Common steps automatically included
- **Time to add:** 1 hour vs 9 hours

**5. Open/Closed Principle**
- Open for extension (add new video types)
- Closed for modification (base pipeline unchanged)

---

## 📊 Requirements Analysis

### Functional Requirements

**FR1: Video Processing Pipeline**
- System shall define consistent 6-step processing pipeline
- All video types must follow same pipeline order
- Pipeline: validate → preprocess → encode → optimize → watermark → save → notify

**FR2: Common Steps Implementation**
- Base class shall implement common steps (validate, preprocess, save, notify)
- Common steps shall be private/final (cannot be overridden)
- All subclasses automatically inherit common behavior

**FR3: Variable Steps (Abstract Methods)**
- Subclasses MUST implement encode() method
- Subclasses MUST implement optimize() method
- Compiler shall enforce implementation (abstract methods)

**FR4: Optional Steps (Hooks)**
- Base class shall provide hook method shouldAddWatermark()
- Default behavior: add watermark (return true)
- Subclasses CAN override to customize behavior

**FR5: Video Type Specific Processing**
- StandardVideoProcessor: 1080p, standard compression
- PremiumVideoProcessor: 4K, HDR, high-quality compression
- LiveStreamProcessor: Live encoding, chat overlay, adaptive bitrate

**FR6: Template Method Protection**
- processVideo() method shall be final (cannot be overridden)
- Ensures pipeline structure never changes
- Subclasses can only customize specific steps

### Non-Functional Requirements

**NFR1: Code Reusability**
- Common code shall be defined in ONE place (base class)
- Code duplication reduction: > 70%
- New video type requires < 50 lines of code

**NFR2: Maintainability**
- Bug fix in common step shall affect all processors (no manual updates)
- Time to fix pipeline bug: < 10 minutes (vs 30 minutes)

**NFR3: Extensibility**
- Adding new video type shall take < 1 hour
- No modification to existing processors required
- Follows Open/Closed Principle

**NFR4: Type Safety**
- Abstract methods shall be enforced at compile-time
- Missing implementation shall cause compilation error
- No runtime errors due to missing steps

**NFR5: Readability**
- Pipeline workflow shall be visible in template method
- Each step shall have clear, descriptive name
- Code shall be self-documenting

---

## 🎯 Success Metrics

### Time Savings

**Before Template Method (Current State):**
- Bug fix time: 3 files × 10 minutes = 30 minutes
- Bugs per month: 5 inconsistency bugs
- Monthly time waste: 5 × 30 min = 2.5 hours
- Adding new video type: 9 hours
- **Annual time waste:** 2.5 hours × 12 = 30 hours

**After Template Method:**
- Bug fix time: 1 file × 5 minutes = 5 minutes
- Bugs per month: 1 (80% reduction due to consistency)
- Monthly time saved: 2.5 - 0.4 = 2.1 hours
- Adding new video type: 1 hour
- **Annual time saved:** 2.1 hours × 12 = 25.2 hours

### Code Metrics

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Total Lines | 600 | 250 | 58% reduction |
| Duplicated Lines | 400 | 0 | 100% elimination |
| Files to Update (bug fix) | 3 | 1 | 67% reduction |
| Time to Add Video Type | 9 hrs | 1 hr | 89% faster |
| Inconsistency Bugs | 5/month | 1/month | 80% reduction |

### ROI Calculation

**Development Cost:**
- Refactor to Template Method: 4 hours
- Testing: 2 hours
- **Total cost:** 6 hours

**Annual Savings:**
- Bug fixes: 25.2 hours
- New video type (1 per year): 8 hours saved
- **Total annual savings:** 33.2 hours

**ROI:**
```
ROI = [(33.2 - 6) / 6] × 100 = 453%
```

**5-Year Projection:**

| Year | Video Types | Hours Saved | Cumulative |
|------|-------------|-------------|------------|
| 1    | 3 → 4       | 33.2        | 33.2       |
| 2    | 4 → 5       | 41.0        | 74.2       |
| 3    | 5 → 7       | 57.4        | 131.6      |
| 4    | 7 → 9       | 73.8        | 205.4      |
| 5    | 9 → 12      | 98.6        | 304.0      |

**Total 5-year savings:** 304 hours
**5-year ROI:** [(304 - 6) / 6] × 100 = 4,967%

---

## 🏗️ Implementation Plan

### Class Structure

```
16-Template-Method-DP/
├── VideoProcessor.java          (Abstract base class)
│   ├── processVideo()           (Final template method)
│   ├── validate()               (Private - common)
│   ├── preprocess()             (Private - common)
│   ├── encode()                 (Abstract - must implement)
│   ├── optimize()               (Abstract - must implement)
│   ├── shouldAddWatermark()     (Hook - can override)
│   ├── addWatermark()           (Hook - can override)
│   ├── save()                   (Private - common)
│   └── notify()                 (Private - common)
│
├── StandardVideoProcessor.java  (Concrete class)
│   ├── encode()                 (1080p transcoding)
│   ├── optimize()               (Standard compression)
│   └── addWatermark()           (Standard watermark)
│
├── PremiumVideoProcessor.java   (Concrete class)
│   ├── encode()                 (4K + HDR)
│   ├── optimize()               (High-quality compression)
│   └── addWatermark()           (Premium watermark)
│
├── LiveStreamProcessor.java     (Concrete class)
│   ├── encode()                 (Live encoding + chat)
│   ├── optimize()               (Adaptive bitrate)
│   └── addWatermark()           (Live badge)
│
├── TemplateMethodDemo.java      (Client - demonstration)
└── package.bluej                (UML diagram)
```

### Processing Pipeline Details

**Step 1: Validate (Common)**
- Check video file format (MP4, AVI, MOV supported)
- Verify file size (< 10GB for standard, < 50GB for premium)
- Validate codec compatibility

**Step 2: Preprocess (Common)**
- Extract metadata (duration, resolution, framerate, codec)
- Generate thumbnail (extract frame at 10% position)
- Create preview clips (5-second samples)

**Step 3: Encode (Variable - Abstract)**
- **Standard:** Transcode to 1080p H.264
- **Premium:** Transcode to 4K H.265 with HDR metadata
- **Live:** Encode live stream with chat overlay

**Step 4: Optimize (Variable - Abstract)**
- **Standard:** Standard compression (CRF 23)
- **Premium:** High-quality compression (CRF 18) with HDR
- **Live:** Adaptive bitrate (multiple quality levels)

**Step 5: Add Watermark (Hook - Optional)**
- **Standard:** Bottom-right corner, semi-transparent
- **Premium:** Subtle top-left, minimal branding
- **Live:** "LIVE" badge with timestamp

**Step 6: Save (Common)**
- Upload to CDN (CloudFront/Akamai)
- Generate HLS playlist for streaming
- Store metadata in database

**Step 7: Notify (Common)**
- Send email notification to uploader
- Push notification to mobile app
- Update processing status in dashboard

### Hook Methods Strategy

**shouldAddWatermark()** - Controls watermark application
- Default: return true (add watermark)
- Premium can override: return false (no watermark for premium users)

**shouldGenerateChapters()** - Controls chapter generation
- Default: return false (no chapters)
- Live can override: return true (generate chapters from chat highlights)

**shouldApplyAIEnhancement()** - Controls AI-based enhancement
- Default: return false (no AI)
- Premium can override: return true (apply AI upscaling)

---

## 🎨 UML Class Diagram (Preview)

```
┌─────────────────────────────────────────────┐
│         VideoProcessor                      │ (Abstract)
├─────────────────────────────────────────────┤
│ + processVideo(): final void                │ ◄─── Template Method
│ # validate(): void                          │
│ # preprocess(): void                        │
│ # encode(): abstract void                   │ ◄─── Must implement
│ # optimize(): abstract void                 │ ◄─── Must implement
│ # shouldAddWatermark(): boolean             │ ◄─── Hook
│ # addWatermark(): void                      │ ◄─── Hook
│ # save(): void                              │
│ # notify(): void                            │
└─────────────────────────────────────────────┘
                    ▲
                    │
        ┌───────────┼───────────┐
        │           │           │
┌───────┴─────┐ ┌──┴────────┐ ┌┴──────────────┐
│  Standard   │ │  Premium  │ │  LiveStream   │
│VideoProcessor│ │VideoProcessor│ │  Processor    │
├─────────────┤ ├───────────┤ ├───────────────┤
│ + encode()  │ │ + encode()│ │ + encode()    │
│ + optimize()│ │ + optimize()│ │ + optimize() │
│ + addWater..│ │ + addWater..│ │ + addWater..│
└─────────────┘ └───────────┘ └───────────────┘
```

---

## 📝 Expected Output (Preview)

```
========================================
🎬 TEMPLATE METHOD PATTERN DEMO
========================================

Processing 3 video types:
1. Standard Video (1080p)
2. Premium Video (4K HDR)
3. Live Stream Recording

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
  → HDR: Yes
✓ Generating thumbnail...
  → High-res thumbnail saved: thumb_premium001.jpg

[STEP 3: ENCODE]
  → Transcoding to 4K H.265...
  → Processing HDR metadata...
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
📹 PROCESSING: Live Stream (live001.mp4)
========================================

[STEP 1: VALIDATE]
✓ Validating video format and size...
✓ Format: MP4 (live recording)
✓ Size: 5.8 GB (within limit)

[STEP 2: PREPROCESS]
✓ Extracting metadata...
  → Duration: 2:15:33 (live stream)
  → Resolution: 1920x1080
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
    • 1080p60 (source)
    • 720p60
    • 480p30
    • 360p30

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
✅ TEMPLATE METHOD BENEFITS DEMONSTRATED
========================================

1. ✓ Consistent Pipeline
   - All 3 video types follow EXACT same workflow
   - Impossible to skip steps or change order
   - Template method is final (cannot be overridden)

2. ✓ Code Reusability
   - Common steps defined ONCE in base class
   - 70% code duplication eliminated
   - Single source of truth

3. ✓ Easy Extension
   - Adding new video type requires < 50 lines
   - Only implement variable steps
   - Common steps inherited automatically

4. ✓ Maintainability
   - Bug fix in common step affects all processors
   - Update in ONE place (base class)
   - No manual synchronization needed

5. ✓ Type Safety
   - Abstract methods enforced at compile-time
   - Missing implementation causes compilation error
   - No runtime surprises

ROI: 453% in Year 1, 4,967% over 5 years
Pattern #10 in StreamFlix cluster - Complete! ✓
```

---

## 🎓 Learning Objectives

After implementing this pattern, you will understand:

1. **Template Method Core Concept**
   - Skeleton algorithm in base class
   - Variable steps in subclasses
   - Final template method prevents override

2. **Method Types**
   - Abstract methods (MUST implement)
   - Hook methods (CAN override)
   - Final methods (CANNOT override)

3. **Hollywood Principle**
   - "Don't call us, we'll call you"
   - Base class controls flow
   - Subclasses plug in behavior

4. **Inheritance vs Composition**
   - Template Method uses inheritance
   - Strategy Pattern uses composition
   - Trade-offs between approaches

5. **Open/Closed Principle**
   - Open for extension (new video types)
   - Closed for modification (base pipeline)

---

## 🔗 Pattern Relationships

### Links to Previous Patterns

**Builder (#5) → Template Method (#10)**
- Builder creates upload configuration
- Template Method processes uploaded video
- Both use multi-step workflows

**Memento (#9) → Template Method (#10)**
- Memento allows editing before upload
- Template Method processes after upload
- Sequential workflow: Edit → Upload → Process

**Factory Method (#6) vs Template Method (#10)**
- Factory Method: Creates different objects
- Template Method: Different algorithms on same object
- Both use abstract methods for variation

### Links to Future Patterns

**Template Method (#10) vs Strategy (#11)**
- Template Method: Inheritance-based variation
- Strategy: Composition-based variation
- Same problem, different solutions

**Template Method (#10) + Decorator (#12)**
- Template Method defines base processing
- Decorator adds optional enhancements
- Complementary patterns

---

## ✅ Definition of Done

Implementation is complete when:

- [x] Requirements document created (req-16.md)
- [ ] Solution documentation created (Documents/Solutions/TemplateMethod.md)
- [ ] VideoProcessor.java implemented (abstract base class)
- [ ] StandardVideoProcessor.java implemented
- [ ] PremiumVideoProcessor.java implemented
- [ ] LiveStreamProcessor.java implemented
- [ ] TemplateMethodDemo.java implemented (comprehensive demo)
- [ ] package.bluej created (UML diagram)
- [ ] All files compile successfully
- [ ] Demo output matches expected output
- [ ] Code demonstrates all learning objectives

---

## 📚 References

**Pattern Catalog:**
- Gang of Four: Template Method (p. 325)
- Head First Design Patterns: Chapter 8

**Real-World Examples:**
- Java: AbstractList, AbstractSet (collection frameworks)
- Android: Activity lifecycle (onCreate, onStart, onResume)
- Spring Framework: JdbcTemplate, RestTemplate
- React: Component lifecycle methods

**Key Principle:**
- Hollywood Principle: "Don't call us, we'll call you"
- Inversion of Control: Base class calls subclass methods

---

**Pattern #10 Requirements Complete!**
**Next Step:** Implement with `do req-16`
**Estimated Implementation Time:** 2 hours
**Estimated Learning Value:** ⭐⭐⭐⭐⭐ (Fundamental OOP pattern)
