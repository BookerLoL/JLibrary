package august_2020;

public class Rope {
	protected static class RopeNode {
		RopeNode left;
		RopeNode right;
		int lengthWeight;
		String data;

		public RopeNode() {

		}

		public RopeNode(String str) {
			data = str;
			lengthWeight = str.length();
		}

		private RopeNode(RopeNode left, RopeNode right) {
			this.left = left;
			this.right = right;

			lengthWeight = left.lengthWeight;
			if (!left.isLeaf()) {
				do {
					lengthWeight += left.right.lengthWeight;
					left = left.right;
				} while (left.right != null);
			}
		}

		public boolean isLeaf() {
			return data != null;
		}

		public static RopeNode concatenate(RopeNode left, RopeNode right) {
			return new RopeNode(left, right);
		}

		public void clear() {
			data = null;
			left = null;
			right = null;
		}

		public String toString() {
			String dataOutput = data != null ? data : "";
			return "weight: " + lengthWeight + "\tis leaf: " + isLeaf() + "\t" + dataOutput;
		}
	}

	private RopeNode root;
	private StringBuilder sb;

	public String report(int start, int end) {
		if (start == 0 && end >= root.lengthWeight) {
			sb = new StringBuilder(root.lengthWeight);
			visitAllAndAppend(root);
			String result = sb.toString();
			sb = null;
			return result;
		}
		return report(start, end, root);
	}

	private void visitAllAndAppend(RopeNode current) {
		if (current != null) {
			if (current.isLeaf()) {
				sb.append(current.data);
			} else {
				visitAllAndAppend(current.left);
				visitAllAndAppend(current.right);
			}
		}
	}

	private String report(int start, int end, RopeNode current) {
		System.out.println("traversing: " + start + "\t" + end + "\t" + current);
		if (start > end || current == null) {
			return "";
		}

		if (current.isLeaf()) {
			return current.data;
		} else {
			return report(start, end - current.lengthWeight, current.left) + report(start + current.lengthWeight, end, current.right);
		}
	}

	public void inorderPrint() {
		inorderPrint(root);
	}

	private void inorderPrint(RopeNode node) {
		if (node != null) {
			inorderPrint(node.left);
			System.out.println(node);
			inorderPrint(node.right);
		}
	}

	public static void main(String[] args) {
		Rope rope = new Rope();

		RopeNode node1 = new RopeNode("Hello_");
		RopeNode node2 = new RopeNode("my_");
		RopeNode node3 = new RopeNode("na");
		RopeNode node4 = new RopeNode("me_i");
		RopeNode node5 = new RopeNode("s");
		RopeNode node6 = new RopeNode("_Simon");

		RopeNode leftNode = new RopeNode(node1, node2);
		RopeNode rightNode = new RopeNode(node3, node4);
		RopeNode rightRightNode = new RopeNode(node5, node6);

		RopeNode baseRight = new RopeNode(rightNode, rightRightNode);
		RopeNode baseLeft = new RopeNode(leftNode, baseRight);
		RopeNode newRoot = new RopeNode(baseLeft, null);

		rope.root = newRoot;
		// rope.inorderPrint();

		System.out.println(rope.report(2, 10));
	}
}
