/**
 * HDVideoStream - Concrete Component (1080p)
 *
 * Represents a high-definition video stream (1080p, 2500 kbps).
 * This is the standard premium quality stream, typically used for:
 * - Premium tier subscribers
 * - Users with broadband internet
 * - Desktop/laptop viewing
 *
 * This is a "concrete component" in the Decorator pattern.
 * It provides better base quality than BasicVideoStream.
 *
 * StreamFlix Context:
 * Premium users get HD 1080p streams without advertisements.
 * This stream is commonly decorated with subtitles, watermarks
 * (for business users), and analytics tracking.
 */
public class HDVideoStream implements VideoStream {

    @Override
    public String play() {
        return "Playing HD 1080p video stream";
    }

    @Override
    public int getBitrate() {
        return 2500; // 2500 kbps for 1080p
    }

    @Override
    public String getDescription() {
        return "HD Stream (1080p, 2500 kbps)";
    }
}
