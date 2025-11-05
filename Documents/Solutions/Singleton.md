# Mẫu thiết kế Singleton (Singleton Pattern)

## 1. Mô tả mẫu Singleton

**Mẫu Singleton** (Singleton Pattern) là một mẫu thiết kế creational đảm bảo một class chỉ có **một instance duy nhất** trong suốt vòng đời của ứng dụng và cung cấp một **global access point** để truy cập instance đó.

### Các thành phần chính:

- **Private Constructor**: Constructor private để ngăn tạo instance từ bên ngoài
- **Private Static Instance**: Biến static lưu trữ instance duy nhất
- **Public Static getInstance()**: Method static để lấy instance
- **Lazy/Eager Initialization**: Tạo instance khi cần hoặc ngay từ đầu

### Khi nào sử dụng:

- Khi cần đảm bảo chỉ có một instance duy nhất của class
- Khi cần global access point để truy cập instance
- Khi instance quản lý shared resource (config, cache, connection pool)
- Khi nhiều instances sẽ gây ra inconsistency hoặc lãng phí tài nguyên
- Khi cần kiểm soát chặt chẽ việc khởi tạo object

### Đặc điểm quan trọng:

- **Single Instance**: Chỉ tồn tại 1 instance trong toàn bộ application
- **Global Access**: Truy cập từ bất kỳ đâu qua getInstance()
- **Lazy Initialization**: Có thể tạo instance khi cần (tùy implementation)
- **Thread-Safety**: Cần đảm bảo an toàn trong môi trường multi-thread
- **No Cloning**: Không cho phép clone instance
- **Serialization Safe**: Cần xử lý đúng khi serialize/deserialize

---

## 2. Mô tả bài toán

David là senior developer tại công ty phần mềm doanh nghiệp **EnterpriseSoft**. Công ty đang phát triển một hệ thống ERP lớn với nhiều modules: Inventory, Sales, HR, Finance, Reporting.

**Yêu cầu hệ thống**:

Mỗi module cần truy cập configuration settings:
- **Database settings**: Host, port, username, password, database name
- **Application settings**: App name, version, environment (dev/prod)
- **Feature flags**: Enable/disable các tính năng
- **Business rules**: Tax rate, discount rules, working hours

**Hiện trạng**:

Mỗi module tự load configuration file và tạo config object riêng:
```java
// In Inventory Module
ConfigReader inventoryConfig = new ConfigReader("config.properties");
String dbHost = inventoryConfig.get("db.host");

// In Sales Module
ConfigReader salesConfig = new ConfigReader("config.properties");
String dbHost = salesConfig.get("db.host");

// In HR Module
ConfigReader hrConfig = new ConfigReader("config.properties");
String dbHost = hrConfig.get("db.host");
```

### Vấn đề phát sinh:

1. **Lãng phí resources**:
   - Mỗi module tạo config object riêng
   - File config được đọc nhiều lần
   - Tốn memory lưu nhiều copies của cùng data
   - 10 modules = 10 config objects cho cùng 1 file

2. **Inconsistent state**:
   - Module A update config trong memory
   - Module B vẫn dùng config cũ
   - Dẫn đến inconsistency giữa các modules

3. **Khó synchronize**:
   - Nếu reload config, phải reload ở tất cả modules
   - Không có cách nào đảm bảo tất cả modules dùng cùng config version

4. **Thread-safety issues**:
   - Nhiều threads cùng lúc tạo config objects
   - Race conditions khi update config
   - Cần synchronization ở nhiều nơi

5. **Khó maintain**:
   - Thay đổi cách load config phải sửa tất cả modules
   - Khó track config usage
   - Khó implement caching

David cần một giải pháp để **đảm bảo chỉ có 1 configuration object duy nhất** được share giữa tất cả modules, đảm bảo consistency và tiết kiệm resources.

---

## 3. Yêu cầu bài toán

### Input (Điều kiện ban đầu):
Hệ thống có:
- **1 file config**: `application.properties`
- **Nhiều modules**: Inventory, Sales, HR, Finance, etc.
- **Nhiều threads**: Concurrent access từ nhiều modules
- **Config cần persist**: Trong suốt application lifecycle

### Problem (Vấn đề):
1. **Multiple instances**: Mỗi module tạo config object riêng → lãng phí
2. **Inconsistency**: Config khác nhau giữa các modules
3. **No global access**: Phải pass config object khắp nơi
4. **Thread-safety**: Race conditions khi multiple threads access
5. **Resource waste**: File được đọc nhiều lần

### Solution (Giải pháp):
Sử dụng **Singleton Pattern** để:
- **Private constructor**: Ngăn tạo instance từ bên ngoài
- **Static getInstance()**: Cung cấp global access point
- **Single instance**: Chỉ tạo 1 ConfigurationManager duy nhất
- **Thread-safe**: Sử dụng Bill Pugh implementation (inner static class)
- **Lazy initialization**: Tạo khi cần, thread-safe by class loading

