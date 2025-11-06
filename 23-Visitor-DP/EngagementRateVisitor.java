/**
 * EngagementRateVisitor - Concrete Visitor (Calculate Engagement Rate)
 *
 * Calculates engagement rate (likes / views) across video elements.
 * Demonstrates complex analytics using visitors.
 *
 * Operation: Calculate Engagement Rate
 * - VideoFile: Calculate likes/views ratio
 * - Playlist: Calculate aggregate engagement across all videos
 * - Category: Calculate engagement recursively
 *
 * Engagement Rate:
 * - Formula: (Total Likes / Total Views) × 100
 * - Industry benchmark: 3-5% is good engagement
 * - High engagement (>5%): Content resonates with audience
 * - Low engagement (<2%): Content needs improvement
 *
 * Key Design:
 * - Accumulates both likes and views
 * - Calculates percentage at the end
 * - Provides insights and recommendations
 *
 * StreamFlix Context:
 * Engagement rate is used for:
 * - Content quality assessment
 * - Recommendation algorithm (prioritize high engagement)
 * - Creator performance dashboards
 * - A/B testing of content strategies
 * - Identifying viral content
 */
public class EngagementRateVisitor implements VideoVisitor {

    private int totalViews;
    private int totalLikes;
    private int videoCount;

    public EngagementRateVisitor() {
        this.totalViews = 0;
        this.totalLikes = 0;
        this.videoCount = 0;
    }

    /**
     * Visit VideoFile and collect engagement data
     */
    @Override
    public void visit(VideoFile video) {
        totalViews += video.getViews();
        totalLikes += video.getLikes();
        videoCount++;
    }

    /**
     * Visit Playlist and collect engagement from all videos
     */
    @Override
    public void visit(Playlist playlist) {
        for (VideoFile video : playlist.getVideos()) {
            video.accept(this);
        }
    }

    /**
     * Visit Category and collect engagement recursively
     */
    @Override
    public void visit(Category category) {
        // Collect from videos in this category
        for (VideoFile video : category.getVideos()) {
            video.accept(this);
        }

        // Recursively collect from subcategories
        for (Category subcat : category.getSubcategories()) {
            subcat.accept(this);
        }
    }

    /**
     * Get engagement rate as percentage
     *
     * @return Engagement rate (0-100)
     */
    public double getEngagementRate() {
        if (totalViews == 0) {
            return 0.0;
        }
        return ((double) totalLikes / totalViews) * 100;
    }

    /**
     * Get total views
     */
    public int getTotalViews() {
        return totalViews;
    }

    /**
     * Get total likes
     */
    public int getTotalLikes() {
        return totalLikes;
    }

    /**
     * Get number of videos analyzed
     */
    public int getVideoCount() {
        return videoCount;
    }

    /**
     * Get engagement assessment
     */
    public String getAssessment() {
        double rate = getEngagementRate();

        if (rate >= 5.0) {
            return "Excellent (High Engagement)";
        } else if (rate >= 3.0) {
            return "Good (Industry Standard)";
        } else if (rate >= 2.0) {
            return "Fair (Needs Improvement)";
        } else {
            return "Poor (Low Engagement)";
        }
    }

    /**
     * Get detailed analytics report
     */
    public String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("Engagement Analytics Report:\n");
        report.append("---------------------------\n");
        report.append(String.format("Videos Analyzed: %d\n", videoCount));
        report.append(String.format("Total Views: %,d\n", totalViews));
        report.append(String.format("Total Likes: %,d\n", totalLikes));
        report.append(String.format("Engagement Rate: %.2f%%\n", getEngagementRate()));
        report.append(String.format("Assessment: %s\n", getAssessment()));

        // Recommendations
        report.append("\nRecommendations:\n");
        double rate = getEngagementRate();
        if (rate < 2.0) {
            report.append("  - Content quality may need improvement\n");
            report.append("  - Consider audience targeting\n");
            report.append("  - Add calls-to-action for likes\n");
        } else if (rate < 3.0) {
            report.append("  - Good baseline, room for improvement\n");
            report.append("  - Test different content formats\n");
        } else {
            report.append("  - Maintain current content strategy\n");
            report.append("  - Analyze what's working well\n");
        }

        return report.toString();
    }
}
