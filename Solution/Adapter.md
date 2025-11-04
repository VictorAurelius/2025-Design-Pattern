# Mẫu thiết kế Adapter (Adapter Pattern)

## 1. Mô tả mẫu Adapter

**Mẫu Adapter** (Adapter Pattern) là một mẫu thiết kế cấu trúc cho phép các giao diện không tương thích có thể làm việc cùng nhau. Adapter hoạt động như một lớp trung gian, chuyển đổi giao diện của một lớp (Adaptee) thành giao diện mà client mong đợi (Target).

### Các thành phần chính:

- **Target**: Giao diện mà client muốn sử dụng
- **Adaptee**: Lớp có chức năng cần thiết nhưng giao diện không tương thích
- **Adapter**: Lớp trung gian chuyển đổi giao diện từ Adaptee sang Target
- **Client**: Đối tượng sử dụng Target interface

### Khi nào sử dụng:

- Khi cần tích hợp thư viện bên thứ ba có giao diện khác
- Khi muốn tái sử dụng lớp cũ mà không sửa đổi mã nguồn
- Khi cần làm việc với nhiều hệ thống có API khác nhau
- Khi có lớp đã tồn tại và giao diện của nó không khớp với yêu cầu hiện tại

---

## 2. Mô tả bài toán

Kỹ sư Sarah làm việc tại một nhà máy sản xuất thực phẩm, nơi việc giám sát nhiệt độ trong các kho lạnh là cực kỳ quan trọng. Nhà máy hiện đang sử dụng hệ thống cảm biến nhiệt độ cũ từ nhà cung cấp Mỹ, đã hoạt động ổn định trong 10 năm qua.

Hệ thống cảm biến cũ sử dụng interface **LegacyTemperatureSensor** với các đặc điểm:
- Đo nhiệt độ theo thang Fahrenheit (°F)
- Trả về giá trị đơn giản: nhiệt độ và ID cảm biến
- Đã được tích hợp vào khoảng 50 module khác nhau trong hệ thống

Ban quản lý quyết định nâng cấp lên hệ thống giám sát hiện đại **SmartMonitoringSystem** từ nhà cung cấp Châu Âu, cung cấp nhiều tính năng hơn như cảnh báo tự động, phân tích xu hướng, và báo cáo chi tiết.

### Vấn đề phát sinh:

1. **Giao diện không tương thích**:
   - Hệ thống cũ: `getFahrenheit()`, `getSensorId()`
   - Hệ thống mới yêu cầu: `getCelsius()`, `getSensorInfo()`, `getTimestamp()`, `getStatus()`
   - Hệ thống mới cần dữ liệu theo thang Celsius (°C)
   - Hệ thống mới cần metadata bổ sung (timestamp, status)

2. **Chi phí thay đổi lớn**:
   - Có **50 module** đang sử dụng interface `LegacyTemperatureSensor`
   - Thay thế toàn bộ cảm biến vật lý tốn kém (hàng triệu đồng)
   - Rủi ro gián đoạn sản xuất nếu thay đổi hàng loạt
   - Dự án cần hoàn thành trong thời gian ngắn

3. **Không thể thay đổi API**:
   - Sarah không thể thay đổi firmware của cảm biến cũ
   - Không thể thay đổi yêu cầu của hệ thống SmartMonitoringSystem
   - Không muốn refactor 50 module hiện có

Sarah cần một giải pháp để hệ thống giám sát mới có thể đọc dữ liệu từ cảm biến cũ mà không cần thay đổi hardware hoặc refactor code lớn.

---

## 3. Yêu cầu bài toán

### Input (Điều kiện ban đầu):
Hệ thống đang sử dụng interface **LegacyTemperatureSensor** với:
- `getFahrenheit()`: Trả về nhiệt độ theo Fahrenheit (double)
- `getSensorId()`: Trả về mã định danh cảm biến (String)
- Khoảng 50 module đang sử dụng interface này
- Cảm biến vật lý đã được lắp đặt và hoạt động tốt

### Problem (Vấn đề):
Hệ thống giám sát mới **SmartMonitoringSystem** yêu cầu interface **TemperatureReading**:
- `getCelsius()`: Trả về nhiệt độ theo Celsius (double)
- `getSensorInfo()`: Trả về thông tin chi tiết cảm biến (String format khác)
- `getTimestamp()`: Trả về thời điểm đo (long milliseconds)
- `getStatus()`: Trả về trạng thái hoạt động (String: "NORMAL", "WARNING", "CRITICAL")

