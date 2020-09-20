package main.nlp.stemmers;/*
 * Snowball stemmer
 * 
 * For Standard Romanian
 * 
 * https://snowballstem.org/algorithms/romanian/stemmer.html
 * 
 */
public class RomanianSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'ă', 'â', 'e', 'i', 'î', 'o', 'u', 'Ã' };
	private static final String[] step0_suffixes1 = { "ului", "ul" };
	private static final String step0_suffixes2 = "aua";
	private static final String step0_suffixes2_replacement = "a";
	private static final String[] step0_suffixes3 = { "elor", "ele", "ea" };
	private static final String step0_suffixes3_replacement = "e";
	private static final String[] step0_suffixes4 = { "iilor", "ilor", "iile", "iei", "iua", "ii" };
	private static final String step0_suffixes4_replacement = "i";
	private static final String step0_suffixes5 = "ile";
	private static final String step0_suffixes5_preceding = "ab";
	private static final String step0_suffixes5_replacement = "i";
	private static final String step0_suffixes6 = "atei";
	private static final String step0_suffixes6_replacement = "at";
	private static final String[] step0_suffixes7 = { "aţia", "aţie" };
	private static final String step0_suffixes7_replacement = "aţi";
	private static final String[] step1_suffixes1 = { "abilitate", "abilitati", "abilităţi", "abilităi" };
	private static final String step1_suffixes1_replacement = "abil";
	private static final String[] step1_suffixes2 = { "ibilitate" };
	private static final String step1_suffixes2_replacement = "ibil";
	private static final String[] step1_suffixes3 = { "ivitate", "ivitati", "ivităţi", "ivităi" };
	private static final String step1_suffixes3_replacement = "iv";
	private static final String[] step1_suffixes4 = { "icitate", "icitati", "icităţi", "icatori", "icităi", "icator",
			"iciva", "icive", "icivi", "icivă", "icala", "icale", "icali", "icală", "iciv", "ical" };
	private static final String step1_suffixes4_replacement = "ic";
	private static final String[] step1_suffixes5 = { "aţiune", "atoare", "ătoare", "ativa", "ative", "ativi", "ativă",
			"atori", "ători", "ativ", "ator", "ător" };
	private static final String step1_suffixes5_replacement = "at";
	private static final String[] step1_suffixes6 = { "iţiune", "itoare", "itiva", "itive", "itivi", "itivă", "itori",
			"itiv", "itor" };
	private static final String step1_suffixes6_replacement = "it";
	private static final String[] step2_suffixes1 = { "abila", "abile", "abili", "abilă", "ibila", "ibile", "ibili",
			"ibilă", "atori", "itate", "itati", "ităţi", "abil", "ibil", "oasa", "oasă", "oase", "anta", "ante", "anti",
			"antă", "ator", "ităi", "ata", "ată", "ati", "ate", "uta", "ută", "uti", "ute", "ita", "ită", "iti", "ite",
			"ica", "ice", "ici", "ică", "osi", "oşi", "ant", "iva", "ive", "ivi", "ivă", "at", "ut", "it", "ic", "os",
			"iv" };
	private static final String[] step2_suffixes2 = { "iune", "iuni" };
	private static final String[] step2_suffixes3 = { "isme", "ista", "iste", "isti", "istă", "işti", "ism", "ist" };
	private static final String step2_suffixes3_replacement = "ist";
	private static final String[] step3_suffixes1 = { "aserăţi", "iserăţi", "âserăţi", "userăţi", "aserăm", "iserăm",
			"âserăm", "userăm", "ească", "arăţi", "urăţi", "irăţi", "ârăţi", "aseşi", "aseră", "iseşi", "iseră",
			"âseşi", "âseră", "useşi", "useră", "indu", "ându", "ează", "eşti", "eşte", "ăşti", "ăşte", "eaţi", "iaţi",
			"arăm", "urăm", "irăm", "ârăm", "asem", "isem", "âsem", "usem", "are", "ere", "ire", "âre", "ind", "ând",
			"eze", "ezi", "esc", "ăsc", "eam", "eai", "eau", "iam", "iai", "iau", "aşi", "ară", "uşi", "ură", "işi",
			"iră", "âşi", "âră", "ase", "ise", "âse", "use", "ez", "am", "ai", "au", "ea", "ia", "ui", "âi" };
	private static final String[] step3_suffixes2 = { "seserăţi", "seserăm", "serăţi", "seseşi", "seseră", "serăm",
			"sesem", "seşi", "seră", "sese", "aţi", "eţi", "iţi", "âţi", "sei", "ăm", "em", "im", "âm", "se" };
	private static final String[] step4_suffixes1 = { "ie", "ă", "a", "e", "i" };

	private int R1;
	private int R2;
	private int RV;

	private boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private boolean isConsonant(char ch) {
		return !isVowel(ch);
	}

	private void markRegions(String word) {
		markRV(word);
		markRNumbers(word);
	}

	private void markRV(String word) {
		RV = word.length();
		char secondLetter = word.charAt(1);
		if (isConsonant(secondLetter)) { // X C
			for (int i = 2; i < word.length(); i++) {
				if (isVowel(word.charAt(i))) {
					RV = i + 1;
					break;
				}
			}
		} else if (isVowel(word.charAt(0))) { // V V
			for (int i = 2; i < word.length(); i++) {
				if (isConsonant(word.charAt(i))) {
					RV = i + 1;
					break;
				}
			}
		} else {
			RV = 3;
		}
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

	private String step0(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step0_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		if (word.endsWith(step0_suffixes2)) {
			if (R1String.endsWith(step0_suffixes2)) {
				word = removeEnding(word, step0_suffixes2.length()) + step0_suffixes2_replacement;
			}
			return word;
		}

		for (String suffix : step0_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step0_suffixes3_replacement;
				}
				return word;
			}
		}

		for (String suffix : step0_suffixes4) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step0_suffixes4_replacement;
				}
				return word;
			}
		}

		if (word.endsWith(step0_suffixes5)) {
			if (R1String.endsWith(step0_suffixes5) && !word.endsWith(step0_suffixes5_preceding + step0_suffixes5)) {
				word = removeEnding(word, step0_suffixes2.length()) + step0_suffixes5_replacement;
			}
			return word;
		}

		if (word.endsWith(step0_suffixes6)) {
			if (R1String.endsWith(step0_suffixes6)) {
				word = removeEnding(word, step0_suffixes6.length()) + step0_suffixes6_replacement;
			}
			return word;
		}

		for (String suffix : step0_suffixes7) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step0_suffixes7_replacement;
				}
				break;
			}
		}

		return word;
	}

	// This is a repetitive step until no removals occur
	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step1_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes1_replacement;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes2_replacement;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes3_replacement;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes4) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes4_replacement;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes5) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes5_replacement;
					return step1(word);
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes6) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step1_suffixes6_replacement;
					return step1(word);
				}
				break;
			}
		}

		return word;
	}

	private String step2(String word) {
		String R2String = getRegionSubstring(word, R2);
		for (String suffix : step2_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : step2_suffixes2) {
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

		for (String suffix : step2_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + step2_suffixes3_replacement;
				}
				break;
			}
		}

		return word;
	}

	private String step3(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step3_suffixes1) {
			if (RVString.endsWith(suffix)) {
				if (RVString.length() > suffix.length()) {
					char preced = RVString.charAt(RVString.length() - suffix.length() - 1);
					if (isConsonant(preced) || preced == 'u') {
						word = removeEnding(word, suffix.length());
					}
				}
				return word;
			}
		}

		for (String suffix : step3_suffixes2) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private String step4(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step4_suffixes1) {
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
				if (prevIsVowel && i + 1 < word.length() && isVowel(word.charAt(i + 1))) {
					ch = Character.toUpperCase(ch);
				}
			}
			sb.append(ch);
			prevIsVowel = isVowel(ch);
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
}
