# Requirements Document: Decorator Pattern (Pattern #21)

## Pattern Information
- **Pattern Name**: Decorator Pattern
- **Category**: Structural Design Pattern
- **Complexity**: ⭐⭐⭐ (Medium - involves wrapping objects with additional functionality)
- **Popularity**: ⭐⭐⭐⭐ (High - widely used for adding responsibilities dynamically)

## Business Context: StreamFlix Video Platform

### Domain
**Dynamic Video Stream Enhancement System**

StreamFlix needs to deliver video content with various optional features based on user subscription tier, preferences, and viewing context. Different users require different combinations of features:

- **Free users**: Basic video stream + ads
- **Premium users**: HD stream + subtitles + no ads
- **Business users**: HD stream + watermark + analytics tracking
- **Accessibility users**: Any stream + subtitles + audio description

Instead of creating a separate class for every possible combination (which would result in hundreds of classes), StreamFlix uses decorators to dynamically add features to video streams at runtime.

### Current Problem (Without Decorator Pattern)

**Class Explosion Nightmare:**
```java
// ❌ Creating classes for every combination!
public class BasicVideoStream { }
public class BasicVideoStreamWithAds { }
public class BasicVideoStreamWithSubtitles { }
public class BasicVideoStreamWithAdsAndSubtitles { }
public class HDVideoStream { }
public class HDVideoStreamWithAds { }
public class HDVideoStreamWithSubtitles { }
public class HDVideoStreamWithAdsAndSubtitles { }
public class HDVideoStreamWithWatermark { }
public class HDVideoStreamWithAdsAndWatermark { }
public class HDVideoStreamWithSubtitlesAndWatermark { }
public class HDVideoStreamWithAdsAndSubtitlesAndWatermark { }
// ... 100+ more classes for all combinations!

// ❌ Need to add analytics? Create 100+ more classes!
// ❌ Need to add audio description? Create 200+ more classes!
// ❌ IMPOSSIBLE TO MAINTAIN!
```

**Issues:**
1. ❌ **Class Explosion**: N features = 2^N classes
2. ❌ **Code Duplication**: Same logic repeated across classes
3. ❌ **Inflexible**: Cannot add features at runtime
4. ❌ **Hard to Test**: Must test every combination
5. ❌ **Tight Coupling**: Features hardcoded into classes
6. ❌ **No Composition**: Cannot mix and match features
7. ❌ **Impossible to Extend**: Adding one feature breaks everything

### Real-World Problem Scenario

**User Journey:**
```
User: Sarah (Premium subscriber) wants to watch a tutorial video

At 10:00 AM:
  - Sarah starts watching on laptop
  - Context: Home WiFi, quiet environment
  - Required Stream: HD + Subtitles (for quiet environment)

At 2:00 PM:
  - Sarah continues watching on mobile during commute
  - Context: Mobile data, noisy train
  - Required Stream: SD (save data) + Subtitles + Volume Boost

At 8:00 PM:
  - Sarah watches in bed
  - Context: Shared room with sleeping roommate
  - Required Stream: HD + Subtitles + Night Mode (reduced brightness)

Current System:
  ❌ Cannot dynamically adjust stream features
  ❌ Must restart video to change quality
  ❌ No way to combine multiple features
  ❌ Poor user experience
```

**Business User Journey:**
```
Company: TechCorp uses StreamFlix for training videos

Requirements:
  - Must have company watermark on all videos
  - Must track employee viewing analytics
  - Must support subtitles in 5 languages
  - Must restrict downloads

Current System:
  ❌ Would need custom video class:
      HDVideoStreamWithWatermarkAndAnalyticsAndSubtitlesAndDownloadRestriction
  ❌ Cannot reuse for other companies with different requirements
  ❌ Every new feature requires new class
  ❌ Maintenance nightmare
```

**Business Impact:**
- 💰 **High Development Cost**: Creating/maintaining 100+ video classes
- ⏱️ **Slow Feature Delivery**: 2 weeks to add one new feature
- 🐛 **High Bug Rate**: Changes break existing combinations
- 😞 **Poor UX**: Cannot customize viewing experience
- 📉 **Customer Churn**: Users leave for more flexible platforms
- 💸 **Lost Revenue**: Cannot offer customized business solutions

### Why This Occurs

1. **Inheritance Limitations**: Subclassing creates static relationships
   - Must decide all features at compile time
   - Cannot change features at runtime
   - Every combination needs separate class

2. **Feature Combinations**: Combinatorial explosion
   - 5 features = 32 possible combinations
   - 10 features = 1024 possible combinations
   - Impossible to create class for each

