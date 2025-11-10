import java.util.ArrayList;
import java.util.List;

public class YouTubeChannel implements Channel {

	private List<Subscriber> subscribers;
	private String channelName;
	private String latestVideo;
	private String videoDescription;

	public YouTubeChannel(String channelName) {
		this.channelName = channelName;
		this.subscribers = new ArrayList<>();
		System.out.println("YouTube Channel '" + channelName + "' created!");
	}

	@Override
	public void attach(Subscriber subscriber) {
		subscribers.add(subscriber);
		System.out.println("New subscriber added. Total: " + subscribers.size());
	}

	@Override
	public void detach(Subscriber subscriber) {
		int index = subscribers.indexOf(subscriber);
		if (index >= 0) {
			subscribers.remove(index);
			System.out.println("Subscriber removed. Total: " + subscribers.size());
		}
	}

	@Override
	public void notifySubscribers() {
		System.out.println("Notifying " + subscribers.size() + " subscribers...");
		System.out.println("---------------------------------------");
		for (Subscriber subscriber : subscribers) {
			subscriber.update(latestVideo);
		}
		System.out.println("---------------------------------------");
	}

	@Override
	public String getChannelName() {
		return channelName;
	}

	public String getLatestVideo() {
		return latestVideo;
	}

	public String getVideoDescription() {
		return videoDescription;
	}

	// Upload video - automatic notification
	public void uploadVideo(String title, String description) {
		System.out.println("[" + channelName + "] Uploading new video...");
		System.out.println("   Title: " + title);
		System.out.println("   Description: " + description);

		this.latestVideo = title;
		this.videoDescription = description;

		// Automatic notification!
		notifySubscribers();
	}
}
