/**
 * Video - Element class
 *
 * Represents a video in the StreamFlix platform.
 * This is the element that iterators will traverse over.
 *
 * Properties:
 * - id: Unique identifier
 * - title: Video title
 * - duration: Duration in minutes
 * - watched: Whether user has watched this video
 * - category: Video category (tutorial, review, vlog, etc.)
 *
 * StreamFlix Context:
 * Videos are stored in various collections (playlists, watch history,
 * recommendations) and need to be iterated through in different ways.
 */
public class Video {

    private int id;
    private String title;
    private int duration;        // in minutes
    private boolean watched;
    private String category;

    /**
     * Constructor
     *
     * @param id Video ID
     * @param title Video title
     * @param duration Duration in minutes
     * @param watched Whether video has been watched
     * @param category Video category
     */
    public Video(int id, String title, int duration, boolean watched, String category) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.watched = watched;
        this.category = category;
    }

    /**
     * Simplified constructor for quick video creation
     */
    public Video(int id, String title) {
        this(id, title, 10, false, "general");
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        String watchedStatus = watched ? "[WATCHED]" : "[UNWATCHED]";
        return String.format("#%d: %s (%d min, %s) %s",
                             id, title, duration, category, watchedStatus);
    }
}