3. **Tight Coupling**: Features coupled to video class
   - Ads logic inside video class
   - Subtitles logic inside video class
   - Watermark logic inside video class
   - Cannot reuse features independently

4. **No Composition**: Cannot compose features dynamically
   - User preference changes require new classes
   - Context changes (WiFi → mobile data) need different classes
   - No way to add/remove features on the fly

## Requirements

### Functional Requirements

#### FR-1: Video Stream Component
The system must provide a base video stream interface:

**VideoStream Interface:**
```java
public interface VideoStream {
    /**
     * Play the video stream
     * @return Stream data with all applied enhancements
     */
    String play();

    /**
     * Get stream bitrate (quality)
     * @return Bitrate in kbps
     */
    int getBitrate();

    /**
     * Get description of stream features
     * @return Human-readable description
     */
    String getDescription();
}
```

**Concrete Streams:**
1. **BasicVideoStream** (480p, 500 kbps)
2. **HDVideoStream** (1080p, 2500 kbps)
3. **UHDVideoStream** (4K, 8000 kbps)

#### FR-2: Stream Decorators
The system must support dynamic feature addition through decorators:

**1. Subtitle Decorator**
- Adds subtitle overlay to video stream
- Supports multiple languages (EN, ES, FR, DE, JP)
- Can toggle on/off during playback
- Adds 100 kbps to bitrate

**2. Advertisement Decorator**
- Inserts ads at specified intervals
- Supports pre-roll, mid-roll, post-roll ads
- Tracks ad impressions
- Adds 200 kbps to bitrate

**3. Watermark Decorator**
- Overlays company logo/text watermark
- Configurable position (top-left, top-right, bottom-left, bottom-right)
- Configurable opacity (0-100%)
- Adds 50 kbps to bitrate

**4. Analytics Decorator**
- Tracks viewing metrics (play time, pause count, seek events)
- Logs user engagement data
- Reports to analytics service
- Adds 30 kbps to bitrate

**5. Quality Adjustment Decorator**
- Dynamically adjusts video quality based on bandwidth
- Switches between 480p/720p/1080p/4K
- Adaptive streaming (detects slow connection)
- Can increase or decrease bitrate by 50%

**6. DRM Decorator** (Digital Rights Management)
- Encrypts video stream
- Prevents unauthorized downloads
- Validates user license
- Adds 100 kbps to bitrate (encryption overhead)

#### FR-3: Dynamic Feature Composition
The system must allow combining decorators in any order:

**Example Combinations:**
```java
// Free user: Basic stream + ads
VideoStream stream = new BasicVideoStream();
stream = new AdvertisementDecorator(stream, "pre-roll");

// Premium user: HD + subtitles
VideoStream stream = new HDVideoStream();
stream = new SubtitleDecorator(stream, "EN");

// Business user: HD + watermark + analytics
VideoStream stream = new HDVideoStream();
stream = new WatermarkDecorator(stream, "TechCorp", "bottom-right");
stream = new AnalyticsDecorator(stream, "employee_training");

// Accessibility user: Basic + subtitles (multiple languages)
VideoStream stream = new BasicVideoStream();
stream = new SubtitleDecorator(stream, "EN");
stream = new SubtitleDecorator(stream, "ES"); // Multiple decorators of same type!

// Complex: HD + subs + watermark + analytics + quality adjust
VideoStream stream = new HDVideoStream();
stream = new SubtitleDecorator(stream, "EN");
stream = new WatermarkDecorator(stream, "ACME Inc", "top-right");
stream = new AnalyticsDecorator(stream, "corporate_training");
stream = new QualityAdjustmentDecorator(stream, "auto");
stream = new DRMDecorator(stream, "enterprise_license");
```

#### FR-4: Runtime Feature Management
The system must support adding/removing features at runtime:

1. **Add Feature**
   - Wrap existing stream with new decorator
   - No need to recreate stream
   - Seamless transition

2. **Remove Feature**
   - Unwrap decorator
   - Return to previous state
   - No interruption to playback

3. **Replace Feature**
   - Remove old decorator
   - Add new decorator
   - Example: Switch subtitle language EN → ES

4. **Query Features**
   - Check what decorators are currently applied
   - Get description of all active features
   - Calculate total bitrate

### Non-Functional Requirements

#### NFR-1: Extensibility
- **Add New Decorator**: Create one class, no modification to existing code
- **Open/Closed Principle**: Open for extension, closed for modification
- **Example**: Add BufferingDecorator, NightModeDecorator, VolumeBoostDecorator

