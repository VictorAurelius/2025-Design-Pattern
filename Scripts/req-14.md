# REQ-14: Prototype Pattern Implementation

## Pattern Information
- **Pattern Name**: Prototype Pattern
- **Category**: Creational Pattern
- **Difficulty**: ⭐⭐⭐ (Medium)
- **Folder**: `14-Prototype-DP/`

---

## Context Linking Strategy (Memory Optimization)

### Current Video Platform Cluster (7 patterns):
1. ✅ **Adapter** - MediaPlayer (mp3, mp4, vlc compatibility)
2. ✅ **Observer** - YouTube Channel (notification system)
3. ✅ **Proxy** - StreamFlix (lazy video loading)
4. ✅ **Flyweight** - Video Player UI Icons (9,000x memory savings)
5. ✅ **Builder** - Video Upload Configuration (12 params without telescoping hell)
6. ✅ **Factory Method** - Video Export System (4 formats, extensible)
7. ✅ **Abstract Factory** - Video Player Theme System (consistent UI families)

### Adding Pattern #8:
8. **Prototype** → **Video Configuration Templates** 📋

**Why this context?**
- Natural extension: After creating upload config (Builder), users want to reuse it
- Links to: Video Upload (Builder), Video Platform features
- Real-world scenario: YouTube/Vimeo have "Save as preset" for upload settings
- Problem: Recreating 12-parameter configuration each time is tedious
- Solution: Clone existing configurations (templates/presets)
- Easy to remember: "Save favorite upload settings as templates"

---

## Recommended Context: Video Configuration Templates

### Scenario:
Users upload videos frequently with similar settings (e.g., gaming videos, tutorials, vlogs). Instead of reconfiguring **12 parameters** every time using Builder pattern, they want to:
- **Save configurations as templates** ("Gaming Setup", "Tutorial Setup", "Vlog Setup")
- **Clone templates** to create new uploads quickly
- **Modify clones** without affecting original template

### Current Problem (WITHOUT Prototype):

```java
// ❌ BAD: Must recreate configuration manually every time
public class VideoUploader {

    public void uploadGamingVideo() {
        // Recreate configuration with 12 parameters - TEDIOUS!
        VideoUpload config = new VideoUpload.VideoUploadBuilder(
            "Gaming Video",
            "/videos/game.mp4"
        )
            .description("Epic gaming moments")
            .addTag("gaming")
            .addTag("gameplay")
            .addTag("epic")
            .category("Gaming")
            .visibility("public")
            .commentsEnabled(true)
            .ageRestricted(false)
            .license("standard")
            .language("en")
            .allowEmbedding(true)
            .notifySubscribers(true)
            .build();

        // Upload...
    }

    public void uploadAnotherGamingVideo() {
        // Must recreate SAME configuration again! 😱
        VideoUpload config = new VideoUpload.VideoUploadBuilder(
            "Gaming Video 2",
            "/videos/game2.mp4"
        )
            .description("Epic gaming moments")  // Same!
            .addTag("gaming")                      // Same!
            .addTag("gameplay")                    // Same!
            .addTag("epic")                        // Same!
            .category("Gaming")                    // Same!
            .visibility("public")                  // Same!
            .commentsEnabled(true)                 // Same!
            .ageRestricted(false)                  // Same!
            .license("standard")                   // Same!
            .language("en")                        // Same!
            .allowEmbedding(true)                  // Same!
            .notifySubscribers(true)               // Same!
            .build();
        // 90% identical - huge waste of time!
    }
}
```

### Problems with this approach:

1. **Repetitive Configuration**
   - Must set 12 parameters for each upload
   - Same settings repeated 100s of times
   - Typing errors (typo in tags, wrong category)
   - Time wasted: 2 minutes per upload

2. **No Reusability**
   - Cannot save favorite configurations
   - Cannot share templates between videos
   - Must remember settings each time

