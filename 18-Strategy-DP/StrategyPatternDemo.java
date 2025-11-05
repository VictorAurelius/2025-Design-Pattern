/**
 * StrategyPatternDemo - Comprehensive demonstration of Strategy pattern
 *
 * Demonstrates:
 * 1. Creating compressor with different strategies
 * 2. Runtime strategy switching
 * 3. Comparing all 4 compression algorithms
 * 4. Algorithm-specific parameters (HDR, preset, CRF)
 * 5. Benefits: No if-else, type safety, flexibility
 *
 * Scenario: StreamFlix video processing pipeline
 * - Content creators upload raw videos
 * - System selects optimal compression strategy
 * - Different videos require different codecs
 */
public class StrategyPatternDemo {

	public static void main(String[] args) {
		System.out.println("╔════════════════════════════════════════════════════════════════╗");
		System.out.println("║           STRATEGY PATTERN - VIDEO COMPRESSION                 ║");
		System.out.println("║                  StreamFlix Platform                           ║");
		System.out.println("╚════════════════════════════════════════════════════════════════╝");
		System.out.println();

		// Scenario 1: Mobile vlog - need fast processing, universal compatibility
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 1: Daily vlog from mobile device");
		System.out.println("Requirement: Fast processing, universal device compatibility");
		System.out.println("═══════════════════════════════════════════════════════════════");

		VideoCompressor compressor = new VideoCompressor(new H264Strategy());
		compressor.showStrategyInfo();
		compressor.compressVideo("daily-vlog-raw.mp4", "daily-vlog-compressed.mp4");

		System.out.println("\n" + "─".repeat(70) + "\n");

		// Scenario 2: 4K HDR cinematic content - need highest quality
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 2: 4K HDR cinematic video");
		System.out.println("Requirement: Premium quality, HDR support, small file size");
		System.out.println("═══════════════════════════════════════════════════════════════");

		// Runtime strategy switching - same compressor object!
		compressor.setStrategy(new H265Strategy(true));  // Enable HDR
		compressor.compressVideo("cinematic-4k-hdr.mp4", "cinematic-4k-hdr-compressed.mp4");

		System.out.println("\n" + "─".repeat(70) + "\n");

		// Scenario 3: Gaming video for YouTube - need web optimization
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 3: Gaming video for YouTube upload");
		System.out.println("Requirement: Web streaming, open-source, browser compatibility");
		System.out.println("═══════════════════════════════════════════════════════════════");

		compressor.setStrategy(new VP9Strategy());
		compressor.compressVideo("gaming-stream.mp4", "gaming-stream-compressed.webm");

		System.out.println("\n" + "─".repeat(70) + "\n");

		// Scenario 4: Archive massive video library - need maximum compression
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 4: Archive service - massive video library");
		System.out.println("Requirement: Maximum compression, minimum storage cost");
		System.out.println("═══════════════════════════════════════════════════════════════");

		compressor.setStrategy(new AV1Strategy("slow", 28));  // Slow preset, CRF 28
		compressor.compressVideo("archive-footage.mp4", "archive-footage-compressed.mkv");

		System.out.println("\n" + "─".repeat(70) + "\n");

		// Scenario 5: Demonstrating algorithm-specific parameters
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 5: H.265 without HDR (standard 1080p)");
		System.out.println("Requirement: High quality, no HDR needed");
		System.out.println("═══════════════════════════════════════════════════════════════");

		compressor.setStrategy(new H265Strategy(false));  // Disable HDR
		compressor.compressVideo("standard-1080p.mp4", "standard-1080p-compressed.mp4");

		System.out.println("\n" + "─".repeat(70) + "\n");

		// Scenario 6: AV1 fast preset for quicker encoding
		System.out.println("═══════════════════════════════════════════════════════════════");
		System.out.println("SCENARIO 6: AV1 with fast preset (priority: speed)");
		System.out.println("Requirement: Good compression but faster processing");
		System.out.println("═══════════════════════════════════════════════════════════════");

		compressor.setStrategy(new AV1Strategy("fast", 30));  // Fast preset, CRF 30
		compressor.compressVideo("quick-archive.mp4", "quick-archive-compressed.mkv");

		System.out.println("\n" + "═".repeat(70));
		System.out.println("STRATEGY PATTERN BENEFITS DEMONSTRATED:");
		System.out.println("═".repeat(70));
		System.out.println();
		System.out.println("✓ NO IF-ELSE STATEMENTS");
		System.out.println("  - Before: 200+ lines compress() method with 4-8 if-else branches");
		System.out.println("  - After: 5 lines compressVideo() method - delegates to strategy");
		System.out.println();
		System.out.println("✓ RUNTIME STRATEGY SWITCHING");
		System.out.println("  - Same VideoCompressor object used 6 different strategies");
		System.out.println("  - Changed behavior via setStrategy() without creating new objects");
		System.out.println();
		System.out.println("✓ TYPE SAFETY");
		System.out.println("  - Before: String-based selection (\"h264\" vs \"h256\" typo bug)");
		System.out.println("  - After: Type-safe strategy objects (compiler catches errors)");
		System.out.println();
		System.out.println("✓ ALGORITHM-SPECIFIC PARAMETERS");
		System.out.println("  - H.265: hdrEnabled parameter (true/false)");
		System.out.println("  - AV1: preset (slow/medium/fast) and crf (18-28)");
		System.out.println("  - Each strategy can have unique configuration");
		System.out.println();
		System.out.println("✓ OPEN/CLOSED PRINCIPLE");
		System.out.println("  - Add new codec? Just create new strategy class");
		System.out.println("  - No modification of VideoCompressor or other strategies");
		System.out.println();
		System.out.println("✓ SINGLE RESPONSIBILITY");
		System.out.println("  - VideoCompressor: Manages compression workflow");
		System.out.println("  - Each Strategy: Implements ONE specific algorithm");
		System.out.println();
		System.out.println("✓ COMPOSITION OVER INHERITANCE");
		System.out.println("  - VideoCompressor HAS-A strategy (not IS-A strategy)");
		System.out.println("  - More flexible than class inheritance hierarchy");
		System.out.println();
		System.out.println("═".repeat(70));
		System.out.println();

		// Comparison table
		System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
		System.out.println("║               COMPRESSION ALGORITHM COMPARISON                        ║");
		System.out.println("╠═══════════╦════════════╦═══════════╦══════════════╦═════════════════╣");
		System.out.println("║  Codec    ║   Ratio    ║   Speed   ║ Compatibility║   Best For      ║");
		System.out.println("╠═══════════╬════════════╬═══════════╬══════════════╬═════════════════╣");
		System.out.println("║ H.264/AVC ║    50:1    ║ Fast      ║ Universal    ║ Mobile uploads  ║");
		System.out.println("║           ║            ║ (100 fps) ║ (99%+ devices)║ Daily vlogs     ║");
		System.out.println("╠═══════════╬════════════╬═══════════╬══════════════╬═════════════════╣");
		System.out.println("║ H.265/HEVC║   100:1    ║ Medium    ║ Modern       ║ 4K/HDR content  ║");
		System.out.println("║           ║            ║ (50 fps)  ║ (2015+)      ║ Premium quality ║");
		System.out.println("╠═══════════╬════════════╬═══════════╬══════════════╬═════════════════╣");
		System.out.println("║ VP9       ║    80:1    ║ Medium    ║ Web browsers ║ YouTube uploads ║");
		System.out.println("║           ║            ║ (60 fps)  ║ (Chrome/FF)  ║ Web streaming   ║");
		System.out.println("╠═══════════╬════════════╬═══════════╬══════════════╬═════════════════╣");
		System.out.println("║ AV1       ║   150:1    ║ Slow      ║ Latest       ║ Archive footage ║");
		System.out.println("║           ║            ║ (20 fps)  ║ (2020+)      ║ Maximum savings ║");
		System.out.println("╚═══════════╩════════════╩═══════════╩══════════════╩═════════════════╝");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("STRATEGY VS STATE PATTERN:");
		System.out.println("═".repeat(70));
		System.out.println();
		System.out.println("STRATEGY PATTERN (Current example):");
		System.out.println("  • Client selects strategy EXPLICITLY");
		System.out.println("    → compressor.setStrategy(new H265Strategy())");
		System.out.println("  • Algorithms are INDEPENDENT (don't know about each other)");
		System.out.println("  • Context delegates to strategy");
		System.out.println("  • Use case: Selecting compression algorithm for video");
		System.out.println();
		System.out.println("STATE PATTERN (Pattern #17):");
		System.out.println("  • Context transitions AUTOMATICALLY");
		System.out.println("    → player.play() → state changes to PlayingState");
		System.out.println("  • States are INTERDEPENDENT (know about each other)");
		System.out.println("  • States call player.setState() themselves");
		System.out.println("  • Use case: Video player state transitions");
		System.out.println();
		System.out.println("═".repeat(70));
	}
}
