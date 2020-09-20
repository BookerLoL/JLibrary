package test.util;

import main.util.RandomEx;

public class RandomExTester {
	private static RandomEx randex = new RandomEx(0);

	public static void main(String[] args) {
		int upToNumberCount = 5000;
		long upToSeed = 5000L;
		testNextInt("nextInt", -1000, 1000, upToNumberCount, upToSeed);
		testNextFloat("nextFloat", -1000.0f, 1000.0f, upToNumberCount, upToSeed);
		testNextDouble("nextDouble", -1000.0d, 1000.0d, upToNumberCount, upToSeed);
	}

	private static boolean testNextInt(String methodName, int min, int max, int resetCount, long maxSeed) {
		resetCount = Math.max(10, resetCount);
		int numberCount = resetCount;
		long seed = 0;
		randex.setSeed(seed);
		while (seed < maxSeed) {
			if (numberCount == resetCount) {
				randex.setSeed(seed++);
				numberCount = 0;
			}
			int randNumber = randex.nextInt(min, max);
			if (randNumber < min) {
				System.out.println(methodName + " failed: " + randNumber + " less than min: " + min);
				return false;
			} else if (randNumber > max) {
				System.out.println(methodName + " failed: " + randNumber + " greater than max: " + max);
				return false;
			}

			numberCount++;
		}
		return true;
	}

	private static boolean testNextFloat(String methodName, float min, float max, int resetCount, long maxSeed) {
		resetCount = Math.max(10, resetCount);
		int numberCount = resetCount;
		long seed = 0;
		randex.setSeed(seed);
		while (seed < maxSeed) {
			if (numberCount == resetCount) {
				randex.setSeed(seed++);
				numberCount = 0;
			}
			float randNumber = randex.nextFloat(min, max);
			if (randNumber < min) {
				System.out.println(methodName + " failed: " + randNumber + " less than min: " + min);
				return false;
			} else if (randNumber > max) {
				System.out.println(methodName + " failed: " + randNumber + " greater than max: " + max);
				return false;
			}
			numberCount++;
		}
		return true;
	}

	private static boolean testNextDouble(String methodName, double min, double max, int resetCount, long maxSeed) {
		resetCount = Math.max(10, resetCount);
		int numberCount = resetCount;
		long seed = 0;
		randex.setSeed(seed);
		while (seed < maxSeed) {
			if (numberCount == resetCount) {
				randex.setSeed(seed++);
				numberCount = 0;
			}
			double randNumber = randex.nextDouble(min, max);
			if (randNumber < min) {
				System.out.println(methodName + " failed: " + randNumber + " less than min: " + min);
				return false;
			} else if (randNumber > max) {
				System.out.println(methodName + " failed: " + randNumber + " greater than max: " + max);
				return false;
			}

			numberCount++;
		}
		return true;
	}
}
