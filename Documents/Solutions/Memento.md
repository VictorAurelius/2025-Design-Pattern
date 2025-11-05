# Memento Design Pattern - Video Editor History (Undo/Redo System)

## Pattern Description

The **Memento Pattern** is a behavioral design pattern that allows you to save and restore the previous state of an object without revealing the details of its implementation. It provides the ability to restore an object to its previous state (undo via rollback) without violating encapsulation.

### Intent
- Capture and externalize an object's internal state so that the object can be restored to this state later
- Preserve encapsulation boundaries - the originator's state should not be exposed to other objects

### Key Components
1. **Originator** - The object whose state needs to be saved and restored (VideoEditor)
2. **Memento** - Stores the internal state of the Originator (EditorMemento)
3. **Caretaker** - Manages the mementos and decides when to save/restore state (EditorHistory)

### Critical Design Principle
The Memento must be **OPAQUE** to the Caretaker. This is achieved through **package-private access** in Java:
- Memento constructor and getters are package-private (no access modifier)
- Only classes in the same package (Originator) can access Memento internals
- Caretaker can store/retrieve Mementos but cannot peek inside them

---

## Problem: Video Editor Without Undo/Redo

### Context: StreamFlix Video Editing Workflow

**This is Pattern #9 in our StreamFlix cluster** (2nd Behavioral pattern after Observer #2)

