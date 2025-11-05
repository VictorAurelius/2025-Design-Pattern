public class OrderFacade {

	private InventorySystem inventory;
	private PaymentProcessor payment;
	private ShippingService shipping;
	private NotificationService notification;

	public OrderFacade() {
		this.inventory = new InventorySystem();
		this.payment = new PaymentProcessor();
		this.shipping = new ShippingService();
		this.notification = new NotificationService();
	}

	public String placeOrder(String customerId, String productId, int quantity,
	                         String cardNumber, String cvv, String address,
	                         String email, String phone) {

		System.out.println("=== Order Facade: Processing Order ===");

		String orderId = "ORD" + System.currentTimeMillis();
		String transactionId = null;
		String trackingNumber = null;

		try {
			if (!inventory.checkStock(productId, quantity)) {
				System.out.println("  [Facade] ERROR: Product out of stock");
				return null;
			}

			if (!inventory.reserveProduct(productId, quantity)) {
				System.out.println("  [Facade] ERROR: Cannot reserve product");
				return null;
			}

			double amount = quantity * 29.99;
			if (!payment.validatePaymentInfo(cardNumber, cvv, amount)) {
				System.out.println("  [Facade] ERROR: Invalid payment info");
				inventory.releaseProduct(productId, quantity);
				return null;
			}

			transactionId = payment.chargePayment(cardNumber, amount);
			if (transactionId == null) {
				System.out.println("  [Facade] ERROR: Payment failed");
				inventory.releaseProduct(productId, quantity);
				return null;
			}

			double shippingCost = shipping.calculateShippingCost(address, quantity * 0.5);
			trackingNumber = shipping.createShipment(address, productId);

			String orderDetails = "Order ID: " + orderId + ", Product: " + productId +
			                     ", Quantity: " + quantity + ", Total: $" + (amount + shippingCost);
			notification.sendEmailConfirmation(email, orderDetails);
			notification.sendSMSNotification(phone, "Your order " + orderId + " is confirmed!");

			System.out.println("=== Order Facade: Order Completed Successfully ===");
			System.out.println("  Order ID: " + orderId);
			System.out.println("  Tracking: " + trackingNumber);

			return orderId;

		} catch (Exception e) {
			System.out.println("  [Facade] ERROR: " + e.getMessage());
			if (transactionId != null) {
				payment.refundPayment(transactionId);
			}
			if (trackingNumber != null) {
				shipping.cancelShipment(trackingNumber);
			}
			inventory.releaseProduct(productId, quantity);
			return null;
		}
	}

	public boolean cancelOrder(String orderId, String productId, int quantity, String transactionId, String trackingNumber) {
		System.out.println("=== Order Facade: Cancelling Order " + orderId + " ===");

		payment.refundPayment(transactionId);
		shipping.cancelShipment(trackingNumber);
		inventory.releaseProduct(productId, quantity);

		System.out.println("=== Order Facade: Order Cancelled Successfully ===");
		return true;
	}
}
