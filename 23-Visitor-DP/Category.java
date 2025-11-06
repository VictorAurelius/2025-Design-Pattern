import java.util.ArrayList;
import java.util.List;

/**
 * Category - Concrete Element (Hierarchical Composite)
 *
 * Represents a hierarchical category for organizing videos.
 * This is a composite element that can contain both videos and subcategories.
 *
 * Properties:
 * - id: Unique identifier
 * - name: Category name
 * - videos: Videos directly in this category
 * - subcategories: Child categories
 *
 * Hierarchical Structure:
 * - Category can contain videos
 * - Category can contain other categories (tree structure)
 * - Enables recursive operations (total duration of category tree)
 *
 * Key Design:
 * - NO export methods
 * - NO analytics calculations
 * - ONLY accept() method to enable visitor operations
 * - Visitors handle hierarchical traversal
 *
 * StreamFlix Context:
 * Categories organize content hierarchically (e.g., "Tutorials" > "JavaScript" > "React").
 * Visitors can calculate total duration across entire category tree,
 * export hierarchical structure, etc.
 */
public class Category implements VideoElement {

    private int id;
    private String name;
    private List<VideoFile> videos;
    private List<Category> subcategories;

    /**
     * Constructor
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.videos = new ArrayList<>();
        this.subcategories = new ArrayList<>();
    }

    /**
     * Add a video to this category
     *
     * @param video Video to add
     */
    public void addVideo(VideoFile video) {
        videos.add(video);
    }

    /**
     * Add a subcategory to this category
     *
     * @param category Subcategory to add
     */
    public void addSubcategory(Category category) {
        subcategories.add(category);
    }

    /**
     * Accept a visitor to perform operations
     *
     * For hierarchical structures, visitor controls traversal.
     * Visitor can:
     * - Visit only this category
     * - Visit this category and all videos
     * - Visit this category, all videos, and all subcategories (recursive)
     *
     * @param visitor The visitor performing the operation
     */
    @Override
    public void accept(VideoVisitor visitor) {
        visitor.visit(this);
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<VideoFile> getVideos() {
        return videos;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public int getVideoCount() {
        return videos.size();
    }

    public int getSubcategoryCount() {
        return subcategories.size();
    }

    @Override
    public String toString() {
        return String.format("Category: \"%s\" (%d videos, %d subcategories)",
                           name, videos.size(), subcategories.size());
    }
}
