import java.util.ArrayList;
import java.util.List;

/**
 * VideoClip - Receiver class in Command pattern
 *
 * Represents a video file with various editable properties.
 * This class knows HOW to perform video editing operations,
 * but doesn't know WHEN they're called (that's Command's job).
 *
 * The VideoClip is the "Chef" in the restaurant metaphor:
 * - Knows how to prepare food (edit video)
 * - Receives orders (commands) from waiter (invoker)
 * - Doesn't care who ordered or how to undo
 *
 * Properties:
 * - Brightness, contrast, filter, volume
 * - Text overlays
 * - Duration (for trimming)
 */
public class VideoClip {

	private String filename;
	private String duration;           // Format: "MM:SS"
	private int brightness;            // 0-200, default 100
	private int contrast;              // 0-200, default 100
	private String filter;             // "None", "Grayscale", "Sepia", etc.
	private List<String> textOverlays; // Text overlays with positions
	private int volume;                // 0-200%, default 100

	/**
	 * Create video clip with default values
	 *
	 * @param filename Name of video file
	 * @param duration Duration in MM:SS format
	 */
	public VideoClip(String filename, String duration) {
		this.filename = filename;
		this.duration = duration;
		this.brightness = 100;
		this.contrast = 100;
		this.filter = "None";
		this.textOverlays = new ArrayList<>();
		this.volume = 100;
	}

	// ============================================================
	// FILTER OPERATIONS
	// ============================================================

	/**
	 * Apply visual filter to video
	 *
	 * @param filterType Filter name (Grayscale, Sepia, Blur, Sharpen)
	 */
	public void applyFilter(String filterType) {
		this.filter = filterType;
	}

	/**
	 * Remove current filter
	 */
	public void removeFilter() {
		this.filter = "None";
	}

	/**
	 * Get current filter
	 *
	 * @return Current filter name
	 */
	public String getFilter() {
		return filter;
	}

	// ============================================================
	// BRIGHTNESS OPERATIONS
	// ============================================================

	/**
	 * Set brightness value
	 *
	 * @param value Brightness (0-200, 100 = normal)
	 */
	public void setBrightness(int value) {
		// Clamp to valid range
		if (value < 0) value = 0;
		if (value > 200) value = 200;
		this.brightness = value;
	}

	/**
	 * Get current brightness
	 *
	 * @return Current brightness value
	 */
	public int getBrightness() {
		return brightness;
	}

	// ============================================================
	// CONTRAST OPERATIONS
	// ============================================================

	/**
	 * Set contrast value
	 *
	 * @param value Contrast (0-200, 100 = normal)
	 */
	public void setContrast(int value) {
		// Clamp to valid range
		if (value < 0) value = 0;
		if (value > 200) value = 200;
		this.contrast = value;
	}

	/**
	 * Get current contrast
	 *
	 * @return Current contrast value
	 */
	public int getContrast() {
		return contrast;
	}

	// ============================================================
	// TEXT OVERLAY OPERATIONS
	// ============================================================

	/**
	 * Add text overlay to video
	 *
	 * @param text Text content
	 * @param x X position
	 * @param y Y position
	 */
	public void addTextOverlay(String text, int x, int y) {
		String overlay = text + " at (" + x + ", " + y + ")";
		textOverlays.add(overlay);
	}

	/**
	 * Remove text overlay by index
	 *
	 * @param index Index of overlay to remove
	 */
	public void removeTextOverlay(int index) {
		if (index >= 0 && index < textOverlays.size()) {
			textOverlays.remove(index);
		}
	}

	/**
	 * Get number of text overlays
	 *
	 * @return Count of text overlays
	 */
	public int getTextOverlayCount() {
		return textOverlays.size();
	}

	/**
	 * Get all text overlays
	 *
	 * @return List of text overlays
	 */
	public List<String> getTextOverlays() {
		return new ArrayList<>(textOverlays);
	}

	// ============================================================
	// TRIM OPERATIONS
	// ============================================================

	/**
	 * Trim video to specified duration
	 *
	 * @param newDuration New duration in MM:SS format
	 */
	public void setDuration(String newDuration) {
		this.duration = newDuration;
	}

	/**
	 * Get current duration
	 *
	 * @return Duration in MM:SS format
	 */
	public String getDuration() {
		return duration;
	}

	// ============================================================
	// VOLUME OPERATIONS
	// ============================================================

	/**
	 * Set audio volume
	 *
	 * @param value Volume percentage (0-200%, 100 = normal)
	 */
	public void setVolume(int value) {
		// Clamp to valid range
		if (value < 0) value = 0;
		if (value > 200) value = 200;
		this.volume = value;
	}

	/**
	 * Get current volume
	 *
	 * @return Current volume percentage
	 */
	public int getVolume() {
		return volume;
	}

	// ============================================================
	// UTILITY METHODS
	// ============================================================

	/**
	 * Get video filename
	 *
	 * @return Filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Display current video state
	 * Used for debugging and demo output
	 */
	public void showState() {
		System.out.println("Video State:");
		System.out.println("  Filename: " + filename);
		System.out.println("  Duration: " + duration);
		System.out.println("  Brightness: " + brightness);
		System.out.println("  Contrast: " + contrast);
		System.out.println("  Filter: " + filter);
		System.out.println("  Text Overlays: " + textOverlays.size());
		if (!textOverlays.isEmpty()) {
			for (int i = 0; i < textOverlays.size(); i++) {
				System.out.println("    " + (i + 1) + ". " + textOverlays.get(i));
			}
		}
		System.out.println("  Volume: " + volume + "%");
	}

	/**
	 * Get concise state summary
	 *
	 * @return One-line state summary
	 */
	public String getStateSummary() {
		return String.format("Brightness:%d Contrast:%d Filter:%s Volume:%d%% Overlays:%d",
		                     brightness, contrast, filter, volume, textOverlays.size());
	}
}
