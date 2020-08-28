import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FlowPaneExample extends Application {
	final double WIDTH = 800;
	final double HEIGHT = 400;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		FlowPane fp = new FlowPane();
		int numButtons = 5;
		
		for (int i = 0; i < numButtons; i++) {
			fp.getChildren().add(new Button("Button " + i));
		}
		
		Scene scene = new Scene(fp);
		stage.setScene(scene);
		stage.setTitle("FlowPane");
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		stage.show();
	}

}
