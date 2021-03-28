package main.designpattern.command;

public class CrouchCommand implements Command {
	@Override
	public void execute(GameActor actor) {
		actor.crouch();
	}
}