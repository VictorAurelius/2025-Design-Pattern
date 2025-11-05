/**
 * H265Strategy - Concrete compression strategy for H.265/HEVC codec
 *
 * Characteristics:
 * - Codec: H.265/HEVC (High Efficiency Video Coding)
 * - Compression ratio: 100:1 (2× better than H.264)
 * - Processing speed: Medium (50 fps)
 * - Compatibility: Modern devices (2015+)
 * - HDR support: Optional
 * - File size: Small
 *
 * Best for:
 * - 4K video content
 * - HDR video
 * - Premium quality content
 * - Bandwidth-constrained distribution
 *
 * Use case: Professional creator uploading 4K HDR cinematic content
 */
public class H265Strategy implements CompressionStrategy {

	private boolean hdrEnabled;

	/**
	 * Create H.265 strategy
	 *
	 * @param hdrEnabled Enable HDR processing (HDR10)
	 */
	public H265Strategy(boolean hdrEnabled) {
		this.hdrEnabled = hdrEnabled;
	}

	/**
	 * Create H.265 strategy without HDR
	 */
	public H265Strategy() {
		this(false);
	}

	@Override
	public void compress(String inputFile, String outputFile) {
		System.out.println("Using " + getCodecName() + " codec...");
		System.out.println("Compression ratio: " + getCompressionRatio());
		System.out.println("Processing speed: Medium (50 fps)");
		System.out.println("Compatibility: Modern devices (2015+)");
		System.out.println("HDR: " + (hdrEnabled ? "Enabled (HDR10)" : "Disabled"));

		if (hdrEnabled) {
			System.out.println("Color space: BT.2020");
			System.out.println("Bit depth: 10-bit");
			System.out.println("Profile: Main 10");
		} else {
			System.out.println("Color space: BT.709");
			System.out.println("Bit depth: 8-bit");
			System.out.println("Profile: Main");
		}

		System.out.println();

		// Simulate compression process
		System.out.println("Processing video...");
		System.out.println("████████████████████████████████ 100%");
		System.out.println();

		if (hdrEnabled) {
			System.out.println("File size: 5000 MB → 50 MB (99% reduction)");
			System.out.println("Processing time: 100 seconds");
		} else {
			System.out.println("File size: 2000 MB → 20 MB (99% reduction)");
			System.out.println("Processing time: 40 seconds");
		}

		System.out.println();

		System.out.println("✓ H.265 compression successful" +
		                   (hdrEnabled ? " with HDR" : ""));
	}

	@Override
	public String getCodecName() {
		return "H.265/HEVC";
	}

	@Override
	public String getCompressionRatio() {
		return "100:1";
	}

	@Override
	public String getDescription() {
		return "High-quality 4K/HDR encoding" + (hdrEnabled ? " (HDR enabled)" : "");
	}
}
