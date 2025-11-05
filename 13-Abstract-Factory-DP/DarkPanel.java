public class DarkPanel implements Panel {

	@Override
	public void render() {
		System.out.println("\n--- Dark Panel ---");
		System.out.println("  ▭ [Dark Panel] Rendering panel");
		System.out.println("  🎨 Background: " + getBackgroundColor());
		System.out.println("  🖼️  Border: " + getBorderColor());
		System.out.println("  📐 Layout: Flexbox, padding: 20px");
	}

	@Override
	public String getBackgroundColor() {
		return "Dark (#1E1E1E)";
	}

	@Override
	public String getBorderColor() {
		return "Dark gray (#333333)";
	}
}
