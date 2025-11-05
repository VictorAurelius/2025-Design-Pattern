public abstract class SmartDevice {

	protected SmartHomeHub hub;
	protected String deviceName;

	public void setHub(SmartHomeHub hub) {
		this.hub = hub;
	}

	public String getDeviceName() {
		return deviceName;
	}
}
