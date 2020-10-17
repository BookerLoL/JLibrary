package main.javafx.shapes;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * A class designed to create a regular n sided polygon.
 * 
 * This does not handle resizing so you would have to recreate a new Polygon or
 * manage the points yourself.
 * 
 * @author Ethan
 * 
 */
public class NRegPolygon extends Polygon {
	public NRegPolygon(int nSides, double length) {
		getPoints().addAll(getPoints(nSides, length));
	}

	/**
	 * Generates a list of points that creates a regular n sided polygon with each
	 * point having a distance of length away from their nearby points.
	 * 
	 * The first point always starts at 0.0
	 * 
	 * @param nSides The number of sides the polygon should have. Expects at least 3
	 *               or more.
	 * @param length A positive non-zero value.
	 * @return A list of points that would create the outline of polygon shape. The
	 *         list is X coordinate then Y coordinate order.
	 */
	public static List<Double> getPoints(int nSides, double length) {
		checkSides(nSides);

		List<Double> points = new ArrayList<>(nSides * 2);
		Point2D start = new Point2D(0.0, 0.0);
		Point2D end = new Point2D(length, 0.0);
		points.add(start.getX());
		points.add(start.getY());
		int numPoints = nSides - 1;
		double angle = calcInteriorAngle(nSides);

		do {
			Rotate r = new Rotate(angle, start.getX(), start.getY());
			Point2D newEnd = r.transform(end);
			points.add(newEnd.getX());
			points.add(newEnd.getY());
			end = start;
			start = newEnd;
			numPoints--;
		} while (numPoints != 0);

		return points;
	}

	public static double calcInteriorAngle(double nSides) {
		return (nSides - 2) * 180 / nSides;
	}

	private static void checkSides(int nSides) {
		if (nSides < 3) {
			throw new IllegalArgumentException("A regular polygon requires at least 3 sides");
		}
	}
}
