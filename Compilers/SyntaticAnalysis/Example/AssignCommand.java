package syntatic_analysis;

public class AssignCommand extends Command {
	public Vname V;
	public Expression E;
	
	public AssignCommand(Vname v, Expression e) {
		V = v;
		E = e;
	}
}
