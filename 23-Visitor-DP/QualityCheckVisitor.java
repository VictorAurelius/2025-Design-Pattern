import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * QualityCheckVisitor - Concrete Visitor (Validate Content Quality)
 *
 * Validates content quality and detects issues across video elements.
 * Demonstrates validation and error collection using visitors.
 *
 * Operation: Quality Check
 * - VideoFile: Validate metadata, duration, engagement
 * - Playlist: Validate consistency, no duplicates
 * - Category: Validate hierarchy, no circular references
 *
 * Quality Criteria:
 * - Title: Non-empty, reasonable length
 * - Duration: > 0 minutes
 * - Upload date: Valid and not in future
 * - Engagement: Views >= 0, likes >= 0
 * - File size: > 0 MB
 *
 * Key Design:
 * - Accumulates issues in a list
 * - Provides pass/fail status
 * - Gives actionable recommendations
 *
 * StreamFlix Context:
 * Quality check is used for:
 * - Content moderation (before publishing)
 * - Data integrity validation
 * - Migration verification (ensure no data corruption)
 * - Cleanup operations (find and fix bad data)
 */
public class QualityCheckVisitor implements VideoVisitor {

    private List<String> issues;
    private int totalChecked;
    private int passedChecks;
    private int failedChecks;

    public QualityCheckVisitor() {
        this.issues = new ArrayList<>();
        this.totalChecked = 0;
        this.passedChecks = 0;
        this.failedChecks = 0;
    }

    /**
     * Visit VideoFile and validate quality
     */
    @Override
    public void visit(VideoFile video) {
        totalChecked++;
        List<String> videoIssues = new ArrayList<>();

        // Validate title
        if (video.getTitle() == null || video.getTitle().trim().isEmpty()) {
            videoIssues.add("Title is empty");
        } else if (video.getTitle().length() > 200) {
            videoIssues.add("Title too long (> 200 characters)");
        }

        // Validate duration
        if (video.getDuration() <= 0) {
            videoIssues.add("Duration must be greater than 0");
        } else if (video.getDuration() > 600) {
            videoIssues.add("Duration unusually long (> 10 hours) - verify");
        }

        // Validate views and likes
        if (video.getViews() < 0) {
            videoIssues.add("Views cannot be negative");
        }

        if (video.getLikes() < 0) {
            videoIssues.add("Likes cannot be negative");
        }

        if (video.getLikes() > video.getViews()) {
            videoIssues.add("Likes cannot exceed views");
        }

        // Validate upload date
        if (video.getUploadDate() == null) {
            videoIssues.add("Upload date is missing");
        } else if (video.getUploadDate().isAfter(LocalDate.now())) {
            videoIssues.add("Upload date is in the future");
        }

        // Validate file size
        if (video.getFileSize() <= 0) {
            videoIssues.add("File size must be greater than 0");
        }

        // Low engagement warning
        if (video.getViews() > 100 && video.getLikes() < video.getViews() * 0.01) {
            videoIssues.add("Warning: Low engagement rate (< 1%)");
        }

        // Record results
        if (videoIssues.isEmpty()) {
            passedChecks++;
        } else {
            failedChecks++;
            String issuePrefix = "Video #" + video.getId() + " \"" + video.getTitle() + "\": ";
            for (String issue : videoIssues) {
                issues.add(issuePrefix + issue);
            }
        }
    }

    /**
     * Visit Playlist and validate
     */
    @Override
    public void visit(Playlist playlist) {
        totalChecked++;
        List<String> playlistIssues = new ArrayList<>();

        // Validate name
        if (playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            playlistIssues.add("Playlist name is empty");
        }

        // Validate videos
        if (playlist.getVideos().isEmpty()) {
            playlistIssues.add("Playlist is empty");
        }

        // Check for duplicates
        List<Integer> videoIds = new ArrayList<>();
        for (VideoFile video : playlist.getVideos()) {
            if (videoIds.contains(video.getId())) {
                playlistIssues.add("Duplicate video detected: " + video.getTitle());
            }
            videoIds.add(video.getId());

            // Also validate each video
            video.accept(this);
        }

        // Record results
        if (playlistIssues.isEmpty()) {
            passedChecks++;
        } else {
            failedChecks++;
            String issuePrefix = "Playlist \"" + playlist.getName() + "\": ";
            for (String issue : playlistIssues) {
                issues.add(issuePrefix + issue);
            }
        }
    }

    /**
     * Visit Category and validate
     */
    @Override
    public void visit(Category category) {
        totalChecked++;
        List<String> categoryIssues = new ArrayList<>();

        // Validate name
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            categoryIssues.add("Category name is empty");
        }

        // Validate content
        if (category.getVideos().isEmpty() && category.getSubcategories().isEmpty()) {
            categoryIssues.add("Category is empty (no videos or subcategories)");
        }

        // Record results for this category
        if (categoryIssues.isEmpty()) {
            passedChecks++;
        } else {
            failedChecks++;
            String issuePrefix = "Category \"" + category.getName() + "\": ";
            for (String issue : categoryIssues) {
                issues.add(issuePrefix + issue);
            }
        }

        // Validate videos in this category
        for (VideoFile video : category.getVideos()) {
            video.accept(this);
        }

        // Recursively validate subcategories
        for (Category subcat : category.getSubcategories()) {
            subcat.accept(this);
        }
    }

    /**
     * Check if all validations passed
     */
    public boolean isValid() {
        return issues.isEmpty();
    }

    /**
     * Get list of issues found
     */
    public List<String> getIssues() {
        return new ArrayList<>(issues);
    }

    /**
     * Get total number of elements checked
     */
    public int getTotalChecked() {
        return totalChecked;
    }

    /**
     * Get number of elements that passed
     */
    public int getPassedChecks() {
        return passedChecks;
    }

    /**
     * Get number of elements that failed
     */
    public int getFailedChecks() {
        return failedChecks;
    }

    /**
     * Get validation summary
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Quality Check Summary:\n");
        summary.append("---------------------\n");
        summary.append(String.format("Total Checked: %d\n", totalChecked));
        summary.append(String.format("Passed: %d (%.1f%%)\n",
                                    passedChecks,
                                    (passedChecks * 100.0 / totalChecked)));
        summary.append(String.format("Failed: %d (%.1f%%)\n",
                                    failedChecks,
                                    (failedChecks * 100.0 / totalChecked)));
        summary.append(String.format("Issues Found: %d\n", issues.size()));

        if (isValid()) {
            summary.append("\n✓ All quality checks passed!");
        } else {
            summary.append("\n✗ Quality issues detected:\n");
            for (String issue : issues) {
                summary.append("  - ").append(issue).append("\n");
            }
        }

        return summary.toString();
    }
}
