# Requirements Document: Visitor Pattern (Pattern #23)

## Pattern Information
- **Pattern Name**: Visitor Pattern
- **Category**: Behavioral Design Pattern
- **Complexity**: ⭐⭐⭐⭐ (High - involves double dispatch and complex relationships)
- **Popularity**: ⭐⭐⭐ (Medium - powerful but complex, used in specialized scenarios)

## Business Context: StreamFlix Video Platform

### Domain
**Multi-Format Video Export and Analytics System**

StreamFlix manages various types of video content entities:
- **VideoFile**: Individual video files with metadata
- **Playlist**: Collections of videos
- **Category**: Hierarchical organization of content
- **Series**: Multi-episode video series

The platform needs to perform various operations on these entities:
- **Export Operations**: Export to different formats (JSON, XML, CSV, PDF reports)
- **Analytics Operations**: Calculate metrics (total duration, view counts, engagement rates)
- **Quality Check Operations**: Validate content quality, check encoding, detect issues
- **Backup Operations**: Generate backups with different strategies
- **Search Indexing**: Build search indices for different search engines

**The Challenge**: These operations need to be added/modified frequently based on business needs, but we don't want to modify the video entity classes every time a new operation is needed.

### Current Problem (Without Visitor Pattern)

**Operations Scattered Across Entity Classes:**
```java
// ❌ Every entity class contains ALL operation logic!

public class VideoFile {
    private String title;
    private int duration;
    private int views;

    // Core responsibility: Video data

    // ❌ Export operations embedded in class
    public String exportToJSON() { ... }
    public String exportToXML() { ... }
    public String exportToCSV() { ... }
    public String exportToPDF() { ... }

    // ❌ Analytics operations embedded in class
    public int calculateTotalDuration() { ... }
    public double calculateEngagementRate() { ... }
    public Map<String, Object> getAnalytics() { ... }

    // ❌ Quality check operations embedded in class
    public boolean validateQuality() { ... }
    public List<String> detectIssues() { ... }

    // ❌ Backup operations embedded in class
    public void backupToS3() { ... }
    public void backupToLocal() { ... }

    // ❌ 20+ methods for different operations!
    // ❌ Single Responsibility Principle violated!
    // ❌ Open/Closed Principle violated!
}

public class Playlist {
    // ❌ Same 20+ methods duplicated here!
    public String exportToJSON() { ... }
    public String exportToXML() { ... }
    // ... etc
}

public class Category {
    // ❌ Same 20+ methods duplicated again!
    public String exportToJSON() { ... }
    public String exportToXML() { ... }
    // ... etc
}
```

**Issues:**
1. ❌ **Scattered Responsibility**: Export logic scattered across all entity classes
2. ❌ **Code Duplication**: Similar export logic repeated in each class
3. ❌ **Violation of SRP**: Video classes handle both data AND operations
4. ❌ **Violation of OCP**: Must modify entity classes to add new operations
5. ❌ **Type Checking Hell**: Need instanceof checks for polymorphic operations
6. ❌ **Difficult to Test**: Operations mixed with data, hard to test independently
7. ❌ **Maintenance Nightmare**: Change to export format requires modifying all classes

### Real-World Problem Scenario

