package august_2020;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

import august_2020.Octree.Quadrant;

/*
 * Need to check if it can be subdivded anymore
 * 
 * There is also memory optimization with initializing only a specific quadrant instead of all 4
 * 
 * Can use if statements to determine which quadrant to check rather than iterating each quadrant (worst case)
 * 
 * Assumes a normal graph such that there is a -x to the left, x to the right, y upwards, and -y downwards of the origin
 */
public class QuadTree<T> {
	protected static class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			return p == this ? true : x == p.x && y == p.y;
		}

		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}

	protected static class Boundary {
		Point topLeft;
		Point bottomRight;

		public Boundary(Point topLeft, Point bottomRight) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
		}

		public boolean canContain(Point p) {
			return withinXBounds(p.x) && withinYBounds(p.y);
		}

		private boolean withinXBounds(int xPoint) {
			return topLeft.x <= xPoint && xPoint <= bottomRight.x;
		}

		private boolean withinYBounds(int yPoint) {
			return bottomRight.y <= yPoint && yPoint <= topLeft.y;
		}
	}

	protected class Node {
		private Point point;
		private T data;

		public Node(Point p, T data) {
			point = p;
			this.data = data;
		}

		public boolean equals(Node n) {
			return n == this ? true : this.point.equals(n.point);
		}
	}

	protected class Quadrant {
		private List<Node> values;
		private Boundary boundary;
		private Quadrant[] quadrants;

		public Quadrant(Boundary boundary) {
			this.boundary = boundary;
			values = new LinkedList<>();
		}

		public boolean add(Node value) {
			return values.size() == maxCapacityPerQuad ? false : values.add(value);
		}

		public boolean contains(Node value) {
			return values.contains(value);
		}

		public Node get(Point p) {
			for (Node n : values) {
				if (n.point.equals(p)) {
					return n;
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		public void subdivide() {
			Point centerPoint = new Point((boundary.topLeft.x + boundary.bottomRight.x) / 2,
					(boundary.topLeft.y + boundary.bottomRight.y) / 2);
			Quadrant northWest = new Quadrant(new Boundary(boundary.topLeft, centerPoint));
			Quadrant southEast = new Quadrant(new Boundary(centerPoint, boundary.bottomRight));

			Quadrant northEast = new Quadrant(new Boundary(new Point(centerPoint.x, boundary.topLeft.y),
					new Point(boundary.bottomRight.x, centerPoint.y)));
			Quadrant southWest = new Quadrant(new Boundary(new Point(boundary.topLeft.x, centerPoint.y),
					new Point(centerPoint.x, boundary.bottomRight.y)));

			quadrants = new QuadTree.Quadrant[] { northWest, northEast, southWest, southEast };
		}
	}

	public QuadTree(Boundary boundingArea) {
		this(boundingArea, 4);
	}

	public QuadTree(Boundary boundingArea, int capacityPerQuadrant) {
		root = new Quadrant(boundingArea);
		this.maxCapacityPerQuad = capacityPerQuadrant;

	}

	private Quadrant root;
	private int maxCapacityPerQuad;

	public boolean insert(Node node) {
		return insert(root, node);
	}

	private boolean insert(Quadrant quadrant, Node node) {
		if (!quadrant.boundary.canContain(node.point) || quadrant.contains(node)) {
			return false;
		}

		if (quadrant.add(node)) {
			return true;
		}

		if (quadrant.quadrants == null) {
			quadrant.subdivide();
		}

		for (Quadrant subquadrant : quadrant.quadrants) {
			if (insert(subquadrant, node)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Point p) {
		return contains(root, p);
	}

	private boolean contains(Quadrant quadrant, Point p) {
		return get(quadrant, p) != null;
	}

	// method to get the quadrant that contains the point, maybe a subquandrant
	private Quadrant get(Quadrant quadrant, Point p) {
		if (!quadrant.boundary.canContain(p)) {
			return null;
		}

		if (quadrant.get(p) != null) {
			return quadrant;
		}

		// Could do a specific boundary check to avoid extra checks
		if (quadrant.quadrants != null) {
			for (Quadrant subquadrant : quadrant.quadrants) {
				Quadrant result = get(subquadrant, p);
				if (result != null) {
					return result;
				}
			}
		}

		return null;
	}

	public T set(Point p, T newData) {
		Quadrant quadrant = get(root, p);

		if (quadrant == null) {
			return null;
		}

		T prevData = quadrant.get(p).data;
		quadrant.get(p).data = newData;
		return prevData;
	}

	public boolean remove(Point p) {
		Quadrant quadrant = get(root, p);

		if (quadrant == null) {
			return false;
		}

		ListIterator<Node> iterator = quadrant.values.listIterator();
		while (iterator.hasNext()) {
			Node curr = iterator.next();
			if (curr.point.equals(p)) {
				iterator.remove();
				break;
			}
		}
		return true;
	}

	public static Point create(int x, int y) {
		return new Point(x, y);
	}

	public static Boundary create(Point topLeftBound, Point bottomRightBound) {
		return new Boundary(topLeftBound, bottomRightBound);
	}

	public Node create(Point p, T data) {
		return new Node(p, data);
	}

	public static void main(String[] args) {
		test1();
	}
	
	private static void test1() {
		Point topLeftBound = QuadTree.create(-10, 10);
		Point bottomRightBound = QuadTree.create(10, -10);
		QuadTree<Boolean> qtree = new QuadTree<>(QuadTree.create(topLeftBound, bottomRightBound));

		int numNodes = 100;
		QuadTree.Node[] nodes = new QuadTree.Node[numNodes];
		for (int i = 0; i < numNodes; i++) {
			nodes[i] = qtree.create(new Point(randomNumber(topLeftBound.x, bottomRightBound.x),
					randomNumber(bottomRightBound.y, topLeftBound.y)), true);
		}

		for (int i = 0; i < numNodes; i++) {
			qtree.insert(nodes[i]);
		}

		for (int i = 0; i < numNodes; i++) {
			System.out.println(qtree.contains(nodes[i].point));
		}
		
		System.out.println("\n====TESTING RANDOM POINTS===\n");
		int numRandomPoints = 1000;
		for (int i = 0; i < numRandomPoints; i++) {
			System.out.println(qtree.contains(new Point(randomNumber(topLeftBound.x, bottomRightBound.x),
					randomNumber(bottomRightBound.y, topLeftBound.y))));
		}
	}

	private static int randomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