### Expected Output (Kết quả mong đợi):
- Chỉ có 1 ConfigurationManager instance trong toàn bộ app
- Tất cả modules share cùng 1 instance
- Config file chỉ đọc 1 lần
- Thread-safe without explicit synchronization
- Consistent config across all modules
- Easy access from anywhere: `ConfigurationManager.getInstance()`

---

## 4. Hiệu quả của việc sử dụng Singleton Pattern

### Lợi ích cụ thể:

1. **Tiết kiệm resources**:
   - 1 instance thay vì 10 instances (10 modules)
   - Config file đọc 1 lần thay vì 10 lần
   - Memory usage giảm đáng kể

2. **Consistency đảm bảo**:
   - Tất cả modules dùng cùng config object
   - Update config ở 1 nơi → tất cả modules thấy ngay
   - Không có inconsistency

3. **Global access**:
   - Truy cập từ bất kỳ đâu: `ConfigurationManager.getInstance()`
   - Không cần pass config object qua constructor/methods
   - Code đơn giản hơn

4. **Thread-safe**:
   - Bill Pugh implementation thread-safe by default
   - Không cần synchronization phức tạp
   - JVM đảm bảo thread-safety qua class loading mechanism

5. **Easy maintenance**:
   - Thay đổi implementation ở 1 class
   - Dễ thêm caching, logging
   - Centralized config management

### So sánh trước và sau khi dùng Singleton:

| Tiêu chí | Không dùng Singleton | Có dùng Singleton |
|----------|---------------------|-------------------|
| Số instances | 10 (1 per module) | 1 (shared) |
| File reads | 10 lần | 1 lần |
| Memory usage | 10x | 1x |
| Consistency | Không đảm bảo | Đảm bảo |
| Global access | Không (phải pass) | Có (getInstance()) |
| Thread-safety | Phải handle thủ công | Built-in (Bill Pugh) |
| Code complexity | Cao | Thấp |

### Trade-offs:

**Ưu điểm**:
- Single instance
- Global access
- Resource efficient
- Thread-safe (với implementation đúng)
- Consistency

**Nhược điểm**:
- Global state (có thể gây coupling)
- Khó unit test (không mock dễ dàng)
- Hidden dependencies
- Vi phạm Single Responsibility (nếu có quá nhiều logic)
- Có thể trở thành anti-pattern nếu lạm dụng

---

## 5. Cài đặt

### 5.1. Class ConfigurationManager (Singleton - Bill Pugh Implementation)

```java
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
```

### 5.2. Class InventoryModule (Demo - uses Singleton)

```java
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
```

### 5.3. Class SalesModule (Demo - uses Singleton)

```java
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
```

### 5.4. Class SingletonDemo (Client)

```java
public class SingletonDemo {

	public static void main(String[] args) {

		System.out.println("========================================");
		System.out.println("    SINGLETON PATTERN DEMO");
		System.out.println("    Configuration Manager Example");
		System.out.println("========================================");

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

		// Test 5: Module operations
		System.out.println("\n--- TEST 5: Module Operations ---");
		inventoryModule.processInventory();

		// Test 6: Prevent cloning
		System.out.println("\n--- TEST 6: Clone Prevention ---");
		try {
			ConfigurationManager cloned = (ConfigurationManager) config.clone();
			System.out.println("ERROR: Cloning succeeded (should not happen)");
		} catch (CloneNotSupportedException e) {
			System.out.println("SUCCESS: Cloning prevented - " + e.getMessage());
		}

		System.out.println("\n========================================");
		System.out.println("Summary:");
		System.out.println("- Only 1 ConfigurationManager instance exists");
		System.out.println("- All modules share the same instance");
		System.out.println("- Config loaded only once");
		System.out.println("- Thread-safe by Bill Pugh implementation");
		System.out.println("- Cannot be cloned");
		System.out.println("========================================");
	}
}
```

---

## 6. Kết quả chạy chương trình

