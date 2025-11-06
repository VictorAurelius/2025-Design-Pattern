/**
 * WatermarkDecorator - Concrete Decorator
 *
 * Overlays a watermark (company logo or text) on the video stream.
 *
 * Features:
 * - Custom watermark text (company name, logo, "CONFIDENTIAL", etc.)
 * - Configurable position (top-left, top-right, bottom-left, bottom-right, center)
 * - Adds 50 kbps bitrate overhead for watermark rendering
 *
 * Use Cases:
 * - Business customers: Company branding on training videos
 * - Enterprise: "CONFIDENTIAL" watermark on sensitive content
 * - Content creators: Creator name/logo watermark
 * - Anti-piracy: User ID watermark to track leaks
 *
 * StreamFlix Context:
 * Common for business tier subscriptions. Companies want their
 * branding on training materials and need "CONFIDENTIAL" markers
 * on sensitive content to prevent unauthorized distribution.
 *
 * Example:
 * VideoStream stream = new HDVideoStream();
 * stream = new WatermarkDecorator(stream, "TechCorp Confidential", "bottom-right");
 * // Output: "Playing HD stream + Watermark [TechCorp Confidential @ bottom-right]"
 */
public class WatermarkDecorator extends StreamDecorator {

    private String watermarkText;
    private String position; // top-left, top-right, bottom-left, bottom-right, center

    /**
     * Constructor
     *
     * @param stream The video stream to add watermark to
     * @param watermarkText Text to display as watermark (company name, logo, etc.)
     * @param position Position on screen (top-left, top-right, bottom-left, bottom-right, center)
     */
    public WatermarkDecorator(VideoStream stream, String watermarkText, String position) {
        super(stream);
        this.watermarkText = watermarkText;
        this.position = position;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + Watermark [" + watermarkText + " @ " + position + "]";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 50; // 50 kbps overhead for watermark rendering
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Watermark (" + watermarkText + ")";
    }
}
