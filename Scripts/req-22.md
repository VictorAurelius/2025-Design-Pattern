# Requirements Document: Iterator Pattern (Pattern #22)

## Pattern Information
- **Pattern Name**: Iterator Pattern
- **Category**: Behavioral Design Pattern
- **Complexity**: ⭐⭐ (Low-Medium - straightforward traversal mechanism)
- **Popularity**: ⭐⭐⭐⭐⭐ (Very High - fundamental pattern, built into most languages)

## Business Context: StreamFlix Video Platform

### Domain
**Universal Video Collection Traversal System**

StreamFlix manages video collections in various data structures for different use cases:
- **Playlists**: Array-based for fast access
- **Watch History**: Linked list for efficient append operations
- **Recommendations**: Tree structure for hierarchical categories
- **Trending Videos**: Priority queue based on popularity
- **Search Results**: Dynamic collections that change

The platform needs a unified way to iterate through these different collections without exposing their internal structure to client code. Different users also need different iteration strategies:
- **Sequential**: One by one (default)
- **Filtered**: Only matching criteria (e.g., only unwatched videos)
- **Shuffled**: Random order (for "Surprise Me" feature)
- **Reverse**: Newest first (for history)
- **Batched**: Load 10 at a time (for infinite scroll)

### Current Problem (Without Iterator Pattern)

**Different Traversal Code for Each Structure:**
```java
// ❌ Client must know internal structure!

// Array-based playlist
public void displayPlaylist(Video[] playlist) {
    for (int i = 0; i < playlist.length; i++) {
        System.out.println(playlist[i]);
    }
}

// LinkedList-based history
public void displayHistory(VideoNode head) {
    VideoNode current = head;
    while (current != null) {
        System.out.println(current.video);
        current = current.next;
    }
}

// Tree-based recommendations
public void displayRecommendations(VideoTreeNode root) {
    // Complex tree traversal logic
    traverseTree(root);
}

// ❌ Client code is tightly coupled to data structure!
// ❌ Changing data structure breaks all client code!
// ❌ Cannot use same iteration logic across different collections!
```

**Issues:**
1. ❌ **Tight Coupling**: Client code depends on internal structure
2. ❌ **Code Duplication**: Iteration logic repeated for each structure
3. ❌ **No Abstraction**: Cannot write generic code for "any collection"
4. ❌ **Exposed Internals**: Collection implementation details leaked
5. ❌ **Hard to Change**: Switching data structure requires changing all client code
6. ❌ **No Multiple Iterators**: Cannot have multiple simultaneous traversals
7. ❌ **No Custom Iterations**: Difficult to add filtered/shuffled/reversed iteration

### Real-World Problem Scenario

**User Journey: Sarah's Playlist Nightmare**
```
Sarah: Premium user with 500 videos in "Watch Later" playlist

Current Implementation: Playlist stored as array (Video[])

Feature Request 1: "Show only unwatched videos"
  Developer: "Need to write custom loop to filter unwatched"
  → 20 lines of code
  → Duplicated across 15 different UI components
  → Maintenance nightmare

Feature Request 2: "Sort by date added (newest first)"
  Developer: "Need to reverse iterate through array"
  → Another custom loop
  → More code duplication
  → Inconsistent iteration logic

Feature Request 3: "Switch to linked list for efficient removals"
  Developer: "PROBLEM! All iteration code assumes array!"
  → Must rewrite 50+ files
  → Risk breaking existing features
  → 1 week of development time
  → ❌ Feature rejected due to cost

Sarah's Experience:
  ❌ Cannot filter unwatched videos
  ❌ Cannot sort by date added
  ❌ Stuck with slow array-based implementation
  😞 Frustrated, considers canceling subscription
```

