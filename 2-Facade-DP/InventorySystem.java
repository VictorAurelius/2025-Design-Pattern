public class InventorySystem {

	public boolean checkStock(String productId, int quantity) {
		System.out.println("  [Inventory] Checking stock for product: " + productId);
		return true;
	}

	public boolean reserveProduct(String productId, int quantity) {
		System.out.println("  [Inventory] Reserving " + quantity + " units of product: " + productId);
		return true;
	}

	public void releaseProduct(String productId, int quantity) {
		System.out.println("  [Inventory] Releasing " + quantity + " units of product: " + productId);
	}
}
