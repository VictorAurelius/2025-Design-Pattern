public class Thermostat extends SmartDevice {

	private int currentTemperature = 22;
	private int targetTemperature = 22;

	public Thermostat() {
		this.deviceName = "Thermostat";
	}

	public void setTemperature(int temperature) {
		this.targetTemperature = temperature;
		System.out.println("[" + deviceName + "] 🌡️  Temperature set to " + temperature + "°C");

		if (temperature != currentTemperature) {
			hub.notify(this, "temperature_changed");
		}
	}

	public void adjustToCurrentWeather() {
		System.out.println("[" + deviceName + "] Adjusting to current weather conditions");
		setTemperature(23);
	}

	public int getTemperature() {
		return targetTemperature;
	}
}
