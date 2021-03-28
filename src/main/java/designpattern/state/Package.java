package main.designpattern.state;

public abstract class Package {
	private State<Package> state;
	String id;
	
	public Package(String id) {
		this.id = id;
		state = new OrderedState();
	}
	
	public void setState(State<Package> newState) {
		state = newState;
	}
}
