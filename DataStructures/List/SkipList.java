import java.util.Arrays;
import java.util.Comparator;

public class SkipList<T extends Comparable<? super T>> {
	private class Node {
		T data;
		Node[] nextPointers;

		@SuppressWarnings("unchecked")
		public Node(T data, int levels) {
			nextPointers = Arrays.copyOf(new Object[levels], levels, Node[].class);
			this.data = data;
		}

		public Node getNext(int atLevel) {
			return nextPointers[atLevel];
		}

		public void setNext(int atLevel, Node next) {
			nextPointers[atLevel] = next;
		}

		public void setNext(int startLevel, int toLevel, Node next) {
			for (int level = startLevel; level < toLevel; level++) {
				nextPointers[level] = next;
			}
		}

		public int maxLevel() {
			return nextPointers.length;
		}

		public void clear() {
			data = null;
			nextPointers = null;
		}
	}

	private Node head;
	private Node tail;
	private int maxLevel;
	private float threshold;
	private Comparator<T> comp;

	public SkipList() {
		this(5, 0.5f, Comparator.naturalOrder());
	}

	public SkipList(int maxLevel) {
		this(maxLevel, 0.5f, Comparator.naturalOrder());
	}

	public SkipList(int maxLevel, Comparator<T> comp) {
		this(maxLevel, 0.5f, comp);
	}

	public SkipList(int maxLevel, float threshold, Comparator<T> comp) {
		this.maxLevel = maxLevel <= 1 ? 5 : maxLevel;
		head = new Node(null, maxLevel);
		tail = new Node(null, 0);
		head.setNext(0, maxLevel, tail);
		this.threshold = threshold;
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public void insert(T data) {
		Node[] currentNode = getNodesLessThanData(data);

		if (currentNode[0].getNext(0) != tail && comp.compare(data, currentNode[0].getNext(0).data) == 0) {
			return;
		} else {
			int newLevels = generateRandomLevel();
			Node newNode = new Node(data, newLevels);
			for (int atLevel = 0; atLevel < newLevels; atLevel++) {
				newNode.setNext(atLevel, currentNode[atLevel].getNext(atLevel));
				currentNode[atLevel].setNext(atLevel, newNode);
			}
		}
	}

	private int generateRandomLevel() {
		int level = 1;
		while (Math.random() < threshold && level < maxLevel) {
			level++;
		}
		return level;
	}

	private Node[] getNodesLessThanData(T data) {
		Node[] currentNode = getCopyOf(head, maxLevel);
		for (int atLevel = maxLevel - 1; atLevel >= 0; atLevel--) {
			while (currentNode[atLevel].getNext(atLevel) != tail
					&& comp.compare(data, currentNode[atLevel].getNext(atLevel).data) < 0) {
				currentNode[atLevel] = currentNode[atLevel].getNext(atLevel);
			}
		}

		return currentNode;
	}

	@SuppressWarnings("unchecked")
	private Node[] getCopyOf(Node toCopy, int levels) {
		Node[] nodes = Arrays.copyOf(new Object[levels], levels, Node[].class);
		for (int i = 0; i < levels; i++) {
			nodes[i] = toCopy;
		}
		return nodes;
	}

	public void delete(T data) {
		Node[] currentNode = getNodesLessThanData(data);

		if (currentNode[0].getNext(0) != tail && comp.compare(data, currentNode[0].getNext(0).data) == 0) {
			Node dataNode = currentNode[0].getNext(0);
			for (int atLevel = 0; atLevel < dataNode.maxLevel(); atLevel++) {
				currentNode[atLevel].setNext(atLevel, dataNode.getNext(atLevel));
			}
			dataNode.clear();
		} else {
			return;
		}
	}

	private Node getNodeEqualTo(T data) {
		Node currentNode = head;
		for (int atLevel = maxLevel - 1; atLevel >= 0; atLevel--) {
			while (currentNode.getNext(atLevel) != tail && comp.compare(data, currentNode.getNext(atLevel).data) < 0) {
				currentNode = currentNode.getNext(atLevel);
			}
		}

		return currentNode.getNext(0) != tail && comp.compare(data, currentNode.getNext(0).data) == 0
				? currentNode.getNext(0)
				: null;
	}

	public boolean contains(T data) {
		return getNodeEqualTo(data) != null;
	}

	public void set(T data, T newData) {
		Node dataNode = getNodeEqualTo(data);
		if (dataNode != null) {
			dataNode.data = newData;
		}
	}

	public static void main(String[] args) {
		SkipList<Integer> lists = new SkipList<>();
		Integer[] nums = { 12, 5, 20, 3, 0, 50, 22, 13, 17, 1, 40 };
		Arrays.asList(nums).forEach(n -> lists.insert(n));
		for (int i = 0; i < nums.length; i++) {
			System.out.println(lists.contains(nums[i]));
			System.out.println(lists.contains(nums[i] + 100));

		}
		Arrays.asList(nums).forEach(n -> lists.delete(n));
		System.out.println("After delete");
		for (int i = 0; i < nums.length; i++) {
			System.out.println(lists.contains(nums[i]));
		}
	}
}
