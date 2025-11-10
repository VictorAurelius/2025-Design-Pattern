public class SmartHomeController implements SmartHomeHub {

	private MotionSensor motionSensor;
	private SecurityCamera securityCamera;
	private SmartLight smartLight;
	private Thermostat thermostat;

	private String currentMode = "NORMAL";
	private boolean isNightTime = false;

	public SmartHomeController() {
		System.out.println("  Smart Home Controller Initialized");
	}

	// Register devices
	public void setMotionSensor(MotionSensor sensor) {
		this.motionSensor = sensor;
	}

	public void setSecurityCamera(SecurityCamera camera) {
		this.securityCamera = camera;
	}

	public void setSmartLight(SmartLight light) {
		this.smartLight = light;
	}

	public void setThermostat(Thermostat thermostat) {
		this.thermostat = thermostat;
	}

	@Override
	public void registerDevice(SmartDevice device) {
		device.setHub(this);
		System.out.println("[Hub] Device registered: " + device.getDeviceName());
	}

	public void setMode(String mode) {
		this.currentMode = mode;
		System.out.println("\n[Hub] Mode changed to: " + mode);
	}

	public void setNightTime(boolean isNight) {
		this.isNightTime = isNight;
	}

	@Override
	public void notify(SmartDevice device, String event) {
		System.out.println("\n[Hub] Received notification from " + device.getDeviceName() + ": " + event);
		System.out.println("[Hub] Current mode: " + currentMode);

		// Coordination logic based on event and current state
		switch (event) {
			case "motion_detected":
				handleMotionDetection();
				break;
			case "recording_started":
				handleRecordingStarted();
				break;
			case "temperature_changed":
				handleTemperatureChange();
				break;
			case "light_turned_on":
				handleLightOn();
				break;
			default:
				System.out.println("[Hub] No specific action for this event");
		}
	}

	// Coordination Methods

	private void handleMotionDetection() {
		System.out.println("[Hub] Coordinating response to motion detection...");

		if (currentMode.equals("SECURITY") || isNightTime) {
			// Security/Night mode: Full alert
			System.out.println("[Hub] → Activating SECURITY protocol");
			securityCamera.startRecording();
			smartLight.turnOn(100);  // Full brightness
		} else if (currentMode.equals("WELCOME_HOME")) {
			// Welcome home mode: Comfort
			System.out.println("[Hub] → Activating WELCOME HOME protocol");
			smartLight.turnOn(50);   // Medium brightness
			thermostat.setTemperature(24);
			securityCamera.stopRecording();
		} else {
			// Normal mode: Basic response
			System.out.println("[Hub] → Normal mode response");
			smartLight.turnOn(70);
		}
	}

	private void handleRecordingStarted() {
		System.out.println("[Hub] Coordinating support for recording...");

		// Check if it's dark, turn on lights to help recording
		if (isNightTime || currentMode.equals("SECURITY")) {
			System.out.println("[Hub] → Turning on lights to assist recording");
			smartLight.turnOn(80);
		}
	}

	private void handleTemperatureChange() {
		System.out.println("[Hub] Temperature changed, checking mode...");

		if (currentMode.equals("AWAY")) {
			System.out.println("[Hub] → AWAY mode: Reverting temperature to eco mode");
			thermostat.setTemperature(18);  // Eco temperature
		}
	}

	private void handleLightOn() {
		System.out.println("[Hub] Lights turned on");
		// Could coordinate with other devices if needed
	}

	// Mode-specific coordination methods

		public void activateSecurityMode() {
		System.out.println("ACTIVATING SECURITY MODE");
		setMode("SECURITY");
		securityCamera.enableSurveillance();
		motionSensor.increaseSensitivity();
		thermostat.setTemperature(20);  // Moderate temperature
		System.out.println("[Hub] Security mode activated successfully");
	}

	public void activateAwayMode() {
		System.out.println("ACTIVATING AWAY MODE");
		setMode("AWAY");
		smartLight.turnOff();
		securityCamera.enableSurveillance();
		thermostat.setTemperature(18);  // Eco temperature
		motionSensor.increaseSensitivity();
		System.out.println("[Hub] Away mode activated successfully");
	}

	public void activateWelcomeHomeMode() {
		System.out.println("ACTIVATING WELCOME HOME MODE");
		setMode("WELCOME_HOME");
		smartLight.turnOn(60);
		thermostat.setTemperature(24);  // Comfortable temperature
		securityCamera.stopRecording();
		System.out.println("[Hub] Welcome Home mode activated successfully");
	}
}
