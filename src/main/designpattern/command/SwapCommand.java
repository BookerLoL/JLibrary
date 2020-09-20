package main.designpattern.command;

public class SwapCommand implements Command {
	@Override
	public void execute(GameActor actor) {
		actor.swap();
	}
}