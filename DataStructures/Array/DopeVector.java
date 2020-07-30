package arrays;

public class DopeVector {
	private Object[] dopeVector;
	
	public DopeVector(String type, int rank, int[] extendsOfArray, int stride, Object aryReference) {
		dopeVector = new Object[5];
		dopeVector[0] = type; //class
		dopeVector[1] = rank; //# of subscripts
		dopeVector[2] = extendsOfArray; //# elements in dimension
		dopeVector[3] = stride; //# bytes from one element o another
		dopeVector[4] = aryReference; //pointer to array
	}
	
	public String getType() {
		return (String) dopeVector[0];
	}
	
	public int getRank() {
		return (int) dopeVector[1];
	}
	
	public int getExtends(int dimension) {
		int[] extendsOfArray = (int[]) dopeVector[2];
		if (dimension < 0 || dimension >= extendsOfArray.length) {
			throw new IndexOutOfBoundsException();
		}
		return extendsOfArray[dimension];
	}
	
	public int getStride() {
		return (int) dopeVector[3];
	}
	
	public Object getArray() {
		return dopeVector[4];
	}
}