#### NFR-2: Performance
- **Wrapping Overhead**: < 1ms per decorator
- **Memory Overhead**: < 100 bytes per decorator
- **Bitrate Calculation**: O(n) where n = number of decorators
- **Maximum Decorators**: Support at least 10 decorators per stream

#### NFR-3: Maintainability
- **Single Responsibility**: Each decorator handles one feature
- **Testability**: Each decorator independently testable
- **Debugging**: Clear description of applied features

#### NFR-4: Usability
- **Clear Composition**: Easy to understand decorator stacking
- **Type Safety**: Compile-time checking of decorator types
- **Readable Code**: Natural fluent interface for wrapping

### Acceptance Criteria

✅ **Implementation Complete When:**

1. VideoStream interface defines core operations
2. At least 3 concrete video streams (Basic, HD, UHD)
3. At least 5 concrete decorators implemented
4. Decorators can be stacked in any order
5. Multiple decorators of same type can be applied
6. Bitrate calculation includes all decorator overheads
7. Description shows all applied features in order
8. Demo shows at least 5 different user scenarios
9. UML diagram shows decorator hierarchy
10. Code compiles without errors

## Decorator Pattern Structure

### Components

#### 1. Component Interface
```java
/**
 * VideoStream - Core component interface
 */
public interface VideoStream {
    String play();
    int getBitrate();
    String getDescription();
}
```

#### 2. Concrete Components
```java
/**
 * BasicVideoStream - Concrete component (480p)
 */
public class BasicVideoStream implements VideoStream {
    @Override
    public String play() {
        return "Playing basic 480p video stream";
    }

    @Override
    public int getBitrate() {
        return 500; // kbps
    }

    @Override
    public String getDescription() {
        return "Basic Stream (480p, 500 kbps)";
    }
}

/**
 * HDVideoStream - Concrete component (1080p)
 */
public class HDVideoStream implements VideoStream {
    @Override
    public String play() {
        return "Playing HD 1080p video stream";
    }

    @Override
    public int getBitrate() {
        return 2500; // kbps
    }

    @Override
    public String getDescription() {
        return "HD Stream (1080p, 2500 kbps)";
    }
}
```

#### 3. Decorator Base Class
```java
/**
 * StreamDecorator - Abstract decorator
 *
 * Key aspects:
 * - Implements VideoStream (is-a relationship)
 * - Contains VideoStream reference (has-a relationship)
 * - Delegates to wrapped stream
 */
public abstract class StreamDecorator implements VideoStream {
    protected VideoStream wrappedStream;

    public StreamDecorator(VideoStream stream) {
        this.wrappedStream = stream;
    }

    @Override
    public String play() {
        return wrappedStream.play();
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate();
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription();
    }
}
```

#### 4. Concrete Decorators
```java
/**
 * SubtitleDecorator - Adds subtitle overlay
 */
public class SubtitleDecorator extends StreamDecorator {
    private String language;

    public SubtitleDecorator(VideoStream stream, String language) {
        super(stream);
        this.language = language;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + Subtitles (" + language + ")";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 100; // subtitle overhead
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Subtitles (" + language + ")";
    }
}

/**
 * AdvertisementDecorator - Inserts ads
 */
public class AdvertisementDecorator extends StreamDecorator {
    private String adType; // pre-roll, mid-roll, post-roll

    public AdvertisementDecorator(VideoStream stream, String adType) {
        super(stream);
        this.adType = adType;
    }

    @Override
    public String play() {
        String result = "";
        if (adType.contains("pre-roll")) {
            result += "[Playing pre-roll ad] ";
        }
        result += wrappedStream.play();
        if (adType.contains("post-roll")) {
            result += " [Playing post-roll ad]";
        }
        return result;
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 200;
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Ads (" + adType + ")";
    }
}

/**
 * WatermarkDecorator - Adds watermark overlay
 */
public class WatermarkDecorator extends StreamDecorator {
    private String watermarkText;
    private String position; // top-left, top-right, etc.

    public WatermarkDecorator(VideoStream stream, String text, String position) {
        super(stream);
        this.watermarkText = text;
        this.position = position;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + Watermark [" + watermarkText + " @ " + position + "]";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 50;
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Watermark (" + watermarkText + ")";
    }
}
```

