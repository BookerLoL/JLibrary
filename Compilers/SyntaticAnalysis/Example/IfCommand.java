package syntatic_analysis;

public class IfCommand extends Command {
	public Expression E;
	public Command C1, C2;
	
	public IfCommand(Expression e, Command c1, Command c2) {
		E = e;
		C1 = c1;
		C2 = c2;
	}
}
