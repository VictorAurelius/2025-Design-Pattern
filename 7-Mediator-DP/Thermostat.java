public class Thermostat extends SmartDevice {

	private int currentTemperature = 22;
	private int targetTemperature = 22;

	public Thermostat() {
		this.deviceName = "Thermostat";
	}

	public void setTemperature(int temperature) {
		// Only notify if the target temperature actually changes
		if (this.targetTemperature != temperature) {
			this.targetTemperature = temperature;
			System.out.println("[" + deviceName + "] Temperature set to " + temperature + "°C");
			hub.notify(this, "temperature_changed");
		} else {
			System.out.println("[" + deviceName + "] Temperature already set to " + temperature + "°C");
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