### Class Diagram Structure
```
┌─────────────────────────────────────┐
│       VideoStream (Interface)       │
│           <<interface>>             │
├─────────────────────────────────────┤
│ + play(): String                    │
│ + getBitrate(): int                 │
│ + getDescription(): String          │
└────────┬────────────────────────────┘
         │
         │ implements
         │
    ┌────┴─────────────────┐
    │                      │
┌───▼──────────────┐  ┌───▼──────────────────┐
│ ConcreteStream   │  │  StreamDecorator     │ (Abstract Decorator)
├──────────────────┤  ├──────────────────────┤
│BasicVideoStream  │  │- wrappedStream       │◄────┐
│HDVideoStream     │  │+ StreamDecorator()   │     │ wraps
│UHDVideoStream    │  │+ play()              │     │
└──────────────────┘  │+ getBitrate()        │     │
                      │+ getDescription()    │     │
                      └──────────┬───────────┘     │
                                 │                 │
                                 │ extends         │
                                 │                 │
                      ┌──────────┴───────────┐     │
                      │                      │     │
            ┌─────────▼─────────┐  ┌────────▼──────────┐
            │SubtitleDecorator  │  │AdvertisementDec.  │
            ├───────────────────┤  ├───────────────────┤
            │- language: String │  │- adType: String   │
            │+ play()           │  │+ play()           │
            │+ getBitrate()     │  │+ getBitrate()     │
            └───────────────────┘  └───────────────────┘

            ┌─────────────────────────┐
            │ WatermarkDecorator      │
            ├─────────────────────────┤
            │- watermarkText: String  │
            │- position: String       │
            │+ play()                 │
            │+ getBitrate()           │
            └─────────────────────────┘
```

**Key Relationships:**
- **Component Interface**: VideoStream
- **Concrete Components**: BasicVideoStream, HDVideoStream, UHDVideoStream
- **Abstract Decorator**: StreamDecorator (implements VideoStream, wraps VideoStream)
- **Concrete Decorators**: SubtitleDecorator, AdvertisementDecorator, WatermarkDecorator, etc.

**Critical Aspect**: Decorator both "is-a" VideoStream AND "has-a" VideoStream!

## Use Cases

### Use Case 1: Free User
```
User: Alex (Free tier)
Video: "JavaScript Tutorial"

Stream Configuration:
  Base: BasicVideoStream (480p)
  + AdvertisementDecorator (pre-roll + mid-roll)

Code:
  VideoStream stream = new BasicVideoStream();
  stream = new AdvertisementDecorator(stream, "pre-roll,mid-roll");

Output:
  Description: "Basic Stream (480p, 500 kbps) + Ads (pre-roll,mid-roll)"
  Bitrate: 700 kbps (500 + 200)
  Play: "[Playing pre-roll ad] Playing basic 480p video stream"
```

### Use Case 2: Premium User
```
User: Maria (Premium tier)
Video: "Advanced React Patterns"

Stream Configuration:
  Base: HDVideoStream (1080p)
  + SubtitleDecorator (English)

Code:
  VideoStream stream = new HDVideoStream();
  stream = new SubtitleDecorator(stream, "EN");

Output:
  Description: "HD Stream (1080p, 2500 kbps) + Subtitles (EN)"
  Bitrate: 2600 kbps (2500 + 100)
  Play: "Playing HD 1080p video stream + Subtitles (EN)"
```

### Use Case 3: Business User
```
User: TechCorp (Business tier)
Video: "Employee Training - Security Protocols"

Stream Configuration:
  Base: HDVideoStream (1080p)
  + SubtitleDecorator (EN)
  + WatermarkDecorator ("TechCorp Confidential", "bottom-right")
  + AnalyticsDecorator ("employee_training")
  + DRMDecorator ("enterprise_license")

Code:
  VideoStream stream = new HDVideoStream();
  stream = new SubtitleDecorator(stream, "EN");
  stream = new WatermarkDecorator(stream, "TechCorp Confidential", "bottom-right");
  stream = new AnalyticsDecorator(stream, "employee_training");
  stream = new DRMDecorator(stream, "enterprise_license");

Output:
  Description: "HD Stream (1080p, 2500 kbps)
                + Subtitles (EN)
                + Watermark (TechCorp Confidential)
                + Analytics (employee_training)
                + DRM (enterprise_license)"
  Bitrate: 2780 kbps (2500 + 100 + 50 + 30 + 100)
  Play: "Playing HD 1080p video stream
         + Subtitles (EN)
         + Watermark [TechCorp Confidential @ bottom-right]
         + [Tracking analytics: employee_training]
         + [DRM protection enabled]"
```