**Business User Journey: TechCorp Training Platform**
```
TechCorp: Enterprise customer with 10,000 training videos

Requirements:
  - Browse videos by category (tree structure)
  - Browse by completion status (filtered iteration)
  - Browse by popularity (priority queue)
  - Browse chronologically (array)

Current System:
  ❌ Each browsing mode has different iteration code
  ❌ UI components cannot reuse iteration logic
  ❌ Adding new browsing mode requires UI changes
  ❌ 500 lines of duplicated iteration code
  ❌ Bugs in one iteration logic don't get fixed in others

Impact:
  - 😞 Confusing user experience (inconsistent navigation)
  - 🐛 Bugs in some browsing modes but not others
  - ⏱️ 2 weeks to add new browsing mode
  - 💰 High maintenance cost
```

**Business Impact:**
- 💰 **High Development Cost**: 2 weeks per new iteration feature
- 🐛 **Code Duplication**: Iteration logic duplicated 50+ times
- 😞 **Poor UX**: Cannot provide filtering, sorting, shuffling
- 📉 **Customer Churn**: Users frustrated by limited navigation
- 🔒 **Vendor Lock-in**: Cannot change data structure without breaking everything
- ⏱️ **Slow Innovation**: Simple features take weeks to implement

### Why This Occurs

1. **No Separation of Concerns**: Iteration logic mixed with data structure
   - Client must know: Is it array? Linked list? Tree?
   - Client must write: Different loops for each structure
   - Cannot change: Data structure without breaking clients

2. **No Abstraction**: No common interface for "traversable collection"
   - Arrays use: `for (int i = 0; i < arr.length; i++)`
   - Lists use: `while (node != null) { node = node.next; }`
   - Trees use: Recursive traversal
   - No common pattern!

3. **Multiple Iteration Strategies**: Need different ways to traverse
   - Forward vs reverse
   - Filtered vs all
   - Shuffled vs sequential
   - Batched vs continuous
   - No way to parameterize iteration strategy

4. **Encapsulation Violation**: Collection internals exposed
   - Client has direct access to internal array
   - Client can modify structure during iteration
   - No protection against concurrent modification

## Requirements

### Functional Requirements

#### FR-1: Iterator Interface
The system must provide a common interface for iterating through collections:

**Iterator Interface:**
```java
public interface VideoIterator {
    /**
     * Check if there are more videos to iterate
     * @return true if more videos available, false otherwise
     */
    boolean hasNext();

    /**
     * Get the next video in the iteration
     * @return Next video
     * @throws NoSuchElementException if no more videos
     */
    Video next();

    /**
     * Get current position in iteration (optional)
     * @return Current index/position
     */
    int currentIndex();

    /**
     * Reset iterator to beginning (optional)
     */
    void reset();
}
```

**Collection Interface (Aggregate):**
```java
public interface VideoCollection {
    /**
     * Create an iterator for this collection
     * @return Iterator for traversing this collection
     */
    VideoIterator createIterator();

    /**
     * Add a video to the collection
     */
    void add(Video video);

    /**
     * Get collection size
     */
    int size();
}
```

#### FR-2: Concrete Collections
The system must support multiple collection implementations:

**1. ArrayVideoCollection**
- Internal storage: `Video[]` array
- Fast random access
- Fixed/dynamic size
- Use case: Playlists, curated collections

**2. LinkedListVideoCollection**
- Internal storage: Linked nodes
- Efficient insertion/deletion
- Sequential access only
- Use case: Watch history, queue

**3. TreeVideoCollection** (Bonus)
- Internal storage: Tree structure (by category/genre)
- Hierarchical organization
- In-order/pre-order/post-order traversal
- Use case: Category browsing, recommendations

#### FR-3: Concrete Iterators
The system must provide iterator implementations for each collection:

**1. ArrayVideoIterator**
- Traverses array from index 0 to length-1
- Supports `hasNext()`, `next()`, `currentIndex()`, `reset()`
- Handles null elements gracefully

**2. LinkedListVideoIterator**
- Traverses linked nodes using next pointers
- Supports `hasNext()`, `next()`
- Tracks current node position

