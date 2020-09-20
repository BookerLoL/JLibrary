package main.nlp.stemmers;
/*
 * Snowball stemmer
 * 
 * For Standard Italian
 * 
 * https://snowballstem.org/algorithms/italian/stemmer.html
 * 
 */
public class ItalianSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'à', 'è', 'ì', 'ò', 'ù' };
	private static final String[] step0_suffixes1 = { "gliela", "gliele", "glieli", "glielo", "gliene", "sene", "mela",
			"mele", "meli", "melo", "mene", "tela", "tele", "teli", "telo", "tene", "cela", "cele", "celi", "celo",
			"cene", "vela", "vele", "veli", "velo", "vene", "gli", "ci", "la", "le", "li", "lo", "mi", "ne", "si", "ti",
			"vi" };
	private static final String[] step0_suffixes1_preceding1 = { "ando", "endo" };
	private static final String[] step0_suffixes1_preceding2 = { "ar", "er", "ir" };
	private static final String[] step1_suffixes1 = { "atrice", "atrici", "abile", "abili", "ibile", "ibili", "mente",
			"anza", "anze", "iche", "ichi", "ismo", "ismi", "ista", "iste", "isti", "istà", "istè", "istì", "ante",
			"anti", "ico", "ici", "ica", "ice", "oso", "osi", "osa", "ose" };
	private static final String[] step1_suffixes2 = { "azione", "azioni", "atore", "atori" };
	private static final String step1_suffixes2_replacement = "ic";

	private static final String[] step1_suffixes3 = { "logia", "logie" };
	private static final String step1_suffixes3_replacement = "log";
	private static final String[] step1_suffixes4 = { "uzione", "uzioni", "usione", "usioni" };
	private static final String step1_suffixes4_replacement = "u";
	private static final String[] step1_suffixes5 = { "enza", "enze" };
	private static final String step1_suffixes5_replacement = "ente";
	private static final String[] step1_suffixes6 = { "amento", "amenti", "imento", "imenti" };
	private static final String[] step1_suffixes7 = { "amente" };
	private static final String[] step1_suffixes7_preceding1 = { "ativ", "iv", "os", "ic", "abil" };
	private static final String[] step1_suffixes8 = { "ità" };
	private static final String[] step1_suffixes8_preceding1 = { "abil", "ic", "iv" };
	private static final String[] step1_suffixes9 = { "ivo", "ivi", "iva", "ive" };
	private static final String[] step1_suffixes9_preceding1 = { "icat", "at" };
	private static final String[] step2_suffixes1 = { "erebbero", "irebbero", "assero", "assimo", "eranno", "erebbe",
			"eremmo", "ereste", "eresti", "essero", "iranno", "irebbe", "iremmo", "ireste", "iresti", "iscano",
			"iscono", "issero", "arono", "avamo", "avano", "avate", "eremo", "erete", "erono", "evamo", "evano",
			"evate", "iremo", "irete", "irono", "ivamo", "ivano", "ivate", "ammo", "ando", "asse", "assi", "emmo",
			"enda", "ende", "endi", "endo", "erai", "erei", "Yamo", "iamo", "immo", "irai", "irei", "isca", "isce",
			"isci", "isco", "ano", "are", "ata", "ate", "ati", "ato", "ava", "avi", "avo", "erà", "ere", "erò", "ete",
			"eva", "evi", "evo", "irà", "ire", "irò", "ita", "ite", "iti", "ito", "iva", "ivi", "ivo", "ono", "uta",
			"ute", "uti", "uto", "ar", "ir" };
	private static final String[] step3a_suffixes1 = { "a", "e", "i", "o", "à", "è", "ì", "ò" };
	private static final String step3a_suffixes_precedingChar = "i";

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
				R1 = i + 2; // Get area after combination
				break;
			}
		}

		for (int i = R1; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R2 = i + 2; // Get area after combination
				break;
			}
		}
	}

	private String step0(String word) {
		for (String suffix : step0_suffixes1) {
			if (word.endsWith(suffix)) {
				String RVString = getRegionSubstring(word, RV);
				RVString = removeEnding(RVString, suffix.length());
				for (String precedingSuffix : step0_suffixes1_preceding1) {
					if (RVString.endsWith(precedingSuffix)) {
						word = removeEnding(word, suffix.length());
						return word;
					}
				}

				for (String precedingSuffix : step0_suffixes1_preceding2) {
					if (RVString.endsWith(precedingSuffix)) {
						word = removeEnding(word, suffix.length()) + "e";
						break;
					}
				}
				break;
			}
		}
		return word;
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		String R2String = getRegionSubstring(word, R2);
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : step1_suffixes7) { // must do this before subset of that suffix
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String precedingSuffix : step1_suffixes7_preceding1) {
						if (R2String.endsWith(precedingSuffix)) {
							word = removeEnding(word, precedingSuffix.length());
							break;
						}
					}
					return word;
				}
			}

		}

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

					if (R2String.endsWith(step1_suffixes2_replacement)) {
						word = removeEnding(word, step1_suffixes2_replacement.length());
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes3) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + step1_suffixes3_replacement;
				return word;
			}
		}

		for (String suffix : step1_suffixes4) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + step1_suffixes4_replacement;
				return word;
			}
		}

		for (String suffix : step1_suffixes5) {

			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + step1_suffixes5_replacement;
				return word;
			}

		}

		for (String suffix : step1_suffixes6) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : step1_suffixes8) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String precedingSuffix : step1_suffixes8_preceding1) {
					if (R2String.endsWith(precedingSuffix)) {
						word = removeEnding(word, precedingSuffix.length());
						break;
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes9) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String precedingSuffix : step1_suffixes9_preceding1) {
					if (R2String.endsWith(precedingSuffix)) {
						word = removeEnding(word, precedingSuffix.length());
						break;
					}
				}
				break;
			}
		}
		return word;
	}

	private String step2(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step2_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private String step3a(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step3a_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				RVString = removeEnding(RVString, suffix.length());
				if (RVString.endsWith(step3a_suffixes_precedingChar)) {
					word = removeEnding(word, step3a_suffixes_precedingChar.length());
				}
				break;
			}
		}
		return word;
	}

	private String step3b(String word) {
		String RVString = getRegionSubstring(word, RV);
		if (RVString.endsWith("ch") || RVString.endsWith("gh")) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String finalize(String word) {
		return word.toLowerCase();
	}

	private String replace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		boolean prevIsVowel = false;
		char prevCh = '\0';
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (prevCh == 'q' && ch == 'u') {
				ch = Character.toUpperCase(ch);
			} else if (prevIsVowel && (ch == 'i' || ch == 'u')) {
				if (i + 1 < word.length() && isVowel(word.charAt(i + 1))) {
					ch = Character.toUpperCase(ch);
				}
			} else if (ch == 'á') {
				ch = 'à';
			} else if (ch == 'é') {
				ch = 'è';
			} else if (ch == 'í') {
				ch = 'ì';
			} else if (ch == 'ó') {
				ch = 'ò';
			} else if (ch == 'ú') {
				ch = 'ù';
			}
			sb.append(ch);
			prevCh = ch;
			prevIsVowel = isVowel(ch);
		}
		return sb.toString();
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = replace(word);
		if (word.length() < MIN_LENGTH) {
			return word;
		}
		markRegions(word);
		word = step0(word);
		String temp = word;
		word = step1(word);
		if (temp.equals(word)) {
			word = step2(word);
		}
		word = step3a(word);
		word = step3b(word);
		return finalize(word);
	}
}
