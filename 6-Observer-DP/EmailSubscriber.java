public class EmailSubscriber implements Subscriber {

	private String name;
	private String email;
	private YouTubeChannel channel;

	public EmailSubscriber(String name, String email, YouTubeChannel channel) {
		this.name = name;
		this.email = email;
		this.channel = channel;
	}

	@Override
	public void update(String videoTitle) {
		// Pull model: get additional information from channel
		String description = channel.getVideoDescription();
		String channelName = channel.getChannelName();

		System.out.println("\n[Email Notification]");
		System.out.println("   To: " + name + " <" + email + ">");
		System.out.println("   Subject: New video from " + channelName);
		System.out.println("   Body:");
		System.out.println("   -------------");
		System.out.println("   Hi " + name + ",");
		System.out.println("   " + channelName + " just uploaded: " + videoTitle);
		System.out.println("   " + description);
		System.out.println("   Watch now: https://youtube.com/watch?v=xyz123");
		System.out.println("   -------------");
	}

	@Override
	public void subscribe() {
		System.out.println("\n" + name + " subscribing via Email...");
		channel.attach(this);
		System.out.println("  → Subscribed successfully!");
	}

	@Override
	public void unsubscribe() {
		System.out.println("\n" + name + " unsubscribing via Email...");
		channel.detach(this);
		System.out.println("  → Unsubscribed successfully!");
	}
}
