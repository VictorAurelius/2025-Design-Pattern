/**
 * EqualsExpression - Terminal expression for "=" operator
 *
 * Evaluates equality comparisons like:
 * - category = 'tutorial'
 * - resolution = '4K'
 *
 * Supports both numeric and string comparisons.
 * String comparison is case-insensitive.
 */
public class EqualsExpression implements Expression {

	private String property;
	private String value;  // Store as string to handle both types

	/**
	 * Create equals expression
	 *
	 * @param property Property name
	 * @param value Value to compare (can be number or string)
	 */
	public EqualsExpression(String property, String value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public boolean interpret(Video context) {
		Object actualValue = context.getProperty(property);

		// Handle numeric comparison
		if (actualValue instanceof Integer) {
			try {
				int expectedValue = Integer.parseInt(value);
				return actualValue.equals(expectedValue);
			} catch (NumberFormatException e) {
				return false;
			}
		}

		// Handle string comparison (case-insensitive)
		String actualString = actualValue.toString();
		return actualString.equalsIgnoreCase(value);
	}

	@Override
	public String toString() {
		return property + " = '" + value + "'";
	}
}
