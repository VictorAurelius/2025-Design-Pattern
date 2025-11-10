public class SecurityCamera extends SmartDevice {

	private boolean isRecording = false;
	private boolean surveillanceMode = false;

	public SecurityCamera() {
		this.deviceName = "Security Camera";
	}

	public void startRecording() {
		if (!isRecording) {
			isRecording = true;
			System.out.println("[" + deviceName + "] Recording started");
			hub.notify(this, "recording_started");
		} else {
			System.out.println("[" + deviceName + "] Already recording");
		}
	}

	public void stopRecording() {
		if (isRecording) {
			isRecording = false;
			System.out.println("[" + deviceName + "] Recording stopped");
		} else {
			System.out.println("[" + deviceName + "] Not currently recording");
		}
	}

	public void enableSurveillance() {
		surveillanceMode = true;
		startRecording();
		System.out.println("[" + deviceName + "] Surveillance mode enabled");
	}

	public void disableSurveillance() {
		surveillanceMode = false;
		stopRecording();
		System.out.println("[" + deviceName + "] Surveillance mode disabled");
	}
}
