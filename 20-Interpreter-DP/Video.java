/**
 * Video - Context class for Interpreter pattern
 *
 * Represents a video object with properties that can be queried.
 * This is the "context" that expressions interpret against.
 *
 * Properties available for querying:
 * - duration (int): Video duration in minutes
 * - views (int): Number of views
 * - likes (int): Number of likes
 * - comments (int): Number of comments
 * - category (String): Video category (tutorial, review, vlog, etc.)
 * - resolution (String): Video resolution (720p, 1080p, 4K, 8K)
 * - uploadedDate (String): Upload date in YYYY-MM-DD format
 */
public class Video {

	private String title;
	private int duration;        // minutes
	private int views;
	private int likes;
	private int comments;
	private String category;
	private String resolution;
	private String uploadedDate; // YYYY-MM-DD

	/**
	 * Create video with all properties
	 */
	public Video(String title, int duration, int views, int likes, int comments,
	             String category, String resolution, String uploadedDate) {
		this.title = title;
		this.duration = duration;
		this.views = views;
		this.likes = likes;
		this.comments = comments;
		this.category = category;
		this.resolution = resolution;
		this.uploadedDate = uploadedDate;
	}

	// Getters
	public String getTitle() { return title; }
	public int getDuration() { return duration; }
	public int getViews() { return views; }
	public int getLikes() { return likes; }
	public int getComments() { return comments; }
	public String getCategory() { return category; }
	public String getResolution() { return resolution; }
	public String getUploadedDate() { return uploadedDate; }

	/**
	 * Generic property accessor for query interpreter
	 * Allows expressions to access properties by name
	 *
	 * @param propertyName Property name (duration, views, likes, etc.)
	 * @return Property value as Object
	 */
	public Object getProperty(String propertyName) {
		switch (propertyName.toLowerCase()) {
			case "duration": return duration;
			case "views": return views;
			case "likes": return likes;
			case "comments": return comments;
			case "category": return category;
			case "resolution": return resolution;
			case "uploadeddate": return uploadedDate;
			default:
				throw new IllegalArgumentException("Unknown property: " + propertyName);
		}
	}

	/**
	 * Get property as integer (for numeric comparisons)
	 */
	public int getIntProperty(String propertyName) {
		Object value = getProperty(propertyName);
		if (value instanceof Integer) {
			return (Integer) value;
		}
		throw new IllegalArgumentException("Property " + propertyName + " is not numeric");
	}

	/**
	 * Get property as string (for string comparisons)
	 */
	public String getStringProperty(String propertyName) {
		Object value = getProperty(propertyName);
		return value.toString();
	}

	@Override
	public String toString() {
		return String.format("\"%s\" (%d min, %d views, %d likes, category: %s, resolution: %s)",
		                     title, duration, views, likes, category, resolution);
	}

	/**
	 * Get detailed information
	 */
	public String getDetails() {
		return String.format(
			"Title: %s\n" +
			"Duration: %d minutes\n" +
			"Views: %d\n" +
			"Likes: %d\n" +
			"Comments: %d\n" +
			"Category: %s\n" +
			"Resolution: %s\n" +
			"Uploaded: %s",
			title, duration, views, likes, comments, category, resolution, uploadedDate
		);
	}
}