**Business User Journey: StreamFlix Data Export Feature**
```
Product Manager: "We need to export user data to JSON for API integration"

Developer: "OK, I'll add exportToJSON() to VideoFile"
  → 200 lines of JSON export code added to VideoFile
  → Test VideoFile (30 test cases)
  → Deploy ✓

1 week later...
Product Manager: "We also need JSON export for Playlist and Category"

Developer: "Need to copy JSON logic to 2 more classes"
  → Copy-paste 200 lines to Playlist
  → Copy-paste 200 lines to Category
  → Test all 3 classes (90 test cases)
  → Deploy ✓

2 weeks later...
Product Manager: "We also need XML export for GDPR compliance"

Developer: "Need to add exportToXML() to all 3 classes"
  → 3 classes × 200 lines = 600 lines of new code
  → Test all combinations (180 test cases)
  → Deploy ✓

1 month later...
Product Manager: "JSON format needs to change (add timestamps)"

Developer: "Need to modify all 3 classes"
  → Modify VideoFile.exportToJSON()
  → Modify Playlist.exportToJSON()
  → Modify Category.exportToJSON()
  → Risk: Might forget to update one class → inconsistent exports
  → Test all 3 classes again (90 test cases)
  → Deploy ✓

2 months later...
Product Manager: "We need 5 more export formats: CSV, PDF, Excel, Parquet, Avro"

Developer: "Need to add 5 methods to each of 3 classes = 15 new methods"
  → 15 methods × 200 lines = 3,000 lines of code
  → Test 15 × 30 = 450 test cases
  → 😱 Maintenance nightmare!
  → 💰 Estimated cost: $50,000
  → ⏱️ Development time: 6 weeks

Result:
  ❌ VideoFile has 20+ methods (SRP violated)
  ❌ 3,000+ lines of duplicated export logic
  ❌ Every format change requires modifying 3 classes
  ❌ High risk of inconsistencies
  ❌ Developer: "This is unsustainable!"
```

**Analytics Feature Request:**
```
Business: "We need analytics reports for investors"

Required Operations:
  - Calculate total duration across all videos
  - Calculate total views and engagement
  - Generate trend reports
  - Export analytics to Excel

Current System:
  ❌ Must add 10+ analytics methods to each entity class
  ❌ 3 classes × 10 methods = 30 new methods
  ❌ 1,500 lines of code
  ❌ 6 weeks of development
  ❌ VideoFile class now has 30+ methods!

Developer: "The classes are becoming god classes!"
Manager: "Can't we just... add methods?"
Developer: "That's what we've been doing. It's not working."
```

**Business Impact:**
- 💰 **High Development Cost**: $50,000 per new operation type
- ⏱️ **Slow Feature Delivery**: 6 weeks to add simple export format
- 🐛 **High Bug Rate**: Inconsistencies between classes
- 😞 **Developer Frustration**: Constant copy-paste programming
- 📉 **Code Quality Degradation**: God classes with 30+ methods
- 🔒 **Vendor Lock-in**: Cannot change data structures without breaking operations

### Why This Occurs

1. **No Separation of Operations**: Operations embedded in data classes
   - VideoFile contains export logic, analytics logic, backup logic
   - Every new operation = modify data classes
   - Violation of Single Responsibility Principle

2. **Type-Specific Logic Scattered**: Each operation needs different logic per type
   - Exporting VideoFile vs Playlist requires different code
   - But both are in separate classes
   - Cannot centralize operation logic

3. **Inflexible Design**: Cannot add operations without modifying classes
   - Closed for extension (must modify existing code)
   - Open for modification (violates OCP)
   - Cannot plug in new operations dynamically

4. **Polymorphism Limitations**: instanceof checks everywhere
   - Need to check type to perform correct operation
   - Switch statements based on type
   - Difficult to extend with new types

## Requirements

### Functional Requirements

#### FR-1: Video Entity Hierarchy
The system must support various video entity types:

**Element Interface:**
```java
public interface VideoElement {
    /**
     * Accept a visitor to perform operations on this element
     * This is the key method in Visitor pattern - enables double dispatch
     *
     * @param visitor The visitor performing the operation
     */
    void accept(VideoVisitor visitor);
}
```

**Concrete Elements:**
1. **VideoFile** - Individual video
   - Properties: id, title, duration, views, likes, uploadDate, fileSize
   - Accepts visitors to perform operations

2. **Playlist** - Collection of videos
   - Properties: id, name, videos (List<VideoFile>)
   - Accepts visitors to perform operations on entire playlist

3. **Category** - Hierarchical content organization
   - Properties: id, name, subcategories, videos
   - Accepts visitors to traverse hierarchy

4. **Series** - Multi-episode content
   - Properties: id, title, seasons, episodes
   - Accepts visitors to process series data

#### FR-2: Visitor Interface
The system must provide a visitor interface for operations:

