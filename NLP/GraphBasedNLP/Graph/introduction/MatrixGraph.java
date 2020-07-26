package introduction;

public class MatrixGraph {
	private static double[][] vertices;

	public MatrixGraph(int numberOfVertices) {
		vertices = new double[numberOfVertices][numberOfVertices];
	}

	public void setWeight(int fromNode, int toNode, double weight) {
		vertices[fromNode][toNode] = weight;
	}

	protected double[] getTailVertex(int row) {
		return vertices[row];
	}

	protected double getHeadVertex(int row, int col) {
		return vertices[row][col];
	}

	public int degree(int vertex) {
		return outDegree(vertex) + inDegree(vertex);
	}

	public int outDegree(int vertex) {
		int outDegrees = 0;
		for (double value : getTailVertex(vertex)) {
			if (value != 0.0d) {
				outDegrees++;
			}
		}
		return outDegrees;
	}

	public int inDegree(int vertex) {
		int inDegrees = 0;
		for (int i = 0; i < vertices.length; i++) {
			if (getHeadVertex(i, vertex) != 0.0d) {
				inDegrees++;
			}
		}
		return inDegrees;
	}

	public boolean isCoCited(int vertexOne, int vertexTwo) {
		for (int i = 0; i < vertices.length; i++) {
			boolean hasEdge1 = getHeadVertex(i, vertexOne) > 0.0d;
			boolean hasEdge2 = getHeadVertex(i, vertexTwo) > 0.0d;
			if (hasEdge1  != hasEdge2) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isBibliographic(int vertexOne, int vertexTwo) {
		double[] vertexOneNodes = getTailVertex(vertexOne);
		double[] vertexTwoNodes = getTailVertex(vertexTwo);
		for (int i = 0; i < vertexOneNodes.length; i++) {
			boolean hasEdge1 = vertexOneNodes[i] > 0.0d;
			boolean hasEdge2 = vertexTwoNodes[i] > 0.0d;
			
			if (hasEdge1 != hasEdge2) {
				return false;
			}
		}
		return true;
	}
}
