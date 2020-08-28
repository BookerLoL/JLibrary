package august_2020;

public class KDTree {
	public class Point {
		int[] dimensionValues;

		public Point(int[] dimensionValues) {
			this.dimensionValues = dimensionValues;
		}
		
		public boolean equals(Point p) {
			if (p.dimensionValues.length != dimensionValues.length) {
				return false;
			}
			
			for (int i = 0; i < dimensionValues.length; i++) {
				if (dimensionValues[i] != p.dimensionValues[i]) {
					return false;
				}
			}
			
			return true;
		}
	}

	private class Node {
		Point p;
		Node left;
		Node right;

		public Node(Point p) {
			this.p = p;
		}
	}

	private final int MAX_DIMENSION;
	private Node root;

	public KDTree(int dimensions) {
		this.MAX_DIMENSION = dimensions;
	}

	public void insert(Point data) {
		validatePoint(data);
		root = insert(root, data, 0);
	}

	private void validatePoint(Point p) {
		if (p.dimensionValues.length != MAX_DIMENSION) {
			throw new IllegalArgumentException("Point should have the same dimensions as the tree: " + MAX_DIMENSION);
		}
	}

	private Node insert(Node current, Point data, int currDimension) {
		if (current == null) {
			return new Node(data);
		}

		if (current.p.dimensionValues[currDimension] > data.dimensionValues[currDimension]) {
			current.left = insert(current.left, data, (currDimension + 1) % MAX_DIMENSION);
		} else {
			current.right = insert(current.right, data, (currDimension + 1) % MAX_DIMENSION);
		}

		return current;
	}

	public Point findMin(int dimension) {
		if (dimension < 0 || dimension >= MAX_DIMENSION) {
			throw new IllegalArgumentException("Dimension should be between 0 and " + (MAX_DIMENSION - 1));
		}

		Node result = findMin(root, dimension, 0);
		return result != null ? result.p : null;
	}

	private Node findMin(Node current, int targetDimension, int currDimension) {
		if (current == null) {
			return null;
		}

		if (currDimension == targetDimension) {
			return current.left != null ? findMin(current.left, targetDimension, (currDimension + 1) % MAX_DIMENSION)
					: current;
		} else {
			return min(targetDimension, current,
					findMin(current.left, targetDimension, (currDimension + 1) % MAX_DIMENSION),
					findMin(current.right, targetDimension, (currDimension + 1) % MAX_DIMENSION));
		}
	}

	// Assuming nodes has the first input as non-null
	private Node min(int dimension, Node... nodes) {
		Node min = nodes[0];
		for (int i = 1; i < nodes.length; i++) {
			if (nodes[i] != null && nodes[i].p.dimensionValues[dimension] < min.p.dimensionValues[dimension]) {
				min = nodes[i];
			}
		}
		return min;
	}
	
	public void delete(Point p) {
		root = delete(root, 0, p);
	}

	private Node delete(Node curr, int currDimension, Point targetData) {
		if (curr == null) {
			return null;
		}
		
		int nextDimension = (currDimension + 1) % MAX_DIMENSION;
		if (curr.p.equals(targetData)) {
			if (curr.right != null) {
				Node min = findMin(curr.right, currDimension, nextDimension);
				curr.p = min.p;
				curr.right = delete(curr.right, nextDimension, min.p);
			} else if (curr.left != null) {
				Node min = findMin(curr.left, currDimension, nextDimension);
				curr.p = min.p;
				curr.left = delete(curr.left, nextDimension, min.p);
			} else {
				curr = null;
			}
		} else {
			if (curr.p.dimensionValues[currDimension] > targetData.dimensionValues[currDimension]) {
				curr.left = delete(curr.left, nextDimension, targetData);
			} else {
				curr.right = delete(curr.right, nextDimension, targetData);
			}
		}
		return curr;
	}
}
