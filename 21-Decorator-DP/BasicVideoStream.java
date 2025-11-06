/**
 * BasicVideoStream - Concrete Component (480p)
 *
 * Represents a basic quality video stream (480p, 500 kbps).
 * This is the lowest quality stream, typically used for:
 * - Free tier users
 * - Users with slow internet connections
 * - Mobile data conservation mode
 *
 * This is a "concrete component" in the Decorator pattern.
 * It provides the base functionality that decorators will enhance.
 *
 * StreamFlix Context:
 * Free users get basic 480p streams, often with advertisements.
 * This stream can be decorated with ads, subtitles, or quality
 * adjustment features.
 */
public class BasicVideoStream implements VideoStream {

    @Override
    public String play() {
        return "Playing basic 480p video stream";
    }

    @Override
    public int getBitrate() {
        return 500; // 500 kbps for 480p
    }

    @Override
    public String getDescription() {
        return "Basic Stream (480p, 500 kbps)";
    }
}
