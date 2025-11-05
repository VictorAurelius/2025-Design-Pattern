/**
 * PremiumVideoProcessor - Concrete implementation for premium videos
 *
 * Processing for high-quality professional uploads:
 * - Target resolution: 4K (3840x2160)
 * - Encoding: H.265 with HDR10 support
 * - Compression: High quality (CRF 18)
 * - Watermark: Subtle top-left corner
 *
 * Use case: Professional content creator uploads, film content
 * Target audience: Premium subscribers, professional creators
 */
public class PremiumVideoProcessor extends VideoProcessor {

	/**
	 * Step 3: Encode video to 4K H.265 with HDR
	 * Required by abstract base class
	 */
	@Override
	protected void encode() {
		System.out.println("  → Transcoding to 4K H.265...");
		System.out.println("  → Processing HDR metadata (HDR10)...");
		System.out.println("  → Color space: BT.2020");
		System.out.println("  → Progress: ████████████ 100%");
	}

	/**
	 * Step 4: Apply high-quality compression
	 * Required by abstract base class
	 */
	@Override
	protected void optimize() {
		System.out.println("  → Applying high-quality compression (CRF 18)...");
		System.out.println("  → Preserving HDR color space...");
		System.out.println("  → Size reduced: 15.3GB → 8.2GB (46% savings)");
	}

	/**
	 * Step 5: Add premium watermark (hook override)
	 * Optional override - customizes watermark for premium content
	 */
	@Override
	protected void addWatermark() {
		System.out.println("  → Adding premium watermark (subtle, top-left)...");
	}

	// ========================================
	// METADATA OVERRIDES
	// Provide specific information for premium videos
	// ========================================

	@Override
	protected String getVideoType() {
		return "Premium video";
	}

	@Override
	protected String getFileSize() {
		return "15.3 GB";
	}

	@Override
	protected String getDuration() {
		return "25:42";
	}

	@Override
	protected String getResolution() {
		return "3840x2160";
	}

	@Override
	protected String getFramerate() {
		return "60fps";
	}

	@Override
	protected String getCodec() {
		return "H.265";
	}

	@Override
	protected String getAdditionalMetadata() {
		return "  → HDR: Yes (HDR10)";
	}

	@Override
	protected String getPlaylistInfo() {
		return " (4K + HDR)";
	}

	@Override
	protected String getUserEmail() {
		return "premium@example.com";
	}
}
