/**
 * LiveStreamProcessor - Concrete implementation for live stream recordings
 *
 * Processing for recorded live streams:
 * - Target resolution: 1080p with adaptive bitrate
 * - Encoding: Live stream encoding with chat overlay
 * - Optimization: Multi-bitrate versions (1080p, 720p, 480p, 360p)
 * - Watermark: "LIVE" badge with timestamp
 *
 * Use case: Twitch/YouTube live stream recordings
 * Target audience: Live streamers, gaming content creators
 */
public class LiveStreamProcessor extends VideoProcessor {

	/**
	 * Step 3: Encode live stream with chat overlay
	 * Required by abstract base class
	 */
	@Override
	protected void encode() {
		System.out.println("  → Encoding live stream recording...");
		System.out.println("  → Processing chat overlay...");
		System.out.println("  → Embedding 15,432 chat messages");
		System.out.println("  → Progress: ████████████ 100%");
	}

	/**
	 * Step 4: Generate adaptive bitrate versions
	 * Required by abstract base class
	 */
	@Override
	protected void optimize() {
		System.out.println("  → Optimizing for streaming...");
		System.out.println("  → Generating adaptive bitrate versions:");
		System.out.println("    • 1080p60 (6000 kbps) - source");
		System.out.println("    • 720p60 (4500 kbps)");
		System.out.println("    • 480p30 (2500 kbps)");
		System.out.println("    • 360p30 (1000 kbps)");
	}

	/**
	 * Step 5: Add LIVE badge (hook override)
	 * Optional override - customizes watermark for live streams
	 */
	@Override
	protected void addWatermark() {
		System.out.println("  → Adding 'LIVE' badge with timestamp...");
		System.out.println("  → Badge position: top-right");
	}

	// ========================================
	// METADATA OVERRIDES
	// Provide specific information for live streams
	// ========================================

	@Override
	protected String getVideoType() {
		return "Live stream";
	}

	@Override
	protected String getFileSize() {
		return "5.8 GB";
	}

	@Override
	protected String getDuration() {
		return "2:15:33 (live stream)";
	}

	@Override
	protected String getResolution() {
		return "1920x1080";
	}

	@Override
	protected String getFramerate() {
		return "60fps";
	}

	@Override
	protected String getCodec() {
		return "H.264";
	}

	@Override
	protected String getAdditionalMetadata() {
		return "  → Chat messages: 15,432";
	}

	@Override
	protected String getPlaylistInfo() {
		return " (multi-bitrate)";
	}

	@Override
	protected String getUserEmail() {
		return "streamer@example.com";
	}
}
