public class EmailChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [Email] Sending via Email...");
		System.out.println("  To: recipient@company.com");
		System.out.println("  Subject: Notification from TechCorp");
		System.out.println("  Body:");
		System.out.println(formattedMessage);
		System.out.println("  [Email] Message sent successfully!");
	}

	@Override
	public String getChannelName() {
		return "Email";
	}
}
