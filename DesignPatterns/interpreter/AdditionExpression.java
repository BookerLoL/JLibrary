package interpreter;

public class AdditionExpression extends Expression {
	private Expression left;
	private Expression right;
	
	public AdditionExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public double evaluate() {
		return left.evaluate() + right.evaluate();
	}

}
