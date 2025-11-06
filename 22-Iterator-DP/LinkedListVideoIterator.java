import java.util.NoSuchElementException;

/**
 * LinkedListVideoIterator - Concrete Iterator (for linked list-based collections)
 *
 * Implements VideoIterator for traversing linked list-based collections.
 *
 * Internal State:
 * - current: Current node being processed
 * - head: First node (for reset)
 *
 * Traversal Strategy:
 * - Sequential: Follows next pointers from head to tail
 * - Handles null nodes gracefully
 * - Supports reset to restart from head
 *
 * Key Design:
 * - Same interface as ArrayVideoIterator (polymorphism!)
 * - Client code doesn't know (or care) about internal linked structure
 * - Encapsulates node traversal logic
 *
 * StreamFlix Context:
 * Used for watch history and queue collections.
 * Provides efficient sequential iteration following next pointers.
 */
public class LinkedListVideoIterator implements VideoIterator {

    private VideoNode current;   // Current node in iteration
    private VideoNode head;      // First node (for reset)
    private int position;        // Current position (for currentIndex)

    /**
     * Constructor
     *
     * @param head First node in the linked list
     */
    public LinkedListVideoIterator(VideoNode head) {
        this.head = head;
        this.current = head;
        this.position = 0;
    }

    /**
     * Check if more videos are available
     *
     * Returns true if current node is not null.
     *
     * @return true if more videos available
     */
    @Override
    public boolean hasNext() {
        return current != null;
    }

    /**
     * Get next video and advance to next node
     *
     * Returns the video from current node and moves to next node.
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

        Video video = current.getVideo();
        current = current.getNext();
        position++;

        return video;
    }

    /**
     * Get current position in iteration
     *
     * Note: For linked list, tracking position requires counter
     * (unlike array where position is naturally the index).
     *
     * @return Current position (0-based)
     */
    @Override
    public int currentIndex() {
        return position;
    }

    /**
     * Reset iterator to beginning
     *
     * Resets current pointer back to head.
     * Allows reusing the same iterator for multiple passes.
     */
    @Override
    public void reset() {
        current = head;
        position = 0;
    }
}
