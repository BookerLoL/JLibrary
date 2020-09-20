package main.designpattern.abstractfactory;

public class PetDog implements Dog {
	@Override
	public void speak() {
		System.out.println("pet dog woof woof");
	}

	@Override
	public void setPreferredAction() {
		System.out.println("dog is sheltered");
	}
}
