public class ECommerceDemo {

	public static void main(String[] args) {
		OrderFacade orderFacade = new OrderFacade();
		String orderId = orderFacade.placeOrder(
				"CUST001", "LAPTOP-X1", 2,
				"4532-1234-5678", "123",
				"123 Main St, Hanoi",
				"customer@email.com",
				"+84-123-456-789");

		if (orderId != null) {
			System.out.println("✓ Order placed successfully: " + orderId);
		}
		boolean cancelled = orderFacade.cancelOrder(
				orderId, "LAPTOP-X1", 2, "TXN123456", "SHIP789");

		if (cancelled) {
			System.out.println("\n✓ Order cancelled successfully");
		}
	}
}
