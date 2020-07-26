package syntatic_analysis;

public class LetCommand extends Command{
	public Declaration D;
	public Command C;
	
	public LetCommand(Declaration d, Command c) {
		D = d;
		C = c;
	}
}
