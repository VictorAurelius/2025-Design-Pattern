/**
 * AddTextCommand - Concrete command for adding text overlay
 *
 * Encapsulates the operation of adding text overlay to a video.
 * Text is positioned at specified (x, y) coordinates.
 *
 * Undo Strategy:
 * - Stores index of added text overlay
 * - Undo removes the text overlay at that index
 *
 * Example:
 *   Before: 0 text overlays
 *   Execute: Add "Subscribe!" at (100, 50)
 *   After: 1 text overlay
 *   Undo: Remove text overlay (back to 0)
 */
public class AddTextCommand implements Command {

	private VideoClip video;      // Receiver
	private String text;           // Parameter: text content
	private int x;                 // Parameter: X position
	private int y;                 // Parameter: Y position
	private int overlayIndex;      // For undo: index of added overlay

	/**
	 * Create command to add text overlay
	 *
	 * @param video VideoClip to add text to
	 * @param text Text content
	 * @param x X position (pixels from left)
	 * @param y Y position (pixels from top)
	 */
	public AddTextCommand(VideoClip video, String text, int x, int y) {
		this.video = video;
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		// Store index where text will be added (for undo)
		overlayIndex = video.getTextOverlayCount();

		// Add text overlay
		video.addTextOverlay(text, x, y);

		// Feedback
		System.out.println("Added text: \"" + text + "\" at (" + x + ", " + y + ")");
	}

	@Override
	public void undo() {
		// Remove the text overlay at stored index
		video.removeTextOverlay(overlayIndex);
		System.out.println("Removed text: \"" + text + "\"");
	}

	@Override
	public String getDescription() {
		return "Add Text: \"" + text + "\" at (" + x + ", " + y + ")";
	}
}
