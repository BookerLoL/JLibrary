package main.graphics;

import java.util.Arrays;

//I try to reduce extra for loops for the cost of readability
//Didn't provide all the safety checks for arguments 
public class ImmutableMatrix {
	private double[][] matrix;

	public ImmutableMatrix(int rows, int cols) {
		this(rows, cols, 0);
	}

	public ImmutableMatrix(int rows, int cols, int fillNumber) {
		matrix = new double[rows][cols];
		for (double[] row : matrix) {
			Arrays.fill(row, fillNumber);
		}
	}

	public ImmutableMatrix(double[][] values) {
		this(values, true);
	}

	private ImmutableMatrix(double[][] values, boolean copy) {
		if (copy) {
			matrix = initArray(values, true);
		} else {
			matrix = values;
		}
	}

	private double[][] initArray(double[][] values, boolean copy) {
		final int cols = values[0].length;
		double[][] newMatrix = new double[values.length][cols];

		if (copy) {
			for (int row = 0; row < values.length; row++) {
				if (values[row].length != cols) {
					throw new IllegalArgumentException("Each row should have equal length");
				}
				System.arraycopy(values[row], 0, newMatrix[row], 0, cols);
			}
		} else {
			for (int row = 0; row < values.length; row++) {
				if (values[row].length != cols) {
					throw new IllegalArgumentException("Each row should have equal length");
				}
			}
		}

		return newMatrix;
	}

	public double get(int row, int col) {
		return matrix[row][col];
	}

	public int rows() {
		return matrix.length;
	}

	public int cols() {
		return matrix[0].length;
	}

