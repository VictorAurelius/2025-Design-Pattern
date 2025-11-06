/**
 * VideoIterator - Iterator Interface
 *
 * Defines the contract for iterating through video collections.
 * All concrete iterators must implement this interface.
 *
 * This is the core of the Iterator pattern - it provides a uniform
 * way to traverse different collection types without exposing their
 * internal structure.
 *
 * Key Methods:
 * - hasNext(): Check if more elements available
 * - next(): Get next element and advance position
 * - currentIndex(): Get current position (optional)
 * - reset(): Restart iteration from beginning (optional)
 *
 * StreamFlix Context:
 * Different collections (array-based playlists, linked-list history,
 * tree-based recommendations) all use this same interface for iteration.
 * Client code can iterate through any collection without knowing its
 * internal structure.
 *
 * Design Pattern:
 * This is the "Iterator" component in the Iterator pattern.
 * Concrete implementations: ArrayVideoIterator, LinkedListVideoIterator, etc.
 */
public interface VideoIterator {

    /**
     * Check if there are more videos to iterate
     *
     * This method should be called before next() to avoid exceptions.
     * It does not advance the iterator position.
     *
     * @return true if more videos available, false if end of collection
     */
    boolean hasNext();

    /**
     * Get the next video and advance the iterator position
     *
     * This method advances the internal position counter/pointer.
     * Should throw exception if called when hasNext() is false.
     *
     * @return Next video in the collection
     * @throws java.util.NoSuchElementException if no more videos
     */
    Video next();

    /**
     * Get current position in iteration (optional method)
     *
     * Useful for displaying progress (e.g., "Video 5 of 10").
     * Not all iterators can provide meaningful index (e.g., tree iterator).
     *
     * @return Current position/index, or -1 if not applicable
     */
    int currentIndex();

    /**
     * Reset iterator to beginning (optional method)
     *
     * Allows reusing the same iterator for multiple passes.
     * Not all iterators support reset (e.g., one-time iterators).
     */
    void reset();
}
