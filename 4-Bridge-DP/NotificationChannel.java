public interface NotificationChannel {

	void sendMessage(String formattedMessage);

	String getChannelName();
}
