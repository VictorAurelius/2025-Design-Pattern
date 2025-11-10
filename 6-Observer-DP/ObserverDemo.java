public class ObserverDemo {

	public static void main(String[] args) {
		// Create YouTube Channel (Subject)
		YouTubeChannel channel = new YouTubeChannel("TechReview Pro");

		// Create Subscribers (Observers)
		Subscriber alice = new EmailSubscriber("Alice Johnson", "alice@email.com", channel);
		Subscriber bob = new MobileAppSubscriber("Bob Smith", "iPhone-12345", channel);
		Subscriber charlie = new SMSSubscriber("Charlie Brown", "+1-555-0123", channel);

		// Test 1: Subscribe multiple observers
		System.out.println("=== TEST 1: Multiple Subscriptions ===");
		alice.subscribe();
		bob.subscribe();
		charlie.subscribe();

		// Test 2: Upload first video - all receive notifications
		System.out.println("\n=== TEST 2: First Video Upload ===");
		channel.uploadVideo(
			"iPhone 16 Pro Review",
			"Complete review of Apple's latest flagship phone with camera tests and performance benchmarks."
		);

		// Test 3: One subscriber unsubscribes
		System.out.println("\n=== TEST 3: Unsubscribe ===");
		charlie.unsubscribe();

		// Test 4: Upload second video - only remaining subscribers notified
		System.out.println("\n=== TEST 4: Second Video Upload (After Unsubscribe) ===");
		channel.uploadVideo(
			"MacBook Pro M4 Unboxing",
			"First look at the new MacBook Pro with M4 chip. Unboxing and initial impressions."
		);

		// Test 5: New subscriber joins
		System.out.println("\n=== TEST 5: New Subscriber Joins ===");
		Subscriber diana = new MobileAppSubscriber("Diana Prince", "Android-98765", channel);
		diana.subscribe();

		// Test 6: Upload third video - including new subscriber
		System.out.println("\n=== TEST 6: Third Video Upload (With New Subscriber) ===");
		channel.uploadVideo(
			"Top 5 Gadgets of 2024",
			"My favorite tech gadgets this year including smartphones, laptops, and accessories."
		);

		// Test 7: Re-subscribe previous subscriber
		System.out.println("\n=== TEST 7: Re-subscribe ===");
		charlie.subscribe();

		// Test 8: Final video upload - all subscribers notified
		System.out.println("\n=== TEST 8: Final Video Upload ===");
		channel.uploadVideo(
			"AirPods Pro 3 vs Sony WF-1000XM5",
			"Head-to-head comparison of the best wireless earbuds. Sound quality, ANC, and battery life tested."
		);
	}
}
