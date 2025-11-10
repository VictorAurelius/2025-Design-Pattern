public class MobileAppSubscriber implements Subscriber {

	private String name;
	private String deviceId;
	private YouTubeChannel channel;

	public MobileAppSubscriber(String name, String deviceId, YouTubeChannel channel) {
		this.name = name;
		this.deviceId = deviceId;
		this.channel = channel;
	}

	@Override
	public void update(String videoTitle) {
		String channelName = channel.getChannelName();

		System.out.println("\n[Mobile Push Notification]");
		System.out.println("   Device: " + deviceId);
		System.out.println("   User: " + name);
		System.out.println(" " + channelName + " uploaded:");
		System.out.println("   \"" + videoTitle + "\"");
		System.out.println("   Tap to watch now!");
	}

	@Override
	public void subscribe() {
		System.out.println("\n" + name + " subscribing via Mobile App...");
		channel.attach(this);
		System.out.println("  → Subscribed successfully!");
	}

	@Override
	public void unsubscribe() {
		System.out.println("\n" + name + " unsubscribing via Mobile App...");
		channel.detach(this);
		System.out.println("  → Unsubscribed successfully!");
	}
}
