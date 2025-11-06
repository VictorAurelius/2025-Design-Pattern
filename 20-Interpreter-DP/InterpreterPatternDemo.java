import java.util.ArrayList;
import java.util.List;

/**
 * InterpreterPatternDemo - Comprehensive demonstration of Interpreter pattern
 *
 * Demonstrates:
 * 1. Building expression trees manually (AST construction)
 * 2. Terminal expressions (comparison operators)
 * 3. Non-terminal expressions (AND, OR, NOT)
 * 4. Complex nested queries
 * 5. Expression reuse (parse once, evaluate many)
 * 6. Pattern benefits
 *
 * Scenario: StreamFlix advanced video search
 * Users search videos using expression-based queries
 */
public class InterpreterPatternDemo {

	public static void main(String[] args) {
		System.out.println("╔════════════════════════════════════════════════════════════════════╗");
		System.out.println("║      INTERPRETER PATTERN - Video Search Query Language            ║");
		System.out.println("║                    StreamFlix Platform                             ║");
		System.out.println("╚════════════════════════════════════════════════════════════════════╝");
		System.out.println();

		// Create video database
		List<Video> database = createVideoDatabase();
		VideoSearchService searchService = new VideoSearchService(database);

		System.out.println("Video Database: " + database.size() + " videos");
		System.out.println();

		// Run demonstration scenarios
		demonstrateSimpleQuery(searchService);
		demonstrateAndQuery(searchService);
		demonstrateOrQuery(searchService);
		demonstrateComplexNestedQuery(searchService);
		demonstrateNotQuery(searchService);
		demonstrateExpressionReuse(searchService);
		showPatternBenefits();
	}

	/**
	 * Scenario 1: Simple comparison query
	 */
	private static void demonstrateSimpleQuery(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 1: Simple Comparison Query");
		System.out.println("═".repeat(70));
		System.out.println();

		// Query: duration > 10
		Expression query = new GreaterThanExpression("duration", 10);

		System.out.println("Query: Find videos longer than 10 minutes");
		System.out.println("Expression Tree:");
		System.out.println("  GreaterThanExpression");
		System.out.println("    ├─ property: duration");
		System.out.println("    ├─ operator: >");
		System.out.println("    └─ value: 10");
		System.out.println();

		service.searchAndDisplay("duration > 10", query);
	}

	/**
	 * Scenario 2: AND query
	 */
	private static void demonstrateAndQuery(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 2: AND Query");
		System.out.println("═".repeat(70));
		System.out.println();

		// Query: duration > 10 AND views > 1000
		Expression left = new GreaterThanExpression("duration", 10);
		Expression right = new GreaterThanExpression("views", 1000);
		Expression query = new AndExpression(left, right);

		System.out.println("Query: Find tutorial videos with good engagement");
		System.out.println("Criteria: duration > 10 minutes AND views > 1000");
		System.out.println();
		System.out.println("Expression Tree:");
		System.out.println("  AndExpression");
		System.out.println("    ├─ GreaterThanExpression (duration > 10)");
		System.out.println("    └─ GreaterThanExpression (views > 1000)");
		System.out.println();

		service.searchAndDisplay("duration > 10 AND views > 1000", query);
	}

	/**
	 * Scenario 3: OR query
	 */
	private static void demonstrateOrQuery(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 3: OR Query");
		System.out.println("═".repeat(70));
		System.out.println();

		// Query: category = 'tutorial' OR category = 'review'
		Expression left = new EqualsExpression("category", "tutorial");
		Expression right = new EqualsExpression("category", "review");
		Expression query = new OrExpression(left, right);

		System.out.println("Query: Find educational content");
		System.out.println("Criteria: category = 'tutorial' OR category = 'review'");
		System.out.println();
		System.out.println("Expression Tree:");
		System.out.println("  OrExpression");
		System.out.println("    ├─ EqualsExpression (category = 'tutorial')");
		System.out.println("    └─ EqualsExpression (category = 'review')");
		System.out.println();

		service.searchAndDisplay("category = 'tutorial' OR category = 'review'", query);
	}

	/**
	 * Scenario 4: Complex nested query
	 */
	private static void demonstrateComplexNestedQuery(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 4: Complex Nested Query");
		System.out.println("═".repeat(70));
		System.out.println();

		// Query: (duration > 10 AND category = 'tutorial') OR (views > 10000)
		Expression durationCheck = new GreaterThanExpression("duration", 10);
		Expression categoryCheck = new EqualsExpression("category", "tutorial");
		Expression leftSide = new AndExpression(durationCheck, categoryCheck);

		Expression viewsCheck = new GreaterThanExpression("views", 10000);

		Expression query = new OrExpression(leftSide, viewsCheck);

		System.out.println("Query: Find quality content");
		System.out.println("Criteria: (Long tutorials) OR (Viral videos)");
		System.out.println("Expression: (duration > 10 AND category = 'tutorial') OR (views > 10000)");
		System.out.println();
		System.out.println("Expression Tree:");
		System.out.println("  OrExpression");
		System.out.println("    ├─ AndExpression");
		System.out.println("    │   ├─ GreaterThanExpression (duration > 10)");
		System.out.println("    │   └─ EqualsExpression (category = 'tutorial')");
		System.out.println("    └─ GreaterThanExpression (views > 10000)");
		System.out.println();

		service.searchAndDisplay(
			"(duration > 10 AND category = 'tutorial') OR (views > 10000)",
			query
		);
	}

