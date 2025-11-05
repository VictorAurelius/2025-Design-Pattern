/**
 * AddFilterCommand - Concrete command for adding visual filter
 *
 * Encapsulates the operation of applying a filter to a video clip.
 * Supports filters: Grayscale, Sepia, Blur, Sharpen
 *
 * Undo Strategy:
 * - Stores previous filter before applying new one
 * - Undo restores previous filter
 *
 * Example:
 *   Before: Filter = "None"
 *   Execute: Apply "Grayscale"
 *   After: Filter = "Grayscale"
 *   Undo: Restore "None"
 */
public class AddFilterCommand implements Command {

	private VideoClip video;         // Receiver
	private String filterType;        // Parameter: which filter to apply
	private String previousFilter;    // For undo: previous filter state

	/**
	 * Create command to add filter
	 *
	 * @param video VideoClip to apply filter to
	 * @param filterType Filter name (Grayscale, Sepia, Blur, Sharpen)
	 */
	public AddFilterCommand(VideoClip video, String filterType) {
		this.video = video;
		this.filterType = filterType;
	}

	@Override
	public void execute() {
		// Save current state for undo
		previousFilter = video.getFilter();

		// Apply new filter
		video.applyFilter(filterType);

		// Feedback
		System.out.println("Applying " + filterType + " filter...");
		System.out.println("  Previous filter: " + previousFilter);
		System.out.println("  New filter: " + filterType);
	}

	@Override
	public void undo() {
		// Restore previous filter
		if (previousFilter.equals("None")) {
			video.removeFilter();
			System.out.println("  Filter removed: " + filterType + " → None");
		} else {
			video.applyFilter(previousFilter);
			System.out.println("  Filter restored: " + filterType + " → " + previousFilter);
		}
	}

	@Override
	public String getDescription() {
		return "Add " + filterType + " filter";
	}
}
