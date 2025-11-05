import java.util.ArrayList;
import java.util.List;

public class FlyweightDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════════════════╗");
		System.out.println("║           FLYWEIGHT PATTERN DEMO                           ║");
		System.out.println("║           StreamFlix Video Player UI Icons                 ║");
		System.out.println("║  (Linked: Proxy + Observer + Adapter patterns)            ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");

		// Demo 1: Show the problem
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("PROBLEM: Memory Usage WITHOUT Flyweight");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n📊 Calculation:");
		System.out.println("   10,000 videos × 4 icons × 500KB = 20,000,000 KB");
		System.out.println("   = 20,000 MB = 20 GB 😱");
		System.out.println("\n❌ Result: Browser crashes / Out of Memory");

		// Demo 2: Create large number of videos
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("SOLUTION: Using Flyweight Pattern");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n📺 Creating 10,000 video objects...");

		List<Video> videos = new ArrayList<>();

		// Create 10,000 videos
		for (int i = 0; i < 10000; i++) {
			int x = (i % 50) * 200;
			int y = (i / 50) * 150;
			videos.add(new Video("Video_" + (i + 1), x, y));
		}

		System.out.println("✓ Created 10,000 video objects (only storing position)");

		// Demo 3: Render first 5 videos (show flyweight creation)
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 1: Rendering First 5 Videos");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n→ Watch how flyweights are created ONCE and reused:");

		for (int i = 0; i < 5; i++) {
			System.out.println("\n--- Rendering " + videos.get(i).getTitle() + " ---");
			videos.get(i).renderIcons();
		}

		// Demo 4: Render more videos (show flyweight reuse)
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 2: Rendering Videos 100-105 (Flyweight Reuse)");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n→ All subsequent videos reuse existing flyweights:");

		for (int i = 100; i < 105; i++) {
			System.out.println("\n--- Rendering " + videos.get(i).getTitle() + " ---");
			videos.get(i).renderIcons();
		}

		// Demo 5: Get all 4 icon types for one video
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST 3: Rendering All 4 Icons for One Video");
		System.out.println("═══════════════════════════════════════════════════════════");

		Video video = videos.get(500);
		System.out.println("\n→ Video: " + video.getTitle() + " at (" + video.getX() + "," + video.getY() + ")");

		VideoIcon play = IconFactory.getIcon("play");
		VideoIcon pause = IconFactory.getIcon("pause");
		VideoIcon like = IconFactory.getIcon("like");
		VideoIcon share = IconFactory.getIcon("share");

		System.out.println("\n→ Rendering all icons:");
		play.render(video.getX(), video.getY(), video.getTitle());
		pause.render(video.getX() + 70, video.getY(), video.getTitle());
		like.render(video.getX() + 140, video.getY(), video.getTitle());
		share.render(video.getX() + 210, video.getY(), video.getTitle());

		// Demo 6: Show factory statistics
		IconFactory.printStatistics();

		// Demo 7: Calculate memory savings
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("MEMORY SAVINGS CALCULATION");
		System.out.println("═══════════════════════════════════════════════════════════");

		int numVideos = 10000;
		int iconsPerVideo = 4;
		int iconSizeKB = 500;

		long memoryWithoutFlyweight = (long) numVideos * iconsPerVideo * iconSizeKB;
		long flyweightMemory = IconFactory.getPoolSize() * iconSizeKB;
		long extrinsicMemory = numVideos * 16 / 1024;  // 16 bytes per video → KB
		long memoryWithFlyweight = flyweightMemory + extrinsicMemory;

		System.out.println("\n📊 WITHOUT Flyweight:");
		System.out.println("   " + numVideos + " videos × " + iconsPerVideo + " icons × " + iconSizeKB + " KB");
		System.out.println("   = " + memoryWithoutFlyweight + " KB = " + (memoryWithoutFlyweight / 1024) + " MB");

		System.out.println("\n✅ WITH Flyweight:");
		System.out.println("   Flyweights: " + IconFactory.getPoolSize() + " icons × " + iconSizeKB + " KB = " + flyweightMemory + " KB");
		System.out.println("   Extrinsic state: " + numVideos + " videos × 16 bytes = " + extrinsicMemory + " KB");
		System.out.println("   Total: " + memoryWithFlyweight + " KB = " + (memoryWithFlyweight / 1024.0) + " MB");

		System.out.println("\n💾 MEMORY SAVINGS:");
		long savedMemory = memoryWithoutFlyweight - memoryWithFlyweight;
		double reductionRatio = (double) memoryWithoutFlyweight / memoryWithFlyweight;
		System.out.println("   Memory saved: " + savedMemory + " KB = " + (savedMemory / 1024) + " MB");
		System.out.println("   Reduction: " + String.format("%.0f", reductionRatio) + "x");
		System.out.println("   Percentage: " + String.format("%.2f", (1 - 1.0 / reductionRatio) * 100) + "% memory saved");

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║                      SUMMARY                               ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");
		System.out.println("\n✓ Flyweight Benefits:");
		System.out.println("  - Created only " + IconFactory.getPoolSize() + " flyweight objects (not " + (numVideos * iconsPerVideo) + "!)");
		System.out.println("  - Memory: " + (memoryWithFlyweight / 1024.0) + " MB (not " + (memoryWithoutFlyweight / 1024) + " MB!)");
		System.out.println("  - Reduction: " + String.format("%.0f", reductionRatio) + "x memory savings");
		System.out.println("  - Scalable: Can handle millions of videos");
		System.out.println("  - Fast: Instant page load (not 30 seconds)");

		System.out.println("\n🎬 Context Link: Video Platform uses Flyweight (UI icons) +");
		System.out.println("   Proxy (lazy loading) + Observer (notifications) +");
		System.out.println("   Adapter (media players) = Complete streaming solution!");
		System.out.println("════════════════════════════════════════════════════════════");
	}
}
