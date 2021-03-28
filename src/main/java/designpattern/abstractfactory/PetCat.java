package main.designpattern.abstractfactory;

public class PetCat implements Cat {

	@Override
	public void speak() {
		System.out.println("I'm pet cat, meow");
	}

	@Override
	public void setPreferredAction() {
		System.out.println("I want to sit inside and sleep all day");
	}

}