**Điểm không tương thích**:
- Đơn vị đo khác nhau: Fahrenheit vs Celsius
- Format thông tin khác nhau: simple ID vs detailed info
- Thiếu metadata: không có timestamp và status
- Không thể sửa đổi cảm biến cũ
- Không thể thay đổi yêu cầu của SmartMonitoringSystem

### Solution (Giải pháp):
Sử dụng **Adapter Pattern** để:
- Tạo lớp **TemperatureSensorAdapter** implements **TemperatureReading**
- Adapter bao bọc (wrap) đối tượng **LegacyTemperatureSensor**
- Adapter chuyển đổi:
  - Fahrenheit → Celsius: `(F - 32) × 5/9`
  - Simple ID → Detailed info: thêm prefix và format
  - Tạo timestamp khi đọc dữ liệu
  - Tính status dựa trên nhiệt độ (cold/normal/warning/critical)

### Expected Output (Kết quả mong đợi):
- SmartMonitoringSystem có thể đọc dữ liệu từ cảm biến cũ
- Không cần thay đổi hardware hoặc refactor 50 module cũ
- Dữ liệu được chuyển đổi chính xác sang Celsius với metadata đầy đủ
- Dễ dàng bảo trì và mở rộng trong tương lai
- Có thể thêm nhiều adapter cho các loại cảm biến khác

---

## 4. Hiệu quả của việc sử dụng Adapter Pattern

### Lợi ích cụ thể:

1. **Tiết kiệm chi phí lớn**:
   - Không cần thay thế cảm biến vật lý (tiết kiệm hàng triệu đồng)
   - Không cần refactor 50 module hiện có
   - Giảm thời gian triển khai từ nhiều tháng xuống vài ngày

2. **Tích hợp linh hoạt**:
   - Có thể sử dụng cả cảm biến cũ và mới cùng lúc
   - Dễ dàng migrate từng phần thay vì thay đổi toàn bộ
   - Không gián đoạn hoạt động sản xuất

3. **Mở rộng dễ dàng**:
   - Có thể tạo adapter cho các loại cảm biến khác
   - Dễ dàng thêm logic chuyển đổi mới
   - Tách biệt logic business và logic chuyển đổi dữ liệu

4. **Tuân thủ nguyên tắc SOLID**:
   - **Single Responsibility**: Adapter chỉ lo chuyển đổi interface
   - **Open/Closed**: Mở rộng bằng adapter mới, không sửa code cũ
   - **Dependency Inversion**: Client phụ thuộc vào abstraction (TemperatureReading)

### So sánh trước và sau khi dùng Adapter:

| Tiêu chí | Không dùng Adapter | Có dùng Adapter |
|----------|-------------------|-----------------|
| Chi phí phần cứng | Thay toàn bộ cảm biến (~triệu đồng) | Giữ nguyên (0 đồng) |
| Số module cần sửa | ~50 modules | 0 modules |
| Thời gian triển khai | 2-3 tháng | 3-5 ngày |
| Rủi ro gián đoạn | Cao | Thấp |
| Khả năng rollback | Khó | Dễ (xóa adapter) |
| Tích hợp dần dần | Không thể | Có thể |

### Trade-offs (Đánh đổi):

**Ưu điểm**:
- Tiết kiệm chi phí và thời gian đáng kể
- Giảm rủi ro khi thay đổi hệ thống lớn
- Linh hoạt trong việc migrate
- Code cũ vẫn hoạt động như cũ

**Nhược điểm**:
- Thêm một lớp trung gian (overhead nhỏ trong performance)
- Cần maintain thêm code adapter
- Chuyển đổi dữ liệu runtime (tính toán F→C mỗi lần đọc)
- Nếu có quá nhiều loại cảm biến, cần nhiều adapter

**Kết luận**: Trong trường hợp này, lợi ích vượt trội so với nhược điểm, đặc biệt về mặt chi phí và rủi ro.

---

## 5. Cài đặt

### 5.1. Interface LegacyTemperatureSensor (Adaptee)

```java
public interface LegacyTemperatureSensor {

	public double getFahrenheit();
	public String getSensorId();

	public void setFahrenheit(double fahrenheit);
	public void setSensorId(String sensorId);
}
```

### 5.2. Class FahrenheitSensor (Concrete Adaptee)

```java
public class FahrenheitSensor implements LegacyTemperatureSensor {

	private double fahrenheit;
	private String sensorId;

	@Override
	public double getFahrenheit() {
		return fahrenheit;
	}

	@Override
	public String getSensorId() {
		return sensorId;
	}

	@Override
	public void setFahrenheit(double fahrenheit) {
		this.fahrenheit = fahrenheit;
	}

	@Override
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
}
```

