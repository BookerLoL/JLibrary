package flyweight;

public class Terrain {
	private int movementCost;
	private boolean isWater;
	// other shared properties

	public Terrain(int movementCost, boolean isWater) {
		this.movementCost = movementCost;
		this.isWater = isWater;
	}

	public int getMovementCost() {
		return movementCost;
	}

	public boolean isWater() {
		return isWater;
	}
}