**Visitor Interface:**
```java
public interface VideoVisitor {
    /**
     * Visit a VideoFile element
     * Perform operation specific to VideoFile
     */
    void visit(VideoFile video);

    /**
     * Visit a Playlist element
     * Perform operation specific to Playlist
     */
    void visit(Playlist playlist);

    /**
     * Visit a Category element
     * Perform operation specific to Category
     */
    void visit(Category category);

    /**
     * Visit a Series element
     * Perform operation specific to Series
     */
    void visit(Series series);
}
```

**Key Insight**: One method per element type (method overloading)

#### FR-3: Export Visitors
The system must support various export formats:

**1. JSONExportVisitor**
- Exports VideoFile to JSON: `{"id":1, "title":"...", "duration":30, ...}`
- Exports Playlist to JSON: `{"name":"...", "videos":[...]}`
- Exports Category to JSON: `{"name":"...", "subcategories":[...], "videos":[...]}`

**2. XMLExportVisitor**
- Exports VideoFile to XML: `<video><title>...</title>...</video>`
- Exports Playlist to XML: `<playlist><name>...</name><videos>...</videos></playlist>`
- Supports hierarchical XML structure

**3. CSVExportVisitor**
- Exports VideoFile to CSV: `1,"Title",30,1000,100`
- Exports Playlist to CSV: Flattened list of videos
- Handles quoting and escaping

**4. PDFReportVisitor** (Bonus)
- Generates PDF report for VideoFile with charts
- Generates PDF report for Playlist with summary
- Professional formatting with headers/footers

#### FR-4: Analytics Visitors
The system must support analytics operations:

**1. DurationCalculatorVisitor**
- Calculates total duration for VideoFile: video.duration
- Calculates total duration for Playlist: sum of all video durations
- Calculates total duration for Category: recursive sum including subcategories

**2. ViewCountVisitor**
- Counts total views for VideoFile
- Counts total views for Playlist
- Counts total views for Category (aggregated)

**3. EngagementRateVisitor**
- Calculates engagement rate: (likes / views) × 100
- Aggregates engagement across playlists
- Provides insights for recommendations

**4. AnalyticsReportVisitor**
- Generates comprehensive analytics report
- Includes duration, views, likes, engagement rate
- Provides summary statistics

#### FR-5: Quality Check Visitors
The system must support quality validation:

**1. QualityCheckVisitor**
- Validates video file properties (duration > 0, valid upload date)
- Checks playlist consistency (no duplicate videos)
- Validates category hierarchy (no circular references)

**2. IssueDetectorVisitor**
- Detects missing metadata
- Finds videos with low engagement
- Identifies quality issues

#### FR-6: Composite Structure Traversal
The system must traverse composite structures:

**Playlist Traversal:**
```java
Playlist playlist = new Playlist("My Playlist");
playlist.addVideo(video1);
playlist.addVideo(video2);

VideoVisitor visitor = new JSONExportVisitor();
playlist.accept(visitor); // Exports entire playlist with all videos
```

**Category Traversal:**
```java
Category parent = new Category("Tutorials");
Category child = new Category("JavaScript");
parent.addSubcategory(child);
child.addVideo(video1);

VideoVisitor visitor = new DurationCalculatorVisitor();
parent.accept(visitor); // Calculates total duration recursively
```

### Non-Functional Requirements

#### NFR-1: Extensibility
- **Add New Operation**: Create one new visitor class (15 minutes)
- **No Entity Modification**: Entity classes never change when adding operations
- **Open/Closed Principle**: Open for extension (new visitors), closed for modification (elements)

#### NFR-2: Maintainability
- **Centralized Logic**: All export logic in export visitors
- **Single Responsibility**: VideoFile handles data, visitors handle operations
- **Easy Testing**: Test visitors independently from elements

#### NFR-3: Performance
- **Visitor Creation**: O(1) - constant time
- **visit() Method**: O(1) per element
- **Traversal**: O(n) where n = number of elements

#### NFR-4: Type Safety
- **Compile-Time Checking**: Visitor must implement visit() for all element types
- **No instanceof**: Use double dispatch instead of type checking
- **Polymorphism**: Correct method called automatically

