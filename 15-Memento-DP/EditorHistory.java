import java.util.Stack;

/**
 * EditorHistory - Caretaker class
 *
 * Manages undo/redo history using two stacks:
 * - undoStack: Contains states to undo to
 * - redoStack: Contains states to redo to
 *
 * Key behaviors:
 * - save() pushes to undo stack and CLEARS redo stack
 * - undo() pops from undo, pushes to redo
 * - redo() pops from redo, pushes to undo
 *
 * CRITICAL: This class stores EditorMemento objects but
 * CANNOT access their internals (encapsulation preserved!)
 */
public class EditorHistory {

	private Stack<EditorMemento> undoStack;
	private Stack<EditorMemento> redoStack;

	/**
	 * Create new history manager
	 */
	public EditorHistory() {
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
	}

	/**
	 * Save current editor state
	 *
	 * CRITICAL BEHAVIOR:
	 * - Pushes memento to undo stack
	 * - Clears redo stack (cannot redo after new change)
	 *
	 * @param editor VideoEditor to save
	 */
	public void save(VideoEditor editor) {
		EditorMemento memento = editor.save();
		undoStack.push(memento);
		redoStack.clear();  // New change clears redo!
		System.out.println("✓ State saved to history");
	}

	/**
	 * Undo last change
	 *
	 * Algorithm:
	 * 1. Pop current state from undo stack
	 * 2. Push it to redo stack
	 * 3. Return previous state (peek at undo stack)
	 *
	 * @return Memento to restore, or null if cannot undo
	 */
	public EditorMemento undo() {
		if (undoStack.isEmpty()) {
			System.out.println("⚠️  Cannot undo: no more history");
			return null;
		}

		// Pop current state, push to redo
		EditorMemento current = undoStack.pop();
		redoStack.push(current);

		// Return previous state (or null if we're at the beginning)
		if (undoStack.isEmpty()) {
			System.out.println("⚠️  Reached beginning of history");
			return null;
		}

		EditorMemento previous = undoStack.peek();
		return previous;
	}

	/**
	 * Redo previously undone change
	 *
	 * Algorithm:
	 * 1. Pop memento from redo stack
	 * 2. Push it to undo stack
	 * 3. Return it for restoration
	 *
	 * @return Memento to restore, or null if cannot redo
	 */
	public EditorMemento redo() {
		if (redoStack.isEmpty()) {
			System.out.println("⚠️  Cannot redo: no undone changes");
			return null;
		}

		// Pop from redo, push to undo
		EditorMemento memento = redoStack.pop();
		undoStack.push(memento);

		return memento;
	}

	/**
	 * Check if undo is available
	 *
	 * @return true if can undo
	 */
	public boolean canUndo() {
		return undoStack.size() > 1;  // Need at least 2 states (current + previous)
	}

	/**
	 * Check if redo is available
	 *
	 * @return true if can redo
	 */
	public boolean canRedo() {
		return !redoStack.isEmpty();
	}

	/**
	 * Clear all history
	 */
	public void clear() {
		undoStack.clear();
		redoStack.clear();
		System.out.println("✓ History cleared");
	}

	/**
	 * Display current history state
	 */
	public void showHistory() {
		System.out.println("\n========================================");
		System.out.println("📚 EDIT HISTORY");
		System.out.println("========================================");

		System.out.println("Undo Stack (" + undoStack.size() + " states):");
		if (undoStack.isEmpty()) {
			System.out.println("  (empty)");
		} else {
			for (int i = undoStack.size() - 1; i >= 0; i--) {
				System.out.println("  [" + i + "] " + undoStack.get(i));
			}
		}

		System.out.println("\nRedo Stack (" + redoStack.size() + " states):");
		if (redoStack.isEmpty()) {
			System.out.println("  (empty)");
		} else {
			for (int i = redoStack.size() - 1; i >= 0; i--) {
				System.out.println("  [" + i + "] " + redoStack.get(i));
			}
		}

		System.out.println("\nCan undo? " + (canUndo() ? "✓ Yes" : "✗ No"));
		System.out.println("Can redo? " + (canRedo() ? "✓ Yes" : "✗ No"));
	}

	/**
	 * Get undo stack size (for testing)
	 */
	public int getUndoSize() {
		return undoStack.size();
	}

	/**
	 * Get redo stack size (for testing)
	 */
	public int getRedoSize() {
		return redoStack.size();
	}
}