### Use Case 4: Multi-Language Support
```
User: International viewer
Video: "Design Patterns Explained"

Stream Configuration:
  Base: BasicVideoStream (480p)
  + SubtitleDecorator (EN)
  + SubtitleDecorator (ES) // Multiple subtitles!
  + SubtitleDecorator (FR)

Code:
  VideoStream stream = new BasicVideoStream();
  stream = new SubtitleDecorator(stream, "EN");
  stream = new SubtitleDecorator(stream, "ES");
  stream = new SubtitleDecorator(stream, "FR");

Output:
  Description: "Basic Stream (480p, 500 kbps) + Subtitles (EN) + Subtitles (ES) + Subtitles (FR)"
  Bitrate: 800 kbps (500 + 100 + 100 + 100)
  Play: "Playing basic 480p video stream + Subtitles (EN) + Subtitles (ES) + Subtitles (FR)"
```

### Use Case 5: Context-Aware Streaming
```
User: Sarah (changing contexts throughout day)

Morning (Home WiFi):
  VideoStream stream = new HDVideoStream();
  stream = new SubtitleDecorator(stream, "EN");
  // Bitrate: 2600 kbps

Afternoon (Mobile data on commute):
  VideoStream stream = new BasicVideoStream(); // Switched to save data
  stream = new SubtitleDecorator(stream, "EN");
  stream = new QualityAdjustmentDecorator(stream, "auto"); // Adaptive quality
  // Bitrate: 600 kbps (lower quality for mobile)

Evening (Shared room):
  VideoStream stream = new HDVideoStream();
  stream = new SubtitleDecorator(stream, "EN");
  stream = new NightModeDecorator(stream); // Reduced brightness
  // Bitrate: 2650 kbps
```

## Expected Output (Demo Scenarios)

### Scenario 1: Free vs Premium Comparison
```
═══════════════════════════════════════════════════════════════
DECORATOR PATTERN - Dynamic Video Stream Enhancement
StreamFlix Video Platform
═══════════════════════════════════════════════════════════════

SCENARIO 1: Free vs Premium User Experience
─────────────────────────────────────────────────────────────

Video: "JavaScript Tutorial - Async/Await"

FREE USER (Alex):
  Stream: Basic 480p + Ads

  Configuration:
    ├─ BasicVideoStream (480p, 500 kbps)
    └─ AdvertisementDecorator (pre-roll, mid-roll)

  Total Bitrate: 700 kbps

  Playback:
    [Playing pre-roll ad: "Premium awaits! Upgrade now!"]
    Playing basic 480p video stream
    [Mid-roll ad at 10:00 - "StreamFlix Premium"]

  User Experience: ⭐⭐ (Ads interrupt learning)

PREMIUM USER (Maria):
  Stream: HD 1080p + Subtitles

  Configuration:
    ├─ HDVideoStream (1080p, 2500 kbps)
    └─ SubtitleDecorator (EN)

  Total Bitrate: 2600 kbps

  Playback:
    Playing HD 1080p video stream + Subtitles (EN)
    [No ads - uninterrupted learning]

  User Experience: ⭐⭐⭐⭐⭐ (Smooth, high-quality)

BENEFIT: Same video, different features applied dynamically!
```

### Scenario 2: Business Customer
```
SCENARIO 2: Business Customer with Custom Requirements
─────────────────────────────────────────────────────────────

Client: TechCorp Inc.
Video: "Cybersecurity Training Module 1"
Requirements:
  ✓ Company watermark (branding)
  ✓ Multi-language subtitles (EN, ES, FR)
  ✓ Analytics tracking (compliance)
  ✓ DRM protection (prevent leaks)
  ✓ HD quality (professionalism)

Stream Configuration:
  ├─ HDVideoStream (1080p, 2500 kbps)
  ├─ SubtitleDecorator (EN)
  ├─ SubtitleDecorator (ES)
  ├─ SubtitleDecorator (FR)
  ├─ WatermarkDecorator ("TechCorp Confidential", "top-right")
  ├─ AnalyticsDecorator ("cybersecurity_training_q1_2024")
  └─ DRMDecorator ("enterprise_license_techcorp")

Total Bitrate: 2930 kbps
  Base HD: 2500 kbps
  + Subtitles (EN): 100 kbps
  + Subtitles (ES): 100 kbps
  + Subtitles (FR): 100 kbps
  + Watermark: 50 kbps
  + Analytics: 30 kbps
  + DRM: 100 kbps (encryption overhead)

Playback Output:
  Playing HD 1080p video stream
  + Subtitles (EN)
  + Subtitles (ES)
  + Subtitles (FR)
  + Watermark [TechCorp Confidential @ top-right]
  + [Tracking analytics: cybersecurity_training_q1_2024]
  + [DRM protection enabled: enterprise_license_techcorp]

Features Added Dynamically: 6
Time to Configure: < 1 second
Maintenance Cost: $0 (no new classes needed!)

Without Decorator Pattern:
  Would need: HDVideoStreamWithTripleSubtitlesAndWatermarkAndAnalyticsAndDRM class
  + 100 other classes for all combinations
  Maintenance nightmare!
```

