package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Italian
 * 
 * Source: https://snowballstem.org/algorithms/italian/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class ItalianSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'à', 'è', 'ì', 'ò', 'ù' };

	private static final String[] STEP0_SUFFIXES1 = { "gliela", "gliele", "glieli", "glielo", "gliene", "sene", "mela",
			"mele", "meli", "melo", "mene", "tela", "tele", "teli", "telo", "tene", "cela", "cele", "celi", "celo",
			"cene", "vela", "vele", "veli", "velo", "vene", "gli", "ci", "la", "le", "li", "lo", "mi", "ne", "si", "ti",
			"vi" };
	private static final String[] STEP0_SUFFIXES1_PRECEDING1 = { "ando", "endo" };
	private static final String[] STEP0_SUFFIXES1_PRECEDING2 = { "ar", "er", "ir" };
	private static final String[] STEP1_SUFFIXES1 = { "atrice", "atrici", "abile", "abili", "ibile", "ibili", "mente",
			"anza", "anze", "iche", "ichi", "ismo", "ismi", "ista", "iste", "isti", "istà", "istè", "istì", "ante",
			"anti", "ico", "ici", "ica", "ice", "oso", "osi", "osa", "ose" };
	private static final String[] STEP1_SUFFIXES2 = { "azione", "azioni", "atore", "atori" };
	private static final String STEP1_SUFFIXES2_replacement = "ic";

	private static final String[] STEP1_SUFFIXES3 = { "logia", "logie" };
	private static final String STEP1_SUFFIXES3_replacement = "log";
	private static final String[] STEP1_SUFFIXES4 = { "uzione", "uzioni", "usione", "usioni" };
	private static final String STEP1_SUFFIXES4_replacement = "u";
	private static final String[] STEP1_SUFFIXES5 = { "enza", "enze" };
	private static final String STEP1_SUFFIXES5_replacement = "ente";
	private static final String[] STEP1_SUFFIXES6 = { "amento", "amenti", "imento", "imenti" };
	private static final String[] STEP1_SUFFIXES7 = { "amente" };
	private static final String[] STEP1_SUFFIXES7_PRECEDING1 = { "ativ", "iv", "os", "ic", "abil" };
	private static final String[] STEP1_SUFFIXES8 = { "ità" };
	private static final String[] STEP1_SUFFIXES8_PRECEDING1 = { "abil", "ic", "iv" };
	private static final String[] STEP1_SUFFIXES9 = { "ivo", "ivi", "iva", "ive" };
	private static final String[] STEP1_SUFFIXES9_PRECEDING1 = { "icat", "at" };
	private static final String[] STEP2_SUFFIXES1 = { "erebbero", "irebbero", "assero", "assimo", "eranno", "erebbe",
			"eremmo", "ereste", "eresti", "essero", "iranno", "irebbe", "iremmo", "ireste", "iresti", "iscano",
			"iscono", "issero", "arono", "avamo", "avano", "avate", "eremo", "erete", "erono", "evamo", "evano",
			"evate", "iremo", "irete", "irono", "ivamo", "ivano", "ivate", "ammo", "ando", "asse", "assi", "emmo",
			"enda", "ende", "endi", "endo", "erai", "erei", "Yamo", "iamo", "immo", "irai", "irei", "isca", "isce",
			"isci", "isco", "ano", "are", "ata", "ate", "ati", "ato", "ava", "avi", "avo", "erà", "ere", "erò", "ete",
			"eva", "evi", "evo", "irà", "ire", "irò", "ita", "ite", "iti", "ito", "iva", "ivi", "ivo", "ono", "uta",
			"ute", "uti", "uto", "ar", "ir" };
	private static final String[] STEP3A_SUFFIXES1 = { "a", "e", "i", "o", "à", "è", "ì", "ò" };
	private static final String STEP3A_SUFFIXES_PRECEDING_CHAR = "i";

	private int R1;
	private int R2;
	private int RV;

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = replace(word);
		if (word.length() < MIN_LENGTH) {
			return word;
		}
		markRegions(word);
		word = step0(word);
		String prevWord = word;
		word = step1(word);
		if (prevWord.equals(word)) {
			word = step2(word);
		}
		word = step3a(word);
		word = step3b(word);
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
		for (String suffix : STEP0_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				String RVString = getRegionSubstring(word, RV);
				RVString = removeEnding(RVString, suffix.length());
				for (String PRECEDINGSuffix : STEP0_SUFFIXES1_PRECEDING1) {
					if (RVString.endsWith(PRECEDINGSuffix)) {
						word = removeEnding(word, suffix.length());
						return word;
					}
				}

				for (String PRECEDINGSuffix : STEP0_SUFFIXES1_PRECEDING2) {
					if (RVString.endsWith(PRECEDINGSuffix)) {
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

		for (String suffix : STEP1_SUFFIXES7) { // must do this before subset of that suffix
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String PRECEDINGSuffix : STEP1_SUFFIXES7_PRECEDING1) {
						if (R2String.endsWith(PRECEDINGSuffix)) {
							word = removeEnding(word, PRECEDINGSuffix.length());
							break;
						}
					}
					return word;
				}
			}

		}

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

					if (R2String.endsWith(STEP1_SUFFIXES2_replacement)) {
						word = removeEnding(word, STEP1_SUFFIXES2_replacement.length());
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES3) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES3_replacement;
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES4) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES4_replacement;
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES5) {

			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length()) + STEP1_SUFFIXES5_replacement;
				return word;
			}

		}

		for (String suffix : STEP1_SUFFIXES6) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES8) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String PRECEDINGSuffix : STEP1_SUFFIXES8_PRECEDING1) {
					if (R2String.endsWith(PRECEDINGSuffix)) {
						word = removeEnding(word, PRECEDINGSuffix.length());
						break;
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES9) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String PRECEDINGSuffix : STEP1_SUFFIXES9_PRECEDING1) {
					if (R2String.endsWith(PRECEDINGSuffix)) {
						word = removeEnding(word, PRECEDINGSuffix.length());
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
		for (String suffix : STEP2_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private String step3a(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP3A_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				RVString = removeEnding(RVString, suffix.length());
				if (RVString.endsWith(STEP3A_SUFFIXES_PRECEDING_CHAR)) {
					word = removeEnding(word, STEP3A_SUFFIXES_PRECEDING_CHAR.length());
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

	private String replace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char prevCh = EMPTY_CH;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (prevCh == 'q' && ch == 'u') {
				ch = Character.toUpperCase(ch);
			} else if ((ch == 'i' || ch == 'u') && isVowel(prevCh, VOWELS)) {
				if (i + 1 < word.length() && isVowel(word.charAt(i + 1), VOWELS)) {
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
		}
		return sb.toString();
	}

	private String finalize(String word) {
		return word.toLowerCase();
	}

	@Override
	public Language getLanguage() {
		return Language.ITALIAN;
	}
}
