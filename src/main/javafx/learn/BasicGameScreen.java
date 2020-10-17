package main.javafx.learn;

import java.io.InputStream;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

//His work: Almas Baimagambetov
public class BasicGameScreen extends Application {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	private GameMenu gameMenu;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();
		root.setPrefSize(WIDTH, HEIGHT);

		InputStream bgImg = getClass().getResourceAsStream("thief.jpg");
		Image image = new Image(bgImg);
		bgImg.close();

		ImageView imageView = new ImageView(image);
		imageView.fitWidthProperty().bind(root.widthProperty());
		imageView.fitHeightProperty().bind(root.heightProperty());

		gameMenu = new GameMenu();
		gameMenu.setVisible(false);
		root.getChildren().addAll(imageView, gameMenu);

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				if (!gameMenu.isVisible()) {
					FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
					ft.setFromValue(0);
					ft.setToValue(1);
					ft.setOnFinished(e -> gameMenu.setVisible(true));
					ft.play();
				} else {
					FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
					ft.setFromValue(1);
					ft.setToValue(0);
					ft.setOnFinished(e -> gameMenu.setVisible(false));
					ft.play();
				}
			}
		});

		stage.setScene(scene);
		stage.show();
	}

	private static class MenuButton extends StackPane {
		private Text text;

		public MenuButton(String title) {
			text = new Text(title);
			text.setFont(Font.font(24.0));
			text.setFill(Color.WHITE);

			Rectangle background = new Rectangle(250, 30);
			background.setOpacity(0.6);
			background.setFill(Color.BLACK);
			background.setEffect(new GaussianBlur(3.5));
			setAlignment(Pos.CENTER_LEFT);
			setRotate(-0.5);
			getChildren().addAll(background, text);

			this.setOnMouseEntered(event -> {
				background.setTranslateX(10);
				text.setTranslateX(10);
				background.setFill(Color.WHITE);
				text.setFill(Color.BLACK);
			});

			this.setOnMouseExited(event -> {
				background.setTranslateX(0);
				text.setTranslateX(0);
				background.setFill(Color.BLACK);
				text.setFill(Color.WHITE);
			});

			DropShadow drop = new DropShadow(50, Color.WHITE);
			drop.setInput(new Glow());

			this.setOnMousePressed(event -> this.setEffect(drop));
			this.setOnMouseReleased(event -> this.setEffect(null));
		}
	}

	private class GameMenu extends Parent {
		public GameMenu() {
			VBox menu0 = new VBox(10);
			VBox menu1 = new VBox(10);

			menu0.setTranslateX(100);
			menu0.setTranslateY(200);

			menu1.setTranslateX(100);
			menu1.setTranslateY(200);

			final int offset = 400;
			menu1.setTranslateX(offset);

			MenuButton resumeBtn = new MenuButton("RESUME");
			resumeBtn.setOnMouseClicked(event -> {
				FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.setOnFinished(e -> gameMenu.setVisible(false));
				ft.play();
			});

			MenuButton optionsBtn = new MenuButton("OPTIONS");
			optionsBtn.setOnMouseClicked(event -> {
				getChildren().add(menu1);
				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
				tt.setToX(menu0.getTranslateX() - offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
				tt1.setToX(menu0.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu0);
				});
			});

			MenuButton exitBtn = new MenuButton("EXIT");
			exitBtn.setOnMouseClicked(event -> {
				System.exit(0);
			});

			menu0.getChildren().addAll(resumeBtn, optionsBtn, exitBtn);

			MenuButton backBtn = new MenuButton("BACK");
			backBtn.setOnMouseClicked(event -> {
				getChildren().add(menu0);
				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
				tt.setToX(menu1.getTranslateX() - offset);

				TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
				tt1.setToX(menu1.getTranslateX());

				tt.play();
				tt1.play();

				tt.setOnFinished(evt -> {
					getChildren().remove(menu1);
				});
			});
			MenuButton soundBtn = new MenuButton("SOUND");
			MenuButton videoBtn = new MenuButton("VIDEO");
			menu1.getChildren().addAll(backBtn, soundBtn, videoBtn);

			Rectangle bg = new Rectangle(WIDTH, HEIGHT);
			bg.setFill(Color.GRAY);
			bg.setOpacity(0.4);
			this.getChildren().addAll(bg, menu0);
		}
	}

}
