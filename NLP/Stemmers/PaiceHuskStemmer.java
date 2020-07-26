package stemmers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Paice Husk Stemmer
 * 
 * For English
 * 
 * https://dl.acm.org/doi/pdf/10.1145/101306.101310
 * 
 * Note: There are some stems that I'm unsure are right or wrong because there was no expected file was found 
 * but compared to other implementations there is a 0.25% difference in stemmed words.
 * 
 * Note: The stem text file will have this stemmer's stemmed words, but if there is a low extremely low rate you should be fine. 
 * 
 * You can also add or change rules if you need certain results.
 * 
 * You can include a lexical lookup for exceptions
 * 
 */
public class PaiceHuskStemmer extends Stemmer {
	private class Rule {
		String suffix; 
		boolean intactFlag; 
		int removeAmount; 
		String appendAfter; 
		boolean continueFlag; 

		public Rule(String ending, boolean intact, int removeAmount, String append, boolean continuation) {
			suffix = ending;
			intactFlag = intact;
			this.removeAmount = removeAmount;
			appendAfter = append;
			continueFlag = continuation;
		}

		public boolean isContinue() {
			return continueFlag;
		}

		public boolean canApply(String word) {
			if (intactFlag) {
				return isUnchanged && word.endsWith(suffix) && passesAcceptabilityConditions(word);
			}
			return word.endsWith(suffix) && passesAcceptabilityConditions(word);
		}

		/*
		 * If the first letter is a vowel, check if the stemmed word has at least length
		 * of 2 Else, first letter is a consonant, check if stemmed word has at least
		 * length of 3 and must contain a vowel or 'y'
		 * 
		 * I changed the meaning of 'y' to be a vowel if it's not the first letter of
		 * the word and it's not following a vowel Look up "porter 2" defintion of a
		 * vowel
		 */
		private boolean passesAcceptabilityConditions(String word) {
			char firstLetter = word.charAt(0);
			char prev = 'a';
			if (isVowel(firstLetter, prev)) {
				return (word.length() - removeAmount) + appendAfter.length() >= 2;
			} else {
				String newWord = removeEnding(word, removeAmount) + appendAfter;
				if (newWord.length() >= 3) {
					for (int i = 0; i < newWord.length(); i++) {
						char curr = newWord.charAt(i);
						if (isVowel(curr, prev)) {
							return true;
						}
						prev = curr;
					}
				}
				return false;
			}
		}

		private boolean isVowel(char ch, char prev) {
			return isNormalVowel(ch) || isYVowel(ch, prev);
		}

		private boolean isYVowel(char ch, char prev) {
			return ch == 'y' && !isNormalVowel(prev);
		}

		private boolean isNormalVowel(char ch) {
			return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
		}

		public String apply(String word) {
			word = removeEnding(word, removeAmount);
			if (!isEmpty(appendAfter)) {
				word += appendAfter;
			}
			isUnchanged = false;
			return word;
		}
	}

