import java.util.Arrays;

public class BuilderDemo {

	public static void main(String[] args) {

		System.out.println("=== Builder Pattern Demo ===\n");

		// Test 1: Manual builder with multiple optional parameters
		System.out.println("--- Test 1: Full Configuration ---");
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

		// Test 2: Minimal configuration (only required parameters)
		System.out.println("\n--- Test 2: Minimal Configuration ---");
		VideoUpload upload2 = new VideoUpload.VideoUploadBuilder(
			"Quick Vlog",
			"/videos/vlog-001.mp4"
		)
			.build();  // Uses default values

		upload2.displayUploadInfo();

		// Test 3: Using Director for quick public upload
		System.out.println("\n--- Test 3: Director - Quick Public ---");
		VideoUpload upload3 = VideoUploadDirector.buildQuickPublic(
			"My First Upload",
			"/videos/first-video.mp4"
		);
		upload3.displayUploadInfo();

		// Test 4: Using Director for complete public upload
		System.out.println("\n--- Test 4: Director - Complete Public ---");
		VideoUpload upload4 = VideoUploadDirector.buildCompletePublic(
			"StreamFlix Platform Demo",
			"/videos/streamflix-demo.mp4",
			"Full demo of our video streaming platform",
			new String[]{"streaming", "platform", "demo"}
		);
		upload4.displayUploadInfo();

		// Test 5: Using Director for kids content
		System.out.println("\n--- Test 5: Director - Kids Content ---");
		VideoUpload upload5 = VideoUploadDirector.buildKidsContent(
			"Learn ABC Song",
			"/videos/kids-abc.mp4"
		);
		upload5.displayUploadInfo();

		// Test 6: Validation test
		System.out.println("\n--- Test 6: Validation ---");
		try {
			VideoUpload invalid = new VideoUpload.VideoUploadBuilder(
				"",  // Empty title - should fail
				"/test.mp4"
			)
				.build();
		} catch (IllegalStateException e) {
			System.out.println("Validation caught error: " + e.getMessage());
		}
	}
}
