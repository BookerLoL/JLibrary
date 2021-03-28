package main.javafx.shapes;

import javafx.scene.Parent;
import javafx.scene.shape.CubicCurve;

/**
 * Still a work in progress. Should display a realistic heart shape object.
 * 
 * TODO: preserve aspect ratios from default heart. Picking different widths and heights cause the heart to look bad.
 * 
 * @author Ethan
 *
 */
public class Heart extends Parent {
	CubicCurve rightHalf = new CubicCurve();
	CubicCurve leftHalf = new CubicCurve();

	public Heart() {
		this(70, 50);
	}

	public Heart(double width, double height) {
		this(width, height, 6.4, -35);
	}

	private Heart(double width, double height, double cX1, double cY1) {
		rightHalf.setEndY(height);
		rightHalf.setControlX1(cX1);
		rightHalf.setControlX2(width);
		rightHalf.setControlY1(-35);

		leftHalf.setEndY(height);
		leftHalf.setControlX1(-cX1);
		leftHalf.setControlX2(-width);
		leftHalf.setControlY1(-35);

		this.getChildren().addAll(rightHalf, leftHalf);
	}

	public CubicCurve getLeftHalf() {
		return leftHalf;
	}

	public CubicCurve getRightHalf() {
		return rightHalf;
	}
}
