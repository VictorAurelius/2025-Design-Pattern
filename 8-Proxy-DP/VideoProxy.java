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

		System.out.println("[VideoProxy] Proxy created: " + title);
	}

	@Override
	public void display() {
		// Can display without loading actual video!
		String type = isPremium ? "PREMIUM" : "FREE";
		System.out.println(title + " (" + duration + ") " + type);
	}

	@Override
	public void play() {
		System.out.println("[VideoProxy] Play requested: " + title);

		// Lazy loading: create RealVideo only when needed
		if (realVideo == null) {
			System.out.println("[VideoProxy] Lazy loading...");
			realVideo = new RealVideo(filename);
		} else {
			System.out.println("[VideoProxy] Using cached video");
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
