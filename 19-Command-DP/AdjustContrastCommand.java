/**
 * AdjustContrastCommand - Concrete command for contrast adjustment
 *
 * Encapsulates the operation of adjusting video contrast.
 * Adjustment can be positive (higher contrast) or negative (lower contrast).
 *
 * Undo Strategy:
 * - Stores previous contrast value
 * - Undo restores previous contrast
 *
 * Example:
 *   Before: Contrast = 100
 *   Execute: Adjust -10
 *   After: Contrast = 90
 *   Undo: Restore 100
 */
public class AdjustContrastCommand implements Command {

	private VideoClip video;           // Receiver
	private int adjustment;             // Parameter: how much to adjust (+/-)
	private int previousContrast;       // For undo: previous contrast

	/**
	 * Create command to adjust contrast
	 *
	 * @param video VideoClip to adjust
	 * @param adjustment Amount to adjust (-100 to +100)
	 */
	public AdjustContrastCommand(VideoClip video, int adjustment) {
		this.video = video;
		this.adjustment = adjustment;
	}

	@Override
	public void execute() {
		// Save current state for undo
		previousContrast = video.getContrast();

		// Calculate new contrast
		int newContrast = previousContrast + adjustment;

		// Apply new contrast
		video.setContrast(newContrast);

		// Feedback
		System.out.println("Adjusting contrast " +
		                   (adjustment >= 0 ? "+" : "") + adjustment + "...");
		System.out.println("  Previous contrast: " + previousContrast);
		System.out.println("  New contrast: " + video.getContrast());
	}

	@Override
	public void undo() {
		// Restore previous contrast
		video.setContrast(previousContrast);
		System.out.println("  Contrast restored: " + video.getContrast() +
		                   " → " + previousContrast);
	}

	@Override
	public String getDescription() {
		return "Adjust Contrast (" + (adjustment >= 0 ? "+" : "") + adjustment + ")";
	}
}
