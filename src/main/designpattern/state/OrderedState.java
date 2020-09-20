package main.designpattern.state;

public class OrderedState implements State<Package> {

	@Override
	public void next(Package pkg) {
		pkg.setState(new DeliveredState());
	}

	@Override
	public void prev(Package pkg) {
		System.out.println("There is no previous state");
	}

	@Override
	public void printStatus() {
		System.out.println("Package ordered, not delivered");
	}
}
