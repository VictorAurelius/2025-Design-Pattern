/**
 * VideoCollection - Aggregate Interface
 *
 * Defines the contract for collections that can be iterated.
 * All concrete collections must implement this interface.
 *
 * This is the "Aggregate" or "Collection" component in the Iterator pattern.
 * It provides a factory method (createIterator) to create iterators for
 * traversing the collection.
 *
 * Key Design:
 * - Factory Method: createIterator() creates appropriate iterator
 * - Encapsulation: Internal structure hidden from client
 * - Polymorphism: Client code works with interface, not concrete class
 *
 * StreamFlix Context:
 * Different collection implementations:
 * - ArrayVideoCollection: Fixed/dynamic array storage (playlists)
 * - LinkedListVideoCollection: Linked node storage (watch history)
 * - TreeVideoCollection: Hierarchical storage (category browsing)
 *
 * All provide same interface for adding videos and creating iterators,
 * but use different internal structures.
 *
 * Design Pattern:
 * This is the "Aggregate" component in the Iterator pattern.
 * Concrete implementations: ArrayVideoCollection, LinkedListVideoCollection, etc.
 */
public interface VideoCollection {

    /**
     * Create an iterator for traversing this collection
     *
     * This is a factory method that creates the appropriate iterator
     * for this collection's internal structure.
     *
     * Factory Method Pattern:
     * - ArrayVideoCollection returns ArrayVideoIterator
     * - LinkedListVideoCollection returns LinkedListVideoIterator
     * - Each collection knows which iterator to create
     *
     * @return Iterator for this collection
     */
    VideoIterator createIterator();

    /**
     * Add a video to the collection
     *
     * Implementation varies by collection type:
     * - Array: Add to next available index
     * - LinkedList: Append to end of list
     * - Tree: Insert based on category hierarchy
     *
     * @param video Video to add
     */
    void add(Video video);

    /**
     * Get the number of videos in this collection
     *
     * @return Number of videos
     */
    int size();

    /**
     * Get collection name/description
     *
     * @return Human-readable collection description
     */
    String getName();
}
