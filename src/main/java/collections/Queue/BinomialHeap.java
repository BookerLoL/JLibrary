package main.collections.Queue;
import java.util.Comparator;

public class BinomialHeap<T extends Comparable<T>> {
	/*
	 * I'm unsure what the purpose of parent link is used for but will keep it in there just in case
	 * 
	 */
	private class Node {
		Node parent;
		Node child;
		Node sibling;
		T data;
		int degree;

		public Node(T data) {
			this.data = data;
		}

		public T getData() {
			return data;
		}

		@SuppressWarnings("unused")
		public Node getParent() {
			return parent;
		}

		public Node getChild() {
			return child;
		}

		public Node getSibling() {
			return sibling;
		}

		public int getDegree() {
			return degree;
		}

		public void setParent(Node newParent) {
			parent = newParent;
		}

		public void setChild(Node newChild) {
			child = newChild;
		}

		public void setSibling(Node newSibling) {
			sibling = newSibling;
		}

		public void setDegree(int newDegree) {
			degree = newDegree;
		}

		public String toString() {
			return "( " + getData().toString() + ", d: " + getDegree() + ") ";
		}
	}

	private Node head;
	private int size;
	private Comparator<T> comp;

	public BinomialHeap() {
		this(Comparator.naturalOrder());
	}

	public BinomialHeap(Comparator<T> comp) {
		this.comp = comp;
	}

	private int compare(T data1, T data2) {
		if (data1 == null) {
			return -1;
		} else if (data2 == null) {
			return 1;
		}

		return comp.compare(data1, data2);
	}

	private void link(Node c, Node p) {
		c.setParent(p);
		c.setSibling(p.getChild());
		p.setChild(c);
		p.setDegree(p.getDegree() + 1);
	}

	private Node mergeRoots(Node h1, Node h2) {
		if (h1 == null) {
			return h2;
		} else if (h2 == null) {
			return h1;
		}

		if (h1.getDegree() <= h2.getDegree()) {
			h1.setSibling(mergeRoots(h1.getSibling(), h2));
			return h1;
		} else {
			h2.setSibling(mergeRoots(h1, h2.getSibling()));
			return h2;
		}
	}

	public void insert(T data) {
		Node newNode = new Node(data);
		head = mergeRoots(head, newNode);
		head = adjust(head);
		size++;
	}

	private Node adjust(Node h1) {
		if (head == null) {
			return null;
		}
		Node prev = null;
		Node curr = h1;
		Node next = curr.getSibling();

		while (next != null) {
			if (curr.getDegree() != next.getDegree()
					|| (next.getSibling() != null && next.getSibling().getDegree() == curr.getDegree())) {
				prev = curr;
				curr = next;
			} else if (compare(curr.getData(), next.getData()) >= 0) {
				curr.setSibling(next.getSibling());
				link(next, curr);
			} else {
				if (prev == null) {
					head = next;
				} else {
					prev.setSibling(next);
				}
				link(curr, next);
				curr = next;
			}
			next = curr.getSibling();
		}
		return head;
	}

	private Node removeMinTree(Node tree) {
		Node curr = tree.getChild();
		Node prev = null;
		Node next = null;

		while (curr != null) { // reversing the order of siblings
			next = curr.getSibling();
			curr.setSibling(prev);
			prev = curr;
			curr = next;
		}

		return prev;
	}

	private Node getMinRoot(Node root) {
		if (root == null) {
			return null;
		}

		Node min = root;
		Node curr = root.getSibling();
		while (curr != null) {
			if (compare(curr.getData(), min.getData()) >= 0) {
				min = curr;
			}
			curr = curr.getSibling();
		}
		return min;
	}

	public T remove() {
		if (isEmpty()) {
			return null;
		}

		return removeHelper();
	}

	private T removeHelper() {
		Node mustRemove = getMinRoot(head);

		if (head == mustRemove) {
			head = head.getSibling();
		} else {
			Node temp = head;
			while (temp != null) {
				if (temp.getSibling() == mustRemove) {
					temp.setSibling(temp.getSibling().getSibling());
				}
				temp = temp.getSibling();
			}
		}

		Node low = removeMinTree(mustRemove);
		head = mergeRoots(head, low);
		head = adjust(head);
		size--;
		return mustRemove.getData();
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public String postorder() {
		StringBuilder sb = new StringBuilder();
		postorder(head, sb);
		return sb.toString();
	}

	private void postorder(Node curr, StringBuilder sb) {
		if (curr != null) {
			postorder(curr.getChild(), sb);
			postorder(curr.getSibling(), sb);
			sb.append(curr + " ");
		}
	}
}
