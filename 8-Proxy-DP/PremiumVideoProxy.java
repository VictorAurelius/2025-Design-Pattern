public class PremiumVideoProxy implements Video {

	private VideoProxy videoProxy;
	private String filename;
	private User currentUser;
	private String title;

	public PremiumVideoProxy(String filename, User user) {
		this.filename = filename;
		this.currentUser = user;
		this.title = filename.replace(".mp4", "").replace("_", " ");

		System.out.println("[PremiumProxy] Protection proxy created: " + title);
	}

	@Override
	public void display() {
		// Can always display preview
		String access = currentUser.hasSubscription() ? "" : " 🔒";
		System.out.println("🎬 " + title + " (10:45) PREMIUM" + access);
	}

	@Override
	public void play() {
		System.out.println("[PremiumProxy] Checking access: " + currentUser.getName());

		if (!currentUser.hasSubscription()) {
			// Access denied
			System.out.println("⛔ ACCESS DENIED - Premium content");
			System.out.println("💎 Upgrade to Premium ($9.99/month)");
			return;
		}

		// Access granted - delegate to VideoProxy
		System.out.println("[PremiumProxy] ✅ Access granted!");

		if (videoProxy == null) {
			videoProxy = new VideoProxy(filename, true);
		}

		videoProxy.play();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDuration() {
		return "10:45";
	}
}
