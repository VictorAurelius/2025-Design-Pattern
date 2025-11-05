/**
 * CommandPatternDemo - Comprehensive demonstration of Command pattern
 *
 * Demonstrates:
 * 1. Basic video editing with undo/redo
 * 2. Macro commands (composite commands/presets)
 * 3. Complete editing session with history
 * 4. Multiple undo/redo operations
 * 5. Command pattern benefits
 *
 * Scenario: StreamFlix video editor
 * Content creators edit videos with full undo/redo support
 */
public class CommandPatternDemo {

	public static void main(String[] args) {
		System.out.println("╔════════════════════════════════════════════════════════════════════╗");
		System.out.println("║          COMMAND PATTERN - VIDEO EDITOR WITH UNDO/REDO            ║");
		System.out.println("║                      StreamFlix Platform                           ║");
		System.out.println("╚════════════════════════════════════════════════════════════════════╝");
		System.out.println();

		// Run demonstration scenarios
		demonstrateBasicOperations();
		System.out.println("\n" + "═".repeat(70) + "\n");

		demonstrateMacroCommand();
		System.out.println("\n" + "═".repeat(70) + "\n");

		demonstrateCompleteEditingSession();
		System.out.println("\n" + "═".repeat(70) + "\n");

		showPatternBenefits();
	}

	/**
	 * Scenario 1: Basic operations with undo/redo
	 */
	private static void demonstrateBasicOperations() {
		System.out.println("SCENARIO 1: Basic Video Editing with Undo/Redo");
		System.out.println("═".repeat(70));
		System.out.println();

		// Create video clip
		VideoClip video = new VideoClip("summer-vlog-2024.mp4", "5:30");
		VideoEditor editor = new VideoEditor(video);

		System.out.println("Video: " + video.getFilename() + " (Duration: " +
		                   video.getDuration() + ")");
		System.out.println();
		System.out.println("Initial State:");
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println("  Filter: " + video.getFilter());
		System.out.println();

		// Execute operations
		System.out.println("─".repeat(70));
		System.out.println("[Operation 1] Add Grayscale Filter");
		System.out.println("─".repeat(70));
		Command addFilter = new AddFilterCommand(video, "Grayscale");
		editor.executeCommand(addFilter);
		System.out.println();

		System.out.println("─".repeat(70));
		System.out.println("[Operation 2] Adjust Brightness (+20)");
		System.out.println("─".repeat(70));
		Command adjustBrightness = new AdjustBrightnessCommand(video, 20);
		editor.executeCommand(adjustBrightness);
		System.out.println();

		System.out.println("─".repeat(70));
		System.out.println("[Operation 3] Adjust Contrast (-10)");
		System.out.println("─".repeat(70));
		Command adjustContrast = new AdjustContrastCommand(video, -10);
		editor.executeCommand(adjustContrast);
		System.out.println();

		System.out.println("Current State:");
		System.out.println("  Filter: " + video.getFilter());
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println();

		// Demonstrate undo
		System.out.println("─".repeat(70));
		System.out.println("UNDO OPERATIONS");
		System.out.println("─".repeat(70));
		editor.undo();  // Undo contrast
		System.out.println();
		editor.undo();  // Undo brightness
		System.out.println();

		System.out.println("Current State After Undo:");
		System.out.println("  Filter: " + video.getFilter());
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println();

		// Demonstrate redo
		System.out.println("─".repeat(70));
		System.out.println("REDO OPERATIONS");
		System.out.println("─".repeat(70));
		editor.redo();  // Redo brightness
		System.out.println();

		System.out.println("Current State After Redo:");
		System.out.println("  Filter: " + video.getFilter());
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println();

		// Show history and stacks
		editor.showHistory();
		editor.showStackStatus();
	}

