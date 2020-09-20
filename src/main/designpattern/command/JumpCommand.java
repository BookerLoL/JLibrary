package main.designpattern.command;

public class JumpCommand implements Command {
	@Override
	public void execute(GameActor actor) {
		actor.jump();
	}
}
