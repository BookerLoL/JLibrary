package main.designpattern.abstractfactory;

public class WildDog implements Dog {
	@Override
	public void speak() {
		System.out.println("wild woof woof");
	}

	@Override
	public void setPreferredAction() {
		System.out.println("dog is running freely");
	}
}
