public class SingletonDemo {

	public static void main(String[] args) {
		// Test 1: Get instance from different places
		System.out.println("\n--- TEST 1: Single Instance Verification ---");

		ConfigurationManager config1 = ConfigurationManager.getInstance();
		ConfigurationManager config2 = ConfigurationManager.getInstance();
		ConfigurationManager config3 = ConfigurationManager.getInstance();

		System.out.println("config1 hashCode: " + config1.hashCode());
		System.out.println("config2 hashCode: " + config2.hashCode());
		System.out.println("config3 hashCode: " + config3.hashCode());
		System.out.println("All references point to same instance: " + (config1 == config2 && config2 == config3));

		// Test 2: Display configuration
		System.out.println("\n--- TEST 2: Configuration Display ---");
		config1.displayConfiguration();

		// Test 3: Multiple modules accessing same instance
		System.out.println("\n--- TEST 3: Multiple Modules Access ---");

		InventoryModule inventoryModule = new InventoryModule();
		inventoryModule.initialize();

		SalesModule salesModule = new SalesModule();
		salesModule.initialize();

		System.out.println("\nVerify both modules use same ConfigManager instance");

		// Test 4: Runtime configuration update
		System.out.println("\n--- TEST 4: Runtime Configuration Update ---");

		ConfigurationManager config = ConfigurationManager.getInstance();
		System.out.println("Current tax rate: " + config.getProperty("business.tax.rate") + "%");

		salesModule.processSale(1000.00);

		System.out.println("\nUpdating tax rate to 15%...");
		config.setProperty("business.tax.rate", "15");

		System.out.println("New tax rate: " + config.getProperty("business.tax.rate") + "%");
		salesModule.processSale(1000.00);

		// Test 5: Prevent cloning
		System.out.println("\n--- TEST 5: Clone Prevention ---");
		try {
			ConfigurationManager cloned = (ConfigurationManager) config.clone();
			System.out.println("ERROR: Cloning succeeded (should not happen)");
		} catch (CloneNotSupportedException e) {
			System.out.println("SUCCESS: Cloning prevented - " + e.getMessage());
		}
	}
}
