package flyweight;

public class World {
	private final int WIDTH = 40;
	private final int HEIGHT = 40;
	private Terrain[][] tiles = new Terrain[WIDTH][HEIGHT];
	private final Terrain GRASS = new Terrain(1, false);
	private final Terrain HILL = new Terrain(3, false);
	private final Terrain RIVER = new Terrain(2, true);
	
	public World() {
		generateTerrain();
	}
	
	private void generateTerrain() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (x % 2 == 0 && y % 2 == 0) {
					tiles[x][y] = HILL;
				} else {
					tiles[x][y] = GRASS;
				}
			}
		}
		
		for (int y = 0; y < HEIGHT; y++) {
			tiles[0][y] = RIVER;
		}
	}
	
	public Terrain getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public int getMaxWidth() {
		return WIDTH;
	}
	
	public int getMaxHeight() {
		return HEIGHT;
	}
}
