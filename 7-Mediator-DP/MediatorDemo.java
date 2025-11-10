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
		System.out.println("\n--- Registering Devices ---");
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
		System.out.println("TEST 1: Normal Mode - Motion Detection");
		hub.setMode("NORMAL");
		motionSensor.detectMotion();

		// Test 2: Security Mode (Night Time)
		System.out.println("TEST 2: Security Mode - Motion Detection at Night");
		hub.setNightTime(true);
		hub.activateSecurityMode();
		motionSensor.detectMotion();

		// Test 3: Welcome Home Mode
		System.out.println("TEST 3: Welcome Home Mode");
		hub.setNightTime(false);
		hub.activateWelcomeHomeMode();
		motionSensor.detectMotion();

		// Test 4: Away Mode
		System.out.println("TEST 4: Away Mode");
		hub.activateAwayMode();

		// Test 5: Manual Camera Recording (triggers coordination)
		System.out.println("TEST 5: Manual Camera Recording");
		hub.setNightTime(true);
		securityCamera.startRecording();

		// Test 6: Thermostat adjustment in Away mode
		System.out.println("TEST 6: Temperature Adjustment in Away Mode");
		thermostat.setTemperature(25);  // Hub will revert to eco mode

	}
}
