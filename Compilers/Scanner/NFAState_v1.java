package Scanner;

public class NFAState_v1 extends State<NFAEdge_v1> {
	public static final String PREFIX = "s";
	public static final int ERROR_ID = -1;
	
	public NFAState_v1(int id, boolean isFinal) {
		super(PREFIX + id, isFinal);
	}
	
	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}
}

