package main.util.bits;

public class Bitwise {
	public static int and(int left, int right) {
		return left & right;
	}

	public static int or(int left, int right) {
		return left | right;
	}

	public static int exor(int left, int right) {
		return left ^ right;
	}

	public static int leftShift(int num, int shiftAmt) {
		return num << shiftAmt;
	}

	public static int rightShift(int num, int shiftAmt) {
		return num >> shiftAmt;
	}

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
}
