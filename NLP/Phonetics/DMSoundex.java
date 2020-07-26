package phonetics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Daitch-Mokotoff Soundex
 * 
 * For Standard English Names and European Jewish names
 * 
 * https://www.jewishgen.org/InfoFiles/soundex.html#DM
 * 
 * The algorithm was rather complex so I tried to make it was straight forward as possible.
 * An important note is that, if there is a "split" (encountered a special character with two encodings) then
 * there will be multiple encodings to look at to determine if they are phonetically the same since the default encoding returned will be the first item in the list.
 * 
 * These two methods will need to be used unless you are fine with what was given by the encoding.
 *  hasAlternatives()
 *  getAlternatives() 
 *  
 *  
 * 
 */
public class DMSoundex extends Phonetizer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u' };
	private static final int MAX_LENGTH = 6;
	private static final int NC = -1;
	private static final char PAD = '0';

	//Helper class to represent the rules 
	private static class EncodingRow {
		final String[] alternatives;
		final int startValue;
		final int vowelValue;
		final int otherValue;
		final EncodingRow[] specialCases;

		public EncodingRow(String[] alternatives, int startValue, int vowelValue, int otherValue,
				EncodingRow[] specialCases) {
			this.alternatives = alternatives;
			this.startValue = startValue;
			this.vowelValue = vowelValue;
			this.otherValue = otherValue;
			this.specialCases = specialCases;
		}

		public boolean hasSpecialCases() {
			return specialCases != null && specialCases.length > 0;
		}

		public EncodingRow[] getSpecialCases() {
			return specialCases;
		}

		public int getStartValue() {
			return startValue;
		}

		public int getBeforeVowelValue() {
			return vowelValue;
		}

		public int getAnyOtherSituationValue() {
			return otherValue;
		}

		public String[] getAlternatives() {
			return alternatives;
		}
	}

	private static final EncodingRow EMPTY = new EncodingRow(new String[] { "\0" }, NC, NC, NC, null); //Made a default value to void null checks
	private static final EncodingRow AI = new EncodingRow(new String[] { "ai", "aj", "ay" }, 0, 1, NC, null);
	private static final EncodingRow AU = new EncodingRow(new String[] { "au" }, 0, 7, NC, null);
	private static final EncodingRow AOGONEK = new EncodingRow(new String[] { "ą" }, NC, NC, 6, new EncodingRow[] {});
	private static final EncodingRow A = new EncodingRow(new String[] { "a" }, 0, NC, NC, null);
	private static final EncodingRow B = new EncodingRow(new String[] { "b" }, 7, 7, 7, null);
	private static final EncodingRow CHS = new EncodingRow(new String[] { "chs" }, 5, 54, 54, null);
	private static final EncodingRow K = new EncodingRow(new String[] { "k" }, 5, 5, 5, null);
	private static final EncodingRow KH = new EncodingRow(new String[] { "kh" }, 5, 5, 5, null);
	private static final EncodingRow TCH = new EncodingRow(new String[] { "ttsch", "ttch", "tch" }, 4, 4, 4, null);
	private static final EncodingRow TSK = new EncodingRow(new String[] { "tsk" }, 45, 45, 45, null);
	private static final EncodingRow CH = new EncodingRow(new String[] { "ch" }, NC, NC, NC, new EncodingRow[] { KH, TCH });
	private static final EncodingRow CK = new EncodingRow(new String[] { "ck" }, NC, NC, NC, new EncodingRow[] { K, TSK });
	private static final EncodingRow CZ = new EncodingRow(new String[] { "csz", "czs", "cs", "cz" }, 4, 4, 4, null);
	private static final EncodingRow TZ = new EncodingRow(new String[] { "ttz", "tzs", "tsz", "tz" }, 4, 4, 4, null);
	private static final EncodingRow C = new EncodingRow(new String[] { "c" }, NC, NC, NC, new EncodingRow[] { K, TZ });
	private static final EncodingRow DRZ = new EncodingRow(new String[] { "drz", "drs" }, 4, 4, 4, null);
	private static final EncodingRow DS = new EncodingRow(new String[] { "dsh", "dsz", "ds" }, 4, 4, 4, null);
	private static final EncodingRow DZ = new EncodingRow(new String[] { "dzh", "dzs", "dz" }, 4, 4, 4, null);
	private static final EncodingRow D = new EncodingRow(new String[] { "dt", "d" }, 3, 3, 3, null);
	private static final EncodingRow EI = new EncodingRow(new String[] { "ei", "ej", "ey" }, 0, 1, NC, null);
	private static final EncodingRow EU = new EncodingRow(new String[] { "eu" }, 1, 1, NC, null);
	private static final EncodingRow EOGONEK = new EncodingRow(new String[] { "ę" }, NC, NC, 6, new EncodingRow[] {});
	private static final EncodingRow E = new EncodingRow(new String[] { "e" }, 0, NC, NC, null);
	private static final EncodingRow FB = new EncodingRow(new String[] { "fb" }, 7, 7, 7, null);
	private static final EncodingRow F = new EncodingRow(new String[] { "f" }, 7, 7, 7, null);
	private static final EncodingRow G = new EncodingRow(new String[] { "g" }, 5, 5, 5, null);
	private static final EncodingRow H = new EncodingRow(new String[] { "h" }, 5, 5, NC, null);
	private static final EncodingRow IA = new EncodingRow(new String[] { "ia", "ie", "io", "iu" }, 1, NC, NC, null);
	private static final EncodingRow I = new EncodingRow(new String[] { "i" }, 0, NC, NC, null);
	private static final EncodingRow Y = new EncodingRow(new String[] { "y" }, 1, NC, NC, null);
	private static final EncodingRow DZH = new EncodingRow(new String[] { "dzh" }, 4, 4, 4, null);
	private static final EncodingRow J = new EncodingRow(new String[] { "j" }, NC, NC, NC, new EncodingRow[] { Y, DZH });
	private static final EncodingRow KS = new EncodingRow(new String[] { "ks" }, 5, 54, 54, null);
	private static final EncodingRow L = new EncodingRow(new String[] { "l" }, 8, 8, 8, null);
	private static final EncodingRow MN = new EncodingRow(new String[] { "mn" }, NC, 66, 66, null);
	private static final EncodingRow M = new EncodingRow(new String[] { "m" }, 6, 6, 6, null);
	private static final EncodingRow NM = new EncodingRow(new String[] { "nm" }, NC, 66, 66, null);
	private static final EncodingRow N = new EncodingRow(new String[] { "n" }, 6, 6, 6, null);
	private static final EncodingRow OI = new EncodingRow(new String[] { "oi", "oj", "oy" }, 0, 1, NC, null);
	private static final EncodingRow O = new EncodingRow(new String[] { "o" }, 0, NC, NC, null);
	private static final EncodingRow P = new EncodingRow(new String[] { "pf", "ph", "p" }, 7, 7, 7, null);
	private static final EncodingRow Q = new EncodingRow(new String[] { "q" }, 5, 5, 5, null);
	private static final EncodingRow RTZ = new EncodingRow(new String[] { "rtz" }, 94, 94, 94, null);
	private static final EncodingRow ZH = new EncodingRow(new String[] { "zsch", "zsh", "zs", "zh" }, 4, 4, 4, null);
	private static final EncodingRow RZ = new EncodingRow(new String[] { "rz" }, NC, NC, NC, new EncodingRow[] { RTZ, ZH });
	private static final EncodingRow RS = new EncodingRow(new String[] { "rs" }, NC, NC, NC, new EncodingRow[] { RTZ, ZH });
	private static final EncodingRow R = new EncodingRow(new String[] { "r" }, 9, 9, 9, null);
	private static final EncodingRow SCHTSCH = new EncodingRow(new String[] { "schtsch", "schtsh", "schtch" }, 2, 4, 4, null);
	private static final EncodingRow SCH = new EncodingRow(new String[] { "sch" }, 4, 4, 4, null);
	private static final EncodingRow SHTCH = new EncodingRow(new String[] { "shtsh", "shtch", "shch" }, 2, 4, 4, null);
	private static final EncodingRow SHT = new EncodingRow(new String[] { "scht", "schd", "sht" }, 2, 43, 43, null);
	private static final EncodingRow SH = new EncodingRow(new String[] { "sh" }, 4, 4, 4, null);
	private static final EncodingRow STCH = new EncodingRow(new String[] { "stsch", "stch", "sc" }, 2, 4, 4, null);
	private static final EncodingRow STRZ = new EncodingRow(new String[] { "strs", "stsh", "strz" }, 2, 4, 4, null);
	private static final EncodingRow ST = new EncodingRow(new String[] { "st" }, 2, 43, 43, null);
	private static final EncodingRow SZCZ = new EncodingRow(new String[] { "szcs", "szcz" }, 2, 4, 4, null);
	private static final EncodingRow SZT = new EncodingRow(new String[] { "szt", "shd", "szd", "sd" }, 2, 43, 43, null);
	private static final EncodingRow SZ = new EncodingRow(new String[] { "sz" }, 4, 4, 4, null);
	private static final EncodingRow S = new EncodingRow(new String[] { "s" }, 4, 4, 4, null);
	private static final EncodingRow TH = new EncodingRow(new String[] { "th" }, 3, 3, 3, null);
	private static final EncodingRow TRZ = new EncodingRow(new String[] { "trs", "trz" }, 4, 4, 4, null);
	private static final EncodingRow TSCH = new EncodingRow(new String[] { "tsch", "tsh" }, 4, 4, 4, null);
	private static final EncodingRow TS = new EncodingRow(new String[] { "ttsz", "tts", "tc", "ts" }, 4, 4, 4, null);
	// Selected 4 just because I decided so based on two options to choose from. [3  or 4]
	private static final EncodingRow TCEDILLA = new EncodingRow(new String[] { "ţ" }, 4, 4, 4, new EncodingRow[] {});
	private static final EncodingRow T = new EncodingRow(new String[] { "t" }, 3, 3, 3, null);
	private static final EncodingRow UI = new EncodingRow(new String[] { "ui", "uj", "uy" }, 0, 1, NC, null);
	private static final EncodingRow U = new EncodingRow(new String[] { "ue", "u" }, 0, NC, NC, null);
	private static final EncodingRow V = new EncodingRow(new String[] { "v" }, 7, 7, 7, null);
	private static final EncodingRow W = new EncodingRow(new String[] { "w" }, 7, 7, 7, null);
	private static final EncodingRow X = new EncodingRow(new String[] { "x" }, 5, 54, 54, null);
	private static final EncodingRow ZDZ = new EncodingRow(new String[] { "zhdzh", "zdzh", "zdz" }, 2, 4, 4, null);
	private static final EncodingRow ZD = new EncodingRow(new String[] { "zhd", "zd" }, 2, 43, 43, null);
	private static final EncodingRow Z = new EncodingRow(new String[] { "z" }, 4, 4, 4, null);

	/*
	 * Needed a structure that would maintain a previous encoding value for that specific StringBuilder 
	 * to properly add the correct encoding values
	 */
	private class EncodingHelper {
		StringBuilder sb;
		int prevEncodingValue;

		public EncodingHelper(StringBuilder sb, int previousEncodingValue) {
			this.sb = sb;
			prevEncodingValue = previousEncodingValue;
		}

		public int getPrevEncodingValue() {
			return prevEncodingValue;
		}

		public int getLength() {
			return sb.length();
		}

		public StringBuilder getBuilder() {
			return sb;
		}

		private boolean isSpecialEncodingRow() {
			return prevUsed == NM || prevUsed == MN;
		}

		public boolean append(int encodingValue) {
			if (getLength() < MAX_LENGTH) {
				if (prevEncodingValue != encodingValue || isSpecialEncodingRow()) {
					if (!isIgnoreValue(encodingValue)) {
						sb.append(encodingValue);

						if (getLength() > MAX_LENGTH) { //ensures length never exceeds the max length
							sb.setLength(MAX_LENGTH);
						}
						return true;
					}
				}
				prevEncodingValue = encodingValue;
			}
			return false;
		}

		public List<EncodingHelper> split(int[] encodingValues) {
			List<EncodingHelper> encoding = new LinkedList<>();
			if (encodingValues == null) {
				encoding.add(this);
			} else {
				for (int encodingValue : encodingValues) {
					EncodingHelper clone = new EncodingHelper(new StringBuilder(getBuilder()), getPrevEncodingValue());
					if (clone.append(encodingValue)) {
						encoding.add(clone);
					}
				}
			}

			return encoding;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof EncodingHelper)) {
				return false;
			}
			EncodingHelper eh = (EncodingHelper) o;
			return eh.getBuilder().compareTo(this.getBuilder()) == 0;
		}
	}

	private List<String> alternatives = new LinkedList<>();
	private boolean specialAllowed;
	private int index;
	private String name;
	private EncodingRow prevUsed;
	private EncodingRow currUsed;
	private Map<Character, EncodingRow[]> mappings;

	public DMSoundex(boolean includeSpecialCharacters) {
		specialAllowed = includeSpecialCharacters;
		initMapOfEncodingRows();
	}

	public DMSoundex() {
		this(false);
	}

	/*
	 * Order matters for the values in the arrays and the order of
	 * alternatives within an EncodingRow object matters too.
	 * 
	 * Don't want a situation like:  "s" being accpeted when "schtsch" would be the best fit as it's the longest matching rule
	 */
	private void initMapOfEncodingRows() {
		mappings = new HashMap<>(29);
		mappings.put('a', new EncodingRow[] { AI, AU, AOGONEK, A });
		mappings.put('b', new EncodingRow[] { B });
		mappings.put('c', new EncodingRow[] { CHS, CH, CK, CZ, C });
		mappings.put('d', new EncodingRow[] { DRZ, DS, DZ, D });
		mappings.put('e', new EncodingRow[] { EI, EU, EOGONEK, E });
		mappings.put('f', new EncodingRow[] { FB, F });
		mappings.put('g', new EncodingRow[] { G });
		mappings.put('h', new EncodingRow[] { H });
		mappings.put('i', new EncodingRow[] { IA, I });
		mappings.put('j', new EncodingRow[] { J });
		mappings.put('k', new EncodingRow[] { KS, KH, K });
		mappings.put('l', new EncodingRow[] { L });
		mappings.put('m', new EncodingRow[] { MN, M });
		mappings.put('n', new EncodingRow[] { NM, N });
		mappings.put('o', new EncodingRow[] { OI, O });
		mappings.put('p', new EncodingRow[] { P });
		mappings.put('q', new EncodingRow[] { Q });
		mappings.put('r', new EncodingRow[] { RZ, RS, R });
		mappings.put('s', new EncodingRow[] { SCHTSCH, SCH, SHTCH, SHT, SH, STCH, STRZ, ST, SZCZ, SZT, SZ, S });
		mappings.put('t', new EncodingRow[] { TCH, TH, TRZ, TSCH, TS, TZ, TCEDILLA, T });
		mappings.put('u', new EncodingRow[] { UI, U });
		mappings.put('v', new EncodingRow[] { V });
		mappings.put('w', new EncodingRow[] { W });
		mappings.put('x', new EncodingRow[] { X });
		mappings.put('y', new EncodingRow[] { Y });
		mappings.put('z', new EncodingRow[] { ZDZ, ZD, ZH, Z });
	}
	
	private static boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}
	
	private char getTranslated(char ch) {
		if (specialAllowed) {
			if (ch == 'ą') {
				ch = 'a';
			} else if (ch == 'ę') {
				ch = 'e';
			} else if (ch == 'ţ') {
				ch = 't';
			}
		}
		return ch;
	}

	private boolean isIgnoreValue(int encodeValue) {
		return encodeValue <= NC;
	}

	private static String padZeros(StringBuilder sb) {
		while (sb.length() < MAX_LENGTH) {
			sb.append(PAD);
		}
		return sb.toString();
	}

	private int getEncodingValue(EncodingRow eRow, int nextIndex) {
		int encodeValue = NC;
		if (index == 0) {
			encodeValue = eRow.getStartValue();
		} else if ((nextIndex < name.length()) && isVowel(name.charAt(nextIndex))) {
			encodeValue = eRow.getBeforeVowelValue();
		} else {
			encodeValue = eRow.getAnyOtherSituationValue();
		}
		return encodeValue;
	}
	
	private static <T> boolean hasNoValues(T[] encodingValues) {
		return encodingValues == null || encodingValues.length == 0;
	}

	private int[] getEncodeMapping() {
		char translatedCurr = getTranslated(name.charAt(index));
		EncodingRow[] encodings = mappings.get(translatedCurr);
		int[] encodedValues = null;
		
		if (hasNoValues(encodings)) { //no encoding for that character, move on to next char
			encodedValues = new int[] { NC };
			index++;
		} else {
			outer: for (EncodingRow eRow : encodings) {
				String[] alternatives = eRow.getAlternatives();
				for (String option : alternatives) {
					if (name.startsWith(option, index)) {
						//System.out.println(option);
						if (eRow.hasSpecialCases()) {
							EncodingRow[] specialCases = eRow.getSpecialCases();
							encodedValues = new int[specialCases.length];
							for (int i = 0; i < specialCases.length; i++) {
								encodedValues[i] = getEncodingValue(specialCases[i], index + option.length());
							}
						} else {
							encodedValues = new int[] { getEncodingValue(eRow, index + option.length()) };
						}
						currUsed = eRow;
						index += option.length();
						break outer;
					}
				}
			}
		}
		return encodedValues;
	}

	@Override
	public String encode(String name) {
		name = name.toLowerCase();
		this.name = name;
		alternatives.clear();
		List<EncodingHelper> alts = new LinkedList<>();
		EncodingHelper original = new EncodingHelper(new StringBuilder(MAX_LENGTH), NC);
		prevUsed = EMPTY;
		currUsed = EMPTY;
		index = 0;
		while (index < this.name.length()) {
			int[] encodings = getEncodeMapping();
			if (alts.isEmpty()) {
				if (encodings.length > 1) { //special case encountered, must split
					alts.addAll(original.split(encodings));
				} else {
					original.append(encodings[0]);
				}
			} else { // already splitted, so must process all of them
				if (encodings.length > 1) {
					List<EncodingHelper> removedList = new LinkedList<>();
					for (int i = 0, initialSize = alts.size(); i < initialSize; i++) {
						EncodingHelper currEncoding = alts.get(i);
						alts.addAll(currEncoding.split(encodings));
						alts.remove(i);
						removedList.add(currEncoding);
						i--; // must adjust index and size if removed
						initialSize--;
					}

					// Add back non-duplicates
					removedList.forEach(removed -> {
						if (!alts.contains(removed)) {
							alts.add(removed);
						}
					});
				} else {
					alts.forEach(encodingHelper -> encodingHelper.append(encodings[0]));
				}
			}
			prevUsed = currUsed;
		}

		if (!alts.isEmpty()) {
			alternatives = alts.stream().map(sbAlts -> padZeros(sbAlts.getBuilder())).collect(Collectors.toList());
			return alternatives.get(0); //by default, returns the first one in the list
		} else {
			return padZeros(original.getBuilder());
		}
	}

	public boolean hasAlternatives() {
		return !alternatives.isEmpty();
	}

	// Returns a list of alternatives based on the most recent encoding, includes the encoded value
	public List<String> getAlternatives() {
		return alternatives;
	}
}
