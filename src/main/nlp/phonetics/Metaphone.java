package main.nlp.phonetics;

/*
 * Metaphone
 * 
 * For Standard English Names 
 * 
 * https://medium.com/@ievgenii.shulitskyi/phonetic-matching-algorithms-50165e684526
 * 
 * This implementation is slow due to so many passes over the string 
 * but I made the code rather simple and easy to follow instead of massive complicated one loopers
 *  
 *  
 * Note: Needs more testing but can't find large amount of test data
 * 
 * Need to modify code to not rely on toUpperCase changing every lowercase letters enclosed by ('', "") to uppercase
 */

/**
 * 
 * @author Ethan Booker
 *
 */
public class Metaphone extends Phonetizer {
	public static final int DEFAULT_MAX_LENGTH = 4;
	public static final int NO_LIMIT = Integer.MAX_VALUE;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u' };

	private int maxLength;

	public Metaphone() {
		this(DEFAULT_MAX_LENGTH);
	}

	public Metaphone(int maxLength) {
		maxLength = maxLength <= 0 ? DEFAULT_MAX_LENGTH : maxLength;
		this.maxLength = maxLength;
	}

	@Override
	public String encode(String name) {
		name = name.trim().toLowerCase();
		name = dropDuplicateAdjacents(name);
		name = removePrefix(name);
		name = removeBAfterM(name);
		name = transform1(name);
		name = transform2(name);
		name = transform3(name);
		name = removeH(name);
		name = transform4(name);
		name = transform5(name);
		name = transform6(name);
		name = removeYNotBeforeVowel(name);
		name = removeVowelsAfterFirstLetter(name);
		return limitLength(name, maxLength).toUpperCase();
	}

	private String dropDuplicateAdjacents(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		char prev = '\0';
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (curr != prev || curr == 'c') {
				sb.append(curr);
			}
			prev = curr;
		}
		return sb.toString();
	}

	private String removePrefix(String name) {
		if (name.startsWith("kn") || name.startsWith("gn") || name.startsWith("pn") || name.startsWith("ae")
				|| name.startsWith("wr")) {
			return name.substring(1); // drop first letter
		}
		return name;
	}

	/*
	 * delete b if after m
	 */
	private String removeBAfterM(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (ch == 'm') {
				if (name.startsWith("b", 1)) {
					i++;
				}
				sb.append(ch);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/*
	 * cia -> xia sch -> skh ch -> xh ci -> si ce -> se cy -> sy ck -> k c -> k
	 */
	private String transform1(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			int oneAhead = i + 1;
			if (ch == 'c') {
				if (name.startsWith("ia", oneAhead) || name.startsWith("h", oneAhead)) {
					sb.append('x');
				} else if (name.startsWith("i", oneAhead) || name.startsWith("e", oneAhead)
						|| name.startsWith("y", oneAhead)) {
					sb.append('s');
				} else {
					sb.append('k');
					if (name.startsWith("k", oneAhead)) {
						i++; // skip
					}
				}
			} else if (ch == 's' && name.startsWith("ch", oneAhead)) {
				i += 2;
				sb.append("skh");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/*
	 * dge -> jge dgy -> jgy dgi -> dgy d -> t
	 */
	private String transform2(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			int oneAhead = i + 1;
			if (ch == 'd') {
				if (name.startsWith("ge", oneAhead) || name.startsWith("gy")) {
					sb.append("jg");
					i++; // skip g
				} else if (name.startsWith("gi", oneAhead)) {
					sb.append("dgy");
					i += 2;
				} else {
					sb.append('t');
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/*
	 * GH -> H if not ending and not followed by vowel
	 * 
	 * If ending gn -> n gned -> ned
	 * 
	 * 
	 * 
	 * gi -> ji ge -> je gy -> jy g -> k
	 */
	private String transform3(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			int oneAhead = i + 1;
			if (ch == 'g') {
				// check if at end
				if (i == name.length() - 2 && name.startsWith("n", oneAhead)) {
					sb.append('n');
					break;
				} else if (i == name.length() - 4 == name.startsWith("ned", oneAhead)) {
					sb.append("ned");
					break;
				} else if (name.startsWith("h", oneAhead) && i != name.length() - 2 && i + 2 < name.length()
						&& !isVowel(name.charAt(i + 2), VOWELS)) {
					sb.append('h');
					i++;
				} else if (name.startsWith("i", oneAhead) || name.startsWith("e", oneAhead)
						|| name.startsWith("y", oneAhead)) {
					sb.append('j');
				} else {
					sb.append('k');
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	// remove h if after vowel but not before vowel
	private String removeH(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (ch == 'h' && i > 0) {
				boolean isVowelPrev = isVowel(name.charAt(i - 1), VOWELS);
				if (isVowelPrev && (i == name.length() - 1 || !isVowel(name.charAt(i + 1), VOWELS))) {
					continue;
				} else {
					sb.append('h');
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/*
	 * q -> k v, ph -> f z -> s
	 */
	private String transform4(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			int oneAhead = i + 1;
			if (curr == 'q') {
				sb.append('k');
			} else if (curr == 'v') {
				sb.append('f');
			} else if (curr == 'p' && name.startsWith("h", oneAhead)) {
				sb.append('f');
				i++;
			} else if (curr == 'z') {
				sb.append('s');
			} else {
				sb.append(curr);
			}
		}
		return sb.toString();
	}

	/*
	 * sh -> xh sio, tio -> xio sia, tia -> xia th -> 0 tch -> ch
	 */
	private String transform5(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			int oneAhead = i + 1;
			if (curr == 's') {
				if (name.startsWith("h", oneAhead) || name.startsWith("io", oneAhead)
						|| name.startsWith("ia", oneAhead)) {
					sb.append('x');
				} else {
					sb.append('s');
				}
			} else if (curr == 't') {
				if (name.startsWith("h", oneAhead)) {
					sb.append('0');
					i++;
				} else if (name.startsWith("ch", oneAhead)) {
					sb.append("ch");
					i += 2;
				} else if (name.startsWith("io", oneAhead) || name.startsWith("ia", oneAhead)) {
					sb.append('x');
				} else {
					sb.append('t');
				}
			} else {
				sb.append(curr);
			}
		}
		return sb.toString();
	}

	/*
	 * y delete if not before vowel
	 * 
	 * x if at beginning -> s else -> ks
	 * 
	 * tch -> ch
	 * 
	 * if at beginning, wh -> w w delete if not followed by vowel
	 * 
	 */
	private String transform6(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (curr == 'x') {
				if (i == 0) {
					sb.append('s');
				} else {
					sb.append("ks");
				}
			} else if (curr == 'w') {
				if (i == 0 && name.startsWith("h", i + 1)) {
					i++;
				}

				if (i + 1 < name.length() && isVowel(name.charAt(i + 1), VOWELS)) {
					sb.append(curr);
				}
			} else {
				sb.append(curr);
			}
		}
		return sb.toString();
	}

	private String removeYNotBeforeVowel(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (curr == 'y') {
				if (i + 1 < name.length() && !isVowel(name.charAt(i + 1), VOWELS)) {
					sb.append(curr);
				}
			} else {
				sb.append(curr);
			}
		}
		return sb.toString();
	}

	// remove vowels after first letter
	private String removeVowelsAfterFirstLetter(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		sb.append(name.charAt(0));
		for (int i = 1; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (!isVowel(curr, VOWELS)) {
				sb.append(curr);
			}
		}
		return sb.toString();
	}
}
