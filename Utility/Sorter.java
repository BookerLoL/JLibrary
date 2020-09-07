import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Sorter {
	public static void main(String[] args) {
		while (true) {
			Random rand = new Random();
			Integer[] ary = new Integer[300];
			for (int i = 0; i < ary.length; i++) {
				ary[i] = Math.abs(rand.nextInt() % 1000);
			}

			Sorter.heapSort(ary);
			boolean result = Sorter.isSorted(ary);
			//System.out.println(Arrays.toString(ary));
			if (!result) {
				System.out.println(Arrays.toString(ary));
				break;
			}
		}
	}

	private static void swap(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	public static <T extends Comparable<? super T>> void bubbleSort(T[] array) {
		bubbleSort(array, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void bubbleSort(T[] array, Comparator<T> comp) {
		boolean swapped = true;
		for (int lengthToCheck = array.length - 1; lengthToCheck > 0; lengthToCheck--) {
			if (swapped) {
				swapped = false;
				for (int i = 0; i < lengthToCheck; i++) {
					if (comp.compare(array[i + 1], array[i]) < 0) {
						swap(array, i, i + 1);
						swapped = true;
					}
				}
			} else {
				break;
			}
		}
	}

	public static <T extends Comparable<? super T>> void selectionSort(T[] array) {
		selectionSort(array, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void selectionSort(T[] array, Comparator<T> comp) {
		for (int i = 0; i < array.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < array.length; j++) {
				if (comp.compare(array[j], array[minIndex]) < 0) {
					minIndex = j;
				}
			}

			if (minIndex != i) {
				swap(array, i, minIndex);
			}
		}
	}

	public static <T extends Comparable<? super T>> void insertionSort(T[] array) {
		insertionSort(array, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void insertionSort(T[] array, Comparator<T> comp) {
		for (int i = 1; i < array.length; i++) {
			T currElem = array[i];
			int j = i;
			while (j > 0 && comp.compare(currElem, array[j - 1]) < 0) {
				array[j] = array[j - 1];
				j--;
			}
			array[j] = currElem;
		}
	}

	public static <T extends Comparable<? super T>> void shellSort(T[] array) {
		shellSort(array, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void shellSort(T[] array, Comparator<T> comp) {
		int gapNum = 1, numGaps = 1;
		for (; gapNum < array.length / 3; gapNum = 3 * gapNum + 1, numGaps++);
	
		int[] gaps = new int[numGaps];
		for (; numGaps > 0; gapNum /= 3, numGaps--) {
			gaps[gaps.length - numGaps] = gapNum;
		}
		
		shellSort(array, comp, gaps);
	}

	public static <T extends Comparable<? super T>> void shellSort(T[] array, Comparator<T> comp, int[] gaps) {
		int i, j;
		for (int gap : gaps) {
			for (i = gap; i < array.length; i++) {
				T temp = array[i];
				for (j = i; j >= gap && comp.compare(temp, array[j - gap]) < 0; j -= gap) {
					array[j] = array[j - gap];
				}
				array[j] = temp;
			}
		}
	}

	public static <T extends Comparable<? super T>> void mergeSort(T[] array) {
		mergeSort(array, array.length, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void mergeSort(T[] array, Comparator<T> comp) {
		mergeSort(array, array.length, comp);
	}

	private static <T extends Comparable<? super T>> void mergeSort(T[] array, int length, Comparator<T> comp) {
		if (length < 2) {
			return;
		}

		int mid = length / 2;
		T[] left = Arrays.copyOfRange(array, 0, mid);
		T[] right = Arrays.copyOfRange(array, mid, length);
		mergeSort(left, mid, comp);
		mergeSort(right, length - mid, comp);
		merge(array, left, mid, right, length - mid, comp);
	}

	private static <T extends Comparable<? super T>> void merge(T[] array, T[] left, int leftLength, T[] right,
			int rightLength, Comparator<T> comp) {
		int lIndex = 0, rIndex = 0, aIndex = 0;
		while (lIndex < leftLength && rIndex < rightLength) {
			if (comp.compare(left[lIndex], right[rIndex]) <= 0) {
				array[aIndex++] = left[lIndex++];
			} else {
				array[aIndex++] = right[rIndex++];
			}
		}

		while (lIndex < leftLength) {
			array[aIndex++] = left[lIndex++];
		}

		while (rIndex < rightLength) {
			array[aIndex++] = right[rIndex++];
		}
	}

	public static <T extends Comparable<? super T>> void quickSort(T[] array) {
		quickSort(array, 0, array.length, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void quickSort(T[] array, Comparator<T> comp) {
		quickSort(array, 0, array.length, comp);
	}

	private static <T extends Comparable<? super T>> void quickSort(T[] array, int start, int end, Comparator<T> comp) {
		if (end - start <= 0) {
			return;
		}

		int pivotIndex = (int) ((end - start) * Math.random()) + start;
		swap(array, pivotIndex, end - 1);
		boolean iMoving = true;
		int i = start, j = end - 1;
		while (i < j) {
			if (comp.compare(array[i], array[j]) > 0) {
				swap(array, i, j);
				iMoving = !iMoving;
			} else {
				if (iMoving) {
					i++;
				} else {
					j--;
				}
			}
		}
		quickSort(array, start, i, comp);
		quickSort(array, i + 1, end, comp);
	}
	
	public static <T extends Comparable<? super T>> void heapSort(T[] array) {
		heapSort(array, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> void heapSort(T[] array, Comparator<T> comp) {
		PriorityQueue<T> pq = new PriorityQueue<>(array.length, comp); //uses priority heap
		for (T elem : array) {
			pq.add(elem);
		}
		
		for (int i = 0; i < array.length; i++) {
			array[i] = pq.remove();
		}
	}

	public static <T extends Comparable<? super T>> boolean isSorted(T[] ary) {
		return isSorted(ary, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> boolean isSorted(T[] ary, Comparator<T> comp) {
		if (ary.length <= 1) {
			return true;
		}

		for (int i = 1; i < ary.length; i++) {
			if (comp.compare(ary[i], ary[i - 1]) < 0) {
				return false;
			}
		}

		return true;
	}
}
