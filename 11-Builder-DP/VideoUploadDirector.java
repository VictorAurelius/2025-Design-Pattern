import java.util.Arrays;

public class VideoUploadDirector {

	// ==================== COMMON BUILD WORKFLOWS ====================

	/**
	 * Workflow 1: Quick Public Upload
	 * Use case: Fast upload with minimal configuration
	 */
	public static VideoUpload buildQuickPublic(String title, String filePath) {
		System.out.println("\n🚀 Director: Building QUICK PUBLIC upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.build();  // Use all defaults
	}

	/**
	 * Workflow 2: Complete Public Upload
	 * Use case: Professional upload with full metadata
	 */
	public static VideoUpload buildCompletePublic(String title, String filePath,
	                                               String description, String[] tagArray) {
		System.out.println("\n🎬 Director: Building COMPLETE PUBLIC upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.description(description)
			.tags(Arrays.asList(tagArray))
			.category("Education")
			.visibility("public")
			.commentsEnabled(true)
			.ageRestricted(false)
			.license("standard")
			.language("en")
			.allowEmbedding(true)
			.notifySubscribers(true)
			.build();
	}

	/**
	 * Workflow 3: Private Draft Upload
	 * Use case: Upload for review before publishing
	 */
	public static VideoUpload buildPrivateDraft(String title, String filePath) {
		System.out.println("\n🔒 Director: Building PRIVATE DRAFT upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.visibility("private")
			.notifySubscribers(false)
			.build();
	}

	/**
	 * Workflow 4: Educational Content Upload
	 * Use case: Educational videos with strict moderation
	 */
	public static VideoUpload buildEducational(String title, String filePath,
	                                            String description) {
		System.out.println("\n📚 Director: Building EDUCATIONAL upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.description(description)
			.addTag("education")
			.addTag("tutorial")
			.addTag("learning")
			.category("Education")
			.visibility("public")
			.commentsEnabled(true)
			.ageRestricted(false)
			.license("creative commons")
			.language("en")
			.allowEmbedding(true)
			.notifySubscribers(true)
			.build();
	}

	/**
	 * Workflow 5: Kids Content Upload
	 * Use case: Child-safe content with restricted features
	 */
	public static VideoUpload buildKidsContent(String title, String filePath) {
		System.out.println("\n👶 Director: Building KIDS CONTENT upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.category("Kids")
			.visibility("public")
			.commentsEnabled(false)  // Disable comments for kids
			.ageRestricted(false)
			.allowEmbedding(false)   // Restrict embedding for safety
			.notifySubscribers(true)
			.build();
	}

	/**
	 * Workflow 6: Mature Content Upload
	 * Use case: Age-restricted content with limited distribution
	 */
	public static VideoUpload buildMatureContent(String title, String filePath,
	                                              String description) {
		System.out.println("\n🔞 Director: Building MATURE CONTENT upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.description(description)
			.visibility("unlisted")  // Limited distribution
			.commentsEnabled(true)
			.ageRestricted(true)     // Age gate
			.allowEmbedding(false)   // Restrict embedding
			.notifySubscribers(false) // Don't notify (sensitive)
			.build();
	}

	/**
	 * Workflow 7: Unlisted Link-Only Upload
	 * Use case: Share with specific people via link
	 */
	public static VideoUpload buildUnlistedShare(String title, String filePath,
	                                              String description) {
		System.out.println("\n🔗 Director: Building UNLISTED SHARE upload...");
		return new VideoUpload.VideoUploadBuilder(title, filePath)
			.description(description)
			.visibility("unlisted")
			.commentsEnabled(true)
			.allowEmbedding(true)
			.notifySubscribers(false)  // Don't notify for unlisted
			.build();
	}
}