### Scenario 3: Runtime Feature Switching
```
SCENARIO 3: Dynamic Feature Switching During Playback
─────────────────────────────────────────────────────────────

User: Sarah watching "Design Patterns Course"
Context: Commuting on train (changing network conditions)

10:00 AM - Start watching at home (WiFi):
  Stream: HD (1080p) + Subtitles (EN)
  Bitrate: 2600 kbps
  ✓ Smooth playback

10:30 AM - Leave home, switch to mobile data:
  Network: 4G, bandwidth limited
  Action: Downgrade quality

  New Stream: Basic (480p) + Subtitles (EN) + Quality Adjust (auto)
  Bitrate: 650 kbps
  ✓ Reduced buffering

11:00 AM - Enter tunnel, poor connection:
  Network: 3G, very slow
  Action: Further downgrade

  New Stream: Basic (480p) + Quality Adjust (aggressive)
  Bitrate: 400 kbps (removed subtitles, ultra compression)
  ✓ Video continues playing (no interruption)

12:00 PM - Arrive at office, back on WiFi:
  Network: High-speed WiFi
  Action: Upgrade to best quality

  New Stream: UHD (4K) + Subtitles (EN)
  Bitrate: 8100 kbps
  ✓ Maximum quality restored

BENEFIT: Same video object, features added/removed dynamically!
No need to create new video instance!
Seamless user experience!
```

### Scenario 4: Decorator Stacking Order
```
SCENARIO 4: Understanding Decorator Order
─────────────────────────────────────────────────────────────

Base Video: "React Hooks Tutorial"

Decorators Applied (in order):
  1. BasicVideoStream         → "Playing basic 480p video stream"
  2. SubtitleDecorator (EN)   → + "Subtitles (EN)"
  3. WatermarkDecorator       → + "Watermark [Company @ top-right]"
  4. AdvertisementDecorator   → "[Pre-roll ad] " + content + " [Post-roll ad]"

Execution Flow (play() method):
  AdvertisementDecorator.play():
    → Adds "[Pre-roll ad]"
    → Calls wrappedStream.play() // WatermarkDecorator

  WatermarkDecorator.play():
    → Calls wrappedStream.play() // SubtitleDecorator
    → Adds "+ Watermark [Company @ top-right]"

  SubtitleDecorator.play():
    → Calls wrappedStream.play() // BasicVideoStream
    → Adds "+ Subtitles (EN)"

  BasicVideoStream.play():
    → Returns "Playing basic 480p video stream"

Final Output:
  "[Pre-roll ad] Playing basic 480p video stream + Subtitles (EN) + Watermark [Company @ top-right] [Post-roll ad]"

Bitrate Calculation (getBitrate() method):
  AdvertisementDecorator: 200 + wrappedStream.getBitrate()
  WatermarkDecorator:      50 + wrappedStream.getBitrate()
  SubtitleDecorator:      100 + wrappedStream.getBitrate()
  BasicVideoStream:       500 (base)

  Total: 500 + 100 + 50 + 200 = 850 kbps

KEY INSIGHT: Decorators are processed in reverse order for accumulation!
```

### Scenario 5: Extensibility Demo
```
SCENARIO 5: Adding New Features Without Modifying Existing Code
─────────────────────────────────────────────────────────────

NEW REQUIREMENT (Q2 2024): Add "Night Mode" feature
  - Reduces screen brightness for night viewing
  - Adds blue light filter
  - Requested by 10,000 users

WITHOUT Decorator Pattern:
  ❌ Modify 50+ existing video classes
  ❌ Create 100+ new combination classes
  ❌ Risk breaking existing functionality
  ❌ Testing nightmare (500+ test cases)
  ❌ Development time: 2 weeks
  ❌ Bug risk: HIGH

WITH Decorator Pattern:
  ✅ Create ONE new class: NightModeDecorator
  ✅ No modification to existing code (Open/Closed Principle)
  ✅ Works with all existing combinations
  ✅ Testing: Only test NightModeDecorator (10 test cases)
  ✅ Development time: 2 hours
  ✅ Bug risk: MINIMAL

Implementation (15 lines of code):

  public class NightModeDecorator extends StreamDecorator {
      private int brightnessReduction;

      public NightModeDecorator(VideoStream stream, int reduction) {
          super(stream);
          this.brightnessReduction = reduction;
      }

      @Override
      public String play() {
          return wrappedStream.play()
                 + " + Night Mode (brightness: -" + brightnessReduction + "%)";
      }

      @Override
      public int getBitrate() {
          return wrappedStream.getBitrate() + 20; // filter overhead
      }
  }

Usage (works immediately with all existing streams):

  VideoStream stream = new HDVideoStream();
  stream = new SubtitleDecorator(stream, "EN");
  stream = new NightModeDecorator(stream, 30); // Reduce brightness 30%

  Output: "HD Stream (1080p, 2500 kbps) + Subtitles (EN) + Night Mode (brightness: -30%)"

RESULT: Feature deployed in 2 hours instead of 2 weeks!
        Cost savings: $10,000+
        Zero bugs introduced!
        Customer satisfaction: ⭐⭐⭐⭐⭐
```

