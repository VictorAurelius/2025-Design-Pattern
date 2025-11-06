import java.util.NoSuchElementException;

/**
 * ArrayVideoIterator - Concrete Iterator (for array-based collections)
 *
 * Implements VideoIterator for traversing array-based collections.
 *
 * Internal State:
 * - videos: Reference to array being iterated
 * - count: Number of valid videos in array
 * - position: Current iteration position
 *
 * Traversal Strategy:
 * - Sequential: Iterates from index 0 to count-1
 * - Handles null elements gracefully
 * - Supports reset for multiple passes
 *
 * Key Design:
 * - Encapsulates position tracking (client doesn't see index)
 * - Decouples iteration logic from storage structure
 * - Same interface as other iterators (LinkedListVideoIterator, etc.)
 *
 * StreamFlix Context:
 * Used for array-based playlists and collections.
 * Provides efficient sequential iteration with O(1) access time.
 */
public class ArrayVideoIterator implements VideoIterator {

    private Video[] videos;      // Reference to collection's internal array
    private int count;           // Number of valid videos
    private int position;        // Current iteration position

    /**
     * Constructor
     *
     * @param videos Array of videos to iterate over
     * @param count Number of valid videos in array (may be < array.length)
     */
    public ArrayVideoIterator(Video[] videos, int count) {
        this.videos = videos;
        this.count = count;
        this.position = 0;
    }

    /**
     * Check if more videos are available
     *
     * Returns true if:
     * - Position is within bounds (position < count)
     * - Current video is not null
     *
     * @return true if more videos available
     */
    @Override
    public boolean hasNext() {
        return position < count && videos[position] != null;
    }

    /**
     * Get next video and advance position
     *
     * Returns the video at current position and increments position.
     * Throws exception if called when hasNext() is false.
     *
     * @return Next video
     * @throws NoSuchElementException if no more videos
     */
    @Override
    public Video next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more videos in collection");
        }
        return videos[position++];
    }

    /**
     * Get current position in iteration
     *
     * Useful for showing progress: "Video 5 of 20"
     *
     * @return Current position (0-based index)
     */
    @Override
    public int currentIndex() {
        return position;
    }

    /**
     * Reset iterator to beginning
     *
     * Allows reusing the same iterator for multiple passes:
     *
     * while (iterator.hasNext()) { ... }
     * iterator.reset();
     * while (iterator.hasNext()) { ... } // Second pass
     */
    @Override
    public void reset() {
        position = 0;
    }
}
