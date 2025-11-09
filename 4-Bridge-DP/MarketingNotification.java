public class MarketingNotification extends Notification {

	public MarketingNotification(NotificationChannel channel) {
		super(channel);
	}

	@Override
	public String formatMessage(String content) {
		return "SPECIAL OFFER\n" +
				content + "\n" +
				"Don't miss out! Limited time only.\n" +
				"Unsubscribe: techcorp.com/unsubscribe";
	}

	@Override
	public String getNotificationType() {
		return "Marketing Notification";
	}
}
