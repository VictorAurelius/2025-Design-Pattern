import java.util.ArrayList;
import java.util.List;

public class FlyweightDemo {

	public static void main(String[] args) {

		System.out.println("=== Flyweight Pattern Demo ===\n");

		// Create 10,000 video objects
		System.out.println("Creating 10,000 video objects...");
		List<Video> videos = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			int x = (i % 50) * 200;
			int y = (i / 50) * 150;
			videos.add(new Video("Video_" + (i + 1), x, y));
		}
		System.out.println("Created 10,000 videos (storing only positions)\n");

		// Test 1: Render first 5 videos (shows flyweight creation)
		System.out.println("--- Test 1: Rendering First 5 Videos ---");
		for (int i = 0; i < 5; i++) {
			System.out.println("\nRendering " + videos.get(i).getTitle() + ":");
			videos.get(i).renderIcons();
		}

		// Test 2: Render videos 100-105 (shows flyweight reuse)
		System.out.println("\n--- Test 2: Rendering Videos 100-105 (Flyweight Reuse) ---");
		for (int i = 100; i < 105; i++) {
			System.out.println("\nRendering " + videos.get(i).getTitle() + ":");
			videos.get(i).renderIcons();
		}

		// Test 3: Render all 4 icon types for one video
		System.out.println("\n--- Test 3: All 4 Icons for One Video ---");
		Video video = videos.get(500);
		System.out.println("Video: " + video.getTitle() + " at (" + video.getX() + "," + video.getY() + ")");

		VideoIcon play = IconFactory.getIcon("play");
		VideoIcon pause = IconFactory.getIcon("pause");
		VideoIcon like = IconFactory.getIcon("like");
		VideoIcon share = IconFactory.getIcon("share");

		play.render(video.getX(), video.getY(), video.getTitle());
		pause.render(video.getX() + 70, video.getY(), video.getTitle());
		like.render(video.getX() + 140, video.getY(), video.getTitle());
		share.render(video.getX() + 210, video.getY(), video.getTitle());

		// Show factory statistics
		IconFactory.printStatistics();

		// Memory savings calculation
		System.out.println("\n--- Memory Savings ---");
		int numVideos = 10000;
		int iconsPerVideo = 4;
		int iconSizeKB = 500;

		long memoryWithoutFlyweight = (long) numVideos * iconsPerVideo * iconSizeKB;
		long flyweightMemory = IconFactory.getPoolSize() * iconSizeKB;
		long extrinsicMemory = numVideos * 16 / 1024;
		long memoryWithFlyweight = flyweightMemory + extrinsicMemory;

		System.out.println("WITHOUT Flyweight: " + (memoryWithoutFlyweight / 1024) + " MB");
		System.out.println("WITH Flyweight: " + (memoryWithFlyweight / 1024.0) + " MB");

		long savedMemory = memoryWithoutFlyweight - memoryWithFlyweight;
		double reductionRatio = (double) memoryWithoutFlyweight / memoryWithFlyweight;
		System.out.println("Memory saved: " + (savedMemory / 1024) + " MB");
		System.out.println("Reduction: " + String.format("%.0f", reductionRatio) + "x");
	}
}