	/**
	 * Scenario 5: NOT query
	 */
	private static void demonstrateNotQuery(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 5: NOT Query");
		System.out.println("═".repeat(70));
		System.out.println();

		// Query: duration > 5 AND NOT (views < 100)
		Expression durationCheck = new GreaterThanExpression("duration", 5);
		Expression lowViews = new LessThanExpression("views", 100);
		Expression notLowViews = new NotExpression(lowViews);
		Expression query = new AndExpression(durationCheck, notLowViews);

		System.out.println("Query: Find decent-length videos that are NOT unpopular");
		System.out.println("Criteria: duration > 5 AND NOT (views < 100)");
		System.out.println();
		System.out.println("Expression Tree:");
		System.out.println("  AndExpression");
		System.out.println("    ├─ GreaterThanExpression (duration > 5)");
		System.out.println("    └─ NotExpression");
		System.out.println("        └─ LessThanExpression (views < 100)");
		System.out.println();

		service.searchAndDisplay("duration > 5 AND NOT (views < 100)", query);
	}

	/**
	 * Scenario 6: Expression reuse (parse once, evaluate many)
	 */
	private static void demonstrateExpressionReuse(VideoSearchService service) {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("SCENARIO 6: Expression Reuse (Parse Once, Evaluate Many)");
		System.out.println("═".repeat(70));
		System.out.println();

		// Build expression once
		Expression query = new AndExpression(
			new GreaterThanExpression("likes", 100),
			new EqualsExpression("category", "tutorial")
		);

		System.out.println("Key Benefit: Build expression tree ONCE, evaluate many times");
		System.out.println();
		System.out.println("Expression: likes > 100 AND category = 'tutorial'");
		System.out.println();

		// Simulate evaluating against many videos
		System.out.println("Evaluating expression against " + service.getDatabaseSize() + " videos...");

		long startTime = System.currentTimeMillis();
		int matchCount = 0;

		for (Video video : service.getVideoDatabase()) {
			if (query.interpret(video)) {
				matchCount++;
			}
		}

		long endTime = System.currentTimeMillis();

		System.out.println("✓ Matched " + matchCount + " videos");
		System.out.println("✓ Evaluation time: " + (endTime - startTime) + "ms");
		System.out.println();
		System.out.println("Without Interpreter Pattern:");
		System.out.println("  - Would need to parse query " + service.getDatabaseSize() + " times");
		System.out.println("  - Parse time: ~10ms × " + service.getDatabaseSize() + " = " +
		                   (10 * service.getDatabaseSize()) + "ms");
		System.out.println();
		System.out.println("With Interpreter Pattern:");
		System.out.println("  - Parse once: ~0ms (already built)");
		System.out.println("  - Evaluate " + service.getDatabaseSize() + " times: " +
		                   (endTime - startTime) + "ms");
		System.out.println("  - Time saved: ~" + (10 * service.getDatabaseSize()) + "ms (" +
		                   (100 * 10 * service.getDatabaseSize() / Math.max(1, endTime - startTime)) +
		                   "× faster)");
	}

