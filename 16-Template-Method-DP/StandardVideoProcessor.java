/**
 * StandardVideoProcessor - Concrete implementation for standard videos
 *
 * Processing for regular content creator uploads:
 * - Target resolution: 1080p
 * - Encoding: H.264
 * - Compression: Standard quality (CRF 23)
 * - Watermark: Bottom-right corner, semi-transparent
 *
 * Use case: Regular YouTube-style video uploads
 * Target audience: General content creators
 */
public class StandardVideoProcessor extends VideoProcessor {

	/**
	 * Step 3: Encode video to 1080p H.264
	 * Required by abstract base class
	 */
	@Override
	protected void encode() {
		System.out.println("  → Transcoding to 1080p H.264...");
		System.out.println("  → Progress: ████████████ 100%");
	}

	/**
	 * Step 4: Apply standard compression
	 * Required by abstract base class
	 */
	@Override
	protected void optimize() {
		System.out.println("  → Applying standard compression (CRF 23)...");
		System.out.println("  → Size reduced: 2.5GB → 850MB (66% savings)");
	}

	/**
	 * Step 5: Add standard watermark (hook override)
	 * Optional override - customizes watermark placement
	 */
	@Override
	protected void addWatermark() {
		System.out.println("  → Adding standard watermark (bottom-right)...");
	}

	// ========================================
	// METADATA OVERRIDES
	// Provide specific information for standard videos
	// ========================================

	@Override
	protected String getVideoType() {
		return "Standard video";
	}

	@Override
	protected String getFileSize() {
		return "2.5 GB";
	}

	@Override
	protected String getDuration() {
		return "10:35";
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
	protected String getUserEmail() {
		return "user@example.com";
	}
}
