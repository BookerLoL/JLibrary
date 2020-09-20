package main.designpattern.command;

public class InputHandler {
	private Command buttonX;
	private Command buttonY;
	private Command buttonA;
	private Command buttonB;

	public InputHandler() {
		buttonX = new JumpCommand();
		buttonY = new FireCommand();
		buttonA = new SwapCommand();
		buttonB = new CrouchCommand();
	}

	public Command handleInput(String input) {
		if (input.equals("x")) {
			return buttonX;
		} else if (input.equals("y")) {
			return buttonY;
		} else if (input.equals("a")) {
			return buttonA;
		} else if (input.equals("b")) {
			return buttonB;
		}
		return null;
	}
}