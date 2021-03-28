package main.util.compression;

//Assumes only: A, T, C, G are used
//Encodes using bits A = 00, C = 01, G = 10, T = 11, uses 1 byte to store a sequence of 256 genome length string
public class Genome2Bit {
	public static final int MAX_LENGTH = 256;

	public static byte[] encode(String genomeSeq) {
		final int genomeLength = genomeSeq.length();
		checkEncodingInputLength(genomeLength);

		int numBytes = (genomeLength + 3) / 4;
		byte[] encodingBytes = new byte[numBytes + 1];
		encodingBytes[0] = encodeLength(genomeLength);

		int charIndex = 0;
		for (int i = 1; i < encodingBytes.length; i++) {
			byte encodedByte = 0;
			for (int shiftAmount = 6; shiftAmount >= 0 && charIndex < genomeLength; shiftAmount -= 2, charIndex++) {
				byte bitPair = (byte) getEncoding(genomeSeq.charAt(charIndex));
				encodedByte |= (bitPair << shiftAmount);
			}
			encodingBytes[i] = encodedByte;
		}

		return encodingBytes;
	}

	private static int getEncoding(char letter) {
		switch (letter) {
		case 'A':
			return 0; // 00
		case 'C':
			return 1; // 01
		case 'G':
			return 2; // 10
		case 'T':
			return 3; // 11
		default:
			break;
		}

		throw new IllegalArgumentException("Letter must be: A, C, G, T");
	}

	private static void checkEncodingInputLength(int genomeLength) {
		if (genomeLength < 0 || genomeLength > MAX_LENGTH) {
			throw new IllegalArgumentException("Length should be greater than 0 and at most " + Byte.MAX_VALUE
					+ "\tinput length: " + genomeLength);
		}
	}

	// Special case where length == max length is represented as 0
	private static byte encodeLength(int length) {
		return length != MAX_LENGTH ? (byte) length : 0;
	}

	// Use this if you can provide a valid encoded sequence
	public static String decode(byte[] encoding) {
		int genomeLength = decodeLength(encoding[0]);
		StringBuilder result = new StringBuilder(genomeLength);

		for (int byteIndex = 1; byteIndex < encoding.length; byteIndex++) {
			byte currByte = encoding[byteIndex];
			for (int shiftAmount = 6; shiftAmount >= 0 && result.length() < genomeLength; shiftAmount -= 2) {
				int currentBits = currByte & (0b00000011 << shiftAmount);
				int number = currentBits >> shiftAmount;
				result.append(getDecoding(number));
			}
		}

		return result.toString();
	}

	private static char getDecoding(int number) {
		switch (number) {
		case 0:
			return 'A';
		case 1:
			return 'C';
		case 2:
			return 'G';
		case 3:
			return 'T';
		default:
			break;
		}
		throw new IllegalArgumentException("Number must be between 0 and 3, inclusive\tinput: " + number);
	}

	private static int decodeLength(byte byteLength) {
		if (byteLength == 0) {
			return MAX_LENGTH;
		}

		return byteLength > 0 ? byteLength : MAX_LENGTH + byteLength;
	}

	public static void main(String[] args) {
		byte[] encoding = Genome2Bit.encode("ATAGATGCATAGCGCATAGCTAGATGTGCTAGCATGGG");
		System.out.println(Genome2Bit.decode(encoding));
	}
}
