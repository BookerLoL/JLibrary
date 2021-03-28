package main.javafx.learn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

//Inspired by Almas Baimagambetov
public class BasicClickGame extends Application {
	private static final int W = 800, H = 800;
	Rectangle target = new Rectangle(50, 50);
	private SimpleIntegerProperty score = new SimpleIntegerProperty(0);
	double currentDurationSeconds = 1.0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {

		Label scoreText = new Label("Score: " + score.get());
		scoreText.setFont(Font.font("arial", FontWeight.BOLD, 24));

		Timeline timeline = new Timeline();
		setTimer(timeline);

		target.setOnMouseClicked(clickEvent -> {
			score.set(score.get() + 1);
			scoreText.setText(String.format("Score: %d", score.get()));

			if (score.get() % 10 == 0) {
				currentDurationSeconds *= 0.9;
				setTimer(timeline);
			}
		});

		target.setTranslateX(Math.random() * (W - target.getWidth()));
		target.setTranslateY(Math.random() * (H - target.getHeight()));

		Pane root = new Pane();
		root.getChildren().addAll(scoreText, target);
		root.setPrefSize(W, H);

		mainStage.setScene(new Scene(root));
		mainStage.setTitle("Simple Click Target Game");
		mainStage.show();
	}

	private void setTimer(Timeline timer) {
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(currentDurationSeconds), event -> {
			target.setTranslateX(Math.random() * (W - target.getWidth()));
			target.setTranslateY(Math.random() * (H - target.getHeight()));
		});
		timer.stop();
		timer.getKeyFrames().setAll(keyFrame);
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
		target.setTranslateX(Math.random() * (W - target.getWidth()));
		target.setTranslateY(Math.random() * (H - target.getHeight()));
	}
}