**3. ReverseArrayIterator** (Bonus)
- Traverses array in reverse (newest first)
- Use case: Watch history (newest first)

**4. FilteredIterator** (Bonus)
- Wraps another iterator
- Skips elements not matching filter criteria
- Use case: Show only unwatched videos, specific genre

**5. ShuffledIterator** (Bonus)
- Randomizes iteration order
- Use case: "Surprise Me" feature, random playlist

**6. BatchIterator** (Bonus)
- Returns videos in batches (e.g., 10 at a time)
- Use case: Infinite scroll, pagination

#### FR-4: Iteration Strategies
The system must support different iteration strategies:

**Sequential Iteration:**
```java
VideoCollection playlist = new ArrayVideoCollection();
// ... add videos ...

VideoIterator iterator = playlist.createIterator();
while (iterator.hasNext()) {
    Video video = iterator.next();
    System.out.println(video.getTitle());
}
```

**Filtered Iteration:**
```java
VideoIterator allVideos = playlist.createIterator();
VideoIterator unwatchedOnly = new FilteredIterator(allVideos,
    video -> !video.isWatched());

while (unwatchedOnly.hasNext()) {
    Video video = unwatchedOnly.next();
    System.out.println(video.getTitle());
}
```

**Reverse Iteration:**
```java
VideoCollection history = new ArrayVideoCollection();
// ... add videos ...

VideoIterator reverseIterator = new ReverseArrayIterator(history);
while (reverseIterator.hasNext()) {
    Video video = reverseIterator.next();
    System.out.println(video.getTitle()); // Newest first
}
```

**Shuffled Iteration:**
```java
VideoCollection playlist = new ArrayVideoCollection();
// ... add videos ...

VideoIterator shuffled = new ShuffledIterator(playlist.createIterator());
while (shuffled.hasNext()) {
    Video video = shuffled.next();
    System.out.println(video.getTitle()); // Random order
}
```

#### FR-5: Multiple Simultaneous Iterations
The system must support multiple independent iterations over the same collection:

```java
VideoCollection playlist = new ArrayVideoCollection();
// ... add videos ...

// Iterator 1: Display titles
VideoIterator iterator1 = playlist.createIterator();

// Iterator 2: Count unwatched
VideoIterator iterator2 = playlist.createIterator();

// Both iterators work independently
// Modifying one doesn't affect the other
```

### Non-Functional Requirements

#### NFR-1: Encapsulation
- **Internal Structure Hidden**: Client cannot access collection's internal array/list/tree
- **Iterator Encapsulates Position**: Current position hidden from client
- **Safe Modification**: Collection can change implementation without breaking clients

#### NFR-2: Performance
- **Iterator Creation**: O(1) - constant time
- **hasNext()**: O(1) - constant time check
- **next()**: O(1) for array/list, O(log n) for tree
- **Memory Overhead**: < 100 bytes per iterator instance

#### NFR-3: Usability
- **Consistent Interface**: All iterators have same methods
- **Familiar Pattern**: Matches Java's Iterator interface
- **Easy to Extend**: Adding new iterator type requires one class

#### NFR-4: Safety
- **Fail-Fast**: Detect concurrent modification (optional)
- **Bounds Checking**: Prevent index out of bounds
- **Null Handling**: Handle null elements gracefully

### Acceptance Criteria

✅ **Implementation Complete When:**

1. `VideoIterator` interface defines iteration contract
2. `VideoCollection` interface defines aggregate contract
3. At least 2 concrete collections (Array, LinkedList)
4. At least 2 concrete iterators (Array, LinkedList)
5. Bonus iterators: Reverse, Filtered, Shuffled (at least 1)
6. Client can iterate without knowing internal structure
7. Multiple simultaneous iterations supported
8. Demo shows at least 5 iteration scenarios
9. UML diagram shows iterator pattern structure
10. Code compiles without errors

