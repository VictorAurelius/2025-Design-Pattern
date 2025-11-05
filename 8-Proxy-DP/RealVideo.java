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
		System.out.println("   [RealVideo] 📁 Loading video from disk: " + filename);
		System.out.println("   [RealVideo] ⏳ Loading...");

		try {
			Thread.sleep(2000);  // Simulate 2 second load time
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Parse filename to get title
		this.title = filename.replace(".mp4", "").replace("_", " ");
		this.duration = "10:45";  // Simulated duration

		System.out.println("   [RealVideo] ✓ Video loaded successfully!");
		System.out.println("   [RealVideo] 💾 Video size: ~500MB");
	}

	@Override
	public void display() {
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: " + duration);
		System.out.println("│ 📊 Quality: 1080p");
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n▶️  [RealVideo] Playing video: " + title);
		System.out.println("   [RealVideo] Buffering: ████████████ 100%");
		System.out.println("   [RealVideo] 🎵 Audio: ON | 🎞️  Video: ON");
		System.out.println("   [RealVideo] Now playing at 1080p...");
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
