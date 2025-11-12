public class AbstractFactoryDemo {

	public static void main(String[] args) {

		System.out.println("=== Abstract Factory Pattern Demo ===\n");

		// Test 1: Light theme
		System.out.println("--- Test 1: Light Theme ---");
		ThemeFactory lightFactory = new LightThemeFactory();
		renderUI(lightFactory, "Light");

		// Test 2: Dark theme
		System.out.println("\n--- Test 2: Dark Theme ---");
		ThemeFactory darkFactory = new DarkThemeFactory();
		renderUI(darkFactory, "Dark");

		// Test 3: Theme switching
		System.out.println("\n--- Test 3: Theme Switching ---");
		ThemeFactory[] themes = {
			new LightThemeFactory(),
			new DarkThemeFactory(),
			new LightThemeFactory()
		};
		String[] names = {"Light", "Dark", "Light"};

		for (int i = 0; i < themes.length; i++) {
			System.out.println("\nSwitching to " + names[i] + " theme:");
			Button btn = themes[i].createButton();
			Icon icon = themes[i].createIcon();
			Panel panel = themes[i].createPanel();

			System.out.println("  Button: " + btn.getClass().getSimpleName());
			System.out.println("  Icon: " + icon.getClass().getSimpleName());
			System.out.println("  Panel: " + panel.getClass().getSimpleName());
		}

		// Test 4: Consistency guarantee
		System.out.println("\n--- Test 4: Consistency Guarantee ---");
		ThemeFactory factory = new DarkThemeFactory();
		Button b1 = factory.createButton();
		Button b2 = factory.createButton();
		Icon i1 = factory.createIcon();

		System.out.println("All components from DarkThemeFactory:");
		System.out.println("  Button 1: " + b1.getClass().getSimpleName());
		System.out.println("  Button 2: " + b2.getClass().getSimpleName());
		System.out.println("  Icon: " + i1.getClass().getSimpleName());
		System.out.println("  All components guaranteed to match!");
	}

	private static void renderUI(ThemeFactory factory, String themeName) {
		System.out.println("Creating " + themeName + " theme UI:");

		Button button = factory.createButton();
		Icon icon = factory.createIcon();
		Panel panel = factory.createPanel();

		button.render();
		icon.render();
		panel.render();
	}
}
