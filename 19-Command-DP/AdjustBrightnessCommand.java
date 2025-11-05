/**
 * AdjustBrightnessCommand - Concrete command for brightness adjustment
 *
 * Encapsulates the operation of adjusting video brightness.
 * Adjustment can be positive (brighter) or negative (darker).
 *
 * Undo Strategy:
 * - Stores previous brightness value
 * - Undo restores previous brightness
 *
 * Example:
 *   Before: Brightness = 100
 *   Execute: Adjust +20
 *   After: Brightness = 120
 *   Undo: Restore 100
 */
public class AdjustBrightnessCommand implements Command {

	private VideoClip video;           // Receiver
	private int adjustment;             // Parameter: how much to adjust (+/-)
	private int previousBrightness;     // For undo: previous brightness

	/**
	 * Create command to adjust brightness
	 *
	 * @param video VideoClip to adjust
	 * @param adjustment Amount to adjust (-100 to +100)
	 */
	public AdjustBrightnessCommand(VideoClip video, int adjustment) {
		this.video = video;
		this.adjustment = adjustment;
	}

	@Override
	public void execute() {
		// Save current state for undo
		previousBrightness = video.getBrightness();

		// Calculate new brightness
		int newBrightness = previousBrightness + adjustment;

		// Apply new brightness
		video.setBrightness(newBrightness);

		// Feedback
		System.out.println("Adjusting brightness " +
		                   (adjustment >= 0 ? "+" : "") + adjustment + "...");
		System.out.println("  Previous brightness: " + previousBrightness);
		System.out.println("  New brightness: " + video.getBrightness());
	}

	@Override
	public void undo() {
		// Restore previous brightness
		video.setBrightness(previousBrightness);
		System.out.println("  Brightness restored: " + video.getBrightness() +
		                   " → " + previousBrightness);
	}

	@Override
	public String getDescription() {
		return "Adjust Brightness (" + (adjustment >= 0 ? "+" : "") + adjustment + ")";
	}
}
