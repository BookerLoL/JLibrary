package main.designpattern.command;
import java.util.Scanner;

//Source: https://gameprogrammingpatterns.com/command.html
//Can be useful for implementing Undo/Redo functions
public class CommandTester {
	public static void main(String[] args) {
		GameActor player = new GameActor();
		Scanner scanner = new Scanner(System.in);
		InputHandler inputHandler = new InputHandler();
		String input = "";
		System.out.println("q to quit");
		System.out.println("type either: a b x y");
		while (!input.equals("q")) {
			input = scanner.next();
			Command command = inputHandler.handleInput(input);
			if (command != null) {
				command.execute(player);
			}
		}
		scanner.close();
	}
}