	/**
	 * Scenario 2: Macro command (composite command)
	 */
	private static void demonstrateMacroCommand() {
		System.out.println("SCENARIO 2: Macro Command (Vintage Effect Preset)");
		System.out.println("═".repeat(70));
		System.out.println();

		// Create video clip
		VideoClip video = new VideoClip("product-review.mp4", "10:00");
		VideoEditor editor = new VideoEditor(video);

		System.out.println("Video: " + video.getFilename());
		System.out.println();

		// Create Vintage Effect macro (3 operations)
		MacroCommand vintageEffect = new MacroCommand("Vintage Effect");
		vintageEffect.addCommand(new AddFilterCommand(video, "Sepia"));
		vintageEffect.addCommand(new AdjustBrightnessCommand(video, -15));
		vintageEffect.addCommand(new AdjustContrastCommand(video, -10));

		System.out.println("Creating Vintage Effect Macro:");
		vintageEffect.showDetails();

		// Execute macro
		editor.executeCommand(vintageEffect);
		System.out.println();

		System.out.println("Video State After Vintage Effect:");
		System.out.println("  Filter: " + video.getFilter());
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println();

		// Undo entire macro in one step
		editor.undo();
		System.out.println();

		System.out.println("Video State After Undo:");
		System.out.println("  Filter: " + video.getFilter());
		System.out.println("  Brightness: " + video.getBrightness());
		System.out.println("  Contrast: " + video.getContrast());
		System.out.println();

		// Demonstrate second macro: Professional Look
		System.out.println("─".repeat(70));
		System.out.println("Creating Professional Look Macro:");
		System.out.println("─".repeat(70));

		MacroCommand professionalLook = new MacroCommand("Professional Look");
		professionalLook.addCommand(new AddFilterCommand(video, "Sharpen"));
		professionalLook.addCommand(new AdjustContrastCommand(video, 20));
		professionalLook.addCommand(new AdjustBrightnessCommand(video, 5));
		professionalLook.addCommand(new AddTextCommand(video, "StreamFlix", 10, 10));

		professionalLook.showDetails();
		System.out.println();

		editor.executeCommand(professionalLook);
		System.out.println();

		System.out.println("Video State After Professional Look:");
		video.showState();
	}

	/**
	 * Scenario 3: Complete editing session
	 */
	private static void demonstrateCompleteEditingSession() {
		System.out.println("SCENARIO 3: Complete Editing Session");
		System.out.println("═".repeat(70));
		System.out.println();

		// Create video clip
		VideoClip video = new VideoClip("tutorial-video.mp4", "15:00");
		VideoEditor editor = new VideoEditor(video);

		System.out.println("Video: " + video.getFilename() +
		                   " (Duration: " + video.getDuration() + ")");
		System.out.println();
		System.out.println("Performing 6 editing operations...");
		System.out.println();

		// Operation 1: Trim video
		System.out.println("[1] Trim Video (0:00:05 - 0:14:50)");
		editor.executeCommand(new TrimVideoCommand(video, "0:05", "14:50"));
		System.out.println();

		// Operation 2: Add sharpen filter
		System.out.println("[2] Add Sharpen Filter");
		editor.executeCommand(new AddFilterCommand(video, "Sharpen"));
		System.out.println();

		// Operation 3: Adjust brightness
		System.out.println("[3] Adjust Brightness (+10)");
		editor.executeCommand(new AdjustBrightnessCommand(video, 10));
		System.out.println();

		// Operation 4: Adjust contrast
		System.out.println("[4] Adjust Contrast (+15)");
		editor.executeCommand(new AdjustContrastCommand(video, 15));
		System.out.println();

		// Operation 5: Add text overlay
		System.out.println("[5] Add Text: 'Subscribe!' at (100, 50)");
		editor.executeCommand(new AddTextCommand(video, "Subscribe!", 100, 50));
		System.out.println();

		// Operation 6: Adjust volume
		System.out.println("[6] Adjust Volume (120%)");
		editor.executeCommand(new AdjustVolumeCommand(video, 120));
		System.out.println();

		// Show final state
		System.out.println("─".repeat(70));
		System.out.println("FINAL VIDEO STATE");
		System.out.println("─".repeat(70));
		video.showState();
		System.out.println();

		// Show operation history
		editor.showHistory();
		editor.showStackStatus();
		System.out.println();

		// Demonstrate multiple undo
		System.out.println("─".repeat(70));
		System.out.println("UNDO LAST 3 OPERATIONS");
		System.out.println("─".repeat(70));
		editor.undoMultiple(3);
		System.out.println();

		System.out.println("Video State After Undo:");
		video.showState();
		System.out.println();

		editor.showStackStatus();
	}

