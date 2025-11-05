import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

/**
 * VideoEditor - Invoker class in Command pattern
 *
 * Manages video editing operations with full undo/redo support.
 * This is the "Waiter" in the restaurant metaphor:
 * - Takes orders (commands) from customer (client)
 * - Passes orders to chef (receiver/VideoClip)
 * - Keeps order slips for undo/redo (command history)
 *
 * Key Responsibilities:
 * - Execute commands
 * - Maintain undo stack (executed commands)
 * - Maintain redo stack (undone commands)
 * - Track operation history
 *
 * IMPORTANT: VideoEditor knows NOTHING about VideoClip!
 * All operations delegated to Command objects.
 * This is decoupling in action!
 */
public class VideoEditor {

	private VideoClip video;
	private Stack<Command> undoStack;   // Commands that can be undone
	private Stack<Command> redoStack;   // Commands that can be redone
	private List<Command> history;      // All executed commands (for display)
	private static final int MAX_HISTORY = 50;  // Prevent memory leaks

	/**
	 * Create video editor for specified video clip
	 *
	 * @param video Video clip to edit
	 */
	public VideoEditor(VideoClip video) {
		this.video = video;
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
		this.history = new ArrayList<>();
	}

	/**
	 * Execute a command
	 *
	 * Steps:
	 * 1. Execute the command (calls command.execute())
	 * 2. Push to undo stack (for future undo)
	 * 3. Clear redo stack (new command invalidates redo history)
	 * 4. Add to history (for display)
	 *
	 * @param command Command to execute
	 */
	public void executeCommand(Command command) {
		// Execute the command
		command.execute();

		// Add to undo stack
		undoStack.push(command);

		// Clear redo stack (standard behavior: new command clears redo)
		redoStack.clear();

		// Add to history
		history.add(command);

		// Limit history size to prevent memory issues
		if (history.size() > MAX_HISTORY) {
			history.remove(0);  // Remove oldest
		}

		// Confirmation message
		System.out.println("✓ Executed: " + command.getDescription());
	}

	/**
	 * Undo the last command
	 *
	 * Steps:
	 * 1. Pop command from undo stack
	 * 2. Call command.undo() to reverse operation
	 * 3. Push to redo stack (for future redo)
	 */
	public void undo() {
		if (undoStack.isEmpty()) {
			System.out.println("Nothing to undo");
			return;
		}

		// Pop from undo stack
		Command command = undoStack.pop();

		// Undo the command
		System.out.println("\n[UNDO] Undoing: " + command.getDescription());
		command.undo();

		// Push to redo stack
		redoStack.push(command);

		System.out.println("✓ Undone: " + command.getDescription());
	}

	/**
	 * Redo the last undone command
	 *
	 * Steps:
	 * 1. Pop command from redo stack
	 * 2. Call command.execute() to re-apply operation
	 * 3. Push to undo stack
	 */
	public void redo() {
		if (redoStack.isEmpty()) {
			System.out.println("Nothing to redo");
			return;
		}

		// Pop from redo stack
		Command command = redoStack.pop();

		// Re-execute the command
		System.out.println("\n[REDO] Redoing: " + command.getDescription());
		command.execute();

		// Push back to undo stack
		undoStack.push(command);

		System.out.println("✓ Redone: " + command.getDescription());
	}

	/**
	 * Undo multiple commands at once
	 *
	 * @param count Number of commands to undo
	 */
	public void undoMultiple(int count) {
		System.out.println("\n[UNDO MULTIPLE] Undoing " + count + " operations...");
		for (int i = 0; i < count && !undoStack.isEmpty(); i++) {
			undo();
		}
	}

	/**
	 * Display operation history
	 * Shows all commands executed, with current position indicator
	 */
	public void showHistory() {
		System.out.println("\nOperation History (" + history.size() + " operations):");

		if (history.isEmpty()) {
			System.out.println("  (No operations performed yet)");
			return;
		}

		// Calculate current position (undo stack size)
		int currentPosition = undoStack.size();

		for (int i = 0; i < history.size(); i++) {
			String marker = (i < currentPosition) ? "  " : "  [UNDONE]";
			String current = (i == currentPosition - 1) ? " ◄ CURRENT" : "";
			System.out.println(marker + (i + 1) + ". " +
			                   history.get(i).getDescription() + current);
		}
	}

	/**
	 * Display stack status
	 * Useful for debugging and understanding state
	 */
	public void showStackStatus() {
		System.out.println("\nStack Status:");
		System.out.println("  Undo Stack Size: " + undoStack.size() +
		                   " (can undo " + undoStack.size() + " operations)");
		System.out.println("  Redo Stack Size: " + redoStack.size() +
		                   " (can redo " + redoStack.size() + " operations)");
		System.out.println("  Total History: " + history.size() + " operations");
	}

	/**
	 * Clear all history and stacks
	 * Useful for starting fresh editing session
	 */
	public void clearHistory() {
		undoStack.clear();
		redoStack.clear();
		history.clear();
		System.out.println("✓ History cleared");
	}

	/**
	 * Check if undo is possible
	 *
	 * @return true if can undo
	 */
	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	/**
	 * Check if redo is possible
	 *
	 * @return true if can redo
	 */
	public boolean canRedo() {
		return !redoStack.isEmpty();
	}

	/**
	 * Get the video clip being edited
	 *
	 * @return VideoClip instance
	 */
	public VideoClip getVideo() {
		return video;
	}

	/**
	 * Get number of operations that can be undone
	 *
	 * @return Undo stack size
	 */
	public int getUndoCount() {
		return undoStack.size();
	}

	/**
	 * Get number of operations that can be redone
	 *
	 * @return Redo stack size
	 */
	public int getRedoCount() {
		return redoStack.size();
	}

	/**
	 * Show current video state
	 * Delegates to VideoClip.showState()
	 */
	public void showVideoState() {
		video.showState();
	}
}
