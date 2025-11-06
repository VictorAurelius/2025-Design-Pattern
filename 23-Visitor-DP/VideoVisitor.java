/**
 * VideoVisitor - Visitor Interface
 *
 * Defines the contract for all operations that can be performed on video elements.
 * This is the core of the Visitor pattern on the visitor side.
 *
 * Key Design:
 * - One visit() method per element type (method overloading)
 * - All methods named "visit" but different parameter types
 * - Compiler selects correct method at compile time
 *
 * Double Dispatch Mechanism:
 * When element.accept(visitor) is called:
 * 1. Element's accept() calls visitor.visit(this)
 * 2. "this" has concrete type (VideoFile, Playlist, etc.)
 * 3. Compiler selects appropriate visit() method
 * 4. Correct operation is performed based on element type
 *
 * StreamFlix Context:
 * All operations on video elements (export, analytics, quality check)
 * implement this interface. Each operation can handle all element types
 * with type-specific logic.
 *
 * Design Pattern:
 * This is the "Visitor" component in the Visitor pattern.
 * Concrete implementations: JSONExportVisitor, DurationCalculatorVisitor, etc.
 *
 * Adding New Element Type:
 * ⚠️ WARNING: Adding new element type requires adding visit() method here
 * and updating ALL existing visitor implementations.
 * This is the main drawback of Visitor pattern.
 *
 * Adding New Operation:
 * ✅ BENEFIT: Adding new operation = create one new visitor class.
 * No need to modify any element classes!
 */
public interface VideoVisitor {

    /**
     * Visit a VideoFile element
     *
     * Perform operation specific to individual video files.
     * Examples:
     * - Export video metadata to JSON/XML/CSV
     * - Calculate video duration
     * - Count video views
     * - Validate video quality
     *
     * @param video The video file to visit
     */
    void visit(VideoFile video);

    /**
     * Visit a Playlist element
     *
     * Perform operation specific to playlists.
     * Examples:
     * - Export playlist with all videos to JSON/XML
     * - Calculate total duration of playlist
     * - Count total views across all videos
     * - Validate playlist consistency
     *
     * Composite Handling:
     * Visitor decides whether to traverse playlist's videos.
     * Can call playlist.getVideos() and visit each video.
     *
     * @param playlist The playlist to visit
     */
    void visit(Playlist playlist);

    /**
     * Visit a Category element
     *
     * Perform operation specific to categories.
     * Examples:
     * - Export category hierarchy to JSON/XML
     * - Calculate total duration recursively
     * - Count total views in category tree
     * - Validate category structure
     *
     * Hierarchical Handling:
     * Visitor decides whether to traverse:
     * - Just this category
     * - This category and its videos
     * - This category, videos, and subcategories (recursive)
     *
     * @param category The category to visit
     */
    void visit(Category category);
}
