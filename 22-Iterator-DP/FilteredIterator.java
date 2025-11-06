import java.util.NoSuchElementException;

/**
 * FilteredIterator - Bonus Iterator (conditional traversal)
 *
 * Wraps another iterator and filters elements based on a predicate.
 *
 * Traversal Strategy:
 * - Delegates to wrapped iterator
 * - Skips elements that don't match filter
 * - Only returns elements matching filter criteria
 *
 * Use Cases:
 * - Show only unwatched videos
 * - Show only specific category
 * - Show videos meeting duration criteria
 * - Combine multiple filters (chain FilteredIterators)
 *
 * StreamFlix Context:
 * Users frequently want filtered views of collections:
 * - Playlist: "Show only unwatched"
 * - History: "Show only tutorials"
 * - Recommendations: "Show only videos > 30 min"
 *
 * Key Design:
 * - Decorator pattern: Wraps another iterator
 * - Strategy pattern: Filter is pluggable
 * - Lazy evaluation: Filters during iteration (not in advance)
 * - Same VideoIterator interface (polymorphic)
 *
 * Example:
 * VideoIterator allVideos = playlist.createIterator();
 * VideoIterator unwatchedOnly = new FilteredIterator(allVideos,
 *     video -> !video.isWatched());
 *
 * while (unwatchedOnly.hasNext()) {
 *     Video video = unwatchedOnly.next(); // Only unwatched videos
 *     display(video);
 * }
 */
public class FilteredIterator implements VideoIterator {

    private VideoIterator wrappedIterator;  // Underlying iterator
    private VideoFilter filter;              // Filter predicate
    private Video nextVideo;                 // Next video that matches filter (cached)
    private int position;                    // Position counter

    /**
     * Constructor
     *
     * @param iterator Iterator to wrap (provides videos)
     * @param filter Filter predicate (determines which videos to include)
     */
    public FilteredIterator(VideoIterator iterator, VideoFilter filter) {
        this.wrappedIterator = iterator;
        this.filter = filter;
        this.position = 0;
        this.nextVideo = null;

        // Pre-fetch first matching video
        advanceToNextMatch();
    }

    /**
     * Advance to next video that matches filter
     *
     * Searches through wrapped iterator until finding a matching video.
     * Sets nextVideo to the match (or null if no more matches).
     */
    private void advanceToNextMatch() {
        nextVideo = null;

        while (wrappedIterator.hasNext()) {
            Video video = wrappedIterator.next();
            if (filter.matches(video)) {
                nextVideo = video;
                return;
            }
        }
    }

    /**
     * Check if more videos are available
     *
     * Returns true if we have a cached next video that matches filter.
     *
     * @return true if more matching videos available
     */
    @Override
    public boolean hasNext() {
        return nextVideo != null;
    }

    /**
     * Get next matching video
     *
     * Returns cached next video and advances to find next match.
     *
     * @return Next video that matches filter
     * @throws NoSuchElementException if no more matching videos
     */
    @Override
    public Video next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more videos matching filter");
        }

        Video result = nextVideo;
        position++;
        advanceToNextMatch(); // Find next match for subsequent call

        return result;
    }

    /**
     * Get current position in filtered iteration
     *
     * Note: This is position in filtered results, not original collection.
     * Example: If we've returned 5 filtered videos, position is 5,
     * even if we've skipped 20 non-matching videos.
     *
     * @return Current position in filtered results
     */
    @Override
    public int currentIndex() {
        return position;
    }

    /**
     * Reset iterator to beginning
     *
     * Note: Only works if wrapped iterator supports reset.
     */
    @Override
    public void reset() {
        wrappedIterator.reset();
        position = 0;
        nextVideo = null;
        advanceToNextMatch();
    }
}