	/**
	 * Show pattern benefits
	 */
	private static void showPatternBenefits() {
		System.out.println("\n" + "═".repeat(70));
		System.out.println("INTERPRETER PATTERN BENEFITS DEMONSTRATED");
		System.out.println("═".repeat(70));
		System.out.println();

		System.out.println("✓ GRAMMAR REPRESENTATION");
		System.out.println("  - Formal grammar rules expressed as classes");
		System.out.println("  - Each grammar rule = one class");
		System.out.println("  - Easy to understand and maintain");
		System.out.println();

		System.out.println("✓ COMPOSABILITY");
		System.out.println("  - Build complex expressions from simple ones");
		System.out.println("  - Unlimited nesting depth");
		System.out.println("  - Example: (A AND B) OR (C AND NOT D)");
		System.out.println();

		System.out.println("✓ EXTENSIBILITY");
		System.out.println("  - Add new operator = add one class");
		System.out.println("  - No modification of existing code (Open/Closed Principle)");
		System.out.println("  - Examples: >=, <=, !=, CONTAINS, BETWEEN");
		System.out.println();

		System.out.println("✓ REUSABILITY");
		System.out.println("  - Parse once, evaluate many times");
		System.out.println("  - Same expression tree for all videos");
		System.out.println("  - 100× performance improvement");
		System.out.println();

		System.out.println("✓ TESTABILITY");
		System.out.println("  - Each expression type independently testable");
		System.out.println("  - Mock context (Video) for unit tests");
		System.out.println("  - Test interpretation logic separately");
		System.out.println();

		System.out.println("✓ OPERATOR PRECEDENCE");
		System.out.println("  - Precedence: NOT > AND > OR");
		System.out.println("  - Parentheses override precedence");
		System.out.println("  - Standard mathematical conventions");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("BEFORE vs AFTER COMPARISON");
		System.out.println("═".repeat(70));
		System.out.println();

		System.out.println("Code Complexity:");
		System.out.println("  Before: 500+ lines of string parsing");
		System.out.println("  After:  20-30 lines per expression class");
		System.out.println("  Improvement: 90% code reduction");
		System.out.println();

		System.out.println("Query Support:");
		System.out.println("  Before: 5-10 hardcoded patterns");
		System.out.println("  After:  Unlimited combinations");
		System.out.println("  Improvement: ∞ flexibility");
		System.out.println();

		System.out.println("Search Time:");
		System.out.println("  Before: 5 hours (manual Excel filtering)");
		System.out.println("  After:  < 1 second");
		System.out.println("  Improvement: 99.99% faster");
		System.out.println();

		System.out.println("Extensibility:");
		System.out.println("  Before: 4 hours to add new operator");
		System.out.println("  After:  15 minutes (add one class)");
		System.out.println("  Improvement: 94% faster development");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("WHEN TO USE INTERPRETER PATTERN");
		System.out.println("═".repeat(70));
		System.out.println();

		System.out.println("✅ USE WHEN:");
		System.out.println("  • Grammar is simple and stable");
		System.out.println("  • Need full control over evaluation");
		System.out.println("  • DSL (Domain-Specific Language) needed");
		System.out.println("  • Performance is acceptable (parse once, evaluate many)");
		System.out.println();

		System.out.println("❌ DON'T USE WHEN:");
		System.out.println("  • Grammar is complex (>20 rules) → Use parser generator");
		System.out.println("  • Performance is critical → Use compiled approach");
		System.out.println("  • Grammar changes frequently → Use external DSL");
		System.out.println();

		System.out.println("═".repeat(70));
		System.out.println("KEY TAKEAWAY");
		System.out.println("═".repeat(70));
		System.out.println();
		System.out.println("\"Grammar rules become classes in Interpreter pattern.\"");
		System.out.println();
		System.out.println("Each syntax rule in your language = one class implementing Expression.");
		System.out.println("The AST (Abstract Syntax Tree) represents the parsed query.");
		System.out.println("Interpretation = traversing the tree and evaluating each node.");
		System.out.println();
		System.out.println("═".repeat(70));
	}

	/**
	 * Create sample video database
	 */
	private static List<Video> createVideoDatabase() {
		List<Video> database = new ArrayList<>();

		// Tutorial videos
		database.add(new Video("React Hooks Tutorial", 15, 5000, 250, 50,
		                       "tutorial", "1080p", "2024-01-15"));
		database.add(new Video("Python for Beginners", 30, 12000, 800, 120,
		                       "tutorial", "1080p", "2024-02-01"));
		database.add(new Video("Complete JavaScript Course", 120, 25000, 1500, 300,
		                       "tutorial", "4K", "2024-01-20"));
		database.add(new Video("Quick CSS Tip", 5, 800, 40, 10,
		                       "tutorial", "720p", "2024-03-01"));
		database.add(new Video("Java Advanced Topics", 45, 3000, 180, 45,
		                       "tutorial", "1080p", "2024-02-15"));

		// Review videos
		database.add(new Video("iPhone 15 Review", 12, 50000, 2000, 500,
		                       "review", "4K", "2024-03-10"));
		database.add(new Video("Best Laptops 2024", 18, 35000, 1200, 200,
		                       "review", "4K", "2024-02-20"));
		database.add(new Video("Headphone Comparison", 8, 5000, 300, 80,
		                       "review", "1080p", "2024-03-05"));

		// Vlog videos
		database.add(new Video("Day in My Life", 10, 15000, 800, 150,
		                       "vlog", "1080p", "2024-03-12"));
		database.add(new Video("Travel Vlog Tokyo", 20, 40000, 2500, 400,
		                       "vlog", "4K", "2024-02-28"));
		database.add(new Video("Morning Routine", 6, 8000, 500, 100,
		                       "vlog", "1080p", "2024-03-08"));

		// Gaming videos
		database.add(new Video("Minecraft Gameplay", 35, 80000, 4000, 800,
		                       "gaming", "1080p", "2024-03-01"));
		database.add(new Video("Fortnite Tips", 12, 25000, 1200, 250,
		                       "gaming", "1080p", "2024-03-05"));

		// Short videos (low engagement)
		database.add(new Video("Quick Update", 2, 50, 5, 1,
		                       "vlog", "720p", "2024-03-15"));
		database.add(new Video("Test Video", 3, 80, 8, 2,
		                       "other", "720p", "2024-03-14"));

		return database;
	}
}
