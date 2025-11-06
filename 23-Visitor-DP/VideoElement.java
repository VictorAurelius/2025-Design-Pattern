/**
 * VideoElement - Element Interface
 *
 * Defines the contract for all video entities that can accept visitors.
 * This is the core of the Visitor pattern on the element side.
 *
 * Key Method:
 * - accept(VideoVisitor): Accepts a visitor to perform operations
 *
 * Double Dispatch Mechanism:
 * When element.accept(visitor) is called:
 * 1. Element's accept() method is invoked (first dispatch)
 * 2. accept() calls visitor.visit(this) (second dispatch)
 * 3. Correct visit() method is called based on element's concrete type
 *
 * StreamFlix Context:
 * All video entities (VideoFile, Playlist, Category, Series) implement
 * this interface to accept various operations (export, analytics, quality check)
 * without having those operations embedded in their classes.
 *
 * Design Pattern:
 * This is the "Element" component in the Visitor pattern.
 * Concrete implementations: VideoFile, Playlist, Category, Series
 */
public interface VideoElement {

    /**
     * Accept a visitor to perform operations on this element
     *
     * This method enables the Visitor pattern through double dispatch:
     * - Client calls: element.accept(visitor)
     * - Element calls: visitor.visit(this)
     * - Correct visit() method is invoked based on "this" type
     *
     * Example:
     * VideoFile video = new VideoFile(...);
     * VideoVisitor exporter = new JSONExportVisitor();
     * video.accept(exporter); // Calls exporter.visit(VideoFile)
     *
     * @param visitor The visitor performing the operation
     */
    void accept(VideoVisitor visitor);
}
