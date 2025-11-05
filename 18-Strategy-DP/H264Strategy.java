/**
 * H264Strategy - Concrete compression strategy for H.264/AVC codec
 *
 * Characteristics:
 * - Codec: H.264/AVC (Advanced Video Coding)
 * - Compression ratio: 50:1
 * - Processing speed: Fast (100 fps)
 * - Compatibility: Universal (99%+ devices)
 * - File size: Moderate
 *
 * Best for:
 * - Standard definition videos
 * - Mobile uploads
 * - Maximum compatibility required
 * - Fast processing needed
 *
 * Use case: Content creator uploading daily vlogs from mobile device
 */
public class H264Strategy implements CompressionStrategy {

	@Override
	public void compress(String inputFile, String outputFile) {
		System.out.println("Using " + getCodecName() + " codec...");
		System.out.println("Compression ratio: " + getCompressionRatio());
		System.out.println("Processing speed: Fast (100 fps)");
		System.out.println("Compatibility: Universal (99%+ devices)");
		System.out.println("Target bitrate: 5000 kbps");
		System.out.println("Profile: High");
		System.out.println("Level: 4.1");
		System.out.println();

		// Simulate compression process
		System.out.println("Processing video...");
		System.out.println("████████████████████████████████ 100%");
		System.out.println();

		System.out.println("File size: 1000 MB → 20 MB (98% reduction)");
		System.out.println("Processing time: 10 seconds");
		System.out.println();

		System.out.println("✓ H.264 compression successful");
	}

	@Override
	public String getCodecName() {
		return "H.264/AVC";
	}

	@Override
	public String getCompressionRatio() {
		return "50:1";
	}

	@Override
	public String getDescription() {
		return "Fast encoding, universal device compatibility";
	}
}
