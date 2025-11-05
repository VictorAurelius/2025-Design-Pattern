public class LightIcon implements Icon {

	@Override
	public void render() {
		System.out.println("\n--- Light Icon ---");
		System.out.println("  🔳 [Light Icon] Rendering icon");
		System.out.println("  🎨 Color: " + getColor());
		System.out.println("  📏 Size: " + getSize() + "px");
		System.out.println("  🎯 Use case: Visible on light background");
	}

	@Override
	public String getColor() {
		return "Dark gray (#666666)";
	}

	@Override
	public int getSize() {
		return 24;
	}
}