## Iterator Pattern Structure

### Components

#### 1. Iterator Interface
```java
/**
 * VideoIterator - Iterator interface
 * Provides methods to traverse a video collection
 */
public interface VideoIterator {
    boolean hasNext();
    Video next();
    int currentIndex(); // Optional
    void reset();       // Optional
}
```

#### 2. Aggregate Interface
```java
/**
 * VideoCollection - Aggregate interface
 * Collections that can be iterated implement this
 */
public interface VideoCollection {
    VideoIterator createIterator();
    void add(Video video);
    int size();
}
```

#### 3. Concrete Aggregate
```java
/**
 * ArrayVideoCollection - Concrete aggregate (array-based)
 */
public class ArrayVideoCollection implements VideoCollection {
    private Video[] videos;
    private int count;

    public ArrayVideoCollection(int capacity) {
        videos = new Video[capacity];
        count = 0;
    }

    @Override
    public void add(Video video) {
        if (count < videos.length) {
            videos[count++] = video;
        }
    }

    @Override
    public VideoIterator createIterator() {
        return new ArrayVideoIterator(videos, count);
    }

    @Override
    public int size() {
        return count;
    }
}
```

#### 4. Concrete Iterator
```java
/**
 * ArrayVideoIterator - Concrete iterator for array-based collection
 */
public class ArrayVideoIterator implements VideoIterator {
    private Video[] videos;
    private int count;
    private int position;

    public ArrayVideoIterator(Video[] videos, int count) {
        this.videos = videos;
        this.count = count;
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < count && videos[position] != null;
    }

    @Override
    public Video next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more videos");
        }
        return videos[position++];
    }

    @Override
    public int currentIndex() {
        return position;
    }

    @Override
    public void reset() {
        position = 0;
    }
}
```

#### 5. Client Code
```java
// Client code - doesn't know about internal structure!
VideoCollection playlist = new ArrayVideoCollection(10);
playlist.add(new Video("JavaScript Tutorial"));
playlist.add(new Video("Python Basics"));
playlist.add(new Video("React Course"));

VideoIterator iterator = playlist.createIterator();
while (iterator.hasNext()) {
    Video video = iterator.next();
    System.out.println(video.getTitle());
}

// Can easily switch to LinkedListVideoCollection
// Client code remains the same!
```

### Class Diagram Structure
```
┌─────────────────────────────────┐
│   VideoIterator (Interface)     │
│       <<interface>>             │
├─────────────────────────────────┤
│ + hasNext(): boolean            │
│ + next(): Video                 │
│ + currentIndex(): int           │
│ + reset(): void                 │
└────────┬────────────────────────┘
         │
         │ implements
         │
    ┌────┴─────────────────┐
    │                      │
┌───▼──────────────┐  ┌───▼──────────────────┐
│ArrayVideoIterator│  │LinkedListVideoIterator│
├──────────────────┤  ├──────────────────────┤
│- videos: Video[] │  │- current: VideoNode  │
│- position: int   │  │- head: VideoNode     │
├──────────────────┤  ├──────────────────────┤
│+ hasNext()       │  │+ hasNext()           │
│+ next()          │  │+ next()              │
└──────────────────┘  └──────────────────────┘


┌─────────────────────────────────┐
│  VideoCollection (Interface)    │
│       <<interface>>             │
├─────────────────────────────────┤
│ + createIterator(): VideoIterator│
│ + add(video: Video): void       │
│ + size(): int                   │
└────────┬────────────────────────┘
         │
         │ implements
         │
    ┌────┴─────────────────┐
    │                      │
┌───▼──────────────┐  ┌───▼───────────────────┐
│ArrayVideoCollection│ │LinkedListVideoCollection│
├──────────────────┤  ├───────────────────────┤
│- videos: Video[] │  │- head: VideoNode      │
│- count: int      │  │- count: int           │
├──────────────────┤  ├───────────────────────┤
│+ createIterator()│  │+ createIterator()     │
│+ add()           │  │+ add()                │
└──────────────────┘  └───────────────────────┘
         │                      │
         │ creates              │ creates
         ▼                      ▼
   ArrayVideoIterator    LinkedListVideoIterator


┌────────────────┐
│     Video      │ (Element)
├────────────────┤
│- id: int       │
│- title: String │
│- duration: int │
│- watched: boolean│
├────────────────┤
│+ getters/setters│
└────────────────┘
```

