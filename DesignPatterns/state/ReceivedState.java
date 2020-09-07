package state;

public class ReceivedState implements State<Package> {

	@Override
	public void next(Package pkg) {
		System.out.println("Package has been received");
	}

	@Override
	public void prev(Package pkg) {
		pkg.setState(new DeliveredState());
	}

	@Override
	public void printStatus() {
		System.out.println("Package has been received");
	}
}
