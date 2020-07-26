package stemmers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Snowball stemmer
 * 
 * For Latin
 * 
 * https://snowballstem.org/otherapps/schinke/
 * 
 */
public class LatinSchinkeStemmer extends Stemmer {
	private static final Set<String> queSuffixWords = new HashSet<>(Arrays.asList(new String[] { "quotusquisque",
			"praetorque", "plenisque", "quandoque", "quorumque", "quarumque", "quibusque", "utribique", "contorque",
			"peraeque", "cuiusque", "quousque", "concoque", "detorque", "extorque", "obtorque", "optorque", "retorque",
			"attorque", "intorque", "abusque", "adaeque", "adusque", "denique", "oblique", "quisque", "quaeque",
			"quemque", "quamque", "quosque", "quasque", "undique", "uterque", "utroque", "decoque", "excoque",
			"recoque", "incoque", "quoque", "itaque", "absque", "apsque", "susque", "cuique", "quaque", "quique",
			"ubique", "utique", "torque", "atque", "neque", "deque", "usque", "coque" }));

	private static final String[] noun_suffixes = { "ibus", "ius", "ae", "am", "as", "em", "es", "ia", "is", "nt", "os",
			"ud", "um", "us", "a", "e", "i", "o", "u" };

	// even indexes are suffixes, odd are replacement values
	private static final String[][] verb_suffixes = { { "iuntur", "erunt", "untur", "iunt", "unt" }, { "i" },
			{ "beris", "bor", "bo" }, { "bi" }, { "ero" }, { "eri" },
			{ "mini", "ntur", "stis", "mur", "mus", "ris", "sti", "tis", "tur", "ns", "nt", "ri", "m", "r", "s", "t" },
			{ EMPTY } };

	private Map<String, String> nounForms = new HashMap<>();
	private Map<String, String> verbForms = new HashMap<>();

	public LatinSchinkeStemmer() {
		fillDictionaries();
	}

	private void fillDictionaries() {
		queSuffixWords.forEach(word -> {
			nounForms.put(word, word);
			verbForms.put(word, word);
		});
	}

	private String prelude(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'j') {
				sb.append('i');
			} else if (ch == 'v') {
				sb.append('u');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String isQueSuffixEnding(String word) {
		if (queSuffixWords.contains(word)) {
			return word;
		}
		return removeEnding(word, 3); 
	}

	private void determineNounForm(String word) {
		if (nounForms.containsKey(word)) {
			return;
		}
		String original = word;

		for (String suffix : noun_suffixes) {
			if (word.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}

		if (word.length() > 1) {
			writeToNounDictionary(original, word);
		}
	}

	private void determineVerbForm(String word) {
		if (verbForms.containsKey(word)) {
			return;
		}
		String original = word;

		outer: for (int i = 0; i < verb_suffixes.length; i += 2) {
			for (String suffix : verb_suffixes[i]) {
				if (word.endsWith(suffix)) {
					if (word.length() - suffix.length() >= 2) {
						word = removeEnding(word, suffix.length());
						word += verb_suffixes[i + 1][0];
					}
					break outer;
				}
			}
		}

		if (word.length() > 1) {
			writeToVerbDictionary(original, word);
		}
	}

	private boolean writeToNounDictionary(String wordKey, String value) {
		if (!nounForms.containsKey(wordKey)) {
			nounForms.put(wordKey, value);
		}
		return true;
	}

	private boolean writeToVerbDictionary(String wordKey, String value) {
		if (!verbForms.containsKey(wordKey)) {
			verbForms.put(wordKey, value);
		}
		return true;
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = prelude(word);

		if (word.endsWith("que")) {
			word = isQueSuffixEnding(word);
		}

		determineNounForm(word);
		determineVerbForm(word);

		if (!nounForms.containsKey(word)) {
			writeToNounDictionary(word, word);
		}
		if (!verbForms.containsKey(word)) {
			writeToVerbDictionary(word, word);
		}
		return word;
	}

	public String getNounForm(String word) {
		return nounForms.get(stem(word));
	}

	public String getVerbForm(String word) {
		return verbForms.get(stem(word));
	}
}
