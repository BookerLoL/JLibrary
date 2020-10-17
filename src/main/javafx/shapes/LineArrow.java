package main.javafx.shapes;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

/**
 * A default Arrow implementation that relies on using a line as it's body and a
 * polyine shape to represent the head.
 * 
 * By default you are given a horizontal arrow with a triangle head.
 * 
 * @author Ethan
 * 
 */
public class LineArrow extends Arrow<Line, Polyline> {
	public LineArrow() {
		super(new Line(), new Polyline());

		double endX = 50.0;
		body.setEndX(endX);
		head.getPoints().addAll(createTriangle(endX, 0.0, 10));
	}

	/**
	 * Assumes you have handled the connection of the body and head.
	 * 
	 * @param body The body of the arrow.
	 * @param head The head of the arrow.
	 */
	public LineArrow(Line body, Polyline head) {
		super(body, head);
	}

	private Double[] createTriangle(double startPointX, double startPointY, double distance) {
		return new Double[] { startPointX, startPointY - distance, startPointX, startPointY + distance,
				startPointX + distance, startPointY, startPointX, startPointY - distance };
	}
}
