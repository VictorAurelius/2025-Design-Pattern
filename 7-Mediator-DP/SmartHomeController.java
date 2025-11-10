public class SmartHomeController implements SmartHomeHub {

	private MotionSensor motionSensor;
	private SecurityCamera securityCamera;
	private SmartLight smartLight;
	private Thermostat thermostat;

	private String currentMode = "NORMAL";
	private boolean isNightTime = false;

	public SmartHomeController() {
		System.out.println("Smart Home Controller Initialized");
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
		System.out.println("[Hub] " + device.getDeviceName() + " registered");
	}

	public void setMode(String mode) {
		this.currentMode = mode;
		System.out.println("[Hub] Mode: " + mode);
	}

	public void setNightTime(boolean isNight) {
		this.isNightTime = isNight;
	}

	@Override
	public void notify(SmartDevice device, String event) {
		System.out.println("[Hub] " + device.getDeviceName() + " → " + event);

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
		}
	}

	// Coordination Methods

	private void handleMotionDetection() {
		if (currentMode.equals("SECURITY") || isNightTime) {
			System.out.println("[Hub] → Security protocol");
			securityCamera.startRecording();
			smartLight.turnOn(100);
		} else if (currentMode.equals("WELCOME_HOME")) {
			System.out.println("[Hub] → Welcome home protocol");
			smartLight.turnOn(50);
			thermostat.setTemperature(24);
			securityCamera.stopRecording();
		} else {
			smartLight.turnOn(70);
		}
	}

	private void handleRecordingStarted() {
		if (isNightTime || currentMode.equals("SECURITY")) {
			System.out.println("[Hub] → Assist recording with lights");
			smartLight.turnOn(80);
		}
	}

	private void handleTemperatureChange() {
		if (currentMode.equals("AWAY")) {
			if (thermostat.getTemperature() != 18) {
				System.out.println("[Hub] → Eco mode: 18°C");
				thermostat.setTemperature(18);
			}
		}
	}

	private void handleLightOn() {
		// Mediator can coordinate with other devices if needed
	}

	// Mode-specific coordination methods

	public void activateSecurityMode() {
		System.out.println("\n=== SECURITY MODE ===");
		setMode("SECURITY");
		securityCamera.enableSurveillance();
		motionSensor.increaseSensitivity();
		thermostat.setTemperature(20);
	}

	public void activateAwayMode() {
		System.out.println("\n=== AWAY MODE ===");
		setMode("AWAY");
		smartLight.turnOff();
		securityCamera.enableSurveillance();
		thermostat.setTemperature(18);
		motionSensor.increaseSensitivity();
	}

	public void activateWelcomeHomeMode() {
		System.out.println("\n=== WELCOME HOME MODE ===");
		setMode("WELCOME_HOME");
		smartLight.turnOn(60);
		thermostat.setTemperature(24);
		securityCamera.stopRecording();
	}
}