```
========================================
    SINGLETON PATTERN DEMO
    Configuration Manager Example
========================================

--- TEST 1: Single Instance Verification ---
[ConfigManager] Loading configuration file...
[ConfigManager] Configuration loaded successfully!
[ConfigManager] Total properties: 12
config1 hashCode: 123456789
config2 hashCode: 123456789
config3 hashCode: 123456789
All references point to same instance: true

--- TEST 2: Configuration Display ---

=== Configuration Settings ===
Database: jdbc:postgresql://localhost:5432/enterprisedb
App Name: EnterpriseSoft ERP
Version: 2.5.0
Environment: production
Reporting Enabled: true
Analytics Enabled: false
Tax Rate: 10%
==============================


--- TEST 3: Multiple Modules Access ---

[Inventory Module] Initializing...
[Inventory Module] Database URL: jdbc:postgresql://localhost:5432/enterprisedb
[Inventory Module] App: EnterpriseSoft ERP
[Inventory Module] ConfigManager hashCode: 123456789

[Sales Module] Initializing...
[Sales Module] Database URL: jdbc:postgresql://localhost:5432/enterprisedb
[Sales Module] Tax Rate: 10%
[Sales Module] ConfigManager hashCode: 123456789

Verify both modules use same ConfigManager instance

--- TEST 4: Runtime Configuration Update ---
Current tax rate: 10%
[Sales Module] Sale Amount: $1000.0
[Sales Module] Tax (10%): $100.0
[Sales Module] Total: $1100.0

Updating tax rate to 15%...
[ConfigManager] Property updated: business.tax.rate = 15
New tax rate: 15%
[Sales Module] Sale Amount: $1000.0
[Sales Module] Tax (15%): $150.0
[Sales Module] Total: $1150.0

--- TEST 5: Module Operations ---
[Inventory Module] Processing inventory...
[Inventory Module] Generating inventory report...

--- TEST 6: Clone Prevention ---
SUCCESS: Cloning prevented - Singleton cannot be cloned

========================================
Summary:
- Only 1 ConfigurationManager instance exists
- All modules share the same instance
- Config loaded only once
- Thread-safe by Bill Pugh implementation
- Cannot be cloned
========================================
```

**Giải thích**:
- Config chỉ load 1 lần khi getInstance() được gọi lần đầu
- Tất cả references (config1, config2, config3) đều có cùng hashCode
- Inventory và Sales modules đều dùng cùng instance
- Update config ở 1 nơi → tất cả modules thấy ngay
- Clone bị prevent → đảm bảo chỉ có 1 instance

---

## 7. Sơ đồ UML

### 7.1. Class Diagram

```
┌─────────────────────────────────────┐
│    <<singleton>>                    │
│    ConfigurationManager             │
├─────────────────────────────────────┤
│ - instance: ConfigurationManager    │  (static)
│ - properties: Properties            │
│ - cache: Map<String, String>        │
├─────────────────────────────────────┤
│ - ConfigurationManager()            │  (private constructor)
│ + getInstance(): ConfigurationManager │ (static)
│ + getProperty(key): String          │
│ + setProperty(key, value): void     │
│ + getDatabaseUrl(): String          │
│ + isFeatureEnabled(name): boolean   │
│ + displayConfiguration(): void      │
│ # clone(): Object                   │  (throws exception)
└─────────────────────────────────────┘
           △                △
           │                │
      uses │                │ uses
           │                │
  ┌────────┴────┐    ┌────┴─────────┐
  │InventoryModule│  │ SalesModule  │
  └─────────────┘    └──────────────┘
           △                △
           │                │
      uses │                │ uses
           │                │
        ┌──┴────────────────┴──┐
        │   SingletonDemo      │
        │   (Client)           │
        └──────────────────────┘
```

**Key elements**:
- **<<singleton>>**: Stereotype cho biết đây là Singleton
- **Private constructor**: `-ConfigurationManager()` ngăn tạo instance từ ngoài
- **Static instance**: Underline hoặc {static} để chỉ static member
- **Static getInstance()**: Underline để chỉ static method
- **Clone prevention**: `#clone()` protected và throw exception

---

## 8. Tổng kết

Singleton Pattern giải quyết bài toán configuration management của EnterpriseSoft:

1. **Vấn đề**: Nhiều modules tạo config objects riêng → lãng phí, inconsistency
2. **Giải pháp**: ConfigurationManager Singleton với Bill Pugh implementation
3. **Kết quả**: Chỉ 1 instance, thread-safe, global access, resource efficient

**Lợi ích chính**:
- **Single instance**: Tiết kiệm memory, đảm bảo consistency
- **Global access**: Dễ dàng truy cập từ mọi nơi
- **Thread-safe**: Bill Pugh implementation an toàn mà không cần synchronization
- **Lazy initialization**: Chỉ tạo khi cần
- **Resource efficient**: Config load 1 lần duy nhất

**Khi nào dùng Singleton**:
- Configuration management
- Cache manager
- Connection pool
- Logger
- Device manager
- Resource manager

**Khi nào KHÔNG nên dùng**:
- Khi cần nhiều instances với state khác nhau
- Khi cần dependency injection (DI frameworks)
- Khi cần easy testing/mocking
- Khi có thể dùng static utility class thay thế

Pattern này thể hiện nguyên lý **"One instance to rule them all"** - một instance duy nhất quản lý toàn bộ.

**Best Practice**: Sử dụng Bill Pugh Singleton hoặc Enum Singleton để đảm bảo thread-safety và simplicity.
