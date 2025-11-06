/**
 * Expression - Abstract Expression interface for Interpreter pattern
 *
 * Represents a node in the Abstract Syntax Tree (AST) for video search queries.
 * Each expression can interpret (evaluate) itself against a Video context.
 *
 * Grammar supported:
 *   Terminal: duration > 10, views > 1000, category = 'tutorial'
 *   Non-terminal: expr AND expr, expr OR expr, NOT expr, (expr)
 *
 * Example AST for "duration > 10 AND views > 1000":
 *   AndExpression
 *     ├─ GreaterThanExpression(duration, 10)
 *     └─ Greater ThanExpression(views, 1000)
 *
 * Key Concept: Grammar rules become classes in Interpreter pattern
 */
public interface Expression {

	/**
	 * Interpret (evaluate) this expression against a video context
	 *
	 * @param context Video object to evaluate against
	 * @return true if video matches this expression, false otherwise
	 */
	boolean interpret(Video context);

	/**
	 * Get string representation of this expression
	 * Useful for debugging and displaying query structure
	 *
	 * @return Human-readable expression string
	 */
	String toString();
}