### Acceptance Criteria

✅ **Implementation Complete When:**

1. VideoElement interface with accept() method defined
2. VideoVisitor interface with visit() methods for all element types
3. At least 3 concrete elements (VideoFile, Playlist, Category)
4. At least 3 concrete visitors (JSON export, Duration calculator, Quality check)
5. Visitors can traverse composite structures (Playlist with videos)
6. New operations can be added without modifying element classes
7. Demo shows at least 5 different operations using visitors
8. UML diagram shows visitor pattern structure
9. Code compiles without errors
10. No instanceof checks used

## Visitor Pattern Structure

### Components

#### 1. Element Interface
```java
/**
 * VideoElement - Element interface
 * All video entities implement this to accept visitors
 */
public interface VideoElement {
    void accept(VideoVisitor visitor);
}
```

#### 2. Concrete Elements
```java
/**
 * VideoFile - Concrete element
 */
public class VideoFile implements VideoElement {
    private int id;
    private String title;
    private int duration;
    private int views;
    private int likes;

    // Only data and basic accessors
    // NO operation methods (export, analytics, etc.)

    @Override
    public void accept(VideoVisitor visitor) {
        // Double dispatch: delegates to visitor
        visitor.visit(this);
    }

    // Getters...
}

/**
 * Playlist - Concrete element (composite)
 */
public class Playlist implements VideoElement {
    private String name;
    private List<VideoFile> videos;

    @Override
    public void accept(VideoVisitor visitor) {
        visitor.visit(this);
        // Can also traverse children
        for (VideoFile video : videos) {
            video.accept(visitor);
        }
    }
}
```

#### 3. Visitor Interface
```java
/**
 * VideoVisitor - Visitor interface
 * One visit() method per element type (method overloading)
 */
public interface VideoVisitor {
    void visit(VideoFile video);
    void visit(Playlist playlist);
    void visit(Category category);
    void visit(Series series);
}
```

#### 4. Concrete Visitors
```java
/**
 * JSONExportVisitor - Concrete visitor
 * Exports elements to JSON format
 */
public class JSONExportVisitor implements VideoVisitor {
    private StringBuilder result = new StringBuilder();

    @Override
    public void visit(VideoFile video) {
        result.append("{");
        result.append("\"id\":" + video.getId() + ",");
        result.append("\"title\":\"" + video.getTitle() + "\",");
        result.append("\"duration\":" + video.getDuration());
        result.append("}");
    }

    @Override
    public void visit(Playlist playlist) {
        result.append("{");
        result.append("\"name\":\"" + playlist.getName() + "\",");
        result.append("\"videos\":[");
        // Visit all videos in playlist
        result.append("]");
        result.append("}");
    }

    public String getResult() {
        return result.toString();
    }
}

/**
 * DurationCalculatorVisitor - Concrete visitor
 * Calculates total duration across elements
 */
public class DurationCalculatorVisitor implements VideoVisitor {
    private int totalDuration = 0;

    @Override
    public void visit(VideoFile video) {
        totalDuration += video.getDuration();
    }

    @Override
    public void visit(Playlist playlist) {
        // Will be called for each video by playlist.accept()
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
```

