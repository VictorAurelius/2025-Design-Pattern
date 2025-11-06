/**
 * OrExpression - Non-terminal expression for OR operator
 *
 * Evaluates logical OR of two sub-expressions:
 * - expr1 OR expr2
 * - Returns true if AT LEAST ONE expression is true
 *
 * Example: "category = 'tutorial' OR category = 'review'"
 *   OrExpression
 *     ├─ EqualsExpression(category, 'tutorial')
 *     └─ EqualsExpression(category, 'review')
 *
 * Uses short-circuit evaluation: if left is true, right is not evaluated.
 */
public class OrExpression implements Expression {

	private Expression left;
	private Expression right;

	/**
	 * Create OR expression
	 *
	 * @param left Left sub-expression
	 * @param right Right sub-expression
	 */
	public OrExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean interpret(Video context) {
		// Short-circuit evaluation: if left is true, don't evaluate right
		return left.interpret(context) || right.interpret(context);
	}

	@Override
	public String toString() {
		return "(" + left.toString() + " OR " + right.toString() + ")";
	}
}
