/**
 * LinkedListVideoCollection - Concrete Aggregate (Linked List-based)
 *
 * Implements VideoCollection using a linked list for storage.
 *
 * Characteristics:
 * - Efficient insertion: O(1) at end (with tail pointer)
 * - Sequential access only: O(n) for random access
 * - Dynamic size (no fixed capacity)
 * - Memory overhead: Extra pointer per element
 *
 * Use Cases:
 * - Watch history (chronological order, frequent appends)
 * - Video queue (FIFO operations)
 * - Recently watched (dynamic, no size limit)
 *
 * StreamFlix Context:
 * Watch history uses linked list because:
 * - Unlimited growth (no capacity limit)
 * - Always append to end (recent videos)
 * - Sequential iteration is sufficient
 * - No need for random access
 *
 * Iterator Pattern:
 * This is a "Concrete Aggregate" that creates LinkedListVideoIterator
 * instances. Despite different internal structure from ArrayVideoCollection,
 * both provide the same VideoIterator interface for traversal.
 */
public class LinkedListVideoCollection implements VideoCollection {

    private VideoNode head;      // First node in list
    private VideoNode tail;      // Last node in list (for efficient append)
    private int count;           // Number of videos in list
    private String name;

    /**
     * Constructor
     *
     * @param name Collection name (e.g., "Watch History", "Queue")
     */
    public LinkedListVideoCollection(String name) {
        this.head = null;
        this.tail = null;
        this.count = 0;
        this.name = name;
    }

    /**
     * Factory method to create an iterator for this collection
     *
     * Returns LinkedListVideoIterator which knows how to traverse
     * the linked node structure.
     *
     * Key Design:
     * - Returns VideoIterator interface (client doesn't know concrete type)
     * - Same interface as ArrayVideoIterator (polymorphism!)
     * - Client code works identically for both collection types
     *
     * @return Iterator for this linked list-based collection
     */
    @Override
    public VideoIterator createIterator() {
        return new LinkedListVideoIterator(head);
    }

    /**
     * Add a video to the collection
     *
     * Appends to end of list (O(1) with tail pointer).
     *
     * @param video Video to add
     */
    @Override
    public void add(Video video) {
        VideoNode newNode = new VideoNode(video);

        if (head == null) {
            // First node
            head = newNode;
            tail = newNode;
        } else {
            // Append to end
            tail.setNext(newNode);
            tail = newNode;
        }

        count++;
    }

    /**
     * Get number of videos in collection
     *
     * @return Count of videos
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Get collection name
     *
     * @return Collection name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get head node (for iterator creation)
     *
     * Package-private - only LinkedListVideoIterator should use this.
     *
     * @return Head node
     */
    VideoNode getHead() {
        return head;
    }
}
