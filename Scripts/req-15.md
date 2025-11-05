# REQ-15: Memento Pattern Implementation

## Pattern Information
- **Pattern Name**: Memento Pattern
- **Category**: Behavioral Pattern
- **Difficulty**: ⭐⭐⭐ (Medium)
- **Folder**: `15-Memento-DP/`

---

## Context Linking Strategy (Memory Optimization)

### Current Video Platform Cluster (8 patterns):
1. ✅ **Adapter** - MediaPlayer (mp3, mp4, vlc compatibility)
2. ✅ **Observer** - YouTube Channel (notification system)
3. ✅ **Proxy** - StreamFlix (lazy video loading)
4. ✅ **Flyweight** - Video Player UI Icons (9,000x memory savings)
5. ✅ **Builder** - Video Upload Configuration (12 params)
6. ✅ **Factory Method** - Video Export System (4 formats)
7. ✅ **Abstract Factory** - Video Player Theme (consistent UI families)
8. ✅ **Prototype** - Video Configuration Templates (clone settings)

### Adding Pattern #9:
9. **Memento** → **Video Editor History (Undo/Redo)** ⏮️

**Why this context?**
- Natural extension: After uploading (Builder) and configuring (Prototype), users edit videos
- Links to: Video Upload workflow, Video Platform features
- Real-world scenario: YouTube Studio, Adobe Premiere, DaVinci Resolve have undo/redo
- Problem: Users make editing mistakes and want to undo
- Solution: Save editing states, restore previous versions
- Easy to remember: "Undo/Redo in video editor"

---

## Recommended Context: Video Editor History (Undo/Redo System)

### Scenario:
StreamFlix has a built-in video editor where users can:
- **Trim** videos (start/end time)
- **Adjust volume** (0-100)
- **Apply filters** ("Sepia", "Grayscale", "Vintage")
- **Add watermark** (text overlay)

Users frequently make mistakes and need **undo/redo** functionality. The editor needs to:
- Save editing states after each change
- Undo to previous state (Ctrl+Z)
- Redo to next state (Ctrl+Y)
- View editing history

### Current Problem (WITHOUT Memento):

```java
// ❌ BAD: Cannot undo changes - state is lost forever!
public class VideoEditor {
    private String videoId;
    private int startTime;
    private int endTime;
    private int volume;
    private String filter;
    private String watermark;

    public void trimVideo(int start, int end) {
        // Old state is LOST forever! 😱
        this.startTime = start;
        this.endTime = end;
        System.out.println("Trimmed to " + start + "-" + end);
    }

    public void adjustVolume(int volume) {
        // Cannot remember previous volume! 😱
        this.volume = volume;
        System.out.println("Volume set to " + volume);
    }

    public void applyFilter(String filter) {
        // Previous filter is gone! 😱
        this.filter = filter;
        System.out.println("Applied filter: " + filter);
    }

    // NO UNDO METHOD! 😱
    // User makes mistake → Must start over!
}
```

### Problems with this approach:

1. **No Undo Functionality**
   - User trims video incorrectly → Cannot undo
   - Must manually reset all values
   - Frustrating user experience

2. **State History Lost**
```java
// User's editing session:
editor.trimVideo(0, 60);     // State 1: Trimmed
editor.adjustVolume(80);     // State 2: Volume adjusted
editor.applyFilter("Sepia"); // State 3: Filter applied

// User realizes volume is too loud, wants to undo
// ❌ NO WAY TO GO BACK! 😱
// Must manually remember previous volume was 100
editor.adjustVolume(100);  // But what if user forgot?
```

3. **Encapsulation Violation**
```java
// Attempting to save state manually - VIOLATES ENCAPSULATION!
public class VideoEditor {
    // Expose all internal state
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }
    public int getVolume() { return volume; }
    public String getFilter() { return filter; }
    public String getWatermark() { return watermark; }

    // Expose setters for restoring
    public void setStartTime(int time) { this.startTime = time; }
    public void setEndTime(int time) { this.endTime = time; }
    // ... 10 more setters exposing internal state! 😱
}

// Client must know all internal details
class EditorManager {
    // Save state manually
    int savedStart = editor.getStartTime();
    int savedEnd = editor.getEndTime();
    int savedVolume = editor.getVolume();
    String savedFilter = editor.getFilter();
    String savedWatermark = editor.getWatermark();

    // Restore state manually - TEDIOUS!
    editor.setStartTime(savedStart);
    editor.setEndTime(savedEnd);
    editor.setVolume(savedVolume);
    editor.setFilter(savedFilter);
    editor.setWatermark(savedWatermark);
}
```

