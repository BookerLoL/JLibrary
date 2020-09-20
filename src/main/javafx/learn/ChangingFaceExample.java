package main.javafx.learn;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChangingFaceExample extends Application {
	private static final double WIDTH = 800;
	private static final double HEIGHT = 800;

	public static void main(String[] args) {
		Application.launch(ChangingFaceExample.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		final double midX = WIDTH / 2;
		final double midY = HEIGHT / 2;
		double radius = ((WIDTH + HEIGHT) / 6);
		Circle face = new Circle(midX, midY, radius);
		face.setFill(Color.YELLOW);
		face.setStroke(Color.RED);
		
		Circle leftEye = new Circle(midX - midX / 3, midY  - midY/3, radius/11);
		leftEye.setStroke(Color.GRAY);
		leftEye.setFill(Color.YELLOW);
		Circle rightEye = new Circle(midX + midX / 3, midY  - midY/3, radius/11);
		rightEye.setStroke(Color.GRAY);
		rightEye.setFill(Color.YELLOW);
		
		double lengthToMid = (WIDTH + HEIGHT) / 50;
		Polygon triangleNose = new Polygon(midX, midY - lengthToMid, midX + lengthToMid, midY + lengthToMid, midX - lengthToMid, midY + lengthToMid);
		triangleNose.setStroke(Color.GRAY);
		triangleNose.setFill(Color.YELLOW);
		
		Arc smile = new Arc();
		smile.setCenterX(midX);
		smile.setCenterY(midY + midY/3);
		smile.setStartAngle(180);
		smile.setLength(180);
		smile.setRadiusX(WIDTH/6);
		smile.setRadiusY(HEIGHT/10);
		smile.setStroke(Color.GRAY);
		smile.setFill(Color.YELLOW);
		smile.setType(ArcType.OPEN);
		
		Text text = new Text(face.getCenterX() - 50,face.getCenterY() + face.getRadius() + 30, "Smiley Face");
		text.setFill(Color.BLUE);
		text.setFont(Font.font("Verdana", 20));
	
		Group smileGroup = new Group(face, leftEye, rightEye, triangleNose, smile, text);
		
		Button smileButton = new Button("Smile");
		smileButton.setOnAction(e -> {
			smile.setLength(180);
			smile.setRadiusY(HEIGHT/10);
			text.setText("Smiley Face");
		});
		Button frownButton = new Button("Frown");
		frownButton.setOnAction(e -> {
			smile.setLength(-180);
			smile.setRadiusY(HEIGHT/10);
			text.setText("Frowny Face");
		});
		Button straightButton = new Button("Straight");
		straightButton.setOnAction(e -> {
			smile.setRadiusY(0.0);
			text.setText("Straight Face");
		});
		
		HBox buttonRow = new HBox(10);
		buttonRow.setAlignment(Pos.CENTER);
		buttonRow.getChildren().addAll(smileButton, straightButton, frownButton);
		
		VBox mainView = new VBox(10);
		mainView.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
		mainView.getChildren().addAll(buttonRow, smileGroup);
		mainView.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(mainView, WIDTH, HEIGHT, Color.YELLOW);
		stage.setScene(scene);
		stage.setTitle("Changing Face");
		stage.show();
	}
}
