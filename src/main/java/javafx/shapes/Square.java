package main.javafx.shapes;

import javafx.scene.shape.Rectangle;

/**
 * A class to help create a square shape
 * 
 * The square will maintain it's properties, meaning that changing the width
 * will also change the height and vice versa.
 * 
 * @author Ethan
 * 
 */
public class Square extends Rectangle {
	public Square(double length) {
		super(length, length);

		widthProperty().bindBidirectional(heightProperty());
	}
}
