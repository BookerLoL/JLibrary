package state;

public interface State<T extends Package> {
	void next(T pkg);
	void prev(T pkg);
	void printStatus();
}