## Use Cases

### Use Case 1: Simple Sequential Iteration
```
User: Maria browsing "Watch Later" playlist

Scenario:
  Playlist: ArrayVideoCollection with 5 videos
  Iteration: Sequential (first to last)

Expected Output:
  1. JavaScript Tutorial
  2. Python Basics
  3. React Hooks
  4. Design Patterns
  5. Database Fundamentals

Implementation:
  VideoIterator iterator = playlist.createIterator();
  while (iterator.hasNext()) {
      Video video = iterator.next();
      display(video);
  }
```

### Use Case 2: Watch History (Reverse Iteration)
```
User: Alex viewing watch history

Scenario:
  History: LinkedListVideoCollection (oldest to newest)
  Display: Newest first (reverse order)

Expected Output:
  1. Video watched 5 minutes ago
  2. Video watched 1 hour ago
  3. Video watched yesterday
  4. Video watched last week

Implementation:
  VideoIterator reverseIterator = new ReverseIterator(history.createIterator());
  while (reverseIterator.hasNext()) {
      Video video = reverseIterator.next();
      display(video);
  }
```

### Use Case 3: Filtered Iteration (Unwatched Only)
```
User: Sarah wants to see only unwatched videos in playlist

Scenario:
  Playlist: 10 videos (5 watched, 5 unwatched)
  Filter: Show only unwatched

Expected Output:
  1. React Hooks (unwatched)
  2. Design Patterns (unwatched)
  3. Database Fundamentals (unwatched)
  4. Docker Tutorial (unwatched)
  5. Kubernetes Basics (unwatched)

Implementation:
  VideoIterator allVideos = playlist.createIterator();
  VideoIterator unwatchedOnly = new FilteredIterator(allVideos,
      video -> !video.isWatched());

  while (unwatchedOnly.hasNext()) {
      Video video = unwatchedOnly.next();
      display(video);
  }
```

### Use Case 4: Shuffled Iteration (Surprise Me)
```
User: John clicks "Surprise Me" to shuffle playlist

Scenario:
  Playlist: 5 videos in specific order
  Iteration: Random/shuffled

Original Order:
  1. JavaScript Tutorial
  2. Python Basics
  3. React Hooks
  4. Design Patterns
  5. Database Fundamentals

Shuffled Output (random):
  1. Design Patterns
  2. JavaScript Tutorial
  3. Database Fundamentals
  4. React Hooks
  5. Python Basics

Implementation:
  VideoIterator shuffled = new ShuffledIterator(playlist.createIterator());
  while (shuffled.hasNext()) {
      Video video = shuffled.next();
      display(video);
  }
```

### Use Case 5: Batch Iteration (Infinite Scroll)
```
User: Mobile app loading videos in batches

Scenario:
  Collection: 1000 videos
  Display: 10 at a time (infinite scroll)

Batch 1 (initial load):
  Videos 1-10

User scrolls down:
  Batch 2: Videos 11-20

User scrolls down:
  Batch 3: Videos 21-30

Implementation:
  VideoIterator batchIterator = new BatchIterator(collection.createIterator(), 10);

  while (batchIterator.hasNext()) {
      Video[] batch = batchIterator.nextBatch();
      displayBatch(batch);
      waitForUserScroll();
  }
```

## Expected Output (Demo Scenarios)

