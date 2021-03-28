package main.javafx.learn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

//Inspired by Almas Baimagambetov
public class Pong extends Application {

	private static final double FPS = 60;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;
	private static final int BALL_RADIUS = 25;
	private static final int PADDLE_HEIGHT = 30, PADDLE_WIDTH = 120;

	private Circle ball = new Circle(25);
	private Rectangle paddle = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
	private boolean leftDir, upDir;

	private int paddleDirection = 0; // [-1, 0, 1]
	private boolean isRunning = false;
	private Timeline timeline = new Timeline();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane gameContent = this.getGameScreen();
		Scene scene = new Scene(gameContent);

		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case A:
				paddleDirection = -1;
				break;
			case D:
				paddleDirection = 1;
				break;
			default:
				break;
			}
		});

		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case A:
				paddleDirection = 0;
				break;
			case D:
				paddleDirection = 0;
				break;
			default:
				break;
			}
		});

		stage.setScene(scene);
		stage.show();
		startGame();
	}

	private Pane getGameScreen() {
		Pane root = new Pane();
		root.setPrefSize(WIDTH, HEIGHT);

		ball.setFill(Color.BLUE);

		paddle.setFill(Color.RED);
		paddle.setTranslateX(WIDTH / 2);
		paddle.setTranslateY(HEIGHT - PADDLE_HEIGHT);

		KeyFrame frame = new KeyFrame(Duration.seconds(1 / FPS), event -> {
			if (!isRunning) {
				return;
			}

			switch (paddleDirection) {
			case -1:
				if (paddle.getTranslateX() - 5 >= 0) {
					paddle.setTranslateX(paddle.getTranslateX() - 5);
				}
				break;
			case 1:
				if (paddle.getTranslateX() + 5 <= WIDTH - PADDLE_WIDTH) {
					paddle.setTranslateX(paddle.getTranslateX() + 5);
				}
				break;
			default:
				break;
			}

			ball.setTranslateX(ball.getTranslateX() + (leftDir ? -5 : 5));
			ball.setTranslateY(ball.getTranslateY() + (upDir ? -5 : 5));

			// Which wall did it hit? Left or right
			if (ball.getTranslateX() - BALL_RADIUS == 0) {
				leftDir = false;
			} else if (ball.getTranslateX() == WIDTH - BALL_RADIUS) {
				leftDir = true;
			}

			if (ball.getTranslateY() - BALL_RADIUS == 0) {
				upDir = false;
			} else if (ball.getTranslateY() + BALL_RADIUS == HEIGHT - PADDLE_HEIGHT
					&& ball.getTranslateX() + BALL_RADIUS >= paddle.getTranslateX()
					&& ball.getTranslateX() - BALL_RADIUS <= paddle.getTranslateX() + PADDLE_WIDTH) {
				upDir = true;
			}

			if (ball.getTranslateY() + BALL_RADIUS == HEIGHT) {
				restartGame();
			}
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		root.getChildren().addAll(paddle, ball);
		return root;
	}

	private void restartGame() {
		stopGame();
		startGame();
	}

	private void startGame() {
		timeline.play();
		isRunning = true;
		upDir = true;
		timeline.play();
		ball.setTranslateX(WIDTH / 2);
		ball.setTranslateY(HEIGHT / 2);
	}

	private void stopGame() {
		isRunning = false;
		timeline.stop();
	}
}
