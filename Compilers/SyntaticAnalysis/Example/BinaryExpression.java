package syntatic_analysis;

public class BinaryExpression extends Expression {
	public Operator O;
	public Expression E1, E2;
	
	public BinaryExpression(Expression e1, Expression e2, Operator o) {
		E1 = e1;
		E2 = e2;
		O = o;
	}
}
