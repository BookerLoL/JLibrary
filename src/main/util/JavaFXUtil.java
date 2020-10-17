package main.util;

import javafx.scene.Node;

public class JavaFXUtil {
	/**
	 * Assumes both entities share the same parent. Detects whether or not two
	 * entities are touching each other.
	 * 
	 * @param entity1
	 * @param entity2
	 * @return collision detected
	 */
	public static boolean checkCollision(Node entity1, Node entity2) {
		return entity1.getBoundsInParent().intersects(entity2.getBoundsInParent());
	}
}
