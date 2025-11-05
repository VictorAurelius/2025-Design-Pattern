/**
 * AV1Strategy - Concrete compression strategy for AV1 codec
 *
 * Characteristics:
 * - Codec: AV1 (AOMedia Video 1)
 * - Compression ratio: 150:1 (3× better than H.264)
 * - Processing speed: Slow (20 fps)
 * - Compatibility: Latest browsers (2020+)
 * - Royalty-free: Yes (open-source)
 * - File size: Very small (best compression)
 *
 * Best for:
 * - Archival footage
 * - Bandwidth-constrained scenarios
 * - Maximum compression needed
 * - Future-proof encoding
 *
 * Use case: Archive service storing massive video libraries with minimum storage
 */
public class AV1Strategy implements CompressionStrategy {

	private String preset;  // "slow", "medium", "fast"
	private int crf;        // Constant Rate Factor (0-63, lower = better quality)

	/**
	 * Create AV1 strategy with custom parameters
	 *
	 * @param preset Encoder preset ("slow" = best quality, "fast" = faster encoding)
	 * @param crf Constant Rate Factor (18-28 recommended, lower = better quality)
	 */
	public AV1Strategy(String preset, int crf) {
		this.preset = preset;
		this.crf = crf;
	}

	/**
	 * Create AV1 strategy with default parameters
	 */
	public AV1Strategy() {
		this("slow", 28);  // Default: slow preset, CRF 28
	}

	@Override
	public void compress(String inputFile, String outputFile) {
		System.out.println("Using " + getCodecName() + " codec...");
		System.out.println("Compression ratio: " + getCompressionRatio());
		System.out.println("Processing speed: Slow (20 fps)");
		System.out.println("Compatibility: Latest browsers (2020+)");
		System.out.println("Royalty-free: Yes (open-source)");
		System.out.println("Encoder preset: " + preset.substring(0, 1).toUpperCase() +
		                   preset.substring(1) + " (maximum quality)");
		System.out.println("CRF: " + crf);
		System.out.println("CPU threads: 8");
		System.out.println("Tile rows/columns: 2/2");
		System.out.println();

		// Simulate compression process
		System.out.println("Processing video...");
		System.out.println("████████████████████████████████ 100%");
		System.out.println();

		// Slower preset = better compression
		int fileSize = preset.equals("slow") ? 53 :
		               preset.equals("medium") ? 60 : 70;

		int processingTime = preset.equals("slow") ? 400 :
		                    preset.equals("medium") ? 200 : 100;

		System.out.println("File size: 8000 MB → " + fileSize + " MB (99.34% reduction)");
		System.out.println("Processing time: " + processingTime + " seconds (" +
		                   (processingTime / 60) + "m " + (processingTime % 60) + "s)");
		System.out.println();

		System.out.println("✓ AV1 compression successful - best compression achieved!");
	}

	@Override
	public String getCodecName() {
		return "AV1";
	}

	@Override
	public String getCompressionRatio() {
		return "150:1";
	}

	@Override
	public String getDescription() {
		return "Next-generation codec with best compression efficiency";
	}
}