**Pattern Linkage:**
- Links to **Builder (#5)**: Users build complex upload configurations
- Links to **Prototype (#8)**: Users clone templates for quick reuse
- **NEW: Video Editor History (#9)**: Users edit videos before uploading, need undo/redo capability

Content creators on StreamFlix edit videos before uploading:
- Trim unwanted sections (adjust startTime/endTime)
- Adjust volume levels
- Apply filters (grayscale, sepia, vintage, etc.)
- Add watermarks

**The Problem:**
Without undo/redo functionality, users face these issues:

1. **Irreversible Mistakes**
   - User accidentally trims too much → Cannot undo → Must restart editing
   - User applies wrong filter → Cannot undo → Must recreate from scratch
   - User adjusts volume too high → Cannot undo → Must remember exact previous value

2. **Time Waste**
   - Gaming YouTuber makes 5 edits, realizes edit #2 was wrong
   - Must restart entire editing session (10 minutes wasted)
   - No way to jump back to specific checkpoint

3. **Lost Experimentation**
   - User wants to try different filters to compare
   - Applies Filter A → Cannot compare with original → Stuck with Filter A
   - Cannot explore creative options

4. **No Edit History**
   - User makes 20 edits over 30 minutes
   - Cannot track what changed
   - Cannot revert to "5 edits ago" state

**Real-World Impact:**
- **GamingPro123** spends 10 minutes editing a highlight reel
- Accidentally trims the epic final kill at timestamp 4:55
- No undo button → Must restart entire editing process
- Total time wasted: 10+ minutes per mistake

### Why Not Just Use Version Control?

**Option 1: Manual Copies (No Pattern)**
```java
VideoEditor editor1 = new VideoEditor("video123.mp4");
// Before each edit, manually copy all state
int oldStart = editor1.getStartTime();
int oldEnd = editor1.getEndTime();
int oldVolume = editor1.getVolume();
// ... copy 6 fields manually

editor1.trimVideo(10, 120);

// Undo manually
editor1.setStartTime(oldStart);
editor1.setEndTime(oldEnd);
editor1.setVolume(oldVolume);
// ... restore 6 fields manually
```

**Problems:**
- Violates encapsulation (exposes all internal fields)
- Fragile (adding new field requires updating all undo code)
- No automatic history management
- Redo is impossible

**Option 2: Memento Pattern (CORRECT)**
```java
VideoEditor editor = new VideoEditor("video123.mp4");
EditorHistory history = new EditorHistory();

history.save(editor);           // Save checkpoint
editor.trimVideo(10, 120);      // Make changes

editor.restore(history.undo()); // Undo to previous state
editor.restore(history.redo()); // Redo the change
```

**Benefits:**
- Encapsulation preserved (Memento is opaque to EditorHistory)
- Automatic state capture (all 6 fields saved atomically)
- Undo/redo stacks managed automatically
- Adding new field only requires updating Memento internals

---

## Requirements Analysis

### Functional Requirements

**R1: State Capture**
- System shall capture complete VideoEditor state in EditorMemento
- State includes: videoId, startTime, endTime, volume, filter, watermark
- Memento must be immutable (final fields)

**R2: Undo Functionality**
- User can undo last edit operation
- System maintains stack of previous states
- Undo moves state from undo stack to redo stack
- Multiple undo operations supported

**R3: Redo Functionality**
- User can redo undone operation
- System maintains stack of undone states
- Redo moves state from redo stack to undo stack
- Multiple redo operations supported

**R4: History Management**
- New edit clears redo stack (cannot redo after new change)
- History displays edit descriptions
- User can check if undo/redo available

**R5: Encapsulation Preservation**
- EditorHistory cannot access EditorMemento internals
- Only VideoEditor can create and restore from EditorMemento
- Package-private access enforces this constraint

### Non-Functional Requirements

**NFR1: Performance**
- Save operation: < 1ms (create Memento)
- Restore operation: < 1ms (restore from Memento)
- Memory efficient (no deep copying of videoId string)

**NFR2: Type Safety**
- No casting required in client code
- Immutable mementos prevent accidental modification

**NFR3: Maintainability**
- Adding new VideoEditor field only requires updating EditorMemento
- Client code (MementoDemo) unaffected by state changes

---

## Effectiveness Analysis

### Time Savings Calculation

**Scenario: GamingPro123 - Gaming YouTuber**
- Edits 3 videos/day
- 10 edits per video
- 1 mistake requiring undo every 20 edits

**Without Memento Pattern:**
- Mistake occurs → Restart editing from beginning
- Average editing time before mistake: 5 minutes
- Time wasted: 5 minutes per mistake
- Mistakes per day: (3 videos × 10 edits) ÷ 20 = 1.5 mistakes
- **Daily time waste: 1.5 × 5 = 7.5 minutes**
- **Annual time waste: 7.5 min × 365 days = 2,738 minutes (45.6 hours)**

**With Memento Pattern:**
- Mistake occurs → Press undo (instant recovery)
- Time to undo: 2 seconds
- **Daily time saved: 7.5 - 0.05 = 7.45 minutes**
- **Annual time saved: 45.6 - 0.3 = 45.3 hours**

### ROI Analysis

**Development Cost:**
- 4 classes: VideoEditor, EditorMemento, EditorHistory, MementoDemo
- Implementation time: 2 hours
- Testing time: 1 hour
- **Total cost: 3 hours**

**Annual Savings:**
- Gaming YouTubers (3 creators): 45.3 hours each = 136 hours
- Tutorial creators (2 creators): 30 hours each = 60 hours
- Vloggers (5 creators): 20 hours each = 100 hours
- **Total annual savings: 296 hours**

**ROI Calculation:**
```
ROI = [(296 - 3) / 3] × 100 = 9,767%
```

### 5-Year Projection

| Year | Creators | Hours Saved | Cumulative Savings |
|------|----------|-------------|-------------------|
| 1    | 10       | 296         | 296               |
| 2    | 15       | 444         | 740               |
| 3    | 25       | 740         | 1,480             |
| 4    | 35       | 1,036       | 2,516             |
| 5    | 50       | 1,480       | 3,996             |

**Total 5-year time savings: 3,996 hours**

### Qualitative Benefits

**1. Creative Freedom**
- Creators can experiment with filters without fear
- "Try this filter → Don't like it → Undo → Try another"
- Encourages exploration and creativity

**2. User Confidence**
- Knowing undo is available reduces stress
- Users take more risks (leading to better content)
- "I can always undo if this doesn't work"

**3. Professional Workflow**
- Matches industry-standard video editors (Adobe Premiere, Final Cut Pro)
- Users familiar with Ctrl+Z expect this functionality
- Competitive feature parity

**4. Error Recovery**
- Accidental clicks (common on mobile) can be undone
- Network interruptions won't lose progress
- System errors can be recovered from

---

## Implementation

### Project Structure
```
15-Memento-DP/
├── VideoEditor.java           (Originator - 6 state fields)
├── EditorMemento.java         (Memento - package-private!)
├── EditorHistory.java         (Caretaker - undo/redo stacks)
├── MementoDemo.java           (Client - demonstration)
└── package.bluej              (UML diagram)
```

### Class Diagram (UML)
```
┌─────────────────────────────┐
│      VideoEditor            │ (Originator)
├─────────────────────────────┤
│ - videoId: String           │
│ - startTime: int            │
│ - endTime: int              │
│ - volume: int               │
│ - filter: String            │
│ - watermark: String         │
├─────────────────────────────┤
│ + VideoEditor(videoId)      │
│ + trimVideo(start, end)     │
│ + adjustVolume(level)       │
│ + applyFilter(filter)       │
│ + addWatermark(text)        │
│ + save(): EditorMemento     │◄─┐
│ + restore(memento): void    │  │
│ + showState(): void         │  │
└─────────────────────────────┘  │
                                  │
        ┌─────────────────────────┘
        │
        │ creates/restores
        │
        ▼
┌─────────────────────────────┐
│     EditorMemento           │ (Memento)
├─────────────────────────────┤
│ - videoId: String           │ (package-private!)
│ - startTime: int            │
│ - endTime: int              │
│ - volume: int               │
│ - filter: String            │
│ - watermark: String         │
├─────────────────────────────┤
│ EditorMemento(...)          │ (package-private constructor)
│ String getVideoId()         │ (package-private getter)
│ int getStartTime()          │ (package-private getter)
│ ... (all getters)           │
└─────────────────────────────┘
        ▲
        │
        │ stores (opaque!)
        │
┌─────────────────────────────┐
│    EditorHistory            │ (Caretaker)
├─────────────────────────────┤
│ - undoStack: Stack          │
│ - redoStack: Stack          │
├─────────────────────────────┤
│ + save(editor): void        │
│ + undo(): EditorMemento     │
│ + redo(): EditorMemento     │
│ + canUndo(): boolean        │
│ + canRedo(): boolean        │
│ + clear(): void             │
│ + showHistory(): void       │
└─────────────────────────────┘
```

### Key Implementation Details

#### 1. EditorMemento - Package-Private Access
```java
// NO public modifier! This makes it package-private
class EditorMemento {
    private final String videoId;
    private final int startTime;
    // ... other final fields

    // Package-private constructor
    EditorMemento(String videoId, int startTime, ...) {
        this.videoId = videoId;
        // ...
    }

    // Package-private getters
    String getVideoId() { return videoId; }
    int getStartTime() { return startTime; }
    // ...
}
```

**Why package-private?**
- VideoEditor (same package) CAN access: ✓
- EditorHistory (same package) CAN store but CANNOT access internals: ✓
- Client code (MementoDemo) CANNOT access: ✓
- Encapsulation preserved!

#### 2. EditorHistory - Two-Stack Undo/Redo
```java
public class EditorHistory {
    private Stack<EditorMemento> undoStack = new Stack<>();
    private Stack<EditorMemento> redoStack = new Stack<>();

    public void save(VideoEditor editor) {
        undoStack.push(editor.save());
        redoStack.clear();  // Critical! New change clears redo
    }

    public EditorMemento undo() {
        if (undoStack.isEmpty()) return null;
        EditorMemento current = undoStack.pop();
        redoStack.push(current);
        return undoStack.isEmpty() ? null : undoStack.peek();
    }

    public EditorMemento redo() {
        if (redoStack.isEmpty()) return null;
        EditorMemento memento = redoStack.pop();
        undoStack.push(memento);
        return memento;
    }
}
```

**Critical Behavior:**
- `save()` clears redo stack (you cannot redo after making a new change)
- `undo()` pops from undo, pushes to redo, returns NEW state to restore
- `redo()` pops from redo, pushes to undo, returns state to restore

#### 3. VideoEditor - Save/Restore Methods
```java
public class VideoEditor {
    private String videoId;
    private int startTime;
    private int endTime;
    private int volume;
    private String filter;
    private String watermark;

    // Create memento
    public EditorMemento save() {
        return new EditorMemento(
            videoId, startTime, endTime,
            volume, filter, watermark
        );
    }

    // Restore from memento
    public void restore(EditorMemento memento) {
        if (memento == null) return;
        this.videoId = memento.getVideoId();
        this.startTime = memento.getStartTime();
        this.endTime = memento.getEndTime();
        this.volume = memento.getVolume();
        this.filter = memento.getFilter();
        this.watermark = memento.getWatermark();
    }
}
```

#### 4. Client Workflow
```java
VideoEditor editor = new VideoEditor("video123.mp4");
EditorHistory history = new EditorHistory();

// Initial save
history.save(editor);

// Make edits
editor.trimVideo(10, 120);
history.save(editor);

editor.adjustVolume(75);
history.save(editor);

editor.applyFilter("sepia");
history.save(editor);

// Undo twice
editor.restore(history.undo());  // Undo sepia filter
editor.restore(history.undo());  // Undo volume adjustment

// Redo once
editor.restore(history.redo());  // Redo volume adjustment

// New change clears redo
editor.addWatermark("StreamFlix");
history.save(editor);  // Redo stack cleared!
```

---

## Expected Output

```
========================================
🎬 MEMENTO PATTERN DEMO - Video Editor History
========================================

📹 Creating video editor for: video123.mp4
✓ Initial state saved to history

Current State:
├─ Video: video123.mp4
├─ Duration: 0s - 300s (5:00)
├─ Volume: 50%
├─ Filter: none
└─ Watermark: none

========================================
🎬 EDIT SESSION #1: Make Multiple Edits
========================================

✂️  EDIT 1: Trim video to 10s - 120s
✓ State saved to history

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 50%
├─ Filter: none
└─ Watermark: none

🔊 EDIT 2: Adjust volume to 75%
✓ State saved to history

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: none
└─ Watermark: none

🎨 EDIT 3: Apply sepia filter
✓ State saved to history

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: sepia
└─ Watermark: none

💧 EDIT 4: Add watermark 'StreamFlix'
✓ State saved to history

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: sepia
└─ Watermark: StreamFlix

========================================
📚 EDIT HISTORY
========================================
Undo Stack (4 states):
  [3] trimVideo(10, 120) → adjustVolume(75) → applyFilter(sepia) → addWatermark(StreamFlix)
  [2] trimVideo(10, 120) → adjustVolume(75) → applyFilter(sepia)
  [1] trimVideo(10, 120) → adjustVolume(75)
  [0] trimVideo(10, 120)

Redo Stack (0 states): (empty)

Can undo? ✓ Yes
Can redo? ✗ No

========================================
⏪ UNDO OPERATIONS
========================================

↶ Undo #1: Removing watermark...
✓ Restored to previous state

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: sepia
└─ Watermark: none

↶ Undo #2: Removing sepia filter...
✓ Restored to previous state

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: none
└─ Watermark: none

↶ Undo #3: Reverting volume...
✓ Restored to previous state

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 50%
├─ Filter: none
└─ Watermark: none

========================================
📚 EDIT HISTORY (After Undo)
========================================
Undo Stack (1 state):
  [0] trimVideo(10, 120)

Redo Stack (3 states):
  [0] adjustVolume(75)
  [1] applyFilter(sepia)
  [2] addWatermark(StreamFlix)

Can undo? ✓ Yes
Can redo? ✓ Yes

========================================
⏩ REDO OPERATIONS
========================================

↷ Redo #1: Reapplying volume adjustment...
✓ Restored to redone state

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: none
└─ Watermark: none

↷ Redo #2: Reapplying sepia filter...
✓ Restored to redone state

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: sepia
└─ Watermark: none

========================================
🆕 NEW EDIT CLEARS REDO STACK
========================================

🎨 EDIT 5: Apply grayscale filter (new change)
✓ State saved to history
⚠️  Redo stack cleared (cannot redo after new change)

Current State:
├─ Video: video123.mp4
├─ Duration: 10s - 120s (1:50)
├─ Volume: 75%
├─ Filter: grayscale
└─ Watermark: none

========================================
📚 FINAL EDIT HISTORY
========================================
Undo Stack (3 states):
  [2] trimVideo(10, 120) → adjustVolume(75) → applyFilter(sepia) → applyFilter(grayscale)
  [1] trimVideo(10, 120) → adjustVolume(75) → applyFilter(sepia)
  [0] trimVideo(10, 120) → adjustVolume(75)

Redo Stack (0 states): (empty)

Can undo? ✓ Yes
Can redo? ✗ No

========================================
⏪ UNDO TO BEGINNING
========================================

↶ Undo all changes...
✓ Restored to initial state

Current State:
├─ Video: video123.mp4
├─ Duration: 0s - 300s (5:00)
├─ Volume: 50%
├─ Filter: none
└─ Watermark: none

✓ History cleared

========================================
✅ MEMENTO PATTERN BENEFITS DEMONSTRATED
========================================

1. ✓ Encapsulation Preserved
   - EditorHistory cannot access EditorMemento internals
   - Only VideoEditor can create/restore mementos
   - Package-private access enforces this

2. ✓ Undo/Redo Functionality
   - Multiple undo operations supported
   - Multiple redo operations supported
   - Undo/redo stacks managed automatically

3. ✓ New Changes Clear Redo
   - Making new edit clears redo stack
   - Prevents confusing redo behavior
   - Matches standard editor behavior

4. ✓ Complete State Capture
   - All 6 fields saved atomically
   - Immutable mementos prevent corruption
   - No partial state restoration

5. ✓ Time Savings
   - Instant undo vs 5-minute restart
   - 45.3 hours saved annually per creator
   - 9,767% ROI
```

---

## UML Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT CODE                              │
│                     (MementoDemo.java)                          │
└────────────┬────────────────────────────────────┬───────────────┘
             │                                    │
             │ creates                            │ creates
             │                                    │
             ▼                                    ▼
┌─────────────────────────────┐      ┌─────────────────────────────┐
│      VideoEditor            │      │    EditorHistory            │
│     (Originator)            │      │     (Caretaker)             │
├─────────────────────────────┤      ├─────────────────────────────┤
│ - videoId: String           │      │ - undoStack: Stack          │
│ - startTime: int            │      │ - redoStack: Stack          │
│ - endTime: int              │      ├─────────────────────────────┤
│ - volume: int               │      │ + save(editor): void        │
│ - filter: String            │      │ + undo(): EditorMemento     │
│ - watermark: String         │      │ + redo(): EditorMemento     │
├─────────────────────────────┤      │ + canUndo(): boolean        │
│ + VideoEditor(videoId)      │      │ + canRedo(): boolean        │
│ + trimVideo(start, end)     │      │ + clear(): void             │
│ + adjustVolume(level)       │      │ + showHistory(): void       │
│ + applyFilter(filter)       │      └─────────────┬───────────────┘
│ + addWatermark(text)        │                    │
│ + save(): EditorMemento     │◄───┐               │
│ + restore(memento): void    │    │               │ stores
│ + showState(): void         │    │               │ (opaque - cannot
└─────────────────────────────┘    │               │  access internals)
                                   │               │
                     creates/      │               │
                     restores      │               │
                                   │               ▼
                      ┌────────────┴────────────────────────────────┐
                      │         EditorMemento                       │
                      │          (Memento)                          │
                      ├─────────────────────────────────────────────┤
                      │ - videoId: String (final)                   │
                      │ - startTime: int (final)                    │
                      │ - endTime: int (final)                      │
                      │ - volume: int (final)                       │
                      │ - filter: String (final)                    │
                      │ - watermark: String (final)                 │
                      ├─────────────────────────────────────────────┤
                      │ EditorMemento(...)      (package-private)   │
                      │ String getVideoId()     (package-private)   │
                      │ int getStartTime()      (package-private)   │
                      │ int getEndTime()        (package-private)   │
                      │ int getVolume()         (package-private)   │
                      │ String getFilter()      (package-private)   │
                      │ String getWatermark()   (package-private)   │
                      └─────────────────────────────────────────────┘

KEY RELATIONSHIPS:
═══════════════════════════════════════════════════════════════════
1. VideoEditor creates EditorMemento (save())
2. VideoEditor restores from EditorMemento (restore())
3. EditorHistory stores EditorMemento objects (opaque!)
4. EditorHistory CANNOT access EditorMemento internals (encapsulation!)
5. Only VideoEditor can access EditorMemento internals (same package)
```

### Sequence Diagram: Undo/Redo Flow

```
Client          VideoEditor       EditorHistory      EditorMemento
  |                  |                  |                  |
  |─────────────────►|                  |                  |
  | create("vid123") |                  |                  |
  |                  |                  |                  |
  |─────────────────────────────────►|  |                  |
  |       save(editor)                |  |                  |
  |                  |◄─────────────────|                  |
  |                  | save()           |                  |
  |                  |─────────────────────────────────────►|
  |                  |            create memento            |
  |                  |◄─────────────────────────────────────|
  |                  | return memento   |                  |
  |                  |─────────────────►|                  |
  |                  |                  | push(undoStack)  |
  |                  |                  |                  |
  |─────────────────►|                  |                  |
  | trimVideo(10,120)|                  |                  |
  |                  |                  |                  |
  |─────────────────────────────────►|  |                  |
  |       save(editor)                |  |                  |
  |                  |◄─────────────────|                  |
  |                  | save()           |                  |
  |                  |─────────────────────────────────────►|
  |                  |◄─────────────────────────────────────|
  |                  |─────────────────►|                  |
  |                  |                  | push(undoStack)  |
  |                  |                  |                  |
  |─────────────────────────────────►|  |                  |
  |            undo()                 |  |                  |
  |                  |                  | pop(undoStack)   |
  |                  |                  | push(redoStack)  |
  |                  |                  | peek(undoStack)  |
  |◄─────────────────────────────────────|                  |
  | return memento   |                  |                  |
  |                  |                  |                  |
  |─────────────────►|                  |                  |
  | restore(memento) |                  |                  |
  |                  | restore state    |                  |
  |                  |                  |                  |
  |─────────────────────────────────►|  |                  |
  |            redo()                 |  |                  |
  |                  |                  | pop(redoStack)   |
  |                  |                  | push(undoStack)  |
  |◄─────────────────────────────────────|                  |
  | return memento   |                  |                  |
  |                  |                  |                  |
  |─────────────────►|                  |                  |
  | restore(memento) |                  |                  |
  |                  | restore state    |                  |
```

---

## Conclusion

### Pattern Summary

The **Memento Pattern** provides undo/redo functionality while preserving encapsulation:

**Key Achievement: Encapsulation Preservation**
- EditorMemento uses package-private access
- EditorHistory can store mementos but cannot peek inside
- Only VideoEditor (same package) can access memento internals
- **This is the CRITICAL distinction from just exposing getters/setters**

**Core Benefits:**
1. **Time Savings**: 45.3 hours/year per creator (instant undo vs 5-minute restart)
2. **Creative Freedom**: Experiment without fear ("I can always undo")
3. **Professional Feature**: Matches industry-standard video editors
4. **Error Recovery**: Accidental clicks and mistakes easily reversed

**ROI: 9,767% (296 hours saved annually across 10 creators)**

### When to Use Memento Pattern

✅ **Use Memento when:**
- Need undo/redo functionality
- Object has complex internal state (6+ fields)
- Must preserve encapsulation (state should not be exposed)
- Need snapshot capability (save checkpoints)

❌ **Avoid Memento when:**
- Object state is simple (1-2 fields) - just save them directly
- State is already immutable - no need to restore
- Memory is severely constrained - mementos consume memory
- State changes are irreversible by design (e.g., transaction commit)

### Pattern Linkage in StreamFlix Cluster

This is **Pattern #9** in our cluster (2nd Behavioral pattern):

**Previous Patterns:**
1. Adapter (#1) - MediaPlayer compatibility
2. Observer (#2) - YouTube channel subscriptions
3. Proxy (#3) - StreamFlix access control
4. Flyweight (#4) - Video player UI icons
5. Builder (#5) - Video upload configuration
6. Factory Method (#6) - Video export formats
7. Abstract Factory (#7) - Video player themes
8. Prototype (#8) - Clone upload templates

**Current Pattern:**
9. **Memento (#9)** - Video editor undo/redo

**Context Links:**
- Users build upload configs (Builder #5)
- Users clone templates (Prototype #8)
- Users edit videos BEFORE uploading (Memento #9)
- Complete workflow: Build config → Clone template → Edit video → Upload

### Real-World Applications Beyond StreamFlix

**Text Editors:**
- Microsoft Word, Google Docs - Ctrl+Z undo
- Vim - u (undo), Ctrl+R (redo)
- Every text editor uses Memento pattern

**Graphics Software:**
- Adobe Photoshop - History panel
- GIMP - Undo stack
- Drawing apps - Step-by-step undo

**Game Development:**
- Save game states
- Turn-based games (chess - undo last move)
- Level editors

**Database Transactions:**
- Transaction rollback
- Checkpoint/restore mechanisms
- Snapshot isolation

### Implementation Checklist

When implementing Memento pattern, ensure:

- [x] Memento class uses package-private access (NO public modifier)
- [x] Memento fields are final (immutability)
- [x] Memento constructor is package-private
- [x] Memento getters are package-private
- [x] Caretaker stores mementos but CANNOT access internals
- [x] Originator has save() method returning Memento
- [x] Originator has restore(Memento) method
- [x] New changes clear redo stack
- [x] Undo/redo stacks properly managed

### Final Thoughts

The Memento pattern is **essential for any application with editing capabilities**. The key insight is **encapsulation preservation through package-private access** - this separates Memento from naive "save all fields" approaches.

For StreamFlix creators, undo/redo is not a luxury - it's a **necessity** for professional content editing. The pattern saves 45.3 hours annually per creator, enabling them to experiment freely and recover from mistakes instantly.

**Pattern #9 completed. The editing workflow is now robust and user-friendly!**

---

**Pattern Cluster Progress: 9/24 patterns implemented (37.5%)**

Next patterns to implement:
- Template Method (Behavioral)
- Strategy (Behavioral)
- Decorator (Structural)
- ... 15 more patterns
