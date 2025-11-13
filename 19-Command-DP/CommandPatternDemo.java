/**
 * CommandPatternDemo - Simple demonstration of Command pattern
 *
 * Demonstrates:
 * 1. Basic video editing with undo/redo
 * 2. Two concrete commands: AddText and AdjustBrightness
 * 3. Command pattern benefits
 *
 * Scenario: Video editor with 2 operations
 */
public class CommandPatternDemo {

	public static void main(String[] args) {
		System.out.println("COMMAND PATTERN - VIDEO EDITOR");
		System.out.println("===============================");
		System.out.println();

		// Run demonstration scenarios
		demonstrateBasicOperations();
		System.out.println();
		
		showPatternBenefits();
	}

	/**
	 * Basic operations with undo/redo
	 */
	private static void demonstrateBasicOperations() {
		System.out.println("Basic Video Editing with Undo/Redo");
		System.out.println("===================================");
		System.out.println();

		// Create video clip
		VideoClip video = new VideoClip("video.mp4", "5:30");
		VideoEditor editor = new VideoEditor(video);

		System.out.println("Video: " + video.getFilename());
		System.out.println("Initial brightness: " + video.getBrightness());
		System.out.println("Initial text overlays: " + video.getTextOverlayCount());
		System.out.println();

		// Operation 1: Add text
		System.out.println("[Operation 1] Add Text");
		Command addText = new AddTextCommand(video, "Hello World", 100, 50);
		editor.executeCommand(addText);
		System.out.println();

		// Operation 2: Adjust brightness
		System.out.println("[Operation 2] Adjust Brightness (+20)");
		Command adjustBrightness = new AdjustBrightnessCommand(video, 20);
		editor.executeCommand(adjustBrightness);
		System.out.println();

		System.out.println("Current State:");
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Text overlays: " + video.getTextOverlayCount());
		System.out.println();

		// Demonstrate undo
		System.out.println("UNDO OPERATIONS");
		editor.undo();  // Undo brightness
		System.out.println();
		editor.undo();  // Undo text
		System.out.println();

		System.out.println("After Undo:");
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Text overlays: " + video.getTextOverlayCount());
		System.out.println();

		// Demonstrate redo
		System.out.println("REDO OPERATIONS");
		editor.redo();  // Redo text
		System.out.println();

		System.out.println("After Redo:");
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Text overlays: " + video.getTextOverlayCount());
	}

	/**
	 * Show benefits of Command pattern
	 */
	private static void showPatternBenefits() {
		System.out.println("COMMAND PATTERN BENEFITS");
		System.out.println("========================");
		System.out.println();

		System.out.println("✓ UNDO/REDO FUNCTIONALITY");
		System.out.println("  Every operation is reversible");
		System.out.println();

		System.out.println("✓ DECOUPLING");
		System.out.println("  VideoEditor doesn't know VideoClip internals");
		System.out.println("  Commands encapsulate operations as objects");
		System.out.println();

		System.out.println("✓ EXTENSIBILITY");
		System.out.println("  Add new command: Create one new class");
		System.out.println("  No modification of existing code");
		System.out.println();

		System.out.println("Key takeaway:");
		System.out.println("Encapsulate requests as objects for undo/redo support");
	}
}
