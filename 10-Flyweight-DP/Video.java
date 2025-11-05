public class Video {

	// Extrinsic state (unique to each video)
	private String title;
	private int x;
	private int y;

	public Video(String title, int x, int y) {
		this.title = title;
		this.x = x;
		this.y = y;
	}

	public void renderIcons() {
		// Get shared flyweights from factory
		VideoIcon playIcon = IconFactory.getIcon("play");
		VideoIcon likeIcon = IconFactory.getIcon("like");

		// Pass extrinsic state to flyweight
		playIcon.render(x, y, title);
		likeIcon.render(x + 70, y, title);
	}

	public String getTitle() {
		return title;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
