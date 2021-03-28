package main.javafx.shapes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Tester extends Application {
	private static final double HEIGHT = 600, WIDTH = 800;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane content = new Pane();
		content.setPrefSize(WIDTH, HEIGHT);

		Circle a = new Circle(100.0);
		a.setFill(Color.RED);
		Circle b = new Circle(100.0);
		b.setFill(Color.YELLOW);
		Circle c = new Circle(100.0);
		c.setFill(Color.GREEN);

		a.setTranslateX(WIDTH / 2);
		a.setTranslateY(HEIGHT / 4);
		b.setTranslateX(WIDTH / 2 + 100);
		b.setTranslateY(HEIGHT / 4);
		c.setTranslateX(WIDTH / 2 + 50);
		c.setTranslateY(HEIGHT / 4 + 100);

		// List<Shape> shapes = DiagramUtil.createVennDiagramSections(a, b, c);
		// shapes.forEach(shape -> shape.setFill(Color.color(Math.random(),
		// Math.random(), Math.random())));
		Shape i = Shape.intersect(a, b);
		Shape i2 = Shape.intersect(a, c);
		Shape i3 = Shape.intersect(i2, i);
		Shape i4 = Shape.subtract(i2, i3);
		Shape i5 = Shape.intersect(i, i4);
		Path p = (Path) i5;

		System.out.println(((Path) i4).getElements().size());
		System.out.println(p.getElements().size());
		content.getChildren().addAll(i4, p);
		System.out.println(p.getLayoutBounds());
		System.out.println(i4.getLayoutBounds());
		Scene scene = new Scene(content);
		stage.setTitle("Tester Program");
		stage.setScene(scene);
		stage.show();
	}
}
