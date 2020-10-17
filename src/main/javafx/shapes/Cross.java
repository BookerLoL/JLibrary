package main.javafx.shapes;

import javafx.scene.Parent;
import javafx.scene.shape.Line;

public class Cross extends Parent {
	Line verticalBar;
	Line horizontalBar;

	public Cross(double width, double height) {
		this(width, height, 25, 25);
	}

	public Cross(double width, double height, double widthThickness, double heightThickness) {
		horizontalBar = new Line();
		verticalBar = new Line();

		horizontalBar.setEndX(width);
		verticalBar.setEndX(height);
		horizontalBar.setStrokeWidth(widthThickness);
		verticalBar.setStrokeWidth(heightThickness);

		double centerXDiff = (width / 2) - (height / 2);
		verticalBar.setTranslateX(centerXDiff);
		verticalBar.setRotate(90);

		this.getChildren().addAll(verticalBar, horizontalBar);
	}

	public Line getVerticalBar() {
		return verticalBar;
	}

	public Line getHorizontalBar() {
		return horizontalBar;
	}
}
