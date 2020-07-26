package stemmers;
/*
 * UEA Stemmer
 * 
 * For English
 * 
 * https://www.uea.ac.uk/computing/word-stemming/
 * 
 */
public class UEAStemmer extends Stemmer {
	private static final String[] ignoreWords = { "is", "as", "this", "has", "was", "during" };

	private boolean isIgnoreWord(String word) {
		for (String ignoreWord : ignoreWords) {
			if (word.equals(ignoreWord)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Will handle multiple contractions in one word
	 * 
	 * Even though there really isn't a case for this, it handles it just in case
	 */
	private String handleContractions(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char prev = '\0';
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == '\'') {
				if (i + 1 < word.length()) {
					char next = word.charAt(i + 1);
					if (next == 'r' || next == 'v') {
						if (i + 2 < word.length()) {
							char afterNext = word.charAt(i + 2);
							if (afterNext == 'e') {
								sb.append(next == 'r' ? "are" : "have");
								i += 2;
								prev = afterNext;
							} else if (afterNext != '\'') {
								sb.append(next);
								sb.append(afterNext);
								i += 2;
								prev = afterNext;
							} else {
								sb.append(next);
								i++;
								prev = next;
							}
						}
					} else if (next == 'm') {
						sb.append("am");
						prev = next;
						i++;
					} else if (prev == 'n' && next == 't') {
						sb.append("ot");
						prev = next;
						i++;
					} else if (next == 's') {
						if (i + 2 < word.length()) {
							sb.append(next);
							prev = next;
						}
						i++;
					} else if (next != '\'') {
						sb.append(next);
						i++;
					}
				}
			} else {
				sb.append(ch);
				prev = ch;
			}
		}
		return sb.toString();
	}

	private String oneFullPassStem(String word) {
		if (word.endsWith("aceous")) {
			word = removeEnding(word, 6);
		} else if (word.endsWith("ces")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("cs") || word.endsWith("sis") || word.endsWith("tis") || word.endsWith("ss")
				|| word.endsWith("eed")) {
			// ignore
		} else if (word.endsWith("eeds") || word.endsWith("ued") || word.endsWith("ues") || word.endsWith("ees")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("iases") || word.endsWith("sses")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("eses")) {
			word = removeEnding(word, 2) + "is";
		} else if (word.endsWith("uses") || word.endsWith("ses") || word.endsWith("tled") || word.endsWith("pled")
				|| word.endsWith("bled")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("eled") || word.endsWith("lled") || word.endsWith("led") || word.endsWith("ened")
				|| word.endsWith("ained") || word.endsWith("erned") || word.endsWith("rned") || word.endsWith("oned")
				|| word.endsWith("gned")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("nned")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ned")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("ifted") || word.endsWith("ected")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("vided") || word.endsWith("ved") || word.endsWith("ced")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("erred") || word.endsWith("urred")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("lored") || word.endsWith("eared")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("tored")) {
			word = word.replaceFirst("ed", "e");
		} else if (word.endsWith("ered") || word.endsWith("reds")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("red") || word.endsWith("noted") || word.endsWith("leted") || word.endsWith("uted")
				|| word.endsWith("ated") || word.endsWith("anges")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("tted")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ted")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("aining") || word.endsWith("acting")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("tting")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("ttings")) {
			word = removeEnding(word, 5);
		} else if (word.endsWith("viding")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("ssed")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("ulted")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("uming")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("ssed") || word.endsWith("umed") || word.endsWith("titudes")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("fulness") || word.endsWith("ousness")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("rabed") || word.endsWith("rebed") || word.endsWith("ribed") || word.endsWith("robed")
				|| word.endsWith("rubed")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("ving")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("vings")) {
			word = word.replaceFirst("ings", "e");
		} else if (word.endsWith("bed")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("beds")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ssing") || word.endsWith("ulting") || word.endsWith("eading")
				|| word.endsWith("oading") || word.endsWith("eding") || word.endsWith("lding") || word.endsWith("rding")
				|| word.endsWith("nding")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ssings") || word.endsWith("eadings") || word.endsWith("oadings")
				|| word.endsWith("edings") || word.endsWith("ldings") || word.endsWith("rdings")
				|| word.endsWith("ndings") || word.endsWith("dding")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("ddings")) {
			word = removeEnding(word, 5);
		} else if (word.endsWith("ding")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("dings")) {
			word = word.replaceFirst("ings", "e");
		} else if (word.endsWith("lling")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("llings")) {
			word = removeEnding(word, 5);
		} else if (word.endsWith("ealing") || word.endsWith("oling") || word.endsWith("ailing")
				|| word.endsWith("eling")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ealings") || word.endsWith("olings") || word.endsWith("ailings")
				|| word.endsWith("elings")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("ling")) {
			word = removeEnding(word, 3) + "e";
		} else if (word.endsWith("lings")) {
			word = removeEnding(word, 4) + "e";
		} else if (word.endsWith("gged")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ged")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("mming")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("mmings")) {
			word = removeEnding(word, 5);
		} else if (word.endsWith("rming") || word.endsWith("lming")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ming")) {
			word = removeEnding(word, 3) + "e";
		} else if (word.endsWith("mings")) {
			word = removeEnding(word, 4) + "e";
		} else if (word.endsWith("gging")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("ggings")) {
			word = removeEnding(word, 5);
		} else if (word.endsWith("ging")) {
			word = removeEnding(word, 3);
			if (!word.endsWith("ng")) {
				word += "e";
			}
		} else if (word.endsWith("gings")) {
			word = removeEnding(word, 4);
			if (!word.endsWith("ng")) {
				word += "e";
			}
		} else if (word.endsWith("aning") || word.endsWith("ening") || word.endsWith("gning") || word.endsWith("oning")
				|| word.endsWith("rning")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("nning")) {
			word = removeEnding(word, 4);
		} else if (word.endsWith("ning")) {
			word = removeEnding(word, 3) + "e";
		} else if (word.endsWith("tings")) {
			word = removeEnding(word, 4);
			if (!(word.endsWith("st") || word.endsWith("et") || word.endsWith("nt"))) {
				word += "e";
			}
		} else if (word.endsWith("ting")) {
			word = removeEnding(word, 3);
			if (!(word.endsWith("st") || word.endsWith("et") || word.endsWith("pt") || word.endsWith("nt")
					|| word.endsWith("ct"))) {
				word += "e";
			}
		} else if (word.endsWith("ssed") || word.endsWith("lled")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("les") || word.endsWith("tes") || word.endsWith("zed")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("iring") || word.endsWith("uring") || word.endsWith("ncing") || word.endsWith("zing")
				|| word.endsWith("sing")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("irings") || word.endsWith("urings") || word.endsWith("ncings")
				|| word.endsWith("sings")) {
			word = word.replaceFirst("ings", "e");
		} else if (word.endsWith("lling")) {
			word = removeEnding(word, 3);
		} else if (word.endsWith("ied")) {
			word = word.replaceFirst("ied", "y");
		} else if (word.endsWith("ating")) {
			word = word.replaceFirst("ing", "e");
		} else if (word.endsWith("thing")) {
			// do nothing
		} else if (word.endsWith("things")) {
			word = removeEnding(word, 1);
		} else if (word.matches(".*\\w\\wings?$")) {
			removeDuplicateCharacter(word, "ing");
		} else if (word.endsWith("ies")) {
			word = word.replaceFirst("ies", "y");
		} else if (word.endsWith("lves")) {
			word = word.replaceFirst("ves", "f");
		} else if (word.endsWith("ves") || word.endsWith("aped") || word.endsWith("uded") || word.endsWith("oded")
				|| word.endsWith("ated")) {
			word = removeEnding(word, 1);
		} else if (word.matches(".*\\w\\weds?$")) {
			word = removeDuplicateCharacter(word, "ed");
		} else if (word.endsWith("pes") || word.endsWith("mes") || word.endsWith("ones") || word.endsWith("izes")
				|| word.endsWith("ures") || word.endsWith("ines") || word.endsWith("ides") || word.endsWith("ges")) {
			word = removeEnding(word, 1);
		} else if (word.endsWith("es")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("is")) {
			word = word.replaceFirst("is", "e");
		} else if (word.endsWith("ous") || word.endsWith("ums") || word.endsWith("us")) {
			// do nothing
		} else if (word.endsWith("s")) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String removeDuplicateCharacter(String word, String remove) {
		if (word.endsWith("s"))
			remove = remove.concat("s");
		String stemmedWord = removeEnding(word, remove.length());
		if (stemmedWord.matches(".*(\\w)\\1$")) {
			stemmedWord = removeEnding(stemmedWord, 1);
		}
		return stemmedWord;
	}
	
	@Override
	public String stem(String word) {
		word = normalize(word); // because i normalize it to lowercase, I skip all the uppercase rules
		if (isIgnoreWord(word)) {
			return word;
		}
		word = handleContractions(word);
		word = oneFullPassStem(word);
		return word;
	}
}
