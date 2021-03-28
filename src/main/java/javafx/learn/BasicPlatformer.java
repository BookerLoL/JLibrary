package main.javafx.learn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Inspired by Almas Baimagambetov
public class BasicPlatformer extends Application {
	private static class LevelData {
		public static final String[][] LEVELS = { { "000000000000000000000000000000", "000000000000000000000000000000",
				"000000000000000000000000000000", "000000000000000000000000000000", "000000000000000000000000000000",
				"000000000000000000000000000000", "000000000000000000000000000000", "000111000000000000000000000000",
				"000000001110000000000000000000", "000000200000011100000000000000", "000001110000000000111000110000",
				"111111100111100011111001111111" } };
	}

	private static final int BLOCK_SIZE = 60;

	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	private ArrayList<Node> platforms = new ArrayList<>();
	private ArrayList<Node> coins = new ArrayList<>();

	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private Pane uiRoot = new Pane();

	private Node player;
	private Point2D playerVelocity = new Point2D(0, 0);
	private boolean canJump = true;
	private boolean isRunning = true, dialogEvent = false;

	private int levelWidth;

	public static void main(String[] args) {
		launch(args);
	}

	private void initContent() {
		Rectangle bg = new Rectangle(1280, 720);
		String[] level = LevelData.LEVELS[0];

		levelWidth = level[0].length() * BLOCK_SIZE;

		for (int i = 0; i < level.length; i++) {
			String line = level[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
				case '1':
					Node platform = createEntity(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, Color.BROWN);
					platforms.add(platform);
					break;
				case '2':
					Node coin = createEntity(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, Color.GOLD);
					coins.add(coin);
					break;
				default:
					break;
				}
			}
		}

		player = createEntity(0, 600, 40, 40, Color.BLUE);
		player.translateXProperty().addListener((obs, old, newVal) -> {
			int offset = newVal.intValue();

			if (offset > 640 && offset < levelWidth - 640) {
				System.out.println("here");
				gameRoot.setLayoutX(-(offset - 640));
			}
		});

		appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
	}

	private Node createEntity(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		entity.getProperties().put("alive", true);

		gameRoot.getChildren().add(entity);
		return entity;
	}

	private void update() {
		if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
			jumpPlayer();
		}

		if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
			movePlayerX(-5);
		}

		if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
			movePlayerX(5);
		}

		if (playerVelocity.getY() < 10) {
			playerVelocity = playerVelocity.add(0, 1);
		}

		movePlayerY((int) playerVelocity.getY());
		checkCoinCollision();
	}

	private void checkCoinCollision() {
		for (Node coin : coins) {
			if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
				coin.getProperties().put("alive", false);
				dialogEvent = true;
				isRunning = false;
			}
		}

		Iterator<Node> coinIter = coins.iterator();
		while (coinIter.hasNext()) {
			Node c = coinIter.next();
			if (!(Boolean) c.getProperties().get("alive")) {
				coinIter.remove();
				gameRoot.getChildren().remove(c);
			}
		}
	}

	private void movePlayerY(int value) {
		if (player.getTranslateY() >= 620) {
			gameRoot.setLayoutX(0);
			player.setTranslateY(599);
			player.setTranslateX(0);
			return;
		}

		boolean movingDown = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingDown) {
						if (player.getTranslateY() + 40 == platform.getTranslateY()) {
							player.setTranslateY(player.getTranslateY() - 1);
							canJump = true;
							return;
						}

					} else {
						if (player.getTranslateY() == platform.getTranslateY() + BLOCK_SIZE) {
							return;
						}
					}
				}
			}
			player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
		}
	}

	private void movePlayerX(int value) {
		boolean movingRight = value > 0;
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (player.getTranslateX() + 40 == platform.getTranslateX()) {
							return;
						}
					} else {
						if (player.getTranslateX() == platform.getTranslateX() + BLOCK_SIZE) {
							return;
						}
					}
				}
			}
			player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
		}
	}

	private void jumpPlayer() {
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -30);
			canJump = false;
		}
	}

	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	@Override
	public void start(Stage stage) throws Exception {
		initContent();
		Scene scene = new Scene(appRoot);
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		stage.setScene(scene);
		stage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (isRunning) {
					update();
				}
				
				if (dialogEvent) {
					dialogEvent = false;
					keys.keySet().forEach(key -> keys.put(key, false));
					
					GameDialog dialog = new GameDialog();
					dialog.setOnCloseRequest(event -> {
						if (dialog.isCorrect()) {
							//do sth
						} else {
							//do sth
						}
						isRunning = true;
					});
					dialog.open();
				}
			}
		};
		timer.start();
	}
	
	private static class GameDialog extends Stage {
		private Text questionT = new Text();
		private TextField answerTF = new TextField();
		private Text answerT = new Text(); 
		private boolean correct = false;
		
		public GameDialog() {
			Button btnSubmit = new Button("Submit");
			btnSubmit.setOnAction(event -> {
				answerTF.setEditable(false);
				answerT.setVisible(true);
				correct = answerTF.getText().trim().equals(answerT.getText());
			});
			
			VBox vbox = new VBox(10, questionT, answerTF, answerT, btnSubmit);
			vbox.setAlignment(Pos.CENTER);
			Scene scene = new Scene(vbox);
			setScene(scene);
			initModality(Modality.APPLICATION_MODAL);
		}
		
		public void open() {
			questionT.setText("What is 1+1?");
			answerTF.setText("");
			answerTF.setEditable(true);
			answerT.setText("2");
			answerT.setVisible(false);
			correct = false;
			show();
		}
		
		public boolean isCorrect() {
			return correct;
		}
	}
}
