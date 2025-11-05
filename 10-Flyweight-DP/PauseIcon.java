public class PauseIcon implements VideoIcon {

	// Intrinsic state (shared, immutable)
	private final String iconImage;
	private final String color;
	private final int size;

	public PauseIcon() {
		// Simulate loading heavy icon data (500KB)
		System.out.println("   [Loading] pause.png icon data (500KB)...");
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.iconImage = "⏸️ pause.png";
		this.color = "white";
		this.size = 64;

		System.out.println("   ✓ PauseIcon object created (500KB in memory)");
	}

	@Override
	public void render(int x, int y, String videoTitle) {
		System.out.println("   [PauseIcon] Rendering " + iconImage + " at (" + x + "," + y + ") for video: " + videoTitle);
	}

	@Override
	public String getIconType() {
		return "Pause";
	}

	@Override
	public int getIconSize() {
		return size;
	}
}
