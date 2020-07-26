package syntatic_analysis;

public class UnaryExpression extends Expression {
	public Operator O;
	public Expression E;
	
	public UnaryExpression(Operator o, Expression e) {
		O = o;
		E = e;
	}
}
