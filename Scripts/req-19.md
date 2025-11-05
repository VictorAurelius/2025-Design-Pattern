# Requirements Document: Command Pattern (Pattern #19)

## Pattern Information
- **Pattern Name**: Command Pattern
- **Category**: Behavioral Design Pattern
- **Complexity**: ⭐⭐⭐ (Medium)
- **Popularity**: ⭐⭐⭐⭐ (Very High - used in GUI frameworks, editors, transaction systems)

## Business Context: StreamFlix Video Platform

### Domain
**Video Editor with Undo/Redo Functionality**

Content creators use StreamFlix's built-in video editor to enhance their videos before publishing. The editor provides various operations (trim, add filters, adjust brightness, add text, etc.) and must support undo/redo functionality to allow creators to experiment freely.

### Current Problem (Without Command Pattern)

**Tightly Coupled Implementation:**
```java
public class VideoEditor {
    private VideoClip video;

    public void addFilter(String filterType) {
        if (filterType.equals("grayscale")) {
            video.applyGrayscaleFilter();
            // How to undo this? Need to store original state!
        } else if (filterType.equals("sepia")) {
            video.applySepiaFilter();
            // How to undo this? Need different logic for each operation!
        }
        // Cannot undo, cannot redo, cannot log operations
    }

    public void adjustBrightness(int value) {
        video.setBrightness(video.getBrightness() + value);
        // How to undo? Need to remember old value!
    }

    // Undo is impossible without major refactoring
    // Each method would need custom undo logic
}
```

**Issues:**
1. ❌ **No Undo/Redo**: Cannot reverse operations
2. ❌ **No Operation History**: Cannot track what was done
3. ❌ **Tight Coupling**: VideoEditor directly calls VideoClip methods
4. ❌ **No Macro Support**: Cannot group operations (e.g., "apply vintage effect" = grayscale + sepia + vignette)
5. ❌ **No Queuing**: Cannot schedule operations for later
6. ❌ **No Logging**: Cannot audit what operations were performed
7. ❌ **Hard to Test**: Cannot test operations in isolation
8. ❌ **Violates Open/Closed**: Adding new operation requires modifying VideoEditor

### Real-World Problem Scenario

**Content Creator Journey:**
```
Creator: "Let me add a grayscale filter..."
[Applies grayscale]

Creator: "Hmm, actually I want to adjust brightness first..."
[No undo button - have to reload original video, losing all work]

Creator: "I want to apply my 'vintage' preset to 50 videos..."
[Have to manually repeat same 5 operations on each video - tedious!]

Creator: "What operations did I perform? I need to replicate this..."
[No operation history - have to remember and manually repeat]
```

**Business Impact:**
- ⏱️ Lost Productivity: Creators waste time re-doing work
- 😞 Poor User Experience: No undo = frustration + fear of experimenting
- 🐛 More Errors: Manual repetition causes inconsistencies
- 📉 Feature Requests Ignored: Cannot add macro/batch operations easily

## Requirements

### Functional Requirements

#### FR-1: Video Editing Operations
The system must support the following video editing operations:
- **Add Filter**: Apply visual filters (Grayscale, Sepia, Blur, Sharpen)
- **Adjust Brightness**: Increase/decrease brightness (-100 to +100)
- **Adjust Contrast**: Increase/decrease contrast (-100 to +100)
- **Add Text Overlay**: Add text at specified position
- **Trim Video**: Cut video to specified start/end times
- **Adjust Volume**: Increase/decrease audio volume (0-200%)

Each operation must be encapsulated as a separate command object.

#### FR-2: Undo/Redo Functionality
The system must support:
- **Undo**: Reverse the last operation
- **Redo**: Re-apply an operation that was undone
- **Undo Stack**: Maintain history of all executed commands
- **Redo Stack**: Maintain history of all undone commands

#### FR-3: Macro Commands
The system must support macro commands (composite commands):
- **Group Operations**: Combine multiple commands into one
- **Example Presets**:
  - "Vintage Effect" = Grayscale + Sepia + Lower Brightness
  - "Professional Look" = Sharpen + Adjust Contrast + Add Logo
- **Atomic Execution**: All operations in macro execute together
- **Atomic Undo**: Undo entire macro in one step

#### FR-4: Operation History
The system must:
- **Log Operations**: Track all executed commands
- **Display History**: Show user what operations were performed
- **Replay Operations**: Re-execute same sequence on different video
- **Export History**: Save operation sequence as preset

### Non-Functional Requirements

#### NFR-1: Performance
- Command execution: < 100ms (excluding actual video processing)
- Undo/Redo: < 50ms
- History size: Support 50+ operations without memory issues

#### NFR-2: Maintainability
- Adding new command: Create one new class (no modification of existing code)
- Zero if-else for command selection
- Each command class < 50 lines

#### NFR-3: Testability
- Each command independently testable
- Mock receiver (VideoClip) for unit tests
- Test undo functionality in isolation

