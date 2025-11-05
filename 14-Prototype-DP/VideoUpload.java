import java.util.ArrayList;
import java.util.List;

public class VideoUpload implements VideoUploadPrototype {

	// Mutable fields (can be changed after cloning)
	private String title;
	private String filePath;

	// Immutable fields (configured once in Builder)
	private final String description;
	private final List<String> tags;
	private final String category;
	private final String visibility;
	private final boolean commentsEnabled;
	private final boolean ageRestricted;
	private final String license;
	private final String language;
	private final boolean allowEmbedding;
	private final boolean notifySubscribers;

	// Private constructor - only Builder can create
	private VideoUpload(VideoUploadBuilder builder) {
		this.title = builder.title;
		this.filePath = builder.filePath;
		this.description = builder.description;
		this.tags = new ArrayList<>(builder.tags);  // Defensive copy
		this.category = builder.category;
		this.visibility = builder.visibility;
		this.commentsEnabled = builder.commentsEnabled;
		this.ageRestricted = builder.ageRestricted;
		this.license = builder.license;
		this.language = builder.language;
		this.allowEmbedding = builder.allowEmbedding;
		this.notifySubscribers = builder.notifySubscribers;
	}

	// Clone method with DEEP COPY
	@Override
	public VideoUpload clone() {
		try {
			// Shallow copy (copies primitives and references)
			VideoUpload cloned = (VideoUpload) super.clone();

			// Deep copy mutable objects (List)
			// This is CRITICAL - without this, both objects share same list!
			cloned.tags = new ArrayList<>(this.tags);

			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Cloning not supported", e);
		}
	}

	// Setters for mutable fields only (title and filePath)
	public void setTitle(String title) {
		this.title = title;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	// Getters for all fields
	public String getTitle() {
		return title;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDescription() {
		return description;
	}

	// Return defensive copy to prevent external modification
	public List<String> getTags() {
		return new ArrayList<>(tags);
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

	// Display method
	public void displayInfo() {
		System.out.println("   Title: " + title);
		System.out.println("   File: " + filePath);
		System.out.println("   Description: " + description);
		System.out.println("   Tags: " + tags);
		System.out.println("   Category: " + category);
		System.out.println("   Visibility: " + visibility);
		System.out.println("   License: " + license);
	}

	// ==================== INNER BUILDER CLASS ====================
	public static class VideoUploadBuilder {

		// Required parameters
		private final String title;
		private final String filePath;

		// Optional parameters with defaults
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

		public VideoUploadBuilder(String title, String filePath) {
			this.title = title;
			this.filePath = filePath;
		}

		public VideoUploadBuilder description(String description) {
			this.description = description;
			return this;
		}

		public VideoUploadBuilder tags(List<String> tags) {
			this.tags = new ArrayList<>(tags);
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

		public VideoUpload build() {
			return new VideoUpload(this);
		}
	}
}
