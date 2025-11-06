/**
 * JSONExportVisitor - Concrete Visitor (Export to JSON)
 *
 * Exports video elements to JSON format.
 * This visitor demonstrates how to implement type-specific export logic.
 *
 * Operation: Export to JSON
 * - VideoFile: Export as JSON object with all properties
 * - Playlist: Export as JSON object with name and array of videos
 * - Category: Export as JSON object with hierarchical structure
 *
 * Key Design:
 * - Accumulates result in StringBuilder
 * - Each visit() method appends to result
 * - getResult() returns final JSON string
 *
 * StreamFlix Context:
 * JSON export is used for:
 * - API responses
 * - Data interchange with mobile apps
 * - Backup and restore
 * - Integration with third-party services
 */
public class JSONExportVisitor implements VideoVisitor {

    private StringBuilder result;
    private int indentLevel;

    public JSONExportVisitor() {
        this.result = new StringBuilder();
        this.indentLevel = 0;
    }

    /**
     * Visit VideoFile and export to JSON
     */
    @Override
    public void visit(VideoFile video) {
        indent();
        result.append("{\n");
        indentLevel++;

        addField("id", video.getId());
        addField("title", video.getTitle());
        addField("duration", video.getDuration());
        addField("views", video.getViews());
        addField("likes", video.getLikes());
        addField("uploadDate", video.getUploadDate().toString());
        addField("category", video.getCategory());
        addField("fileSize", video.getFileSize(), true); // last field

        indentLevel--;
        indent();
        result.append("}");
    }

    /**
     * Visit Playlist and export to JSON (with all videos)
     */
    @Override
    public void visit(Playlist playlist) {
        indent();
        result.append("{\n");
        indentLevel++;

        addField("id", playlist.getId());
        addField("name", playlist.getName());
        addField("videoCount", playlist.getVideoCount());

        // Export all videos
        indent();
        result.append("\"videos\": [\n");
        indentLevel++;

        for (int i = 0; i < playlist.getVideos().size(); i++) {
            VideoFile video = playlist.getVideos().get(i);
            video.accept(this); // Recursive visit

            if (i < playlist.getVideos().size() - 1) {
                result.append(",");
            }
            result.append("\n");
        }

        indentLevel--;
        indent();
        result.append("]\n");

        indentLevel--;
        indent();
        result.append("}");
    }

    /**
     * Visit Category and export to JSON (hierarchical)
     */
    @Override
    public void visit(Category category) {
        indent();
        result.append("{\n");
        indentLevel++;

        addField("id", category.getId());
        addField("name", category.getName());
        addField("videoCount", category.getVideoCount());
        addField("subcategoryCount", category.getSubcategoryCount());

        // Export videos
        if (!category.getVideos().isEmpty()) {
            indent();
            result.append("\"videos\": [\n");
            indentLevel++;

            for (int i = 0; i < category.getVideos().size(); i++) {
                VideoFile video = category.getVideos().get(i);
                video.accept(this);

                if (i < category.getVideos().size() - 1) {
                    result.append(",");
                }
                result.append("\n");
            }

            indentLevel--;
            indent();
            result.append("]");

            if (!category.getSubcategories().isEmpty()) {
                result.append(",");
            }
            result.append("\n");
        }

        // Export subcategories (recursive)
        if (!category.getSubcategories().isEmpty()) {
            indent();
            result.append("\"subcategories\": [\n");
            indentLevel++;

            for (int i = 0; i < category.getSubcategories().size(); i++) {
                Category subcat = category.getSubcategories().get(i);
                subcat.accept(this);

                if (i < category.getSubcategories().size() - 1) {
                    result.append(",");
                }
                result.append("\n");
            }

            indentLevel--;
            indent();
            result.append("]\n");
        }

        indentLevel--;
        indent();
        result.append("}");
    }

    /**
     * Get the accumulated JSON result
     */
    public String getResult() {
        return result.toString();
    }

    // Helper methods for formatting

    private void addField(String name, String value) {
        addField(name, value, false);
    }

    private void addField(String name, String value, boolean isLast) {
        indent();
        result.append("\"").append(name).append("\": \"").append(value).append("\"");
        if (!isLast) result.append(",");
        result.append("\n");
    }

    private void addField(String name, int value) {
        addField(name, value, false);
    }

    private void addField(String name, int value, boolean isLast) {
        indent();
        result.append("\"").append(name).append("\": ").append(value);
        if (!isLast) result.append(",");
        result.append("\n");
    }

    private void indent() {
        for (int i = 0; i < indentLevel; i++) {
            result.append("  ");
        }
    }
}
