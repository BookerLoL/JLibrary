import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPaneExample extends Application {
	final double WIDTH = 800;
	final double HEIGHT = 400;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Button topB = new Button("Top");
		Button bottomB = new Button("Bottom");
		Button leftB = new Button("Left");
		Button rightB = new Button("Right");
		Button centerB = new Button("Center");

		BorderPane bp = new BorderPane();
		bp.setCenter(centerB);
		bp.setLeft(leftB);
		bp.setRight(rightB);
		bp.setTop(topB);
		bp.setBottom(bottomB);

		BorderPane.setAlignment(topB, Pos.CENTER);
		BorderPane.setAlignment(leftB, Pos.CENTER);
		BorderPane.setAlignment(rightB, Pos.CENTER);
		BorderPane.setAlignment(bottomB, Pos.CENTER);

		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.setTitle("BorderPane");
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		stage.show();
	}

}