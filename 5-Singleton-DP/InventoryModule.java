public class InventoryModule {

	private String moduleName;

	public InventoryModule() {
		this.moduleName = "Inventory Module";
	}

	public void initialize() {
		System.out.println("\n[" + moduleName + "] Initializing...");

		ConfigurationManager config = ConfigurationManager.getInstance();

		System.out.println("[" + moduleName + "] Database URL: " + config.getDatabaseUrl());
		System.out.println("[" + moduleName + "] App: " + config.getProperty("app.name"));
		System.out.println("[" + moduleName + "] ConfigManager hashCode: " + config.hashCode());
	}

	public void processInventory() {
		System.out.println("[" + moduleName + "] Processing inventory...");
		ConfigurationManager config = ConfigurationManager.getInstance();

		if (config.isFeatureEnabled("reporting")) {
			System.out.println("[" + moduleName + "] Generating inventory report...");
		}
	}
}
