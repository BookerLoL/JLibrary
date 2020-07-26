package syntatic_analysis;

public class VarDeclaration extends Declaration {
	public Identifier I;
	public TypeDenoter T;
	
	public VarDeclaration(Identifier i, TypeDenoter t) {
		I = i;
		T = t;
	}
}
