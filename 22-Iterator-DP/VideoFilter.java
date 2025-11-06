/**
 * VideoFilter - Filter Interface (for FilteredIterator)
 *
 * Functional interface for filtering videos during iteration.
 *
 * This interface defines a predicate (boolean test) that determines
 * whether a video should be included in filtered iteration.
 *
 * Use Cases:
 * - Filter unwatched videos: video -> !video.isWatched()
 * - Filter by category: video -> video.getCategory().equals("tutorial")
 * - Filter by duration: video -> video.getDuration() > 30
 * - Complex filters: Combine multiple conditions
 *
 * StreamFlix Context:
 * Users frequently want to view filtered subsets of collections:
 * - "Show only unwatched"
 * - "Show only tutorials"
 * - "Show videos longer than 1 hour"
 *
 * This interface enables flexible filtering without modifying
 * the collection or creating specialized iterator classes for
 * each filter type.
 *
 * Design Pattern:
 * This is an example of Strategy pattern applied to filtering.
 * Different filter strategies can be provided at runtime.
 */
@FunctionalInterface
public interface VideoFilter {

    /**
     * Test whether a video matches the filter criteria
     *
     * @param video Video to test
     * @return true if video matches filter (should be included), false otherwise
     */
    boolean matches(Video video);
}