### 5.3. Interface TemperatureReading (Target)

```java
public interface TemperatureReading {

	public double getCelsius();
	public String getSensorInfo();
	public long getTimestamp();
	public String getStatus();

	public void setCelsius(double celsius);
	public void setSensorInfo(String sensorInfo);
	public void setTimestamp(long timestamp);
	public void setStatus(String status);
}
```

### 5.4. Class TemperatureSensorAdapter (Adapter)

```java
public class TemperatureSensorAdapter implements TemperatureReading {

	private double celsius;
	private String sensorInfo;
	private long timestamp;
	private String status;

	private final LegacyTemperatureSensor legacySensor;

	public TemperatureSensorAdapter(LegacyTemperatureSensor legacySensor) {
		this.legacySensor = legacySensor;
		convertData();
	}

	@Override
	public double getCelsius() {
		return celsius;
	}

	@Override
	public String getSensorInfo() {
		return sensorInfo;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setCelsius(double celsius) {
		this.celsius = celsius;
	}

	@Override
	public void setSensorInfo(String sensorInfo) {
		this.sensorInfo = sensorInfo;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Chuyển đổi dữ liệu từ LegacyTemperatureSensor sang TemperatureReading
	 */
	private void convertData() {
		// Chuyển đổi Fahrenheit sang Celsius: (F - 32) × 5/9
		double fahrenheit = this.legacySensor.getFahrenheit();
		double celsiusValue = (fahrenheit - 32) * 5.0 / 9.0;
		setCelsius(celsiusValue);

		// Format thông tin cảm biến chi tiết
		String detailedInfo = "Sensor[ID=" + this.legacySensor.getSensorId() +
		                      ", Type=Legacy, Unit=Converted]";
		setSensorInfo(detailedInfo);

		// Thêm timestamp hiện tại
		setTimestamp(System.currentTimeMillis());

		// Xác định status dựa trên nhiệt độ Celsius
		setStatus(determineStatus(celsiusValue));
	}

	/**
	 * Xác định trạng thái dựa trên nhiệt độ
	 * - Below 0°C: CRITICAL (quá lạnh)
	 * - 0-5°C: WARNING (gần ngưỡng)
	 * - 5-25°C: NORMAL (bình thường)
	 * - Above 25°C: CRITICAL (quá nóng)
	 */
	private String determineStatus(double celsius) {
		if (celsius < 0) {
			return "CRITICAL-COLD";
		} else if (celsius >= 0 && celsius < 5) {
			return "WARNING-LOW";
		} else if (celsius >= 5 && celsius <= 25) {
			return "NORMAL";
		} else {
			return "CRITICAL-HOT";
		}
	}
}
```

**Giải thích**:
- Adapter implements interface `TemperatureReading` (Target)
- Adapter giữ reference đến `LegacyTemperatureSensor` (Adaptee)
- Constructor nhận đối tượng legacy sensor và gọi `convertData()`
- Method `convertData()` thực hiện:
  - **Chuyển đổi đơn vị**: Fahrenheit → Celsius theo công thức
  - **Format lại info**: Từ simple ID → detailed sensor info
  - **Thêm timestamp**: Lấy thời gian hiện tại
  - **Tính status**: Dựa trên nhiệt độ Celsius
- Method `determineStatus()` áp dụng business logic đơn giản

### 5.5. Class MonitoringSystem (Client)

```java
public class MonitoringSystem {

	public static void main(String[] args) {

		System.out.println("=== Smart Monitoring System ===\n");

		// Tạo cảm biến legacy (hiện có trong nhà máy)
		LegacyTemperatureSensor sensor1 = new FahrenheitSensor();
		sensor1.setSensorId("WAREHOUSE-A-01");
		sensor1.setFahrenheit(39.2); // 39.2°F = 4°C (cold storage)

		LegacyTemperatureSensor sensor2 = new FahrenheitSensor();
		sensor2.setSensorId("WAREHOUSE-B-07");
		sensor2.setFahrenheit(68.0); // 68°F = 20°C (room temperature)

		LegacyTemperatureSensor sensor3 = new FahrenheitSensor();
		sensor3.setSensorId("WAREHOUSE-C-15");
		sensor3.setFahrenheit(-4.0); // -4°F = -20°C (freezer)

		// Sử dụng Adapter để tích hợp với hệ thống mới
		TemperatureReading reading1 = new TemperatureSensorAdapter(sensor1);
		TemperatureReading reading2 = new TemperatureSensorAdapter(sensor2);
		TemperatureReading reading3 = new TemperatureSensorAdapter(sensor3);

		// Hệ thống mới xử lý dữ liệu
		displayTemperatureReading(reading1);
		displayTemperatureReading(reading2);
		displayTemperatureReading(reading3);
	}

	/**
	 * Phương thức của SmartMonitoringSystem
	 * Chỉ chấp nhận TemperatureReading interface
	 */
	private static void displayTemperatureReading(TemperatureReading reading) {
		System.out.println("Sensor Info: " + reading.getSensorInfo());
		System.out.printf("Temperature: %.2f°C%n", reading.getCelsius());
		System.out.println("Status: " + reading.getStatus());
		System.out.println("Timestamp: " + new java.util.Date(reading.getTimestamp()));
		System.out.println("---");
	}
}
```