3. **Manual Copying is Error-Prone**
```java
// Trying to copy manually - WRONG!
VideoUpload original = /* ... 12 parameters ... */;

// Attempt 1: Doesn't work - VideoUpload is immutable!
VideoUpload copy = original;  // Same reference, not a copy!
copy.setTitle("New title");   // No setter - immutable!

// Attempt 2: Manual field copying - error-prone!
VideoUpload copy = new VideoUpload.VideoUploadBuilder(
    "New title",
    original.getFilePath()  // Forgot to copy description!
)
    // Forgot to copy tags!
    .category(original.getCategory())
    // Forgot to copy 8 more fields!
    .build();
// Result: Incomplete copy! 😱
```

4. **Deep Copy vs Shallow Copy Issues**
```java
// Tags are a List - shallow copy problem!
List<String> originalTags = original.getTags();
List<String> copiedTags = originalTags;  // Shallow copy!

copiedTags.add("new-tag");
// originalTags also has "new-tag" now! 😱
// Both objects share the same List!
```

5. **Performance Cost**
```java
// Creating complex object is expensive
VideoUpload config = new VideoUpload.VideoUploadBuilder(...)
    .description(/* Database query to get description */)
    .addTag(/* API call to get recommended tags */)
    .category(/* ML model to predict category */)
    // ... expensive operations
    .build();

// Want to create 10 similar configs?
// Must repeat all expensive operations 10 times! 🐌
```

### Real-World Impact:

**User story**: Gaming YouTuber uploads 5 videos/day, all with similar settings

**Without Prototype:**
- Time per upload: 2 minutes (configuring 12 parameters)
- Uploads per day: 5 videos
- Daily time wasted: 10 minutes
- Monthly time wasted: 5 hours
- Yearly time wasted: 60 hours! 😱
- Error rate: 10% (typos, forgotten settings)

**With Prototype:**
- Time per upload: 10 seconds (clone template + change title/file)
- Uploads per day: 5 videos
- Daily time saved: 9.5 minutes
- Monthly time saved: 4.75 hours
- Yearly time saved: 57 hours! 🎉
- Error rate: 0% (perfect copies)

---

## Solution: Prototype Pattern

### Key Idea:
- **Create new objects by cloning existing objects** (prototypes)
- Avoid expensive initialization by copying pre-configured instances
- Support both **shallow copy** (copy references) and **deep copy** (copy objects)
- Save common configurations as reusable templates

### Structure:

```
Prototype (Interface)
    └─ VideoUpload (implements Cloneable)

Concrete Prototypes
    ├─ VideoUpload (with clone() method)
    └─ UploadTemplate (named configurations)

Prototype Registry
    └─ TemplateRegistry (stores named templates)

Client
    └─ PrototypeDemo
```

### Benefits:

1. **Fast Object Creation** ✅
   - Clone existing object vs build from scratch
   - 10x faster for complex objects

2. **Reusable Templates** ✅
   - Save favorite configurations
   - Share templates across videos
   - Consistent settings

3. **Avoid Expensive Initialization** ✅
   - Clone pre-initialized objects
   - Skip database queries, API calls, ML models

4. **Deep Copy Support** ✅
   - Independent copies (no shared references)
   - Modify clone without affecting original

5. **Easy Configuration Management** ✅
   - "Gaming Setup", "Tutorial Setup", "Vlog Setup"
   - One-click configuration

### Code with Prototype:

```java
// ✅ GOOD: Clone template instead of rebuilding

// 1. Create template once (with Builder)
VideoUpload gamingTemplate = new VideoUpload.VideoUploadBuilder(
    "Gaming Template",
    ""
)
    .description("Epic gaming moments")
    .addTag("gaming")
    .addTag("gameplay")
    .category("Gaming")
    .visibility("public")
    // ... 12 parameters configured once
    .build();

// 2. Save template to registry
TemplateRegistry.addTemplate("gaming", gamingTemplate);

// 3. Clone template for new uploads (fast!)
VideoUpload video1 = TemplateRegistry.getTemplate("gaming").clone();
video1.setTitle("Gaming Video 1");
video1.setFilePath("/videos/game1.mp4");

VideoUpload video2 = TemplateRegistry.getTemplate("gaming").clone();
video2.setTitle("Gaming Video 2");
video2.setFilePath("/videos/game2.mp4");

// Result: 2 seconds per upload vs 2 minutes! 🚀
```

