package main.javafx.learn;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/*
 * A simple Sierpinkski Triangles program to learn JavaFX. I know there are
 * other more efficient ways of implementing, but I wanted to practice certain
 * concepts in particular rather than optimizing everything.
 * 
 * Sierpinski Triangles Used: Middle point theorem, ColorPicker
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class SierpinskiTriangles extends Application {
	private static final double WIDTH = 800;
	private static final double HEIGHT = 800;
	private static final double INIT_LENGTH = 400;
	private static final int INIT_ITERATIONS = 3;

	private int prevIteration = INIT_ITERATIONS;
	private Color baseTriangleColor = Color.GREEN;
	private Color middleTriangleColor = Color.WHITE;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Group group = new Group();
		drawTriangles(group, INIT_ITERATIONS);

		Label lengthInputLabel = new Label("Iterations: ");
		Slider iterationSlider = new Slider(0, 7, INIT_ITERATIONS);
		iterationSlider.setShowTickLabels(true);
		iterationSlider.setShowTickMarks(true);
		iterationSlider.setMajorTickUnit(1.0);
		iterationSlider.setMinorTickCount(0);
		iterationSlider.setSnapToTicks(true);

		iterationSlider.setOnMouseReleased(e -> {
			int iteration = (int) iterationSlider.getValue();
			if (prevIteration != iteration) {
				drawTriangles(group, iteration);
				prevIteration = iteration;
			}
		});

		HBox iterationContainer = new HBox();
		iterationContainer.getChildren().addAll(lengthInputLabel, iterationSlider);

		Label triangleColorLabel = new Label("Base color: ");
		ColorPicker triangleColorPicker = new ColorPicker(baseTriangleColor);
		triangleColorPicker.valueProperty().addListener((obs, oldV, newV) -> {
			if (oldV != newV) {
				baseTriangleColor = newV;
				drawTriangles(group, (int) iterationSlider.getValue());
			}
		});
		HBox baseColorContainer = new HBox();
		baseColorContainer.getChildren().addAll(triangleColorLabel, triangleColorPicker);

		Label triangleCutoutLabel = new Label("Cutout color: ");
		ColorPicker cutoutColorPicker = new ColorPicker(middleTriangleColor);
		cutoutColorPicker.valueProperty().addListener((obs, oldV, newV) -> {
			if (oldV != newV) {
				middleTriangleColor = newV;
				drawTriangles(group, (int) iterationSlider.getValue());
			}
		});
		HBox cutoutColorContainer = new HBox();
		cutoutColorContainer.getChildren().addAll(triangleCutoutLabel, cutoutColorPicker);

		HBox inputSection = new HBox();
		inputSection.getChildren().addAll(iterationContainer, baseColorContainer, cutoutColorContainer);
		inputSection.setAlignment(Pos.CENTER);
		inputSection.setSpacing(10.0);

		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setCenter(group);
		layout.setBottom(inputSection);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		stage.setTitle("Sierpinski Triangles");
		stage.show();
	}

	private void drawTriangles(Group g, int iterations) {
		g.getChildren().clear();

		double midX = WIDTH / 2;
		double midY = HEIGHT / 2;
		double[] baseTriangleCoordinates = new double[] { midX, midY - INIT_LENGTH / 2, midX - INIT_LENGTH / 2,
				midY + INIT_LENGTH / 2, midX + INIT_LENGTH / 2, midY + INIT_LENGTH / 2 };
		Polygon baseTriangle = new Polygon(baseTriangleCoordinates);
		baseTriangle.setStroke(Color.BLACK);
		baseTriangle.setStrokeWidth(1.0);
		baseTriangle.setFill(baseTriangleColor);

		g.getChildren().add(baseTriangle);
		addTriangles(g, baseTriangleCoordinates, iterations);
	}

	private double[] findMiddleTriangleCoordinates(double[] points) {
		return new double[] { (points[0] + points[2]) / 2, (points[1] + points[3]) / 2, (points[0] + points[4]) / 2,
				(points[1] + points[5]) / 2, (points[2] + points[4]) / 2, (points[3] + points[5]) / 2 };
	}

	/*
	 * Too many small insignificant triangles can cause the program to slow down, so
	 * limited the amount of iterations
	 * 
	 * Could just store all the possible middle triangles rather than recalculating
	 * all these triangles again then just iterate through each phase until the
	 * current iteration matches.
	 */
	private void addTriangles(Group g, double[] baseTriangleCords, int currIteration) {
		if (currIteration <= 0) {
			return;
		}

		double[] middleTriangleCords = findMiddleTriangleCoordinates(baseTriangleCords);
		Polygon middleTriangle = new Polygon(middleTriangleCords);
		middleTriangle.setStroke(Color.BLACK);
		middleTriangle.setStrokeWidth(1.0);
		middleTriangle.setFill(middleTriangleColor);
		g.getChildren().add(middleTriangle);

		double[] topTriangleCords = new double[] { baseTriangleCords[0], baseTriangleCords[1], middleTriangleCords[0],
				middleTriangleCords[1], middleTriangleCords[2], middleTriangleCords[3] };
		double[] leftTriangleCords = new double[] { middleTriangleCords[0], middleTriangleCords[1],
				baseTriangleCords[2], baseTriangleCords[3], middleTriangleCords[4], middleTriangleCords[5] };
		double[] rightTriangleCords = new double[] { middleTriangleCords[2], middleTriangleCords[3],
				middleTriangleCords[4], middleTriangleCords[5], baseTriangleCords[4], baseTriangleCords[5] };

		addTriangles(g, topTriangleCords, currIteration - 1);
		addTriangles(g, leftTriangleCords, currIteration - 1);
		addTriangles(g, rightTriangleCords, currIteration - 1);
	}
}
