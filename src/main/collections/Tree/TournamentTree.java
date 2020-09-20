package main.collections.Tree;
import java.util.*;

/*
 * Tournament Trees are perfect binary trees, so you need 2n -1 nodes.
 * If you lack a power of 2 teams, then dummy teams are created. 
 * 
 * Must set up the tournament first then simulate the tournament rounds
 * 
 * 
 * Can simulate either winner tree or loser tree
 */
public class TournamentTree<T extends Comparable<T>> {
	protected class Player {
		int teamIndex;
		T data;

		public Player(T data, int teamIndex) {
			this.data = data;
			this.teamIndex = teamIndex;
		}

		public int getTeamIndex() {
			return teamIndex;
		}

		public T getData() {
			return data;
		}

		public String toString() {
			return "Team:" + teamIndex + " " + data;
		}
	}

	private static final int FIRST = 0;
	private Player[] players;
	private List<List<T>> teams;
	private Comparator<T> comp;
	

	public TournamentTree() {
		this(Comparator.naturalOrder());
	}

	public TournamentTree(Comparator<T> comp) {
		this.comp = comp;
	}

	//The power of 2 that can hold all the players
	private static int computeHighestPowerOf2(int numberOfPlayers) {
		int b = 1;
		while (b < numberOfPlayers) {
			b = b << 1;
		}
		return b;
	}

	private static int computePlayersSize(int numberOfPlayers) {
		return 2 * computeHighestPowerOf2(numberOfPlayers) - 1;
	}

	@SuppressWarnings("unchecked")
	public void setUpTournament(List<List<T>> orderedTeams) {
		teams = orderedTeams;
		int numOfPlayers = computePlayersSize(orderedTeams.size());
		players = new TournamentTree.Player[numOfPlayers];

		int playerPos = getStartOfPlayers();
		while (playerPos < players.length) {
			players[playerPos] = getNextPlayer(getTeamIndex(playerPos));
			playerPos++;
		}
	}

	private int getStartOfPlayers() {
		return players.length / 2;
	}

	private int getTeamIndex(int playerIndex) {
		return playerIndex - getStartOfPlayers();
	}

	public boolean isOver() {
		for (List<T> team : teams) {
			if (!team.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private Player determineWinner(Player left, Player right) {
		if (left.getData() == null) {
			return right;
		} else if (right.getData() == null) {
			return left;
		}

		if (comp.compare(left.getData(), right.getData()) < 0) {
			return left;
		} else {
			return right;
		}
	}

	public T getNextWinner() {
		if (isOver()) {
			return null;
		}

		T winner = playTournament();
		return winner;
	}

	private Player getNextPlayer(int teamIdx) {
		if (teamIdx >= teams.size()) {
			return new Player(null, teamIdx);
		}

		List<T> team = teams.get(teamIdx);
		if (team.isEmpty()) {
			return new Player(null, teamIdx);
		} else {
			return new Player(team.remove(FIRST), teamIdx);
		}
	}

	private T playTournament() {
		Player winner = null;
		int startPos = getStartOfPlayers();
		int endPos = players.length;
		int i;
		int newWinnerPos;

		while (startPos != endPos) { // Play until all rounds played
			newWinnerPos = startPos / 2;
			i = startPos;
			while (i < endPos) { // Play Round
				players[newWinnerPos] = determineWinner(players[i], players[i + 1]);
				newWinnerPos++;
				i += 2;
			}
			endPos = startPos - 1;
			startPos /= 2;
		}

		winner = players[FIRST];
		players[getStartOfPlayers() + winner.getTeamIndex()] = getNextPlayer(winner.getTeamIndex());
		clearRounds();
		return winner.getData();
	}

	/*
	 * Don't need to clear rounds, but decided if you wanted to see how the matches
	 * played out, if you can see
	 *
	 */
	private void clearRounds() {
		for (int i = FIRST; i < getStartOfPlayers(); i++) {
			players[i] = null;
		}
	}

	public List<List<T>> getTeams() {
		return teams;
	}

	public String toString() {
		return Arrays.toString(players);
	}

	public static void main(String[] args) {
		TournamentTree<Integer> tournament = new TournamentTree<>();
		java.util.List<Integer> list1 = new java.util.ArrayList<>(Arrays.asList(19, 20));
		java.util.List<Integer> list2 = new java.util.ArrayList<>(Arrays.asList(25, 30));
		java.util.List<Integer> list3 = new java.util.ArrayList<>(Arrays.asList(20, 26));
		java.util.List<Integer> list4 = new java.util.ArrayList<>(Arrays.asList(50, 62));
		java.util.List<Integer> list5 = new java.util.ArrayList<>(Arrays.asList(40, 50));
		java.util.List<Integer> list6 = new java.util.ArrayList<>(Arrays.asList(42, 43));
		java.util.List<Integer> list7 = new java.util.ArrayList<>(Arrays.asList(21, 36));
		java.util.List<Integer> list8 = new java.util.ArrayList<>(Arrays.asList(31, 38));
		java.util.List<List<Integer>> teams = Arrays.asList(list1, list2, list3, list4, list5, list6, list7, list8);

		tournament.setUpTournament(teams);
		while (!tournament.isOver()) {
			System.out.println(tournament.getNextWinner());
			System.out.println(tournament); // Look at results, need to comment out "clearRounds"
		}
	}
}
