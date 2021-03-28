package main.javafx.shapes;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * A class to help create a triangle through various ways.
 * 
 * If the angle is invalid for a triangle (180 or 360), a rectangle is created instead.
 * 
 * This is an extension of a polygon, so intially the fill color is black.
 * 
 * @author Ethan
 * 
 */
public class Triangle extends Polygon {
	/**
	 * A constructor to create a right triangle.
	 * 
	 * @param legA the vertical leg
	 * @param legB the horizontal leg
	 */
	public Triangle(double legA, double legB) {
		this(90.0, legA, legB);
	}

	public Triangle(double equilateralLeg) {
		this(60.0, equilateralLeg, equilateralLeg);
	}

	public Triangle(double angle, double legA, double legB) {
		if (angle % 360 == 0) {
			double maxLength = Math.max(legA, legB);
			this.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 1.0, maxLength, 1.0, maxLength, 0.0 });
		} else if (angle % 180 == 0) {
			double totalLength = legA + legB;
			this.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 1.0, totalLength, 1.0, totalLength, 0.0 });
		} else {
			Rotate rotation = new Rotate(angle, 0.0, 0.0);
			Point2D rotatedPoint = rotation.transform(legB, 0.0);
			this.getPoints().addAll(new Double[] { 0.0, 0.0, legA, 0.0, rotatedPoint.getX(), rotatedPoint.getY() });
		}
	}
}
