/**
 * DecoratorPatternDemo - Comprehensive demonstration of Decorator pattern
 *
 * Demonstrates:
 * 1. Free vs Premium user experience (simple decorators)
 * 2. Business customer with complex requirements (multiple decorators)
 * 3. Runtime feature switching (dynamic decorator addition/removal)
 * 4. Decorator stacking and order (understanding execution flow)
 * 5. Extensibility - no class explosion (pattern benefits)
 *
 * Scenario: StreamFlix video platform
 * Different users require different combinations of stream features:
 * - Free users: Basic stream + ads
 * - Premium users: HD stream + subtitles
 * - Business users: HD + watermark + analytics + DRM
 * - Mobile users: Quality adjustment based on network
 *
 * Key Insight:
 * Without Decorator pattern: Would need 100+ classes for all combinations
 * With Decorator pattern: 3 base streams + 6 decorators = unlimited combinations!
 */
public class DecoratorPatternDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║      DECORATOR PATTERN - Dynamic Video Stream Enhancement        ║");
        System.out.println("║                    StreamFlix Platform                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Run demonstration scenarios
        demonstrateFreeVsPremium();
        demonstrateBusinessCustomer();
        demonstrateRuntimeSwitching();
        demonstrateDecoratorStacking();
        demonstrateExtensibility();
        showPatternBenefits();
    }

    /**
     * Scenario 1: Free vs Premium User Experience
     * Shows how same video has different features based on subscription tier
     */
    private static void demonstrateFreeVsPremium() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 1: Free vs Premium User Experience");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Video: \"JavaScript Tutorial - Async/Await\"");
        System.out.println();

        // Free User
        System.out.println("FREE USER (Alex):");
        System.out.println("  Stream: Basic 480p + Ads");
        System.out.println();

        VideoStream freeStream = new BasicVideoStream();
        freeStream = new AdvertisementDecorator(freeStream, "pre-roll,mid-roll");

        System.out.println("  Configuration:");
        System.out.println("    ├─ BasicVideoStream (480p, 500 kbps)");
        System.out.println("    └─ AdvertisementDecorator (pre-roll, mid-roll)");
        System.out.println();
        System.out.println("  Total Bitrate: " + freeStream.getBitrate() + " kbps");
        System.out.println();
        System.out.println("  Playback:");
        System.out.println("    " + freeStream.play());
        System.out.println();
        System.out.println("  User Experience: ⭐⭐ (Ads interrupt learning)");
        System.out.println();

        // Premium User
        System.out.println("PREMIUM USER (Maria):");
        System.out.println("  Stream: HD 1080p + Subtitles");
        System.out.println();

        VideoStream premiumStream = new HDVideoStream();
        premiumStream = new SubtitleDecorator(premiumStream, "EN");

        System.out.println("  Configuration:");
        System.out.println("    ├─ HDVideoStream (1080p, 2500 kbps)");
        System.out.println("    └─ SubtitleDecorator (EN)");
        System.out.println();
        System.out.println("  Total Bitrate: " + premiumStream.getBitrate() + " kbps");
        System.out.println();
        System.out.println("  Playback:");
        System.out.println("    " + premiumStream.play());
        System.out.println();
        System.out.println("  User Experience: ⭐⭐⭐⭐⭐ (Smooth, high-quality)");
        System.out.println();

        System.out.println("BENEFIT: Same video, different features applied dynamically!");
    }

    /**
     * Scenario 2: Business Customer with Complex Requirements
     * Shows multiple decorators stacked together
     */
    private static void demonstrateBusinessCustomer() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 2: Business Customer with Custom Requirements");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Client: TechCorp Inc.");
        System.out.println("Video: \"Cybersecurity Training Module 1\"");
        System.out.println();
        System.out.println("Requirements:");
        System.out.println("  ✓ Company watermark (branding)");
        System.out.println("  ✓ Multi-language subtitles (EN, ES, FR)");
        System.out.println("  ✓ Analytics tracking (compliance)");
        System.out.println("  ✓ DRM protection (prevent leaks)");
        System.out.println("  ✓ HD quality (professionalism)");
        System.out.println();

        // Build complex stream with 6 decorators
        VideoStream businessStream = new HDVideoStream();
        businessStream = new SubtitleDecorator(businessStream, "EN");
        businessStream = new SubtitleDecorator(businessStream, "ES");
        businessStream = new SubtitleDecorator(businessStream, "FR");
        businessStream = new WatermarkDecorator(businessStream, "TechCorp Confidential", "top-right");
        businessStream = new AnalyticsDecorator(businessStream, "cybersecurity_training_q1_2024");
        businessStream = new DRMDecorator(businessStream, "enterprise_license_techcorp");

        System.out.println("Stream Configuration:");
        System.out.println("  ├─ HDVideoStream (1080p, 2500 kbps)");
        System.out.println("  ├─ SubtitleDecorator (EN)");
        System.out.println("  ├─ SubtitleDecorator (ES)");
        System.out.println("  ├─ SubtitleDecorator (FR)");
        System.out.println("  ├─ WatermarkDecorator (\"TechCorp Confidential\", \"top-right\")");
        System.out.println("  ├─ AnalyticsDecorator (\"cybersecurity_training_q1_2024\")");
        System.out.println("  └─ DRMDecorator (\"enterprise_license_techcorp\")");
        System.out.println();

        System.out.println("Total Bitrate: " + businessStream.getBitrate() + " kbps");
        System.out.println("  Base HD: 2500 kbps");
        System.out.println("  + Subtitles (EN): 100 kbps");
        System.out.println("  + Subtitles (ES): 100 kbps");
        System.out.println("  + Subtitles (FR): 100 kbps");
        System.out.println("  + Watermark: 50 kbps");
        System.out.println("  + Analytics: 30 kbps");
        System.out.println("  + DRM: 100 kbps (encryption overhead)");
        System.out.println();

        System.out.println("Description:");
        System.out.println("  " + businessStream.getDescription());
        System.out.println();

        System.out.println("Features Added Dynamically: 6");
        System.out.println("Time to Configure: < 1 second");
        System.out.println("Maintenance Cost: $0 (no new classes needed!)");
        System.out.println();

        System.out.println("Without Decorator Pattern:");
        System.out.println("  Would need: HDVideoStreamWithTripleSubtitlesAndWatermarkAndAnalyticsAndDRM class");
        System.out.println("  + 100 other classes for all combinations");
        System.out.println("  Maintenance nightmare!");
    }

    /**
     * Scenario 3: Runtime Feature Switching
     * Shows dynamic decorator addition based on changing context
     */
    private static void demonstrateRuntimeSwitching() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 3: Dynamic Feature Switching During Playback");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("User: Sarah watching \"Design Patterns Course\"");
        System.out.println("Context: Commuting on train (changing network conditions)");
        System.out.println();

        // 10:00 AM - Home WiFi
        System.out.println("10:00 AM - Start watching at home (WiFi):");
        VideoStream stream1 = new HDVideoStream();
        stream1 = new SubtitleDecorator(stream1, "EN");
        System.out.println("  Stream: HD (1080p) + Subtitles (EN)");
        System.out.println("  Bitrate: " + stream1.getBitrate() + " kbps");
        System.out.println("  ✓ Smooth playback");
        System.out.println();

        // 10:30 AM - Mobile 4G
        System.out.println("10:30 AM - Leave home, switch to mobile data:");
        System.out.println("  Network: 4G, bandwidth limited");
        System.out.println("  Action: Downgrade quality");
        System.out.println();
        VideoStream stream2 = new BasicVideoStream();
        stream2 = new SubtitleDecorator(stream2, "EN");
        stream2 = new QualityAdjustmentDecorator(stream2, "auto");
        System.out.println("  New Stream: Basic (480p) + Subtitles (EN) + Quality Adjust (auto)");
        System.out.println("  Bitrate: " + stream2.getBitrate() + " kbps");
        System.out.println("  ✓ Reduced buffering");
        System.out.println();

        // 11:00 AM - Poor 3G
        System.out.println("11:00 AM - Enter tunnel, poor connection:");
        System.out.println("  Network: 3G, very slow");
        System.out.println("  Action: Further downgrade");
        System.out.println();
        VideoStream stream3 = new BasicVideoStream();
        stream3 = new QualityAdjustmentDecorator(stream3, "aggressive");
        System.out.println("  New Stream: Basic (480p) + Quality Adjust (aggressive)");
        System.out.println("  Bitrate: " + stream3.getBitrate() + " kbps (removed subtitles, ultra compression)");
        System.out.println("  ✓ Video continues playing (no interruption)");
        System.out.println();

        // 12:00 PM - Office WiFi
        System.out.println("12:00 PM - Arrive at office, back on WiFi:");
        System.out.println("  Network: High-speed WiFi");
        System.out.println("  Action: Upgrade to best quality");
        System.out.println();
        VideoStream stream4 = new UHDVideoStream();
        stream4 = new SubtitleDecorator(stream4, "EN");
        System.out.println("  New Stream: UHD (4K) + Subtitles (EN)");
        System.out.println("  Bitrate: " + stream4.getBitrate() + " kbps");
        System.out.println("  ✓ Maximum quality restored");
        System.out.println();

        System.out.println("BENEFIT: Same video object, features added/removed dynamically!");
        System.out.println("         No need to create new video instance!");
        System.out.println("         Seamless user experience!");
    }

    /**
     * Scenario 4: Understanding Decorator Stacking and Execution Order
     * Shows how decorators are processed in a chain
     */
    private static void demonstrateDecoratorStacking() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 4: Understanding Decorator Order");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Base Video: \"React Hooks Tutorial\"");
        System.out.println();

        System.out.println("Decorators Applied (in order):");
        System.out.println("  1. BasicVideoStream         → \"Playing basic 480p video stream\"");
        System.out.println("  2. SubtitleDecorator (EN)   → + \"Subtitles (EN)\"");
        System.out.println("  3. WatermarkDecorator       → + \"Watermark [Company @ top-right]\"");
        System.out.println("  4. AdvertisementDecorator   → \"[Pre-roll ad] \" + content + \" [Post-roll ad]\"");
        System.out.println();

        // Build the chain
        VideoStream stream = new BasicVideoStream();
        System.out.println("Step 1: Create base stream");
        System.out.println("  Description: " + stream.getDescription());
        System.out.println("  Bitrate: " + stream.getBitrate() + " kbps");
        System.out.println();

        stream = new SubtitleDecorator(stream, "EN");
        System.out.println("Step 2: Add subtitles");
        System.out.println("  Description: " + stream.getDescription());
        System.out.println("  Bitrate: " + stream.getBitrate() + " kbps");
        System.out.println();

        stream = new WatermarkDecorator(stream, "Company", "top-right");
        System.out.println("Step 3: Add watermark");
        System.out.println("  Description: " + stream.getDescription());
        System.out.println("  Bitrate: " + stream.getBitrate() + " kbps");
        System.out.println();

        stream = new AdvertisementDecorator(stream, "pre-roll,post-roll");
        System.out.println("Step 4: Add advertisements");
        System.out.println("  Description: " + stream.getDescription());
        System.out.println("  Bitrate: " + stream.getBitrate() + " kbps");
        System.out.println();

        System.out.println("Final Output:");
        System.out.println("  " + stream.play());
        System.out.println();

        System.out.println("Execution Flow (play() method calls):");
        System.out.println("  AdvertisementDecorator.play()");
        System.out.println("    → Adds \"[Pre-roll ad]\"");
        System.out.println("    → Calls wrappedStream.play() // WatermarkDecorator");
        System.out.println("  WatermarkDecorator.play()");
        System.out.println("    → Calls wrappedStream.play() // SubtitleDecorator");
        System.out.println("    → Adds \"+ Watermark [Company @ top-right]\"");
        System.out.println("  SubtitleDecorator.play()");
        System.out.println("    → Calls wrappedStream.play() // BasicVideoStream");
        System.out.println("    → Adds \"+ Subtitles (EN)\"");
        System.out.println("  BasicVideoStream.play()");
        System.out.println("    → Returns \"Playing basic 480p video stream\"");
        System.out.println();

        System.out.println("Bitrate Calculation:");
        System.out.println("  BasicVideoStream:       500 kbps (base)");
        System.out.println("  SubtitleDecorator:     +100 kbps");
        System.out.println("  WatermarkDecorator:     +50 kbps");
        System.out.println("  AdvertisementDecorator: +200 kbps");
        System.out.println("  Total: " + stream.getBitrate() + " kbps");
        System.out.println();

        System.out.println("KEY INSIGHT: Decorators are processed in reverse order for accumulation!");
    }

    /**
     * Scenario 5: Extensibility - Adding New Features Without Class Explosion
     * Shows the main benefit of Decorator pattern
     */
    private static void demonstrateExtensibility() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("SCENARIO 5: Adding New Features Without Modifying Existing Code");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("NEW REQUIREMENT (Q2 2024): Add \"Quality Adjustment\" feature");
        System.out.println("  - Dynamically adjust quality based on network bandwidth");
        System.out.println("  - Requested by 10,000 users");
        System.out.println();

        System.out.println("WITHOUT Decorator Pattern:");
        System.out.println("  ❌ Modify 50+ existing video classes");
        System.out.println("  ❌ Create 100+ new combination classes");
        System.out.println("  ❌ Risk breaking existing functionality");
        System.out.println("  ❌ Testing nightmare (500+ test cases)");
        System.out.println("  ❌ Development time: 2 weeks");
        System.out.println("  ❌ Bug risk: HIGH");
        System.out.println();

        System.out.println("WITH Decorator Pattern:");
        System.out.println("  ✅ Create ONE new class: QualityAdjustmentDecorator");
        System.out.println("  ✅ No modification to existing code (Open/Closed Principle)");
        System.out.println("  ✅ Works with all existing combinations");
        System.out.println("  ✅ Testing: Only test QualityAdjustmentDecorator (10 test cases)");
        System.out.println("  ✅ Development time: 2 hours");
        System.out.println("  ✅ Bug risk: MINIMAL");
        System.out.println();

        System.out.println("Usage (works immediately with all existing streams):");
        System.out.println();

        VideoStream stream = new HDVideoStream();
        stream = new SubtitleDecorator(stream, "EN");
        stream = new QualityAdjustmentDecorator(stream, "auto");

        System.out.println("  VideoStream stream = new HDVideoStream();");
        System.out.println("  stream = new SubtitleDecorator(stream, \"EN\");");
        System.out.println("  stream = new QualityAdjustmentDecorator(stream, \"auto\");");
        System.out.println();
        System.out.println("  Output: " + stream.getDescription());
        System.out.println();

        System.out.println("RESULT: Feature deployed in 2 hours instead of 2 weeks!");
        System.out.println("        Cost savings: $10,000+");
        System.out.println("        Zero bugs introduced!");
        System.out.println("        Customer satisfaction: ⭐⭐⭐⭐⭐");
    }

    /**
     * Show pattern benefits summary
     */
    private static void showPatternBenefits() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("DECORATOR PATTERN BENEFITS DEMONSTRATED");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✓ NO CLASS EXPLOSION");
        System.out.println("  - 3 base streams + 6 decorators = unlimited combinations");
        System.out.println("  - Without pattern: Would need 100+ classes");
        System.out.println("  - Savings: 90%+ code reduction");
        System.out.println();

        System.out.println("✓ OPEN/CLOSED PRINCIPLE");
        System.out.println("  - Add new features without modifying existing code");
        System.out.println("  - New decorator = ONE new class");
        System.out.println("  - Zero impact on existing functionality");
        System.out.println();

        System.out.println("✓ FLEXIBLE COMPOSITION");
        System.out.println("  - Mix and match features at runtime");
        System.out.println("  - Add/remove features dynamically");
        System.out.println("  - Different users get different combinations");
        System.out.println();

        System.out.println("✓ SINGLE RESPONSIBILITY");
        System.out.println("  - Each decorator handles ONE feature");
        System.out.println("  - SubtitleDecorator: Only subtitles");
        System.out.println("  - WatermarkDecorator: Only watermarks");
        System.out.println("  - Clean separation of concerns");
        System.out.println();

        System.out.println("✓ TRANSPARENT WRAPPING");
        System.out.println("  - Client treats decorated object same as base object");
        System.out.println("  - All implement VideoStream interface");
        System.out.println("  - Seamless substitution");
        System.out.println();

        System.out.println("✓ RUNTIME CONFIGURATION");
        System.out.println("  - Configure features without recompiling");
        System.out.println("  - Adapt to user preferences");
        System.out.println("  - Adapt to context (WiFi vs mobile)");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("BEFORE vs AFTER COMPARISON");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Class Count:");
        System.out.println("  Before: 100+ classes (every combination)");
        System.out.println("  After:  10 classes (3 streams + 1 decorator base + 6 decorators)");
        System.out.println("  Improvement: 90% reduction");
        System.out.println();

        System.out.println("Feature Combinations:");
        System.out.println("  Before: Limited to hardcoded classes");
        System.out.println("  After:  Unlimited (runtime composition)");
        System.out.println("  Improvement: ∞ flexibility");
        System.out.println();

        System.out.println("Adding New Feature:");
        System.out.println("  Before: 2 weeks (modify 50+ classes)");
        System.out.println("  After:  2 hours (add 1 class)");
        System.out.println("  Improvement: 95% faster");
        System.out.println();

        System.out.println("Maintenance Cost:");
        System.out.println("  Before: High (changes ripple through all classes)");
        System.out.println("  After:  Low (changes isolated to one decorator)");
        System.out.println("  Improvement: 80% cost reduction");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("WHEN TO USE DECORATOR PATTERN");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("✅ USE WHEN:");
        System.out.println("  • Need to add responsibilities to objects dynamically");
        System.out.println("  • Subclassing would cause class explosion");
        System.out.println("  • Features can be combined in many ways");
        System.out.println("  • Want to add/remove features at runtime");
        System.out.println();

        System.out.println("✅ REAL-WORLD USE CASES:");
        System.out.println("  • Java I/O: BufferedReader(FileReader(file))");
        System.out.println("  • GUI components: borders, scrollbars, shadows");
        System.out.println("  • Web frameworks: middleware, filters, interceptors");
        System.out.println("  • Video/audio processing: filters, effects, codecs");
        System.out.println();

        System.out.println("❌ DON'T USE WHEN:");
        System.out.println("  • Only one combination needed (use inheritance)");
        System.out.println("  • Features depend heavily on each other (use strategy)");
        System.out.println("  • Performance is critical (decorator chain overhead)");
        System.out.println();

        System.out.println("═".repeat(70));
        System.out.println("KEY TAKEAWAY");
        System.out.println("═".repeat(70));
        System.out.println();
        System.out.println("\"Decorators add responsibilities to objects dynamically\"");
        System.out.println();
        System.out.println("Instead of creating subclasses for every feature combination,");
        System.out.println("wrap objects with decorators that add features one at a time.");
        System.out.println("This avoids class explosion and enables runtime composition.");
        System.out.println();
        System.out.println("═".repeat(70));
    }
}
