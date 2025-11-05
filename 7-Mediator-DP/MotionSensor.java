public class MotionSensor extends SmartDevice {

	private int sensitivity = 50;

	public MotionSensor() {
		this.deviceName = "Motion Sensor";
	}

	public void detectMotion() {
		System.out.println("\n[" + deviceName + "] ⚠️  Motion detected!");
		hub.notify(this, "motion_detected");
	}

	public void increaseSensitivity() {
		sensitivity = 90;
		System.out.println("[" + deviceName + "] Sensitivity increased to " + sensitivity + "%");
	}

	public void normalSensitivity() {
		sensitivity = 50;
		System.out.println("[" + deviceName + "] Sensitivity set to normal: " + sensitivity + "%");
	}
}