#### NFR-4: Extensibility
- Support parameterized commands (e.g., brightness value, filter type)
- Support asynchronous execution (for long operations)
- Support command queuing/scheduling

## Command Pattern Structure

### Components

#### 1. Command Interface
```java
public interface Command {
    void execute();        // Perform the operation
    void undo();          // Reverse the operation
    String getDescription(); // For history display
}
```

#### 2. Concrete Commands (Examples)
- **AddFilterCommand**: Apply filter to video
- **AdjustBrightnessCommand**: Change brightness
- **AdjustContrastCommand**: Change contrast
- **AddTextCommand**: Add text overlay
- **TrimVideoCommand**: Cut video segment
- **AdjustVolumeCommand**: Change audio volume

Each command stores:
- Reference to receiver (VideoClip)
- Parameters needed for operation
- Previous state (for undo)

#### 3. Receiver (VideoClip)
```java
public class VideoClip {
    // Actual video editing operations
    public void applyFilter(String filterType) { }
    public void removeFilter() { }
    public void setBrightness(int value) { }
    public int getBrightness() { }
    // ... other operations
}
```

#### 4. Invoker (VideoEditor)
```java
public class VideoEditor {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack after new operation
    }

    public void undo() {
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo() {
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
}
```

#### 5. Macro Command (Optional)
```java
public class MacroCommand implements Command {
    private List<Command> commands;

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    public void undo() {
        // Undo in reverse order
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
```

### Class Diagram Structure
```
┌─────────────────┐
│    Command      │ (Interface)
│  <<interface>>  │
├─────────────────┤
│ + execute()     │
│ + undo()        │
│ + getDescription()│
└────────┬────────┘
         │
         │ implements
         │
    ┌────┴─────────────────────────────────────┐
    │                                           │
┌───▼──────────────────┐              ┌────────▼─────────┐
│ AddFilterCommand     │              │ MacroCommand     │
├──────────────────────┤              ├──────────────────┤
│ - receiver: VideoClip│              │ - commands: List │
│ - filterType: String │              ├──────────────────┤
│ - prevState: State   │              │ + execute()      │
├──────────────────────┤              │ + undo()         │
│ + execute()          │              └──────────────────┘
│ + undo()             │
└──────┬───────────────┘
       │
       │ uses
       ▼
┌─────────────────────┐
│    VideoClip        │ (Receiver)
├─────────────────────┤
│ - brightness: int   │
│ - filter: String    │
├─────────────────────┤
│ + applyFilter()     │
│ + setBrightness()   │
│ + ...               │
└─────────────────────┘

┌─────────────────────┐
│   VideoEditor       │ (Invoker)
├─────────────────────┤
│ - undoStack: Stack  │
│ - redoStack: Stack  │
├─────────────────────┤
│ + executeCommand()  │
│ + undo()            │
│ + redo()            │
│ + showHistory()     │
└─────────────────────┘
```

## Use Cases

### Use Case 1: Basic Video Editing with Undo/Redo
```
Content creator edits video:
1. Add grayscale filter
2. Adjust brightness +20
3. Undo brightness (back to original)
4. Redo brightness (apply +20 again)
5. Add text overlay "Subscribe!"
6. Undo text overlay
```

### Use Case 2: Macro Command (Vintage Preset)
```
Creator wants "vintage look":
1. Select "Vintage Effect" preset
2. System executes macro:
   - Apply sepia filter
   - Lower brightness -15
   - Lower contrast -10
3. Creator can undo entire preset in one step
```

### Use Case 3: Batch Processing
```
Creator has 10 videos to process:
1. Edit first video (5 operations)
2. Export operation sequence as preset
3. Apply preset to remaining 9 videos automatically
```

### Use Case 4: Operation History & Replay
```
Creator reviews editing history:
1. View all operations performed
2. Identify key operations that worked well
3. Save as new preset for future videos
```

## Expected Output (Demo Scenarios)

### Scenario 1: Basic Operations
```
═══════════════════════════════════════════════════════════
VIDEO EDITOR - Basic Operations Demo
═══════════════════════════════════════════════════════════
Video: "summer-vlog-2024.mp4" (Duration: 5:30, Size: 500 MB)

[Operation 1] Add Grayscale Filter
✓ Grayscale filter applied

[Operation 2] Adjust Brightness (+20)
✓ Brightness: 100 → 120

[Operation 3] Adjust Contrast (-10)
✓ Contrast: 100 → 90

Current State:
  Filter: Grayscale
  Brightness: 120
  Contrast: 90

[UNDO] Undoing: Adjust Contrast (-10)
✓ Contrast: 90 → 100

[UNDO] Undoing: Adjust Brightness (+20)
✓ Brightness: 120 → 100

[REDO] Redoing: Adjust Brightness (+20)
✓ Brightness: 100 → 120

Operation History:
  1. Add Grayscale Filter
  2. Adjust Brightness (+20) [CURRENT]
```