---

## Implementation Requirements

### Files to Create (6 files):

1. **VideoUploadPrototype.java** (Prototype Interface)
   - `VideoUploadPrototype clone()`
   - Extends Cloneable interface

2. **VideoUpload.java** (Concrete Prototype) - Enhanced from Builder pattern
   - Implements VideoUploadPrototype
   - `clone()` method with deep copy
   - Clones all fields including List<String> tags
   - Setters for mutable fields (title, filePath)

3. **TemplateRegistry.java** (Prototype Registry)
   - Static Map<String, VideoUploadPrototype> templates
   - `addTemplate(String name, VideoUploadPrototype template)`
   - `getTemplate(String name)` returns clone
   - `removeTemplate(String name)`
   - `listTemplates()` returns all template names

4. **UploadTemplate.java** (Helper class)
   - Pre-configured templates: GAMING, TUTORIAL, VLOG, MUSIC
   - Static factory methods to create common templates
   - Uses Builder pattern internally

5. **PrototypeDemo.java** (Client)
   - Demonstrates cloning vs building from scratch
   - Shows deep copy vs shallow copy
   - Demonstrates template registry usage
   - Shows performance comparison
   - Links to Builder pattern

6. **package.bluej** (UML Diagram)
   - Shows Prototype interface
   - Shows concrete prototypes
   - Shows registry
   - Shows cloning relationships

---

## Expected Output

