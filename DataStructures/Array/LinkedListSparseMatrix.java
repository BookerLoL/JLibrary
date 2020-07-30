import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * LinkedList approach to sparse array
 * 
 * Can grow
 * 
 * Doesn't check the row and col values
 */
public class LinkedListSparseMatrix<T> {
	private static final int NOT_FOUND = -1;
	final private T ignoreValue;
	final private boolean isNullAllowed;

	protected class Node {
		protected int col;
		protected T data;

		public Node(T data, int col) {
			this.col = col;
			this.data = data;
		}

		public int getCol() {
			return col;
		}

		public T getData() {
			return data;
		}

		public void set(T data) {
			this.data = data;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}

	protected static class Index {
		public static Index INVALID_INDEX = new Index(NOT_FOUND, NOT_FOUND);

		private final int row;
		private final int col;

		public Index(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
	}

	List<List<Node>> matrix;

	private LinkedListSparseMatrix(T ignoreValue, boolean isNullAllowed) {
		this.ignoreValue = ignoreValue;
		this.isNullAllowed = isNullAllowed;
	}

	public LinkedListSparseMatrix(T[][] matrix) {
		this(matrix, null, false);
	}

	public LinkedListSparseMatrix(T[][] matrix, T ignoreValue) {
		this(matrix, ignoreValue, false);
	}

	public LinkedListSparseMatrix(T[][] matrix, T ignoreValue, boolean isNullAllowed) {
		this(ignoreValue, isNullAllowed);
		fillMatrix(matrix);
	}

	public LinkedListSparseMatrix(LinkedListSparseMatrix<T> matrix) {
		this(matrix, null, false);
	}

	public LinkedListSparseMatrix(LinkedListSparseMatrix<T> matrix, T ignoreValue) {
		this(matrix, ignoreValue, false);
	}

	public LinkedListSparseMatrix(LinkedListSparseMatrix<T> matrix, T ignoreValue, boolean isNullAllowed) {
		this(ignoreValue, isNullAllowed);
		fillMatrix(matrix);
	}

	private void fillMatrix(T[][] matrix) {
		if (isNull(matrix)) {
			return;
		}

		this.matrix = new ArrayList<>(matrix.length);
		for (int row = 0; row < matrix.length; row++) {
			T[] rowInfo = matrix[row];
			List<Node> matrixRow = new LinkedList<>();
			for (int col = 0; col < rowInfo.length; col++) {
				if (isValid(matrix[row][col])) {
					matrixRow.add(new Node(matrix[row][col], col));
				}
			}
			this.matrix.add(matrixRow);
		}
	}

	@SuppressWarnings("unchecked")
	private void fillMatrix(LinkedListSparseMatrix<T> matrix) {
		if (isNull(matrix)) {
			return;
		}

		this.matrix = new ArrayList<>(matrix.getRowLength());
		for (int row = 0; row < matrix.getRowLength(); row++) {
			List<Node> rowInfo = matrix.getRow(row);
			List<Node> matrixRow = new LinkedList<>();
			for (int col = 0; col < rowInfo.size(); col++) {
				Node info = rowInfo.get(col);
				if (isValid(info.getData())) {
					try {
						matrixRow.add((LinkedListSparseMatrix<T>.Node) info.clone());
					} catch (CloneNotSupportedException e) {
						continue;
					}
				}
			}
			this.matrix.add(matrixRow);
		}
	}

	private List<Node> getRow(int row) {
		if (row >= 0 && row < getRowLength()) {
			return matrix.get(row);
		}
		return null;
	}

	private Index indexOf(T data) {
		if (isValid(data)) {
			for (int row = 0; row < getRowLength(); row++) {
				List<Node> rowInfo = getRow(row);
				for (int col = 0; col < rowInfo.size(); col++) {
					Node info = rowInfo.get(col);
					if (info.getData().equals(data)) {
						return new Index(row, col);
					}
				}
			}
		}
		return Index.INVALID_INDEX;
	}

	private Index indexOf(int row, int col) {
		if (row >= 0 && row < getRowLength()) {
			List<Node> rowInfo = getRow(row);
			for (int colIdx = 0; colIdx < rowInfo.size(); colIdx++) {
				Node info = rowInfo.get(colIdx);
				if (info.getCol() == col) {
					return new Index(row, colIdx);
				}
			}
		}
		return Index.INVALID_INDEX;
	}

	public boolean add(T data, int row, int col) {
		if (isValid(data)) {
			List<Node> rowInfo = getRow(row);
			int insertPos = 0;
			while (insertPos < rowInfo.size()) {
				Node info = rowInfo.get(insertPos);
				if (col <= info.getCol()) {
					if (col == info.getCol()) {
						info.set(data);
						return true;
					} else {
						break;
					}
				}
			}
			rowInfo.add(insertPos, new Node(data, col));
			return true;
		}
		return false;
	}
	
	public T set(T newData, int row, int col) {
		T oldData = null;
		if (isDefaultValue(newData)) {
			remove(row, col);
		} else if (isValid(newData)) {
			Index index = indexOf(row, col);
			if (index != Index.INVALID_INDEX) {
				Node info = getRow(index.getRow()).get(index.getCol());
				oldData = info.getData();
				info.set(newData);
			} else {
				add(newData, row, col);
			}
		}
		return oldData;
	}

	public T remove(T data) {
		Index index = indexOf(data);

		if (index != Index.INVALID_INDEX) {
			return getRow(index.getRow()).remove(index.getCol()).getData();
		}
		return null;
	}

	public T remove(int row, int col) {
		Index index = indexOf(row, col);

		if (index != Index.INVALID_INDEX) {
			return getRow(index.getRow()).remove(index.getCol()).getData();
		}
		return null;
	}

	public boolean contains(T data) {
		if (!isValid(data)) {
			return false;
		}
		Index index = indexOf(data);
		return index != Index.INVALID_INDEX;
	}

	public boolean contains(int row, int col) {
		Index index = indexOf(row, col);
		return index != Index.INVALID_INDEX;
	}

	public boolean contains(T data, int row, int col) {
		if (isValid(data)) {
			Index index = indexOf(row, col);
			Node node = get(index);
			return node != null && node.getData().equals(data);
		}
		return false;
	}

	public T get(int row, int col) {
		Index index = indexOf(row, col);
		Node node = get(index);
		return node != null ? node.getData() : null;
	}

	private Node get(Index index) {
		if (index == Index.INVALID_INDEX || index.getRow() < 0) {
			return null;
		}
		return getRow(index.getRow()).get(index.getCol());
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

	private int getRowLength() {
		return matrix.size();
	}
}
