/**
 * Memento class - stores VideoEditor state snapshot
 *
 * CRITICAL: Package-private access (no public modifier!)
 * - Only classes in same package (VideoEditor) can access internals
 * - EditorHistory can store mementos but CANNOT peek inside
 * - This preserves encapsulation!
 *
 * Immutable design:
 * - All fields are final
 * - No setters provided
 * - State cannot be modified after creation
 */
class EditorMemento {

	// Immutable state fields
	private final String videoId;
	private final int startTime;
	private final int endTime;
	private final int volume;
	private final String filter;
	private final String watermark;

	/**
	 * Package-private constructor
	 * Only VideoEditor (same package) can create mementos
	 *
	 * @param videoId Video identifier
	 * @param startTime Start time in seconds
	 * @param endTime End time in seconds
	 * @param volume Volume level (0-100)
	 * @param filter Applied filter (none, sepia, grayscale, etc.)
	 * @param watermark Watermark text
	 */
	EditorMemento(String videoId, int startTime, int endTime,
	              int volume, String filter, String watermark) {
		this.videoId = videoId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.volume = volume;
		this.filter = filter;
		this.watermark = watermark;
	}

	// Package-private getters - only VideoEditor can access

	String getVideoId() {
		return videoId;
	}

	int getStartTime() {
		return startTime;
	}

	int getEndTime() {
		return endTime;
	}

	int getVolume() {
		return volume;
	}

	String getFilter() {
		return filter;
	}

	String getWatermark() {
		return watermark;
	}

	/**
	 * Display memento info (for debugging)
	 * Public method safe to expose (doesn't reveal internal state values)
	 */
	@Override
	public String toString() {
		return "EditorMemento[" + videoId + "]";
	}
}
