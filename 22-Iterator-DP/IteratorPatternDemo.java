/**
 * IteratorPatternDemo - Comprehensive demonstration of Iterator pattern
 *
 * Demonstrates:
 * 1. Array vs LinkedList - Same client code works for both
 * 2. Reverse iteration (watch history - newest first)
 * 3. Filtered iteration (unwatched videos only)
 * 4. Multiple simultaneous iterations (independent iterators)
 * 5. Shuffled iteration ("Surprise Me" feature)
 *
 * Scenario: StreamFlix video platform
 * Collections need to be traversed in various ways without exposing
 * their internal structure. Iterator pattern provides uniform interface
 * for traversing different collection types.
 *
 * Key Insight:
 * Without Iterator pattern: Client must know internal structure
 * With Iterator pattern: Client uses uniform interface for all collections
 *
 * Benefits:
 * - Same iteration code for different collections
 * - Multiple simultaneous iterations
 * - Easy to add new iteration strategies
 * - Internal structure hidden from client
 */
public class IteratorPatternDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║      ITERATOR PATTERN - Universal Collection Traversal           ║");
        System.out.println("║                    StreamFlix Platform                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Run demonstration scenarios
        demonstrateArrayVsLinkedList();
        demonstrateReverseIteration();
        demonstrateFilteredIteration();
        demonstrateMultipleIterations();
        demonstrateShuffledIteration();
        showPatternBenefits();
    }

    /**
     * Scenario 1: Different Collections, Same Iteration Code
     * Shows polymorphism - same client code works for array and linked list
     */
    private static void demonstrateArrayVsLinkedList() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 1: Different Collections, Same Iteration Code");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create array-based playlist
        System.out.println("Creating Array-Based Playlist...");
        VideoCollection arrayPlaylist = new ArrayVideoCollection(10, "Watch Later");
        arrayPlaylist.add(new Video(1, "JavaScript Tutorial", 30, false, "tutorial"));
        arrayPlaylist.add(new Video(2, "Python Basics", 45, false, "tutorial"));
        arrayPlaylist.add(new Video(3, "React Hooks", 25, true, "tutorial"));
        System.out.println("  Added 3 videos to array-based collection");
        System.out.println();

        // Create linked list-based history
        System.out.println("Creating LinkedList-Based History...");
        VideoCollection listHistory = new LinkedListVideoCollection("Watch History");
        listHistory.add(new Video(4, "Docker Fundamentals", 40, true, "devops"));
        listHistory.add(new Video(5, "Kubernetes Intro", 35, true, "devops"));
        listHistory.add(new Video(6, "AWS Overview", 50, true, "cloud"));
        System.out.println("  Added 3 videos to linked list-based collection");
        System.out.println();

        // Same client code for both!
        System.out.println("SAME CLIENT CODE for both collections:");
        System.out.println();
        System.out.println("  VideoIterator iterator = collection.createIterator();");
        System.out.println("  while (iterator.hasNext()) {");
        System.out.println("      System.out.println(iterator.next());");
        System.out.println("  }");
        System.out.println();

        // Display array-based playlist
        System.out.println("Array-Based Playlist (" + arrayPlaylist.getName() + "):");
        displayCollection(arrayPlaylist);
        System.out.println();

        // Display linked list-based history
        System.out.println("LinkedList-Based History (" + listHistory.getName() + "):");
        displayCollection(listHistory);
        System.out.println();

        System.out.println("BENEFIT: Client doesn't know (or care) about internal structure!");
        System.out.println("         Same iteration code works for both!");
    }

    /**
     * Scenario 2: Reverse Iteration
     * Shows watch history with newest first
     */
    private static void demonstrateReverseIteration() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 2: Watch History (Newest First)");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create watch history (stored chronologically: oldest to newest)
        ArrayVideoCollection history = new ArrayVideoCollection(10, "Watch History");
        history.add(new Video(1, "Video watched at 9:00 AM", 15, true, "tutorial"));
        history.add(new Video(2, "Video watched at 10:00 AM", 20, true, "review"));
        history.add(new Video(3, "Video watched at 11:00 AM", 25, true, "tutorial"));
        history.add(new Video(4, "Video watched at 12:00 PM", 18, true, "vlog"));
        history.add(new Video(5, "Video watched at 1:00 PM", 30, true, "tutorial"));

        System.out.println("Watch History (stored chronologically - oldest to newest):");
        System.out.println("  Internal storage order:");
        VideoIterator forward = history.createIterator();
        int count = 1;
        while (forward.hasNext()) {
            System.out.println("    " + count++ + ". " + forward.next().getTitle());
        }
        System.out.println();

        System.out.println("Display Order (newest first - reverse iteration):");
        System.out.println();
        System.out.println("  ReverseArrayIterator reverse = new ReverseArrayIterator(...)");
        System.out.println();

        // Create reverse iterator - pass internal array
        // Note: In real implementation, collection would provide this access
        Video[] videos = new Video[history.size()];
        VideoIterator temp = history.createIterator();
        int i = 0;
        while (temp.hasNext()) {
            videos[i++] = temp.next();
        }
        VideoIterator reverse = new ReverseArrayIterator(videos, history.size());

        System.out.println("  Output:");
        count = 1;
        while (reverse.hasNext()) {
            Video video = reverse.next();
            String marker = count == 1 ? "(newest)" : count == history.size() ? "(oldest)" : "";
            System.out.println("    " + count++ + ". " + video.getTitle() + " " + marker);
        }
        System.out.println();

        System.out.println("BENEFIT: Easy to reverse iteration without exposing array indices!");
    }

    /**
     * Scenario 3: Filtered Iteration
     * Shows only unwatched videos from playlist
     */
    private static void demonstrateFilteredIteration() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 3: Show Only Unwatched Videos");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create playlist with mix of watched and unwatched
        VideoCollection playlist = new ArrayVideoCollection(15, "My Playlist");
        playlist.add(new Video(1, "JavaScript Tutorial", 30, true, "tutorial"));
        playlist.add(new Video(2, "Python Basics", 45, false, "tutorial"));
        playlist.add(new Video(3, "React Hooks", 25, true, "tutorial"));
        playlist.add(new Video(4, "Design Patterns", 60, false, "tutorial"));
        playlist.add(new Video(5, "Database Fundamentals", 50, false, "tutorial"));
        playlist.add(new Video(6, "Docker Tutorial", 35, true, "devops"));
        playlist.add(new Video(7, "Kubernetes Basics", 40, false, "devops"));
        playlist.add(new Video(8, "AWS Overview", 55, true, "cloud"));
        playlist.add(new Video(9, "Git Advanced", 28, false, "tools"));
        playlist.add(new Video(10, "CI/CD Pipeline", 42, true, "devops"));

        System.out.println("Playlist: " + playlist.size() + " videos (mix of watched/unwatched)");
        System.out.println();

        System.out.println("All Videos:");
        VideoIterator allVideos = playlist.createIterator();
        int totalCount = 0;
        int watchedCount = 0;
        while (allVideos.hasNext()) {
            Video video = allVideos.next();
            totalCount++;
            if (video.isWatched()) watchedCount++;
            String status = video.isWatched() ? "[WATCHED]" : "[UNWATCHED]";
            System.out.println("  " + totalCount + ". " + video.getTitle() + " " + status);
        }
        System.out.println();
        System.out.println("Total: " + totalCount + " videos (" + watchedCount + " watched, " +
                         (totalCount - watchedCount) + " unwatched)");
        System.out.println();

        // Filter unwatched only
        System.out.println("Filtered View (unwatched only):");
        System.out.println();
        System.out.println("  FilteredIterator unwatchedOnly = new FilteredIterator(");
        System.out.println("      playlist.createIterator(),");
        System.out.println("      video -> !video.isWatched()");
        System.out.println("  );");
        System.out.println();

        VideoIterator unwatchedOnly = new FilteredIterator(
            playlist.createIterator(),
            video -> !video.isWatched()
        );

        System.out.println("  Output (unwatched only):");
        int filteredCount = 0;
        while (unwatchedOnly.hasNext()) {
            filteredCount++;
            Video video = unwatchedOnly.next();
            System.out.println("    " + filteredCount + ". " + video.getTitle());
        }
        System.out.println();
        System.out.println("  Filtered: " + filteredCount + " out of " + totalCount + " videos");
        System.out.println();

        System.out.println("BENEFIT: Flexible filtering without modifying collection!");
    }

    /**
     * Scenario 4: Multiple Simultaneous Iterations
     * Shows independent iterators over same collection
     */
    private static void demonstrateMultipleIterations() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 4: Multiple Independent Iterations");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create playlist
        VideoCollection playlist = new ArrayVideoCollection(10, "Shared Playlist");
        playlist.add(new Video(1, "JavaScript Tutorial", 30, false, "tutorial"));
        playlist.add(new Video(2, "Python Basics", 45, true, "tutorial"));
        playlist.add(new Video(3, "React Hooks", 60, false, "tutorial"));
        playlist.add(new Video(4, "Design Patterns", 90, false, "tutorial"));
        playlist.add(new Video(5, "Database Fundamentals", 50, true, "tutorial"));

        System.out.println("Playlist: " + playlist.size() + " videos");
        System.out.println();

        System.out.println("Running 3 tasks simultaneously with independent iterators:");
        System.out.println();

        // Task 1: Display titles
        VideoIterator iterator1 = playlist.createIterator();
        System.out.println("Task 1: Display Titles");

        // Task 2: Count unwatched
        VideoIterator iterator2 = playlist.createIterator();
        System.out.println("Task 2: Count Unwatched Videos");

        // Task 3: Find longest video
        VideoIterator iterator3 = playlist.createIterator();
        System.out.println("Task 3: Find Longest Video");
        System.out.println();

        System.out.println("Simulating parallel execution:");
        System.out.println();

        // Simulate interleaved execution
        int step = 0;
        while (iterator1.hasNext() || iterator2.hasNext() || iterator3.hasNext()) {
            step++;
            System.out.println("Step " + step + ":");

            if (iterator1.hasNext()) {
                Video v1 = iterator1.next();
                System.out.println("  Iterator 1 (position " + iterator1.currentIndex() + "): " +
                                 "Processing \"" + v1.getTitle() + "\"");
            }

            if (iterator2.hasNext()) {
                Video v2 = iterator2.next();
                String status = v2.isWatched() ? "watched" : "unwatched";
                System.out.println("  Iterator 2 (position " + iterator2.currentIndex() + "): " +
                                 "Checking if \"" + v2.getTitle() + "\" is " + status);
            }

            if (iterator3.hasNext()) {
                Video v3 = iterator3.next();
                System.out.println("  Iterator 3 (position " + iterator3.currentIndex() + "): " +
                                 "Duration of \"" + v3.getTitle() + "\": " + v3.getDuration() + " min");
            }

            System.out.println();
        }

        System.out.println("BENEFIT: Each iterator maintains its own position!");
        System.out.println("         No interference between iterations!");
    }

    /**
     * Scenario 5: Shuffled Iteration
     * Shows "Surprise Me" feature with random order
     */
    private static void demonstrateShuffledIteration() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 5: Shuffle Playlist (Surprise Me!)");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create playlist
        VideoCollection playlist = new ArrayVideoCollection(10, "My Playlist");
        playlist.add(new Video(1, "JavaScript Tutorial"));
        playlist.add(new Video(2, "Python Basics"));
        playlist.add(new Video(3, "React Hooks"));
        playlist.add(new Video(4, "Design Patterns"));
        playlist.add(new Video(5, "Database Fundamentals"));

        System.out.println("Original Playlist Order:");
        VideoIterator original = playlist.createIterator();
        int count = 1;
        while (original.hasNext()) {
            System.out.println("  " + count++ + ". " + original.next().getTitle());
        }
        System.out.println();

        // Shuffle
        System.out.println("ShuffledIterator shuffled = new ShuffledIterator(playlist.createIterator());");
        System.out.println();

        System.out.println("Shuffled Output #1 (random order):");
        ShuffledIterator shuffled1 = new ShuffledIterator(playlist.createIterator());
        count = 1;
        while (shuffled1.hasNext()) {
            System.out.println("  " + count++ + ". " + shuffled1.next().getTitle());
        }
        System.out.println();

        // Shuffle again
        System.out.println("Click \"Shuffle Again\":");
        System.out.println();
        System.out.println("Shuffled Output #2 (different random order):");
        ShuffledIterator shuffled2 = new ShuffledIterator(playlist.createIterator());
        count = 1;
        while (shuffled2.hasNext()) {
            System.out.println("  " + count++ + ". " + shuffled2.next().getTitle());
        }
        System.out.println();

        System.out.println("BENEFIT: Easy to add new iteration strategies (shuffle, random, etc.)!");
    }

    /**
     * Helper method to display entire collection
     */
    private static void displayCollection(VideoCollection collection) {
        VideoIterator iterator = collection.createIterator();
        int count = 1;

        while (iterator.hasNext()) {
            Video video = iterator.next();
            System.out.println("  " + count++ + ". " + video);
        }
    }

    /**
     * Show pattern benefits summary
     */
    private static void showPatternBenefits() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("ITERATOR PATTERN BENEFITS DEMONSTRATED");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✓ UNIFORM INTERFACE");
        System.out.println("  - Same iteration code for array and linked list");
        System.out.println("  - Client doesn't know internal structure");
        System.out.println("  - Easy to switch between collection types");
        System.out.println();

        System.out.println("✓ ENCAPSULATION");
        System.out.println("  - Internal structure hidden from client");
        System.out.println("  - Collection can change implementation");
        System.out.println("  - Client code remains unchanged");
        System.out.println();

        System.out.println("✓ MULTIPLE ITERATIONS");
        System.out.println("  - Multiple iterators over same collection");
        System.out.println("  - Each iterator maintains own position");
        System.out.println("  - No interference between iterations");
        System.out.println();

        System.out.println("✓ FLEXIBLE TRAVERSAL");
        System.out.println("  - Forward, reverse, filtered, shuffled");
        System.out.println("  - Easy to add new iteration strategies");
        System.out.println("  - Combine strategies (filter + reverse)");
        System.out.println();

        System.out.println("✓ SINGLE RESPONSIBILITY");
        System.out.println("  - Collection: Storage management");
        System.out.println("  - Iterator: Traversal logic");
        System.out.println("  - Clear separation of concerns");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("BEFORE vs AFTER COMPARISON");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Code Reusability:");
        System.out.println("  Before: Different iteration code for each collection type");
        System.out.println("  After:  Same iteration code for all collections");
        System.out.println("  Improvement: 100% code reuse");
        System.out.println();

        System.out.println("Flexibility:");
        System.out.println("  Before: Cannot change collection type without rewriting client");
        System.out.println("  After:  Switch collection types with zero client changes");
        System.out.println("  Improvement: Complete flexibility");
        System.out.println();

        System.out.println("Iteration Strategies:");
        System.out.println("  Before: Hardcoded loops, difficult to add new strategies");
        System.out.println("  After:  Pluggable iterators (forward, reverse, filtered, shuffled)");
        System.out.println("  Improvement: Unlimited strategies");
        System.out.println();

        System.out.println("Encapsulation:");
        System.out.println("  Before: Client accesses internal array/list directly");
        System.out.println("  After:  Internal structure completely hidden");
        System.out.println("  Improvement: Perfect encapsulation");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("WHEN TO USE ITERATOR PATTERN");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✅ USE WHEN:");
        System.out.println("  • Have multiple collection types (array, list, tree)");
        System.out.println("  • Want to hide collection internals from client");
        System.out.println("  • Need multiple simultaneous iterations");
        System.out.println("  • Want different traversal strategies");
        System.out.println();

        System.out.println("✅ REAL-WORLD USE CASES:");
        System.out.println("  • Java Collections Framework: Iterator<T>");
        System.out.println("  • Database result sets: ResultSet.next()");
        System.out.println("  • File system traversal: Directory iterators");
        System.out.println("  • DOM tree traversal: NodeIterator");
        System.out.println();

        System.out.println("❌ DON'T USE WHEN:");
        System.out.println("  • Only one collection type (use built-in iterator)");
        System.out.println("  • Collection structure never changes");
        System.out.println("  • Language has built-in iteration (for-each loops)");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("KEY TAKEAWAY");
        System.out.println("═".repeat(70));
        System.out.println();
        System.out.println("\"Iterator provides uniform way to traverse different collections\"");
        System.out.println();
        System.out.println("Collections provide createIterator() factory method.");
        System.out.println("Iterators encapsulate traversal logic with hasNext() and next().");
        System.out.println("Client code works with any collection type through common interface.");
        System.out.println();
        System.out.println("═".repeat(70));
    }
}
