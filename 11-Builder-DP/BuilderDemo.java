import java.util.Arrays;

public class BuilderDemo {

	public static void main(String[] args) {

		System.out.println("╔════════════════════════════════════════════════════════════╗");
		System.out.println("║              BUILDER PATTERN DEMO                          ║");
		System.out.println("║         Video Upload Configuration Builder                ║");
		System.out.println("║  (Linked: StreamFlix + YouTube patterns)                  ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");

		// ==================== PROBLEM DEMONSTRATION ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("PROBLEM: Telescoping Constructor Anti-Pattern");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n❌ Without Builder Pattern:");
		System.out.println("   With 12 parameters (2 required + 10 optional):");
		System.out.println("   You need 2^10 = 1,024 constructor overloads! 😱");
		System.out.println("\n   Examples of ugly constructors needed:");
		System.out.println("   VideoUpload(title, file)");
		System.out.println("   VideoUpload(title, file, desc)");
		System.out.println("   VideoUpload(title, file, desc, tags)");
		System.out.println("   VideoUpload(title, file, desc, tags, category)");
		System.out.println("   ... 1,020 more constructors! 🤯");
		System.out.println("\n   Problems:");
		System.out.println("   1. Unreadable: new VideoUpload(t,f,d,null,null,true,false,\"s\",\"en\",true,false)");
		System.out.println("   2. Error-prone: Easy to swap parameters of same type");
		System.out.println("   3. Unmaintainable: Adding 1 parameter = 512 new constructors!");

		// ==================== SOLUTION 1: BUILDER WITHOUT DIRECTOR ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("SOLUTION 1: Builder Pattern (Manual Construction)");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n✅ With Builder Pattern - Fluent Interface:");
		System.out.println("   Easy to read, self-documenting code! 🎉");

		VideoUpload upload1 = new VideoUpload.VideoUploadBuilder(
			"Design Patterns Tutorial - Builder",
			"/videos/builder-tutorial.mp4"
		)
			.description("Learn the Builder pattern with real examples")
			.addTag("design-patterns")
			.addTag("java")
			.addTag("tutorial")
			.category("Education")
			.visibility("public")
			.commentsEnabled(true)
			.license("creative commons")
			.language("en")
			.build();

		upload1.displayUploadInfo();

		// ==================== SOLUTION 2: BUILDER WITH MINIMAL CONFIG ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("SOLUTION 2: Minimal Configuration (Only Required Params)");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n✅ Builder with defaults - Just 2 required parameters:");

		VideoUpload upload2 = new VideoUpload.VideoUploadBuilder(
			"Quick Vlog",
			"/videos/vlog-001.mp4"
		)
			.build();  // Uses all default values!

		upload2.displayUploadInfo();

		// ==================== SOLUTION 3: BUILDER WITH DIRECTOR ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("SOLUTION 3: Using Director (Common Workflows)");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n🎯 Director provides pre-configured workflows:");
		System.out.println("   - Quick Public Upload (fast, minimal config)");
		System.out.println("   - Complete Public Upload (full metadata)");
		System.out.println("   - Private Draft (review before publish)");
		System.out.println("   - Educational Content (CC license)");
		System.out.println("   - Kids Content (comments disabled, no embed)");
		System.out.println("   - Mature Content (age-restricted)");
		System.out.println("   - Unlisted Share (link-only access)");

		// Test 1: Quick Public
		VideoUpload upload3 = VideoUploadDirector.buildQuickPublic(
			"My First Upload",
			"/videos/first-video.mp4"
		);
		upload3.displayUploadInfo();

		// Test 2: Complete Public
		VideoUpload upload4 = VideoUploadDirector.buildCompletePublic(
			"StreamFlix Platform Demo",
			"/videos/streamflix-demo.mp4",
			"Full demo of our video streaming platform using Proxy + Flyweight patterns",
			new String[]{"streaming", "platform", "demo", "proxy", "flyweight"}
		);
		upload4.displayUploadInfo();

		// Test 3: Educational
		VideoUpload upload5 = VideoUploadDirector.buildEducational(
			"Observer Pattern Explained",
			"/videos/observer-pattern.mp4",
			"YouTube-style notification system using Observer pattern"
		);
		upload5.displayUploadInfo();

		// Test 4: Kids Content
		VideoUpload upload6 = VideoUploadDirector.buildKidsContent(
			"Learn ABC Song",
			"/videos/kids-abc.mp4"
		);
		upload6.displayUploadInfo();

		// Test 5: Mature Content
		VideoUpload upload7 = VideoUploadDirector.buildMatureContent(
			"Horror Game Walkthrough",
			"/videos/horror-game.mp4",
			"Scary horror game with mature themes"
		);
		upload7.displayUploadInfo();

		// ==================== VALIDATION TEST ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("TEST: Builder Validation");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n❌ Testing invalid visibility:");
		try {
			VideoUpload invalid = new VideoUpload.VideoUploadBuilder(
				"Test",
				"/test.mp4"
			)
				.visibility("invalid-value")  // Should fail validation
				.build();
		} catch (IllegalStateException e) {
			System.out.println("✓ Validation caught error: " + e.getMessage());
		}

		System.out.println("\n❌ Testing empty title:");
		try {
			VideoUpload invalid = new VideoUpload.VideoUploadBuilder(
				"",  // Empty title - should fail
				"/test.mp4"
			)
				.build();
		} catch (IllegalStateException e) {
			System.out.println("✓ Validation caught error: " + e.getMessage());
		}

		// ==================== BENEFITS SUMMARY ====================
		System.out.println("\n\n═══════════════════════════════════════════════════════════");
		System.out.println("BENEFITS OF BUILDER PATTERN");
		System.out.println("═══════════════════════════════════════════════════════════");

		System.out.println("\n✅ Advantages:");
		System.out.println("   1. READABILITY: 10x improvement - self-documenting code");
		System.out.println("   2. FLEXIBILITY: Optional parameters without 1,024 constructors");
		System.out.println("   3. IMMUTABILITY: Thread-safe with final fields");
		System.out.println("   4. VALIDATION: Centralized validation logic in build()");
		System.out.println("   5. MAINTAINABILITY: Add parameter = 1 setter (not 512 constructors!)");
		System.out.println("   6. FLUENT API: Method chaining for natural syntax");
		System.out.println("   7. DIRECTOR: Reusable workflows for common scenarios");

		System.out.println("\n⚠️  Trade-offs:");
		System.out.println("   1. More code initially (but saves 100x maintenance time)");
		System.out.println("   2. Two classes (Product + Builder)");
		System.out.println("   3. Slight overhead (negligible for complex objects)");

		System.out.println("\n📊 ROI Calculation:");
		System.out.println("   - Initial effort: 1 hour to create Builder");
		System.out.println("   - Maintenance saved: 10 mins/change × 20 changes/year = 200 mins");
		System.out.println("   - Debugging saved: 30 mins/year (fewer parameter swap bugs)");
		System.out.println("   - Onboarding saved: 1 hour/year (self-documenting code)");
		System.out.println("   - Total saved: ~4.5 hours/year per developer");
		System.out.println("   - ROI: 450% in first year, 14,000% over 5 years! 🚀");

		// ==================== CONTEXT LINKING ====================
		System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║                 CONTEXT LINKING                            ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");

		System.out.println("\n🎬 Video Platform Design Pattern Cluster:");
		System.out.println("   1. ADAPTER: MediaPlayer (mp3, mp4, vlc compatibility)");
		System.out.println("   2. OBSERVER: YouTube Channel (notification system)");
		System.out.println("   3. PROXY: StreamFlix (lazy video loading)");
		System.out.println("   4. FLYWEIGHT: Video Player UI (share icons, save 9,000x memory)");
		System.out.println("   5. BUILDER: Video Upload (12 params without telescoping hell)");
		System.out.println("\n💡 Memorization Strategy:");
		System.out.println("   All 5 patterns in ONE domain = Easier to remember!");
		System.out.println("   Think: \"Video Platform\" → Recall all 5 patterns instantly! 🧠");

		System.out.println("\n════════════════════════════════════════════════════════════");
	}
}