---

## 6. Kết quả chạy chương trình

```
=== Smart Monitoring System ===

Sensor Info: Sensor[ID=WAREHOUSE-A-01, Type=Legacy, Unit=Converted]
Temperature: 4.00°C
Status: WARNING-LOW
Timestamp: Mon Nov 04 12:00:00 ICT 2025
---
Sensor Info: Sensor[ID=WAREHOUSE-B-07, Type=Legacy, Unit=Converted]
Temperature: 20.00°C
Status: NORMAL
Timestamp: Mon Nov 04 12:00:01 ICT 2025
---
Sensor Info: Sensor[ID=WAREHOUSE-C-15, Type=Legacy, Unit=Converted]
Temperature: -20.00°C
Status: CRITICAL-COLD
Timestamp: Mon Nov 04 12:00:02 ICT 2025
---
```

**Giải thích kết quả**:

1. **Sensor 1 (WAREHOUSE-A-01)**: 39.2°F → 4°C
   - Chuyển đổi: (39.2 - 32) × 5/9 = 4°C
   - Status: WARNING-LOW (nhiệt độ gần ngưỡng đóng băng)
   - Info được format lại với chi tiết đầy đủ

2. **Sensor 2 (WAREHOUSE-B-07)**: 68°F → 20°C
   - Chuyển đổi: (68 - 32) × 5/9 = 20°C
   - Status: NORMAL (nhiệt độ phòng bình thường)
   - Timestamp được thêm vào tự động

3. **Sensor 3 (WAREHOUSE-C-15)**: -4°F → -20°C
   - Chuyển đổi: (-4 - 32) × 5/9 = -20°C
   - Status: CRITICAL-COLD (nhiệt độ âm sâu, phù hợp freezer)
   - Có thể trigger cảnh báo nếu ngoài khoảng cho phép

**Adapter đã thực hiện**:
- ✅ Chuyển đổi Fahrenheit → Celsius chính xác
- ✅ Format lại thông tin cảm biến
- ✅ Thêm timestamp cho mỗi lần đọc
- ✅ Tính toán status dựa trên business logic
- ✅ SmartMonitoringSystem hoạt động với cảm biến cũ

---

## 7. Sơ đồ UML

### 7.1. Class Diagram

