public class SlackChannel implements NotificationChannel {

	@Override
	public void sendMessage(String formattedMessage) {
		System.out.println("  [Slack] Posting to #general channel...");
		System.out.println("  Workspace: techcorp.slack.com");
		System.out.println("  Message:");
		System.out.println(formattedMessage);
		System.out.println("  [Slack] Message posted successfully!");
	}

	@Override
	public String getChannelName() {
		return "Slack";
	}
}
