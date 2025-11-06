/**
 * VideoStream - Component Interface
 *
 * Defines the core operations that all video streams (decorated or undecorated)
 * must support. This is the common interface that both concrete components
 * (BasicVideoStream, HDVideoStream) and decorators (StreamDecorator subclasses)
 * will implement.
 *
 * Key Design:
 * - All streams provide play(), getBitrate(), and getDescription()
 * - Client code treats decorated and undecorated streams uniformly
 * - Decorators can wrap other decorators (recursive composition)
 *
 * StreamFlix Context:
 * Video streams can have various optional features (subtitles, ads, watermark,
 * analytics, DRM, quality adjustment) added dynamically at runtime based on
 * user subscription tier, preferences, and viewing context.
 */
public interface VideoStream {

    /**
     * Play the video stream with all applied enhancements
     *
     * This method is called by decorators recursively. Each decorator
     * adds its own enhancement to the output and delegates to the
     * wrapped stream.
     *
     * @return Stream data description with all applied features
     */
    String play();

    /**
     * Get the total bitrate (quality) of the stream
     *
     * Bitrate includes:
     * - Base stream bitrate (480p = 500 kbps, 1080p = 2500 kbps, 4K = 8000 kbps)
     * - Overhead from each decorator (subtitles, ads, watermark, etc.)
     *
     * This method demonstrates decorator accumulation - each decorator
     * adds its overhead to the wrapped stream's bitrate.
     *
     * @return Total bitrate in kbps
     */
    int getBitrate();

    /**
     * Get human-readable description of stream and all applied features
     *
     * This method builds a description showing:
     * - Base stream quality (e.g., "HD Stream (1080p, 2500 kbps)")
     * - All applied decorators in order (e.g., "+ Subtitles (EN)", "+ Watermark")
     *
     * Useful for debugging and showing users what features are active.
     *
     * @return Description of stream with all features
     */
    String getDescription();
}
