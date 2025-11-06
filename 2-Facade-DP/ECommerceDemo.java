public class ECommerceDemo {

	public static void main(String[] args) {
		OrderFacade orderFacade = new OrderFacade();
		System.out.println("--- CASE 1: Place Order (Success) ---\n");
		String orderId = orderFacade.placeOrder(
				"CUST001", "LAPTOP-X1", 2,
				"4532-1234-5678", "123",
				"123 Main St, Hanoi",
				"customer@email.com",
				"+84-123-456-789");

		if (orderId != null) {
			System.out.println("\n✓ Order placed successfully: " + orderId);
		}

		System.out.println("\n--- CASE 2: Cancel Order ---\n");
		boolean cancelled = orderFacade.cancelOrder(
				orderId, "LAPTOP-X1", 2, "TXN123456", "SHIP789");

		if (cancelled) {
			System.out.println("\n✓ Order cancelled successfully");
		}
	}
}
