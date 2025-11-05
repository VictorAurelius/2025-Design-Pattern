/**
 * VideoProcessor - Abstract base class for video processing pipeline
 *
 * Implements the Template Method pattern to define a consistent
 * 7-step video processing workflow while allowing subclasses to
 * customize specific encoding and optimization steps.
 *
 * TEMPLATE METHOD PATTERN KEY CONCEPTS:
 * 1. Template Method (final) - Defines algorithm skeleton
 * 2. Abstract Methods - Subclasses MUST implement
 * 3. Hook Methods - Subclasses CAN override
 * 4. Common Methods - Shared implementation
 *
 * HOLLYWOOD PRINCIPLE:
 * "Don't call us, we'll call you"
 * - Base class controls workflow
 * - Subclasses provide implementations
 * - Not vice versa
 */
public abstract class VideoProcessor {

	private String videoId;

	/**
	 * TEMPLATE METHOD (FINAL - Cannot be overridden)
	 *
	 * Defines the invariant 7-step processing pipeline:
	 * 1. Validate - Check format and size
	 * 2. Preprocess - Extract metadata, generate thumbnail
	 * 3. Encode - Transcode video (VARIABLE - subclass implements)
	 * 4. Optimize - Compress and optimize (VARIABLE - subclass implements)
	 * 5. Watermark - Add branding (HOOK - optional override)
	 * 6. Save - Upload to CDN
	 * 7. Notify - Notify user
	 *
	 * @param videoId Video identifier to process
	 */
	public final void processVideo(String videoId) {
		this.videoId = videoId;

		System.out.println("\n[STEP 1: VALIDATE]");
		validate();

		System.out.println("\n[STEP 2: PREPROCESS]");
		preprocess();

		System.out.println("\n[STEP 3: ENCODE]");
		encode();  // Abstract - subclass implements

		System.out.println("\n[STEP 4: OPTIMIZE]");
		optimize();  // Abstract - subclass implements

		System.out.println("\n[STEP 5: WATERMARK]");
		if (shouldAddWatermark()) {  // Hook - can override
			addWatermark();  // Hook - can override
		} else {
			System.out.println("  ⊘ Watermark skipped (disabled for this video type)");
		}

		System.out.println("\n[STEP 6: SAVE]");
		save();

		System.out.println("\n[STEP 7: NOTIFY]");
		notify();

		System.out.println("\n✅ " + getVideoType() + " processed successfully!");
	}

	// ========================================
	// COMMON METHODS (Implemented in base class)
	// Same for all video types
	// ========================================

	/**
	 * Step 1: Validate video format and size
	 * Common behavior for all video types
	 */
	private void validate() {
		System.out.println("✓ Validating video format and size...");
		System.out.println("✓ Format: MP4 (supported)");
		System.out.println("✓ Size: " + getFileSize() + " (within limit)");
	}

	/**
	 * Step 2: Preprocess - Extract metadata and generate thumbnail
	 * Common behavior for all video types
	 */
	private void preprocess() {
		System.out.println("✓ Extracting metadata...");
		System.out.println("  → Duration: " + getDuration());
		System.out.println("  → Resolution: " + getResolution());
		System.out.println("  → Framerate: " + getFramerate());
		System.out.println("  → Codec: " + getCodec());

		// Additional metadata for specific types
		String additionalInfo = getAdditionalMetadata();
		if (!additionalInfo.isEmpty()) {
			System.out.println(additionalInfo);
		}

		System.out.println("✓ Generating thumbnail...");
		System.out.println("  → Thumbnail saved: thumb_" + videoId + ".jpg");
	}

	/**
	 * Step 6: Save - Upload to CDN and generate playlist
	 * Common behavior for all video types
	 */
	private void save() {
		System.out.println("✓ Uploading to CDN...");
		System.out.println("  → CDN URL: https://cdn.streamflix.com/" + videoId + ".mp4");
		System.out.println("✓ Generating HLS playlist" + getPlaylistInfo() + "...");
	}

	/**
	 * Step 7: Notify - Send notifications to user
	 * Common behavior for all video types
	 */
	private void notify() {
		System.out.println("✓ Notifying user...");
		System.out.println("  → Email sent to " + getUserEmail());
		System.out.println("  → Push notification sent");
	}

	// ========================================
	// ABSTRACT METHODS (Must be implemented by subclasses)
	// Variable behavior - different for each video type
	// ========================================

	/**
	 * Step 3: Encode video
	 * Subclasses MUST implement this method
	 *
	 * StandardVideoProcessor: Transcode to 1080p H.264
	 * PremiumVideoProcessor: Transcode to 4K H.265 with HDR
	 * LiveStreamProcessor: Encode live stream with chat overlay
	 */
	protected abstract void encode();

	/**
	 * Step 4: Optimize video
	 * Subclasses MUST implement this method
	 *
	 * StandardVideoProcessor: Standard compression (CRF 23)
	 * PremiumVideoProcessor: High-quality compression (CRF 18)
	 * LiveStreamProcessor: Adaptive bitrate encoding
	 */
	protected abstract void optimize();

	// ========================================
	// HOOK METHODS (Can be overridden by subclasses)
	// Optional behavior with default implementation
	// ========================================

	/**
	 * Hook: Should watermark be added?
	 * Default: true (add watermark)
	 * Subclasses can override to disable watermark
	 *
	 * @return true if watermark should be added
	 */
	protected boolean shouldAddWatermark() {
		return true;  // Default: add watermark
	}

	/**
	 * Hook: Add watermark to video
	 * Default implementation adds generic watermark
	 * Subclasses can override for custom watermark
	 */
	protected void addWatermark() {
		System.out.println("  → Adding watermark...");
	}

	// ========================================
	// HELPER METHODS (Provide metadata)
	// Subclasses can override for specific information
	// ========================================

	/**
	 * Get video type name for display
	 * Subclasses should override to provide specific name
	 *
	 * @return Video type name
	 */
	protected String getVideoType() {
		return "Video";
	}

	/**
	 * Get file size information
	 * Subclasses can override for specific size
	 *
	 * @return File size string
	 */
	protected String getFileSize() {
		return "unknown size";
	}

	/**
	 * Get video duration
	 * Subclasses can override for specific duration
	 *
	 * @return Duration string
	 */
	protected String getDuration() {
		return "unknown";
	}

	/**
	 * Get video resolution
	 * Subclasses can override for specific resolution
	 *
	 * @return Resolution string
	 */
	protected String getResolution() {
		return "unknown";
	}

	/**
	 * Get video framerate
	 * Subclasses can override for specific framerate
	 *
	 * @return Framerate string
	 */
	protected String getFramerate() {
		return "unknown";
	}

	/**
	 * Get video codec
	 * Subclasses can override for specific codec
	 *
	 * @return Codec string
	 */
	protected String getCodec() {
		return "unknown";
	}

	/**
	 * Get additional metadata specific to video type
	 * Subclasses can override to provide extra information
	 *
	 * @return Additional metadata string (empty if none)
	 */
	protected String getAdditionalMetadata() {
		return "";
	}

	/**
	 * Get playlist information
	 * Subclasses can override for specific playlist details
	 *
	 * @return Playlist info string
	 */
	protected String getPlaylistInfo() {
		return "";
	}

	/**
	 * Get user email for notifications
	 * Subclasses can override for specific user email
	 *
	 * @return User email
	 */
	protected String getUserEmail() {
		return "user@example.com";
	}

	/**
	 * Get video ID
	 *
	 * @return Video ID
	 */
	protected String getVideoId() {
		return videoId;
	}
}
