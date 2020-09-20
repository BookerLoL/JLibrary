package main.collections.List;
/*
 * Generalized Lists 
 * 
 * Not much info about the functionalities that should be provided but depends on the application it's used for.
 * 
 * Good for representing equations as the data could be a list of integers to represent polynomials and etc.
 * 
 * 
 */
public class GeneralizedList<T> {
	public class Node {
		private static final byte TERMINATING_FLAG = 0;
		private static final byte NEXT_FLAG = 1;
		private static final byte DOWN_FLAG = 2;
		private static final byte BOTH_FLAG = 3;
		byte flag;
		Node next;
		Node down;
		T data;

		public boolean isTerminating() {
			return flag == 0;
		}

		public boolean hasNext() {
			return flag == 1 || hasBoth();
		}

		public boolean hasDown() {
			return flag == 2 || hasBoth();
		}

		public boolean hasBoth() {
			return flag == 3;
		}
		
		public Node getNext() {
			return next;
		}
		
		public Node getDown() {
			return down;
		}
		
		public T getData() {
			return data;
		}

		public void setNext(Node newNext) {
			next = newNext;
			if (newNext == null) {
				if (flag == BOTH_FLAG) {
					flag = DOWN_FLAG;
				} else if (flag != DOWN_FLAG) {
					flag = TERMINATING_FLAG;
				}
			} else {
				if (flag == DOWN_FLAG) {
					flag = BOTH_FLAG;
				} else if (flag != BOTH_FLAG) {
					flag = NEXT_FLAG;
				}
			}
		}
		
		public void setDown(Node newDown) {
			down = newDown;
			if (newDown == null) {
				if (flag == BOTH_FLAG) {
					flag = NEXT_FLAG;
				} else if (flag != NEXT_FLAG) {
					flag = TERMINATING_FLAG;
				}
			} else {
				if (flag == NEXT_FLAG) {
					flag = BOTH_FLAG;
				} else if (flag != BOTH_FLAG) {
					flag = DOWN_FLAG;
				}
			}
		}
	}
	
	private Node start;
	
	public  GeneralizedList() {
		start = null;
	}
	
	public boolean isEmpty() {
		return start == null;
	}
	
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
