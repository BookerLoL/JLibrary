package test.collections.Array;

import main.collections.Array.ArraySparseArray;
import main.util.Memory;
import main.util.RandomEx;

public class ArraySparseArrayTester {
	
	//Not complete
	public static void main(String[] args) {
		int max = 10, min = -10;
		int maxRow = 100, maxCol = 100;
		int size = 10000;
		ArraySparseArray<Integer> test = new ArraySparseArray<>(size);
		RandomEx rand = new RandomEx();
		for (int i = 0; i < 0; i++) {
			int r = rand.nextInt(maxRow);
			int c = rand.nextInt(maxCol);
			int val = rand.nextInt(min, max);
			//System.out.println(r + "\t" + c + "\t" + val);
			test.add(r, c, val);
		}
		for (int i = 0; i < size; i++) {
			int r = rand.nextInt(maxRow);
			int c = rand.nextInt(maxCol);
			int val = rand.nextInt(min, max);
			//System.out.println(r + "\t" + c + "\t" + val);
			test.set(r, c, val);
		}
		for (int i = 0; i < size; i++) {
			int val = rand.nextInt(min, max);
			//System.out.println(r + "\t" + c + "\t" + val);
			test.remove(val);
		}
		System.out.println(test);
	}
}
