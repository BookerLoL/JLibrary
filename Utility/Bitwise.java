
public class Bitwise {
	/*
	 * 1 & 1 -> 1 1 & 0 -> 0 0 & 0 -> 0
	 */
	public static int and(int left, int right) {
		return left & right;
	}

	/*
	 * 1 | 1 -> 1 1 | 0 -> 1 0 | 0 -> 0
	 */
	public static int or(int left, int right) {
		return left | right;
	}

	/*
	 * 1 ^ 1 -> 0 1 ^ 0 -> 1 0 ^ 0 -> 0
	 */
	public static int exor(int left, int right) {
		return left ^ right;
	}

	/*
	 * Shift all bits by shift amount to the left
	 * 
	 * 0011 << 2 -> 1100 1100 << 2 -> 0000
	 */
	public static int leftShift(int num, int shiftAmt) {
		return num << shiftAmt;
	}

	public static int rightShift(int num, int shiftAmt) {
		return num >> shiftAmt;
	}

	/*
	 * flip all bits ~ 10101100 -> 01010011
	 */
	public static int complement(int num) {
		return ~num;
	}

	public static boolean isBitSet(int num, int pos) {
		return (num & (1 << pos - 1)) > 0;
	}

	public static int setBit(int num, int pos) {
		return num | (1 << pos - 1);
	}

	public static int clearBit(int num, int pos) {
		return num & ~(1 << pos - 1);
	}

	public static int toggleBit(int num, int pos) {
		return num ^ (1 << pos - 1);
	}

	public static int toggleRightMostOneBit(int num) {
		return num & num - 1;
	}

	public static int isolateRightMostOneBit(int num) {
		return num & -num;
	}

	public static int isolateRightMostZeroBit(int num) {
		return ~num & num + 1;
	}

	public static void main(String[] args) {
		byte b1 = 3;
		byte b2 = 12;
		System.out.println(Bitwise.or(b1, b2));
		System.out.println(Bitwise.isBitSet(3, b2));
	}
}
