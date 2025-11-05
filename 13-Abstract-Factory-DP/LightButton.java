public class LightButton implements Button {

	@Override
	public void render() {
		System.out.println("\n--- Light Button ---");
		System.out.println("  🔲 [Light Button] Rendering button");
		System.out.println("  🎨 Style: " + getStyle());
		System.out.println("  ✨ Effects: Subtle shadow, rounded corners");
	}

	@Override
	public void onClick() {
		System.out.println("  🖱️  onClick: Button animation (smooth transition)");
	}

	@Override
	public String getStyle() {
		return "White background (#FFFFFF), dark text (#333333)";
	}
}
