public class SmartLight extends SmartDevice {

	private boolean isOn = false;
	private int brightness = 0;

	public SmartLight() {
		this.deviceName = "Smart Light";
	}

	public void turnOn(int brightness) {
		this.isOn = true;
		this.brightness = brightness;
		System.out.println("[" + deviceName + "] ON (" + brightness + "%)");
		hub.notify(this, "light_turned_on");
	}

	public void turnOff() {
		this.isOn = false;
		this.brightness = 0;
		System.out.println("[" + deviceName + "] OFF");
	}

	public void adjustBrightness(int level) {
		if (isOn) {
			this.brightness = level;
			System.out.println("[" + deviceName + "] Brightness: " + level + "%");
		}
	}
}
