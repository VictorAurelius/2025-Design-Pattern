public class SMSChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [SMS] Sending via SMS...");
		System.out.println("  To: +84-123-456-789");
		System.out.println("  Message:");
		System.out.println(formattedMessage);
		System.out.println("  [SMS] Message sent successfully!");
	}

	@Override
	public String getChannelName() {
		return "SMS";
	}
}
