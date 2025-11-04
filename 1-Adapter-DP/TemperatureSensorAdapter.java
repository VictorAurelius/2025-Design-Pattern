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
