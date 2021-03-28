package test.main.ai;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.ai.ImageUpSampler;

public class ImageUpSamplerTester extends Application {

	@Override
	public void start(Stage mainStage) throws Exception {
		VBox content = new VBox();
		ImageView imgView = new ImageView();

		imgView.setImage(
				ImageUpSampler.nearestNeighborInterpolation(new Image(getClass().getResourceAsStream("2x2.png")), 2000));
		content.getChildren().add(imgView);

		mainStage.setScene(new Scene(content));
		mainStage.setTitle("Testing ImageUpSampler");
		mainStage.setWidth(800);
		mainStage.setHeight(600);
		mainStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
