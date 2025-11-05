import java.util.ArrayList;
import java.util.List;

/**
 * MacroCommand - Composite command that contains multiple commands
 *
 * Implements Composite pattern for commands.
 * A macro command groups multiple operations into one atomic unit.
 *
 * Key Behaviors:
 * - Execute all child commands in order
 * - Undo all child commands in REVERSE order
 * - Acts as both Command and Container
 *
 * Real-World Use Cases:
 * - "Vintage Effect" preset = Sepia + Lower Brightness + Lower Contrast
 * - "Professional Look" = Sharpen + Adjust Contrast + Adjust Brightness
 * - Any multi-step editing workflow that should be one "undo"
 *
 * This is the Composite Pattern applied to Command Pattern!
 */
public class MacroCommand implements Command {

	private List<Command> commands;   // Child commands
	private String name;               // Preset name (e.g., "Vintage Effect")

	/**
	 * Create empty macro command
	 *
	 * @param name Name of this macro/preset
	 */
	public MacroCommand(String name) {
		this.name = name;
		this.commands = new ArrayList<>();
	}

	/**
	 * Add a command to this macro
	 * Commands will be executed in the order they're added
	 *
	 * @param command Command to add
	 */
	public void addCommand(Command command) {
		commands.add(command);
	}

	/**
	 * Remove a command from this macro
	 *
	 * @param command Command to remove
	 */
	public void removeCommand(Command command) {
		commands.remove(command);
	}

	/**
	 * Get number of commands in this macro
	 *
	 * @return Count of child commands
	 */
	public int getCommandCount() {
		return commands.size();
	}

	@Override
	public void execute() {
		System.out.println("\n[MACRO] Executing: " + name +
		                   " (" + commands.size() + " operations)");
		System.out.println("─".repeat(60));

		// Execute all commands in order
		for (int i = 0; i < commands.size(); i++) {
			System.out.println("\n  [" + (i + 1) + "/" + commands.size() + "] " +
			                   commands.get(i).getDescription());
			commands.get(i).execute();
		}

		System.out.println("\n─".repeat(60));
		System.out.println("✓ " + name + " complete (" + commands.size() +
		                   " operations executed)");
	}

	@Override
	public void undo() {
		System.out.println("\n[MACRO UNDO] Undoing: " + name +
		                   " (" + commands.size() + " operations)");
		System.out.println("─".repeat(60));

		// Undo all commands in REVERSE order (like stack unwinding)
		for (int i = commands.size() - 1; i >= 0; i--) {
			System.out.println("\n  [" + (commands.size() - i) + "/" + commands.size() + "] Undo: " +
			                   commands.get(i).getDescription());
			commands.get(i).undo();
		}

		System.out.println("\n─".repeat(60));
		System.out.println("✓ " + name + " undone (" + commands.size() +
		                   " operations reversed)");
	}

	@Override
	public String getDescription() {
		return name + " (macro: " + commands.size() + " operations)";
	}

	/**
	 * Get list of all child command descriptions
	 * Useful for displaying what this macro does
	 *
	 * @return List of command descriptions
	 */
	public List<String> getChildDescriptions() {
		List<String> descriptions = new ArrayList<>();
		for (Command command : commands) {
			descriptions.add(command.getDescription());
		}
		return descriptions;
	}

	/**
	 * Display macro details
	 * Shows name and all child commands
	 */
	public void showDetails() {
		System.out.println("\nMacro Command: " + name);
		System.out.println("Operations:");
		for (int i = 0; i < commands.size(); i++) {
			System.out.println("  " + (i + 1) + ". " + commands.get(i).getDescription());
		}
	}
}
