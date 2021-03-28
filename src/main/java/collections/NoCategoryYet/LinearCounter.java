package main.collections.NoCategoryYet;

//Impelmentation of a possible linear counter, a probablistic data structure
public class LinearCounter<T> {
	public interface Hasher<T> {
		long hash(T object);
	}
	
	private byte[] map;
	private int totalBytes;
	private long unusedBits;
	private long capacity;
	private Hasher<T> hasher;

	public LinearCounter(int size, T hasher) {
		totalBytes = size;
		capacity = totalBytes * 8; // 8 bits per byte
		unusedBits = capacity; 
		map = new byte[size];
	}
	
	//Should probably use Murmur Hash
	public boolean add(T object) {
		int bits = getBits(object);
		int byteIndex = bits / 8;
		byte b = map[byteIndex];
		byte bitMask = (byte) (1 << (byteIndex % 8));
		if ((b & bitMask) == 0) {
			map[byteIndex] = (byte) (b | bitMask);
			unusedBits--;
			return true;
		}
		return false;
	}
	
	public boolean remove(T object) {
		int bits = getBits(object);
		int byteIndex = bits / 8;
		byte b = map[byteIndex];
		byte bitMask = (byte) (1 << (byteIndex % 8));
		if ((b & bitMask) == 1) {
			map[byteIndex] = (byte) (b ^ bitMask);
			unusedBits++;
			return true;
		}
		return false;
	}
	
	private int getBits(T object) {
		return (int) ((hasher.hash(object) & 0xFFFFFFFFL) & (capacity()));
	}

	public boolean isEmpty() {
		return unusedBits == 0;
	}

	public long capacity() {
		return map.length * 8;
	}

	public long getUsedCount() {
		return capacity() - unusedBits;
	}

	public long getUnusedCount() {
		return unusedBits;
	}
	
	//cardinality = -m ln Vn
	//Vn = unsued / m
	//m = capacity
	public long cardinality() {
		return (long) Math.round(-capacity * Math.log(unusedBits / (double) capacity));
	}

	public byte[] getMapping() {
		return map;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.round(-16 * Math.log(12 / 16.0)));
	}
}
