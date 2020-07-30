package arrays;

import java.util.Arrays;

public class ArraySparseArray<T> {
	private static final int LOW_COUNT_RATIO = 4;

	protected class Item {
		protected int row;
		protected int col;
		protected T item;

		public Item(int row, int col, T item) {
			this.row = row;
			this.col = col;
			this.item = item;
		}

		public String toString() {
			return "[" + row + ", " + col + ", " + item + "]";
		}
	}

	private int count;
	private Item[] matrixInfo;

	@SuppressWarnings("unchecked")
	public ArraySparseArray(int initSize) {
		matrixInfo = Arrays.copyOf(new Object[initSize], initSize, Item[].class);
		count = 0;
	}

	@SuppressWarnings("unchecked")
	public ArraySparseArray(T[][] simpleMatrix, T ignoreValue) {
		int validElements = getValidElements(simpleMatrix, ignoreValue);
		matrixInfo = Arrays.copyOf(new Object[validElements], validElements, Item[].class);
		fillMatrix(simpleMatrix, ignoreValue, validElements);
		count = validElements;
	}

	@SuppressWarnings("unchecked")
	public ArraySparseArray(T[][] simpleMatrix, T ignoreValue, int validElements) {
		matrixInfo = Arrays.copyOf(new Object[validElements], validElements, Item[].class);
		fillMatrix(simpleMatrix, ignoreValue, validElements);
		count = validElements;
	}

	protected int getValidElements(T[][] simpleMatrix, T ignoreValue) {
		int validElements = 0;
		for (Object[] row : simpleMatrix) {
			for (Object colItem : row) {
				if (!colItem.equals(ignoreValue)) {
					validElements++;
				}
			}
		}
		return validElements;
	}

	private void fillMatrix(T[][] simpleMatrix, T ignoreValue, int validElements) {
		int count = 0;
		for (int row = 0; row < simpleMatrix.length; row++) {
			for (int col = 0; col < simpleMatrix[row].length; col++) {
				if (!simpleMatrix[row][col].equals(ignoreValue)) {
					matrixInfo[count] = new Item(row, col, simpleMatrix[row][col]);
					count++;
				}

				if (count == validElements) {
					return;
				}
			}
		}
	}

	private void shrink() {
		matrixInfo = Arrays.copyOf(matrixInfo, matrixInfo.length / (LOW_COUNT_RATIO - 1));
	}

	private void grow() {
		matrixInfo = Arrays.copyOf(matrixInfo, matrixInfo.length * 2);
	}

	private boolean isFull() {
		return count == matrixInfo.length;
	}

	private boolean isLow() {
		return count == matrixInfo.length / 4;
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	private int getIndex(int row, int col) {
		for (int i = 0; i < count; i++) {
			if (matrixInfo[i].row > row || matrixInfo[i].row == row && matrixInfo[i].col > col) {
				break;
			}

			if (matrixInfo[i].row == row && matrixInfo[i].col == col) {
				return i;
			}
		}
		return -1;
	}

	private int getIndex(T o) {
		for (int i = 0; i < count; i++) {
			if (matrixInfo[i].item.equals(o)) {
				return i;
			}
		}
		return -1;
	}

	private Item getItem(int row, int col) {
		int index = getIndex(row, col);
		return index == -1 ? null : matrixInfo[index];
	}

	public T get(int row, int col) {
		Item i = getItem(row, col);
		return i == null ? null : i.item;
	}

	public T set(int row, int col, T item) {
		Item i = getItem(row, col);
		T prevItem = null;
		if (i != null) {
			prevItem = i.item;
			i.item = item;
		}
		return prevItem;
	}

	public boolean add(int row, int col, T o) {
		if (isFull()) {
			grow();
		}

		int insertPos = findAddIndex(row, col);
		shiftItemsRight(insertPos);
		matrixInfo[insertPos] = new Item(row, col, o);
		count++;
		return true;
	}

	private int findAddIndex(int row, int col) {
		for (int i = 0; i < count; i++) {
			if (matrixInfo[count].row > row || matrixInfo[count].row == row && matrixInfo[count].col > col) {
				return i;
			}
		}
		return count;
	}

	public T remove(T item) {
		return removeHelper(getIndex(item));
	}

	public T remove(int row, int col) {
		return removeHelper(getIndex(row, col));
	}

	private T removeHelper(int index) {
		if (index == -1) {
			return null;
		}
		T item = matrixInfo[index].item;
		shiftItemsLeft(index);
		count--;

		if (isLow()) {
			shrink();
		}
		return item;
	}

	private void shiftItemsLeft(int index) {
		for (int i = index; i < count - 1; i++) {
			matrixInfo[i] = matrixInfo[i + 1];
		}
		matrixInfo[count - 1] = null;
	}

	private void shiftItemsRight(int index) {
		for (int i = count; i > index; i--) {
			matrixInfo[i] = matrixInfo[i - 1];
		}
	}

	public boolean contains(T o) {
		return getIndex(o) != -1;
	}

	public void printMatrix() {
		Arrays.asList(matrixInfo).forEach(System.out::println);
	}
}