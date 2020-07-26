package boolean_retrieval;

import java.util.List;
import java.util.stream.Collectors;

public class Normalizer {
	protected static final String EMPTY = "";
	protected static final String ALL_PUNCTUATIONS = "\\p{Punct}";
	protected static final String ALL_DIGITS = "\\p{Digit}";
	protected static final String ALL_SPACES = "\\p{Blank}";
	protected static final String ALL_WHITESPACES = "\\p{Space}";

	protected String pattern;
	protected String replacementValue;

	public Normalizer() {
		this(ALL_PUNCTUATIONS, EMPTY);
	}

	public Normalizer(String removalPattern) {
		this(removalPattern, EMPTY);
	}

	public Normalizer(String removalPattern, String replacementValue) {
		pattern = removalPattern;
	}
	
	public String getRemovalPattern() {
		return pattern;
	}
	
	public String getReplacementValue() {
		return replacementValue;
	}
	
	public void setRemovalPattern(String removalPattern) {
		pattern = removalPattern;
	}
	
	public void setReplacementValue(String replacementValue) {
		this.replacementValue = replacementValue;
	}

	protected List<String> normalize(List<String> tokens) {
		List<String> normalizedTokens = tokens.stream().map(token -> token.replaceAll(pattern, replacementValue).trim())
				.collect(Collectors.toList());
		return normalizedTokens;
	}

	protected String normalize(String token) {
		return token.replaceAll(pattern, replacementValue).trim();
	}
}
