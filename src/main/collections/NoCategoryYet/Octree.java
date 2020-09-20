package main.collections.NoCategoryYet;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/*
 * Need to check if it can be subdivded anymore
 * 
 * There is also memory optimization with initializing only a specific quadrant instead of all 8
 * 
 * Can use if statements to determine which quadrant to check rather than iterating each quadrant (worst case)
 * 
 * Assumes a normal graph such that there is a -x to the left, x to the right, y upwards, and -y downwards of the origin
 */
public class Octree<T> {
	protected static class Point {
		int x;
		int y;
		int z;

		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public boolean equals(Point p) {
			return p == this ? true : x == p.x && y == p.y && z == p.z;
		}

		public String toString() {
			return "(" + x + ", " + y + ", " + z + ")";
		}
	}

	protected static class Boundary {
		Point topLeftFront;
		Point bottomRightBack;

		public Boundary(Point topLeftFront, Point bottomRightBack) {
			this.topLeftFront = topLeftFront;
			this.bottomRightBack = bottomRightBack;
		}

		public boolean canContain(Point p) {
			return withinXBounds(p.x) && withinYBounds(p.y) && withinZBounds(p.z);
		}

		private boolean withinXBounds(int xPoint) {
			return topLeftFront.x <= xPoint && xPoint <= bottomRightBack.x;
		}

		private boolean withinYBounds(int yPoint) {
			return bottomRightBack.y <= yPoint && yPoint <= topLeftFront.y;
		}

		private boolean withinZBounds(int zPoint) {
			return bottomRightBack.z <= zPoint && zPoint <= topLeftFront.z;
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

		//ftl, ftr, fbl, fbr, btl, btr, bbl, bbr
		@SuppressWarnings("unchecked")
		public void subdivide() {
			//Cube split into 8 smaller cubes
			Point centerPoint = new Point((boundary.topLeftFront.x + boundary.bottomRightBack.x) / 2,
					(boundary.topLeftFront.y + boundary.bottomRightBack.y) / 2,
					(boundary.topLeftFront.z + boundary.bottomRightBack.z) / 2);
			Quadrant frontTopLeft = new Quadrant(new Boundary(boundary.topLeftFront, centerPoint));
			Quadrant frontTopRight = new Quadrant(new Boundary(new Point(centerPoint.x, boundary.topLeftFront.y, boundary.topLeftFront.z), new Point(boundary.bottomRightBack.x, centerPoint.y, centerPoint.z)));
			Quadrant frontBottomLeft = new Quadrant(new Boundary(new Point(boundary.topLeftFront.x, centerPoint.y, boundary.topLeftFront.z), new Point(centerPoint.x, boundary.bottomRightBack.y, centerPoint.z)));
			Quadrant frontBottomRight = new Quadrant(new Boundary(new Point(centerPoint.x, centerPoint.y, boundary.topLeftFront.z), new Point(boundary.bottomRightBack.x, boundary.bottomRightBack.y, centerPoint.z)));
			Quadrant backTopLeft = new Quadrant(new Boundary(new Point(boundary.topLeftFront.x, boundary.topLeftFront.y, centerPoint.z), new Point(centerPoint.x, centerPoint.y, boundary.bottomRightBack.z)));
			Quadrant backTopRight = new Quadrant(new Boundary(new Point(centerPoint.x, boundary.topLeftFront.y, centerPoint.z), new Point(boundary.bottomRightBack.x, centerPoint.y, boundary.bottomRightBack.z)));
			Quadrant backBottomLeft = new Quadrant(new Boundary(new Point(boundary.topLeftFront.x, centerPoint.y, centerPoint.z), new Point(centerPoint.x, boundary.bottomRightBack.y, boundary.bottomRightBack.z)));
			Quadrant backBottomRight = new Quadrant(new Boundary(centerPoint, boundary.bottomRightBack));
			quadrants = new Octree.Quadrant[] {frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight, backTopLeft, backTopRight, backBottomLeft, backBottomRight};
		}
	}

	public Octree(Boundary boundingArea) {
		this(boundingArea, 4);
	}

	public Octree(Boundary boundingArea, int capacityPerQuadrant) {
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

	public static Point create(int x, int y, int z) {
		return new Point(x, y, z);
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
		Point topLeftBound = Octree.create(-10, 10, 10);
		Point bottomRightBound = Octree.create(10, -10, -10);
		Octree<Boolean> otree = new Octree<>(Octree.create(topLeftBound, bottomRightBound), 2);

		int numNodes = 2000;
		Octree.Node[] nodes = new Octree.Node[numNodes];
		for (int i = 0; i < numNodes; i++) {
			nodes[i] = otree.create(new Point(randomNumber(topLeftBound.x, bottomRightBound.x),
					randomNumber(bottomRightBound.y, topLeftBound.y), randomNumber(bottomRightBound.z, topLeftBound.z)),
					true);
		}
		
	

		for (int i = 0; i < numNodes; i++) {
			otree.insert(nodes[i]);
			System.out.println("Inserted: " + nodes[i].point);
		}
	
		for (int i = 0; i < numNodes; i++) {
			System.out.println(otree.contains(nodes[i].point));
		}

		System.out.println("\n====TESTING RANDOM POINTS===\n");
		int numRandomPoints = 100;
		int matched = 0;
		for (int i = 0; i < numRandomPoints; i++) {
			Point testPoint = new Point(randomNumber(topLeftBound.x, bottomRightBound.x),
					randomNumber(bottomRightBound.y, topLeftBound.y),
					randomNumber(bottomRightBound.z, topLeftBound.z));

			if (otree.contains(testPoint)) {
				matched++;
			} else {
				System.out.print("Didn't match for point: " + testPoint + "\n");
			}
		}
		
		System.out.println("matched: " + (matched / (double) numRandomPoints) * 100);
	}
	
	private static int randomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
