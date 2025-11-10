public class RealVideo implements Video {

	private String filename;
	private String title;
	private String duration;

	public RealVideo(String filename) {
		this.filename = filename;
		loadVideoFromDisk();  // Expensive operation!
	}

	private void loadVideoFromDisk() {
		// Simulate expensive loading operation
		System.out.println("[RealVideo] Loading: " + filename);

		try {
			Thread.sleep(2000);  // Simulate 2 second load time
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Parse filename to get title
		this.title = filename.replace(".mp4", "").replace("_", " ");
		this.duration = "10:45";  // Simulated duration

		System.out.println("[RealVideo] ✓ Loaded (~500MB)");
	}

	@Override
	public void display() {
		System.out.println("🎬 " + title + " (" + duration + ") 1080p");
	}

	@Override
	public void play() {
		System.out.println("▶️ [RealVideo] Playing: " + title);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return duration;
	}
}
