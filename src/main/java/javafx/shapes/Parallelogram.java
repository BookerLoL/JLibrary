package main.javafx.shapes;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * A class to help create a parallelogram.
 * 
 * You are also able to draw a square, rectanlge, rhombus as well.
 * 
 * You are responsible for updating the points.
 * 
 * @author Ethan
 * 
 */
public class Parallelogram extends Polygon {
	public Parallelogram(double squareLength) {
		this(90.0, squareLength, squareLength);
	}

	public Parallelogram(double acuteAngle, double rhombusLength) {
		this(acuteAngle, rhombusLength, rhombusLength);
	}

	public Parallelogram(double acuteAngle, double baseLength, double slantedLength) {
		if (acuteAngle % 360 == 0) {
			double maxLength = Math.max(baseLength, slantedLength);
			this.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 1.0, maxLength, 1.0, maxLength, 0.0 });
		} else if (acuteAngle % 180 == 0) {
			double totalLength = baseLength + slantedLength;
			this.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 1.0, totalLength, 1.0, totalLength, 0.0 });
		} else {
			Rotate rotation = new Rotate(acuteAngle, 0.0, 0.0);
			Point2D angledLine = rotation.transform(slantedLength, 0.0);
			this.getPoints().addAll(new Double[] { 0.0, 0.0, angledLine.getX(), angledLine.getY(),
					angledLine.getX() + baseLength, angledLine.getY(), baseLength, 0.0 });
		}
	}
}
