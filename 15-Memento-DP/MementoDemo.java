public class MementoDemo {

	public static void main(String[] args) {

		System.out.println("=== Memento Pattern Demo ===\n");

		VideoEditor editor = new VideoEditor("video123.mp4");
		EditorHistory history = new EditorHistory();

		// Save initial state
		System.out.println("--- Initial State ---");
		history.save(editor);
		editor.showState();

		// Test 1: Make edits with saves
		System.out.println("\n--- Test 1: Edit Session ---");
		editor.trimVideo(10, 120);
		history.save(editor);
		editor.showState();

		editor.adjustVolume(75);
		history.save(editor);
		editor.showState();

		editor.applyFilter("sepia");
		history.save(editor);
		editor.showState();

		// Test 2: Undo operations
		System.out.println("\n--- Test 2: Undo Operations ---");
		System.out.println("Undo #1:");
		history.undo(editor);
		editor.showState();

		System.out.println("\nUndo #2:");
		history.undo(editor);
		editor.showState();

		// Test 3: Redo operations
		System.out.println("\n--- Test 3: Redo Operations ---");
		System.out.println("Redo #1:");
		history.redo(editor);
		editor.showState();

		// Test 4: New change clears redo
		System.out.println("\n--- Test 4: New Change Clears Redo ---");
		editor.applyFilter("grayscale");
		history.save(editor);
		System.out.println("Made new change - redo stack cleared");
		editor.showState();
	}
}
