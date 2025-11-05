public interface SmartHomeHub {

	void notify(SmartDevice device, String event);
	void registerDevice(SmartDevice device);
}
