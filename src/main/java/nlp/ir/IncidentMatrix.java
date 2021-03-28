package main.nlp.ir;

/*
 * Indicent Matrices aren't that good because of space complexity 
 * where 0's are wasting space
 * 
 * This implementation does not check boundaries 
 */
public class IncidentMatrix {
	protected boolean[][] incidenceMatrix;
	protected final int rows;
	protected final int cols;

	public IncidentMatrix(final int numWords, final int numSources) {
		rows = numWords;
		cols = numSources;
		incidenceMatrix = new boolean[rows][cols];
	}
	
	public void set(int row, int col, boolean value) {
		incidenceMatrix[row][col] = value;
	}
	
	public boolean get(int row, int col) {
		return incidenceMatrix[row][col];
	}
	
	public boolean[] get(int row) {
		return incidenceMatrix[row];
	}
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public void reset() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				incidenceMatrix[row][col] = false;
			}
		}
	}
}
