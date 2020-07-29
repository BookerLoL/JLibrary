import java.util.List;

//Examples of how to convert data for machine learning
public class MLDataConverter {
	public static double ordinalNormalized(Range<Double> normalize, double totalCategories, double dataValue) {
		double dataPercent = dataValue / totalCategories;
		double normalizeRange = normalize.high() - normalize.low();
		return (dataPercent * normalizeRange) + normalize.low();
	}

	public static double ordinalDenormalized(Range<Double> normalize, double totalCategories, double normalizedValue) {
		double normalizeRange = normalize.high() - normalize.low();
		return ((normalizedValue - normalize.low()) / normalizeRange) * totalCategories;
	}

	public static double quantitativeNormalized(Range<Double> data, Range<Double> normalize, double dataValue) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		double dataPercent = (dataValue - data.low()) / dataRange;
		return (dataPercent * normalizeRange) + normalize.low();
	}

	public static double quantitativeDenormalized(Range<Double> data, Range<Double> normalize, double normalizedValue) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		return (((normalizedValue - normalize.low()) / normalizeRange) * dataRange) + data.low();
	}

	// always between -1.0 and 1.0
	public static double reciprocalNormalized(double dataValue) {
		return 1.0 / dataValue;
	}

	public static double reciprocolDemonoralized(double normalizedValue) {
		return 1.0 / normalizedValue;
	}

	public static int[] oneHotEncoder(List<String> categories, String value) {
		int[] oneHotEncoder = new int[categories.size()];
		oneHotEncoder[categories.indexOf(value)] = 1;
		return oneHotEncoder;
	}

	public static String oneHotDecoder(List<String> categories, int[] oneHotEncoder) {
		for (int i = 0; i < oneHotEncoder.length; i++) {
			if (oneHotEncoder[i] == 1) {
				return categories.get(i);
			}
		}
		return null;
	}

	public static int integerEncoding(List<String> categories, String value) {
		return categories.indexOf(value);
	}

	public static String intergerDecoding(List<String> categories, int value) {
		return categories.get(value);
	}
}
