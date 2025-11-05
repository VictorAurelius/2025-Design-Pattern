public class AlertNotification extends Notification {

	public AlertNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "⚠️ CRITICAL ALERT ⚠️\n" +
		       content + "\n" +
		       "Priority: URGENT\n" +
		       "Action: IMMEDIATE ATTENTION REQUIRED!";
	}

	@Override
	public String getNotificationType() {
		return "Alert Notification";
	}
}
