package main.nlp.stemmers;
/*
 * Snowball stemmer
 * 
 * For Standard French
 * 
 * https://snowballstem.org/algorithms/french/stemmer.html
 * 
 */
public class FrenchSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'â', 'à', 'ë', 'é', 'ê', 'è', 'ï', 'î', 'ô',
			'û', 'ù' };
	private static final String[] RV_START_EXCEPTIONS = { "par", "col", "tap" };
	private static final int RV_START_EXCEPTION_IDX = 3;

	private static final String[] step1_suffixes1 = { "ances", "iqUes", "ismes", "ables", "istes", "ance", "iqUe",
			"isme", "able", "iste", "eux" };
	private static final String[] step1_suffixes2 = { "atrices", "ateurs", "ations", "ation", "ateur", "atrice" };
	private static final String[] step1_suffixes2_1 = { "ic", "iqU" };
	private static final String[] step1_suffixes3 = { "issements", "issement" };
	private static final String[][] step1_suffixes4 = { { "amment", "ant" }, { "emment", "ent" } };

	private static final String[] step1_suffixes5 = { "ements", "ement" };
	private static final String[] step1_suffixes6 = { "ments", "ment" };

	private static final String[] step1_suffixes7 = { "logies", "logie" };
	private static final String[] step1_suffixes8 = { "usions", "utions", "usion", "ution" };
	private static final String[] step1_suffixes9 = { "ences", "ence" };
	private static final String[] step1_suffixes10 = { "ités", "ité" };
	private static final String[][] step1_suffixes11 = { { "abil", "abl" }, { "ic", "iqU" }, { "iv", "iv" } };
	private static final String[] step1_suffixes12 = { "ives", "ive", "ifs", "if" };

	private static final String[] step2a_iStartSuffixes = { "issaIent", "issantes", "iraIent", "issante", "issants",
			"issions", "irions", "issais", "issait", "issant", "issent", "issiez", "issons", "irais", "irait", "irent",
			"iriez", "irons", "iront", "isses", "issez", "îmes", "îtes", "irai", "iras", "irez", "isse", "ies", "ira",
			"ît", "ie", "ir", "is", "it", "i" };

	private static final String[] step2b_endings1 = { "eraIent", "erions", "èrent", "erais", "erait", "eriez", "erons",
			"eront", "erai", "eras", "erez", "ées", "era", "iez", "ée", "és", "er", "ez", "é" };

	private static final String[] step2b_endings2 = { "assions", "assent", "assiez", "aIent", "antes", "asses", "ants",
			"asse", "âmes", "âtes", "ante", "ais", "ait", "ant", "ât", "ai", "as", "a", };

	private static final String[] step4_beforeS = { "a", "i", "o", "u", "è", "s" };
	private static final String[] step4_endings2 = { "Ière", "ière", "ier", "Ier" };
	private static final String[] step5_doubleEndings = { "eill", "enn", "onn", "ett", "ell" };

	private int RV;
	private int R1;
	private int R2;
	private boolean isSpecialCase = false;

	private static boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private static boolean isConsonant(char ch) {
		return !isVowel(ch);
	}

	private String markVowelAsConsonant(String input) {
		StringBuilder sb = new StringBuilder(input.length());
		boolean prevCharIsVowel = false;
		for (int i = 0; i < input.length(); i++) {
			char curr = input.charAt(i);
			if ((curr == 'i' || curr == 'u') && (i != 0 && i != input.length() - 1)) {
				if (prevCharIsVowel && isVowel(input.charAt(i + 1))) {
					sb.append(Character.toUpperCase(curr));
					prevCharIsVowel = false;
				} else if (curr == 'u' && input.charAt(i - 1) == 'q') {
					sb.append(Character.toUpperCase(curr));
					prevCharIsVowel = false;
				} else {
					sb.append(curr);
					prevCharIsVowel = true;
				}
			} else if (curr == 'y') {
				if ((prevCharIsVowel && i != 0) || (i != input.length() - 1 && isVowel(input.charAt(i + 1)))) {
					sb.append(Character.toUpperCase(curr));
					prevCharIsVowel = false;
				} else {
					sb.append(curr);
					prevCharIsVowel = true;
				}
			} else if (curr == 'ë') {
				sb.append("He");
				prevCharIsVowel = true;
			} else if (curr == 'ï') {
				sb.append("Hi");
				prevCharIsVowel = true;
			} else {
				sb.append(curr);
				prevCharIsVowel = isVowel(curr);
			}
		}
		return sb.toString();
	}

	private void markRegions(String word) {
		markRV(word);
		markRNumbers(word);
	}

	private void markRV(String word) {
		RV = word.length();
		if (isVowel(word.charAt(0)) && isVowel(word.charAt(1)) || beginsWithRVException(word)) {
			RV = RV_START_EXCEPTION_IDX;
		} else {
			for (int i = 1; i < word.length(); i++) {
				if (isVowel(word.charAt(i))) {
					RV = i + 1;
					break;
				}
			}
		}
	}

	private boolean beginsWithRVException(String word) {
		for (String prefix : RV_START_EXCEPTIONS) {
			if (word.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	private void markRNumbers(String word) {
		R1 = word.length();
		R2 = R1;

		for (int i = 0; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R1 = i + 2;
				break;
			}
		}

		for (int i = R1; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R2 = i + 2;
				break;
			}
		}
	}

	private String step1(String word) {
		String R2String = getRegionSubstring(word, R2);
		String R1String = getRegionSubstring(word, R1);
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : step1_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes2) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					if (word.endsWith(step1_suffixes2_1[0])) {
						word = removeEnding(word, step1_suffixes2_1[0].length());
						if (!R2String.endsWith(step1_suffixes2_1[0])) {
							word += step1_suffixes2_1[1];
						}
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix) && word.length() > suffix.length()
						&& isConsonant(word.charAt(word.length() - suffix.length() - 1))) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String[] suffixInfo : step1_suffixes4) {
			if (word.endsWith(suffixInfo[0])) {
				if (RVString.endsWith(suffixInfo[0])) {
					word = removeEnding(word, suffixInfo[0].length()) + suffixInfo[1];
					isSpecialCase = true;
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes5) {
			if (word.endsWith(suffix)) {
				if (RVString.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = getRegionSubstring(word, R2);
					R1String = getRegionSubstring(word, R1);
					RVString = getRegionSubstring(word, RV);
					if (R2String.endsWith("ativ")) {
						word = removeEnding(word, 4);
					} else if (R2String.endsWith("iv")) {
						word = removeEnding(word, 2);
					} else if (R2String.endsWith("eus")) {
						word = removeEnding(word, 3);
					} else if (R1String.endsWith("eus")) {
						word = removeEnding(word, 3) + "eux";
					} else if (R2String.endsWith("abl") || R2String.endsWith("iqU")) {
						word = removeEnding(word, 3);
					} else if (RVString.endsWith("ièr") || RVString.endsWith("Ièr")) {
						word = removeEnding(word, 3) + "i";
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes6) {
			if (word.endsWith(suffix)) {
				if (RVString.endsWith(suffix) && RVString.length() > suffix.length()
						&& isVowel(RVString.charAt(RVString.length() - suffix.length() - 1))) {
					word = removeEnding(word, suffix.length());
					isSpecialCase = true;
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes7) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "log";
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes8) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "u";
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes9) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "ent";
					return word;
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes10) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());

					for (String[] suffixInfo : step1_suffixes11) {
						if (word.endsWith(suffixInfo[0])) {
							word = removeEnding(word, suffixInfo[0].length());
							if (!R2String.endsWith(suffixInfo[0])) {
								word += suffixInfo[1];
							}
							break;
						}
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes12) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					if (R2String.endsWith("at")) {
						word = removeEnding(word, 2);
						R2String = removeEnding(R2String, 2);
						if (word.endsWith("ic")) {
							word = removeEnding(word, 2);
							if (!R2String.endsWith("ic")) {
								word += "iqU";
							}
						}
					}
				}
				return word;
			}
		}

		if (word.endsWith("eaux")) {
			word = removeEnding(word, 4) + "eau";
		} else if (word.endsWith("aux")) {
			if (R1String.endsWith("aux")) {
				word = removeEnding(word, 3) + "al";
			}
		} else if (word.endsWith("euses")) {
			if (R2String.endsWith("euses")) {
				word = removeEnding(word, 5);
			} else if (R1String.endsWith("euses")) {
				word = removeEnding(word, 5) + "eux";
			}
		} else if (word.endsWith("euse")) {
			if (R2String.endsWith("euse")) {
				word = removeEnding(word, 4);
			} else if (R1String.endsWith("euse")) {
				word = removeEnding(word, 4) + "eux";
			}
		}
		return word;
	}

	private String step2a(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String ISuffix : step2a_iStartSuffixes) {
			if (RVString.endsWith(ISuffix)) {
				if (RVString.length() >= ISuffix.length() + 1) {
					char preceding = RVString.charAt(RVString.length() - ISuffix.length() - 1);
					if (isConsonant(preceding) && preceding != 'H') {
						word = removeEnding(word, ISuffix.length());
					}
				}
				break;
			}
		}
		return word;
	}

	private String step2b(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String ending : step2b_endings1) {
			if (RVString.endsWith(ending)) {
				word = removeEnding(word, ending.length());
				return word;
			}
		}
		for (String ending : step2b_endings2) {
			if (RVString.endsWith(ending)) {
				word = removeEnding(word, ending.length());
				RVString = removeEnding(RVString, ending.length());
				if (RVString.endsWith("e")) {
					word = removeEnding(word, 1);
				}
				return word;
			}
		}

		if (getRegionSubstring(word, R2).endsWith("ions")) {
			word = removeEnding(word, 4);
		}
		return word;
	}

	private String step3(String word) {
		if (word.charAt(word.length() - 1) == 'ç') {
			word = word.substring(0, word.length() - 1) + 'c';
		} else if (word.charAt(word.length() - 1) == 'Y') {
			word = word.substring(0, word.length() - 1) + 'i';
		}
		return word;
	}

	private String step4(String word) {
		if (word.endsWith("s")) {
			word = removeEnding(word, 1);
			for (String precedingEnding : step4_beforeS) {
				if (word.endsWith(precedingEnding)) {
					if (!word.endsWith("Hi")) {
						word += "s";
					}
					break;
				}
			}
		}

		String RVString = getRegionSubstring(word, RV);
		if (getRegionSubstring(word, R2).contains("ion")
				&& ((RVString.endsWith("sion") || RVString.endsWith("tion")))) {
			word = removeEnding(word, 3);
			return word;
		}

		for (String ending : step4_endings2) {
			if (RVString.endsWith(ending)) {
				word = removeEnding(word, ending.length()) + "i";
				return word;
			}
		}

		if (RVString.endsWith("e")) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step5(String word) {
		for (String ending : step5_doubleEndings) {
			if (word.endsWith(ending)) {
				word = removeEnding(word, 1);
				break;
			}
		}
		return word;
	}

	private String step6(String word) {
		int found = -1;
		boolean followedByNonVowel = false;
		for (int i = word.length() - 1; i >= 0; i--) {
			char ch = word.charAt(i);
			if (ch == 'é' || ch == 'è') {
				found = i;
				break;
			} else if (isConsonant(ch)) {
				followedByNonVowel = true;
			} else if (isVowel(ch)) {
				break;
			}
		}

		if (followedByNonVowel && found != -1) {
			word = word.substring(0, found) + "e" + word.substring(found + 1);
		}
		return word;
	}

	private String finalize(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);

			if (ch == 'I' || ch == 'U' || ch == 'Y') {
				sb.append(Character.toLowerCase(ch));
			} else if (ch == 'H') {
				if (i + 1 < word.length()) {
					if (word.charAt(i + 1) == 'e') {
						sb.append('ë');
						i++;
					} else if (word.charAt(i + 1) == 'i') {
						sb.append('ï');
						i++;
					}
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public String stem(String word) {
		word = normalize(word);

		if (word.length() < MIN_LENGTH) {
			return word;
		}
		
		isSpecialCase = false;
		word = markVowelAsConsonant(word);
		markRegions(word);
		String didntChange = word;
		word = step1(word);

		if (isSpecialCase) {
			didntChange = word;
		}

		if (didntChange.equals(word)) {
			word = step2a(word);
			if (didntChange.equals(word)) {
				word = step2b(word);
			}
		}

		if (!didntChange.equals(word)) {
			word = step3(word);
		} else {
			word = step4(word);
		}
		
		word = step5(word);
		word = step6(word);
		return finalize(word);
	}
}
