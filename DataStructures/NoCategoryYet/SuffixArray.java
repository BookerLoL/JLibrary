package august_2020;

import java.util.Arrays;

public class SuffixArray {
	private class Pair {
		int index;
		String substring;

		public Pair(int index, String substring) {
			this.index = index;
			this.substring = substring;
		}
	}

	private String current;
	private int[] suffixArray;

	public SuffixArray(String str) {
		current = str;
		constructSuffixArray();
	}

	private void constructSuffixArray() {
		Pair[] pairList = new Pair[current.length()];
		for (int i = 0; i < current.length(); i++) {
			pairList[i] = new Pair(i, current.substring(i));
		}

		Arrays.sort(pairList, (p1, p2) -> p1.substring.compareTo(p2.substring));

		suffixArray = new int[pairList.length];

		for (int i = 0; i < pairList.length; i++) {
			suffixArray[i] = pairList[i].index;
		}
	}

	//Only gives 1 indexw
	public int indexOfSubstring(String substring) {
		int low = 0;
		int high = suffixArray.length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			
			String currSub = current.substring(suffixArray[mid]);
			System.out.println(currSub);
			if (currSub.startsWith(substring)) {
				return suffixArray[mid];
			}
			
			int compareResult = substring.compareTo(currSub);
			if (compareResult < 0) {
				high = mid - 1;
			} else if (compareResult > 0) {
				low = mid + 1;
			} else {
				return suffixArray[mid];
			}
		}
		
		return -1;
	}

	public void setString(String str) {
		current = str;
		constructSuffixArray();
	}

	public static void main(String[] args) {
		SuffixArray sa = new SuffixArray("gcatcgc");
		System.out.println(sa.indexOfSubstring("gc"));
	}
}
