package stemmers;
/*
 * Lovins Stemmer
 * 
 * For English
 * 
 * http://snowball.tartarus.org/algorithms/lovins/stemmer.html
 * 
 * This is a working implementation and can be modified if there is something wrong or needs adding
 * 
 * Rather than using regex, they couldve been checked by looking at chars to be even quicker
 * I used regex merely for practice and learning.
 * 
 */
public class EnglishLovinsStemmer extends Stemmer {
	private interface Rule {
		public boolean test(String word);
	}

	private static class ContexualRule {
		private String ending;
		private Rule rule;
		private boolean changed;

		public ContexualRule(String ending, Rule rule) {
			this.ending = ending;
			this.rule = rule;
			changed = false;
		}

		public boolean isTestSuccessful() {
			return changed;
		}

		public String applyTest(String word) {
			changed = false;
			if (word.endsWith(ending)) {
				String stem = removeEnding(word, ending.length());
				if (rule.test(stem)) {
					changed = true;
					word = stem;
				}
			}
			return word;
		}
	}

	/*
	 * For those that don't know regex, here's a quick explanation
	 * 
	 * Regex Guide: 
	 * 		.  -> any character
	 * 		regex* -> 0 or infinite any regex
	 * 		(regexes) -> grouping of multiple regexes
	 *  	s|e -> s or e
	 *  	(regex)$ -> ends with (regex)
	 *  	[abc]  -> a|b|c
	 *  	[^abc] -> not (a|b|c)
	 *  	(?<!regex)$ -> negative lookbehind, does not end with regex
	 *  	
	 *  	If you are still confused, I suggest just playing around with these regexes in an online regex tester 
	 *  	and see what inputs are legal and what inputs aren't legal.
	 */
	//In lambda form, equivalent would be: function boolean ruleA(String word) { ...body... } 
	static Rule A = (word) -> true;
	static Rule B = (word) -> word.length() >= 3;
	static Rule C = (word) -> word.length() >= 4;
	static Rule D = (word) -> word.length() >= 5;
	static Rule E = (word) -> isMatching(word, new String[] {".*[^e]$"});
	static Rule F = (word) -> word.length() >= 3 && isMatching(word, new String[] {".*[^e]$"});
	static Rule G = (word) -> word.length() >= 3 && isMatching(word, new String[] {".*f$"});
	static Rule H = (word) -> isMatching(word, new String[] {".*(t|ll)$"});
	static Rule I = (word) -> isMatching(word, new String[] {".*[^oe]$"}); 
	static Rule J = (word) -> isMatching(word, new String[] {".*[^ae]$"});
	static Rule K = (word) -> word.length() >= 3 && isMatching(word, new String[] {".*(l|i|u.e)$"});
	static Rule L = (word) -> isMatching(word, new String[] {"(.*[^uxs]$)|(.*os$)"});
	static Rule M = (word) -> isMatching(word, new String[] {".*[^acem]$"});
	static Rule N = (word) -> word.length() >= 4 || (word.length() == 3 && isMatching(word, new String[] {"[^s].*$"}));
	static Rule O = (word) -> isMatching(word, new String[] {".*[li]$"});
	static Rule P = (word) -> isMatching(word, new String[] {".*[^c]$"});
	static Rule Q = (word) -> word.length() >= 3 && isMatching(word, new String[] {".*[^ln]$"});
	static Rule R = (word) -> isMatching(word, new String[] {".*[nr]$"});
	static Rule S = (word) -> isMatching(word, new String[] {".*(dr|[^t]t)$", "^t$"});
	static Rule T = (word) -> isMatching(word, new String[] {".*(s|[^o]t)$", "^t$"});
	static Rule U = (word) -> isMatching(word, new String[] {".*[lmnr]$"});
	static Rule V = (word) -> isMatching(word, new String[] {".*c$"});
	static Rule W = (word) -> isMatching(word, new String[] {".*[^su]$"});
	static Rule X = (word) -> isMatching(word, new String[] {".*(l|i|u.e)$"});
	static Rule Y = (word) -> isMatching(word, new String[] {".*in$"});
	static Rule Z = (word) -> isMatching(word, new String[] {".*[^f]$"});
	static Rule AA = (word) -> isMatching(word, new String[] {".*(d|f|ph|th|l|er|or|es|t)$"});
	static Rule BB = (word) -> word.length() >= 3 && isMatching(word, new String[] {".*(?<!met|ryst)$"});
	static Rule CC = (word) -> isMatching(word, new String[] {".*l$"});

