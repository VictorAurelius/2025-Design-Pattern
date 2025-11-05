/**
 * VideoEditor - Originator class
 *
 * Manages video editing state:
 * - Video identification
 * - Trim points (start/end time)
 * - Audio settings (volume)
 * - Visual effects (filter)
 * - Branding (watermark)
 *
 * Responsibilities:
 * - Perform edit operations (trim, adjust, apply, add)
 * - Create mementos (save())
 * - Restore from mementos (restore())
 */
public class VideoEditor {

	// Video state
	private String videoId;
	private int startTime;      // seconds
	private int endTime;        // seconds
	private int volume;         // 0-100
	private String filter;      // none, sepia, grayscale, vintage, etc.
	private String watermark;   // text or none

	/**
	 * Create new video editor
	 *
	 * @param videoId Video file identifier
	 */
	public VideoEditor(String videoId) {
		this.videoId = videoId;
		this.startTime = 0;
		this.endTime = 300;      // Default 5 minutes
		this.volume = 50;        // Default 50%
		this.filter = "none";
		this.watermark = "none";
	}

	// ========================================
	// EDIT OPERATIONS
	// ========================================

	/**
	 * Trim video to specific time range
	 *
	 * @param start Start time in seconds
	 * @param end End time in seconds
	 */
	public void trimVideo(int start, int end) {
		this.startTime = start;
		this.endTime = end;
		System.out.println("✂️  Trimmed video: " + start + "s - " + end + "s");
	}

	/**
	 * Adjust audio volume level
	 *
	 * @param level Volume level (0-100)
	 */
	public void adjustVolume(int level) {
		this.volume = Math.max(0, Math.min(100, level));
		System.out.println("🔊 Adjusted volume: " + this.volume + "%");
	}

	/**
	 * Apply visual filter
	 *
	 * @param filterName Filter to apply (sepia, grayscale, vintage, etc.)
	 */
	public void applyFilter(String filterName) {
		this.filter = filterName;
		System.out.println("🎨 Applied filter: " + filterName);
	}

	/**
	 * Add watermark text
	 *
	 * @param text Watermark text
	 */
	public void addWatermark(String text) {
		this.watermark = text;
		System.out.println("💧 Added watermark: " + text);
	}

	// ========================================
	// MEMENTO OPERATIONS
	// ========================================

	/**
	 * Save current state to memento
	 * Creates immutable snapshot of all state fields
	 *
	 * @return EditorMemento containing current state
	 */
	public EditorMemento save() {
		return new EditorMemento(
			videoId,
			startTime,
			endTime,
			volume,
			filter,
			watermark
		);
	}

	/**
	 * Restore state from memento
	 * Overwrites all state fields with memento values
	 *
	 * @param memento Memento to restore from (null-safe)
	 */
	public void restore(EditorMemento memento) {
		if (memento == null) {
			System.out.println("⚠️  Cannot restore: memento is null");
			return;
		}

		this.videoId = memento.getVideoId();
		this.startTime = memento.getStartTime();
		this.endTime = memento.getEndTime();
		this.volume = memento.getVolume();
		this.filter = memento.getFilter();
		this.watermark = memento.getWatermark();

		System.out.println("✓ Restored to previous state");
	}

	// ========================================
	// DISPLAY
	// ========================================

	/**
	 * Display current editor state
	 */
	public void showState() {
		int duration = endTime - startTime;
		int minutes = duration / 60;
		int seconds = duration % 60;

		System.out.println("\nCurrent State:");
		System.out.println("├─ Video: " + videoId);
		System.out.println("├─ Duration: " + startTime + "s - " + endTime + "s (" +
		                   minutes + ":" + String.format("%02d", seconds) + ")");
		System.out.println("├─ Volume: " + volume + "%");
		System.out.println("├─ Filter: " + filter);
		System.out.println("└─ Watermark: " + watermark);
	}

	// Getters for inspection
	public String getVideoId() { return videoId; }
	public int getStartTime() { return startTime; }
	public int getEndTime() { return endTime; }
	public int getVolume() { return volume; }
	public String getFilter() { return filter; }
	public String getWatermark() { return watermark; }
}
