/**
 * VideoNode - Linked List Node
 *
 * Represents a node in a linked list of videos.
 * Used internally by LinkedListVideoCollection.
 *
 * Structure:
 * - video: The video data
 * - next: Reference to next node (or null if last node)
 *
 * This is an internal implementation detail of LinkedListVideoCollection.
 * Client code should not interact with nodes directly - they use
 * VideoIterator to traverse the list.
 *
 * StreamFlix Context:
 * Used for watch history where videos are added chronologically.
 * Linked list allows efficient append operations without array resizing.
 */
public class VideoNode {

    private Video video;
    private VideoNode next;

    /**
     * Constructor
     *
     * @param video Video data for this node
     */
    public VideoNode(Video video) {
        this.video = video;
        this.next = null;
    }

    /**
     * Get the video stored in this node
     *
     * @return Video data
     */
    public Video getVideo() {
        return video;
    }

    /**
     * Set the video for this node
     *
     * @param video Video data
     */
    public void setVideo(Video video) {
        this.video = video;
    }

    /**
     * Get the next node in the list
     *
     * @return Next node, or null if this is the last node
     */
    public VideoNode getNext() {
        return next;
    }

    /**
     * Set the next node in the list
     *
     * @param next Next node
     */
    public void setNext(VideoNode next) {
        this.next = next;
    }
}