## Design Considerations

### 1. Decorator vs Inheritance

**Problem: Class Explosion**
```
Without Decorator (Inheritance approach):
  VideoStream
  ├─ BasicVideoStream
  ├─ BasicVideoStreamWithAds
  ├─ BasicVideoStreamWithSubtitles
  ├─ BasicVideoStreamWithAdsAndSubtitles
  ├─ HDVideoStream
  ├─ HDVideoStreamWithAds
  ├─ HDVideoStreamWithSubtitles
  ├─ HDVideoStreamWithAdsAndSubtitles
  └─ ... (100+ more classes)

With Decorator:
  VideoStream (interface)
  ├─ BasicVideoStream (concrete)
  ├─ HDVideoStream (concrete)
  └─ StreamDecorator (abstract)
      ├─ AdvertisementDecorator
      └─ SubtitleDecorator

  Total: 5 classes instead of 100+!
  Combinations: Unlimited, created at runtime!
```

**Decision: Use Decorator Pattern**
- Avoids class explosion
- Flexible composition
- Runtime configuration
- Easy to extend

### 2. Decorator Order Matters

**Problem: Different results based on order?**

Example:
```java
// Order 1: Subtitles → Ads
VideoStream stream1 = new BasicVideoStream();
stream1 = new SubtitleDecorator(stream1, "EN");
stream1 = new AdvertisementDecorator(stream1, "pre-roll");
// Output: "[Pre-roll ad] Playing basic stream + Subtitles (EN)"

// Order 2: Ads → Subtitles
VideoStream stream2 = new BasicVideoStream();
stream2 = new AdvertisementDecorator(stream2, "pre-roll");
stream2 = new SubtitleDecorator(stream2, "EN");
// Output: "Playing basic stream [Pre-roll ad] + Subtitles (EN)"
```

**Design Decision:**
- For video streams, order usually doesn't matter (all features applied to final output)
- For some decorators (like compression → encryption), order is critical
- Document expected order in decorator class documentation
- Consider adding validation if order matters

### 3. Decorator Identity

**Problem: How to check which decorators are applied?**

Options:
A) Type checking with instanceof
B) Visitor pattern to inspect decorators
C) Keep list of applied decorator types
D) getDescription() method (chosen)

**Chosen: D (getDescription() method)**
```java
VideoStream stream = new HDVideoStream();
stream = new SubtitleDecorator(stream, "EN");
stream = new WatermarkDecorator(stream, "ACME", "top-right");

System.out.println(stream.getDescription());
// Output: "HD Stream (1080p, 2500 kbps) + Subtitles (EN) + Watermark (ACME)"
```

**Benefits:**
- Simple and readable
- No reflection needed
- Works with all decorators
- Human-readable output

### 4. Performance Considerations

**Decorator Chain Overhead:**
```
10 decorators × 1ms per call = 10ms total overhead

play() call traverses entire chain:
  Decorator10.play() → Decorator9.play() → ... → BasicStream.play()

Optimization strategies:
  1. Cache results (if stream is immutable)
  2. Lazy evaluation (compute only when needed)
  3. Flattening (combine multiple decorators into one)
```

**Trade-off:**
- Flexibility vs Performance
- 10ms overhead is acceptable for video streaming (network latency >> 10ms)
- For high-performance scenarios (game engines), consider alternatives

## Benefits

1. ✅ **Avoids Class Explosion**: N features ≠ 2^N classes
2. ✅ **Open/Closed Principle**: Add features without modifying existing code
3. ✅ **Flexible Composition**: Mix and match features at runtime
4. ✅ **Single Responsibility**: Each decorator handles one feature
5. ✅ **Transparent Wrapping**: Client treats decorated object same as base object
6. ✅ **Incremental Enhancement**: Add features one at a time
7. ✅ **Runtime Configuration**: Change features without recompiling
8. ✅ **Easy Testing**: Test each decorator independently