	public ImmutableMatrix add(ImmutableMatrix other) {
		checkSameRowAndCols(other);
		double[][] summedValues = initArray(matrix, false);
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				summedValues[row][col] = matrix[row][col] + other.matrix[row][col];
			}
		}
		return new ImmutableMatrix(summedValues, false);
	}

	public ImmutableMatrix subtract(ImmutableMatrix other) {
		checkSameRowAndCols(other);
		double[][] differenceValues = initArray(matrix, false);
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				differenceValues[row][col] = matrix[row][col] - other.matrix[row][col];
			}
		}
		return new ImmutableMatrix(differenceValues, false);
	}

	public ImmutableMatrix scalar(double scale) {
		double[][] scaledValues = initArray(matrix, false);
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				scaledValues[row][col] = matrix[row][col] * scale;
			}
		}
		return new ImmutableMatrix(scaledValues, false);
	}

	// https://en.wikipedia.org/wiki/Matrix_multiplication_algorithm
	// This is the simple straightforward approach
	public ImmutableMatrix multiply(ImmutableMatrix other) {
		if (matrix[0].length != other.matrix.length) {
			throw new IllegalArgumentException(
					"Current matrix's column length should be the same lenght as the other matrix rows length");
		}

		double[][] productValues = new double[matrix.length][other.matrix[0].length];
		for (int i = 0; i < productValues.length; i++) {
			for (int j = 0; j < productValues[0].length; j++) {
				double sum = 0.0;
				for (int k = 0; k < other.matrix.length; k++) {
					sum += matrix[i][k] * other.matrix[k][j];
				}
				productValues[i][j] = sum;
			}
		}

		return new ImmutableMatrix(productValues, false);
	}

	public ImmutableMatrix identityMatrix(int rows) {
		// has to be square matrix, NxN
		double[][] identityMatrix = new double[rows][rows];
		for (int row = 0; row < rows; row++) {
			identityMatrix[row][row] = 1.0;
		}
		return new ImmutableMatrix(identityMatrix, false);
	}

	public boolean isIdentityMatrix() {
		return isDigonalMatrix(true, 1.0);
	}

	public boolean isDiagonalMatrix() {
		return isDigonalMatrix(false, 0.0);
	}

	private boolean isDigonalMatrix(boolean checkDiagonal, double expectedDiagonalValue) {
		if (!isSquare()) {
			return false;
		}

		// has to be square matrix, NxN
		if (checkDiagonal) {
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix.length; col++) {
					if (row != col) {
						if (matrix[row][col] != 0.0) {
							return false;
						}
					} else {
						if (matrix[row][col] != expectedDiagonalValue) {
							return false;
						}
					}
				}
			}
		} else {
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix.length; col++) {
					if (row != col) {
						if (matrix[row][col] != 0.0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public ImmutableMatrix transpose() {
		double[][] transposeMatrix = new double[matrix[0].length][matrix.length];
		for (int col = 0; col < transposeMatrix.length; col++) {
			for (int row = 0; row < transposeMatrix[0].length; row++) {
				transposeMatrix[col][row] = matrix[row][col];
			}
		}
		return new ImmutableMatrix(transposeMatrix, false);
	}

	public boolean isSymmetric() {
		return this.equals(transpose());
	}

	private void checkSameRowAndCols(ImmutableMatrix other) {
		if (other.matrix.length != matrix.length || other.matrix[0].length != matrix[0].length) {
			throw new IllegalArgumentException("Both matrices should have the smae number of rows and columns");
		}
	}

	public boolean isSquare() {
		return matrix.length == matrix[0].length;
	}

	public boolean isColumnMatrix() {
		return matrix[0].length == 1;
	}

	public boolean isRowMatrix() {
		return matrix.length == 1;
	}

	public ImmutableMatrix clone() {
		return new ImmutableMatrix(matrix, false);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof ImmutableMatrix)) {
			return false;
		}

		ImmutableMatrix other = (ImmutableMatrix) o;

		if (other.matrix.length != matrix.length || other.matrix[0].length != matrix[0].length) {
			return false;
		}

		for (int row = 0; row < matrix.length; row++) {
			if (Arrays.mismatch(matrix[row], other.matrix[row]) != -1) {
				return false;
			}
		}

		return true;
	}

	public String toString() {
		String result = "";
		for (double[] row : matrix) {
			result += Arrays.toString(row) + "\n";
		}
		return result;
	}

	public static void main(String[] args) {
		ImmutableMatrix matrix = new ImmutableMatrix(3, 3, 1);
		System.out.println(matrix);

		double[][] threeByTwo = { { 1, 4 }, { 2, 5 }, { 3, 6 } };
		double[][] twoByThree = { { -1, 0, 1 }, { 5, 3, 4 } };

		ImmutableMatrix matrix2 = new ImmutableMatrix(threeByTwo);
		ImmutableMatrix matrix3 = new ImmutableMatrix(twoByThree);
		System.out.println(matrix2.transpose());
		System.out.println(matrix3.transpose());

		double[][] twoByTwo0 = { { 1, 2 }, { -2, 1 } };
		double[][] twoByTwo1 = { { 1, 0 }, { 0, 1 } };

		ImmutableMatrix matrix4 = new ImmutableMatrix(twoByTwo0);
		ImmutableMatrix matrix5 = new ImmutableMatrix(twoByTwo1);
		System.out.println(matrix4.multiply(matrix5));
		
		double[][] id2 = { { 1, 0 }, { 0, 1 } };
		double[][] id3 = { { 1, 0, 0}, {0, 1, 0}, { 0, 0, 1}};
		double[][] notId1 = { { .5, 0, 0}, {0, .5, 0}, { 0, 0, .5}};
		ImmutableMatrix matrix6 = new ImmutableMatrix(id2);
		ImmutableMatrix matrix7 = new ImmutableMatrix(id3);
		ImmutableMatrix matrix8 = new ImmutableMatrix(notId1);
		System.out.println(matrix6.isIdentityMatrix() + "\t" + matrix6.isDiagonalMatrix());
		System.out.println(matrix7.isIdentityMatrix() + "\t" + matrix7.isDiagonalMatrix());
		System.out.println(matrix8.isIdentityMatrix() + "\t" + matrix8.isDiagonalMatrix());
	}
}
