package main.collections.Array;

public class ParallelArray {
	Object[][] arrays;

	public ParallelArray() {
		this(null);
	}

	public ParallelArray(Object[][] objects) {
		checkIsValidInputAndUpdate(objects);
	}

	private void checkIsValidInputAndUpdate(Object[][] objects) {
		if (objects == null || objects.length == 0) {
			arrays = new Object[0][0];
		} else {
			final int expectedSize = objects[0].length;
			for (Object[] ary : objects) {
				if (ary.length != expectedSize) {
					throw new IllegalArgumentException("Every array must be the same length");
				}
			}
			arrays = objects;
		}
	}

	public int categories() {
		return arrays.length;
	}

	public int lengthOfCategory() {
		return arrays[0].length;
	}

	public void setArrays(Object[][] newObjects) {
		checkIsValidInputAndUpdate(newObjects);
	}

	public Object[] getArray(int row) {
		if (isNotValidRowIndex(row)) {
			throw new IndexOutOfBoundsException();
		}
		return arrays[row];
	}

	public Object getArrayItem(int row, int col) {
		if (isNotValidRowIndex(row) || isNotValidColIndex(row, col)) {
			throw new IndexOutOfBoundsException();
		}
		return arrays[row][col];
	}

	private boolean isNotValidRowIndex(int index) {
		return index < 0 || index >= arrays.length;
	}

	private boolean isNotValidColIndex(int row, int col) {
		return col < 0 || col >= arrays[row].length;
	}

	public void setArrayRow(int row, Object[] newRecord) {
		if (isNotValidRowIndex(row)) {
			throw new IndexOutOfBoundsException();
		}

		if (newRecord == null || newRecord.length != arrays[row].length) {
			throw new IllegalArgumentException("Every array must be the same length");
		}

		arrays[row] = newRecord;
	}

	public void setArrayItem(int row, int col, Object o) {
		if (isNotValidRowIndex(row) || isNotValidColIndex(row, col)) {
			throw new IndexOutOfBoundsException();
		}
		arrays[row][col] = o;
	}
}
