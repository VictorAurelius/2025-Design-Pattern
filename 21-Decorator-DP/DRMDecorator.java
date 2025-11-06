/**
 * DRMDecorator - Concrete Decorator
 *
 * Digital Rights Management (DRM) - Encrypts video stream and enforces
 * license restrictions to prevent unauthorized copying and distribution.
 *
 * Features:
 * - Stream encryption (prevents unauthorized downloads)
 * - License validation (checks user has valid license)
 * - Watermarking with user ID (tracks piracy)
 * - Adds 100 kbps bitrate overhead (encryption overhead)
 *
 * DRM Levels:
 * - basic: Simple encryption
 * - standard: Encryption + license check
 * - enterprise: Encryption + license + user watermark + audit trail
 *
 * Use Cases:
 * - Premium content: Prevent piracy of exclusive videos
 * - Business customers: Protect confidential training materials
 * - Enterprise: Prevent leaks of sensitive information
 * - Content creators: Protect copyrighted material
 *
 * StreamFlix Context:
 * Essential for business tier. Companies have sensitive content
 * (trade secrets, confidential strategies, unreleased products)
 * that must not leak. DRM prevents:
 * - Screen recording
 * - File downloading
 * - Unauthorized sharing
 * Each stream is watermarked with employee ID for traceability.
 *
 * Example:
 * VideoStream stream = new HDVideoStream();
 * stream = new DRMDecorator(stream, "enterprise_license_techcorp");
 * // Output: "Playing stream + [DRM protection enabled: enterprise_license_techcorp]"
 */
public class DRMDecorator extends StreamDecorator {

    private String licenseKey; // License identifier for validation

    /**
     * Constructor
     *
     * @param stream The video stream to add DRM protection to
     * @param licenseKey License identifier (basic, standard, enterprise, or specific license ID)
     */
    public DRMDecorator(VideoStream stream, String licenseKey) {
        super(stream);
        this.licenseKey = licenseKey;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + [DRM protection enabled: " + licenseKey + "]";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 100; // 100 kbps overhead for encryption
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + DRM (" + licenseKey + ")";
    }
}
