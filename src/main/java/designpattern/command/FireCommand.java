package main.designpattern.command;

public class FireCommand implements Command {
	@Override
	public void execute(GameActor actor) {
		actor.fire();
	}
}