### Class Diagram Structure
```
┌─────────────────────────────────┐
│   VideoElement (Interface)      │
│       <<interface>>             │
├─────────────────────────────────┤
│ + accept(visitor: VideoVisitor) │
└────────┬────────────────────────┘
         │
         │ implements
         │
    ┌────┴─────────────────┐
    │                      │
┌───▼──────────┐   ┌──────▼────────┐
│  VideoFile   │   │   Playlist    │
├──────────────┤   ├───────────────┤
│- id: int     │   │- name: String │
│- title: String│  │- videos: List │
│- duration: int│  ├───────────────┤
├──────────────┤   │+ accept()     │
│+ accept()    │   └───────────────┘
│+ getters()   │
└──────────────┘


┌─────────────────────────────────────────┐
│      VideoVisitor (Interface)           │
│           <<interface>>                 │
├─────────────────────────────────────────┤
│ + visit(video: VideoFile)               │
│ + visit(playlist: Playlist)             │
│ + visit(category: Category)             │
│ + visit(series: Series)                 │
└────────┬────────────────────────────────┘
         │
         │ implements
         │
    ┌────┴──────────────────────────┐
    │                               │
┌───▼──────────────┐   ┌────────────▼──────────────┐
│JSONExportVisitor │   │DurationCalculatorVisitor  │
├──────────────────┤   ├───────────────────────────┤
│- result: String  │   │- totalDuration: int       │
├──────────────────┤   ├───────────────────────────┤
│+ visit(VideoFile)│   │+ visit(VideoFile)         │
│+ visit(Playlist) │   │+ visit(Playlist)          │
│+ getResult()     │   │+ getTotalDuration()       │
└──────────────────┘   └───────────────────────────┘

        ┌────────────────────┐
        │XMLExportVisitor    │
        ├────────────────────┤
        │+ visit(VideoFile)  │
        │+ visit(Playlist)   │
        └────────────────────┘
```

**Key Relationships:**
- **VideoElement**: Accepts visitors (all concrete elements implement this)
- **VideoVisitor**: Visits elements (all concrete visitors implement this)
- **Double Dispatch**: element.accept(visitor) → visitor.visit(element)

## Use Cases

### Use Case 1: Export to JSON
```
User: Export VideoFile to JSON format

Element: VideoFile(id=1, title="JavaScript Tutorial", duration=30)

Operation:
  VideoVisitor visitor = new JSONExportVisitor();
  video.accept(visitor);
  String json = visitor.getResult();

Expected Output:
  {
    "id": 1,
    "title": "JavaScript Tutorial",
    "duration": 30,
    "views": 5000,
    "likes": 250
  }
```

### Use Case 2: Calculate Total Duration
```
User: Calculate total duration of playlist

Element: Playlist("Watch Later")
  - Video 1: 30 minutes
  - Video 2: 45 minutes
  - Video 3: 60 minutes

Operation:
  VideoVisitor visitor = new DurationCalculatorVisitor();
  playlist.accept(visitor);
  int total = visitor.getTotalDuration();

Expected Output:
  Total Duration: 135 minutes (2h 15m)
```

### Use Case 3: Quality Check
```
User: Validate video quality

Element: VideoFile(title="", duration=0) // Invalid!

Operation:
  VideoVisitor visitor = new QualityCheckVisitor();
  video.accept(visitor);
  boolean isValid = visitor.isValid();
  List<String> issues = visitor.getIssues();

Expected Output:
  Valid: false
  Issues:
    - Title is empty
    - Duration must be greater than 0
```

### Use Case 4: Multiple Formats
```
User: Export same playlist to JSON, XML, CSV

Element: Playlist with 3 videos

Operations:
  VideoVisitor jsonVisitor = new JSONExportVisitor();
  playlist.accept(jsonVisitor);

  VideoVisitor xmlVisitor = new XMLExportVisitor();
  playlist.accept(xmlVisitor);

  VideoVisitor csvVisitor = new CSVExportVisitor();
  playlist.accept(csvVisitor);

Benefit: Same playlist, 3 different exports, no modification to Playlist class!
```

### Use Case 5: Add New Operation
```
User: Add new operation (backup to cloud)

Without Visitor Pattern:
  ❌ Modify VideoFile.java
  ❌ Modify Playlist.java
  ❌ Modify Category.java
  ❌ Modify Series.java
  ❌ Test all 4 classes
  ❌ Development time: 1 week

With Visitor Pattern:
  ✅ Create CloudBackupVisitor.java (ONE new class)
  ✅ Implement visit() for each element type
  ✅ Test only CloudBackupVisitor
  ✅ Development time: 2 hours

Result: 20× faster development!
```

## Expected Output (Demo Scenarios)

