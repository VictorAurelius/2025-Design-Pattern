public class ShippingService {

	public double calculateShippingCost(String address, double weight) {
		System.out.println("  [Shipping] Calculating shipping cost to: " + address);
		return 5.99;
	}

	public String createShipment(String address, String productId) {
		System.out.println("  [Shipping] Creating shipment to: " + address);
		return "SHIP" + System.currentTimeMillis();
	}

	public void cancelShipment(String trackingNumber) {
		System.out.println("  [Shipping] Cancelling shipment: " + trackingNumber);
	}
}
