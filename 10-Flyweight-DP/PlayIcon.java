public class PlayIcon implements VideoIcon {

	// Intrinsic state (shared, immutable)
	private final String iconImage;
	private final String color;
	private final int size;

	public PlayIcon() {
		// Simulate loading heavy icon data (500KB)
		System.out.println("   [Loading] play.png icon data (500KB)...");
		try {
			Thread.sleep(1);  // Simulate loading time
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.iconImage = "▶️ play.png";
		this.color = "white";
		this.size = 64;

		System.out.println("   ✓ PlayIcon object created (500KB in memory)");
	}

	@Override
	public void render(int x, int y, String videoTitle) {
		// Use intrinsic state (iconImage, color) + extrinsic state (x, y, videoTitle)
		System.out.println("   [PlayIcon] Rendering " + iconImage + " at (" + x + "," + y + ") for video: " + videoTitle);
	}

	@Override
	public String getIconType() {
		return "Play";
	}

	@Override
	public int getIconSize() {
		return size;
	}
}
