import java.time.LocalDate;

/**
 * VideoFile - Concrete Element (Individual Video)
 *
 * Represents a single video file in the StreamFlix platform.
 * This class focuses ONLY on data management - no operation methods.
 *
 * Properties:
 * - id: Unique identifier
 * - title: Video title
 * - duration: Duration in minutes
 * - views: Total view count
 * - likes: Total like count
 * - uploadDate: Upload date
 * - category: Video category
 * - fileSize: File size in MB
 *
 * Key Design:
 * - NO export methods (exportToJSON, exportToXML, etc.)
 * - NO analytics methods (calculateEngagement, etc.)
 * - NO quality check methods (validateQuality, etc.)
 * - ONLY accept() method to enable visitor operations
 *
 * StreamFlix Context:
 * Individual videos can be exported to various formats, analyzed for
 * metrics, and validated for quality - all through visitors without
 * modifying this class.
 */
public class VideoFile implements VideoElement {

    private int id;
    private String title;
    private int duration;        // in minutes
    private int views;
    private int likes;
    private LocalDate uploadDate;
    private String category;
    private int fileSize;        // in MB

    /**
     * Constructor
     */
    public VideoFile(int id, String title, int duration, int views, int likes,
                     LocalDate uploadDate, String category, int fileSize) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.views = views;
        this.likes = likes;
        this.uploadDate = uploadDate;
        this.category = category;
        this.fileSize = fileSize;
    }

    /**
     * Simplified constructor
     */
    public VideoFile(int id, String title, int duration, int views, int likes) {
        this(id, title, duration, views, likes, LocalDate.now(), "general", 100);
    }

    /**
     * Accept a visitor to perform operations
     *
     * This is the double dispatch mechanism:
     * - visitor.visit(this) where "this" has compile-time type VideoFile
     * - Compiler selects visit(VideoFile) method at compile time
     * - At runtime, correct visitor implementation is called
     *
     * @param visitor The visitor performing the operation
     */
    @Override
    public void accept(VideoVisitor visitor) {
        visitor.visit(this);
    }

    // Getters (no setters - immutable for demo)

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public String getCategory() {
        return category;
    }

    public int getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" (%d min, %d views, %d likes)",
                           title, duration, views, likes);
    }
}
