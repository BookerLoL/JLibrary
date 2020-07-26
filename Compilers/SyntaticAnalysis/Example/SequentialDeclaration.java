package syntatic_analysis;

public class SequentialDeclaration extends Declaration {
	public Declaration D1, D2;
	
	public SequentialDeclaration(Declaration d1, Declaration d2) {
		D1 = d1;
		D2 = d2;
	}
}
