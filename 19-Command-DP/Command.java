/**
 * Command - Command pattern interface
 *
 * Encapsulates a request as an object, allowing:
 * - Undo/redo operations
 * - Queuing and logging of operations
 * - Macro commands (composite operations)
 *
 * Key Principle: "Don't just DO operations - ENCAPSULATE them as objects"
 *
 * This interface defines the contract for all commands in the video editor.
 * Each command encapsulates one editing operation on a video clip.
 */
public interface Command {

	/**
	 * Execute the command
	 * Performs the operation on the receiver (VideoClip)
	 * Must save previous state for undo
	 */
	void execute();

	/**
	 * Undo the command
	 * Reverses the operation, restoring previous state
	 * Must restore exactly what was changed in execute()
	 */
	void undo();

	/**
	 * Get human-readable description of this command
	 * Used for history display and debugging
	 *
	 * @return Description like "Add Grayscale filter" or "Adjust Brightness (+20)"
	 */
	String getDescription();
}
