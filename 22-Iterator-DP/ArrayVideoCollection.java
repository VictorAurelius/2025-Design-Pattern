/**
 * ArrayVideoCollection - Concrete Aggregate (Array-based)
 *
 * Implements VideoCollection using an internal array for storage.
 *
 * Characteristics:
 * - Fast random access by index: O(1)
 * - Fixed capacity (can be made dynamic)
 * - Sequential iteration efficient
 * - Insertion at end: O(1) if space available
 *
 * Use Cases:
 * - Playlists (fixed or known size)
 * - Curated collections
 * - "Watch Later" lists
 * - Trending videos (top 100)
 *
 * StreamFlix Context:
 * Most playlists use array storage because:
 * - Fast access for "jump to video #5"
 * - Predictable memory usage
 * - Good cache locality for iteration
 *
 * Iterator Pattern:
 * This is a "Concrete Aggregate" that creates ArrayVideoIterator
 * instances. The iterator encapsulates traversal logic while this
 * class focuses on storage management.
 */
public class ArrayVideoCollection implements VideoCollection {

    private Video[] videos;
    private int count;           // Number of videos currently stored
    private String name;

    /**
     * Constructor with specified capacity
     *
     * @param capacity Maximum number of videos this collection can hold
     * @param name Collection name (e.g., "Watch Later", "Favorites")
     */
    public ArrayVideoCollection(int capacity, String name) {
        this.videos = new Video[capacity];
        this.count = 0;
        this.name = name;
    }

    /**
     * Default constructor with capacity 100
     */
    public ArrayVideoCollection(String name) {
        this(100, name);
    }

    /**
     * Factory method to create an iterator for this collection
     *
     * Returns ArrayVideoIterator which knows how to traverse
     * the internal array structure.
     *
     * Key Design:
     * - Returns VideoIterator interface (not concrete class)
     * - Client doesn't know it's an ArrayVideoIterator
     * - Can change iterator implementation without affecting client
     *
     * @return Iterator for this array-based collection
     */
    @Override
    public VideoIterator createIterator() {
        return new ArrayVideoIterator(videos, count);
    }

    /**
     * Add a video to the collection
     *
     * Adds to next available position in array.
     * If array is full, video is not added (could be extended to resize).
     *
     * @param video Video to add
     */
    @Override
    public void add(Video video) {
        if (count < videos.length) {
            videos[count] = video;
            count++;
        } else {
            System.err.println("Collection is full. Cannot add: " + video.getTitle());
        }
    }

    /**
     * Get number of videos in collection
     *
     * @return Count of videos (not array capacity)
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
     * Get capacity (maximum size)
     *
     * @return Array capacity
     */
    public int getCapacity() {
        return videos.length;
    }
}
