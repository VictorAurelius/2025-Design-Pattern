public class PremiumVideoProxy implements Video {

	private VideoProxy videoProxy;
	private String filename;
	private User currentUser;
	private String title;

	public PremiumVideoProxy(String filename, User user) {
		this.filename = filename;
		this.currentUser = user;
		this.title = filename.replace(".mp4", "").replace("_", " ");

		System.out.println("[PremiumProxy] 🔒 Protection proxy created for premium content: " + title);
	}

	@Override
	public void display() {
		// Can always display preview
		System.out.println("\n┌────────────────────────────────────┐");
		System.out.println("│ 🎬 " + title);
		System.out.println("│ ⏱️  Duration: 10:45");
		System.out.println("│ 👑 PREMIUM CONTENT");
		if (!currentUser.hasSubscription()) {
			System.out.println("│ 🔒 Subscribe to watch");
		}
		System.out.println("└────────────────────────────────────┘");
	}

	@Override
	public void play() {
		System.out.println("\n[PremiumProxy] 🔐 Checking access rights...");
		System.out.println("[PremiumProxy] User: " + currentUser.getName());
		System.out.println("[PremiumProxy] Has subscription: " + currentUser.hasSubscription());

		if (!currentUser.hasSubscription()) {
			// Access denied
			System.out.println("\n╔════════════════════════════════════════╗");
			System.out.println("║  ⛔ ACCESS DENIED                     ║");
			System.out.println("║                                        ║");
			System.out.println("║  This is premium content.              ║");
			System.out.println("║  Upgrade to Premium to watch!          ║");
			System.out.println("║                                        ║");
			System.out.println("║  💎 Premium: $9.99/month               ║");
			System.out.println("║  ✓ Unlimited premium videos            ║");
			System.out.println("║  ✓ Ad-free experience                  ║");
			System.out.println("║  ✓ 4K quality                          ║");
			System.out.println("╚════════════════════════════════════════╝");
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
