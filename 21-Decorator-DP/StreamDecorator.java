/**
 * StreamDecorator - Abstract Decorator
 *
 * This is the key class in the Decorator pattern. It has two critical properties:
 * 1. IS-A VideoStream (implements VideoStream interface)
 * 2. HAS-A VideoStream (contains reference to wrapped stream)
 *
 * This dual nature allows decorators to:
 * - Be treated as VideoStream by client code (substitutable)
 * - Wrap other VideoStream objects (decorators or concrete streams)
 * - Form chains: Decorator3 → Decorator2 → Decorator1 → ConcreteStream
 *
 * Design Pattern:
 * - Abstract class (not interface) to provide default delegation behavior
 * - Subclasses override only the methods they want to enhance
 * - Methods delegate to wrappedStream by default (transparent wrapping)
 *
 * StreamFlix Context:
 * All stream enhancements (subtitles, ads, watermark, analytics, DRM)
 * extend this class. Each decorator adds one specific feature while
 * delegating core functionality to the wrapped stream.
 *
 * Example Chain:
 * AnalyticsDecorator → WatermarkDecorator → SubtitleDecorator → HDVideoStream
 */
public abstract class StreamDecorator implements VideoStream {

    /**
     * The wrapped video stream (decorated component)
     *
     * This can be:
     * - A concrete stream (BasicVideoStream, HDVideoStream, UHDVideoStream)
     * - Another decorator (forming a chain)
     *
     * Protected to allow subclass access for delegation.
     */
    protected VideoStream wrappedStream;

    /**
     * Constructor - wraps the given video stream
     *
     * @param stream The stream to be decorated (enhanced)
     */
    public StreamDecorator(VideoStream stream) {
        this.wrappedStream = stream;
    }

    /**
     * Default play() implementation - delegates to wrapped stream
     *
     * Subclass decorators override this to add their enhancement:
     * - Call wrappedStream.play() to get base behavior
     * - Add their own enhancement (ads, subtitles, etc.)
     * - Return combined result
     *
     * @return Play output from wrapped stream
     */
    @Override
    public String play() {
        return wrappedStream.play();
    }

    /**
     * Default getBitrate() implementation - delegates to wrapped stream
     *
     * Subclass decorators override this to add their bitrate overhead:
     * - Call wrappedStream.getBitrate() to get base bitrate
     * - Add their own overhead (e.g., +100 kbps for subtitles)
     * - Return total bitrate
     *
     * @return Bitrate from wrapped stream
     */
    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate();
    }

    /**
     * Default getDescription() implementation - delegates to wrapped stream
     *
     * Subclass decorators override this to append their feature description:
     * - Call wrappedStream.getDescription() to get base description
     * - Append their own description (e.g., "+ Subtitles (EN)")
     * - Return combined description
     *
     * @return Description from wrapped stream
     */
    @Override
    public String getDescription() {
        return wrappedStream.getDescription();
    }
}
