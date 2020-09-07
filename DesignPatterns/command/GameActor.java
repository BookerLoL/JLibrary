package command;

public class GameActor implements Crouchable, Fireable, Jumpable, Swappable {
	@Override
	public void swap() {
		System.out.println("Swap called");
	}

	@Override
	public void jump() {
		System.out.println("Jump called");
	}

	@Override
	public void fire() {
		System.out.println("Fire called");
	}

	@Override
	public void crouch() {
		System.out.println("Crouch called");
	}

}
