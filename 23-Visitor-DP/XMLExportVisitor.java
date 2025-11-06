/**
 * XMLExportVisitor - Concrete Visitor (Export to XML)
 *
 * Exports video elements to XML format.
 * Demonstrates how same visitor interface supports different export formats.
 *
 * Operation: Export to XML
 * - VideoFile: <video><id>...</id><title>...</title>...</video>
 * - Playlist: <playlist><name>...</name><videos>...</videos></playlist>
 * - Category: <category><name>...</name><subcategories>...</subcategories></category>
 *
 * Key Design:
 * - Accumulates result in StringBuilder
 * - Properly nests XML elements
 * - Handles hierarchical structures
 *
 * StreamFlix Context:
 * XML export is used for:
 * - GDPR compliance (data export)
 * - Legacy system integration
 * - RSS feeds
 * - SOAP API responses
 */
public class XMLExportVisitor implements VideoVisitor {

    private StringBuilder result;
    private int indentLevel;

    public XMLExportVisitor() {
        this.result = new StringBuilder();
        this.indentLevel = 0;
    }

    /**
     * Visit VideoFile and export to XML
     */
    @Override
    public void visit(VideoFile video) {
        indent();
        result.append("<video>\n");
        indentLevel++;

        addElement("id", String.valueOf(video.getId()));
        addElement("title", video.getTitle());
        addElement("duration", String.valueOf(video.getDuration()));
        addElement("views", String.valueOf(video.getViews()));
        addElement("likes", String.valueOf(video.getLikes()));
        addElement("uploadDate", video.getUploadDate().toString());
        addElement("category", video.getCategory());
        addElement("fileSize", String.valueOf(video.getFileSize()));

        indentLevel--;
        indent();
        result.append("</video>");
    }

    /**
     * Visit Playlist and export to XML
     */
    @Override
    public void visit(Playlist playlist) {
        indent();
        result.append("<playlist>\n");
        indentLevel++;

        addElement("id", String.valueOf(playlist.getId()));
        addElement("name", playlist.getName());
        addElement("videoCount", String.valueOf(playlist.getVideoCount()));

        // Export all videos
        indent();
        result.append("<videos>\n");
        indentLevel++;

        for (VideoFile video : playlist.getVideos()) {
            video.accept(this);
            result.append("\n");
        }

        indentLevel--;
        indent();
        result.append("</videos>\n");

        indentLevel--;
        indent();
        result.append("</playlist>");
    }

    /**
     * Visit Category and export to XML (hierarchical)
     */
    @Override
    public void visit(Category category) {
        indent();
        result.append("<category>\n");
        indentLevel++;

        addElement("id", String.valueOf(category.getId()));
        addElement("name", category.getName());
        addElement("videoCount", String.valueOf(category.getVideoCount()));
        addElement("subcategoryCount", String.valueOf(category.getSubcategoryCount()));

        // Export videos
        if (!category.getVideos().isEmpty()) {
            indent();
            result.append("<videos>\n");
            indentLevel++;

            for (VideoFile video : category.getVideos()) {
                video.accept(this);
                result.append("\n");
            }

            indentLevel--;
            indent();
            result.append("</videos>\n");
        }

        // Export subcategories (recursive)
        if (!category.getSubcategories().isEmpty()) {
            indent();
            result.append("<subcategories>\n");
            indentLevel++;

            for (Category subcat : category.getSubcategories()) {
                subcat.accept(this);
                result.append("\n");
            }

            indentLevel--;
            indent();
            result.append("</subcategories>\n");
        }

        indentLevel--;
        indent();
        result.append("</category>");
    }

    /**
     * Get the accumulated XML result
     */
    public String getResult() {
        return result.toString();
    }

    // Helper methods

    private void addElement(String name, String value) {
        indent();
        result.append("<").append(name).append(">")
              .append(escapeXML(value))
              .append("</").append(name).append(">\n");
    }

    private void indent() {
        for (int i = 0; i < indentLevel; i++) {
            result.append("  ");
        }
    }

    private String escapeXML(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
