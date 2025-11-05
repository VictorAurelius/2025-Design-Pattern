public class LikeIcon implements VideoIcon {

	// Intrinsic state (shared, immutable)
	private final String iconImage;
	private final String color;
	private final int size;

	public LikeIcon() {
		// Simulate loading heavy icon data (500KB)
		System.out.println("   [Loading] like.png icon data (500KB)...");
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.iconImage = "👍 like.png";
		this.color = "gray";
		this.size = 64;

		System.out.println("   ✓ LikeIcon object created (500KB in memory)");
	}

	@Override
	public void render(int x, int y, String videoTitle) {
		System.out.println("   [LikeIcon] Rendering " + iconImage + " at (" + x + "," + y + ") for video: " + videoTitle);
	}

	@Override
	public String getIconType() {
		return "Like";
	}

	@Override
	public int getIconSize() {
		return size;
	}
}
