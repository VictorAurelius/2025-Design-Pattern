public abstract class Notification {

	protected NotificationChannel channel;

	public Notification(NotificationChannel channel) {
		this.channel = channel;
	}

	public abstract String formatMessage(String content);

	public void send(String content) {
		String formattedMessage = formatMessage(content);
		System.out.println("\n=== Sending Notification ===");
		System.out.println("Type: " + getNotificationType());
		System.out.println("Channel: " + channel.getChannelName());
		channel.sendMessage(formattedMessage);
	}

	public abstract String getNotificationType();

	public void setChannel(NotificationChannel channel) {
		this.channel = channel;
	}
}
