package syntatic_analysis;

public class WhileCommand extends Command {
	public Expression E;
	public Command C;
	
	public WhileCommand(Expression e, Command c) {
		E = e;
		C = c;
	}
}
