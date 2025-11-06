/**
 * LessThanExpression - Terminal expression for "<" operator
 *
 * Evaluates numeric comparisons like:
 * - duration < 5
 * - views < 100
 * - likes < 50
 */
public class LessThanExpression implements Expression {

	private String property;
	private int value;

	public LessThanExpression(String property, int value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public boolean interpret(Video context) {
		int actualValue = context.getIntProperty(property);
		return actualValue < value;
	}

	@Override
	public String toString() {
		return property + " < " + value;
	}
}
