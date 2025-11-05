/**
 * VideoCompressor - Context class for Strategy pattern
 *
 * Manages video compression and delegates to current compression strategy.
 * Uses COMPOSITION (HAS-A) instead of inheritance (IS-A).
 *
 * BEFORE STRATEGY PATTERN (IF-ELSE HELL):
 * - 200+ lines in compress() method
 * - 4-8 if-else branches for different algorithms
 * - String-based selection ("h264" vs "h256" typo)
 * - Cyclomatic complexity: 8+
 * - Cannot swap algorithms easily
 *
 * AFTER STRATEGY PATTERN (CLEAN):
 * - 5 lines in compressVideo() method
 * - Zero if-else statements
 * - Type-safe strategy selection
 * - Cyclomatic complexity: 1
 * - Runtime algorithm switching via setStrategy()
 */
public class VideoCompressor {

	private CompressionStrategy strategy;

	/**
	 * Create video compressor with specified strategy
	 *
	 * @param strategy Compression strategy to use
	 */
	public VideoCompressor(CompressionStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Set compression strategy
	 * Allows runtime algorithm switching on same object
	 *
	 * @param strategy New compression strategy
	 */
	public void setStrategy(CompressionStrategy strategy) {
		this.strategy = strategy;
		System.out.println("\nSwitching strategy to: " + strategy.getCodecName());
		System.out.println("Compression Ratio: " + strategy.getCompressionRatio());
		System.out.println("Description: " + strategy.getDescription());
	}

	/**
	 * Compress video using current strategy
	 * Delegates to strategy - no if-else needed!
	 *
	 * @param inputFile Input video file
	 * @param outputFile Output compressed file
	 */
	public void compressVideo(String inputFile, String outputFile) {
		System.out.println("\nCompressing: " + inputFile + " → " + outputFile);
		System.out.println();
		System.out.println("Starting compression...");
		strategy.compress(inputFile, outputFile);  // Delegate to strategy!
		System.out.println("Compression complete!");
	}

	/**
	 * Show current strategy information
	 */
	public void showStrategyInfo() {
		System.out.println("\nCurrent Strategy: " + strategy.getCodecName());
		System.out.println("Compression Ratio: " + strategy.getCompressionRatio());
		System.out.println("Description: " + strategy.getDescription());
	}

	/**
	 * Get current strategy
	 *
	 * @return Current compression strategy
	 */
	public CompressionStrategy getStrategy() {
		return strategy;
	}
}