4. **No History Navigation**
```java
// Want multi-level undo?
// Must implement complex state management manually
Stack<EditorState> history = new Stack<>();

// Save state before each operation
EditorState state1 = new EditorState(
    editor.getStartTime(),
    editor.getEndTime(),
    editor.getVolume(),
    editor.getFilter(),
    editor.getWatermark()
);
history.push(state1);

// Undo = pop and restore
EditorState previous = history.pop();
editor.setStartTime(previous.startTime);
editor.setEndTime(previous.endTime);
// ... restore 5 more fields manually! 😱

// What about redo?
// Need ANOTHER stack! 😱😱😱
```

5. **Complex History Management**
```java
// Want to show editing history?
// Must track every change manually

// Want to skip to specific state?
// Must implement complex navigation logic

// Want to limit history size?
// Must implement cleanup logic

// 100+ lines of boilerplate code! 😱
```

### Real-World Impact:

**User story**: Content creator edits 10-minute video

**Without Memento (No Undo):**
- User makes mistake at 8 minutes of editing
- Must restart entire editing session
- Time wasted: 10+ minutes
- User frustration: HIGH 😡
- Abandons StreamFlix editor → Uses competitor

**With Memento (Undo/Redo):**
- User makes mistake → Presses Ctrl+Z
- Instant undo to previous state
- Time wasted: 1 second
- User satisfaction: HIGH 😊
- Continues using StreamFlix editor

---

## Solution: Memento Pattern

### Key Idea:
- **Capture object's internal state** without exposing it
- **Store state externally** (in Memento object)
- **Restore state** when needed (undo)
- **Maintain history** (Caretaker manages mementos)
- **Preserve encapsulation** (only Originator can access Memento internals)

### Structure:

```
Originator (VideoEditor)
    - Creates memento with current state
    - Restores state from memento

Memento (EditorMemento)
    - Stores Originator's internal state
    - Only Originator can access state

Caretaker (EditorHistory)
    - Manages memento history
    - Provides undo/redo functionality
    - Never examines memento contents

Client
    - Uses Originator and Caretaker
    - Requests undo/redo
```

### Benefits:

1. **Undo/Redo Functionality** ✅
   - Save state before each change
   - Undo = restore previous state
   - Redo = restore next state

2. **Encapsulation Preserved** ✅
   - Memento is opaque to Caretaker
   - Only Originator knows state structure
   - No getter/setter exposure

3. **History Navigation** ✅
   - Multi-level undo (unlimited)
   - History list (show all states)
   - Jump to specific state

4. **Simple Client Code** ✅
   - Just call undo() or redo()
   - No manual state management
   - Clean abstraction

5. **Flexible** ✅
   - Add new state fields easily
   - Change state structure without affecting client
   - Can compress old mementos for memory

### Code with Memento:

```java
// ✅ GOOD: Undo/Redo with Memento pattern

// Originator
public class VideoEditor {
    private String videoId;
    private int startTime;
    private int endTime;
    private int volume;
    private String filter;

    // Create memento (capture state)
    public EditorMemento save() {
        return new EditorMemento(videoId, startTime, endTime, volume, filter);
    }

    // Restore from memento
    public void restore(EditorMemento memento) {
        this.videoId = memento.getVideoId();
        this.startTime = memento.getStartTime();
        // ... restore all fields
    }

    // Edit operations
    public void trimVideo(int start, int end) {
        this.startTime = start;
        this.endTime = end;
    }
}

// Memento (immutable state snapshot)
public class EditorMemento {
    private final String videoId;
    private final int startTime;
    private final int endTime;
    private final int volume;
    private final String filter;

    // Package-private constructor (only VideoEditor can create)
    EditorMemento(...) {
        this.videoId = videoId;
        // ... initialize all fields
    }

    // Package-private getters (only VideoEditor can access)
    String getVideoId() { return videoId; }
    // ... other getters
}

// Caretaker (manages history)
public class EditorHistory {
    private Stack<EditorMemento> undoStack = new Stack<>();
    private Stack<EditorMemento> redoStack = new Stack<>();

    public void save(EditorMemento memento) {
        undoStack.push(memento);
        redoStack.clear();  // Clear redo after new change
    }

    public EditorMemento undo() {
        if (undoStack.isEmpty()) return null;
        EditorMemento memento = undoStack.pop();
        redoStack.push(memento);
        return undoStack.isEmpty() ? null : undoStack.peek();
    }

    public EditorMemento redo() {
        if (redoStack.isEmpty()) return null;
        EditorMemento memento = redoStack.pop();
        undoStack.push(memento);
        return memento;
    }
}

// Usage - SIMPLE!
EditorHistory history = new EditorHistory();

// Save before each operation
history.save(editor.save());
editor.trimVideo(0, 60);

history.save(editor.save());
editor.adjustVolume(80);

// Undo - EASY!
editor.restore(history.undo());  // Back to previous state!

// Redo - EASY!
editor.restore(history.redo());  // Forward to next state!
```

