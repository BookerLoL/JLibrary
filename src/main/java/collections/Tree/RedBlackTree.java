package main.collections.Tree;
import java.util.Comparator;

/*
 *  Incomplete
 *  
 *  Need to finish delete
 */
public class RedBlackTree<T extends Comparable<? super T>> {
	private enum Color {
		RED, BLACK;
	};

	private class Node {
		T data;
		Node parent;
		Node left;
		Node right;
		Color color;

		public Node(T data) {
			this.data = data;
			color = Color.RED;
		}

		public Node grandparent() {
			return parent != null ? parent.parent : null;
		}

		public Node sibling() {
			if (parent == null) {
				return null;
			}
			return parent.left != this ? this : parent.right;
		}

		public Node uncle() {
			return parent != null ? parent.sibling() : null;
		}

		public String toString() {
			return data + " is color: " + color.toString() + "\tparent: " + parent;
		}
	}

	private Node root;
	private Comparator<T> comp;
	private int size;

	public RedBlackTree() {
		this(Comparator.naturalOrder());
	}

	public RedBlackTree(Comparator<T> comp) {
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	private void leftRotate(Node current) {
		Node rightChild = current.right;
		Node parent = current.parent;

		current.right = rightChild.left;
		rightChild.right = current;
		current.parent = rightChild;

		if (current.right != null) {
			current.right.parent = current;
		}

		if (parent != null) {
			if (current == parent.right) {
				parent.right = rightChild;
			} else {
				parent.left = rightChild;
			}
		}
	}

	private void rightRotate(Node current) {
		Node leftChild = current.left;
		Node parent = current.parent;

		current.left = leftChild.right;
		leftChild.right = current;
		current.parent = leftChild;

		if (current.left != null) {
			current.left.parent = current;
		}

		if (parent != null) {
			if (current == parent.left) {
				parent.left = leftChild;
			} else {
				parent.right = leftChild;
			}
		}
	}

	public void insert(T data) {
		root = insert(root, new Node(data));
	}

	private Node insert(Node currRoot, Node newNode) {
		insertIntoLeaf(currRoot, newNode);
		insertRepair(newNode);
		for (currRoot = newNode; currRoot.parent != null; currRoot = currRoot.parent)
			;
		return currRoot;
	}

	private void insertRepair(Node newNode) {
		if (newNode.parent == null) { //at root, needs to be black
			newNode.color = Color.BLACK;
		} else if (newNode.parent.color == Color.BLACK) { 
			return; //parent is black, current node is red, rule is satisfied
		} else {
			Node uncle = newNode.uncle();
			if (uncle != null && uncle.color == Color.RED) { 
				//red child, red parent, red uncle, needs to fix
				newNode.parent.color = Color.BLACK;
				uncle.color = Color.BLACK;
				newNode.grandparent().color = Color.RED;
				insertRepair(newNode.grandparent()); //go up and fix
			} else {
				//parent is red, uncle is black
				Node parent = newNode.parent;
				Node grandParent = parent.grandparent();
				if (parent.right == newNode && grandParent.left == parent) {
					leftRotate(parent);
					newNode = newNode.left;
				} else if (parent.right == newNode && grandParent.right == parent) {
					rightRotate(parent);
					newNode = newNode.right;
				}
				
				//Fixing position of grandparent and parent after rotation
				parent = newNode.parent;
				grandParent = newNode.grandparent();
				
				if (newNode == parent.left) {
					rightRotate(grandParent);
				} else {
					leftRotate(grandParent);
				}
				parent.color = Color.BLACK;
				grandParent.color = Color.RED;
			}
		}
	}

	private void insertIntoLeaf(Node currRoot, Node newNode) {
		if (currRoot != null) {
			// System.out.println(currRoot + "\t" + newNode);
			if (comp.compare(newNode.data, currRoot.data) < 0) {
				if (currRoot.left != null) {
					insertIntoLeaf(currRoot.left, newNode);
					return;
				}
				currRoot.left = newNode;
			} else {
				if (currRoot.right != null) {
					insertIntoLeaf(currRoot.right, newNode);
					return;
				}
				currRoot.right = newNode;
			}
		}

		newNode.parent = currRoot;
	}
	
	

	public void preorder() {
		preorderHelper(root);
	}

	public int size() {
		return size++;
	}

	private void preorderHelper(Node root) {
		if (root != null) {
			System.out.println(root);
			preorderHelper(root.left);
			preorderHelper(root.right);
		}
	}

	public static void main(String[] args) {
		RedBlackTree<Integer> tree = new RedBlackTree<>();
		tree.insert(10);
		tree.insert(5);
		tree.insert(15);
		tree.insert(22);
		tree.insert(33);
		tree.preorder();
	}
}
