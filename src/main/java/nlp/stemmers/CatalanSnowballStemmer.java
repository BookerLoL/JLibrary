package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Catalan
 * 
 * Source: https://snowballstem.org/algorithms/catalan/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class CatalanSnowballStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'à', 'á', 'e', 'é', 'è', 'i', 'í', 'ï', 'o', 'ó', 'ò', 'u', 'ú', 'ü' };
	private static final String[] STEP1_SUFFIXES1 = { "selas", "selos", "-les", "-nos", "sela", "selo", "'hi", "'ho",
			"'ls", "-ls", "-la", "-li", "vos", "nos", "-us", "'ns", "-ns", "-me", "-te", "los", "las", "les", "ens",
			"'s", "'l", "se", "us", "'n", "-n", "'m", "-m", "'t", "li", "lo", "me", "le", "la", "ho", "hi" };

	private static final String[] STEP2_SUFFIXES1 = { "acions", "ades", "ada" };
	private static final String[] STEP2_SUFFIXES2 = { "allengües", "ativitats", "bilitats", "ionistes", "ialistes",
			"ialismes", "ativitat", "atòries", "isament", "bilitat", "ivitats", "ionisme", "ionista", "ialista",
			"ialisme", "íssimes", "formes", "ivisme", "aments", "ificar", "idores", "ancies", "atòria", "ivitat",
			"encies", "ències", "atives", "íssima", "íssims", "ictes", "eries", "itats", "itzar", "ament", "ments",
			"sfera", "ícies", "àries", "cions", "ístic", "issos", "íssem", "íssiu", "issem", "isseu", "ísseu", "dores",
			"adura", "ívola", "ables", "adors", "idors", "adora", "doras", "dures", "ancia", "toris", "encia", "ència",
			"ïtats", "atius", "ativa", "ibles", "asses", "assos", "íssim", "ìssem", "ìsseu", "ìssin", "ismes", "istes",
			"inies", "íinia", "ínies", "trius", "atge", "icte", "ells", "ella", "essa", "eres", "ines", "able", "itat",
			"ives", "ment", "amen", "iste", "aire", "eria", "eses", "esos", "ícia", "icis", "ícis", "ària", "alla",
			"nces", "enca", "issa", "dora", "dors", "bles", "ívol", "egar", "ejar", "itar", "ació", "ants", "tori",
			"ions", "isam", "ores", "aris", "ïtat", "atiu", "ible", "assa", "ents", "imes", "isme", "ista", "inia",
			"ites", "triu", "oses", "osos", "ient", "otes", "ell", "esc", "ets", "eta", "ers", "ina", "iva", "ius",
			"fer", "als", "era", "ana", "esa", "ici", "íci", "ció", "nça", "dor", "all", "enc", "osa", "ble", "dís",
			"dur", "ant", "ats", "ota", "ors", "ora", "ari", "uts", "uds", "ent", "ims", "ima", "ita", "ots", "ar",
			"és", "ès", "et", "ls", "ió", "ot", "al", "or", "il", "ís", "ós", "ud", "ó" };
	private static final String[] STEP2_SUFFIXES3 = { "logíeslogia", "lógiques", "logies", "lógica", "lógics", "logía",
			"logis", "logi" };
	private static final String STEP2_SUFFIXES3_REPLACEMENT = "log";
	private static final String[] STEP2_SUFFIXES4 = { "iques", "ics", "ica", "ic" };
	private static final String STEP2_SUFFIXES4_REPLACEMENT = "ic";
	private static final String[] STEP2_SUFFIXES5 = { "quíssimes", "quíssims", "quíssima", "quíssim" };
	private static final String STEP2_SUFFIXES5_REPLACEMENT = "c";
	private static final String[] STEP3_SUFFIXES1 = { "aríamos", "eríamos", "iríamos", "eresseu", "iéramos", "iésemos",
			"adores", "aríais", "aremos", "eríais", "eremos", "iríais", "iremos", "ierais", "ieseis", "asteis",
			"isteis", "ábamos", "áramos", "ásemos", "isquen", "esquin", "esquis", "esques", "esquen", "ïsquen",
			"ïsques", "adora", "adors", "arían", "arías", "arian", "arien", "aries", "aréis", "erían", "erías", "eréis",
			"erass", "irían", "irías", "iréis", "asseu", "esseu", "àsseu", "àssem", "àssim", "àssiu", "essen", "esses",
			"assen", "asses", "assim", "assiu", "éssen", "ésseu", "éssim", "éssiu", "éssem", "aríem", "aríeu", "eixer",
			"eixes", "ieran", "iesen", "ieron", "iendo", "essin", "essis", "assin", "assis", "essim", "èssim", "èssiu",
			"ieras", "ieses", "abais", "arais", "aseis", "íamos", "irien", "iries", "irìem", "irìeu", "iguem", "igueu",
			"esqui", "eixin", "eixis", "eixen", "iríem", "iríeu", "atges", "issen", "isses", "issin", "issis", "issiu",
			"issim", "ïssin", "íssiu", "íssim", "ïssis", "ïguem", "ïgueu", "ïssen", "ïsses", "itzeu", "itzis", "ador",
			"ents", "udes", "eren", "arán", "arás", "aria", "aràs", "aría", "arés", "erán", "erás", "ería", "erau",
			"irán", "irás", "iría", "írem", "íreu", "aves", "avem", "ávem", "àvem", "àveu", "áveu", "aven", "ares",
			"àrem", "àreu", "àren", "areu", "aren", "tzar", "ides", "ïdes", "ades", "iera", "iese", "aste", "iste",
			"aban", "aran", "asen", "aron", "abas", "adas", "idas", "aras", "ases", "íais", "ados", "idos", "amos",
			"imos", "ques", "iran", "irem", "iren", "ires", "ireu", "iria", "iràs", "eixi", "eixo", "isin", "isis",
			"esca", "isca", "ïsca", "ïren", "ïres", "ïxen", "ïxes", "ixen", "ixes", "inin", "inis", "ineu", "itza",
			"itzi", "itzo", "itzà", "arem", "ent", "arà", "ará", "ara", "aré", "erá", "eré", "irá", "iré", "íeu", "ies",
			"íem", "ìeu", "ien", "uda", "ava", "ats", "ant", "ïen", "ams", "ïes", "dre", "eix", "ïda", "aba", "ada",
			"ida", "its", "ids", "ase", "ían", "ado", "ido", "ieu", "ess", "ass", "ías", "áis", "ira", "irà", "irè",
			"sis", "sin", "int", "isc", "ïsc", "ïra", "ïxo", "ixo", "ixa", "ini", "itz", "iïn", "iïs", "re", "ie", "er",
			"ia", "at", "ut", "au", "ïm", "ïu", "és", "en", "es", "em", "am", "ïa", "it", "ït", "ía", "ad", "ed", "id",
			"an", "ió", "ar", "ir", "as", "ii", "io", "ià", "ís", "ïx", "ix", "in", "às", "iï", "í" };
	private static final String STEP3_SUFFIXES2 = "ando";
	private static final String[] STEP4_SUFFIXES1 = { "itz", "os", "eu", "iu", "is", "ir", "ïn", "ïs", "it", "a", "o",
			"á", "à", "í", "ó", "e", "é", "i", "s", "ì", "ï" };
	private static final String STEP4_SUFFIXES2 = "iqu";
	private static final String STEP4_SUFFIXES2_REPLACEMENT = "ic";

	private int R1;
	private int R2;

	@Override
	public String stem(String word) {
		word = normalize(word);
		markRegions(word);
		word = step1(word);
		String prevWord = word;
		word = step2(word);
		if (prevWord.equals(word)) {
			word = step3(word);
		}
		word = step4(word);
		return clean(word);
	}

	private void markRegions(String word) {
		R1 = calcR1VC(word, VOWELS);
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);

		for (String suffix : STEP1_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}
		return word;
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
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
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP2_SUFFIXES3_REPLACEMENT;
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES4) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP2_SUFFIXES4_REPLACEMENT;
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES5) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + STEP2_SUFFIXES5_REPLACEMENT;
				}
				return word;
			}
		}
		return word;
	}

	private String step3(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP3_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		if (getRegionSubstring(word, R2).endsWith(STEP3_SUFFIXES2)) {
			word = removeEnding(word, STEP3_SUFFIXES2.length());
		}
		return word;
	}

	private String step4(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP4_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		if (R1String.endsWith(STEP4_SUFFIXES2)) {
			word = removeEnding(word, STEP4_SUFFIXES2.length()) + STEP4_SUFFIXES2_REPLACEMENT;
		}
		return word;
	}

	private String clean(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'á' || ch == 'à') {
				sb.append('a');
			} else if (ch == 'é' || ch == 'è') {
				sb.append('e');
			} else if (ch == 'í' || ch == 'ì' || ch == 'ï') {
				sb.append('i');
			} else if (ch == 'ó' || ch == 'ò') {
				sb.append('o');
			} else if (ch == 'ú' || ch == 'ü') {
				sb.append('u');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public Language getLanguage() {
		return Language.CATALAN;
	}
}