---

## Implementation Requirements

### Files to Create (5 files):

1. **VideoEditor.java** (Originator)
   - Private state: videoId, startTime, endTime, volume, filter, watermark
   - Edit operations: trimVideo(), adjustVolume(), applyFilter(), addWatermark()
   - `save()` method returns EditorMemento
   - `restore(EditorMemento)` method restores state
   - `displayState()` shows current state

2. **EditorMemento.java** (Memento)
   - Immutable state snapshot (final fields)
   - Package-private constructor (only VideoEditor can create)
   - Package-private getters (only VideoEditor can access)
   - Timestamp for history display
   - State description for history list

3. **EditorHistory.java** (Caretaker)
   - Undo stack: Stack<EditorMemento>
   - Redo stack: Stack<EditorMemento>
   - `save(EditorMemento)` adds to undo stack
   - `undo()` returns previous memento
   - `redo()` returns next memento
   - `getHistory()` returns list of all states
   - `canUndo()`, `canRedo()` query methods

4. **MementoDemo.java** (Client)
   - Demonstrates editing workflow with undo/redo
   - Shows state changes
   - Shows history navigation
   - Compares with/without Memento pattern

5. **package.bluej** (UML Diagram)
   - Shows Originator, Memento, Caretaker relationships
   - Shows method calls

---

## Expected Output