	private Rule[] rules = { new Rule("ia", true, 2, EMPTY, false), new Rule("a", true, 1, EMPTY, false),
			new Rule("bb", false, 1, EMPTY, false), new Rule("ytic", false, 3, "s", false),
			new Rule("ic", false, 2, EMPTY, true), new Rule("nc", false, 1, "t", true),
			new Rule("dd", false, 1, EMPTY, false), new Rule("ied", false, 3, "y", true),
			new Rule("ceed", false, 2, "ss", false), new Rule("eed", false, 1, EMPTY, false),
			new Rule("ed", false, 2, EMPTY, true), new Rule("hood", false, 4, EMPTY, true),
			new Rule("e", false, 1, EMPTY, true), new Rule("lief", false, 1, "v", false),
			new Rule("if", false, 2, EMPTY, true), new Rule("ing", false, 3, EMPTY, true),
			new Rule("iag", false, 3, "y", false), new Rule("ag", false, 2, EMPTY, true),
			new Rule("gg", false, 1, EMPTY, false), new Rule("th", true, 2, EMPTY, false),
			new Rule("guish", false, 5, "ct", false), new Rule("ish", false, 3, EMPTY, true),
			new Rule("i", true, 1, EMPTY, false), new Rule("i", false, 1, "y", true),
			new Rule("ij", false, 1, "d", false), new Rule("fuj", false, 1, "s", false),
			new Rule("uj", false, 1, "d", false), new Rule("oj", false, 1, "d", false),
			new Rule("hej", false, 1, "r", false), new Rule("verj", false, 1, "t", false),
			new Rule("misj", false, 2, "t", false), new Rule("nj", false, 1, "d", false),
			new Rule("j", false, 1, "s", false), new Rule("ifiabl", false, 6, EMPTY, false),
			new Rule("iabl", false, 4, "y", false), new Rule("abl", false, 3, EMPTY, true),
			new Rule("ibl", false, 3, EMPTY, false), new Rule("bil", false, 2, "l", true),
			new Rule("cl", false, 1, EMPTY, false), new Rule("iful", false, 4, "y", false),
			new Rule("ful", false, 3, EMPTY, true), new Rule("ul", false, 2, EMPTY, false),
			new Rule("ial", false, 3, EMPTY, true), new Rule("ual", false, 3, EMPTY, true),
			new Rule("al", false, 2, EMPTY, true), new Rule("ll", false, 1, EMPTY, false),
			new Rule("ium", false, 3, EMPTY, false), new Rule("um", true, 2, EMPTY, false),
			new Rule("ism", false, 3, EMPTY, true), new Rule("mm", false, 1, EMPTY, false),
			new Rule("sion", false, 4, "j", true), new Rule("xion", false, 4, "ct", false),
			new Rule("ion", false, 3, EMPTY, true), new Rule("ian", false, 3, EMPTY, true),
			new Rule("an", false, 2, EMPTY, true), new Rule("een", false, 0, EMPTY, false),
			new Rule("en", false, 2, EMPTY, true), new Rule("nn", false, 1, EMPTY, false),
			new Rule("ship", false, 4, EMPTY, true), new Rule("pp", false, 1, EMPTY, false),
			new Rule("er", false, 2, EMPTY, true), new Rule("ear", false, 0, EMPTY, false),
			new Rule("ar", false, 2, EMPTY, false), new Rule("or", false, 2, EMPTY, true),
			new Rule("ur", false, 2, EMPTY, true), new Rule("rr", false, 1, EMPTY, false),
			new Rule("tr", false, 1, EMPTY, true), new Rule("ier", false, 3, "y", true),
			new Rule("ies", false, 3, "y", true), new Rule("sis", false, 2, EMPTY, false),
			new Rule("is", false, 2, EMPTY, true), new Rule("ness", false, 4, EMPTY, true),
			new Rule("ss", false, 0, EMPTY, false), new Rule("ous", false, 3, EMPTY, true),
			new Rule("us", true, 2, EMPTY, false), new Rule("s", true, 1, EMPTY, true),
			new Rule("s", false, 0, EMPTY, false), new Rule("plicat", false, 4, "y", false),
			new Rule("at", false, 2, EMPTY, true), new Rule("ment", false, 4, EMPTY, true),
			new Rule("ent", false, 3, EMPTY, true), new Rule("ant", false, 3, EMPTY, true),
			new Rule("ript", false, 2, "b", false), new Rule("orpt", false, 2, "b", false),
			new Rule("duct", false, 1, EMPTY, false), new Rule("sumpt", false, 2, EMPTY, false),
			new Rule("cept", false, 2, "iv", false), new Rule("olut", false, 2, "v", false),
			new Rule("sist", false, 0, EMPTY, false), new Rule("ist", false, 3, EMPTY, true),
			new Rule("tt", false, 1, EMPTY, false), new Rule("iqu", false, 3, EMPTY, false),
			new Rule("ogu", false, 1, EMPTY, false), new Rule("siv", false, 3, "j", true),
			new Rule("eiv", false, 0, EMPTY, false), new Rule("iv", false, 2, EMPTY, true),
			new Rule("bly", false, 1, EMPTY, true), new Rule("ily", false, 3, "y", true),
			new Rule("ply", false, 0, EMPTY, false), new Rule("ly", false, 2, EMPTY, true),
			new Rule("ogy", false, 1, EMPTY, false), new Rule("phy", false, 1, EMPTY, false),
			new Rule("omy", false, 1, EMPTY, false), new Rule("opy", false, 1, EMPTY, false),
			new Rule("ity", false, 3, EMPTY, true), new Rule("ety", false, 3, EMPTY, true),
			new Rule("lty", false, 2, EMPTY, false), new Rule("istry", false, 5, EMPTY, false),
			new Rule("ary", false, 3, EMPTY, true), new Rule("ory", false, 3, EMPTY, true),
			new Rule("ify", false, 3, EMPTY, false), new Rule("ncy", false, 2, "t", true),
			new Rule("acy", false, 3, EMPTY, true), new Rule("iz", false, 2, EMPTY, true),
			new Rule("yz", false, 1, "s", false) };

	private Map<Character, List<Rule>> ruleMapping;
	private boolean isUnchanged;
	private static final int MIN_LENGTH = 4;

	public PaiceHuskStemmer() {
		initRuleMapping();
	}

	private void initRuleMapping() {
		ruleMapping = new HashMap<>();
		for (Rule rule : rules) {
			Character lastCh = getFinalLetter(rule.suffix);

			if (!ruleMapping.containsKey(lastCh)) {
				ruleMapping.put(lastCh, new LinkedList<>());
			}
			ruleMapping.get(lastCh).add(rule);
		}
	}
	
	private String goThroughRules(String word) {
		List<Rule> rules = ruleMapping.get(getFinalLetter(word));
		if (rules == null) {
			return word;
		}

		for (Rule rule : rules) {
			if (rule.canApply(word)) {
				word = rule.apply(word);
				if (rule.isContinue()) {
					return goThroughRules(word);
				}
				break;
			}
		}
		return word;
	}

	private static char getFinalLetter(String word) {
		if (word == null || word.length() == 0) {
			return '\0';
		}
		return word.charAt(word.length() - 1);
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		if (word.length() < MIN_LENGTH) {
			return word;
		}
		isUnchanged = true;
		return goThroughRules(word).trim();
	}
}
