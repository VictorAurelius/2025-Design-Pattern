public class NotificationDemo {

	public static void main(String[] args) {
		// Create channels (implementations)
		NotificationChannel emailChannel = new EmailChannel();
		NotificationChannel smsChannel = new SMSChannel();
		NotificationChannel slackChannel = new SlackChannel();

		// Test 1: System notification via Email
		Notification notification = new SystemNotification(emailChannel);
		notification.send("Server maintenance scheduled for tonight at 11 PM");

		// Test 2: Marketing notification via SMS
		notification = new MarketingNotification(smsChannel);
		notification.send("Get 50% off on all products this weekend!");

		// Test 3: Alert notification via Slack
		notification = new AlertNotification(slackChannel);
		notification.send("Security breach detected on production server!");

		// // Test 4: Runtime flexibility - change channel dynamically
		// System.out.println("\n\n--- RUNTIME FLEXIBILITY DEMO ---");
		// notification = new SystemNotification(emailChannel);
		// notification.send("Database backup completed");

		// System.out.println("\n--- Switching channel from Email to SMS ---");
		// notification.setChannel(smsChannel);
		// notification.send("Database backup completed");

		// // Test 5: All combinations
		// System.out.println("\n\n--- ALL COMBINATIONS DEMO ---");

		// System.out.println("\n[System + Email]");
		// new SystemNotification(emailChannel).send("Test message 1");

		// System.out.println("\n[System + SMS]");
		// new SystemNotification(smsChannel).send("Test message 2");

		// System.out.println("\n[Marketing + Slack]");
		// new MarketingNotification(slackChannel).send("Test message 3");
	}
}