## Drawbacks

1. ❌ **Complexity**: More objects created (decorator chain)
2. ❌ **Order Dependency**: Some decorators depend on order
3. ❌ **Identity Problems**: Decorated object ≠ original object (instanceof issues)
4. ❌ **Configuration Complexity**: Long decorator chains hard to understand
5. ❌ **Performance Overhead**: Method calls traverse entire chain

## When to Use Decorator Pattern

**✅ Use When:**
- Need to add responsibilities to objects dynamically
- Subclassing would cause class explosion
- Features can be combined in many ways
- Want to add/remove features at runtime
- Features are optional and independent

**✅ Real-World Use Cases:**
- Java I/O Streams: `BufferedReader(FileReader(file))`
- GUI components: borders, scrollbars, shadows
- Web frameworks: middleware, filters, interceptors
- Video/audio processing: filters, effects, codecs
- HTTP clients: authentication, logging, retry decorators

**❌ Don't Use When:**
- Only one combination of features needed (use inheritance)
- Features depend heavily on each other (use strategy)
- Performance is critical (decorator chain overhead)
- Simplified interface needed (use facade)

## Implementation Checklist

### Phase 1: Core Components
- [ ] Create `VideoStream` interface
- [ ] Create concrete streams:
  - [ ] `BasicVideoStream` (480p)
  - [ ] `HDVideoStream` (1080p)
  - [ ] `UHDVideoStream` (4K)

### Phase 2: Decorator Framework
- [ ] Create `StreamDecorator` abstract class
- [ ] Implement delegation to wrapped stream
- [ ] Add wrappedStream field and constructor

### Phase 3: Concrete Decorators
- [ ] Create `SubtitleDecorator`
- [ ] Create `AdvertisementDecorator`
- [ ] Create `WatermarkDecorator`
- [ ] Create `AnalyticsDecorator`
- [ ] Create `DRMDecorator`
- [ ] Create `QualityAdjustmentDecorator` (bonus)

### Phase 4: Demo & Documentation
- [ ] Create `DecoratorPatternDemo.java`
- [ ] Demonstrate 5+ user scenarios
- [ ] Show decorator stacking
- [ ] Show bitrate calculation
- [ ] Show extensibility (add new decorator)
- [ ] Create `package.bluej` with UML
- [ ] Create `Solution/Decorator.md`

### Phase 5: Testing & Validation
- [ ] Test each decorator independently
- [ ] Test decorator combinations
- [ ] Test decorator order variations
- [ ] Test bitrate calculations
- [ ] Verify no modification to existing code when adding decorator

## Success Criteria

✅ **Implementation Complete When:**
1. VideoStream interface defined with 3 methods
2. At least 3 concrete streams implemented
3. StreamDecorator abstract class created
4. At least 5 concrete decorators implemented
5. Decorators can be stacked in any order
6. Bitrate calculation includes all decorators
7. Description shows all applied features
8. Demo shows 5+ realistic scenarios
9. Adding new decorator requires only 1 new class
10. UML diagram shows decorator pattern structure

## Deliverables

1. **Source Code** (in `21-Decorator-DP/`)
   - `VideoStream.java` (interface)
   - `BasicVideoStream.java`, `HDVideoStream.java`, `UHDVideoStream.java` (concrete)
   - `StreamDecorator.java` (abstract decorator)
   - `SubtitleDecorator.java`, `AdvertisementDecorator.java`, `WatermarkDecorator.java`, `AnalyticsDecorator.java`, `DRMDecorator.java`, `QualityAdjustmentDecorator.java` (concrete decorators)
   - `DecoratorPatternDemo.java` (demo)

2. **Documentation**
   - `Solution/Decorator.md` (explanation, UML, examples)

3. **UML Diagram**
   - `package.bluej` (BlueJ class diagram)

## References

- **Gang of Four**: Decorator pattern for adding responsibilities dynamically
- **Java I/O Streams**: Classic example of Decorator pattern
- **Related Patterns**:
  - Adapter: Changes interface, Decorator adds responsibility
  - Composite: Tree structure, Decorator linear wrapping
  - Proxy: Controls access, Decorator adds features
  - Strategy: Changes algorithm, Decorator adds features

---

**Pattern #21 of 24 - Decorator Pattern**
**Video Platform Context: Dynamic Video Stream Enhancement**
**Next Pattern: #22 - TBD**
