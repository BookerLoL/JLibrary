import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/*
 * One dimensional array of triples
 * - row, col, data
 * 
 * Is able to grow 
 * 
 * 
 * Doesn't check if row or column is negative
 */
public class OneDSparseMatrix<T> {
	private static final int NOT_FOUND = -1;
	final private T ignoreValue;
	final private boolean isNullAllowed;

	protected class Space implements Cloneable {
		protected int row;
		protected int col;
		protected T data;

		public Space(T data, int row, int col) {
			this.row = row;
			this.col = col;
			this.data = data;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

		@Override
		public String toString() {
			return "( " + row + ", " + col + ", data: " + data.toString() + ")";
		}
	}

	private List<Space> oneDMatrix;

	public OneDSparseMatrix() {
		this(null, true);
	}

	public OneDSparseMatrix(T ignoreValue) {
		this(ignoreValue, true);
	}

	public OneDSparseMatrix(T ignoreValue, boolean isNullAllowed) {
		oneDMatrix = new LinkedList<>();
		this.ignoreValue = ignoreValue;
		this.isNullAllowed = isNullAllowed;
	}

	public OneDSparseMatrix(T[][] matrix, T ignoreValue) {
		this(matrix, ignoreValue, true);
	}

	public OneDSparseMatrix(OneDSparseMatrix<T> matrix, T ignoreValue, boolean isNullAllowed) {
		this(ignoreValue, isNullAllowed);
		fillMatrix(matrix);
	}

	public OneDSparseMatrix(T[][] matrix, T ignoreValue, boolean isNullAllowed) {
		this(ignoreValue, isNullAllowed);
		fillMatrix(matrix);
	}

	private void fillMatrix(T[][] matrix) {
		Objects.requireNonNull(matrix);

		for (int row = 0; row < matrix.length; row++) {
			T[] rowAry = matrix[row];
			for (int col = 0; col < rowAry.length; col++) {
				T data = rowAry[col];
				if (isValid(data)) {
					oneDMatrix.add(new Space(data, row, col));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void fillMatrix(OneDSparseMatrix<T> matrix) {
		Objects.requireNonNull(matrix);

		for (Space space : matrix.oneDMatrix) {
			if (isValid(space.data)) {
				try {
					oneDMatrix.add((OneDSparseMatrix<T>.Space) space.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
	}

	public boolean add(T data, int row, int col) {
		if (isValid(data)) {
			int insertPos = 0;
			while (insertPos < oneDMatrix.size()) {
				Space tempSpace = oneDMatrix.get(insertPos);
				if (tempSpace.row <= row) {
					if (tempSpace.row == row) {
						if (tempSpace.col == col) {
							tempSpace.data = data;
							return true;
						} else if (tempSpace.col > col) {
							break;
						}
					}
				} else {
					break;
				}
				insertPos++;
			}
			oneDMatrix.add(insertPos, new Space(data, row, col));
			return true;
		}
		return false;
	}

	public T set(T newData, int row, int col) {
		T oldData = null;
		if (isDefaultValue(newData)) {
			remove(row, col);
		} else if (isValid(newData)) {
			int index = indexOf(row, col);
			if (index != NOT_FOUND) {
				Space space = oneDMatrix.get(index);
				oldData = space.data;
				space.data = newData;
			} else {
				add(newData, row, col);
			}
		}
		return oldData;
	}

	public T remove(int row, int col) {
		int index = indexOf(row, col);
		return removeHelper(index);
	}

	public T remove(T data) {
		int index = indexOf(data);
		return removeHelper(index);
	}

	private T removeHelper(int index) {
		if (index != NOT_FOUND && index < oneDMatrix.size()) {
			return oneDMatrix.remove(index).data;
		}
		return null;
	}

	public boolean contains(T data) {
		return indexOf(data) != -1;
	}

	public boolean contains(int row, int col) {
		return indexOf(row, col) != -1;
	}

	public boolean contains(T data, int row, int col) {
		if (isValid(data)) {
			int index = indexOf(row, col);
			if (index != NOT_FOUND) {
				Space space = oneDMatrix.get(index);
				return space.data.equals(data);
			}
		}
		return false;
	}

	private int indexOf(T data) {
		if (isValid(data)) {
			for (int i = 0; i < oneDMatrix.size(); i++) {
				Space space = oneDMatrix.get(i);
				if (space.data.equals(data)) {
					return i;
				}
			}
		}
		return NOT_FOUND;
	}

	private int indexOf(int row, int col) {
		for (int i = 0; i < oneDMatrix.size(); i++) {
			Space space = oneDMatrix.get(i);
			if (row == space.row && col == space.col) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	public boolean isNullAccepted() {
		return isNullAllowed;
	}

	public T getDefaultIgnoreValue() {
		return ignoreValue;
	}

	private boolean isValid(T data) {
		return isInvalidNull(data) || !isDefaultValue(data);
	}

	private boolean isInvalidNull(T data) {
		return !isNullAccepted() && isNull(data);
	}

	private boolean isDefaultValue(T data) {
		return data.equals(getDefaultIgnoreValue());
	}

	protected boolean isNull(Object o) {
		return o == null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		oneDMatrix.stream().forEach(space -> sb.append(space));
		sb.append("]");
		return sb.toString();
	}
}
