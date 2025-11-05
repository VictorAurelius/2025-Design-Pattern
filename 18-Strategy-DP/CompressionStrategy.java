/**
 * CompressionStrategy - Strategy interface for video compression
 *
 * Defines the contract that all compression algorithms must implement.
 * Each concrete strategy encapsulates a specific compression algorithm.
 *
 * STRATEGY PATTERN KEY CONCEPTS:
 * - Define family of algorithms
 * - Encapsulate each algorithm in separate class
 * - Make algorithms interchangeable
 * - Client selects algorithm at runtime
 *
 * Available Strategies:
 * - H264Strategy: Fast, universal compatibility
 * - H265Strategy: High quality, 4K/HDR support
 * - VP9Strategy: Open-source, web-optimized
 * - AV1Strategy: Next-gen, best compression
 */
public interface CompressionStrategy {

	/**
	 * Compress video file using this strategy's algorithm
	 *
	 * @param inputFile Input video file path
	 * @param outputFile Output compressed file path
	 */
	void compress(String inputFile, String outputFile);

	/**
	 * Get codec name for this strategy
	 *
	 * @return Codec name (e.g., "H.264/AVC", "H.265/HEVC")
	 */
	String getCodecName();

	/**
	 * Get compression ratio for this strategy
	 *
	 * @return Compression ratio (e.g., "50:1", "100:1")
	 */
	String getCompressionRatio();

	/**
	 * Get description of this strategy
	 *
	 * @return Description of use case and characteristics
	 */
	String getDescription();
}
