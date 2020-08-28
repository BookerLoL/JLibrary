package august_2020;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class RTree {
	// Treated like a normal graph found in math
	public class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}

	public class MinimumBoundingRegion {
		private Point topLeft;
		private Point bottomRight;

		public MinimumBoundingRegion(Point topLeft, Point bottomRight) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
		}

		private boolean canContain(MinimumBoundingRegion region) {
			return topLeft.x <= region.topLeft.x && bottomRight.x >= region.bottomRight.x
					&& topLeft.y >= region.topLeft.y && bottomRight.y <= region.bottomRight.y;
		}

		private int midX() {
			return (bottomRight.x + topLeft.x) / 2;
		}

		private int midY() {
			return (topLeft.y + bottomRight.y) / 2;
		}

		private Point midPoint() {
			return new Point(midX(), midY());
		}

		private int width() {
			return bottomRight.x - topLeft.x;
		}

		private int height() {
			return topLeft.y - bottomRight.y;
		}

		private int perimeter() {
			return 2 * (width() + height());
		}

		private int area() {
			return width() * height();
		}

		private MinimumBoundingRegion createDefault() {
			return new MinimumBoundingRegion(new Point(Integer.MAX_VALUE, Integer.MIN_VALUE),
					new Point(Integer.MIN_VALUE, Integer.MAX_VALUE));
		}

		public String toString() {
			return "TL: " + topLeft + ", BR: " + bottomRight;
		}
	}

	public class Node {
		private MinimumBoundingRegion[] polygons;
		private Node[] regions;
		private int count;
		private MinimumBoundingRegion mbr;

		public Node(boolean isLeaf, MinimumBoundingRegion mbr) {
			this.mbr = mbr;

			if (isLeaf) {
				polygons = new MinimumBoundingRegion[capacity + 1];
			} else {
				regions = new Node[capacity + 1];
			}
		}

		private boolean isLeaf() {
			return polygons != null;
		}

		private boolean exceedsCapacity() {
			return count > capacity;
		}

		private void addToLeaf(MinimumBoundingRegion polygon) {
			polygons[count++] = polygon;
			mbr = calculateIfAdded(polygon);
		}

		private void addToNonLeaf(Node region) {
			regions[count++] = region;
			mbr = calculateIfAdded(region.mbr);
		}

		private Node splitLeaf() {
			MinimumBoundingRegion farthest1 = polygons[0];
			MinimumBoundingRegion farthest2 = polygons[1];
			double dist = distance(farthest1.midPoint(), farthest2.midPoint());
			for (int i = 0; i < count - 1; i++) {
				MinimumBoundingRegion temp1 = polygons[i];
				for (int j = i + 1; j < count; j++) {
					MinimumBoundingRegion temp2 = polygons[j];
					double tempDist = distance(temp1.midPoint(), temp2.midPoint());
					if (tempDist > dist) {
						dist = tempDist;
						farthest1 = temp1;
						farthest2 = temp2;
					}
				}
			}

			Node newParent = new Node(false, root.mbr.createDefault());
			Node leftSplit = new Node(true, root.mbr.createDefault());
			Node rightSplit = new Node(true, root.mbr.createDefault());
			leftSplit.addToLeaf(farthest1);
			rightSplit.addToLeaf(farthest2);

			int halfCapacity = (count + 1) / 2;
			for (int i = 0; i < count; i++) {
				if (polygons[i] != farthest1 && polygons[i] != farthest2) {
					if (leftSplit.count == halfCapacity) {
						rightSplit.addToLeaf(polygons[i]);
					} else if (rightSplit.count == halfCapacity) {
						leftSplit.addToLeaf(polygons[i]);
					} else {
						MinimumBoundingRegion leftAdded = leftSplit.calculateIfAdded(polygons[i]);
						MinimumBoundingRegion rightAdded = rightSplit.calculateIfAdded(polygons[i]);
						int perimeterDiff = leftAdded.perimeter() - rightAdded.perimeter();
						if (perimeterDiff < 0) {
							leftSplit.addToLeaf(polygons[i]);
						} else if (perimeterDiff > 0) {
							rightSplit.addToLeaf(polygons[i]);
						} else {
							if (leftAdded.area() - rightAdded.area() <= 0) {
								leftSplit.addToLeaf(polygons[i]);
							} else {
								rightSplit.addToLeaf(polygons[i]);
							}
						}
					}
				}
			}

			newParent.addToNonLeaf(leftSplit);
			newParent.addToNonLeaf(rightSplit);
			return newParent;
		}

		private Node splitNonLeaf() {
			Node farthest1 = regions[0];
			Node farthest2 = regions[1];
			double dist = distance(farthest1.mbr.midPoint(), farthest2.mbr.midPoint());
			for (int i = 0; i < count - 1; i++) {
				Node temp1 = regions[i];
				for (int j = i + 1; j < count; j++) {
					Node temp2 = regions[j];
					double tempDist = distance(temp1.mbr.midPoint(), temp2.mbr.midPoint());
					if (tempDist > dist) {
						dist = tempDist;
						farthest1 = temp1;
						farthest2 = temp2;
					}
				}
			}

			Node newParent = new Node(false, root.mbr.createDefault());
			Node leftSplit = new Node(false, root.mbr.createDefault());
			Node rightSplit = new Node(false, root.mbr.createDefault());
			leftSplit.addToNonLeaf(farthest1);
			rightSplit.addToNonLeaf(farthest2);
			int halfCapacity = (count + 1) / 2;
			for (int i = 0; i < count; i++) {
				if (regions[i] != farthest1 && regions[i] != farthest2) {
					if (leftSplit.count == halfCapacity) {
						rightSplit.addToNonLeaf(regions[i]);
					} else if (rightSplit.count == halfCapacity) {
						leftSplit.addToNonLeaf(regions[i]);
					} else {
						MinimumBoundingRegion leftAdded = leftSplit.calculateIfAdded(regions[i].mbr);
						MinimumBoundingRegion rightAdded = rightSplit.calculateIfAdded(regions[i].mbr);
						int perimeterDiff = leftAdded.perimeter() - rightAdded.perimeter();
						if (perimeterDiff < 0) {
							leftSplit.addToNonLeaf(regions[i]);
						} else if (perimeterDiff > 0) {
							rightSplit.addToNonLeaf(regions[i]);
						} else {
							if (leftAdded.area() - rightAdded.area() <= 0) {
								leftSplit.addToNonLeaf(regions[i]);
							} else {
								rightSplit.addToNonLeaf(regions[i]);
							}
						}
					}
				}
			}

			newParent.addToNonLeaf(leftSplit);
			newParent.addToNonLeaf(rightSplit);
			return newParent;
		}

		private double distance(Point p, Point p2) {
			return Math.pow(p.x - p2.x, 2) + Math.pow(p.y - p2.y, 2);
		}

		private MinimumBoundingRegion calculateIfAdded(MinimumBoundingRegion newlyAdded) {
			MinimumBoundingRegion tempMBR = new MinimumBoundingRegion(new Point(mbr.topLeft.x, mbr.topLeft.y),
					new Point(mbr.bottomRight.x, mbr.bottomRight.y));

			if (tempMBR.topLeft.x > newlyAdded.topLeft.x) {
				tempMBR.topLeft.x = newlyAdded.topLeft.x;
			}

			if (tempMBR.topLeft.y < newlyAdded.topLeft.y) {
				tempMBR.topLeft.y = newlyAdded.topLeft.y;
			}

			if (tempMBR.bottomRight.x < newlyAdded.bottomRight.x) {
				tempMBR.bottomRight.x = newlyAdded.bottomRight.x;
			}

			if (tempMBR.bottomRight.y > newlyAdded.bottomRight.y) {
				tempMBR.bottomRight.y = newlyAdded.bottomRight.y;
			}

			return tempMBR;
		}

		private void merge(Node prevSplitted, Node other) {
			int index = regionIndexOf(prevSplitted);
			System.arraycopy(regions, index + 1, regions, index, count - index - 1);
			regions[--count] = null;
		
			for (int i = 0; i < other.count; i++) {
				addToNonLeaf(other.regions[i]);
			}
			
			mbr = calculateIfAdded(other.mbr);
		}

		private int regionIndexOf(Node node) {
			for (int i = 0; i < count; i++) {
				if (regions[i] == node) {
					return i;
				}
			}
			return -1;
		}

		public String toString() {
			String msg = "Node is: ";
			int area = mbr.area();
			int perimeter = mbr.perimeter();
			if (isLeaf()) {
				msg += "leaf" + ", " + mbr + "\tarea: " + area + "\tperimeter: " + perimeter + " polygons: " + count
						+ "\t" + Arrays.toString(polygons);
			} else {
				msg += "non-leaf" + ", " + mbr + "\tarea: " + area + "\tperimeter: " + perimeter + "\tsubregion count: "
						+ count +"\t" ;
			}
			return msg;
		}
	}

	public Point create(int x, int y) {
		return new Point(x, y);
	}

	public MinimumBoundingRegion create(Point topLeft, Point bottomRight) {
		return new MinimumBoundingRegion(topLeft, bottomRight);
	}

	private static final int DEFAULT_REGION_CAPACITY = 4;
	private final int capacity;
	private Node root;

	public RTree() {
		this(DEFAULT_REGION_CAPACITY);
	}

	public RTree(int capacity) {
		if (!isValidCapacity(capacity)) {
			capacity = DEFAULT_REGION_CAPACITY;
		}

		this.capacity = capacity;
		root = new Node(true, new MinimumBoundingRegion(new Point(Integer.MAX_VALUE, Integer.MIN_VALUE),
				new Point(Integer.MIN_VALUE, Integer.MAX_VALUE)));
	}

	private boolean isValidCapacity(int capacity) {
		return capacity > 1;
	}

	public void insert(MinimumBoundingRegion polygon) {
		root = insert(root, polygon);
	}

	private Node insert(Node current, MinimumBoundingRegion polygon) {
		if (!current.isLeaf()) {
			System.out.println("HERE: " + Arrays.toString(current.regions) + current.count);
			Node chosen = current.regions[0]; 
			int chosenPerimeter = chosen.calculateIfAdded(polygon).perimeter();

			for (int i = 1; i < current.count; i++) {
				MinimumBoundingRegion resultant = current.regions[i].calculateIfAdded(polygon);
				int resultantPerimeter = resultant.perimeter();

				if (resultantPerimeter < chosenPerimeter
						|| (resultantPerimeter == chosenPerimeter && resultant.area() < chosen.calculateIfAdded(polygon).area())) {
					chosen = current.regions[i];
					chosenPerimeter = resultantPerimeter;
				}
			}
			Node result = insert(chosen, polygon);
			if (chosen != result) {
				current.merge(chosen, result);

				if (current.exceedsCapacity()) {
					current = current.splitNonLeaf();
				}
			}
		} else {
			current.addToLeaf(polygon);
			if (current.exceedsCapacity()) {
				current = current.splitLeaf();
			}
		}
		return current;
	}

	public void print() {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.remove();
			System.out.println(curr);
			if (!curr.isLeaf()) {
				for (int i = 0; i < curr.count; i++) {
					q.add(curr.regions[i]);
				}
			}
		}
	}

	public static void main(String[] args) {
		RTree tree = new RTree(2);
		tree.insert(tree.create(tree.create(20, 25), tree.create(30, 20))); // house 1
		tree.insert(tree.create(tree.create(0, 45), tree.create(50, 40))); // road 1
		tree.insert(tree.create(tree.create(45, 40), tree.create(50, 0))); // road 2
		tree.insert(tree.create(tree.create(20, 75), tree.create(30, 70))); // school
		tree.insert(tree.create(tree.create(60, 60), tree.create(80, 40))); // house 2
		tree.insert(tree.create(tree.create(60, 75), tree.create(80, 70))); // theatre
		tree.insert(tree.create(tree.create(50, 25), tree.create(65, 20))); // h3
		tree.print();

	}
}
