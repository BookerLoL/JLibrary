package main.javafx.learn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

//Inspired by Almas Baimagambetov
/*
 * Modified to have food not appear on the snake.
 * Snake goes to the other side if goes off the scren
 * Added a score
 */
public class SnakeGame extends Application {
	public enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static final int BLOCK_SIZE = 25;
	public static final int WIDTH = 15 * BLOCK_SIZE;
	public static final int HEIGHT = 15 * BLOCK_SIZE;

	private Direction direction = Direction.RIGHT;
	private boolean isRunning = false;
	private boolean moved = false;
	private Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);

	private Timeline timeline = new Timeline();
	private ObservableList<Node> snake;
	IntegerProperty score = new SimpleIntegerProperty(0);

	private Pane getStartContent() {
		Group snakeBody = new Group();
		snake = snakeBody.getChildren();

		food.setFill(Color.RED);
		placeFood();
		// Normalize the position

		Pane content = new Pane();
		content.setPrefSize(WIDTH, HEIGHT);

		KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {
			if (!isRunning) {
				return;
			}

			boolean toRemove = snake.size() > 1;
			Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

			double tailX = tail.getTranslateX();
			double tailY = tail.getTranslateY();

			double headX = snake.get(0).getTranslateX();
			double headY = snake.get(0).getTranslateY();

			switch (direction) {
			case UP:
				tail.setTranslateX(headX);
				tail.setTranslateY(headY - BLOCK_SIZE);
				break;
			case DOWN:
				tail.setTranslateX(headX);
				tail.setTranslateY(headY + BLOCK_SIZE);
				break;
			case LEFT:
				tail.setTranslateX(headX - BLOCK_SIZE);
				tail.setTranslateY(headY);
				break;
			case RIGHT:
				tail.setTranslateX(headX + BLOCK_SIZE);
				tail.setTranslateY(headY);
				break;
			}

			// Bounds checking, wrap it to other side
			if (tail.getTranslateX() < 0) {
				tail.setTranslateX(WIDTH - BLOCK_SIZE);
			} else if (tail.getTranslateX() >= WIDTH) {
				tail.setTranslateX(0);
			}

			if (tail.getTranslateY() < 0) {
				tail.setTranslateY(HEIGHT - BLOCK_SIZE);
			} else if (tail.getTranslateY() >= HEIGHT) {
				tail.setTranslateY(0);
				}

			moved = true;

			if (toRemove) {
				snake.add(0, tail);
			}

			for (Node bodyPart : snake) {
				if (bodyPart != tail && tail.getTranslateX() == bodyPart.getTranslateX()
						&& tail.getTranslateY() == bodyPart.getTranslateY()) {
					restartGame();
					break;
				}
			}

			if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
				Rectangle newTail = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
				newTail.setTranslateX(tailX);
				newTail.setTranslateY(tailY);
				snake.add(newTail);
				placeFood();
				score.setValue(score.get() + 1);
			}
		});

		timeline.getKeyFrames().add(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		content.getChildren().addAll(snakeBody, food);
		return content;
	}

	private void placeFood() {
		// ensuring that food is not on the snake body
		boolean onSnakeBody = false;
		double foodX, foodY;
		do {
			onSnakeBody = false;
			foodX = (int) (Math.random() * (WIDTH - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE;
			foodY = (int) (Math.random() * (HEIGHT - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE;

			for (Node bodyPart : snake) {
				if (foodX == bodyPart.getTranslateX() && foodY == bodyPart.getTranslateY()) {
					onSnakeBody = true;
					break;
				}
			}
		} while (onSnakeBody);

		food.setTranslateX(foodX);
		food.setTranslateY(foodY);
	}

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane screenPane = new BorderPane();
	
		
		Text scoreText = new Text();
		scoreText.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + score.get(), score));
		
		screenPane.setCenter(getStartContent());
		screenPane.setTop(scoreText);
		BorderPane.setAlignment(scoreText, Pos.CENTER_RIGHT);
		
		Scene scene = new Scene(screenPane);
		scene.setOnKeyPressed(event -> {
			if (!moved && isRunning) {
				return;
			}

			switch (event.getCode()) {
			case W:
				if (isRunning && direction != Direction.DOWN) {
					direction = Direction.UP;
				}
				moved = false;
				break;
			case S:
				if (isRunning && direction != Direction.UP) {
					direction = Direction.DOWN;
				}
				moved = false;
				break;
			case A:
				if (isRunning && direction != Direction.RIGHT) {
					direction = Direction.LEFT;
				}
				moved = false;
				break;
			case D:
				if (isRunning && direction != Direction.LEFT) {
					direction = Direction.RIGHT;
				}
				moved = false;
				break;
			case SPACE:
				if (isRunning) {
					pause();
				} else {
					resume();
				}
				break;
			}
		});
		stage.setScene(scene);
		stage.setTitle("Snake Game");
		stage.show();
		startGame();
	}

	private void restartGame() {
		score.setValue(0);
		stopGame();
		startGame();
	}

	private void startGame() {
		direction = Direction.RIGHT;
		snake.add(new Rectangle(BLOCK_SIZE, BLOCK_SIZE));
		timeline.play();
		isRunning = true;
	}

	private void stopGame() {
		isRunning = false;
		timeline.stop();
		snake.clear();
	}

	private void pause() {
		isRunning = false;
		timeline.pause();
	}

	private void resume() {
		isRunning = true;
		timeline.play();
	}

}
