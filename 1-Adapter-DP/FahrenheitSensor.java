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
