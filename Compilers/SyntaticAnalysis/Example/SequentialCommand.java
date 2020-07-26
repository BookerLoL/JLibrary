package syntatic_analysis;

public class SequentialCommand extends Command {
	public Command C1, C2;
	
	public SequentialCommand(Command c1, Command c2) {
		C1 = c1;
		C2 = c2;
	}
}
