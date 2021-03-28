package main.graphics;

import java.util.Arrays;

//I try to reduce extra for loops for the cost of readability
//Didn't provide all the safety checks for arguments 
public class ImmutableVector implements Cloneable {
	private final double[] numbers;

	public ImmutableVector(int dimensions) {
		this(dimensions, 0.0);
	}

	public ImmutableVector(int dimensions, double fillNumber) {
		numbers = new double[dimensions];
		Arrays.fill(numbers, fillNumber);
	}

	public ImmutableVector(double[] numbers) {
		this(numbers, true);
	}

	private ImmutableVector(double[] numbers, boolean copy) {
		this.numbers = copy ? Arrays.copyOf(numbers, numbers.length) : numbers;
	}

	public int dimensions() {
		return numbers.length;
	}

	public double get(int element) {
		return numbers[element];
	}

	// ||v||, length
	public double magnitude() {
		return Math.sqrt(quickMagnitude());
	}

	public static ImmutableVector zeroVector(int dimensions) {
		return new ImmutableVector(dimensions, 0.0);
	}

	// Determines the vector that would direct startPoint to endPoint
	public static ImmutableVector displace(double[] startPoint, double[] endPoint) {
		if (startPoint.length != endPoint.length) {
			throw new IllegalArgumentException("Both inputs should have the same length");
		}

		double[] results = new double[startPoint.length];
		for (int i = 0; i < startPoint.length; i++) {
			results[i] = endPoint[i] - startPoint[i];
		}
		return new ImmutableVector(results, false);
	}

	// useful for game programming when don't need true length
	public double quickMagnitude() {
		double total = 0.0;
		for (double num : numbers) {
			total += num * num;
		}
		return total;
	}

	// normalize vector to have magnitude/length 1
	public ImmutableVector normalize() {
		return scalarMultiply(1.0 / magnitude());
	}

	// some scalar k, some vector v: kv
	public ImmutableVector scalarMultiply(double scalar) {
		double[] copy = new double[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			copy[i] = numbers[i] * scalar;
		}
		return new ImmutableVector(copy, false);
	}

	// v + o
	public ImmutableVector add(ImmutableVector other) {
		checkValid(other);
		double[] sumNumbers = new double[numbers.length];
		for (int i = 0; i < other.numbers.length; i++) {
			sumNumbers[i] = numbers[i] + other.numbers[i];
		}
		return new ImmutableVector(sumNumbers, false);
	}

	// v - o
	public ImmutableVector subtract(ImmutableVector other) {
		checkValid(other);
		double[] diffNumbers = new double[numbers.length];
		for (int i = 0; i < other.numbers.length; i++) {
			diffNumbers[i] = numbers[i] - other.numbers[i];
		}
		return new ImmutableVector(diffNumbers, false);
	}

