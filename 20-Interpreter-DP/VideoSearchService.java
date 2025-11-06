import java.util.ArrayList;
import java.util.List;

/**
 * VideoSearchService - Client for Interpreter pattern
 *
 * Provides video search functionality using expression-based queries.
 * Demonstrates the Interpreter pattern in action.
 *
 * Key Principle: Parse once, evaluate many times
 * - Build expression tree once
 * - Reuse same tree for all videos
 * - Example: 10,000 videos = 1 expression tree + 10,000 interpretations
 */
public class VideoSearchService {

	private List<Video> videoDatabase;

	/**
	 * Create search service with video database
	 *
	 * @param videoDatabase List of videos to search
	 */
	public VideoSearchService(List<Video> videoDatabase) {
		this.videoDatabase = videoDatabase;
	}

	/**
	 * Search videos using expression
	 *
	 * @param expression Query expression (AST root)
	 * @return List of videos matching the expression
	 */
	public List<Video> search(Expression expression) {
		List<Video> results = new ArrayList<>();

		// Evaluate expression against each video
		for (Video video : videoDatabase) {
			if (expression.interpret(video)) {
				results.add(video);
			}
		}

		return results;
	}

	/**
	 * Search with detailed output
	 *
	 * @param queryName Query description
	 * @param expression Query expression
	 */
	public void searchAndDisplay(String queryName, Expression expression) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("QUERY: " + queryName);
		System.out.println("═".repeat(70));
		System.out.println("Expression: " + expression.toString());
		System.out.println();

		long startTime = System.currentTimeMillis();
		List<Video> results = search(expression);
		long endTime = System.currentTimeMillis();

		System.out.println("Results (" + results.size() + " videos found):");
		if (results.isEmpty()) {
			System.out.println("  (No videos match this query)");
		} else {
			for (int i = 0; i < Math.min(results.size(), 5); i++) {
				System.out.println("  ✓ " + results.get(i));
			}
			if (results.size() > 5) {
				System.out.println("  ... and " + (results.size() - 5) + " more");
			}
		}

		System.out.println("\nExecution time: " + (endTime - startTime) + "ms");
		System.out.println("Searched " + videoDatabase.size() + " videos");
	}

	/**
	 * Get video database
	 */
	public List<Video> getVideoDatabase() {
		return videoDatabase;
	}

	/**
	 * Get database size
	 */
	public int getDatabaseSize() {
		return videoDatabase.size();
	}
}
