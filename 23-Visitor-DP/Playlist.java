import java.util.ArrayList;
import java.util.List;

/**
 * Playlist - Concrete Element (Composite)
 *
 * Represents a playlist containing multiple videos.
 * This is a composite element that can contain other elements.
 *
 * Properties:
 * - id: Unique identifier
 * - name: Playlist name
 * - videos: List of videos in this playlist
 *
 * Composite Pattern Integration:
 * - Playlist contains VideoFile elements
 * - When accepting visitor, can traverse all videos
 * - Enables operations on entire playlist
 *
 * Key Design:
 * - NO export methods for playlist
 * - NO analytics calculations
 * - ONLY accept() method to enable visitor operations
 * - Visitors handle how to process the playlist
 *
 * StreamFlix Context:
 * Playlists can be exported (with all videos), analyzed for total
 * duration/views, and validated for consistency - all through visitors.
 */
public class Playlist implements VideoElement {

    private int id;
    private String name;
    private List<VideoFile> videos;

    /**
     * Constructor
     */
    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.videos = new ArrayList<>();
    }

    /**
     * Add a video to this playlist
     *
     * @param video Video to add
     */
    public void addVideo(VideoFile video) {
        videos.add(video);
    }

    /**
     * Accept a visitor to perform operations
     *
     * Two strategies for composite elements:
     * 1. Visit only the playlist (visitor decides whether to traverse)
     * 2. Visit playlist AND all videos (automatic traversal)
     *
     * We use strategy #1: Let visitor control traversal.
     * Visitor can call getVideos() and traverse if needed.
     *
     * @param visitor The visitor performing the operation
     */
    @Override
    public void accept(VideoVisitor visitor) {
        visitor.visit(this);
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<VideoFile> getVideos() {
        return videos;
    }

    public int getVideoCount() {
        return videos.size();
    }

    @Override
    public String toString() {
        return String.format("Playlist: \"%s\" (%d videos)", name, videos.size());
    }
}
