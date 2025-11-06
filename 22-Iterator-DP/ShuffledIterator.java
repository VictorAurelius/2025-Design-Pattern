import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ShuffledIterator - Bonus Iterator (random order traversal)
 *
 * Wraps another iterator and returns elements in random (shuffled) order.
 *
 * Traversal Strategy:
 * - Collects all elements from wrapped iterator into list
 * - Shuffles the list randomly
 * - Iterates through shuffled list
 *
 * Use Cases:
 * - "Surprise Me" feature (random playlist)
 * - Randomized quiz questions
 * - Shuffle play mode
 * - Avoid predictable patterns
 *
 * StreamFlix Context:
 * Users want to shuffle playlists for variety:
 * - "Shuffle my Watch Later playlist"
 * - "Play random video from this category"
 * - "Surprise me with something to watch"
 *
 * Key Design:
 * - Eager evaluation: Loads all videos upfront (for shuffling)
 * - Trade-off: Memory vs randomness
 * - Same VideoIterator interface (polymorphic)
 * - Each iteration gives different random order
 *
 * Example:
 * VideoIterator sequential = playlist.createIterator();
 * VideoIterator shuffled = new ShuffledIterator(sequential);
 *
 * while (shuffled.hasNext()) {
 *     Video video = shuffled.next(); // Random order
 *     display(video);
 * }
 *
 * Note: For large collections, consider sampling instead of full shuffle.
 */
public class ShuffledIterator implements VideoIterator {

    private List<Video> videos;    // All videos in shuffled order
    private int position;          // Current position in shuffled list

    /**
     * Constructor
     *
     * Eagerly loads all videos from wrapped iterator and shuffles them.
     *
     * @param iterator Iterator providing videos to shuffle
     */
    public ShuffledIterator(VideoIterator iterator) {
        this.videos = new ArrayList<>();
        this.position = 0;

        // Collect all videos
        while (iterator.hasNext()) {
            videos.add(iterator.next());
        }

        // Shuffle using Fisher-Yates algorithm (via Collections.shuffle)
        Collections.shuffle(videos);
    }

    /**
     * Check if more videos are available
     *
     * @return true if more videos in shuffled list
     */
    @Override
    public boolean hasNext() {
        return position < videos.size();
    }

    /**
     * Get next video from shuffled list
     *
     * @return Next video in random order
     * @throws NoSuchElementException if no more videos
     */
    @Override
    public Video next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more videos in shuffled collection");
        }
        return videos.get(position++);
    }

    /**
     * Get current position in shuffled iteration
     *
     * @return Current position
     */
    @Override
    public int currentIndex() {
        return position;
    }

    /**
     * Reset iterator to beginning
     *
     * Resets to start of shuffled list (same shuffle order).
     * To get a new shuffle, create a new ShuffledIterator.
     */
    @Override
    public void reset() {
        position = 0;
    }

    /**
     * Reshuffle and reset
     *
     * Shuffles the list again (different random order) and resets position.
     * Useful for "shuffle again" feature.
     */
    public void reshuffle() {
        Collections.shuffle(videos);
        position = 0;
    }
}
