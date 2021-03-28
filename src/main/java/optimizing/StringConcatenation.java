package main.optimizing;

import java.util.Arrays;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by using properly concatenating strings.
 * 
 * <p>
 * Concatenating strings need different approaches to provide optimal code. 
 * Concatenating through a loop should use a StringBuffer / StringBuilder.
 * Concatenating with one liners should use the normal string concatenate operator. 
 * 
 * @author Ethan
 * @version 1.0
 */
public class StringConcatenation {
	public static String fastOneLiner(String prefix, String base) {
		double suffix = 3.14;
		return prefix + base + suffix;
	}

	public static String slowOneLiner(String prefix, String base) {
		double suffix = 3.14;
		return new StringBuilder().append(prefix).append(base).append(suffix).toString();
	}

	public static String fastLoopBuildString(String... strings) {
		int totalLength = Arrays.asList(strings).stream().mapToInt(string -> string.length()).sum();
		StringBuilder sb = new StringBuilder(totalLength);

		for (String string : strings) {
			sb.append(string);
		}

		return sb.toString();
	}

	public static String slowLoopBuildString(String... strings) {
		String result = "";
		for (String string : strings) {
			result += string;
		}
		return result;
	}

	public static void main(String[] args) {
		String s1 = "Hello World";
		String s2 = "String concatenation performance";
		String s3 = "Hopefully this will be fast";
		String s4 = "This is a test to see that this will perform faster than a loop concatenation";
		System.out.println(fastOneLiner(s1, s2));
		System.out.println(slowOneLiner(s1, s2));
		System.out.println(fastLoopBuildString(s1, s2, s3, s4));
		System.out.println(slowLoopBuildString(s1, s2, s3, s4));
	}
}
