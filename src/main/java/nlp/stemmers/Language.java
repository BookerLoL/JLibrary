package main.nlp.stemmers;

/**
 * An enum to represent popular languages by following the ISO 639 guidelines.
 * 
 * Not all languages will be supported.
 * 
 * Source: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public enum Language {
	CATALAN("ca"), DUTCH("nl"), ENGLISH("en"), GERMAN("de"), LATIN("la"), PORTUGUESE("pt"), ROMANIAN("ro"),
	SPANISH("es"), SWEDISH("sv"), ITALIAN("it"), FRENCH("fr");

	private String alpha2Code;

	private Language(String alpha2Code) {
		this.alpha2Code = alpha2Code;
	}
	
	/**
	 * Get the two letter alpha code that represents the language if there exists one.
	 * If there is none, {@code EMPTY} string will be returned.
	 * 
	 * @return the Language's associated two letter alpha code
	 */
	public String getAlpha2Code() {
		return alpha2Code;
	}
}
