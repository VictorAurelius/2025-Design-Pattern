import java.util.ArrayList;
import java.util.List;

public class ProxyDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════╗");
		System.out.println("║        PROXY PATTERN DEMO                     ║");
		System.out.println("║        StreamFlix Video Platform              ║");
		System.out.println("║  (Linked: Observer + Adapter patterns)        ║");
		System.out.println("╚════════════════════════════════════════════════╝");

		// Create users
		User freeUser = new User("Bob", false);
		User premiumUser = new User("Lisa", true);

		System.out.println("\n👤 Users:");
		System.out.println("   Bob - Free user");
		System.out.println("   Lisa - Premium subscriber");

		// Test 1: Loading Video Library with Proxies (Fast!)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 1: App Startup - Loading Video Library");
		System.out.println("═══════════════════════════════════════════════");

		long startTime = System.currentTimeMillis();

		List<Video> videoLibrary = new ArrayList<>();
		videoLibrary.add(new VideoProxy("Cooking_Tutorial.mp4", false));
		videoLibrary.add(new VideoProxy("Tech_Review.mp4", false));
		videoLibrary.add(new VideoProxy("Travel_Vlog.mp4", false));
		videoLibrary.add(new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser));
		videoLibrary.add(new PremiumVideoProxy("Masterclass_Series.mp4", freeUser));

		long endTime = System.currentTimeMillis();

		System.out.println("\n✓ Video library loaded!");
		System.out.println("   Videos in catalog: " + videoLibrary.size());
		System.out.println("   Loading time: " + (endTime - startTime) + "ms");
		System.out.println("   Memory used: ~5MB (only proxies)");
		System.out.println("\n💡 Without proxy: Would take 10+ seconds and 2.5GB memory!");

		// Test 2: Browsing Videos (Display without loading)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 2: Browsing Video Catalog");
		System.out.println("═══════════════════════════════════════════════");

		System.out.println("\n📋 Available videos:");
		for (Video video : videoLibrary) {
			video.display();
		}

		// Test 3: Playing Free Video (Lazy Loading)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 3: Free User Plays Free Video");
		System.out.println("═══════════════════════════════════════════════");

		Video cookingTutorial = videoLibrary.get(0);
		System.out.println("\n👤 Bob clicks on: " + cookingTutorial.getTitle());
		cookingTutorial.play();  // First time: loads video (2 seconds)

		System.out.println("\n\n--- Playing Same Video Again ---");
		cookingTutorial.play();  // Second time: instant (cached)

		// Test 4: Free User tries Premium Video (Access Denied)
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 4: Free User Tries Premium Video");
		System.out.println("═══════════════════════════════════════════════");

		Video exclusiveConcert = videoLibrary.get(3);
		System.out.println("\n👤 Bob (free user) clicks on: " + exclusiveConcert.getTitle());
		exclusiveConcert.play();  // Access denied!

		// Test 5: Free User Subscribes
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 5: User Subscribes to Premium");
		System.out.println("═══════════════════════════════════════════════");

		freeUser.subscribe();

		// Test 6: Now Premium User can watch Premium Video
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 6: Premium User Watches Premium Video");
		System.out.println("═══════════════════════════════════════════════");

		// Need to create new proxy with updated user
		Video premiumConcert = new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser);
		System.out.println("\n👤 Bob (now premium) clicks on: " + premiumConcert.getTitle());
		premiumConcert.play();  // Access granted!

		// Test 7: Premium User (Lisa) watches Premium Video
		System.out.println("\n\n═══════════════════════════════════════════════");
		System.out.println("TEST 7: Another Premium User");
		System.out.println("═══════════════════════════════════════════════");

		Video masterclass = new PremiumVideoProxy("Masterclass_Series.mp4", premiumUser);
		System.out.println("\n👤 Lisa (premium) clicks on: " + masterclass.getTitle());
		masterclass.play();

		// Summary
		System.out.println("\n\n╔════════════════════════════════════════════════╗");
		System.out.println("║              SUMMARY                          ║");
		System.out.println("╚════════════════════════════════════════════════╝");
		System.out.println("✓ Virtual Proxy: Lazy loading videos on demand");
		System.out.println("✓ Protection Proxy: Access control for premium content");
		System.out.println("✓ Fast startup: 5ms vs 10+ seconds without proxy");
		System.out.println("✓ Memory efficient: 5MB vs 2.5GB without proxy");
		System.out.println("✓ Transparent: Same interface for all videos");
		System.out.println("✓ Secure: Premium content protected");
		System.out.println("\n🎬 Context Link: Video platform like YouTube (Observer)");
		System.out.println("   using media players (Adapter) with smart loading!");
		System.out.println("════════════════════════════════════════════════");
	}
}
