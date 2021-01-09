package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard French
 * 
 * Source: https://snowballstem.org/algorithms/french/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class FrenchSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'â', 'à', 'ë', 'é', 'ê', 'è', 'ï', 'î', 'ô',
			'û', 'ù' };
	private static final String[] RV_START_EXCEPTIONS = { "par", "col", "tap" };
	private static final int RV_START_EXCEPTION_IDX = 3;

	private static final String[] STEP1_SUFFIXES1 = { "ances", "iqUes", "ismes", "ables", "istes", "ance", "iqUe",
			"isme", "able", "iste", "eux" };
	private static final String[] STEP1_SUFFIXES2 = { "atrices", "ateurs", "ations", "ation", "ateur", "atrice" };
	private static final String[] STEP1_SUFFIXES2_1 = { "ic", "iqU" };
	private static final String[] STEP1_SUFFIXES3 = { "issements", "issement" };
	private static final String[][] STEP1_SUFFIXES4 = { { "amment", "ant" }, { "emment", "ent" } };

	private static final String[] STEP1_SUFFIXES5 = { "ements", "ement" };
	private static final String[] STEP1_SUFFIXES6 = { "ments", "ment" };

	private static final String[] STEP1_SUFFIXES7 = { "logies", "logie" };
	private static final String[] STEP1_SUFFIXES8 = { "usions", "utions", "usion", "ution" };
	private static final String[] STEP1_SUFFIXES9 = { "ences", "ence" };
	private static final String[] STEP1_SUFFIXES10 = { "ités", "ité" };
	private static final String[][] STEP1_SUFFIXES11 = { { "abil", "abl" }, { "ic", "iqU" }, { "iv", "iv" } };
	private static final String[] STEP1_SUFFIXES12 = { "ives", "ive", "ifs", "if" };

	private static final String[] STEP2A_I_SUFFIXES = { "issaIent", "issantes", "iraIent", "issante", "issants",
			"issions", "irions", "issais", "issait", "issant", "issent", "issiez", "issons", "irais", "irait", "irent",
			"iriez", "irons", "iront", "isses", "issez", "îmes", "îtes", "irai", "iras", "irez", "isse", "ies", "ira",
			"ît", "ie", "ir", "is", "it", "i" };

	private static final String[] STEP2B_ENDINGS1 = { "eraIent", "erions", "èrent", "erais", "erait", "eriez", "erons",
			"eront", "erai", "eras", "erez", "ées", "era", "iez", "ée", "és", "er", "ez", "é" };

	private static final String[] STEP2B_ENDINGS2 = { "assions", "assent", "assiez", "aIent", "antes", "asses", "ants",
			"asse", "âmes", "âtes", "ante", "ais", "ait", "ant", "ât", "ai", "as", "a", };

	private static final String[] STEP4_BEFORE_S = { "a", "i", "o", "u", "è", "s" };
	private static final String[] STEP4_ENDINGS1 = { "Ière", "ière", "ier", "Ier" };
	private static final String[] STEP5_SUFFIXES = { "eill", "enn", "onn", "ett", "ell" };

	private int RV;
	private int R1;
	private int R2;
	private boolean isSpecialCase;

	@Override
	public String stem(String word) {
		word = normalize(word);

		if (word.length() < MIN_LENGTH) {
			return word;
		}

		isSpecialCase = false;
		word = markVowelAsConsonant(word);
		markRegions(word);
		String prevWord = word;
		word = step1(word);

		if (isSpecialCase) {
			prevWord = word;
		}

		if (prevWord.equals(word)) {
			word = step2a(word);

			if (prevWord.equals(word)) {
				word = step2b(word);
			}
		}

		if (!prevWord.equals(word)) {
			word = step3(word);
		} else {
			word = step4(word);
		}
		word = step5(word);
		word = step6(word);
		return finalize(word);
	}

	private String markVowelAsConsonant(String input) {
		StringBuilder sb = new StringBuilder(input.length());
		boolean prevCharIsVowel = false;
		for (int i = 0; i < input.length(); i++) {
			char curr = input.charAt(i);
			if ((curr == 'i' || curr == 'u') && (i != 0 && i != input.length() - 1)) {
				if (prevCharIsVowel && isVowel(input.charAt(i + 1), VOWELS)) {
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
				if ((prevCharIsVowel && i != 0) || (i != input.length() - 1 && isVowel(input.charAt(i + 1), VOWELS))) {
					sb.append(Character.toUpperCase(curr));
					prevCharIsVowel = false;
				} else {
					sb.append(curr);
					prevCharIsVowel = true;
				}
			} else if (curr == 'ë') {
				sb.append("He");
				prevCharIsVowel = true;
			} else {
				sb.append(curr);
				prevCharIsVowel = isVowel(curr, VOWELS);
			}
		}
		return sb.toString();
	}

	private void markRegions(String word) {
		RV = markRV(word);
		R1 = calcR1VC(word, VOWELS);
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private int markRV(String word) {
		int rv = word.length();
		if (isVowel(word.charAt(0), VOWELS) && isVowel(word.charAt(1), VOWELS) || beginsWithRVException(word)) {
			rv = RV_START_EXCEPTION_IDX;
		} else {
			for (int i = 1; i < word.length(); i++) {
				if (isVowel(word.charAt(i), VOWELS)) {
					rv = i + 1;
					break;
				}
			}
		}
		return rv;
	}

	private boolean beginsWithRVException(String word) {
		for (String prefix : RV_START_EXCEPTIONS) {
			if (word.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	private String step1(String word) {
		String R2String = getRegionSubstring(word, R2);
		String R1String = getRegionSubstring(word, R1);
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : STEP1_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					if (word.endsWith(STEP1_SUFFIXES2_1[0])) {
						word = removeEnding(word, STEP1_SUFFIXES2_1[0].length());
						if (!R2String.endsWith(STEP1_SUFFIXES2_1[0])) {
							word += STEP1_SUFFIXES2_1[1];
						}
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix) && word.length() > suffix.length()
						&& isConsonant(word.charAt(word.length() - suffix.length() - 1), VOWELS)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String[] suffixInfo : STEP1_SUFFIXES4) {
			if (word.endsWith(suffixInfo[0])) {
				if (RVString.endsWith(suffixInfo[0])) {
					word = removeEnding(word, suffixInfo[0].length()) + suffixInfo[1];
					isSpecialCase = true;
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES5) {
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

		for (String suffix : STEP1_SUFFIXES6) {
			if (word.endsWith(suffix)) {
				if (RVString.endsWith(suffix) && RVString.length() > suffix.length()
						&& isVowel(RVString.charAt(RVString.length() - suffix.length() - 1), VOWELS)) {
					word = removeEnding(word, suffix.length());
					isSpecialCase = true;
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES7) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "log";
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES8) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "u";
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES9) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "ent";
					return word;
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES10) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());

					for (String[] suffixInfo : STEP1_SUFFIXES11) {
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

		for (String suffix : STEP1_SUFFIXES12) {
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
		for (String ISuffix : STEP2A_I_SUFFIXES) {
			if (RVString.endsWith(ISuffix)) {
				if (RVString.length() >= ISuffix.length() + 1) {
					char preceding = RVString.charAt(RVString.length() - ISuffix.length() - 1);
					if (isConsonant(preceding, VOWELS) && preceding != 'H') {
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
		for (String ending : STEP2B_ENDINGS1) {
			if (RVString.endsWith(ending)) {
				word = removeEnding(word, ending.length());
				return word;
			}
		}
		for (String ending : STEP2B_ENDINGS2) {
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
			for (String precedingEnding : STEP4_BEFORE_S) {
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

		for (String ending : STEP4_ENDINGS1) {
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
		for (String ending : STEP5_SUFFIXES) {
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
			} else if (isConsonant(ch, VOWELS)) {
				followedByNonVowel = true;
			} else if (isVowel(ch, VOWELS)) {
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
					}
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public Language getLanguage() {
		return Language.FRENCH;
	}
}