	/**
	 * Show benefits of Command pattern
	 */
	private static void showPatternBenefits() {
		System.out.println("COMMAND PATTERN BENEFITS DEMONSTRATED");
		System.out.println("═".repeat(70));
		System.out.println();

		System.out.println("✓ UNDO/REDO FUNCTIONALITY");
		System.out.println("  - Every operation is reversible");
		System.out.println("  - Unlimited undo/redo (limited by stack size)");
		System.out.println("  - Redo works correctly after undo");
		System.out.println();

		System.out.println("✓ DECOUPLING");
		System.out.println("  - VideoEditor doesn't know about VideoClip internals");
		System.out.println("  - Commands encapsulate operations as objects");
		System.out.println("  - Easy to swap receivers or test in isolation");
		System.out.println();

		System.out.println("✓ MACRO COMMANDS");
		System.out.println("  - Group multiple operations into presets");
		System.out.println("  - Execute all operations atomically");
		System.out.println("  - Undo entire preset in one step");
		System.out.println("  - Example: Vintage Effect = 3 operations");
		System.out.println();

		System.out.println("✓ OPERATION HISTORY");
		System.out.println("  - Track all operations performed");
		System.out.println("  - Display history to user");
		System.out.println("  - Navigate to any point in history");
		System.out.println("  - Export history as preset");
		System.out.println();

		System.out.println("✓ ZERO IF-ELSE STATEMENTS");
		System.out.println("  - Before: 200+ lines with 8+ if-else branches");
		System.out.println("  - After: 20 lines, zero if-else");
		System.out.println("  - 90% code reduction!");
		System.out.println();

		System.out.println("✓ TESTABILITY");
		System.out.println("  - Each command independently testable");
		System.out.println("  - Mock VideoClip for unit tests");
		System.out.println("  - Test undo/redo in isolation");
		System.out.println();

		System.out.println("✓ EXTENSIBILITY");
		System.out.println("  - Add new command: Create one new class");
		System.out.println("  - No modification of existing code");
		System.out.println("  - Open/Closed Principle satisfied");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("COMMAND VS OTHER PATTERNS");
		System.out.println("═".repeat(70));
		System.out.println();

		System.out.println("COMMAND PATTERN (Pattern #19):");
		System.out.println("  • Encapsulates REQUEST as object");
		System.out.println("  • Supports undo/redo operations");
		System.out.println("  • Can queue/log/schedule operations");
		System.out.println("  • Example: editor.execute(new AddFilterCommand())");
		System.out.println();

		System.out.println("STRATEGY PATTERN (Pattern #18):");
		System.out.println("  • Encapsulates ALGORITHM as object");
		System.out.println("  • No undo support");
		System.out.println("  • Swaps behavior at runtime");
		System.out.println("  • Example: compressor.setStrategy(new H265Strategy())");
		System.out.println();

		System.out.println("STATE PATTERN (Pattern #17):");
		System.out.println("  • Encapsulates STATE as object");
		System.out.println("  • Context transitions automatically");
		System.out.println("  • States know about each other");
		System.out.println("  • Example: player.play() → state changes");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println();

		// Show metrics comparison
		System.out.println("BEFORE vs AFTER METRICS");
		System.out.println("═".repeat(70));
		System.out.println();
		System.out.println("Code Complexity:");
		System.out.println("  Before: 200+ lines with 8+ if-else branches");
		System.out.println("  After:  20 lines, zero if-else");
		System.out.println("  Improvement: 90% reduction");
		System.out.println();
		System.out.println("Undo Support:");
		System.out.println("  Before: 0% of operations support undo");
		System.out.println("  After:  100% of operations support undo");
		System.out.println("  Improvement: ∞ (from impossible to complete)");
		System.out.println();
		System.out.println("User Satisfaction:");
		System.out.println("  Before: 2.5/5 ⭐ (frustration, fear of mistakes)");
		System.out.println("  After:  4.5/5 ⭐ (confidence, experimentation)");
		System.out.println("  Improvement: +80%");
		System.out.println();
		System.out.println("Editing Time:");
		System.out.println("  Before: 45 min/video (redo mistakes)");
		System.out.println("  After:  30 min/video (undo/redo)");
		System.out.println("  Improvement: 33% faster");
		System.out.println();
		System.out.println("Support Tickets:");
		System.out.println("  Before: 50/month ('no undo' complaints)");
		System.out.println("  After:  5/month");
		System.out.println("  Improvement: 90% reduction");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("KEY TAKEAWAY:");
		System.out.println("═".repeat(70));
		System.out.println();
		System.out.println("\"Don't just DO operations - ENCAPSULATE them as objects.\"");
		System.out.println();
		System.out.println("This simple shift from procedural to object-oriented thinking");
		System.out.println("unlocks undo/redo, logging, queuing, macros, and more!");
		System.out.println();
		System.out.println("═".repeat(70));
	}
}
