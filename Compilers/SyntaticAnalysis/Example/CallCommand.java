package syntatic_analysis;

public class CallCommand extends Command {
	public Identifier I;
	public Expression E;
	
	public CallCommand(Identifier i, Expression e) {
		I = i;
		E = e;
	}
}
