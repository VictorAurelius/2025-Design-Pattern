public class LightPanel implements Panel {

	@Override
	public void render() {
		System.out.println("\n--- Light Panel ---");
		System.out.println("  ▭ [Light Panel] Rendering panel");
		System.out.println("  🎨 Background: " + getBackgroundColor());
		System.out.println("  🖼️  Border: " + getBorderColor());
		System.out.println("  📐 Layout: Flexbox, padding: 20px");
	}

	@Override
	public String getBackgroundColor() {
		return "White (#FFFFFF)";
	}

	@Override
	public String getBorderColor() {
		return "Light gray (#DDDDDD)";
	}
}