### Scenario 1: Array vs LinkedList - Same Client Code
```
═══════════════════════════════════════════════════════════════
ITERATOR PATTERN - Universal Collection Traversal
StreamFlix Video Platform
═══════════════════════════════════════════════════════════════

SCENARIO 1: Different Collections, Same Iteration Code
─────────────────────────────────────────────────────────────

Creating Array-Based Playlist...
  Added: JavaScript Tutorial
  Added: Python Basics
  Added: React Hooks

Creating LinkedList-Based History...
  Added: Docker Fundamentals
  Added: Kubernetes Intro
  Added: AWS Overview

SAME CLIENT CODE for both collections:

VideoIterator iterator = collection.createIterator();
while (iterator.hasNext()) {
    System.out.println(iterator.next().getTitle());
}

Array-Based Playlist Output:
  1. JavaScript Tutorial
  2. Python Basics
  3. React Hooks

LinkedList-Based History Output:
  1. Docker Fundamentals
  2. Kubernetes Intro
  3. AWS Overview

BENEFIT: Client doesn't know (or care) about internal structure!
         Same iteration code works for both!
```

### Scenario 2: Reverse Iteration
```
SCENARIO 2: Watch History (Newest First)
─────────────────────────────────────────────────────────────

Watch History (chronological order):
  1. Video watched at 9:00 AM
  2. Video watched at 10:00 AM
  3. Video watched at 11:00 AM
  4. Video watched at 12:00 PM
  5. Video watched at 1:00 PM

Display Order (newest first):

ReverseIterator iterator = new ReverseIterator(history);

Output:
  1. Video watched at 1:00 PM  (newest)
  2. Video watched at 12:00 PM
  3. Video watched at 11:00 AM
  4. Video watched at 10:00 AM
  5. Video watched at 9:00 AM  (oldest)

BENEFIT: Easy to reverse iteration without exposing array indices!
```

### Scenario 3: Filtered Iteration
```
SCENARIO 3: Show Only Unwatched Videos
─────────────────────────────────────────────────────────────

Playlist (10 videos):
  1. JavaScript Tutorial (watched)
  2. Python Basics (unwatched)
  3. React Hooks (watched)
  4. Design Patterns (unwatched)
  5. Database Fundamentals (unwatched)
  6. Docker Tutorial (watched)
  7. Kubernetes Basics (unwatched)
  8. AWS Overview (watched)
  9. Git Advanced (unwatched)
  10. CI/CD Pipeline (watched)

FilteredIterator unwatchedOnly = new FilteredIterator(
    playlist.createIterator(),
    video -> !video.isWatched()
);

Output (unwatched only):
  1. Python Basics
  2. Design Patterns
  3. Database Fundamentals
  4. Kubernetes Basics
  5. Git Advanced

Filtered: 5 out of 10 videos

BENEFIT: Flexible filtering without modifying collection!
```

### Scenario 4: Multiple Simultaneous Iterations
```
SCENARIO 4: Multiple Independent Iterations
─────────────────────────────────────────────────────────────

Playlist: 5 videos

Task 1: Display titles (iterator1)
Task 2: Count unwatched (iterator2)
Task 3: Find longest video (iterator3)

All 3 tasks run simultaneously, each with independent iterator:

Iterator 1 at position: 0
Iterator 2 at position: 0
Iterator 3 at position: 0

Iterator 1: Processing "JavaScript Tutorial"
Iterator 2: Checking if "JavaScript Tutorial" is unwatched
Iterator 3: Duration of "JavaScript Tutorial": 30 min

Iterator 1 at position: 1
Iterator 2 at position: 1
Iterator 3 at position: 1

...

BENEFIT: Each iterator maintains its own position!
         No interference between iterations!
```

