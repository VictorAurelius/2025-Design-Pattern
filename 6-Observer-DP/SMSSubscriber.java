public class SMSSubscriber implements Subscriber {

	private String name;
	private String phoneNumber;
	private YouTubeChannel channel;

	public SMSSubscriber(String name, String phoneNumber, YouTubeChannel channel) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.channel = channel;
	}

	@Override
	public void update(String videoTitle) {
		String channelName = channel.getChannelName();

		System.out.println("[SMS Notification]");
		System.out.println("   To: " + phoneNumber + " (" + name + ")");
		System.out.println("   Message:");
		System.out.println("   New video from " + channelName + ": " + videoTitle);
	}

	@Override
	public void subscribe() {
		System.out.println(name + " subscribing via SMS...");
		channel.attach(this);
		System.out.println("  → Subscribed successfully!");
	}

	@Override
	public void unsubscribe() {
		System.out.println(name + " unsubscribing via SMS...");
		channel.detach(this);
		System.out.println("  → Unsubscribed successfully!");
	}
}
