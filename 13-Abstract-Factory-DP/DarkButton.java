public class DarkButton implements Button {

	@Override
	public void render() {
		System.out.println("\n--- Dark Button ---");
		System.out.println("  🔲 [Dark Button] Rendering button");
		System.out.println("  🎨 Style: " + getStyle());
		System.out.println("  ✨ Effects: Glow on hover, rounded corners");
	}

	@Override
	public void onClick() {
		System.out.println("  🖱️  onClick: Button animation (pulse effect)");
	}

	@Override
	public String getStyle() {
		return "Dark gray background (#2D2D2D), white text (#FFFFFF)";
	}
}
