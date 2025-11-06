/**
 * AnalyticsDecorator - Concrete Decorator
 *
 * Tracks viewing metrics and user engagement data for analytics.
 *
 * Tracked Metrics:
 * - Play time, pause count, seek events
 * - Completion rate, drop-off points
 * - User engagement (interactions)
 * - Watch patterns (binge watching, etc.)
 *
 * Features:
 * - Session ID for grouping related views
 * - Adds 30 kbps bitrate overhead for analytics data transmission
 * - Real-time reporting to analytics service
 *
 * Use Cases:
 * - Business customers: Employee training compliance tracking
 * - Enterprise: Required viewing for compliance (must watch 100%)
 * - Content creators: Understand viewer engagement
 * - Platform optimization: Identify popular content
 *
 * StreamFlix Context:
 * Critical for business tier. Companies need to track:
 * - Which employees completed training
 * - How long employees spent on each module
 * - Where employees struggled (replayed sections)
 * - Compliance reporting for audits
 *
 * Example:
 * VideoStream stream = new HDVideoStream();
 * stream = new AnalyticsDecorator(stream, "employee_training_q1_2024");
 * // Output: "Playing stream + [Tracking analytics: employee_training_q1_2024]"
 */
public class AnalyticsDecorator extends StreamDecorator {

    private String sessionId; // Identifier for this viewing session

    /**
     * Constructor
     *
     * @param stream The video stream to add analytics tracking to
     * @param sessionId Unique identifier for this viewing session (for grouping analytics)
     */
    public AnalyticsDecorator(VideoStream stream, String sessionId) {
        super(stream);
        this.sessionId = sessionId;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + [Tracking analytics: " + sessionId + "]";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 30; // 30 kbps overhead for analytics data transmission
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Analytics (" + sessionId + ")";
    }
}
