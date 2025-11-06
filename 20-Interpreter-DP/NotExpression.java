/**
 * NotExpression - Non-terminal expression for NOT operator
 *
 * Evaluates logical NOT (negation) of sub-expression:
 * - NOT expr
 * - Returns opposite of expression result
 *
 * Example: "NOT (views < 100)"
 *   NotExpression
 *     └─ LessThanExpression(views, 100)
 *
 * If expression is true, NOT returns false.
 * If expression is false, NOT returns true.
 */
public class NotExpression implements Expression {

	private Expression expression;

	/**
	 * Create NOT expression
	 *
	 * @param expression Sub-expression to negate
	 */
	public NotExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public boolean interpret(Video context) {
		return !expression.interpret(context);
	}

	@Override
	public String toString() {
		return "NOT " + expression.toString();
	}
}
