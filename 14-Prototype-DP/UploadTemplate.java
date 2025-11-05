import java.util.Arrays;

public class UploadTemplate {

	// Pre-configured Gaming template
	public static VideoUpload createGamingTemplate() {
		return new VideoUpload.VideoUploadBuilder(
			"Gaming Template",
			""
		)
			.description("Epic gaming moments")
			.tags(Arrays.asList("gaming", "gameplay", "epic"))
			.category("Gaming")
			.visibility("public")
			.commentsEnabled(true)
			.ageRestricted(false)
			.license("standard")
			.language("en")
			.allowEmbedding(true)
			.notifySubscribers(true)
			.build();
	}

	// Pre-configured Tutorial template
	public static VideoUpload createTutorialTemplate() {
		return new VideoUpload.VideoUploadBuilder(
			"Tutorial Template",
			""
		)
			.description("Educational content")
			.tags(Arrays.asList("tutorial", "education", "howto"))
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

	// Pre-configured Vlog template
	public static VideoUpload createVlogTemplate() {
		return new VideoUpload.VideoUploadBuilder(
			"Vlog Template",
			""
		)
			.description("Daily life and adventures")
			.tags(Arrays.asList("vlog", "daily", "lifestyle"))
			.category("Entertainment")
			.visibility("public")
			.commentsEnabled(true)
			.ageRestricted(false)
			.license("standard")
			.language("en")
			.allowEmbedding(true)
			.notifySubscribers(true)
			.build();
	}

	// Pre-configured Music template
	public static VideoUpload createMusicTemplate() {
		return new VideoUpload.VideoUploadBuilder(
			"Music Template",
			""
		)
			.description("Original music content")
			.tags(Arrays.asList("music", "original", "audio"))
			.category("Music")
			.visibility("public")
			.commentsEnabled(true)
			.ageRestricted(false)
			.license("creative commons")
			.language("en")
			.allowEmbedding(true)
			.notifySubscribers(false)
			.build();
	}
}
