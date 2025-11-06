/**
 * CSVExportVisitor - Concrete Visitor (Export to CSV)
 *
 * Exports video elements to CSV (Comma-Separated Values) format.
 * Demonstrates flattening hierarchical structures for tabular export.
 *
 * Operation: Export to CSV
 * - VideoFile: One row per video
 * - Playlist: Header row + one row per video with playlist name
 * - Category: Flattened structure with category path
 *
 * Key Design:
 * - Accumulates rows in StringBuilder
 * - Handles quoting and escaping
 * - Flattens hierarchical structures
 *
 * StreamFlix Context:
 * CSV export is used for:
 * - Excel imports
 * - Data analysis in spreadsheets
 * - Reporting to business stakeholders
 * - Bulk data exports
 */
public class CSVExportVisitor implements VideoVisitor {

    private StringBuilder result;
    private boolean headerWritten;

    public CSVExportVisitor() {
        this.result = new StringBuilder();
        this.headerWritten = false;
    }

    /**
     * Visit VideoFile and export to CSV
     */
    @Override
    public void visit(VideoFile video) {
        // Write header if not yet written
        if (!headerWritten) {
            result.append("ID,Title,Duration,Views,Likes,UploadDate,Category,FileSize\n");
            headerWritten = true;
        }

        // Write data row
        result.append(video.getId()).append(",");
        result.append(quote(video.getTitle())).append(",");
        result.append(video.getDuration()).append(",");
        result.append(video.getViews()).append(",");
        result.append(video.getLikes()).append(",");
        result.append(video.getUploadDate()).append(",");
        result.append(quote(video.getCategory())).append(",");
        result.append(video.getFileSize()).append("\n");
    }

    /**
     * Visit Playlist and export to CSV
     * Exports all videos with playlist name in each row
     */
    @Override
    public void visit(Playlist playlist) {
        // Write header with playlist column
        if (!headerWritten) {
            result.append("PlaylistID,PlaylistName,VideoID,Title,Duration,Views,Likes\n");
            headerWritten = true;
        }

        // Export each video with playlist info
        for (VideoFile video : playlist.getVideos()) {
            result.append(playlist.getId()).append(",");
            result.append(quote(playlist.getName())).append(",");
            result.append(video.getId()).append(",");
            result.append(quote(video.getTitle())).append(",");
            result.append(video.getDuration()).append(",");
            result.append(video.getViews()).append(",");
            result.append(video.getLikes()).append("\n");
        }
    }

    /**
     * Visit Category and export to CSV (flattened)
     * Exports all videos with category path
     */
    @Override
    public void visit(Category category) {
        visitCategory(category, "");
    }

    /**
     * Helper method to visit category recursively with path
     */
    private void visitCategory(Category category, String parentPath) {
        String currentPath = parentPath.isEmpty() ? category.getName() :
                            parentPath + " > " + category.getName();

        // Write header if not yet written
        if (!headerWritten) {
            result.append("CategoryPath,VideoID,Title,Duration,Views,Likes\n");
            headerWritten = true;
        }

        // Export videos in this category
        for (VideoFile video : category.getVideos()) {
            result.append(quote(currentPath)).append(",");
            result.append(video.getId()).append(",");
            result.append(quote(video.getTitle())).append(",");
            result.append(video.getDuration()).append(",");
            result.append(video.getViews()).append(",");
            result.append(video.getLikes()).append("\n");
        }

        // Recursively export subcategories
        for (Category subcat : category.getSubcategories()) {
            visitCategory(subcat, currentPath);
        }
    }

    /**
     * Get the accumulated CSV result
     */
    public String getResult() {
        return result.toString();
    }

    // Helper methods

    /**
     * Quote and escape CSV value
     * Quotes if contains comma, newline, or quote
     */
    private String quote(String value) {
        if (value == null) {
            return "";
        }

        boolean needsQuoting = value.contains(",") || value.contains("\n") ||
                              value.contains("\"");

        if (needsQuoting) {
            // Escape quotes by doubling them
            String escaped = value.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        }

        return value;
    }
}
