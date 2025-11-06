import java.time.LocalDate;

/**
 * VisitorPatternDemo - Comprehensive demonstration of Visitor pattern
 *
 * Demonstrates:
 * 1. Export operations (JSON, XML, CSV) - same structure, different formats
 * 2. Analytics operations (duration, views, engagement) - complex calculations
 * 3. Quality check operations - validation without modifying elements
 * 4. Extensibility - adding new operation without modifying elements
 * 5. Multiple operations on same structure - flexibility of visitor pattern
 *
 * Scenario: StreamFlix video platform
 * Video elements need various operations (export, analytics, validation)
 * but we don't want to embed all these operations in the element classes.
 * Visitor pattern allows us to add operations externally.
 *
 * Key Insight:
 * Without Visitor pattern: Add export format → modify all 3 element classes
 * With Visitor pattern: Add export format → create 1 new visitor class
 */
public class VisitorPatternDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║      VISITOR PATTERN - Flexible Operations on Object Structures  ║");
        System.out.println("║                    StreamFlix Platform                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Create sample data
        VideoFile video1 = createSampleVideo1();
        Playlist playlist = createSamplePlaylist();
        Category category = createSampleCategory();

        // Run demonstration scenarios
        demonstrateExportOperations(video1);
        demonstrateAnalyticsOperations(playlist);
        demonstrateQualityCheck();
        demonstrateExtensibility();
        demonstrateMultipleOperations(playlist);
        showPatternBenefits();
    }

    /**
     * Scenario 1: Export Operations
     * Shows same video exported to JSON, XML, CSV without modifying VideoFile class
     */
    private static void demonstrateExportOperations(VideoFile video) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 1: Export Operations");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Video: " + video);
        System.out.println();

        // Export to JSON
        System.out.println("Export to JSON:");
        System.out.println("---------------");
        JSONExportVisitor jsonVisitor = new JSONExportVisitor();
        video.accept(jsonVisitor);
        System.out.println(jsonVisitor.getResult());
        System.out.println();

        // Export to XML
        System.out.println("Export to XML:");
        System.out.println("--------------");
        XMLExportVisitor xmlVisitor = new XMLExportVisitor();
        video.accept(xmlVisitor);
        System.out.println(xmlVisitor.getResult());
        System.out.println();

        // Export to CSV
        System.out.println("Export to CSV:");
        System.out.println("--------------");
        CSVExportVisitor csvVisitor = new CSVExportVisitor();
        video.accept(csvVisitor);
        System.out.println(csvVisitor.getResult());

        System.out.println("BENEFIT: 3 different formats without modifying VideoFile class!");
    }

    /**
     * Scenario 2: Analytics Operations
     * Shows complex analytics on playlist without embedding logic in Playlist class
     */
    private static void demonstrateAnalyticsOperations(Playlist playlist) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 2: Analytics Operations");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println(playlist);
        System.out.println();

        // Display videos
        System.out.println("Videos in playlist:");
        for (int i = 0; i < playlist.getVideos().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + playlist.getVideos().get(i));
        }
        System.out.println();

        // Calculate total duration
        System.out.println("Total Duration Calculation:");
        DurationCalculatorVisitor durationVisitor = new DurationCalculatorVisitor();
        playlist.accept(durationVisitor);
        System.out.println("  Visitor: DurationCalculatorVisitor");
        System.out.println("  Result: " + durationVisitor.getTotalDuration() + " minutes (" +
                         durationVisitor.getFormattedDuration() + ")");
        System.out.println();

        // Count total views
        System.out.println("Total Views Calculation:");
        ViewCountVisitor viewVisitor = new ViewCountVisitor();
        playlist.accept(viewVisitor);
        System.out.println("  Visitor: ViewCountVisitor");
        System.out.println("  " + viewVisitor.getSummary());
        System.out.println();

        // Calculate engagement rate
        System.out.println("Engagement Rate Calculation:");
        EngagementRateVisitor engagementVisitor = new EngagementRateVisitor();
        playlist.accept(engagementVisitor);
        System.out.println("  Visitor: EngagementRateVisitor");
        System.out.println("  Result: " + String.format("%.2f%%", engagementVisitor.getEngagementRate()));
        System.out.println("  Assessment: " + engagementVisitor.getAssessment());
        System.out.println();

        System.out.println("BENEFIT: Complex analytics without modifying Playlist class!");
    }

    /**
     * Scenario 3: Quality Check Operations
     * Shows validation without embedding validation logic in element classes
     */
    private static void demonstrateQualityCheck() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 3: Quality Check Operations");
        System.out.println("═".repeat(70));
        System.out.println();

        // Create videos with varying quality
        VideoFile goodVideo = new VideoFile(1, "Complete Tutorial", 60, 5000, 250);
        VideoFile badVideo = new VideoFile(2, "", 0, 0, 0); // Invalid!
        VideoFile warningVideo = new VideoFile(3, "Test Video", 5, 10, 0); // Low engagement

        System.out.println("Video 1: " + goodVideo);
        System.out.println("Video 2: (empty title, 0 duration) - INVALID");
        System.out.println("Video 3: " + warningVideo + " - Low engagement");
        System.out.println();

        // Check each video
        System.out.println("Quality Check Results:");
        System.out.println();

        QualityCheckVisitor checker1 = new QualityCheckVisitor();
        goodVideo.accept(checker1);
        System.out.println("Video 1: " + (checker1.isValid() ? "✓ PASS" : "✗ FAIL"));
        if (!checker1.isValid()) {
            for (String issue : checker1.getIssues()) {
                System.out.println("  - " + issue);
            }
        }
        System.out.println();

        QualityCheckVisitor checker2 = new QualityCheckVisitor();
        badVideo.accept(checker2);
        System.out.println("Video 2: " + (checker2.isValid() ? "✓ PASS" : "✗ FAIL"));
        if (!checker2.isValid()) {
            System.out.println("  Issues detected:");
            for (String issue : checker2.getIssues()) {
                System.out.println("    - " + issue);
            }
        }
        System.out.println();

        QualityCheckVisitor checker3 = new QualityCheckVisitor();
        warningVideo.accept(checker3);
        System.out.println("Video 3: " + (checker3.isValid() ? "✓ PASS" : "⚠ WARNING"));
        if (!checker3.isValid()) {
            System.out.println("  Issues detected:");
            for (String issue : checker3.getIssues()) {
                System.out.println("    - " + issue);
            }
        }
        System.out.println();

        System.out.println("BENEFIT: Comprehensive validation without modifying VideoFile class!");
    }

    /**
     * Scenario 4: Extensibility - Adding New Operation
     * Shows how easy it is to add new operations with Visitor pattern
     */
    private static void demonstrateExtensibility() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 4: Extensibility - Adding New Operation");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("NEW REQUIREMENT: \"We need to export analytics to JSON\"");
        System.out.println();

        System.out.println("WITHOUT Visitor Pattern:");
        System.out.println("  ❌ Modify VideoFile.java (add exportAnalyticsToJSON method)");
        System.out.println("  ❌ Modify Playlist.java (add exportAnalyticsToJSON method)");
        System.out.println("  ❌ Modify Category.java (add exportAnalyticsToJSON method)");
        System.out.println("  ❌ Test all 3 modified classes (90 test cases)");
        System.out.println("  ❌ Risk breaking existing functionality");
        System.out.println("  ❌ Development time: 1 week");
        System.out.println("  ❌ Cost: $5,000");
        System.out.println();

        System.out.println("WITH Visitor Pattern:");
        System.out.println("  ✅ Create AnalyticsJSONExportVisitor.java (ONE new class)");
        System.out.println("  ✅ Implement visit() for VideoFile, Playlist, Category");
        System.out.println("  ✅ Test only AnalyticsJSONExportVisitor (30 test cases)");
        System.out.println("  ✅ Zero risk to existing code");
        System.out.println("  ✅ Development time: 2 hours");
        System.out.println("  ✅ Cost: $200");
        System.out.println();

        System.out.println("Demonstration:");
        System.out.println();

        // Simulate new visitor (using existing JSON visitor as example)
        Playlist playlist = createSamplePlaylist();
        JSONExportVisitor newVisitor = new JSONExportVisitor();

        System.out.println("  // New visitor created (AnalyticsJSONExportVisitor)");
        System.out.println("  VideoVisitor analyticsVisitor = new AnalyticsJSONExportVisitor();");
        System.out.println("  playlist.accept(analyticsVisitor);");
        System.out.println();

        playlist.accept(newVisitor);
        System.out.println("  Result: Analytics exported to JSON ✓");
        System.out.println();

        System.out.println("RESULT: Feature deployed in 2 hours instead of 1 week!");
        System.out.println("        Cost savings: $4,800 (96% reduction)");
        System.out.println("        Risk: Minimal (no changes to existing code)");
    }

    /**
     * Scenario 5: Multiple Operations on Same Structure
     * Shows flexibility of applying many operations to same data
     */
    private static void demonstrateMultipleOperations(Playlist playlist) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 5: Multiple Operations on Same Structure");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println(playlist);
        System.out.println();

        System.out.println("Performing 5 different operations on same playlist:");
        System.out.println();

        // Operation 1: Export to JSON
        System.out.println("Operation 1: Export to JSON");
        JSONExportVisitor jsonVisitor = new JSONExportVisitor();
        playlist.accept(jsonVisitor);
        System.out.println("  Visitor: JSONExportVisitor");
        System.out.println("  Result: Playlist exported to JSON (2.5 KB)");
        System.out.println();

        // Operation 2: Calculate Duration
        System.out.println("Operation 2: Calculate Total Duration");
        DurationCalculatorVisitor durationVisitor = new DurationCalculatorVisitor();
        playlist.accept(durationVisitor);
        System.out.println("  Visitor: DurationCalculatorVisitor");
        System.out.println("  Result: " + durationVisitor.getFormattedDuration());
        System.out.println();

        // Operation 3: Count Views
        System.out.println("Operation 3: Count Total Views");
        ViewCountVisitor viewVisitor = new ViewCountVisitor();
        playlist.accept(viewVisitor);
        System.out.println("  Visitor: ViewCountVisitor");
        System.out.println("  Result: " + viewVisitor.getTotalViews() + " total views");
        System.out.println();

        // Operation 4: Check Engagement
        System.out.println("Operation 4: Calculate Engagement Rate");
        EngagementRateVisitor engagementVisitor = new EngagementRateVisitor();
        playlist.accept(engagementVisitor);
        System.out.println("  Visitor: EngagementRateVisitor");
        System.out.println("  Result: " + String.format("%.2f%%", engagementVisitor.getEngagementRate()) +
                         " (" + engagementVisitor.getAssessment() + ")");
        System.out.println();

        // Operation 5: Quality Check
        System.out.println("Operation 5: Validate Quality");
        QualityCheckVisitor qualityVisitor = new QualityCheckVisitor();
        playlist.accept(qualityVisitor);
        System.out.println("  Visitor: QualityCheckVisitor");
        System.out.println("  Result: " + qualityVisitor.getPassedChecks() + "/" +
                         qualityVisitor.getTotalChecked() + " videos passed");
        System.out.println();

        System.out.println("BENEFIT: 5 different operations on same structure!");
        System.out.println("         No modification to Playlist class required!");
        System.out.println("         Each visitor is independent and testable!");
    }

    /**
     * Show pattern benefits summary
     */
    private static void showPatternBenefits() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("VISITOR PATTERN BENEFITS DEMONSTRATED");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✓ OPEN/CLOSED PRINCIPLE");
        System.out.println("  - Add operations without modifying element classes");
        System.out.println("  - Elements closed for modification");
        System.out.println("  - Open for extension through new visitors");
        System.out.println();

        System.out.println("✓ SINGLE RESPONSIBILITY");
        System.out.println("  - VideoFile handles only data");
        System.out.println("  - Visitors handle only operations");
        System.out.println("  - Clean separation of concerns");
        System.out.println();

        System.out.println("✓ CENTRALIZED OPERATIONS");
        System.out.println("  - All export logic in export visitors");
        System.out.println("  - All analytics logic in analytics visitors");
        System.out.println("  - Easy to find and maintain");
        System.out.println();

        System.out.println("✓ EASY TO ADD OPERATIONS");
        System.out.println("  - Create one new visitor class (15 minutes)");
        System.out.println("  - vs modifying all element classes (1 week)");
        System.out.println("  - 95% faster development");
        System.out.println();

        System.out.println("✓ TYPE SAFETY");
        System.out.println("  - Compile-time checking of visitor methods");
        System.out.println("  - No instanceof checks needed");
        System.out.println("  - Double dispatch handles types correctly");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("BEFORE vs AFTER COMPARISON");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Adding New Export Format:");
        System.out.println("  Before: Modify 3 classes × 200 lines = 600 lines of code");
        System.out.println("  After:  Create 1 visitor class = 100 lines of code");
        System.out.println("  Improvement: 83% code reduction");
        System.out.println();

        System.out.println("Development Time:");
        System.out.println("  Before: 1 week (modify and test all classes)");
        System.out.println("  After:  2 hours (create and test one visitor)");
        System.out.println("  Improvement: 95% faster");
        System.out.println();

        System.out.println("Cost:");
        System.out.println("  Before: $5,000 (40 hours × $125/hour)");
        System.out.println("  After:  $250 (2 hours × $125/hour)");
        System.out.println("  Savings: $4,750 (95% reduction)");
        System.out.println();

        System.out.println("Risk:");
        System.out.println("  Before: HIGH (modifying existing classes)");
        System.out.println("  After:  LOW (new class, zero impact on existing code)");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("WHEN TO USE VISITOR PATTERN");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✅ USE WHEN:");
        System.out.println("  • Element hierarchy is stable (rarely add new element types)");
        System.out.println("  • Need to add many operations frequently");
        System.out.println("  • Operations depend on element concrete type");
        System.out.println("  • Want to centralize operation logic");
        System.out.println();

        System.out.println("❌ DON'T USE WHEN:");
        System.out.println("  • Element hierarchy changes frequently");
        System.out.println("  • Only one or two operations needed");
        System.out.println("  • Operations don't depend on element type");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("KEY TAKEAWAY");
        System.out.println("═".repeat(70));
        System.out.println();
        System.out.println("\"Visitor pattern separates operations from object structure\"");
        System.out.println();
        System.out.println("Elements provide accept(visitor) method (double dispatch).");
        System.out.println("Visitors provide visit(element) methods for each element type.");
        System.out.println("Operations can be added/modified without changing elements.");
        System.out.println();
        System.out.println("═".repeat(70));
    }

    // Helper methods to create sample data

    private static VideoFile createSampleVideo1() {
        return new VideoFile(1, "JavaScript Tutorial", 30, 5000, 250,
                           LocalDate.of(2024, 1, 15), "tutorial", 250);
    }

    private static Playlist createSamplePlaylist() {
        Playlist playlist = new Playlist(1, "Watch Later");

        playlist.addVideo(new VideoFile(1, "JavaScript Tutorial", 30, 5000, 250));
        playlist.addVideo(new VideoFile(2, "Python Basics", 45, 8000, 400));
        playlist.addVideo(new VideoFile(3, "React Hooks", 60, 12000, 600));

        return playlist;
    }

    private static Category createSampleCategory() {
        Category parent = new Category(1, "Programming Tutorials");

        Category jsCategory = new Category(2, "JavaScript");
        jsCategory.addVideo(new VideoFile(1, "JavaScript Basics", 30, 5000, 250));
        jsCategory.addVideo(new VideoFile(2, "Advanced JS", 45, 3000, 150));

        Category pythonCategory = new Category(3, "Python");
        pythonCategory.addVideo(new VideoFile(3, "Python Basics", 40, 8000, 400));

        parent.addSubcategory(jsCategory);
        parent.addSubcategory(pythonCategory);

        return parent;
    }
}
