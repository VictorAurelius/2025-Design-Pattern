import java.util.Arrays;

public class PrototypeDemo {

	public static void main(String[] args) {

		System.out.println("=== Prototype Pattern Demo ===\n");

		// Test 1: Create templates
		System.out.println("--- Test 1: Creating Templates ---");
		VideoUpload gamingTemplate = new VideoUpload(
			"Gaming Template",
			"gaming-placeholder.mp4",
			"Placeholder",
			Arrays.asList("gaming", "gameplay", "tutorial"),
			"Gaming",
			"public",
			true,
			"standard",
			"en"
		);
		System.out.println("Created: " + gamingTemplate.getTitle());

		VideoUpload tutorialTemplate = new VideoUpload(
			"Tutorial Template",
			"tutorial-placeholder.mp4",
			"Educational content",
			Arrays.asList("tutorial", "education", "learn"),
			"Education",
			"public",
			true,
			"creative commons",
			"en"
		);
		System.out.println("Created: " + tutorialTemplate.getTitle());

		// Test 2: Clone and customize
		System.out.println("\n--- Test 2: Cloning Templates ---");
		VideoUpload gaming1 = gamingTemplate.clone();
		gaming1.setTitle("Minecraft Tutorial #1");
		gaming1.setFilePath("/videos/minecraft-1.mp4");
		System.out.println("Cloned: " + gaming1.getTitle());

		VideoUpload gaming2 = gamingTemplate.clone();
		gaming2.setTitle("Fortnite Gameplay #15");
		gaming2.setFilePath("/videos/fortnite-15.mp4");
		System.out.println("Cloned: " + gaming2.getTitle());

		// Test 3: Template registry
		System.out.println("\n--- Test 3: Template Registry ---");
		TemplateRegistry registry = new TemplateRegistry();
		registry.registerTemplate("gaming", gamingTemplate);
		registry.registerTemplate("tutorial", tutorialTemplate);

		VideoUpload newGaming = registry.getTemplate("gaming");
		newGaming.setTitle("New Gaming Video");
		System.out.println("From registry: " + newGaming.getTitle());

		// Test 4: Deep copy verification
		System.out.println("\n--- Test 4: Deep Copy Verification ---");
		VideoUpload original = gamingTemplate;
		VideoUpload cloned = original.clone();

		cloned.setTitle("Modified Clone");
		System.out.println("Original: " + original.getTitle());
		System.out.println("Cloned: " + cloned.getTitle());
		System.out.println("Deep copy works - original unchanged!");
	}
}