### Scenario 1: Export Operations
```
═══════════════════════════════════════════════════════════════
VISITOR PATTERN - Flexible Operations on Object Structures
StreamFlix Video Platform
═══════════════════════════════════════════════════════════════

SCENARIO 1: Export Operations
─────────────────────────────────────────────────────────────

Video: "JavaScript Tutorial" (30 min, 5000 views, 250 likes)

Export to JSON:
  {
    "id": 1,
    "title": "JavaScript Tutorial",
    "duration": 30,
    "views": 5000,
    "likes": 250
  }

Export to XML:
  <video>
    <id>1</id>
    <title>JavaScript Tutorial</title>
    <duration>30</duration>
    <views>5000</views>
    <likes>250</likes>
  </video>

Export to CSV:
  1,"JavaScript Tutorial",30,5000,250

BENEFIT: 3 different formats without modifying VideoFile class!
```

### Scenario 2: Analytics Operations
```
SCENARIO 2: Analytics Operations
─────────────────────────────────────────────────────────────

Playlist: "Watch Later" (3 videos)
  1. JavaScript Tutorial (30 min, 5000 views, 250 likes)
  2. Python Basics (45 min, 8000 views, 400 likes)
  3. React Hooks (60 min, 12000 views, 600 likes)

Total Duration Calculation:
  Visitor: DurationCalculatorVisitor
  Result: 135 minutes (2 hours 15 minutes)

Total Views Calculation:
  Visitor: ViewCountVisitor
  Result: 25,000 views

Engagement Rate Calculation:
  Visitor: EngagementRateVisitor
  Result: 5.0% average engagement (1,250 likes / 25,000 views)

BENEFIT: Complex analytics without modifying Playlist class!
```

### Scenario 3: Quality Check
```
SCENARIO 3: Quality Check Operations
─────────────────────────────────────────────────────────────

Video 1: "Complete Tutorial" (60 min, 5000 views)
  Quality Check: ✓ PASS
  - All required fields present
  - Duration > 0
  - Valid upload date

Video 2: "" (0 min, 0 views)
  Quality Check: ✗ FAIL
  Issues detected:
    - Title is empty
    - Duration must be greater than 0
    - Upload date is missing

Video 3: "Test Video" (5 min, 10 views)
  Quality Check: ⚠ WARNING
  Issues detected:
    - Low view count (< 100 views)
    - May need promotion

BENEFIT: Comprehensive validation without modifying VideoFile class!
```

### Scenario 4: Adding New Operation
```
SCENARIO 4: Extensibility - Adding New Operation
─────────────────────────────────────────────────────────────

NEW REQUIREMENT: "We need to generate PDF reports"

WITHOUT Visitor Pattern:
  ❌ Modify VideoFile.java (add exportToPDF method)
  ❌ Modify Playlist.java (add exportToPDF method)
  ❌ Modify Category.java (add exportToPDF method)
  ❌ Modify Series.java (add exportToPDF method)
  ❌ Test all 4 modified classes (120 test cases)
  ❌ Risk breaking existing functionality
  ❌ Development time: 1 week
  ❌ Cost: $5,000

WITH Visitor Pattern:
  ✅ Create PDFReportVisitor.java (ONE new class, 100 lines)
  ✅ Implement visit() for VideoFile, Playlist, Category, Series
  ✅ Test only PDFReportVisitor (30 test cases)
  ✅ Zero risk to existing code
  ✅ Development time: 2 hours
  ✅ Cost: $200

Implementation:

  public class PDFReportVisitor implements VideoVisitor {
      private PDFDocument doc = new PDFDocument();

      public void visit(VideoFile video) {
          doc.addHeading(video.getTitle());
          doc.addField("Duration", video.getDuration() + " min");
          doc.addField("Views", video.getViews());
      }

      public void visit(Playlist playlist) {
          doc.addHeading("Playlist: " + playlist.getName());
          // Visit all videos...
      }

      public PDFDocument getDocument() {
          return doc;
      }
  }

Usage:
  VideoVisitor pdfVisitor = new PDFReportVisitor();
  video.accept(pdfVisitor);
  playlist.accept(pdfVisitor);

RESULT: Feature deployed in 2 hours instead of 1 week!
        Cost savings: $4,800 (96% reduction)
        Risk: Minimal (no changes to existing code)
```