```
╔════════════════════════════════════════════════════════════╗
║            MEMENTO PATTERN DEMO                            ║
║         Video Editor History (Undo/Redo)                   ║
║  (Linked: StreamFlix Video Platform patterns)             ║
╚════════════════════════════════════════════════════════════╝


═══════════════════════════════════════════════════════════
PROBLEM: Without Memento Pattern
═══════════════════════════════════════════════════════════

❌ Problems:
   1. No undo functionality (state lost forever)
   2. Must restart editing after mistakes
   3. Violates encapsulation (expose all getters/setters)
   4. Complex manual state management
   5. No history navigation

Example: User edits video
   1. Trim to 0-60s
   2. Set volume to 80
   3. Apply Sepia filter
   4. Realizes mistake → CANNOT UNDO! 😱
   5. Must manually reset everything

   Result: 10 minutes wasted, user frustrated


═══════════════════════════════════════════════════════════
SOLUTION: Memento Pattern
═══════════════════════════════════════════════════════════

✅ Benefits:
   1. Undo/Redo functionality (instant rollback)
   2. Multi-level history (unlimited undo)
   3. Encapsulation preserved (memento is opaque)
   4. Simple client code (just call undo/redo)
   5. History navigation (view and jump to states)


═══════════════════════════════════════════════════════════
TEST 1: Editing Session with Save Points
═══════════════════════════════════════════════════════════

🎬 Starting video editing session...

Initial State:
   Video ID: video-123
   Trim: 0s - 120s
   Volume: 100
   Filter: None
   Watermark: None

✓ State saved to history (State #1)

--- Edit 1: Trim Video ---
   Trimming to 10s - 90s...

Current State:
   Video ID: video-123
   Trim: 10s - 90s ← Changed
   Volume: 100
   Filter: None
   Watermark: None

✓ State saved to history (State #2)

--- Edit 2: Adjust Volume ---
   Setting volume to 80...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80 ← Changed
   Filter: None
   Watermark: None

✓ State saved to history (State #3)

--- Edit 3: Apply Filter ---
   Applying Sepia filter...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80
   Filter: Sepia ← Changed
   Watermark: None

✓ State saved to history (State #4)

--- Edit 4: Add Watermark ---
   Adding watermark "StreamFlix"...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80
   Filter: Sepia
   Watermark: StreamFlix ← Changed

✓ State saved to history (State #5)


═══════════════════════════════════════════════════════════
TEST 2: Undo Functionality
═══════════════════════════════════════════════════════════

🔄 User realizes watermark is wrong...

--- Undo #1 (Ctrl+Z) ---
⏮️  Restoring previous state...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80
   Filter: Sepia
   Watermark: None ← Removed!

✓ Undo successful! Back to State #4

--- Undo #2 (Ctrl+Z) ---
⏮️  Restoring previous state...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80
   Filter: None ← Removed!
   Watermark: None

✓ Undo successful! Back to State #3

--- Undo #3 (Ctrl+Z) ---
⏮️  Restoring previous state...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 100 ← Restored!
   Filter: None
   Watermark: None

✓ Undo successful! Back to State #2


═══════════════════════════════════════════════════════════
TEST 3: Redo Functionality
═══════════════════════════════════════════════════════════

🔄 User wants volume change back...

--- Redo #1 (Ctrl+Y) ---
⏩ Moving forward to next state...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80 ← Restored!
   Filter: None
   Watermark: None

✓ Redo successful! Forward to State #3

--- Redo #2 (Ctrl+Y) ---
⏩ Moving forward to next state...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 80
   Filter: Sepia ← Restored!
   Watermark: None

✓ Redo successful! Forward to State #4


═══════════════════════════════════════════════════════════
TEST 4: History Navigation
═══════════════════════════════════════════════════════════

📜 Viewing editing history...

Editing History:
  1. [2025-11-05 09:00:00] Initial state (trim: 0-120, vol: 100)
  2. [2025-11-05 09:01:30] Trimmed to 10-90s
  3. [2025-11-05 09:02:15] Volume adjusted to 80
→ 4. [2025-11-05 09:03:00] Filter applied: Sepia (CURRENT)
  5. [2025-11-05 09:04:20] Watermark added: StreamFlix

Can undo: Yes (3 states back)
Can redo: Yes (1 state forward)

🎯 Jumping to state #2...

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 100
   Filter: None
   Watermark: None

✓ Jumped to State #2


═══════════════════════════════════════════════════════════
TEST 5: New Changes Clear Redo Stack
═══════════════════════════════════════════════════════════

Current position: State #2
Redo stack: 3 states available

--- Making new edit: Apply Vintage filter ---

Current State:
   Video ID: video-123
   Trim: 10s - 90s
   Volume: 100
   Filter: Vintage ← New change
   Watermark: None

⚠️  Redo stack cleared! (New timeline created)

New History:
  1. Initial state
  2. Trimmed to 10-90s (CURRENT)
  3. Filter applied: Vintage

Can undo: Yes
Can redo: No (timeline branched)


═══════════════════════════════════════════════════════════
TEST 6: Encapsulation Demonstration
═══════════════════════════════════════════════════════════

🔒 Memento preserves encapsulation...

EditorMemento contents:
   - State is OPAQUE to Caretaker ✓
   - Only VideoEditor can read/write ✓
   - Caretaker just stores and retrieves ✓

Client code:
   ❌ Cannot do: memento.getVolume()  // Private!
   ❌ Cannot do: memento.setVolume()  // Immutable!
   ✅ Can do: editor.restore(memento)  // Only Originator

✓ Encapsulation preserved!


═══════════════════════════════════════════════════════════
BENEFITS SUMMARY
═══════════════════════════════════════════════════════════

✅ Advantages:
   1. UNDO/REDO: Instant rollback to any state
   2. ENCAPSULATION: Memento is opaque (no exposure)
   3. MULTI-LEVEL: Unlimited undo history
   4. HISTORY: View and navigate all states
   5. SIMPLE: Clean client code (no manual state management)
   6. FLEXIBLE: Easy to add new state fields
   7. SAFE: Immutable mementos (thread-safe snapshots)

⚠️  Trade-offs:
   1. Memory usage (each memento stores full state)
   2. Performance (cloning state for each save)
   3. May need compression for large histories

📊 When to Use:
   ✅ Use Memento when:
      - Need undo/redo functionality
      - Want to save/restore object state
      - Must preserve encapsulation
      - Need state history/snapshots

   ❌ Don't use when:
      - State is simple (just copy manually)
      - Don't need undo functionality
      - Memory is severely constrained

📈 ROI Calculation:
   - Initial effort: 3 hours to implement Memento
   - User time saved: 10 minutes per editing error
   - Editing errors per user: 5/week
   - Users: 1000 active editors
   - Total saved: 50 min/week × 1000 = 833 hours/week
   - ROI: MASSIVE user satisfaction improvement!


╔════════════════════════════════════════════════════════════╗
║                 CONTEXT LINKING                            ║
╚════════════════════════════════════════════════════════════╝

🎬 Video Platform Design Pattern Cluster (9 patterns):
   1. ADAPTER: MediaPlayer (mp3, mp4, vlc compatibility)
   2. OBSERVER: YouTube Channel (notification system)
   3. PROXY: StreamFlix (lazy video loading)
   4. FLYWEIGHT: Video Player UI Icons (share 9,000x memory)
   5. BUILDER: Video Upload (12 params configuration)
   6. FACTORY METHOD: Video Export (4 formats)
   7. ABSTRACT FACTORY: Video Player Theme (UI families)
   8. PROTOTYPE: Video Config Templates (clone settings)
   9. MEMENTO: Video Editor History (undo/redo)

💡 Complete Video Platform Workflow:
   📥 1. Upload video (BUILDER)
   📋 2. Clone template (PROTOTYPE for quick config)
   ✂️  3. Edit video (MEMENTO for undo/redo) ← NEW!
   💾 4. Store video (PROXY for lazy loading)
   ▶️  5. Play video (ADAPTER for formats)
   🎨 6. Show UI (FLYWEIGHT for memory)
   🔔 7. Notify subscribers (OBSERVER)
   📤 8. Export video (FACTORY METHOD)
   🎨 9. Apply theme (ABSTRACT FACTORY)

🔗 Pattern Relationships:
   PROTOTYPE + MEMENTO:
   - PROTOTYPE: Clone entire object (configuration)
   - MEMENTO: Snapshot state for undo (editing)
   - Both preserve object state, different purposes

🧠 Memorization Strategy:
   All 9 patterns in ONE domain = Super easy!
   Think: "Video Platform = 9 patterns" → Instant recall! ⚡

   Behavioral patterns in cluster:
   - OBSERVER: "Notify subscribers of new videos"
   - MEMENTO: "Undo/redo video editing changes"

════════════════════════════════════════════════════════════
```