```
╔════════════════════════════════════════════════════════════╗
║            PROTOTYPE PATTERN DEMO                          ║
║         Video Configuration Templates                      ║
║  (Linked: StreamFlix Video Platform patterns)             ║
╚════════════════════════════════════════════════════════════╝


═══════════════════════════════════════════════════════════
PROBLEM: Without Prototype Pattern
═══════════════════════════════════════════════════════════

❌ Problems:
   1. Repetitive configuration (12 parameters each time)
   2. Time wasted (2 minutes per upload)
   3. Error-prone (typos, forgotten settings)
   4. No reusability (cannot save templates)
   5. Expensive initialization (database queries, API calls)

Example: Gaming YouTuber uploads 5 videos/day
   Without Prototype:
   - Time per video: 2 minutes
   - Daily time: 10 minutes
   - Yearly time: 60 HOURS wasted! 😱

   With Prototype:
   - Time per video: 10 seconds
   - Daily time: 50 seconds
   - Yearly time saved: 57 hours! 🎉


═══════════════════════════════════════════════════════════
SOLUTION: Prototype Pattern
═══════════════════════════════════════════════════════════

✅ Benefits:
   1. Fast object creation (clone vs build)
   2. Reusable templates (Gaming, Tutorial, Vlog)
   3. Deep copy support (independent objects)
   4. Avoid expensive initialization
   5. Consistent settings (no errors)


═══════════════════════════════════════════════════════════
TEST 1: Building from Scratch (Slow)
═══════════════════════════════════════════════════════════

⏱️  Creating VideoUpload using Builder pattern...

Building config with 12 parameters:
   - Title: Gaming Video 1
   - FilePath: /videos/game1.mp4
   - Description: Epic gaming moments
   - Tags: [gaming, gameplay, epic]
   - Category: Gaming
   - Visibility: public
   - Comments: enabled
   - Age Restriction: false
   - License: standard
   - Language: en
   - Embedding: allowed
   - Notify Subscribers: true

⏱️  Time: 150ms (expensive initialization)

✓ VideoUpload created!


═══════════════════════════════════════════════════════════
TEST 2: Cloning from Template (Fast)
═══════════════════════════════════════════════════════════

📋 Using Gaming template...

⏱️  Cloning template...
⏱️  Time: 5ms (10x faster!)

Cloned config:
   ✓ All 12 parameters copied
   ✓ Deep copy of tags list
   ✓ Independent object

Modifying clone:
   - New title: Gaming Video 2
   - New file: /videos/game2.mp4

Original template unchanged:
   ✓ Title: Gaming Template
   ✓ Tags: [gaming, gameplay, epic]

Clone modified:
   ✓ Title: Gaming Video 2
   ✓ Tags: [gaming, gameplay, epic]

✓ Clone and original are independent!


═══════════════════════════════════════════════════════════
TEST 3: Deep Copy vs Shallow Copy
═══════════════════════════════════════════════════════════

🔍 Demonstrating deep copy importance...

Creating original with tags: [gaming, gameplay]

❌ SHALLOW COPY (Wrong):
   Shallow copy shares tag list reference
   Adding tag to copy: "epic"
   Result:
      - Original tags: [gaming, gameplay, epic]  😱
      - Copy tags:     [gaming, gameplay, epic]
   Problem: Both objects share same list!

✅ DEEP COPY (Correct):
   Deep copy creates new tag list
   Adding tag to copy: "epic"
   Result:
      - Original tags: [gaming, gameplay]  ✓
      - Copy tags:     [gaming, gameplay, epic]  ✓
   Success: Independent lists!


═══════════════════════════════════════════════════════════
TEST 4: Template Registry
═══════════════════════════════════════════════════════════

📚 Registering pre-configured templates...

Template 1: GAMING
   - Description: Epic gaming moments
   - Tags: [gaming, gameplay, epic]
   - Category: Gaming
   - Visibility: public

Template 2: TUTORIAL
   - Description: Educational content
   - Tags: [tutorial, education, howto]
   - Category: Education
   - License: creative commons

Template 3: VLOG
   - Description: Daily life and adventures
   - Tags: [vlog, daily, lifestyle]
   - Category: Entertainment
   - Visibility: public

Template 4: MUSIC
   - Description: Original music content
   - Tags: [music, original, audio]
   - Category: Music
   - License: creative commons

✓ 4 templates registered!


═══════════════════════════════════════════════════════════
TEST 5: Using Template Registry
═══════════════════════════════════════════════════════════

🎮 Creating gaming videos from template...

Video 1:
   📋 Clone from "gaming" template
   ✏️  Set title: "Gaming Video 1"
   ✏️  Set file: "/videos/game1.mp4"
   ✓ Ready to upload!

Video 2:
   📋 Clone from "gaming" template
   ✏️  Set title: "Gaming Video 2"
   ✏️  Set file: "/videos/game2.mp4"
   ✓ Ready to upload!

Video 3:
   📋 Clone from "gaming" template
   ✏️  Set title: "Gaming Video 3"
   ✏️  Set file: "/videos/game3.mp4"
   ✓ Ready to upload!

✓ 3 videos configured in seconds!


═══════════════════════════════════════════════════════════
TEST 6: Performance Comparison
═══════════════════════════════════════════════════════════

📊 Creating 100 VideoUploads:

Method 1: Building from scratch
   ⏱️  Time: 15,000ms (15 seconds)
   💾 Memory: High (100 builder objects)
   ❌ Slow and resource-intensive

Method 2: Cloning from template
   ⏱️  Time: 500ms (0.5 seconds)
   💾 Memory: Low (1 template + 100 clones)
   ✅ 30x faster!

Performance improvement:
   - Speed: 30x faster
   - Memory: 50% less
   - CPU: 80% less
   - Code: 90% less (just clone + modify)


═══════════════════════════════════════════════════════════
TEST 7: Real-World Use Cases
═══════════════════════════════════════════════════════════

Use Case 1: Gaming YouTuber
   - Uploads: 5 videos/day
   - Template: Gaming preset
   - Time saved: 9.5 minutes/day = 57 hours/year
   - ROI: $2,850/year (at $50/hour)

Use Case 2: Tutorial Creator
   - Uploads: 10 videos/week
   - Template: Tutorial preset (CC license)
   - Time saved: 18 minutes/week = 15.6 hours/year
   - Consistency: 100% (no forgotten settings)

Use Case 3: Multi-Channel Creator
   - Channels: 3 (Gaming, Vlog, Tutorial)
   - Templates: 3 presets
   - Time saved: 100+ hours/year
   - Error reduction: 100%

✓ Prototype pattern saves time, money, and sanity!


═══════════════════════════════════════════════════════════
BENEFITS SUMMARY
═══════════════════════════════════════════════════════════

✅ Advantages:
   1. PERFORMANCE: 30x faster object creation
   2. REUSABILITY: Save and reuse configurations
   3. CONSISTENCY: No configuration errors
   4. DEEP COPY: Independent objects
   5. SIMPLICITY: Clone + modify (2 lines vs 12)
   6. MAINTAINABILITY: Change template once, affects all clones
   7. MEMORY EFFICIENT: Share template, clone cheaply

⚠️  Trade-offs:
   1. Must implement clone() correctly (easy to get wrong)
   2. Deep copy of nested objects requires careful coding
   3. Cloneable interface is somewhat dated (but still works)

📊 When to Use:
   ✅ Use Prototype when:
      - Object creation is expensive (DB queries, API calls)
      - Need many objects with similar configurations
      - Want to avoid subclasses for each variation
      - Object initialization is complex

   ❌ Don't use when:
      - Objects are simple (just use constructor)
      - Object creation is cheap
      - Don't need copies

📈 ROI Calculation:
   - Initial effort: 2 hours to implement Prototype
   - Time saved per upload: 1.5 minutes
   - Uploads per year: 500 videos
   - Total saved: 12.5 hours/year
   - ROI: 625% in first year!


╔════════════════════════════════════════════════════════════╗
║                 CONTEXT LINKING                            ║
╚════════════════════════════════════════════════════════════╝

🎬 Video Platform Design Pattern Cluster (8 patterns):
   1. ADAPTER: MediaPlayer (mp3, mp4, vlc compatibility)
   2. OBSERVER: YouTube Channel (notification system)
   3. PROXY: StreamFlix (lazy video loading)
   4. FLYWEIGHT: Video Player UI Icons (share icons, 9,000x memory)
   5. BUILDER: Video Upload (12 params without telescoping hell)
   6. FACTORY METHOD: Video Export (4 formats, extensible)
   7. ABSTRACT FACTORY: Video Player Theme (consistent UI families)
   8. PROTOTYPE: Video Config Templates (clone settings)

💡 Complete Video Platform Workflow:
   📥 1. Upload video (BUILDER with 12 parameters)
          ↓ Save as template
   📋 2. Clone template (PROTOTYPE for quick uploads)
   💾 3. Store video (PROXY for lazy loading)
   ▶️  4. Play video (ADAPTER for format compatibility)
   🎨 5. Show UI icons (FLYWEIGHT for memory optimization)
   🔔 6. Notify subscribers (OBSERVER pattern)
   📤 7. Export video (FACTORY METHOD for formats)
   🎨 8. Apply UI theme (ABSTRACT FACTORY for consistency)

🔗 Pattern Relationships:
   BUILDER + PROTOTYPE:
   - BUILDER: Create complex object (12 parameters)
   - PROTOTYPE: Clone built object (reuse configuration)
   - Together: Build once, clone many times!

🧠 Memorization Strategy:
   All 8 patterns in ONE domain = Super easy to remember!
   Think: "Video Platform = 8 patterns" → Instant recall! ⚡

   Creational patterns in our cluster:
   - BUILDER: "Build complex upload configuration"
   - FACTORY METHOD: "Create different export formats"
   - ABSTRACT FACTORY: "Create matching UI theme families"
   - PROTOTYPE: "Clone upload configuration templates"

════════════════════════════════════════════════════════════
```

