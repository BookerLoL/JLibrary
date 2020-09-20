package main.collections.Tree;

public class DiamondTree {
	public static class Node {
		int data;
		Node left;
		Node right;

		public Node(int data) {
			this.data = data;
		}
	}

	Node root;
	private int kLevels;

	public DiamondTree(int kLevels) {
		this.kLevels = kLevels;
		contructKLevel(kLevels);
	}

	public void contructKLevel(int newKLevels) {
		if (newKLevels <= 0) {
			return;
		}
		int num = 1;
		Node newRoot = new Node(num);
		root = newRoot;
		

		LinkedListQueue<Node> q = new LinkedListQueue<>(true);
		q.add(newRoot);
		while (newKLevels > 1) {
			int size = q.size();

			while (size > 0) {
				Node curr = q.poll();
				num++;
				Node newLeft = new Node(num);
				num++;
				Node newRight = new Node(num);
				curr.left = newLeft;
				curr.right = newRight;
				q.add(newLeft);
				q.add(newRight);
				size--;
			}
			newKLevels--;
		}

		while (q.size() > 1) {
			Node left = q.remove();
			Node right = q.remove();

			Node sumNode = new Node(left.data + right.data);
			left.right = sumNode;
			right.left = sumNode;
			q.add(sumNode);
		}
	}

	public String printLevellOrder() {
		StringBuilder sb = new StringBuilder();
		levelOrderHelper(root, sb, kLevels);
		return sb.toString();
	}

	private void levelOrderHelper(Node start, StringBuilder sb, int level) {
		if (start == null || level < 1) {
			sb.append("Tree is empty");
			return;
		}

		LinkedListQueue<Node> q = new LinkedListQueue<>(true);
		q.add(start);
		int upperLevel = level - 1;
		int size;
		while (upperLevel != 0) {
			size = q.size();
			while (size != 0) {
				Node temp = q.poll();
				sb.append(temp.data + " ");
				if (temp.left != null) {
					q.add(temp.left);
				}
				if (temp.right != null) {
					q.add(temp.right);
				}
				size--;
			}
			sb.append("\n");
			upperLevel--;
		}
		
		while (q.size() > 1) {
			size = q.size();
			while (size > 0) {
				Node left = q.poll();
				Node right = q.poll();
				sb.append(left.data + " " + right.data + " ");
				q.add(left.right);
				size -= 2;
			}
			sb.append("\n");
		}

		sb.append(q.poll().data + "\n");
	}

	public static void main(String[] args) {
		DiamondTree tree = new DiamondTree(20);
		System.out.println(tree.printLevellOrder());
	}
}
