package main.designpattern.flyweight;

//https://gameprogrammingpatterns.com/flyweight.html
//Useful if using enums and have lots of switch cases
public class FlyWeightTester {
	public static void main(String[] args) {
		World world = new World();
		for (int x = 0; x < world.getMaxWidth(); x++) {
			for (int y = 0; y < world.getMaxHeight(); y++) {
				System.out.print(world.getTile(x, y).getMovementCost() + " ");
			}
			System.out.println();
		}
	}
}
