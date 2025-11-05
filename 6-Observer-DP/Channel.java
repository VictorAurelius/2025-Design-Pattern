public interface Channel {

	void attach(Subscriber subscriber);
	void detach(Subscriber subscriber);
	void notifySubscribers();
	String getChannelName();
}
