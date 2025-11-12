public class StrategyPatternDemo {

	public static void main(String[] args) {
		System.out.println("=== Strategy Pattern Demo ===\n");

		// Test 1: H.264 compression (universal compatibility)
		System.out.println("--- Test 1: H.264 (Universal Compatibility) ---");
		VideoCompressor compressor = new VideoCompressor(new H264Strategy());
		compressor.showStrategyInfo();
		compressor.compressVideo("daily-vlog.mp4", "daily-vlog-compressed.mp4");

		// Test 2: H.265 with HDR (premium quality)
		System.out.println("\n--- Test 2: H.265 with HDR (Premium 4K) ---");
		compressor.setStrategy(new H265Strategy(true));  // Enable HDR
		compressor.showStrategyInfo();
		compressor.compressVideo("cinematic-4k.mp4", "cinematic-4k-compressed.mp4");

		// Test 3: VP9 (web streaming)
		System.out.println("\n--- Test 3: VP9 (Web Streaming) ---");
		compressor.setStrategy(new VP9Strategy());
		compressor.showStrategyInfo();
		compressor.compressVideo("gaming-stream.mp4", "gaming-stream-compressed.webm");

		// Test 4: AV1 (maximum compression)
		System.out.println("\n--- Test 4: AV1 (Maximum Compression) ---");
		compressor.setStrategy(new AV1Strategy("slow", 28));
		compressor.showStrategyInfo();
		compressor.compressVideo("archive.mp4", "archive-compressed.mkv");

		// Test 5: Runtime strategy switching
		System.out.println("\n--- Test 5: Runtime Strategy Switching ---");
		System.out.println("Switching strategies on same compressor object:");

		compressor.setStrategy(new H265Strategy(false));  // H.265 without HDR
		System.out.println("Current strategy: " + compressor.getStrategyName());

		compressor.setStrategy(new AV1Strategy("fast", 30));  // AV1 fast preset
		System.out.println("Switched to: " + compressor.getStrategyName());

		// Summary
		System.out.println("\n--- Summary ---");
		System.out.println("Demonstrated 4 compression algorithms");
		System.out.println("Runtime strategy switching: Same object, different behaviors");
		System.out.println("No if-else statements, type-safe algorithm selection");
		System.out.println("Each strategy encapsulates ONE algorithm");
	}
}