### Scenario 5: Shuffled Iteration
```
SCENARIO 5: Shuffle Playlist (Surprise Me!)
─────────────────────────────────────────────────────────────

Original Playlist Order:
  1. JavaScript Tutorial
  2. Python Basics
  3. React Hooks
  4. Design Patterns
  5. Database Fundamentals

ShuffledIterator shuffled = new ShuffledIterator(playlist.createIterator());

Shuffled Output (random):
  1. Design Patterns
  2. JavaScript Tutorial
  3. Database Fundamentals
  4. React Hooks
  5. Python Basics

Click "Shuffle Again":

New Shuffled Output:
  1. React Hooks
  2. Database Fundamentals
  3. Python Basics
  4. Design Patterns
  5. JavaScript Tutorial

BENEFIT: Easy to add new iteration strategies (shuffle, random, etc.)!
```

## Design Considerations

### 1. Internal vs External Iterator

**Internal Iterator (Callback-based):**
```java
playlist.forEach(video -> {
    System.out.println(video.getTitle());
});
```

**External Iterator (Pull-based):**
```java
VideoIterator iterator = playlist.createIterator();
while (iterator.hasNext()) {
    Video video = iterator.next();
    System.out.println(video.getTitle());
}
```

**Decision: Use External Iterator**

**Reasoning:**
- More control: Client controls iteration pace
- Can pause/resume iteration
- Can break out of loop early
- Multiple simultaneous iterations
- Easier to understand for imperative programming

**Trade-off:**
- More verbose than internal iterator
- Client responsible for iteration logic

### 2. Fail-Fast vs Fail-Safe

**Fail-Fast:**
```java
// Detect concurrent modification and throw exception
if (collection.modificationCount != expectedModificationCount) {
    throw new ConcurrentModificationException();
}
```

**Fail-Safe:**
```java
// Iterator works on snapshot/copy of collection
// Modifications don't affect iteration
```

**Decision: Simplified (No Fail-Fast for demo)**

**Reasoning:**
- Fail-fast adds complexity
- Educational focus on iterator pattern basics
- Can be added later if needed

**Note:** Production code should use fail-fast for safety.

### 3. Iterator Creation Strategy

**Option A: Factory Method in Collection**
```java
public interface VideoCollection {
    VideoIterator createIterator(); // Factory method
}
```

**Option B: Expose Internal Structure**
```java
public Video[] getVideos() {
    return videos; // ❌ Breaks encapsulation!
}
```

**Chosen: A (Factory Method)**

**Benefits:**
- Encapsulation: Internal structure hidden
- Flexibility: Can return different iterator types
- Control: Collection controls iterator creation

### 4. Iterator Interface Design

**Option A: Minimal Interface**
```java
public interface VideoIterator {
    boolean hasNext();
    Video next();
}
```

**Option B: Rich Interface**
```java
public interface VideoIterator {
    boolean hasNext();
    Video next();
    void remove();           // Modify during iteration
    int currentIndex();      // Query position
    void reset();            // Restart iteration
    Video peek();            // Look ahead without advancing
}
```

**Chosen: A + Optional Methods**

**Reasoning:**
- Minimal interface is easier to implement
- Optional methods added where needed
- Keeps simple iterators simple

## Benefits

1. ✅ **Uniform Interface**: Iterate different collections the same way
2. ✅ **Encapsulation**: Internal structure hidden from client
3. ✅ **Multiple Iterations**: Multiple simultaneous traversals
4. ✅ **Flexible Traversal**: Easy to add new iteration strategies
5. ✅ **Single Responsibility**: Iterator handles traversal, collection handles storage
6. ✅ **Open/Closed**: Add new iterators without modifying collections
7. ✅ **Simplifies Client**: Client doesn't need to know about internal structure

## Drawbacks

1. ❌ **Overkill for Simple Collections**: Java already has Iterator interface
2. ❌ **More Classes**: Each collection needs iterator class
3. ❌ **Memory Overhead**: Each iterator stores position state
4. ❌ **Not Thread-Safe**: Concurrent modification issues (unless fail-fast)

