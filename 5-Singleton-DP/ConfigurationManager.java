import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

	private Properties properties;
	private Map<String, String> cache;

	// Private constructor
	private ConfigurationManager() {
		properties = new Properties();
		cache = new HashMap<>();
		loadConfiguration();
	}

	// Bill Pugh Singleton - Inner static helper class
	private static class SingletonHelper {
		private static final ConfigurationManager INSTANCE = new ConfigurationManager();
	}

	// Public static method to get instance
	public static ConfigurationManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	// Load configuration from file
	private void loadConfiguration() {
		System.out.println("[ConfigManager] Loading configuration file...");

		// Simulating config loading (in real app, load from file)
		properties.setProperty("db.host", "localhost");
		properties.setProperty("db.port", "5432");
		properties.setProperty("db.name", "enterprisedb");
		properties.setProperty("db.username", "admin");
		properties.setProperty("db.password", "secret123");

		properties.setProperty("app.name", "EnterpriseSoft ERP");
		properties.setProperty("app.version", "2.5.0");
		properties.setProperty("app.environment", "production");

		properties.setProperty("feature.reporting.enabled", "true");
		properties.setProperty("feature.analytics.enabled", "false");

		properties.setProperty("business.tax.rate", "10");
		properties.setProperty("business.discount.max", "50");

		System.out.println("[ConfigManager] Configuration loaded successfully!");
		System.out.println("[ConfigManager] Total properties: " + properties.size());
	}

	// Get property value
	public String getProperty(String key) {
		// Check cache first
		if (cache.containsKey(key)) {
			return cache.get(key);
		}

		// Get from properties and cache it
		String value = properties.getProperty(key);
		if (value != null) {
			cache.put(key, value);
		}
		return value;
	}

	// Get property with default value
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value != null ? value : defaultValue;
	}

	// Set property (runtime configuration update)
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
		cache.put(key, value);
		System.out.println("[ConfigManager] Property updated: " + key + " = " + value);
	}

	// Get database URL
	public String getDatabaseUrl() {
		String host = getProperty("db.host");
		String port = getProperty("db.port");
		String dbName = getProperty("db.name");
		return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
	}

	// Check if feature is enabled
	public boolean isFeatureEnabled(String featureName) {
		String key = "feature." + featureName + ".enabled";
		String value = getProperty(key, "false");
		return Boolean.parseBoolean(value);
	}

	// Display all configuration
	public void displayConfiguration() {
		System.out.println("\n=== Configuration Settings ===");
		System.out.println("Database: " + getDatabaseUrl());
		System.out.println("App Name: " + getProperty("app.name"));
		System.out.println("Version: " + getProperty("app.version"));
		System.out.println("Environment: " + getProperty("app.environment"));
		System.out.println("Reporting Enabled: " + isFeatureEnabled("reporting"));
		System.out.println("Analytics Enabled: " + isFeatureEnabled("analytics"));
		System.out.println("Tax Rate: " + getProperty("business.tax.rate") + "%");
		System.out.println("==============================\n");
	}

	// Prevent cloning
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton cannot be cloned");
	}
}
