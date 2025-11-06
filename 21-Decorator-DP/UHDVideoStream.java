/**
 * UHDVideoStream - Concrete Component (4K)
 *
 * Represents an ultra-high-definition video stream (4K, 8000 kbps).
 * This is the highest quality stream, typically used for:
 * - Ultra/Premium+ tier subscribers
 * - Users with high-speed internet (fiber, 5G)
 * - Large screen viewing (4K TVs, monitors)
 * - Professional/business content
 *
 * This is a "concrete component" in the Decorator pattern.
 * It provides the best base quality available.
 *
 * StreamFlix Context:
 * Business customers and premium+ users get 4K streams.
 * This stream requires significant bandwidth and is often
 * decorated with DRM protection, watermarks, and analytics
 * for business use cases.
 */
public class UHDVideoStream implements VideoStream {

    @Override
    public String play() {
        return "Playing UHD 4K video stream";
    }

    @Override
    public int getBitrate() {
        return 8000; // 8000 kbps for 4K
    }

    @Override
    public String getDescription() {
        return "UHD Stream (4K, 8000 kbps)";
    }
}
