package state;

public class DeliveredState implements State<Package> {
	@Override
	public void next(Package pkg) {
		pkg.setState(new ReceivedState());
	}

	@Override
	public void prev(Package pkg) {
		pkg.setState(new OrderedState());
	}

	@Override
	public void printStatus() {
		System.out.println("Package is being delivered");
	}

}