---

## UML Class Diagram

```
┌──────────────────────────────────┐
│       VideoEditor                │ ← Originator
├──────────────────────────────────┤
│ - videoId: String                │
│ - startTime: int                 │
│ - endTime: int                   │
│ - volume: int                    │
│ - filter: String                 │
│ - watermark: String              │
├──────────────────────────────────┤
│ + trimVideo(start, end): void    │
│ + adjustVolume(vol): void        │
│ + applyFilter(filter): void      │
│ + addWatermark(text): void       │
│ + save(): EditorMemento          │ ← Create memento
│ + restore(memento): void         │ ← Restore state
│ + displayState(): void           │
└──────────────────────────────────┘
        │                     △
        │ creates             │ restores from
        ↓                     │
┌──────────────────────────────────┐
│      EditorMemento               │ ← Memento
├──────────────────────────────────┤
│ - videoId: String (final)        │
│ - startTime: int (final)         │
│ - endTime: int (final)           │
│ - volume: int (final)            │
│ - filter: String (final)         │
│ - watermark: String (final)      │
│ - timestamp: long (final)        │
├──────────────────────────────────┤
│ ~ EditorMemento(...)             │ ← Package-private
│ ~ getVideoId(): String           │ ← Package-private
│ ~ getStartTime(): int            │ ← Package-private
│ + getTimestamp(): long           │
│ + getDescription(): String       │
└──────────────────────────────────┘
        △
        │ manages
        │
┌──────────────────────────────────┐
│      EditorHistory               │ ← Caretaker
├──────────────────────────────────┤
│ - undoStack: Stack<Memento>      │
│ - redoStack: Stack<Memento>      │
│ - maxHistorySize: int            │
├──────────────────────────────────┤
│ + save(memento): void            │
│ + undo(): EditorMemento          │
│ + redo(): EditorMemento          │
│ + canUndo(): boolean             │
│ + canRedo(): boolean             │
│ + getHistory(): List<Memento>    │
│ + clear(): void                  │
└──────────────────────────────────┘
        △
        │ uses
        │
┌──────────────────────────────────┐
│      MementoDemo                 │ ← Client
├──────────────────────────────────┤
│ + main(args): void               │
└──────────────────────────────────┘
```

