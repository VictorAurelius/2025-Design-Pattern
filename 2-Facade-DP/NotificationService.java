public class NotificationService {

	public void sendEmailConfirmation(String email, String orderDetails) {
		System.out.println("  [Notification] Sending email to: " + email);
		System.out.println("    Order details: " + orderDetails);
	}

	public void sendSMSNotification(String phone, String message) {
		System.out.println("  [Notification] Sending SMS to: " + phone);
		System.out.println("    Message: " + message);
	}
}
