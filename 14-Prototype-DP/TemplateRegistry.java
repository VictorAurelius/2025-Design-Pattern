import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateRegistry {

	// Static map to store named templates
	private static Map<String, VideoUploadPrototype> templates = new HashMap<>();

	// Add template to registry
	public static void addTemplate(String name, VideoUploadPrototype template) {
		templates.put(name, template);
		System.out.println("✓ Template '" + name + "' added to registry");
	}

	// Get template (returns CLONE, not original!)
	// This is critical - always return a clone to protect the original
	public static VideoUploadPrototype getTemplate(String name) {
		VideoUploadPrototype template = templates.get(name);
		if (template == null) {
			throw new IllegalArgumentException("❌ Template not found: " + name);
		}
		return template.clone();  // Return clone, not original!
	}

	// Remove template from registry
	public static void removeTemplate(String name) {
		VideoUploadPrototype removed = templates.remove(name);
		if (removed != null) {
			System.out.println("✓ Template '" + name + "' removed from registry");
		} else {
			System.out.println("⚠️  Template '" + name + "' not found in registry");
		}
	}

	// List all template names
	public static List<String> listTemplates() {
		return new ArrayList<>(templates.keySet());
	}

	// Check if template exists
	public static boolean hasTemplate(String name) {
		return templates.containsKey(name);
	}

	// Clear all templates
	public static void clear() {
		templates.clear();
		System.out.println("✓ All templates cleared from registry");
	}

	// Get number of templates
	public static int size() {
		return templates.size();
	}
}
