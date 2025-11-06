/**
 * AndExpression - Non-terminal expression for AND operator
 *
 * Evaluates logical AND of two sub-expressions:
 * - expr1 AND expr2
 * - Returns true only if BOTH expressions are true
 *
 * Example: "duration > 10 AND views > 1000"
 *   AndExpression
 *     ├─ GreaterThanExpression(duration, 10)
 *     └─ GreaterThanExpression(views, 1000)
 *
 * This is a COMPOSITE node in the expression tree (non-terminal).
 * Uses short-circuit evaluation: if left is false, right is not evaluated.
 */
public class AndExpression implements Expression {

	private Expression left;
	private Expression right;

	/**
	 * Create AND expression
	 *
	 * @param left Left sub-expression
	 * @param right Right sub-expression
	 */
	public AndExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean interpret(Video context) {
		// Short-circuit evaluation: if left is false, don't evaluate right
		return left.interpret(context) && right.interpret(context);
	}

	@Override
	public String toString() {
		return "(" + left.toString() + " AND " + right.toString() + ")";
	}
}