```
┌─────────────────────────┐          ┌──────────────────────────────┐
│   MonitoringSystem      │          │      «interface»             │
│      (Client)           │─────────>│   TemperatureReading         │
└─────────────────────────┘          │        (Target)              │
                                     ├──────────────────────────────┤
                                     │ + getCelsius(): double       │
                                     │ + getSensorInfo(): String    │
                                     │ + getTimestamp(): long       │
                                     │ + getStatus(): String        │
                                     │ + setCelsius(double)         │
                                     │ + setSensorInfo(String)      │
                                     │ + setTimestamp(long)         │
                                     │ + setStatus(String)          │
                                     └──────────────────────────────┘
                                                  △
                                                  │ implements
                                                  │
                    ┌─────────────────────────────┴──────────────────────────────┐
                    │         TemperatureSensorAdapter                           │
                    │                  (Adapter)                                 │
                    ├────────────────────────────────────────────────────────────┤
                    │ - celsius: double                                          │
                    │ - sensorInfo: String                                       │
                    │ - timestamp: long                                          │
                    │ - status: String                                           │
                    │ - legacySensor: LegacyTemperatureSensor                    │
                    ├────────────────────────────────────────────────────────────┤
                    │ + TemperatureSensorAdapter(LegacyTemperatureSensor)        │
                    │ + getCelsius(): double                                     │
                    │ + getSensorInfo(): String                                  │
                    │ + getTimestamp(): long                                     │
                    │ + getStatus(): String                                      │
                    │ + setCelsius(double)                                       │
                    │ + setSensorInfo(String)                                    │
                    │ + setTimestamp(long)                                       │
                    │ + setStatus(String)                                        │
                    │ - convertData()                                            │
                    │ - determineStatus(double): String                          │
                    └──────────────────────┬─────────────────────────────────────┘
                                           │ wraps
                                           │
                                           ▼
                    ┌────────────────────────────────────────────────────────────┐
                    │           «interface»                                      │
                    │      LegacyTemperatureSensor                               │
                    │           (Adaptee)                                        │
                    ├────────────────────────────────────────────────────────────┤
                    │ + getFahrenheit(): double                                  │
                    │ + getSensorId(): String                                    │
                    │ + setFahrenheit(double)                                    │
                    │ + setSensorId(String)                                      │
                    └───────────────────────△────────────────────────────────────┘
                                            │ implements
                                            │
                    ┌───────────────────────┴────────────────────────────────────┐
                    │           FahrenheitSensor                                 │
                    │       (Concrete Adaptee)                                   │
                    ├────────────────────────────────────────────────────────────┤
                    │ - fahrenheit: double                                       │
                    │ - sensorId: String                                         │
                    ├────────────────────────────────────────────────────────────┤
                    │ + getFahrenheit(): double                                  │
                    │ + getSensorId(): String                                    │
                    │ + setFahrenheit(double)                                    │
                    │ + setSensorId(String)                                      │
                    └────────────────────────────────────────────────────────────┘
```

### 7.2. Mô tả các thành phần trong UML

**Interfaces**:
- **TemperatureReading**: Target interface mới mà SmartMonitoringSystem yêu cầu
- **LegacyTemperatureSensor**: Adaptee interface hiện có của cảm biến cũ

**Classes**:
- **FahrenheitSensor**: Concrete implementation của cảm biến legacy
- **TemperatureSensorAdapter**: Adapter class implements TemperatureReading và wraps LegacyTemperatureSensor
- **MonitoringSystem**: Client sử dụng TemperatureReading interface

**Relationships**:
- **implements**: FahrenheitSensor implements LegacyTemperatureSensor, TemperatureSensorAdapter implements TemperatureReading
- **wraps/composition**: TemperatureSensorAdapter chứa reference đến LegacyTemperatureSensor
- **dependency**: MonitoringSystem sử dụng TemperatureReading

**Chức năng chính của Adapter**:
1. Chuyển đổi Fahrenheit → Celsius
2. Format simple ID → detailed sensor info
3. Thêm timestamp
4. Tính toán status dựa trên nhiệt độ

---

## 8. Tổng kết

Adapter Pattern là giải pháp tối ưu cho bài toán tích hợp hệ thống cảm biến nhiệt độ của Sarah. Trong bài toán này:

1. **Vấn đề**: Cần tích hợp cảm biến cũ (Fahrenheit) với hệ thống mới (Celsius + metadata)
2. **Giải pháp**: Tạo TemperatureSensorAdapter làm cầu nối giữa hai interface
3. **Kết quả**: Hệ thống mới hoạt động với cảm biến cũ, tiết kiệm chi phí và thời gian

### Ứng dụng thực tế:

Pattern này đặc biệt hữu ích trong các tình huống:
- **IoT và Industrial Systems**: Tích hợp cảm biến/thiết bị từ nhiều nhà sản xuất
- **Legacy System Integration**: Kết nối hệ thống cũ với platform mới
- **Third-party API**: Tích hợp API bên thứ ba với interface nội bộ
- **Migration Projects**: Chuyển đổi dần dần từ hệ thống cũ sang mới
- **Data Format Conversion**: Chuyển đổi giữa các định dạng dữ liệu khác nhau

### Bài học rút ra:

1. **Không phải lúc nào cũng cần thay thế toàn bộ**: Adapter cho phép tích hợp dần dần
2. **Tách biệt concerns**: Logic chuyển đổi tập trung ở Adapter, không làm "bẩn" code khác
3. **Linh hoạt trong thiết kế**: Có thể tạo nhiều adapter cho các tình huống khác nhau
4. **Giảm rủi ro**: Thay đổi nhỏ, test dễ, rollback nhanh nếu có vấn đề

Trong thực tế, bài toán tương tự thường xảy ra khi:
- Nâng cấp hệ thống ERP cũ
- Tích hợp payment gateway mới
- Chuyển đổi database engine
- Migrate từ monolith sang microservices
- Kết nối với các SaaS platforms khác nhau
