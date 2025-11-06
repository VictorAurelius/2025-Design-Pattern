/**
 * AdvertisementDecorator - Concrete Decorator
 *
 * Inserts advertisements into video stream at specified intervals.
 *
 * Ad Types:
 * - pre-roll: Ad before video starts
 * - mid-roll: Ad during video (at intervals)
 * - post-roll: Ad after video ends
 *
 * Features:
 * - Supports multiple ad types (comma-separated: "pre-roll,mid-roll,post-roll")
 * - Adds 200 kbps bitrate overhead for ad content
 * - Tracks ad impressions (for analytics)
 *
 * Use Cases:
 * - Free tier users: Monetization through ads
 * - Limited free content: Single pre-roll ad
 * - Ad-supported premium: Reduced subscription cost
 *
 * StreamFlix Context:
 * Free users see ads (typically pre-roll + mid-roll).
 * This is the primary revenue stream for free tier.
 * Premium users NEVER have this decorator applied.
 *
 * Example:
 * VideoStream stream = new BasicVideoStream();
 * stream = new AdvertisementDecorator(stream, "pre-roll,mid-roll");
 * // Output: "[Pre-roll ad] Playing stream [Mid-roll ad at 10:00]"
 */
public class AdvertisementDecorator extends StreamDecorator {

    private String adType; // pre-roll, mid-roll, post-roll (comma-separated)

    /**
     * Constructor
     *
     * @param stream The video stream to add ads to
     * @param adType Ad type(s): "pre-roll", "mid-roll", "post-roll", or combinations like "pre-roll,mid-roll"
     */
    public AdvertisementDecorator(VideoStream stream, String adType) {
        super(stream);
        this.adType = adType;
    }

    @Override
    public String play() {
        StringBuilder result = new StringBuilder();

        // Pre-roll ad (before content)
        if (adType.contains("pre-roll")) {
            result.append("[Playing pre-roll ad: \"Upgrade to Premium!\"] ");
        }

        // Main content
        result.append(wrappedStream.play());

        // Mid-roll ad (during content)
        if (adType.contains("mid-roll")) {
            result.append(" [Mid-roll ad at 10:00: \"StreamFlix Premium - No Ads!\"]");
        }

        // Post-roll ad (after content)
        if (adType.contains("post-roll")) {
            result.append(" [Post-roll ad: \"Thanks for watching!\"]");
        }

        return result.toString();
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 200; // 200 kbps overhead for ad content
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Ads (" + adType + ")";
    }
}
