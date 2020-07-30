/*
 * Unrolled Linked List 
 * Implements block nodes that each hold circular linked lists of:  
 * 			if even: capacity / 2
 * 			if odd:  capacity / 2 + 1
 * The last block node holds: capacity
 * 
 *  This list is superior compared to doubly linked lists in terms of memory as we reduce the previous node reference.
 *  
 *  You can even change the capacity each node has too, it doesn't always have to be capacity / 2 + 1
 * 
 *  The circular linked list is abnormal as it goes from: head (0) -> tail (size - 1) -> tail - 1  (size - 2 -> ... head  [repeat]
 *  This is due to inserting into a block node where the block node is not the last block node and it's over the it's capacity limit (a.k.a: tail block node)
 *  as it will be easy shift the last element when the block node is full into another block node. 
 *  ex: 1 2 3   4 5 6        insert(at 2, val -1)   -->  1 2 -1  3 4 5 6
 *  
 *  Only time to split is when the last node is past capacity
 *   
 *  Inserts will be at index  0 and at the end of the list. 
 *  Removing at the end of the list will be fast
 *  
 *  lastIndexOf will take time as you have to iterate through the whole list
 */
public class UnrolledLinkedList<T> {
	private static class CircularNode<T> {
		private CircularNode<T> next;
		private T elem;

		public CircularNode(T e, CircularNode<T> n) {
			next = n;
			elem = e;
		}

		public T getElement() {
			return elem;
		}

		public CircularNode<T> getNext() {
			return next;
		}

		public void setNext(CircularNode<T> n) {
			next = n;
		}

		public void setElement(T e) {
			elem = e;
		}

		public String toString() {
			return elem + " ";
		}
	}

	private class BlockNode {
		BlockNode next;
		int numNodes;

		// head will be the first node, next will be the tail
		CircularNode<T> head;

		public BlockNode() {
			next = null;
			numNodes = 0;
			head = null;
		}

		public boolean isEmpty() {
			return numNodes() == 0;
		}

		public boolean isLastBlockNode() {
			return next == null;
		}

		public boolean isOverCapacity() {
			return isLastBlockNode() ? numNodes() > blockCapacity : numNodes() > halfCapacity;
		}

		public void insert(T e) {
			insert(numNodes(), e);
		}

		public void insert(int index, T e) {
			if (index < 0 || index > numNodes()) {
				throw new IndexOutOfBoundsException("Invalid index" + index);
			}

			if (isEmpty()) {
				head = new CircularNode<>(e, head);
				head.setNext(head);
			} else if (index == 0 || index == numNodes()) {
				CircularNode<T> newTail = new CircularNode<>(e, head.getNext());
				head.setNext(newTail);

				if (index == 0) {
					head = newTail;
				}
			} else {
				CircularNode<T> beforeNode = getNode(index);
				CircularNode<T> newNode = new CircularNode<>(e, beforeNode.getNext());
				beforeNode.setNext(newNode);
			}

			numNodes++;
		}

		private int getBeforeIndex(int i) {
			if (i < 0 || i >= numNodes()) {
				return -1;
			}

			return (i + 1) % numNodes();
		}

		/*
		 * returns head if index >= total nodes index 1 takes the longest as it's the
		 * end of the list
		 */
		public CircularNode<T> getNode(int index) {
			if (index == 0) {
				return head;
			} else {
				CircularNode<T> temp = head;
				while (index < numNodes()) {
					temp = temp.getNext();
					index++;
				}
				return temp;
			}
		}

		public String toString() {
			String blockNodeInfo = "BlockNode elements: " + numNodes() + "\n";
			CircularNode<T> temp = head;
			if (head != null) {
				blockNodeInfo += temp.toString() + recursiveOutput(temp.getNext());
			}

			blockNodeInfo += "\n";
			return blockNodeInfo;
		}

		private String recursiveOutput(CircularNode<T> currNode) {
			if (currNode == head) {
				return "";
			}
			return recursiveOutput(currNode.getNext()) + currNode.toString();
		}

		public int indexOf(T t) {
			if (isEmpty()) {
				return -1;
			}

			CircularNode<T> currNode = head.getNext();
			int index = numNodes() - 1, found = -1;
			while (index >= 0) {
				if (currNode.getElement().equals(t)) {
					found = index;
				}
				currNode = currNode.getNext();
				index--;
			}
			return found;
		}

		public int lastIndexOf(T t) {
			if (isEmpty()) {
				return -1;
			}

			CircularNode<T> currNode = head.getNext();
			int index = numNodes() - 1;
			while (index >= 0) {
				if (currNode.getElement().equals(t)) {
					return index;
				}
				currNode = currNode.getNext();
				index--;
			}
			return -1;
		}

		public T remove(int index) {
			checkValidIndex(index);

			T prevElem = null;
			int beforeIndex = getBeforeIndex(index);
			CircularNode<T> beforeNode = getNode(beforeIndex);
			prevElem = beforeNode.getNext().getElement();
			beforeNode.setNext(beforeNode.getNext().getNext());

			numNodes--;
			if (index == 0) {
				head = beforeNode;
			}

			if (isEmpty()) {
				head = null;
			}
			return prevElem;
		}

		public int numNodes() {
			return numNodes;
		}

		private void checkValidIndex(int index) {
			if (index < 0 || index >= numNodes()) {
				throw new IndexOutOfBoundsException("Invalid index" + index);
			}
		}
	}

	private BlockNode head;
	private final int blockCapacity;
	private final int halfCapacity;
	private int size;

