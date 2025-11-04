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
