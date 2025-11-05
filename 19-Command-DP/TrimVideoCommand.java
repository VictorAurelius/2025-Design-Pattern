/**
 * TrimVideoCommand - Concrete command for trimming video duration
 *
 * Encapsulates the operation of cutting video to specified start/end times.
 * Useful for removing unwanted footage from beginning or end.
 *
 * Undo Strategy:
 * - Stores previous duration before trimming
 * - Undo restores original duration
 *
 * Example:
 *   Before: Duration = "5:00"
 *   Execute: Trim to "0:05" - "4:50" = "4:45"
 *   After: Duration = "4:45"
 *   Undo: Restore "5:00"
 */
public class TrimVideoCommand implements Command {

	private VideoClip video;          // Receiver
	private String startTime;          // Parameter: trim start time (MM:SS)
	private String endTime;            // Parameter: trim end time (MM:SS)
	private String newDuration;        // Calculated new duration
	private String previousDuration;   // For undo: previous duration

	/**
	 * Create command to trim video
	 *
	 * @param video VideoClip to trim
	 * @param startTime Start time in MM:SS format (e.g., "0:05")
	 * @param endTime End time in MM:SS format (e.g., "4:50")
	 */
	public TrimVideoCommand(VideoClip video, String startTime, String endTime) {
		this.video = video;
		this.startTime = startTime;
		this.endTime = endTime;
		this.newDuration = calculateDuration(startTime, endTime);
	}

	/**
	 * Calculate duration from start and end times
	 * Simplified calculation (assumes MM:SS format)
	 *
	 * @param start Start time "MM:SS"
	 * @param end End time "MM:SS"
	 * @return Duration "MM:SS"
	 */
	private String calculateDuration(String start, String end) {
		// Parse start time
		String[] startParts = start.split(":");
		int startMinutes = Integer.parseInt(startParts[0]);
		int startSeconds = Integer.parseInt(startParts[1]);
		int startTotalSeconds = startMinutes * 60 + startSeconds;

		// Parse end time
		String[] endParts = end.split(":");
		int endMinutes = Integer.parseInt(endParts[0]);
		int endSeconds = Integer.parseInt(endParts[1]);
		int endTotalSeconds = endMinutes * 60 + endSeconds;

		// Calculate duration
		int durationSeconds = endTotalSeconds - startTotalSeconds;
		int minutes = durationSeconds / 60;
		int seconds = durationSeconds % 60;

		return String.format("%d:%02d", minutes, seconds);
	}

	@Override
	public void execute() {
		// Save current state for undo
		previousDuration = video.getDuration();

		// Apply new duration
		video.setDuration(newDuration);

		// Feedback
		System.out.println("Trimming video...");
		System.out.println("  Trim range: " + startTime + " to " + endTime);
		System.out.println("  Previous duration: " + previousDuration);
		System.out.println("  New duration: " + newDuration);
	}

	@Override
	public void undo() {
		// Restore previous duration
		video.setDuration(previousDuration);
		System.out.println("  Duration restored: " + newDuration + " → " + previousDuration);
	}

	@Override
	public String getDescription() {
		return "Trim Video (" + startTime + " - " + endTime + ") → " + newDuration;
	}
}