### Scenario 5: Multiple Operations
```
SCENARIO 5: Multiple Operations on Same Structure
─────────────────────────────────────────────────────────────

Playlist: "JavaScript Course" (5 videos)

Operation 1: Export to JSON
  Visitor: JSONExportVisitor
  Result: playlist.json (2.5 KB)

Operation 2: Calculate Analytics
  Visitor: AnalyticsReportVisitor
  Result:
    - Total Duration: 180 minutes
    - Total Views: 35,000
    - Average Engagement: 4.5%

Operation 3: Quality Check
  Visitor: QualityCheckVisitor
  Result: 4/5 videos pass quality check

Operation 4: Generate Backup
  Visitor: CloudBackupVisitor
  Result: Uploaded to S3 bucket

Operation 5: Index for Search
  Visitor: SearchIndexVisitor
  Result: Indexed in Elasticsearch

BENEFIT: 5 different operations on same structure!
         No modification to Playlist class required!
         Each visitor is independent and testable!
```

## Design Considerations

### 1. Double Dispatch Mechanism

**Problem**: How does visitor know which visit() method to call?

**Solution: Double Dispatch**

```java
// Client code
video.accept(visitor);

// First dispatch: element.accept()
public void accept(VideoVisitor visitor) {
    visitor.visit(this); // "this" has compile-time type VideoFile
}

// Second dispatch: visitor.visit(VideoFile)
public void visit(VideoFile video) {
    // Correct method called!
}
```

**Key Insight**: Two dynamic dispatches determine correct method:
1. First dispatch: Which accept() method? (determined by element type)
2. Second dispatch: Which visit() method? (determined by element type passed to visit)

### 2. Visitor vs Strategy Pattern

**Strategy Pattern:**
- Encapsulates algorithms
- One strategy per operation
- Operation is primary focus

**Visitor Pattern:**
- Encapsulates operations on structure
- One visitor can handle multiple element types
- Element structure is primary focus

**Decision**: Use Visitor when:
- Have stable element hierarchy
- Need to add many operations
- Operations depend on element type

### 3. Element Hierarchy Stability

**Critical Consideration**: Visitor pattern assumes element hierarchy is stable.

**Impact of Adding New Element:**
- Must add visit() method to VideoVisitor interface
- Must update ALL existing visitor classes
- Breaking change!

**When Element Hierarchy Changes Frequently:**
- Visitor pattern may not be suitable
- Consider Strategy pattern instead
- Or use reflection-based approach

**StreamFlix Context:**
- Element types (VideoFile, Playlist, Category, Series) are stable
- New operations added frequently (export formats, analytics)
- Visitor pattern is appropriate

### 4. Return Values from Visitors

**Options:**

**A) Accumulate in Visitor (Chosen)**
```java
public class DurationCalculatorVisitor implements VideoVisitor {
    private int totalDuration = 0;

    public void visit(VideoFile video) {
        totalDuration += video.getDuration();
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
```

**B) Return from visit() Method**
```java
public interface VideoVisitor<T> {
    T visit(VideoFile video);
    T visit(Playlist playlist);
}
```

**Chosen: A (Accumulate in Visitor)**

**Reasoning:**
- Simpler interface (void methods)
- Visitor can maintain state across visits
- More flexible for complex operations

## Benefits

1. ✅ **Open/Closed Principle**: Add operations without modifying elements
2. ✅ **Single Responsibility**: Elements handle data, visitors handle operations
3. ✅ **Centralized Operations**: All export logic in one visitor class
4. ✅ **Easy to Add Operations**: Create new visitor class (15 minutes)
5. ✅ **Type Safety**: Compile-time checking of visitor methods
6. ✅ **No instanceof**: Use double dispatch instead
7. ✅ **Independent Testing**: Test visitors separately from elements
8. ✅ **Operation Reuse**: One visitor works for all element types

## Drawbacks