---

## UML Class Diagram (package.bluej)

```
┌──────────────────────────────────┐
│  VideoUploadPrototype            │ ← Prototype Interface
│  <<interface>>                   │
├──────────────────────────────────┤
│ + clone(): VideoUploadPrototype  │
└──────────────────────────────────┘
                △
                │ implements
                │
┌───────────────┴──────────────────────┐
│         VideoUpload                  │ ← Concrete Prototype
├──────────────────────────────────────┤
│ - title: String                      │
│ - filePath: String                   │
│ - description: String                │
│ - tags: List<String>                 │
│ - ... (8 more fields)                │
├──────────────────────────────────────┤
│ + clone(): VideoUploadPrototype      │ ← Implements clone
│ + setTitle(title: String): void     │
│ + setFilePath(path: String): void   │
│ + getTags(): List<String>            │
│ + ... (getters)                      │
└──────────────────────────────────────┘


┌──────────────────────────────────────┐
│      TemplateRegistry                │ ← Registry
├──────────────────────────────────────┤
│ - templates: Map<String, Prototype>  │
├──────────────────────────────────────┤
│ + addTemplate(name, template): void  │
│ + getTemplate(name): Prototype       │ ← Returns clone!
│ + removeTemplate(name): void         │
│ + listTemplates(): List<String>      │
└──────────────────────────────────────┘
                │
                │ stores
                ↓
┌──────────────────────────────────────┐
│      VideoUploadPrototype            │
└──────────────────────────────────────┘


┌──────────────────────────────────────┐
│      UploadTemplate                  │ ← Helper
├──────────────────────────────────────┤
│ + createGamingTemplate(): VideoUpload│
│ + createTutorialTemplate(): VU       │
│ + createVlogTemplate(): VideoUpload  │
│ + createMusicTemplate(): VideoUpload │
└──────────────────────────────────────┘
                │
                │ creates using Builder
                ↓
┌──────────────────────────────────────┐
│      VideoUpload                     │
└──────────────────────────────────────┘


┌──────────────────────────────────────┐
│      PrototypeDemo                   │ ← Client
├──────────────────────────────────────┤
│ + main(args: String[]): void         │
└──────────────────────────────────────┘
                │
                │ uses
                ↓
┌──────────────────────────────────────┐
│      TemplateRegistry                │
└──────────────────────────────────────┘
```

