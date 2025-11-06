import java.util.NoSuchElementException;

/**
 * ReverseArrayIterator - Bonus Iterator (reverse traversal)
 *
 * Implements VideoIterator for reverse traversal of array-based collections.
 *
 * Traversal Strategy:
 * - Reverse: Iterates from last element to first
 * - Start position: count - 1 (last element)
 * - End position: 0 (first element)
 *
 * Use Cases:
 * - Watch history (newest first)
 * - Recent uploads (latest first)
 * - Undo operations (most recent first)
 *
 * StreamFlix Context:
 * Watch history is stored chronologically (oldest to newest),
 * but users want to see newest first. ReverseArrayIterator
 * provides this view without modifying the underlying array.
 *
 * Key Design:
 * - Same VideoIterator interface (polymorphic with other iterators)
 * - Client code identical to forward iteration
 * - Only difference: iteration direction is reversed
 *
 * Example:
 * VideoCollection history = new ArrayVideoCollection("History");
 * // Add videos in chronological order
 * history.add(video1); // Watched at 9 AM
 * history.add(video2); // Watched at 10 AM
 * history.add(video3); // Watched at 11 AM
 *
 * // Display newest first
 * VideoIterator reverse = new ReverseArrayIterator(history);
 * while (reverse.hasNext()) {
 *     System.out.println(reverse.next()); // 11 AM, 10 AM, 9 AM
 * }
 */
public class ReverseArrayIterator implements VideoIterator {

    private Video[] videos;
    private int count;
    private int position;        // Position in reverse iteration

    /**
     * Constructor
     *
     * @param videos Array of videos to iterate over
     * @param count Number of valid videos in array
     */
    public ReverseArrayIterator(Video[] videos, int count) {
        this.videos = videos;
        this.count = count;
        this.position = count - 1; // Start from last element
    }

    /**
     * Constructor that takes ArrayVideoCollection directly
     *
     * Convenience constructor for easier usage.
     *
     * Note: This breaks encapsulation slightly (needs package access
     * to collection internals). Alternative: Collection could provide
     * createReverseIterator() method.
     *
     * @param collection Array-based collection to iterate in reverse
     */
    public ReverseArrayIterator(ArrayVideoCollection collection) {
        // Would need package access or getter for videos array
        // For simplicity, this demo uses constructor with arrays directly
        throw new UnsupportedOperationException(
            "Use constructor with videos[] and count instead"
        );
    }

    /**
     * Check if more videos are available
     *
     * In reverse iteration, we have more videos if position >= 0.
     *
     * @return true if more videos available
     */
    @Override
    public boolean hasNext() {
        return position >= 0 && videos[position] != null;
    }

    /**
     * Get next video and move backward
     *
     * Returns video at current position and decrements position
     * (moving backwards through array).
     *
     * @return Next video (in reverse order)
     * @throws NoSuchElementException if no more videos
     */
    @Override
    public Video next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more videos in collection");
        }
        return videos[position--]; // Post-decrement: return current, then move back
    }

    /**
     * Get current position in iteration
     *
     * Returns position in reverse iteration (counting backwards).
     *
     * @return Current position
     */
    @Override
    public int currentIndex() {
        return count - 1 - position; // Convert to forward index for consistency
    }

    /**
     * Reset iterator to beginning (of reverse iteration)
     *
     * Resets position to last element (start of reverse iteration).
     */
    @Override
    public void reset() {
        position = count - 1;
    }
}
