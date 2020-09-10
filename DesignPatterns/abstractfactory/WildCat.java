package abstractfactory;

public class WildCat implements Cat {
	@Override
	public void speak() {
		System.out.println("I'm wild cat");
	}

	@Override
	public void setPreferredAction() {
		System.out.println("I need to hunt, meow");
	}
}
