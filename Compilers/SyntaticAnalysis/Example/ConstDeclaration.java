package syntatic_analysis;

public class ConstDeclaration extends Declaration {
	public Identifier I;
	public Expression E;
	
	public ConstDeclaration(Identifier i, Expression e) {
		I = i;
		E = e;
	}
}
