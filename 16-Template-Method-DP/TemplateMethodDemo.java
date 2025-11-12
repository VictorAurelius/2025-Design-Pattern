public class TemplateMethodDemo {

	public static void main(String[] args) {

		System.out.println("=== Template Method Pattern Demo ===\n");

		// Test 1: Standard video processing
		System.out.println("--- Test 1: Standard Video (1080p) ---");
		VideoProcessor standardProcessor = new StandardVideoProcessor();
		standardProcessor.processVideo("video001");

		// Test 2: Premium video processing
		System.out.println("\n--- Test 2: Premium Video (4K HDR) ---");
		VideoProcessor premiumProcessor = new PremiumVideoProcessor();
		premiumProcessor.processVideo("premium001");

		// Test 3: Live stream processing
		System.out.println("\n--- Test 3: Live Stream Recording ---");
		VideoProcessor liveProcessor = new LiveStreamProcessor();
		liveProcessor.processVideo("livestream001");

		System.out.println("\n--- Summary ---");
		System.out.println("All processors followed the same 7-step pipeline:");
		System.out.println("  1. Validate -> 2. Preprocess -> 3. Encode");
		System.out.println("  4. Optimize -> 5. Watermark -> 6. Save -> 7. Notify");
	}
}
