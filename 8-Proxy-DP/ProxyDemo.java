import java.util.ArrayList;
import java.util.List;

public class ProxyDemo {

	public static void main(String[] args) {

		System.out.println("=== PROXY PATTERN DEMO ===");
		System.out.println("StreamFlix Video Platform\n");

		// Create users
		User freeUser = new User("Bob", false);
		User premiumUser = new User("Lisa", true);

		System.out.println("Users: Bob (Free), Lisa (Premium)");

		// Test 1: Loading Video Library with Proxies (Fast!)
		System.out.println("\n=== TEST 1: LOADING VIDEO LIBRARY ===");

		long startTime = System.currentTimeMillis();

		List<Video> videoLibrary = new ArrayList<>();
		videoLibrary.add(new VideoProxy("Cooking_Tutorial.mp4", false));
		videoLibrary.add(new VideoProxy("Tech_Review.mp4", false));
		videoLibrary.add(new VideoProxy("Travel_Vlog.mp4", false));
		videoLibrary.add(new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser));
		videoLibrary.add(new PremiumVideoProxy("Masterclass_Series.mp4", freeUser));

		long endTime = System.currentTimeMillis();

		System.out.println("✓ Library loaded: " + videoLibrary.size() + " videos");
		System.out.println("✓ Loading time: " + (endTime - startTime) + "ms");
		System.out.println("✓ Memory: ~5MB (proxies only)");

		// Test 2: Browsing Videos (Display without loading)
		System.out.println("\n=== TEST 2: BROWSING CATALOG ===");
		for (Video video : videoLibrary) {
			video.display();
		}

		// Test 3: Playing Free Video (Lazy Loading)
		System.out.println("\n=== TEST 3: LAZY LOADING ===");

		Video cookingTutorial = videoLibrary.get(0);
		System.out.println("Bob plays: " + cookingTutorial.getTitle());
		cookingTutorial.play();  // First time: loads video (2 seconds)

		System.out.println("\n--- Playing Same Video Again ---");
		cookingTutorial.play();  // Second time: instant (cached)

		// Test 4: Free User tries Premium Video (Access Denied)
		System.out.println("\n=== TEST 4: ACCESS CONTROL ===");

		Video exclusiveConcert = videoLibrary.get(3);
		System.out.println("Bob tries: " + exclusiveConcert.getTitle());
		exclusiveConcert.play();  // Access denied!

		// Test 5: Free User Subscribes
		System.out.println("\n=== TEST 5: SUBSCRIPTION ===");
		freeUser.subscribe();

		// Test 6: Now Premium User can watch Premium Video
		System.out.println("\n=== TEST 6: PREMIUM ACCESS ===");

		Video premiumConcert = new PremiumVideoProxy("Exclusive_Concert.mp4", freeUser);
		System.out.println("Bob (premium) plays: " + premiumConcert.getTitle());
		premiumConcert.play();  // Access granted!

		// Test 7: Premium User (Lisa) watches Premium Video
		System.out.println("\n=== TEST 7: ANOTHER PREMIUM USER ===");

		Video masterclass = new PremiumVideoProxy("Masterclass_Series.mp4", premiumUser);
		System.out.println("Lisa plays: " + masterclass.getTitle());
		masterclass.play();
	}
}
