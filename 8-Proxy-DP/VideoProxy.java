public class VideoProxy implements Video {

	private RealVideo realVideo;  // Reference to real video (null initially)
	private String filename;
	private String title;
	private String duration;
	private boolean isPremium;

	public VideoProxy(String filename, boolean isPremium) {
		this.filename = filename;
		this.isPremium = isPremium;

		// Only store lightweight metadata
		this.title = filename.replace(".mp4", "").replace("_", " ");
		this.duration = "10:45";

		System.out.println("[VideoProxy] ⚡ Proxy created for: " + title + " (lightweight)");
	}

	@Override
	public void display() {
		// Can display without loading actual video!
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: " + duration);
		if (isPremium) {
			System.out.println("│ 👑 PREMIUM");
		} else {
			System.out.println("│ 🆓 FREE");
		}
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n[VideoProxy] 🎬 Play requested for: " + title);

		// Lazy loading: create RealVideo only when needed
		if (realVideo == null) {
			System.out.println("[VideoProxy] 🔄 Lazy loading: Creating RealVideo...");
			realVideo = new RealVideo(filename);
		} else {
			System.out.println("[VideoProxy] ♻️  Using cached RealVideo");
		}

		// Delegate to real video
		realVideo.play();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return duration;
	}

	public boolean isPremium() {
		return isPremium;
	}
}
