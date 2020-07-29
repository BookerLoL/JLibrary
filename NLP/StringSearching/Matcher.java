package string_searching;

import java.util.List;

public abstract class Matcher {
	public abstract List<Integer> matches(String text, String pattern);
	public abstract boolean contains(String text, String pattern);

	public static void main(String[] args) {
		Bitap abs = new Bitap();
		System.out.println(abs.matches("ababbaabaaab", "abaa"));
		System.out.println(abs.matches("aabaacaadaabaaba", "aaba"));
	}
}