---

## Key Learning Points

### 1. When to Use Memento

✅ **Use Memento when:**
- Need undo/redo functionality
- Want to implement save/restore snapshots
- Must preserve encapsulation (no getters/setters)
- Need state history or rollback capability
- Want to implement checkpointing

❌ **Don't use Memento when:**
- State is simple (1-2 fields, just copy manually)
- Don't need undo functionality
- Memory is severely constrained
- State changes are rare

### 2. Memento vs Prototype

| Aspect | Memento | Prototype |
|--------|---------|-----------|
| **Purpose** | Save/restore state | Clone object |
| **Use case** | Undo/redo | Quick copying |
| **Visibility** | State hidden (opaque) | Object visible |
| **Intent** | Rollback changes | Create similar objects |
| **Example** | Editor history | Config templates |

### 3. Encapsulation is Critical

**Key principle**: Memento should be opaque to everyone except Originator

```java
// ✅ GOOD: Package-private access
class EditorMemento {
    private final int volume;

    EditorMemento(int volume) {  // Package-private constructor
        this.volume = volume;
    }

    int getVolume() {  // Package-private getter
        return volume;
    }
}

// ❌ BAD: Public access breaks encapsulation
class EditorMemento {
    private final int volume;

    public EditorMemento(int volume) {  // Public!
        this.volume = volume;
    }

    public int getVolume() {  // Public! Anyone can read!
        return volume;
    }
}
```

### 4. Real-World Examples

1. **Text Editors**:
   - Microsoft Word: Undo/Redo
   - Google Docs: Revision history
   - VS Code: Undo changes

2. **Image Editors**:
   - Photoshop: History panel
   - GIMP: Undo/Redo
   - Canva: Version history

3. **Video Editors**:
   - Adobe Premiere: Editing history
   - DaVinci Resolve: Undo/Redo
   - Final Cut Pro: Timeline snapshots

4. **Games**:
   - Save game state
   - Checkpoint system
   - Replay system

5. **Databases**:
   - Transaction rollback
   - Snapshot isolation
   - Point-in-time recovery

---

## Testing Checklist

- [ ] VideoEditor creates memento with save()
- [ ] VideoEditor restores state with restore()
- [ ] EditorMemento is immutable (final fields)
- [ ] EditorMemento has package-private access
- [ ] EditorHistory manages undo/redo stacks
- [ ] Undo moves backward through history
- [ ] Redo moves forward through history
- [ ] New changes clear redo stack
- [ ] History shows all saved states
- [ ] Demo shows editing workflow
- [ ] Demo shows undo/redo operations
- [ ] Demo shows encapsulation preservation

---

## Success Criteria

✅ Implementation is complete when:
1. All 5 Java files created (1 originator + 1 memento + 1 caretaker + 1 demo + 1 bluej)
2. Undo/redo works correctly (multi-level)
3. Memento preserves encapsulation (package-private)
4. History navigation works (view and jump to states)
5. New changes clear redo stack
6. Demo output is comprehensive
7. Pattern effectively links to Video Platform cluster (9 patterns total)

---

## Context Link to Existing Patterns

```
StreamFlix Video Platform - Complete Workflow
│
├─ 1. Upload video (BUILDER with 12 parameters)
├─ 2. Save as template (PROTOTYPE for reuse)
├─ 3. Clone for new upload (PROTOTYPE quick copy)
├─ 4. Edit video (MEMENTO for undo/redo) ← NEW!
├─ 5. Store video (PROXY lazy loading)
├─ 6. Play video (ADAPTER format compatibility)
├─ 7. Show UI icons (FLYWEIGHT memory optimization)
├─ 8. Notify subscribers (OBSERVER notifications)
├─ 9. Export video (FACTORY METHOD formats)
└─ 10. Apply theme (ABSTRACT FACTORY UI families)
```

**Pattern Categories in Cluster:**

**Creational (4 patterns):**
- BUILDER: Create complex upload config
- PROTOTYPE: Clone upload templates
- FACTORY METHOD: Create export formats
- ABSTRACT FACTORY: Create UI theme families

**Structural (3 patterns):**
- ADAPTER: Adapt media players
- PROXY: Lazy load videos
- FLYWEIGHT: Share UI icons

**Behavioral (2 patterns):**
- OBSERVER: Notify subscribers
- MEMENTO: Undo/redo editing ← NEW!

---

**End of req-15.md**
