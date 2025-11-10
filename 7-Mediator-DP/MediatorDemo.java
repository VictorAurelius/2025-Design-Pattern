public class MediatorDemo {

	public static void main(String[] args) {
		// Create Mediator
		SmartHomeController hub = new SmartHomeController();

		// Create Devices (Colleagues)
		MotionSensor motionSensor = new MotionSensor();
		SecurityCamera securityCamera = new SecurityCamera();
		SmartLight smartLight = new SmartLight();
		Thermostat thermostat = new Thermostat();

		// Register devices with mediator
		System.out.println("=== DEVICE REGISTRATION ===");
		hub.registerDevice(motionSensor);
		hub.registerDevice(securityCamera);
		hub.registerDevice(smartLight);
		hub.registerDevice(thermostat);

		// Set device references in mediator
		hub.setMotionSensor(motionSensor);
		hub.setSecurityCamera(securityCamera);
		hub.setSmartLight(smartLight);
		hub.setThermostat(thermostat);

		// Test 1: Normal mode - Motion Detection
		System.out.println("\n=== TEST 1: NORMAL MODE ===");
		hub.setMode("NORMAL");
		motionSensor.detectMotion();

		// Test 2: Security Mode (Night Time)
		System.out.println("\n=== TEST 2: SECURITY MODE ===");
		hub.setNightTime(true);
		hub.activateSecurityMode();
		motionSensor.detectMotion();

		// Test 3: Welcome Home Mode
		System.out.println("\n=== TEST 3: WELCOME HOME MODE ===");
		hub.setNightTime(false);
		hub.activateWelcomeHomeMode();
		motionSensor.detectMotion();

		// Test 4: Away Mode
		hub.activateAwayMode();

		// Test 5: Manual Camera Recording (triggers coordination)
		System.out.println("\n=== TEST 5: CAMERA RECORDING ===");
		hub.setNightTime(true);
		securityCamera.startRecording();

		// Test 6: Thermostat adjustment in Away mode
		System.out.println("\n=== TEST 6: TEMPERATURE IN AWAY MODE ===");
		thermostat.setTemperature(25);  // Hub will revert to eco mode

	}
}