1. ❌ **Complex**: Double dispatch is hard to understand
2. ❌ **Breaking Changes**: Adding element type breaks all visitors
3. ❌ **Access to Internals**: Visitors need access to element data (public getters)
4. ❌ **Circular Dependency**: Elements know about visitors, visitors know about elements
5. ❌ **Verbose**: Many visit() methods if many element types

## When to Use Visitor Pattern

**✅ Use When:**
- Element hierarchy is stable (rarely add new element types)
- Need to add many operations frequently
- Operations depend on element concrete type
- Want to centralize operation logic
- Elements should not know about operations

**✅ Real-World Use Cases:**
- Compilers: AST traversal (code generation, optimization, pretty-printing)
- XML/HTML processing: Different operations on document structure
- File systems: Operations on files/directories (backup, search, indexing)
- Graphics: Operations on shape hierarchies (rendering, hit-testing, exporting)

**❌ Don't Use When:**
- Element hierarchy changes frequently
- Only one or two operations needed
- Operations don't depend on element type
- Simple iterator would suffice

## Implementation Checklist

### Phase 1: Element Hierarchy
- [ ] Create `VideoElement` interface with accept() method
- [ ] Create concrete elements:
  - [ ] `VideoFile` (individual video)
  - [ ] `Playlist` (collection of videos)
  - [ ] `Category` (hierarchical organization)

### Phase 2: Visitor Framework
- [ ] Create `VideoVisitor` interface
- [ ] Add visit() method for each element type
- [ ] Use method overloading (same name, different parameters)

### Phase 3: Export Visitors
- [ ] Create `JSONExportVisitor`
- [ ] Create `XMLExportVisitor`
- [ ] Create `CSVExportVisitor`

### Phase 4: Analytics Visitors
- [ ] Create `DurationCalculatorVisitor`
- [ ] Create `ViewCountVisitor`
- [ ] Create `EngagementRateVisitor`

### Phase 5: Quality Visitors
- [ ] Create `QualityCheckVisitor`
- [ ] Create `IssueDetectorVisitor`

### Phase 6: Demo & Documentation
- [ ] Create `VisitorPatternDemo.java`
- [ ] Demonstrate 5+ visitor scenarios
- [ ] Show extensibility (add new visitor)
- [ ] Show multiple operations on same structure
- [ ] Create `package.bluej` with UML
- [ ] Create `Solution/Visitor.md`

## Success Criteria

✅ **Implementation Complete When:**
1. VideoElement interface with accept() method
2. VideoVisitor interface with visit() methods for all elements
3. At least 3 concrete elements (VideoFile, Playlist, Category)
4. At least 5 concrete visitors (JSON, XML, Duration, Views, Quality)
5. Visitors can traverse composite structures
6. New operations added without modifying elements
7. Demo shows 5+ different operations
8. No instanceof checks used (double dispatch only)
9. UML diagram shows visitor pattern structure
10. Code compiles without errors

## Deliverables

1. **Source Code** (in `23-Visitor-DP/`)
   - `VideoElement.java` (element interface)
   - `VideoFile.java`, `Playlist.java`, `Category.java` (concrete elements)
   - `VideoVisitor.java` (visitor interface)
   - `JSONExportVisitor.java`, `XMLExportVisitor.java`, `CSVExportVisitor.java` (export visitors)
   - `DurationCalculatorVisitor.java`, `ViewCountVisitor.java`, `EngagementRateVisitor.java` (analytics visitors)
   - `QualityCheckVisitor.java` (quality visitor)
   - `VisitorPatternDemo.java` (demo)

2. **Documentation**
   - `Solution/Visitor.md` (explanation, UML, examples)

3. **UML Diagram**
   - `package.bluej` (BlueJ class diagram)

## References

- **Gang of Four**: Visitor pattern for operations on object structures
- **Double Dispatch**: Key mechanism enabling visitor pattern
- **Related Patterns**:
  - Composite: Often used with Visitor to traverse structures
  - Iterator: Visitor can use iterator to traverse elements
  - Interpreter: AST nodes accept visitors for interpretation

---

**Pattern #23 of 24 - Visitor Pattern**
**Video Platform Context: Multi-Format Export and Analytics System**
**Next Pattern: #24 - TBD**
