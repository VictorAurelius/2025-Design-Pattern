public class MediatorDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════╗");
		System.out.println("║     MEDIATOR PATTERN DEMO                     ║");
		System.out.println("║     Smart Home Automation System              ║");
		System.out.println("╚════════════════════════════════════════════════╝");

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
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 1: Normal Mode - Motion Detection");
		System.out.println("═══════════════════════════════════════════════");
		hub.setMode("NORMAL");
		motionSensor.detectMotion();

		// Test 2: Security Mode (Night Time)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 2: Security Mode - Motion Detection at Night");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(true);
		hub.activateSecurityMode();
		pause(1);
		motionSensor.detectMotion();

		// Test 3: Welcome Home Mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 3: Welcome Home Mode");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(false);
		hub.activateWelcomeHomeMode();
		pause(1);
		motionSensor.detectMotion();

		// Test 4: Away Mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 4: Away Mode");
		System.out.println("═══════════════════════════════════════════════");
		hub.activateAwayMode();

		// Test 5: Manual Camera Recording (triggers coordination)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 5: Manual Camera Recording");
		System.out.println("═══════════════════════════════════════════════");
		hub.setNightTime(true);
		securityCamera.startRecording();

		// Test 6: Thermostat adjustment in Away mode
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 6: Temperature Adjustment in Away Mode");
		System.out.println("═══════════════════════════════════════════════");
		thermostat.setTemperature(25);  // Hub will revert to eco mode

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════╗");
		System.out.println("║              SUMMARY                          ║");
		System.out.println("╚════════════════════════════════════════════════╝");
		System.out.println("✓ Devices communicate only through SmartHomeHub");
		System.out.println("✓ Devices don't know about each other");
		System.out.println("✓ Hub coordinates complex interactions");
		System.out.println("✓ Centralized coordination logic");
		System.out.println("✓ Easy to add new devices or modes");
		System.out.println("✓ Reduced coupling from O(n²) to O(n)");
		System.out.println("════════════════════════════════════════════════");
	}

	private static void pause(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
