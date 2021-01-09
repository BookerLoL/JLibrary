package main.nlp.stemmers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Snowball stemmer for standard Italian
 * 
 * Source: https://snowballstem.org/otherapps/schinke/
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class LatinSchinkeStemmer extends Stemmer {
	private static final Set<String> QUE_SUFFIXES = new HashSet<>(Arrays.asList(new String[] { "quotusquisque",
			"praetorque", "plenisque", "quandoque", "quorumque", "quarumque", "quibusque", "utribique", "contorque",
			"peraeque", "cuiusque", "quousque", "concoque", "detorque", "extorque", "obtorque", "optorque", "retorque",
			"attorque", "intorque", "abusque", "adaeque", "adusque", "denique", "oblique", "quisque", "quaeque",
			"quemque", "quamque", "quosque", "quasque", "undique", "uterque", "utroque", "decoque", "excoque",
			"recoque", "incoque", "quoque", "itaque", "absque", "apsque", "susque", "cuique", "quaque", "quique",
			"ubique", "utique", "torque", "atque", "neque", "deque", "usque", "coque" }));

	private static final String[] NOUN_SUFFIXES = { "ibus", "ius", "ae", "am", "as", "em", "es", "ia", "is", "nt", "os",
			"ud", "um", "us", "a", "e", "i", "o", "u" };

	// even indexes are suffixes, odd are replacement values
	private static final String[][] VERB_SUFFIXES = { { "iuntur", "erunt", "untur", "iunt", "unt" }, { "i" },
			{ "beris", "bor", "bo" }, { "bi" }, { "ero" }, { "eri" },
			{ "mini", "ntur", "stis", "mur", "mus", "ris", "sti", "tis", "tur", "ns", "nt", "ri", "m", "r", "s", "t" },
			{ EMPTY } };

	private Map<String, String> nounForms;
	private Map<String, String> verbForms;

	public LatinSchinkeStemmer() {
		nounForms = new HashMap<>();
		verbForms = new HashMap<>();
		fillDictionaries();
	}

	private void fillDictionaries() {
		QUE_SUFFIXES.forEach(word -> {
			nounForms.put(word, word);
			verbForms.put(word, word);
		});
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

		return word;
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
		if (QUE_SUFFIXES.contains(word)) {
			return word;
		}
		return removeEnding(word, 3);
	}

	private void determineNounForm(String word) {
		if (nounForms.containsKey(word)) {
			return;
		}
		String original = word;

		for (String suffix : NOUN_SUFFIXES) {
			if (word.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}

		if (word.length() > 1) {
			final String nounForm = word;
			this.nounForms.computeIfAbsent(original, k -> nounForm);
		}
	}

	private void determineVerbForm(String word) {
		if (verbForms.containsKey(word)) {
			return;
		}

		String original = word;

		outer: for (int i = 0; i < VERB_SUFFIXES.length; i += 2) {
			for (String suffix : VERB_SUFFIXES[i]) {
				if (word.endsWith(suffix)) {
					if (word.length() - suffix.length() >= 2) {
						word = removeEnding(word, suffix.length());
						word += VERB_SUFFIXES[i + 1][0];
					}
					break outer;
				}
			}
		}

		if (word.length() > 1) {
			final String verbForm = word;
			verbForms.computeIfAbsent(original, k -> verbForm);
		}
	}

	public String getNounForm(String word) {
		return nounForms.get(stem(word));
	}

	public String getVerbForm(String word) {
		return verbForms.get(stem(word));
	}

	@Override
	public Language getLanguage() {
		return Language.LATIN;
	}
}