### Scenario 2: Macro Command
```
═══════════════════════════════════════════════════════════
VIDEO EDITOR - Macro Command (Vintage Effect Preset)
═══════════════════════════════════════════════════════════

[Macro Command] Applying Vintage Effect...
  → Add Sepia Filter
  → Adjust Brightness (-15)
  → Adjust Contrast (-10)
  → Add Vignette Effect
✓ Vintage Effect applied (4 operations)

[UNDO] Undoing: Vintage Effect
  → Undo Vignette Effect
  → Undo Contrast Adjustment
  → Undo Brightness Adjustment
  → Undo Sepia Filter
✓ Vintage Effect removed (4 operations reversed)
```

### Scenario 3: Complete Editing Session
```
═══════════════════════════════════════════════════════════
VIDEO EDITOR - Complete Editing Session
═══════════════════════════════════════════════════════════
Video: "product-review.mp4"

[1] Trim Video (0:05 - 4:50)
[2] Add Sharpen Filter
[3] Adjust Brightness (+10)
[4] Adjust Contrast (+15)
[5] Add Text: "Subscribe!" at position (100, 50)
[6] Adjust Volume (120%)

Operation History (6 operations):
  1. Trim Video (0:05 - 4:50)
  2. Add Sharpen Filter
  3. Adjust Brightness (+10)
  4. Adjust Contrast (+15)
  5. Add Text: "Subscribe!" at position (100, 50)
  6. Adjust Volume (120%)

Undo Stack Size: 6
Redo Stack Size: 0

Video State:
  Duration: 4:45 (trimmed from 5:00)
  Filter: Sharpen
  Brightness: 110
  Contrast: 115
  Text Overlays: 1
  Volume: 120%
```

## Design Considerations

### 1. Undo Implementation
Each command must store enough information to reverse itself:
- **Store Previous State**: Save old value before changing
- **Store Parameters**: Remember what operation was performed
- **Two-Way Operations**: Both execute() and undo() must be implemented

### 2. Command vs Strategy Pattern
**Command Pattern (This requirement):**
- Encapsulates REQUEST as object
- Supports undo/redo
- Can queue/log/schedule operations
- Example: `editor.execute(new AddFilterCommand())`

**Strategy Pattern (Pattern #18):**
- Encapsulates ALGORITHM as object
- No undo support
- Swaps behavior at runtime
- Example: `compressor.setStrategy(new H265Strategy())`

### 3. Memory Management
- Limit undo stack size (e.g., 50 operations)
- Clear redo stack when new command executed
- Implement command pooling for frequently used commands

### 4. Asynchronous Commands
For long-running operations (e.g., applying complex filters):
- Execute command in background thread
- Show progress indicator
- Support cancellation

## Benefits

1. ✅ **Undo/Redo Support**: Built-in reversibility
2. ✅ **Decoupling**: Invoker doesn't know receiver implementation
3. ✅ **Macro Commands**: Group operations into presets
4. ✅ **Logging/Auditing**: Track all operations performed
5. ✅ **Queuing**: Schedule operations for later execution
6. ✅ **Testability**: Test commands independently
7. ✅ **Open/Closed Principle**: Add new commands without modifying existing code
8. ✅ **Single Responsibility**: Each command does one thing

## Implementation Checklist

### Phase 1: Core Structure
- [ ] Create `Command` interface with execute(), undo(), getDescription()
- [ ] Create `VideoClip` receiver class with video properties
- [ ] Create `VideoEditor` invoker with undo/redo stacks

### Phase 2: Basic Commands
- [ ] `AddFilterCommand` (grayscale, sepia, blur, sharpen)
- [ ] `AdjustBrightnessCommand` (with int value)
- [ ] `AdjustContrastCommand` (with int value)
- [ ] `AddTextCommand` (text, position)
- [ ] `TrimVideoCommand` (start, end times)
- [ ] `AdjustVolumeCommand` (volume percentage)

### Phase 3: Advanced Features
- [ ] `MacroCommand` (composite command)
- [ ] Create presets (Vintage Effect, Professional Look)
- [ ] Operation history display
- [ ] Export/import preset functionality

### Phase 4: Demo & Documentation
- [ ] `CommandPatternDemo.java` with 4-5 scenarios
- [ ] `package.bluej` with UML diagram
- [ ] `Documents/Solutions/Command.md` with 8 sections

## Success Criteria

✅ **Implementation Complete When:**
1. All 6+ command types implemented
2. Undo/redo working for all commands
3. At least 2 macro commands (presets)
4. Operation history tracking
5. Demo shows 4+ scenarios
6. Zero if-else in VideoEditor for command execution
7. Each command independently testable
8. UML diagram shows all relationships

## References

- **Gang of Four**: Command pattern for encapsulating requests
- **Use Case**: GUI frameworks (Swing, JavaFX), Text editors (VSCode, Photoshop), Transaction systems
- **Related Patterns**:
  - Memento (for storing state)
  - Composite (for macro commands)
  - Strategy (similar structure, different intent)

---

**Pattern #19 of 24 - Command Pattern**
**Video Platform Context: Video Editor with Undo/Redo**
**Next Pattern: #20 - TBD**
