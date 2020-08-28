package utility;

public class MemoryGrowth {
	public static int doubleSize(int length) {
		return length == 0 ? 16 : length << 1;
	}

	public static int slowGrowth(int length) {
		return (length * 3) / 2 + 1;
	}

	public static int javaGrowth(int length) {
		return length + (length >> 1);
	}
}