	private static final ContexualRule[][] RULE_ASSOCIATIONS = {
			{ new ContexualRule("alistically", B), new ContexualRule("arizability", A),
					new ContexualRule("izationally", B) },
			{ new ContexualRule("antialness", A), new ContexualRule("arisations", A),
					new ContexualRule("arizations", A), new ContexualRule("entialness", A) },
			{ new ContexualRule("allically", C), new ContexualRule("antaneous", A), new ContexualRule("antiality", A),
					new ContexualRule("arisation", A), new ContexualRule("arization", A),
					new ContexualRule("ationally", B), new ContexualRule("ativeness", A),
					new ContexualRule("eableness", E), new ContexualRule("entations", A),
					new ContexualRule("entiality", A), new ContexualRule("entialize", A),
					new ContexualRule("entiation", A), new ContexualRule("ionalness", A),
					new ContexualRule("istically", A), new ContexualRule("itousness", A),
					new ContexualRule("izability", A), new ContexualRule("izational", A) },
			{ new ContexualRule("ableness", A), new ContexualRule("arizable", A), new ContexualRule("entation", A),
					new ContexualRule("entially", A), new ContexualRule("eousness", A),
					new ContexualRule("ibleness", A), new ContexualRule("icalness", A),
					new ContexualRule("ionalism", A), new ContexualRule("ionality", A),
					new ContexualRule("ionalize", A), new ContexualRule("iousness", A),
					new ContexualRule("izations", A), new ContexualRule("lessness", A) },
			{ new ContexualRule("ability", A), new ContexualRule("aically", A), new ContexualRule("alistic", B),
					new ContexualRule("alities", A), new ContexualRule("ariness", E), new ContexualRule("aristic", A),
					new ContexualRule("arizing", A), new ContexualRule("ateness", A), new ContexualRule("atingly", A),
					new ContexualRule("ational", B), new ContexualRule("atively", A), new ContexualRule("ativism", A),
					new ContexualRule("elihood", E), new ContexualRule("encible", A), new ContexualRule("entally", A),
					new ContexualRule("entials", A), new ContexualRule("entiate", A), new ContexualRule("entness", A),
					new ContexualRule("fulness", A), new ContexualRule("ibility", A), new ContexualRule("icalism", A),
					new ContexualRule("icalist", A), new ContexualRule("icality", A), new ContexualRule("icalize", A),
					new ContexualRule("ication", G), new ContexualRule("icianry", A), new ContexualRule("ination", A),
					new ContexualRule("ingness", A), new ContexualRule("ionally", A), new ContexualRule("isation", A),
					new ContexualRule("ishness", A), new ContexualRule("istical", A), new ContexualRule("iteness", A),
					new ContexualRule("iveness", A), new ContexualRule("ivistic", A), new ContexualRule("ivities", A),
					new ContexualRule("ization", F), new ContexualRule("izement", A), new ContexualRule("oidally", A),
					new ContexualRule("ousness", A) },
			{ new ContexualRule("aceous", A), new ContexualRule("acious", B), new ContexualRule("action", G),
					new ContexualRule("alness", A), new ContexualRule("ancial", A), new ContexualRule("ancies", A),
					new ContexualRule("ancing", B), new ContexualRule("ariser", A), new ContexualRule("arized", A),
					new ContexualRule("arizer", A), new ContexualRule("atable", A), new ContexualRule("ations", B),
					new ContexualRule("atives", A), new ContexualRule("eature", Z), new ContexualRule("efully", A),
					new ContexualRule("encies", A), new ContexualRule("encing", A), new ContexualRule("ential", A),
					new ContexualRule("enting", C), new ContexualRule("entist", A), new ContexualRule("eously", A),
					new ContexualRule("ialist", A), new ContexualRule("iality", A), new ContexualRule("ialize", A),
					new ContexualRule("ically", A), new ContexualRule("icance", A), new ContexualRule("icians", A),
					new ContexualRule("icists", A), new ContexualRule("ifully", A), new ContexualRule("ionals", A),
					new ContexualRule("ionate", D), new ContexualRule("ioning", A), new ContexualRule("ionist", A),
					new ContexualRule("iously", A), new ContexualRule("istics", A), new ContexualRule("izable", E),
					new ContexualRule("lessly", A), new ContexualRule("nesses", A), new ContexualRule("oidism", A) },
			{ new ContexualRule("acies", A), new ContexualRule("acity", A), new ContexualRule("aging", B),
					new ContexualRule("aical", A), new ContexualRule("alist", A), new ContexualRule("alism", B),
					new ContexualRule("ality", A), new ContexualRule("alize", A), new ContexualRule("allic", BB),
					new ContexualRule("anced", B), new ContexualRule("ances", B), new ContexualRule("antic", C),
					new ContexualRule("arial", A), new ContexualRule("aries", A), new ContexualRule("arily", A),
					new ContexualRule("arity", B), new ContexualRule("arize", A), new ContexualRule("aroid", A),
					new ContexualRule("ately", A), new ContexualRule("ating", I), new ContexualRule("ation", B),
					new ContexualRule("ative", A), new ContexualRule("ators", A), new ContexualRule("atory", A),
					new ContexualRule("ature", E), new ContexualRule("early", Y), new ContexualRule("ehood", A),
					new ContexualRule("eless", A), new ContexualRule("elity", A), new ContexualRule("ement", A),
					new ContexualRule("enced", A), new ContexualRule("ences", A), new ContexualRule("eness", E),
					new ContexualRule("ening", E), new ContexualRule("ental", A), new ContexualRule("ented", C),
					new ContexualRule("ently", A), new ContexualRule("fully", A), new ContexualRule("ially", A),
					new ContexualRule("icant", A), new ContexualRule("ician", A), new ContexualRule("icide", A),
					new ContexualRule("icism", A), new ContexualRule("icist", A), new ContexualRule("icity", A),
					new ContexualRule("idine", I), new ContexualRule("iedly", A), new ContexualRule("ihood", A),
					new ContexualRule("inate", A), new ContexualRule("iness", A), new ContexualRule("ingly", B),
					new ContexualRule("inism", J), new ContexualRule("inity", CC), new ContexualRule("ional", A),
					new ContexualRule("ioned", A), new ContexualRule("ished", A), new ContexualRule("istic", A),
					new ContexualRule("ities", A), new ContexualRule("itous", A), new ContexualRule("ively", A),
					new ContexualRule("ivity", A), new ContexualRule("izers", F), new ContexualRule("izing", F),
					new ContexualRule("oidal", A), new ContexualRule("oides", A), new ContexualRule("otide", A),
					new ContexualRule("ously", A) },
			{ new ContexualRule("able", A), new ContexualRule("ably", A), new ContexualRule("ages", B),
					new ContexualRule("ally", B), new ContexualRule("ance", B), new ContexualRule("ancy", B),
					new ContexualRule("ants", B), new ContexualRule("aric", A), new ContexualRule("arly", K),
					new ContexualRule("ated", I), new ContexualRule("ates", A), new ContexualRule("atic", B),
					new ContexualRule("ator", A), new ContexualRule("ealy", Y), new ContexualRule("edly", E),
					new ContexualRule("eful", A), new ContexualRule("eity", A), new ContexualRule("ence", A),
					new ContexualRule("ency", A), new ContexualRule("ened", E), new ContexualRule("enly", E),
					new ContexualRule("eous", A), new ContexualRule("hood", A), new ContexualRule("ials", A),
					new ContexualRule("ians", A), new ContexualRule("ible", A), new ContexualRule("ibly", A),
					new ContexualRule("ical", A), new ContexualRule("ides", L), new ContexualRule("iers", A),
					new ContexualRule("iful", A), new ContexualRule("ines", M), new ContexualRule("ings", N),
					new ContexualRule("ions", B), new ContexualRule("ious", A), new ContexualRule("isms", B),
					new ContexualRule("ists", A), new ContexualRule("itic", H), new ContexualRule("ized", F),
					new ContexualRule("izer", F), new ContexualRule("less", A), new ContexualRule("lily", A),
					new ContexualRule("ness", A), new ContexualRule("ogen", A), new ContexualRule("ward", A),
					new ContexualRule("wise", A), new ContexualRule("ying", B), new ContexualRule("yish", A) },
			{ new ContexualRule("acy", A), new ContexualRule("age", B), new ContexualRule("aic", A),
					new ContexualRule("als", BB), new ContexualRule("ant", B), new ContexualRule("ars", O),
					new ContexualRule("ary", F), new ContexualRule("ata", A), new ContexualRule("ate", A),
					new ContexualRule("eal", Y), new ContexualRule("ear", Y), new ContexualRule("ely", E),
					new ContexualRule("ene", E), new ContexualRule("ent", C), new ContexualRule("ery", E),
					new ContexualRule("ese", A), new ContexualRule("ful", A), new ContexualRule("ial", A),
					new ContexualRule("ian", A), new ContexualRule("ics", A), new ContexualRule("ide", L),
					new ContexualRule("ied", A), new ContexualRule("ier", A), new ContexualRule("ies", P),
					new ContexualRule("ily", A), new ContexualRule("ine", M), new ContexualRule("ing", N),
					new ContexualRule("ion", Q), new ContexualRule("ish", C), new ContexualRule("ism", B),
					new ContexualRule("ist", A), new ContexualRule("ite", AA), new ContexualRule("ity", A),
					new ContexualRule("ium", A), new ContexualRule("ive", A), new ContexualRule("ize", F),
					new ContexualRule("oid", A), new ContexualRule("one", R), new ContexualRule("ous", A) },
			{ new ContexualRule("ae", A), new ContexualRule("al", BB), new ContexualRule("ar", X),
					new ContexualRule("as", B), new ContexualRule("ed", E), new ContexualRule("en", F),
					new ContexualRule("es", E), new ContexualRule("ia", A), new ContexualRule("ic", A),
					new ContexualRule("is", A), new ContexualRule("ly", B), new ContexualRule("on", S),
					new ContexualRule("or", T), new ContexualRule("um", U), new ContexualRule("us", V),
					new ContexualRule("yl", R), new ContexualRule("'s", A), new ContexualRule("s'", A) },
			{ new ContexualRule("a", A), new ContexualRule("e", A), new ContexualRule("i", A),
					new ContexualRule("o", A), new ContexualRule("s", W), new ContexualRule("y", B) } };

