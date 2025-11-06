/**
 * ViewCountVisitor - Concrete Visitor (Count Total Views)
 *
 * Calculates total view count across video elements.
 * Demonstrates aggregation of metrics using visitors.
 *
 * Operation: Count Total Views
 * - VideoFile: Add video views
 * - Playlist: Add views of all videos
 * - Category: Add views recursively across entire tree
 *
 * Key Design:
 * - Accumulates total views in instance variable
 * - Each visit() adds to total
 * - Supports multiple metrics (views, unique views, watch time)
 *
 * StreamFlix Context:
 * View count calculation is used for:
 * - Playlist popularity metrics
 * - Category performance analytics
 * - Content creator dashboards
 * - Trending video identification
 * - Monetization calculations (ad revenue based on views)
 */
public class ViewCountVisitor implements VideoVisitor {

    private int totalViews;
    private int totalLikes;
    private int videoCount;

    public ViewCountVisitor() {
        this.totalViews = 0;
        this.totalLikes = 0;
        this.videoCount = 0;
    }

    /**
     * Visit VideoFile and add its views and likes
     */
    @Override
    public void visit(VideoFile video) {
        totalViews += video.getViews();
        totalLikes += video.getLikes();
        videoCount++;
    }

    /**
     * Visit Playlist and add views from all videos
     */
    @Override
    public void visit(Playlist playlist) {
        for (VideoFile video : playlist.getVideos()) {
            video.accept(this);
        }
    }

    /**
     * Visit Category and add views recursively
     */
    @Override
    public void visit(Category category) {
        // Add views from videos in this category
        for (VideoFile video : category.getVideos()) {
            video.accept(this);
        }

        // Recursively add views from subcategories
        for (Category subcat : category.getSubcategories()) {
            subcat.accept(this);
        }
    }

    /**
     * Get total views calculated
     */
    public int getTotalViews() {
        return totalViews;
    }

    /**
     * Get total likes calculated
     */
    public int getTotalLikes() {
        return totalLikes;
    }

    /**
     * Get number of videos counted
     */
    public int getVideoCount() {
        return videoCount;
    }

    /**
     * Get average views per video
     */
    public double getAverageViews() {
        return videoCount == 0 ? 0 : (double) totalViews / videoCount;
    }

    /**
     * Get formatted summary
     */
    public String getSummary() {
        return String.format("%,d total views across %d videos (avg: %.0f views/video)",
                           totalViews, videoCount, getAverageViews());
    }
}
