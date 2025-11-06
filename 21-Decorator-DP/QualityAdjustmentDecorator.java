/**
 * QualityAdjustmentDecorator - Concrete Decorator
 *
 * Dynamically adjusts video quality based on network bandwidth and device capabilities.
 *
 * Adjustment Modes:
 * - auto: Automatically adjust based on bandwidth detection
 * - aggressive: Quickly lower quality when bandwidth drops
 * - conservative: Maintain quality as long as possible
 * - manual: User-specified quality override
 *
 * Features:
 * - Real-time bandwidth monitoring
 * - Seamless quality switching (no buffering)
 * - Bitrate can increase or decrease by up to 50%
 * - Adaptive streaming (HLS/DASH)
 *
 * Use Cases:
 * - Mobile users: Network changes (WiFi → 4G → 3G)
 * - Congested networks: Peak hours, public WiFi
 * - Data savings: Mobile data caps
 * - Device limitations: Old devices can't handle 4K
 *
 * StreamFlix Context:
 * Critical for mobile users who switch between WiFi and cellular.
 * Also important for users with variable bandwidth (shared WiFi,
 * public networks, peak hours). Prevents buffering interruptions.
 *
 * Example - Network deterioration:
 * 1. WiFi (fast): Stream at full quality (2500 kbps)
 * 2. 4G (medium): Reduce to 70% quality (1750 kbps)
 * 3. 3G (slow): Reduce to 40% quality (1000 kbps)
 * 4. Back to WiFi: Restore full quality (2500 kbps)
 *
 * Example:
 * VideoStream stream = new HDVideoStream();
 * stream = new QualityAdjustmentDecorator(stream, "auto");
 * // Output: "Playing stream + Quality Adjustment (auto, current: 70% = 1750 kbps)"
 */
public class QualityAdjustmentDecorator extends StreamDecorator {

    private String mode; // auto, aggressive, conservative, manual
    private int qualityPercent; // Current quality percentage (0-150%)

    /**
     * Constructor
     *
     * @param stream The video stream to add quality adjustment to
     * @param mode Adjustment mode (auto, aggressive, conservative, manual)
     */
    public QualityAdjustmentDecorator(VideoStream stream, String mode) {
        super(stream);
        this.mode = mode;
        this.qualityPercent = 100; // Start at 100% (no adjustment)
    }

    /**
     * Simulate bandwidth detection and quality adjustment
     * In real implementation, this would monitor actual network conditions
     */
    private void adjustQuality() {
        if (mode.equals("auto")) {
            // Simulate: reduce quality to 70% due to bandwidth constraints
            qualityPercent = 70;
        } else if (mode.equals("aggressive")) {
            // Aggressive mode: reduce more aggressively to prevent buffering
            qualityPercent = 50;
        } else if (mode.equals("conservative")) {
            // Conservative mode: try to maintain quality
            qualityPercent = 90;
        } else {
            // Manual or unknown mode
            qualityPercent = 100;
        }
    }

    @Override
    public String play() {
        adjustQuality();
        int adjustedBitrate = (wrappedStream.getBitrate() * qualityPercent) / 100;
        return wrappedStream.play() + " + Quality Adjustment (" + mode + ", current: " +
               qualityPercent + "% = " + adjustedBitrate + " kbps)";
    }

    @Override
    public int getBitrate() {
        adjustQuality();
        // Adjust bitrate based on quality percentage
        return (wrappedStream.getBitrate() * qualityPercent) / 100;
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Quality Adjustment (" + mode + ")";
    }
}