	private static final String[] DOUBLE_ENDINGS = { "bb", "dd", "gg", "ll", "mm", "nn", "pp", "rr", "ss", "tt" };

	// {ending, replacement, delimited can't replace characters*}
	private static final String[][] TRANSFORMATIONS = { { "iev", "ief" }, { "uct", "uc" }, { "umpt", "um" },
			{ "rpt", "rb" }, { "urs", "ur" }, { "istr", "ister" }, { "metr", "meter" }, { "olv", "olut" },
			{ "ul", "l", "a,o,i" }, { "bex", "bic" }, { "dex", "dic" }, { "pex", "pic" }, { "tex", "tic" },
			{ "ax", "ac" }, { "ex", "ec" }, { "ix", "ic" }, { "lux", "luc" }, { "uad", "uas" }, { "vad", "vas" },
			{ "cid", "cis" }, { "lid", "lis" }, { "erid", "eris" }, { "pand", "pans" }, { "end", "ens", "s" },
			{ "ond", "ons" }, { "lud", "lus" }, { "rud", "rus" }, { "her", "hes", "p,t" }, { "mit", "mis" },
			{ "ent", "ens", "m" }, { "ert", "ers" }, { "et", "es", "n" }, { "yt", "ys" }, { "yz", "ys" }
	};
	
