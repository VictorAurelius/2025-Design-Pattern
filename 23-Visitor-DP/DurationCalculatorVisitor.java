/**
 * DurationCalculatorVisitor - Concrete Visitor (Calculate Total Duration)
 *
 * Calculates total duration across video elements.
 * Demonstrates accumulation pattern in visitors.
 *
 * Operation: Calculate Total Duration
 * - VideoFile: Add video duration
 * - Playlist: Add duration of all videos
 * - Category: Add duration recursively across entire tree
 *
 * Key Design:
 * - Accumulates total in instance variable
 * - Each visit() adds to total
 * - getTotalDuration() returns accumulated result
 *
 * StreamFlix Context:
 * Duration calculation is used for:
 * - Playlist total time display
 * - Category content volume metrics
 * - User engagement analytics (time watched vs time available)
 * - Content planning (how much content in each category)
 */
public class DurationCalculatorVisitor implements VideoVisitor {

    private int totalDuration; // in minutes

    public DurationCalculatorVisitor() {
        this.totalDuration = 0;
    }

    /**
     * Visit VideoFile and add its duration
     */
    @Override
    public void visit(VideoFile video) {
        totalDuration += video.getDuration();
    }

    /**
     * Visit Playlist and add duration of all videos
     */
    @Override
    public void visit(Playlist playlist) {
        for (VideoFile video : playlist.getVideos()) {
            video.accept(this); // Recursive visit accumulates
        }
    }

    /**
     * Visit Category and add duration recursively
     */
    @Override
    public void visit(Category category) {
        // Add duration of videos in this category
        for (VideoFile video : category.getVideos()) {
            video.accept(this);
        }

        // Recursively add duration from subcategories
        for (Category subcat : category.getSubcategories()) {
            subcat.accept(this);
        }
    }

    /**
     * Get total duration calculated
     *
     * @return Total duration in minutes
     */
    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * Get total duration formatted as hours and minutes
     *
     * @return Formatted string (e.g., "2h 30m")
     */
    public String getFormattedDuration() {
        int hours = totalDuration / 60;
        int minutes = totalDuration % 60;

        if (hours == 0) {
            return minutes + " minutes";
        } else if (minutes == 0) {
            return hours + " hours";
        } else {
            return hours + "h " + minutes + "m";
        }
    }
}
