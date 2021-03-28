package main.ai;

import java.util.Arrays;
import java.util.List;

//Examples of how to convert data for machine learning
public class MLDataEncoderDecoder {
	public static double ordinalNormalized(Range<Double> normalize, double totalCategories, double dataValue) {
		double dataPercent = dataValue / totalCategories;
		double normalizeRange = normalize.high() - normalize.low();
		return (dataPercent * normalizeRange) + normalize.low();
	}

	public static double ordinalDenormalized(Range<Double> normalize, double totalCategories, double normalizedValue) {
		double normalizeRange = normalize.high() - normalize.low();
		return ((normalizedValue - normalize.low()) / normalizeRange) * totalCategories;
	}

	// always between -1.0 and 1.0
	public static double reciprocalNormalized(double dataValue) {
		return 1.0 / dataValue;
	}
	
	public static double reciprocolDemonoralized(double normalizedValue) {
		return reciprocalNormalized(normalizedValue);
	}
	
	public static double[] reciprocalNormalized(double[] values) {
		double[] normalized = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			normalized[i] = 1.0 / values[i];
		}
		return normalized;
	}
	
	public static double[] reciprocalDenormalized(double[] normalizedValues) {
		return reciprocalNormalized(normalizedValues);
	}

	// Will ensure that the value is between the normalized range
	public static double rangedMinMaxNormalized(Range<Double> data, Range<Double> normalize, double dataValue) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		double dataPercent = (dataValue - data.low()) / dataRange;
		return (dataPercent * normalizeRange) + normalize.low();
	}

	public static double[] rangedMinMaxNormalized(Range<Double> data, Range<Double> normalize, double[] values) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		double[] normalized = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			normalized[i] = ((values[i] - data.low()) / dataRange) * normalizeRange + normalize.low();
		}
		return normalized;
	}

	public static double rangedMinMaxDenormalized(Range<Double> data, Range<Double> normalize, double normalizedValue) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		return (((normalizedValue - normalize.low()) / normalizeRange) * dataRange) + data.low();
	}

	public static double[] rangedMinMaxDenormalized(Range<Double> data, Range<Double> normalize, double[] normalizedValues) {
		double dataRange = data.high() - data.low();
		double normalizeRange = normalize.high() - normalize.low();
		double[] denormalized = new double[normalizedValues.length];
		for (int i = 0; i < normalizedValues.length; i++) {
			denormalized[i] = (((normalizedValues[i] - normalize.low()) / normalizeRange) * dataRange) + data.low();
		}
		return denormalized;
	}

	// 0-1 normalization
	public static double[] minMaxNomralized(double[] values) {
		return minMaxNormalized(values, findMinMax(values));
	}

	private static Range<Double> findMinMax(double[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		
		double max = values[0];
		double min = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			} else if (values[i] < min) {
				min = values[i];
			}
		}
		return new Range<Double>(min, max);
	}

	public static double[] minMaxNormalized(double[] value, Range<Double> minMax) {
		double range = minMax.high() - minMax.low();
		double[] normalized = new double[value.length];
		for (int i = 0; i < normalized.length; i++) {
			normalized[i] = (value[i] - minMax.low()) / range;
		}
		return normalized;
	}

	public static double[] guassianNormalized(double[] values) {
		double sum = getSum(values);
		double mean = sum / values.length;
		
		double sumSquares = 0.0;
		for (int i = 0; i < values.length; i++) {
			sumSquares += (values[i] - mean) * (values[i] - mean);
		}
		
		//stdDev could be 0
		double stdDev = Math.sqrt(sumSquares / values.length);
		
		double[] normalized = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			normalized[i] = ( values[i] - mean ) / stdDev;
		}
		return normalized;
	}
	
	private static double getSum(double[] values) {
		double sum = 0.0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i];
		}
		return sum;
	}

	// 1-of-N, allow customization for default values and selected value ex: [-1, 1]
	public static <T> int[] oneHotEncoder(List<T> categories, String value, Range<Integer> vals) {
		int[] oneHotEncoder = new int[categories.size()];
		Arrays.fill(oneHotEncoder, vals.low());
		oneHotEncoder[categories.indexOf(value)] = vals.high();
		return oneHotEncoder;
	}

	public static <T> T oneHotDecoder(List<T> categories, int[] oneHotEncoder, Range<Integer> vals) {
		for (int i = 0; i < oneHotEncoder.length; i++) {
			if (oneHotEncoder[i] == vals.high()) {
				return categories.get(i);
			}
		}
		return null;
	}

	// difficult for neural networks to learn good weights and bias values
	public static int integerEncoding(List<String> categories, String value) {
		return categories.indexOf(value);
	}

	public static String intergerDecoding(List<String> categories, int value) {
		return categories.get(value);
	}
}
