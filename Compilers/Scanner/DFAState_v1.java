package Scanner;

public class DFAState_v1 extends State<DFAEdge_v1> {
	public static final String PREFIX = "d";

	public DFAState_v1(int id, boolean isFinal) {
		super(PREFIX + id, isFinal);
	}
	
	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}
}