### Clone Operation:

```
Original Object                    Cloned Object
┌─────────────────┐               ┌─────────────────┐
│ VideoUpload     │   clone()     │ VideoUpload     │
├─────────────────┤   ────────>   ├─────────────────┤
│ title: "T1"     │               │ title: "T1"     │
│ tags: [a, b]    │               │ tags: [a, b]    │ ← Deep copy!
└─────────────────┘               └─────────────────┘
       │                                  │
       │ Original list                    │ New list
       ↓                                  ↓
   [a, b, c]                          [a, b, c]

   Modifying clone doesn't affect original!
```

---

## Key Learning Points

### 1. When to Use Prototype

✅ **Use Prototype when:**
- Object creation is expensive (database queries, API calls, file I/O)
- Need many objects with similar configurations
- Want to avoid subclasses for each variation
- Object initialization is complex (12+ parameters)
- Need runtime configuration (don't know types at compile time)

❌ **Don't use Prototype when:**
- Objects are simple (1-3 fields)
- Object creation is cheap (just `new` is fine)
- Don't need multiple similar objects
- Objects are immutable singletons

### 2. Deep Copy vs Shallow Copy

**Shallow Copy** (wrong for mutable objects):
```java
// Shares references - BAD!
List<String> tags = original.getTags();
this.tags = tags;  // Both point to same list!
```

**Deep Copy** (correct):
```java
// Creates new list - GOOD!
this.tags = new ArrayList<>(original.getTags());
```

### 3. Prototype vs Other Creational Patterns

| Pattern | Purpose | When to Use |
|---------|---------|-------------|
| **Prototype** | Clone existing objects | Expensive initialization, similar configs |
| **Factory Method** | Create objects of different types | Need polymorphism, different types |
| **Abstract Factory** | Create families of objects | Need consistent families |
| **Builder** | Construct complex objects | Many parameters, step-by-step |

**In our Video Platform:**
- **Builder**: Create upload config (first time, 12 parameters)
- **Prototype**: Clone config (subsequent times, quick!)
- **Factory Method**: Create export format (MP4, AVI, MOV)
- **Abstract Factory**: Create UI theme (Button + Icon + Panel)

### 4. Prototype + Builder = Perfect Combo

```java
// Step 1: Use Builder to create template (once)
VideoUpload template = new VideoUpload.VideoUploadBuilder(...)
    .description("Gaming")
    .addTag("gaming")
    // ... 12 parameters
    .build();

// Step 2: Save template
TemplateRegistry.addTemplate("gaming", template);

// Step 3: Clone template (many times)
VideoUpload video1 = TemplateRegistry.getTemplate("gaming").clone();
VideoUpload video2 = TemplateRegistry.getTemplate("gaming").clone();
VideoUpload video3 = TemplateRegistry.getTemplate("gaming").clone();
// Fast and easy! 🚀
```

### 5. Real-World Examples

1. **Document Templates**:
   - Microsoft Word: Clone document templates
   - Google Docs: "Make a copy"

2. **Game Development**:
   - Unity: Prefabs (clone game objects)
   - Unreal: Blueprints

3. **E-commerce**:
   - Amazon: "Duplicate listing"
   - eBay: Clone product configurations

4. **Video Platforms**:
   - YouTube: Upload presets
   - Vimeo: Video templates

5. **Software Development**:
   - Git: Clone repositories
   - Docker: Clone containers

---

## Testing Checklist

- [ ] VideoUpload implements clone() with deep copy
- [ ] Tags list is deep copied (not shallow)
- [ ] TemplateRegistry stores and retrieves templates
- [ ] getTemplate() returns clone (not original)
- [ ] UploadTemplate provides 4 pre-configured templates
- [ ] Clone performance is 10x+ faster than building
- [ ] Demo shows building vs cloning comparison
- [ ] Demo shows deep copy vs shallow copy
- [ ] Demo shows template registry usage
- [ ] Demo shows real-world use cases
- [ ] Demo shows link to Builder pattern
- [ ] UML diagram shows prototype relationships

---

## Success Criteria

✅ Implementation is complete when:
1. All 6 Java files created (1 interface + 1 concrete prototype + 1 registry + 1 helper + 1 demo + 1 bluej)
2. clone() method performs deep copy correctly
3. Cloning is 10x+ faster than building from scratch
4. Template registry allows saving and retrieving templates
5. Demo output is comprehensive and educational
6. UML diagram clearly shows Prototype pattern structure
7. Pattern effectively links to Video Platform cluster (8 patterns total)
8. Shows integration with Builder pattern (build once, clone many)

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
├─ 6. FACTORY METHOD: Video Export System
│   └─ Problem: Switch/if-else for every format
│   └─ Solution: Each format has its own factory
│
├─ 7. ABSTRACT FACTORY: Video Player Theme
│   └─ Problem: Mixing UI components breaks consistency
│   └─ Solution: Factory creates matching component families
│
└─ 8. PROTOTYPE: Video Config Templates ⭐ NEW!
    └─ Problem: Recreating 12-parameter config is tedious
    └─ Solution: Clone existing configurations (templates)
```

**Complete workflow:**
1. User creates upload config (BUILDER - 12 parameters)
2. **User saves as template (PROTOTYPE - for reuse)** ⭐
3. User clones template for quick uploads (PROTOTYPE)
4. Video is stored lazily (PROXY)
5. User watches video (ADAPTER for compatibility)
6. UI shows play/pause icons (FLYWEIGHT for memory)
7. Subscribers get notified (OBSERVER)
8. User exports video (FACTORY METHOD)
9. UI applies consistent theme (ABSTRACT FACTORY)

**Creational Patterns in our cluster:**
- **BUILDER** (Pattern #5): Create complex object (first time)
- **FACTORY METHOD** (Pattern #6): Create different types (formats)
- **ABSTRACT FACTORY** (Pattern #7): Create families (themes)
- **PROTOTYPE** (Pattern #8): Clone objects (templates) ⭐

---

**End of req-14.md**
