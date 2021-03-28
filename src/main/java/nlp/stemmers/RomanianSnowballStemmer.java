package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Romanian
 * 
 * Source: https://snowballstem.org/algorithms/romanian/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class RomanianSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'ă', 'â', 'e', 'i', 'î', 'o', 'u', 'Ã' };

	private static final String[] STEP0_SUFFIXES1 = { "ului", "ul" };
	private static final String STEP0_SUFFIXES2 = "aua";
	private static final String STEP0_SUFFIXES2_REPLACEMENT = "a";
	private static final String[] STEP0_SUFFIXES3 = { "elor", "ele", "ea" };
	private static final String STEP0_SUFFIXES3_REPLACEMENT = "e";
	private static final String[] STEP0_SUFFIXES4 = { "iilor", "ilor", "iile", "iei", "iua", "ii" };
	private static final String STEP0_SUFFIXES4_REPLACEMENT = "i";
	private static final String STEP0_SUFFIXES5 = "ile";
	private static final String STEP0_SUFFIXES5_preceding = "ab";
	private static final String STEP0_SUFFIXES5_REPLACEMENT = "i";
	private static final String STEP0_SUFFIXES6 = "atei";
	private static final String STEP0_SUFFIXES6_REPLACEMENT = "at";
	private static final String[] STEP0_SUFFIXES7 = { "aţia", "aţie" };
	private static final String STEP0_SUFFIXES7_REPLACEMENT = "aţi";
	private static final String[] STEP1_SUFFIXES1 = { "abilitate", "abilitati", "abilităţi", "abilităi" };
	private static final String STEP1_SUFFIXES1_REPLACEMENT = "abil";
	private static final String[] STEP1_SUFFIXES2 = { "ibilitate" };
	private static final String STEP1_SUFFIXES2_REPLACEMENT = "ibil";
	private static final String[] STEP1_SUFFIXES3 = { "ivitate", "ivitati", "ivităţi", "ivităi" };
	private static final String STEP1_SUFFIXES3_REPLACEMENT = "iv";
	private static final String[] STEP1_SUFFIXES4 = { "icitate", "icitati", "icităţi", "icatori", "icităi", "icator",
			"iciva", "icive", "icivi", "icivă", "icala", "icale", "icali", "icală", "iciv", "ical" };
	private static final String STEP1_SUFFIXES4_REPLACEMENT = "ic";
	private static final String[] STEP1_SUFFIXES5 = { "aţiune", "atoare", "ătoare", "ativa", "ative", "ativi", "ativă",
			"atori", "ători", "ativ", "ator", "ător" };
	private static final String STEP1_SUFFIXES5_REPLACEMENT = "at";
	private static final String[] STEP1_SUFFIXES6 = { "iţiune", "itoare", "itiva", "itive", "itivi", "itivă", "itori",
			"itiv", "itor" };
	private static final String STEP1_SUFFIXES6_REPLACEMENT = "it";
	private static final String[] STEP2_SUFFIXES1 = { "abila", "abile", "abili", "abilă", "ibila", "ibile", "ibili",
			"ibilă", "atori", "itate", "itati", "ităţi", "abil", "ibil", "oasa", "oasă", "oase", "anta", "ante", "anti",
			"antă", "ator", "ităi", "ata", "ată", "ati", "ate", "uta", "ută", "uti", "ute", "ita", "ită", "iti", "ite",
			"ica", "ice", "ici", "ică", "osi", "oşi", "ant", "iva", "ive", "ivi", "ivă", "at", "ut", "it", "ic", "os",
			"iv" };
	private static final String[] STEP2_SUFFIXES2 = { "iune", "iuni" };
	private static final String[] STEP2_SUFFIXES3 = { "isme", "ista", "iste", "isti", "istă", "işti", "ism", "ist" };
	private static final String STEP2_SUFFIXES3_REPLACEMENT = "ist";
	private static final String[] STEP3_SUFFIXES1 = { "aserăţi", "iserăţi", "âserăţi", "userăţi", "aserăm", "iserăm",
			"âserăm", "userăm", "ească", "arăţi", "urăţi", "irăţi", "ârăţi", "aseşi", "aseră", "iseşi", "iseră",
			"âseşi", "âseră", "useşi", "useră", "indu", "ându", "ează", "eşti", "eşte", "ăşti", "ăşte", "eaţi", "iaţi",
			"arăm", "urăm", "irăm", "ârăm", "asem", "isem", "âsem", "usem", "are", "ere", "ire", "âre", "ind", "ând",
			"eze", "ezi", "esc", "ăsc", "eam", "eai", "eau", "iam", "iai", "iau", "aşi", "ară", "uşi", "ură", "işi",
			"iră", "âşi", "âră", "ase", "ise", "âse", "use", "ez", "am", "ai", "au", "ea", "ia", "ui", "âi" };
	private static final String[] STEP3_SUFFIXES2 = { "seserăţi", "seserăm", "serăţi", "seseşi", "seseră", "serăm",
			"sesem", "seşi", "seră", "sese", "aţi", "eţi", "iţi", "âţi", "sei", "ăm", "em", "im", "âm", "se" };
	private static final String[] STEP4_SUFFIXES1 = { "ie", "ă", "a", "e", "i" };

	private int R1;
	private int R2;
	private int RV;

	@Override
	public String stem(String word) {
		if (word.length() < MIN_LENGTH) {
			return word;
		}

		word = replace(word);
		markRegions(word);
		word = step0(word);
		String didntChange = word;
		word = step1(word);
		word = step2(word);

		if (didntChange.equals(word)) {
			word = step3(word);
		}

		word = step4(word);
		return finalize(word);
	}

	private void markRegions(String word) {
		RV = markRV(word);
		R1 = calcR1VC(word, VOWELS);
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private int markRV(String word) {
		int rv = word.length();
		char secondLetter = word.charAt(1);
		if (isConsonant(secondLetter, VOWELS)) {
			for (int i = 2; i < word.length(); i++) {
				if (isVowel(word.charAt(i), VOWELS)) {
					rv = i + 1;
					break;
				}
			}
		} else if (isVowel(word.charAt(0), VOWELS)) {
			for (int i = 2; i < word.length(); i++) {
				if (isConsonant(word.charAt(i), VOWELS)) {
					rv = i + 1;
					break;
				}
			}
		} else {
			rv = 3;
		}
		return rv;
	}

	private String step0(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP0_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		if (word.endsWith(STEP0_SUFFIXES2)) {
			if (R1String.endsWith(STEP0_SUFFIXES2)) {
				word = removeEnding(word, STEP0_SUFFIXES2.length()) + STEP0_SUFFIXES2_REPLACEMENT;
			}
			return word;
		}

		for (String suffix : STEP0_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP0_SUFFIXES3_REPLACEMENT;
				}
				return word;
			}
		}

		for (String suffix : STEP0_SUFFIXES4) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP0_SUFFIXES4_REPLACEMENT;
				}
				return word;
			}
		}

		if (word.endsWith(STEP0_SUFFIXES5)) {
			if (R1String.endsWith(STEP0_SUFFIXES5) && !word.endsWith(STEP0_SUFFIXES5_preceding + STEP0_SUFFIXES5)) {
				word = removeEnding(word, STEP0_SUFFIXES2.length()) + STEP0_SUFFIXES5_REPLACEMENT;
			}
			return word;
		}

		if (word.endsWith(STEP0_SUFFIXES6)) {
			if (R1String.endsWith(STEP0_SUFFIXES6)) {
				word = removeEnding(word, STEP0_SUFFIXES6.length()) + STEP0_SUFFIXES6_REPLACEMENT;
			}
			return word;
		}

		for (String suffix : STEP0_SUFFIXES7) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP0_SUFFIXES7_REPLACEMENT;
				}
				break;
			}
		}

		return word;
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP1_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES1_REPLACEMENT;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES2_REPLACEMENT;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES3_REPLACEMENT;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES4) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES4_REPLACEMENT;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES5) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES5_REPLACEMENT;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES6) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES6_REPLACEMENT;
					return step1(word);
				}
				break;
			}
		}

		return word;
	}

	private String step2(String word) {
		String R2String = getRegionSubstring(word, R2);
		for (String suffix : STEP2_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					if (word.length() > suffix.length()) {
						char preceding = word.charAt(word.length() - suffix.length() - 1);
						if (preceding == 'ţ') {
							word = removeEnding(word, suffix.length() + 1) + 't';
						}
					}
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP2_SUFFIXES3_REPLACEMENT;
				}
				break;
			}
		}

		return word;
	}

	private String step3(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP3_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				if (RVString.length() > suffix.length()) {
					char preced = RVString.charAt(RVString.length() - suffix.length() - 1);
					if (isConsonant(preced, VOWELS) || preced == 'u') {
						word = removeEnding(word, suffix.length());
					}
				}
				return word;
			}
		}

		for (String suffix : STEP3_SUFFIXES2) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private String step4(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP4_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (RVString.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				break;
			}
		}
		return word;
	}

	private String replace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		boolean prevIsVowel = false;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'i' || ch == 'u') {
				if (prevIsVowel && i + 1 < word.length() && isVowel(word.charAt(i + 1), VOWELS)) {
					ch = Character.toUpperCase(ch);
				}
			}
			sb.append(ch);
			prevIsVowel = isVowel(ch, VOWELS);
		}
		return sb.toString();
	}

	private String finalize(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'I' || ch == 'U') {
				ch = Character.toLowerCase(ch);
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	@Override
	public Language getLanguage() {
		return Language.ROMANIAN;
	}
}
