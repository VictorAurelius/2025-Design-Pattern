import java.util.ArrayList;
import java.util.List;

public class VideoUpload {

	// ==================== REQUIRED PARAMETERS (final - immutable) ====================
	private final String title;
	private final String filePath;

	// ==================== OPTIONAL PARAMETERS (final - immutable) ====================
	private final String description;
	private final List<String> tags;
	private final String category;
	private final String visibility;  // public, unlisted, private
	private final boolean commentsEnabled;
	private final boolean ageRestricted;
	private final String license;  // standard, creative commons
	private final String language;
	private final boolean allowEmbedding;
	private final boolean notifySubscribers;

	// ==================== PRIVATE CONSTRUCTOR ====================
	// Only Builder can create VideoUpload objects
	private VideoUpload(VideoUploadBuilder builder) {
		// Required parameters
		this.title = builder.title;
		this.filePath = builder.filePath;

		// Optional parameters
		this.description = builder.description;
		this.tags = builder.tags;
		this.category = builder.category;
		this.visibility = builder.visibility;
		this.commentsEnabled = builder.commentsEnabled;
		this.ageRestricted = builder.ageRestricted;
		this.license = builder.license;
		this.language = builder.language;
		this.allowEmbedding = builder.allowEmbedding;
		this.notifySubscribers = builder.notifySubscribers;
	}

	// ==================== GETTERS ONLY (NO SETTERS - IMMUTABLE) ====================
	public String getTitle() {
		return title;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getTags() {
		return new ArrayList<>(tags);  // Return defensive copy
	}

	public String getCategory() {
		return category;
	}

	public String getVisibility() {
		return visibility;
	}

	public boolean isCommentsEnabled() {
		return commentsEnabled;
	}

	public boolean isAgeRestricted() {
		return ageRestricted;
	}

	public String getLicense() {
		return license;
	}

	public String getLanguage() {
		return language;
	}

	public boolean isAllowEmbedding() {
		return allowEmbedding;
	}

	public boolean isNotifySubscribers() {
		return notifySubscribers;
	}

	// ==================== DISPLAY METHOD ====================
	public void displayUploadInfo() {
		System.out.println("\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║              VIDEO UPLOAD CONFIGURATION                    ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");
		System.out.println("📹 Title: " + title);
		System.out.println("📁 File: " + filePath);
		System.out.println("📝 Description: " + (description.isEmpty() ? "(none)" : description));
		System.out.println("🏷️  Tags: " + (tags.isEmpty() ? "(none)" : tags));
		System.out.println("📂 Category: " + category);
		System.out.println("👁️  Visibility: " + visibility);
		System.out.println("💬 Comments: " + (commentsEnabled ? "enabled" : "disabled"));
		System.out.println("🔞 Age Restriction: " + (ageRestricted ? "yes" : "no"));
		System.out.println("⚖️  License: " + license);
		System.out.println("🌐 Language: " + language);
		System.out.println("🔗 Allow Embedding: " + (allowEmbedding ? "yes" : "no"));
		System.out.println("🔔 Notify Subscribers: " + (notifySubscribers ? "yes" : "no"));
		System.out.println("════════════════════════════════════════════════════════════");
	}

	// ==================== INNER STATIC BUILDER CLASS ====================
	public static class VideoUploadBuilder {

		// ========== REQUIRED PARAMETERS ==========
		private final String title;
		private final String filePath;

		// ========== OPTIONAL PARAMETERS (with default values) ==========
		private String description = "";
		private List<String> tags = new ArrayList<>();
		private String category = "Entertainment";
		private String visibility = "public";
		private boolean commentsEnabled = true;
		private boolean ageRestricted = false;
		private String license = "standard";
		private String language = "en";
		private boolean allowEmbedding = true;
		private boolean notifySubscribers = true;

		// ========== CONSTRUCTOR (only required parameters) ==========
		public VideoUploadBuilder(String title, String filePath) {
			this.title = title;
			this.filePath = filePath;
		}

		// ========== FLUENT SETTERS (return this for method chaining) ==========
		public VideoUploadBuilder description(String description) {
			this.description = description;
			return this;
		}

		public VideoUploadBuilder tags(List<String> tags) {
			this.tags = tags;
			return this;
		}

		public VideoUploadBuilder addTag(String tag) {
			this.tags.add(tag);
			return this;
		}

		public VideoUploadBuilder category(String category) {
			this.category = category;
			return this;
		}

		public VideoUploadBuilder visibility(String visibility) {
			this.visibility = visibility;
			return this;
		}

		public VideoUploadBuilder commentsEnabled(boolean commentsEnabled) {
			this.commentsEnabled = commentsEnabled;
			return this;
		}

		public VideoUploadBuilder ageRestricted(boolean ageRestricted) {
			this.ageRestricted = ageRestricted;
			return this;
		}

		public VideoUploadBuilder license(String license) {
			this.license = license;
			return this;
		}

		public VideoUploadBuilder language(String language) {
			this.language = language;
			return this;
		}

		public VideoUploadBuilder allowEmbedding(boolean allowEmbedding) {
			this.allowEmbedding = allowEmbedding;
			return this;
		}

		public VideoUploadBuilder notifySubscribers(boolean notifySubscribers) {
			this.notifySubscribers = notifySubscribers;
			return this;
		}

		// ========== BUILD METHOD (with validation) ==========
		public VideoUpload build() {
			// Validation logic
			if (title == null || title.trim().isEmpty()) {
				throw new IllegalStateException("Title is required and cannot be empty");
			}
			if (filePath == null || filePath.trim().isEmpty()) {
				throw new IllegalStateException("File path is required and cannot be empty");
			}
			if (!visibility.matches("public|unlisted|private")) {
				throw new IllegalStateException("Visibility must be: public, unlisted, or private");
			}
			if (!license.matches("standard|creative commons")) {
				throw new IllegalStateException("License must be: standard or creative commons");
			}

			// Create and return the VideoUpload object
			return new VideoUpload(this);
		}
	}
}
