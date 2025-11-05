public class DarkIcon implements Icon {

	@Override
	public void render() {
		System.out.println("\n--- Dark Icon ---");
		System.out.println("  🔳 [Dark Icon] Rendering icon");
		System.out.println("  🎨 Color: " + getColor());
		System.out.println("  📏 Size: " + getSize() + "px");
		System.out.println("  🎯 Use case: Visible on dark background");
	}

	@Override
	public String getColor() {
		return "White (#FFFFFF)";
	}

	@Override
	public int getSize() {
		return 24;
	}
}
