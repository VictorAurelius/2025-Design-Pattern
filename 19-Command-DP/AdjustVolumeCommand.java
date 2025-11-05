/**
 * AdjustVolumeCommand - Concrete command for audio volume adjustment
 *
 * Encapsulates the operation of adjusting video audio volume.
 * Volume can be 0% (mute) to 200% (amplified).
 *
 * Undo Strategy:
 * - Stores previous volume value
 * - Undo restores previous volume
 *
 * Example:
 *   Before: Volume = 100%
 *   Execute: Set to 120%
 *   After: Volume = 120%
 *   Undo: Restore 100%
 */
public class AdjustVolumeCommand implements Command {

	private VideoClip video;        // Receiver
	private int newVolume;           // Parameter: new volume percentage
	private int previousVolume;      // For undo: previous volume

	/**
	 * Create command to adjust volume
	 *
	 * @param video VideoClip to adjust
	 * @param newVolume New volume percentage (0-200%, 100 = normal)
	 */
	public AdjustVolumeCommand(VideoClip video, int newVolume) {
		this.video = video;
		this.newVolume = newVolume;
	}

	@Override
	public void execute() {
		// Save current state for undo
		previousVolume = video.getVolume();

		// Apply new volume
		video.setVolume(newVolume);

		// Feedback
		System.out.println("Adjusting volume...");
		System.out.println("  Previous volume: " + previousVolume + "%");
		System.out.println("  New volume: " + newVolume + "%");

		if (newVolume == 0) {
			System.out.println("  (Muted)");
		} else if (newVolume > 100) {
			System.out.println("  (Amplified)");
		}
	}

	@Override
	public void undo() {
		// Restore previous volume
		video.setVolume(previousVolume);
		System.out.println("  Volume restored: " + newVolume + "% → " + previousVolume + "%");
	}

	@Override
	public String getDescription() {
		return "Adjust Volume (" + newVolume + "%)";
	}
}
