import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StackPaneExample extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		final double WIDTH = 800;
		final double HEIGHT = 400;
		StackPane stackPane = new StackPane();
		
		for (double width = WIDTH, height = HEIGHT; width > WIDTH / 10; width /= 1.1, height /= 1.1) {
			Rectangle rectangle = new Rectangle(width, height, Color.color(Math.random(), Math.random(), Math.random()));
			stackPane.getChildren().add(rectangle);
		}
		
		
		Scene scene = new Scene(stackPane);
		
		stage.setScene(scene);
		stage.setTitle("StackPane");
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		stage.show();
	}

}
