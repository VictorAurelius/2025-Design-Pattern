/**
 * SubtitleDecorator - Concrete Decorator
 *
 * Adds subtitle overlay to video stream in specified language.
 *
 * Features:
 * - Supports multiple languages (EN, ES, FR, DE, JP, etc.)
 * - Can apply multiple subtitle decorators for multi-language support
 * - Adds 100 kbps bitrate overhead for subtitle data
 *
 * Use Cases:
 * - Premium users: Single language subtitles
 * - Accessibility: Hearing impaired users
 * - International: Multi-language subtitles
 * - Educational: Language learning with dual subtitles
 *
 * StreamFlix Context:
 * Most common decorator. Premium users often enable subtitles,
 * business customers need multi-language support for international
 * employees, and educational content benefits from dual subtitles.
 *
 * Example:
 * VideoStream stream = new HDVideoStream();
 * stream = new SubtitleDecorator(stream, "EN");
 * stream = new SubtitleDecorator(stream, "ES"); // Dual subtitles
 */
public class SubtitleDecorator extends StreamDecorator {

    private String language;

    /**
     * Constructor
     *
     * @param stream The video stream to add subtitles to
     * @param language Subtitle language code (EN, ES, FR, DE, JP, etc.)
     */
    public SubtitleDecorator(VideoStream stream, String language) {
        super(stream);
        this.language = language;
    }

    @Override
    public String play() {
        return wrappedStream.play() + " + Subtitles (" + language + ")";
    }

    @Override
    public int getBitrate() {
        return wrappedStream.getBitrate() + 100; // 100 kbps overhead for subtitle data
    }

    @Override
    public String getDescription() {
        return wrappedStream.getDescription() + " + Subtitles (" + language + ")";
    }
}