	// v dot o = ||u|| ||v|| cos theta
	public double dotProduct(ImmutableVector other) {
		checkValid(other);

		double resultant = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			resultant += (numbers[i] * other.numbers[i]);
		}
		return resultant;
	}

	// Java math functions returns answers in radians
	public static double angle(double radians) {
		return radians * (180 / Math.PI);
	}

	public static double radians(double angle) {
		return angle * (Math.PI / 180);
	}

	public double angleOf() {
		return angleOf(0);
	}

	/*
	 * Get angle based on which axis to base it off of.
	 * 
	 * 0 = X, 1 = Y, 2 = Z
	 * 
	 * Most times you want X axis angle as that's the norm when asked for the angle
	 * that the vector creates.
	 * 
	 * Returns angle between -180 <= angle <= 180
	 */
	public double angleOf(int dimension) throws UnsupportedOperationException {
		if (dimension < 0 || dimension >= dimensions()) {
			throw new IllegalArgumentException("Dimension must be between 0 and dimensions()-1, input: " + dimension);
		}

		if (dimensions() == 2) {
			return dimension == 0 ? angle(Math.atan2(numbers[1], numbers[0]))
					: angle(Math.atan2(numbers[0], numbers[1]));
		} else if (dimensions() == 3) {
			if (dimension == 0) { // radians
				return angle(Math.atan2(Math.sqrt((numbers[1] * numbers[1]) + numbers[2] * numbers[2]), numbers[0]));
			} else if (dimension == 1) {
				return angle(Math.atan2(Math.sqrt((numbers[0] * numbers[0]) + numbers[2] * numbers[2]), numbers[1]));
			} else {
				return angle(Math.atan2(Math.sqrt((numbers[0] * numbers[0]) + numbers[1] * numbers[1]), numbers[2]));
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	// Angle between two vectors
	// eq: v dot o = ||v|| ||u|| cos theta
	// if both normalized, equation simplifies to: v dot u = cos theta
	public double angleOf(ImmutableVector other, boolean bothNormalized) {
		checkValid(other);

		if (bothNormalized) {
			return angle(Math.acos(this.dotProduct(other)));
		}

		double length = 0.0, otherLength = 0.0;
		double dotProduct = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			length += (numbers[i] * numbers[i]);
			otherLength += (other.numbers[i] * other.numbers[i]);
			dotProduct += (numbers[i] * other.numbers[i]);
		}

		length = Math.sqrt(length);
		otherLength = Math.sqrt(otherLength);

		double divisor = length * otherLength;
		return divisor != 0.0 ? angle(Math.acos(dotProduct / divisor)) : Double.MIN_VALUE;
	}

	public double angleOf(ImmutableVector other) {
		return angleOf(other, false);
	}

	// assumes both are nonzero
	public double component(ImmutableVector other) {
		checkValid(other);
		double dotProduct = 0.0;
		double length = 0.0;

		for (int i = 0; i < numbers.length; i++) {
			length += (numbers[i] * numbers[i]);
			dotProduct += (numbers[i] * other.numbers[i]);
		}

		return dotProduct / Math.sqrt(length);
	}

	// w = kv + u -> solve for u
	// kv is figured out in the method, just provide w and v
	// kv = |w| (wu � vu) vu, w will cancel out
	// kv projection of w onto v
	// find orthogonal projection of kv (aka: other)
	public ImmutableVector orthogonalProjection(ImmutableVector other) {
		checkValid(other);
		double lengthOther = 0.0;
		double dotProduct = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			lengthOther += other.numbers[i] * other.numbers[i];
			dotProduct += (numbers[i] * other.numbers[i]);
		}
		ImmutableVector kv = other.scalarMultiply(dotProduct / lengthOther);
		return this.subtract(kv);
	}

	public ImmutableVector crossProduct(ImmutableVector other) {
		checkValid(other);
		if (numbers.length != 3) {
			throw new IllegalArgumentException("Both vectors should be length 3");
		}

		return new ImmutableVector(new double[] { numbers[1] * other.numbers[2] - numbers[2] * other.numbers[1],
				numbers[2] * other.numbers[0] - numbers[0] * other.numbers[2],
				numbers[0] * other.numbers[1] - numbers[1] * other.numbers[0] }, false);
	}

	// https://en.wikipedia.org/wiki/Cauchy%E2%80%93Schwarz_inequality, proof 2
	// eq: ||v dot other||^2 <= ||v||^2 + ||o||^2
	// simplified eq: abs(v dot other) <= ||v|| + ||o|| by squaring rooting both
	// sides
	public boolean cauchySchwarzInequality(ImmutableVector other) {
		checkValid(other);
		double magnitude = 0.0, otherMagnitude = 0.0;
		double dotProduct = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			magnitude += (numbers[i] * numbers[i]);
			otherMagnitude += (other.numbers[i] * other.numbers[i]);
			dotProduct += (numbers[i] * other.numbers[i]);
		}
		return Math.abs(dotProduct) <= magnitude * otherMagnitude;
	}

	// Triangle inequality wikipedia
	// eq: ||v + o|| <= ||v|| + ||o||
	public boolean triangleInequality(ImmutableVector other) {
		checkValid(other);
		double magnitude = 0.0, otherMagnitude = 0.0;
		double sumMagnitude = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			magnitude += numbers[i] * numbers[i];
			otherMagnitude += other.numbers[i] * other.numbers[i];
			sumMagnitude += (numbers[i] + other.numbers[i]) * (numbers[i] + other.numbers[i]);
		}
		return Math.sqrt(sumMagnitude) <= (Math.sqrt(magnitude) + Math.sqrt(otherMagnitude));
	}

	// this, references the slope
	// if 0.0, means it's exactly on the line, > 0 it's above the line, < 0 it's
	// below the line
	// Useful for checking if pixel is inside a triangle
	public double determineEdgeSide(double[] startingPoint, double[] somePoint) {
		if (numbers.length != 2 || startingPoint.length != 2 || somePoint.length != 2) {
			throw new IllegalArgumentException("Can only handle 2D inputs");
		}
		ImmutableVector normal = new ImmutableVector(new double[] { numbers[1], -numbers[0] }, false);
		ImmutableVector pointVector = displace(startingPoint, somePoint);

		return normal.dotProduct(pointVector);
	}

	private void checkValid(ImmutableVector other) throws IllegalArgumentException {
		if (other.numbers.length != numbers.length) {
			throw new IllegalArgumentException("Vectors should have the same size");
		}
	}

	public static boolean isZero(ImmutableVector vector) {
		return isAll(vector, 0.0);
	}
	
	private static boolean isAll(ImmutableVector vector, double value) {
		for (double element : vector.numbers) {
			if (element != value) {
				return false;
			}
		}
		return true;
	}

	public ImmutableVector clone() {
		return new ImmutableVector(numbers, true);
	}

	public String toString() {
		return Arrays.toString(numbers);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof ImmutableVector)) {
			return false;
		}
		
		ImmutableVector other = (ImmutableVector) o;
		
		if (other.numbers.length != numbers.length) {
			return false;
		}
		
		return Arrays.mismatch(numbers, other.numbers) == -1; 
	}

	public static void main(String[] args) {
		test2D();
		test3D();
	}

	private static void test2D() {
		System.out.println("\nTesting 2D\n");

		double[] nums1 = { 1, 2 };
		double[] nums2 = { 3, 1 };
		ImmutableVector v1 = new ImmutableVector(nums1);
		ImmutableVector v2 = new ImmutableVector(nums2);

		double[] slope = { 1, 1 };
		double[] points1 = { -1, -1 };
		double[] points2 = { 1, 1 };
		ImmutableVector vfp1 = new ImmutableVector(slope);
		ImmutableVector v3 = ImmutableVector.displace(points1, points2);

		ImmutableVector testp1 = new ImmutableVector(points1);
		ImmutableVector testp2 = new ImmutableVector(points2);

		ImmutableVector testp3 = testp1.subtract(testp2);
		ImmutableVector testp4 = testp2.subtract(testp1);

		ImmutableVector testAngle1 = new ImmutableVector(new double[] { 3, 4 });
		ImmutableVector testAngle2 = new ImmutableVector(new double[] { -3, 4 });
		ImmutableVector testAngle3 = new ImmutableVector(new double[] { -3, -4 });
		ImmutableVector testAngle4 = new ImmutableVector(new double[] { 3, -4 });
		System.out.println(testAngle1.angleOf(0) + "\t" + testAngle1.angleOf(1));
		System.out.println(testAngle2.angleOf(0) + "\t" + testAngle2.angleOf(1));
		System.out.println(testAngle3.angleOf(0) + "\t" + testAngle3.angleOf(1));
		System.out.println(testAngle4.angleOf(0) + "\t" + testAngle4.angleOf(1));

		ImmutableVector qU = new ImmutableVector(new double[] { 0, 1 });
		ImmutableVector rU = new ImmutableVector(new double[] { -0.5, -0.866 });
		System.out.println(qU.angleOf(rU));

		// [(6, 5), (9, 0)] [(3.2, 7), (8, 4)]
		ImmutableVector ortho1 = new ImmutableVector(new double[] { -5, 5 });
		ImmutableVector ortho2 = new ImmutableVector(new double[] { -3.8, -.8 });
		System.out.println(ortho1.orthogonalProjection(ortho2));
	}

	private static void test3D() {
		System.out.println("\nTesting 3D\n");
		ImmutableVector a = new ImmutableVector(new double[] { 4, 4, 4 });
		ImmutableVector b = new ImmutableVector(new double[] { 4, 0, 4 });
		System.out.println(a.angleOf(b));

		ImmutableVector a1 = new ImmutableVector(new double[] { 2, 4, 6 });
		ImmutableVector b1 = new ImmutableVector(new double[] { 6, 4, 3 });
		System.out.println(a1.angleOf(b1));

		ImmutableVector ortho1 = new ImmutableVector(new double[] { 4, 2.5, 2.5 });
		ImmutableVector ortho2 = new ImmutableVector(new double[] { 5, 0, 3.1 });
		System.out.println(ortho1.orthogonalProjection(ortho2));

		ImmutableVector c1 = new ImmutableVector(new double[] { 1, 2, 1 });
		ImmutableVector c2 = new ImmutableVector(new double[] { 0, -1, 2 });
		System.out.println(c1.crossProduct(c2));
	}
}