## When to Use Iterator Pattern

**✅ Use When:**
- Have multiple collection types (array, list, tree, etc.)
- Want to hide collection internals from client
- Need multiple simultaneous iterations
- Want to add different traversal strategies (reverse, filtered, shuffled)
- Need uniform way to iterate different structures

**✅ Real-World Use Cases:**
- Java Collections Framework: `Iterator<T>`
- Database result sets: `ResultSet.next()`
- File system traversal: Directory iterators
- DOM tree traversal: `NodeIterator`
- UI component trees: Widget iterators

**❌ Don't Use When:**
- Only one collection type (use built-in iterator)
- Collection structure never changes
- Performance is critical (direct access faster)
- Language has built-in iteration (for-each loops)

## Implementation Checklist

### Phase 1: Core Interfaces
- [ ] Create `Video` class (element)
- [ ] Create `VideoIterator` interface
- [ ] Create `VideoCollection` interface (aggregate)

### Phase 2: Array-Based Implementation
- [ ] Create `ArrayVideoCollection` (concrete aggregate)
- [ ] Create `ArrayVideoIterator` (concrete iterator)
- [ ] Test sequential iteration

### Phase 3: LinkedList-Based Implementation
- [ ] Create `VideoNode` class (linked list node)
- [ ] Create `LinkedListVideoCollection` (concrete aggregate)
- [ ] Create `LinkedListVideoIterator` (concrete iterator)
- [ ] Test sequential iteration

### Phase 4: Bonus Iterators (at least 1)
- [ ] Create `ReverseArrayIterator` (reverse traversal)
- [ ] Create `FilteredIterator` (conditional traversal)
- [ ] Create `ShuffledIterator` (random order traversal)

### Phase 5: Demo & Documentation
- [ ] Create `IteratorPatternDemo.java`
- [ ] Demonstrate 5+ iteration scenarios
- [ ] Show multiple simultaneous iterations
- [ ] Show collection independence (array vs list)
- [ ] Create `package.bluej` with UML
- [ ] Create `Solution/Iterator.md`

## Success Criteria

✅ **Implementation Complete When:**
1. `VideoIterator` and `VideoCollection` interfaces defined
2. At least 2 concrete collections (Array, LinkedList)
3. At least 2 concrete iterators (Array, LinkedList)
4. At least 1 bonus iterator (Reverse, Filtered, or Shuffled)
5. Client can iterate without knowing internal structure
6. Same client code works for different collections
7. Multiple simultaneous iterations supported
8. Demo shows 5+ realistic scenarios
9. UML diagram shows iterator pattern structure
10. Code compiles without errors

## Deliverables

1. **Source Code** (in `22-Iterator-DP/`)
   - `Video.java` (element)
   - `VideoIterator.java` (iterator interface)
   - `VideoCollection.java` (aggregate interface)
   - `ArrayVideoCollection.java`, `ArrayVideoIterator.java` (array-based)
   - `VideoNode.java`, `LinkedListVideoCollection.java`, `LinkedListVideoIterator.java` (list-based)
   - At least 1 bonus iterator: `ReverseArrayIterator.java` OR `FilteredIterator.java` OR `ShuffledIterator.java`
   - `IteratorPatternDemo.java` (demo)

2. **Documentation**
   - `Solution/Iterator.md` (explanation, UML, examples)

3. **UML Diagram**
   - `package.bluej` (BlueJ class diagram)

## References

- **Gang of Four**: Iterator pattern for sequential access
- **Java Collections**: `Iterator<T>` interface
- **Related Patterns**:
  - Composite: Often used with iterator to traverse tree structures
  - Factory Method: Iterator creation via factory method
  - Memento: Can save/restore iterator state

---

**Pattern #22 of 24 - Iterator Pattern**
**Video Platform Context: Universal Video Collection Traversal**
**Next Pattern: #23 - TBD**