	private static final String TRANSFORMATION_DELIMITER = ",";
	private static final int MIN_DOUBLE_LENGTH = 2;
	private static final int MIN_STEM_LENGTH = 2;
	private static final int MAX_SUFFIX_LENGTH = 11;

	private String removeSuffix(String word) {
		if (word.length() >= MIN_STEM_LENGTH) {
			int suffixLength = MAX_SUFFIX_LENGTH + 1;
			for (ContexualRule[] associations : RULE_ASSOCIATIONS) {
				suffixLength--;
				if (word.length() - suffixLength < MIN_STEM_LENGTH) { // word - suffix  < stem length
					continue;
				}
				for (ContexualRule association : associations) {
					word = association.applyTest(word);
					if (association.isTestSuccessful()) {
						return word;
					}
				}
			}
		}
		return word;
	}

	private String undouble(String word) {
		if (word.length() < MIN_DOUBLE_LENGTH) {
			return word;
		}
		
		for (String ending : DOUBLE_ENDINGS) {
			if (word.endsWith(ending)) {
				word = removeEnding(word, 1);
				break;
			}
		}
		return word;
	}
	
	private String respell(String word) {
		for (String[] options : TRANSFORMATIONS) {
			String ending = options[0];
			String replacement = options[1];

			if (word.endsWith(ending)) {
				String stem = removeEnding(word, ending.length());

				if (options.length == 2) {
					word = stem + replacement;
					break;
				} else {
					boolean pass = true;
					for (String cantEndStr : options[2].split(TRANSFORMATION_DELIMITER)) {
						if (stem.endsWith(cantEndStr)) {
							pass = false;
							break;
						}
					}
					if (pass) {
						word = stem + replacement;
						break;
					}
				}
			}
		}
		return word;
	}
	
	private static boolean isMatching(String word, String[] regexOptions) {
		if (isEmpty(regexOptions)) {
			return true;
		}

		for (String regex : regexOptions) {
			if (word.matches(regex)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEmpty(String[] array) {
		return array == null || array.length == 0;
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = removeSuffix(word);
		word = undouble(word);
		word = respell(word);
		return word;
	}
}