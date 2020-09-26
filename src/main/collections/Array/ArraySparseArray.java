package main.collections.Array;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import main.util.Memory;

/**
 * 
 * @author Ethan
 */
public class ArraySparseArray<T> {
	private static final int NOT_FOUND_INDEX = -1;
	private static final int LOW_COUNT_RATIO = 4;
	private final Item<T> NULL_ITEM = new Item<>(NOT_FOUND_INDEX, NOT_FOUND_INDEX, null);

	private static class Item<T> {
		protected int row;
		protected int col;
		protected T value;

		public Item(int row, int col, T value) {
			this.row = row;
			this.col = col;
			this.value = value;
		}

		public boolean isRowAndColMatch(int row, int col) {
			return row == this.row && col == this.col;
		}

		public String toString() {
			return "[" + row + ", " + col + ", " + value + "]";
		}
	}

	private int count;
	private Item<T>[] sparseMatrix;

	@SuppressWarnings("unchecked")
	public ArraySparseArray(int initSize) {
		sparseMatrix = Arrays.copyOf(new Object[initSize], initSize, Item[].class);
	}

	@SuppressWarnings("unchecked")
	public ArraySparseArray(T[][] simpleMatrix, T ignoreValue) {
		sparseMatrix = (Item<T>[]) getMatrixItems(simpleMatrix, ignoreValue).toArray();
		count = sparseMatrix.length;
	}

	private List<Item<T>> getMatrixItems(T[][] simpleMatrix, T ignoreValue) {
		List<Item<T>> items = new LinkedList<>();
		for (int row = 0; row < simpleMatrix.length; row++) {
			for (int col = 0; col < simpleMatrix[row].length; col++) {
				if (!simpleMatrix[row][col].equals(ignoreValue)) {
					items.add(new Item<T>(row, col, simpleMatrix[row][col]));
				}
			}
		}
		return items;
	}

	public boolean add(int row, int col, T o) {
		int insertIndex = getIndex(row, col);
		if (setHelper(row, col, o, insertIndex) != NULL_ITEM) {
			return true;
		}
		return addHelper(row, col, o, insertIndex);
	}

	private boolean addHelper(int row, int col, T o, int insertIndex) {
		if (isFull()) {
			grow();
		}

		if (insertIndex == NOT_FOUND_INDEX) {
			insertIndex = count;
		}

		shiftItemsRight(insertIndex);
		sparseMatrix[insertIndex] = new Item<T>(row, col, o);
		count++;
		return true;
	}

	private boolean isFull() {
		return count == sparseMatrix.length;
	}

	private void grow() {
		sparseMatrix = Arrays.copyOf(sparseMatrix, Memory.doubleSize(sparseMatrix.length));
	}

	private void shiftItemsRight(int index) {
		for (int i = count; i > index; i--) {
			sparseMatrix[i] = sparseMatrix[i - 1];
		}
	}

	public T remove(T value) {
		return removeHelper(getIndex(value));
	}

	public T remove(int row, int col) {
		return removeHelper(getIndex(row, col));
	}

	private T removeHelper(int index) {
		if (index == NOT_FOUND_INDEX) {
			return null;
		}
		T value = sparseMatrix[index].value;
		shiftItemsLeft(index);
		count--;

		if (isLow()) {
			shrink();
		}
		return value;
	}

	private void shiftItemsLeft(int index) {
		for (int i = index; i < count - 1; i++) {
			sparseMatrix[i] = sparseMatrix[i + 1];
		}
		sparseMatrix[count - 1] = null;
	}

	private boolean isLow() {
		return count == sparseMatrix.length / LOW_COUNT_RATIO;
	}

	private void shrink() {
		sparseMatrix = Arrays.copyOf(sparseMatrix, sparseMatrix.length / (LOW_COUNT_RATIO - 1));
	}

	public T set(int row, int col, T o) {
		int index = getIndex(row, col);
		Item<T> item = setHelper(row, col, o, index);
		if (item == NULL_ITEM) {
			addHelper(row, col, o, index);
		}
		return item.value;
	}

	private Item<T> setHelper(int row, int col, T o, int index) {
		if (index != NOT_FOUND_INDEX && sparseMatrix[index].isRowAndColMatch(row, col)) {
			Item<T> prev = sparseMatrix[index];
			sparseMatrix[index] = new Item<>(row, col, o);
			return prev;
		}
		return NULL_ITEM;
	}

	private int getIndex(int row, int col) {
		for (int i = 0; i < count; i++) {
			if (sparseMatrix[i].row > row || (sparseMatrix[i].row == row && sparseMatrix[i].col >= col)) {
				return i;
			}
		}
		return NOT_FOUND_INDEX;
	}

	private int getIndex(T o) {
		if (o != null) {
			for (int i = 0; i < count; i++) {
				if (o.equals(sparseMatrix[i].value)) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < count; i++) {
				if (sparseMatrix[i].value == o) {
					return i;
				}
			}
		}
		return NOT_FOUND_INDEX;
	}

	public T get(int row, int col) {
		return getItem(row, col).value;
	}

	private Item<T> getItem(int row, int col) {
		if (!isValidBounds(row, col)) {
			return NULL_ITEM;

		}
		int index = getIndex(row, col);
		return isValidIndex(index, row, col) ? sparseMatrix[index] : NULL_ITEM;
	}

	private boolean isValidBounds(int row, int col) {
		return row >= 0 && col >= 0;
	}

	private boolean isValidIndex(int index, int row, int col) {
		return index != NOT_FOUND_INDEX && sparseMatrix[index].isRowAndColMatch(row, col);
	}

	public boolean contains(T o) {
		return getIndex(o) != NOT_FOUND_INDEX;
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public String toString() {
		return Arrays.toString(sparseMatrix);
	}
}