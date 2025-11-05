public class SystemNotification extends Notification {

	public SystemNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "[SYSTEM] " + content + "\n" +
		       "Priority: Normal\n" +
		       "Action: Please acknowledge";
	}

	@Override
	public String getNotificationType() {
		return "System Notification";
	}
}