	public UnrolledLinkedList(int capacity) {
		head = null;
		capacity = capacity < 2 ? 2 : capacity;
		blockCapacity = capacity;
		halfCapacity = blockCapacity % 2 == 0 ? blockCapacity / 2 : blockCapacity / 2 + 1;
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int capacity() {
		return blockCapacity;
	}

	public void insert(T e) {
		insert(size(), e);
	}

	public void insert(int index, T e) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		}

		if (isEmpty()) {
			head = new BlockNode();
			head.insert(e);
		} else {
			BlockNode insertNode = getBlockNode(calcBlockNodeOffset(index));
			insertNode.insert(calcInnerBlockIndex(index), e);
			if (insertNode.isOverCapacity()) {
				shift(insertNode);
			}
		}

		size++;
	}

	private void shift(BlockNode insertNode) {
		while (insertNode.isOverCapacity() && !insertNode.isLastBlockNode()) {
			T lastElement = insertNode.remove(insertNode.numNodes() - 1);
			insertNode = insertNode.next;
			insertNode.insert(0, lastElement);
		}

		if (insertNode.isLastBlockNode() && insertNode.isOverCapacity()) {
			splitLastBlockNode(insertNode);
		}
	}

	private BlockNode splitLastBlockNode(BlockNode lastBlock) {
		if (lastBlock != getLastBlock()) {
			throw new IllegalArgumentException("Not the last BlockNode");
		}

		CircularNode<T> newLastBlockHead = lastBlock.getNode(halfCapacity);
		CircularNode<T> newLastBlockTail = lastBlock.head.getNext();

		lastBlock.head.setNext(newLastBlockHead.getNext());
		lastBlock.numNodes = halfCapacity;

		BlockNode newLastBlock = new BlockNode();
		newLastBlock.head = newLastBlockHead;
		newLastBlock.head.setNext(newLastBlockTail);
		newLastBlock.numNodes = blockCapacity - halfCapacity + 1;

		lastBlock.next = newLastBlock;
		return newLastBlock;
	}

	public boolean contains(T e) {
		return indexOf(e) != -1;
	}

	public int indexOf(T e) {
		int index = 0;
		BlockNode curr = head;
		while (curr != null) {
			int innerListIndex = curr.indexOf(e);

			if (innerListIndex == -1) {
				index += curr.numNodes();
			} else {
				return index + innerListIndex;
			}

			curr = curr.next;
		}

		return -1;
	}

	public int lastIndexOf(T e) {

		int index = 0, lastFound = -1;
		BlockNode curr = head;
		while (curr != null) {
			int innerListIndex = curr.lastIndexOf(e);

			if (innerListIndex != -1) {
				lastFound = index + innerListIndex;
			}

			index += curr.numNodes();

			curr = curr.next;
		}
		return lastFound;
	}

	public T set(int index, T e) {
		checkValidIndex(index);

		int blockOffset = calcBlockNodeOffset(index);
		BlockNode curr = getBlockNode(blockOffset);
		int innerBlockIndex = calcInnerBlockIndex(index);

		CircularNode<T> node = curr.getNode(innerBlockIndex);
		T prevElem = node.getElement();
		node.setElement(e);
		return prevElem;
	}

	public T get(int index) {
		checkValidIndex(index);

		int blockOffset = calcBlockNodeOffset(index);
		BlockNode curr = getBlockNode(blockOffset);
		int innerBlockIndex = calcInnerBlockIndex(index);

		return curr.getNode(innerBlockIndex).getElement();
	}

	private int calcBlockNodeOffset(int index) {
		// allow size for inserts
		if (index == size()) {
			index--;
		}

		int block = 0;
		BlockNode curr = head;
		while (curr != null && index >= curr.numNodes()) {
			block++;
			index -= curr.numNodes();
			curr = curr.next;
		}

		return block;
	}

	private int calcInnerBlockIndex(int index) {
		BlockNode curr = head;
		while (curr != null && index >= curr.numNodes()) {
			index -= curr.numNodes();

			if (curr.next == null && index == 0) {
				return curr.numNodes();
			}
			curr = curr.next;
		}

		return curr == null ? -1 : index;
	}

	private BlockNode getBlockNode(int block) {
		BlockNode curr = head;
		while (block > 0) {
			curr = curr.next;
			block--;
		}
		return curr;
	}

	public T remove(int index) {
		checkValidIndex(index);

		int blockOffset = calcBlockNodeOffset(index);
		BlockNode curr = getBlockNode(blockOffset);
		int innerBlockIndex = calcInnerBlockIndex(index);

		T removeElem = curr.remove(innerBlockIndex);

		if (curr.isEmpty()) {
			removeBlock(blockOffset);
		}
		size--;
		return removeElem;
	}

	private void checkValidIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		}
	}

	private void removeBlock(int block) {
		BlockNode currBlock = getBlockNode(block);
		BlockNode prevBlock = getBlockNode(block - 1);

		if (currBlock == prevBlock) {
			head = head.next;
		} else {
			prevBlock.next = currBlock.next;
		}
	}

	private BlockNode getLastBlock() {
		if (isEmpty()) {
			return null;
		}

		BlockNode curr = head;
		while (!curr.isLastBlockNode()) {
			curr = curr.next;
		}
		return curr;
	}

	public void printList() {
		if (isEmpty()) {
			System.out.println("List is empty");
		}
		BlockNode currNode = head;
		while (currNode != null) {
			System.out.println(currNode.toString());
			currNode = currNode.next;
		}
	}
}
