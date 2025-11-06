/**
 * GreaterThanExpression - Terminal expression for ">" operator
 *
 * Evaluates numeric comparisons like:
 * - duration > 10
 * - views > 1000
 * - likes > 100
 *
 * This is a LEAF node in the expression tree (terminal expression).
 */
public class GreaterThanExpression implements Expression {

	private String property;  // Property name (duration, views, etc.)
	private int value;        // Value to compare against

	/**
	 * Create greater-than expression
	 *
	 * @param property Property name (duration, views, likes, comments)
	 * @param value Value to compare against
	 */
	public GreaterThanExpression(String property, int value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public boolean interpret(Video context) {
		int actualValue = context.getIntProperty(property);
		return actualValue > value;
	}

	@Override
	public String toString() {
		return property + " > " + value;
	}
}
