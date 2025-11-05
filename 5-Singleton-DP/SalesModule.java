public class SalesModule {

	private String moduleName;

	public SalesModule() {
		this.moduleName = "Sales Module";
	}

	public void initialize() {
		System.out.println("\n[" + moduleName + "] Initializing...");

		ConfigurationManager config = ConfigurationManager.getInstance();

		System.out.println("[" + moduleName + "] Database URL: " + config.getDatabaseUrl());
		System.out.println("[" + moduleName + "] Tax Rate: " + config.getProperty("business.tax.rate") + "%");
		System.out.println("[" + moduleName + "] ConfigManager hashCode: " + config.hashCode());
	}

	public void processSale(double amount) {
		ConfigurationManager config = ConfigurationManager.getInstance();
		int taxRate = Integer.parseInt(config.getProperty("business.tax.rate"));
		double tax = amount * taxRate / 100;
		double total = amount + tax;

		System.out.println("[" + moduleName + "] Sale Amount: $" + amount);
		System.out.println("[" + moduleName + "] Tax (" + taxRate + "%): $" + tax);
		System.out.println("[" + moduleName + "] Total: $" + total);
	}
}
