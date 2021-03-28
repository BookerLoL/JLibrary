package main.networking.learn;

public class TestingProtocol {
	private enum State {
		WAITING, SENTKNOCKKNOCK, SENTCLUE, ANOTHER;
	}

	private State state = State.WAITING;
	private int currentJoke = 0;

	private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
	private String[] answers = { "Turnip the heat, it's cold in here!", "I didn't know you could yodel!", "Bless you!",
			"Is there an owl in here?", "Is there an echo in here?" };
	private static final int NUMJOKES = 5;

	public String processInput(String theInput) {
		String theOutput = null;

		if (state == State.WAITING) {
			theOutput = "Knock! Knock!";
			state = State.SENTKNOCKKNOCK;
		} else if (state == State.SENTKNOCKKNOCK) {
			if (theInput.equalsIgnoreCase("Who's there?")) {
				theOutput = clues[currentJoke];
				state = State.SENTCLUE;
			} else {
				theOutput = "You're supposed to say \"Who's there?\"! " + "Try again. Knock! Knock!";
			}
		} else if (state == State.SENTCLUE) {
			if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
				theOutput = answers[currentJoke] + " Want another? (y/n)";
				state = State.ANOTHER;
			} else {
				theOutput = "You're supposed to say \"" + clues[currentJoke] + " who?\"" + "! Try again. Knock! Knock!";
				state = State.SENTKNOCKKNOCK;
			}
		} else if (state == State.ANOTHER) {
			if (theInput.equalsIgnoreCase("y")) {
				theOutput = "Knock! Knock!";
				if (currentJoke == (NUMJOKES - 1))
					currentJoke = 0;
				else
					currentJoke++;
				state = State.SENTKNOCKKNOCK;
			} else {
				theOutput = "Bye.";
				state = State.WAITING;
			}
		}
		return theOutput;
	}
}
