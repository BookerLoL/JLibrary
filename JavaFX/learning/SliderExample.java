
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SliderExample extends Application {
	@Override
	public void start(Stage stage) {
		final double horizSliderWidth = 300;
		final double vertSliderHeight = 300;
		DecimalFormat df = new DecimalFormat("0.0");
		Slider vertSlider = new Slider(0, 20, 0);
		vertSlider.setMinHeight(vertSliderHeight);
		vertSlider.setShowTickMarks(true);
		vertSlider.setShowTickLabels(true);
		vertSlider.setSnapToTicks(true);
		vertSlider.setMajorTickUnit(5.0);
		vertSlider.setMinorTickCount(10);
		vertSlider.setOrientation(Orientation.VERTICAL); // default is horizontal

		Slider horizSlider = new Slider(0, 10, 0);
		horizSlider.setMinWidth(horizSliderWidth);
		horizSlider.setShowTickMarks(true);
		horizSlider.setShowTickLabels(true);
		horizSlider.setSnapToTicks(true);
		horizSlider.setMajorTickUnit(1.0);
		horizSlider.setMinorTickCount(3);

		Label horizLabel = new Label("Current value is 0.0");
		Label vertLabel = new Label("Current value is 0.0");
		vertSlider.valueProperty().addListener(e -> vertLabel.setText("Current value is " + df.format(vertSlider.getValue())));
		horizSlider.valueProperty().addListener(
				(obsValue, oldValue, newValue) -> horizLabel.setText("Current value is " + df.format(newValue)));

		VBox vertBox = new VBox(10);
		vertBox.setAlignment(Pos.BOTTOM_LEFT);
		vertBox.setMinWidth(horizSliderWidth / 3);
		vertBox.getChildren().addAll(vertSlider, vertLabel);
		
		VBox horizBox = new VBox(10);
		horizBox.setAlignment(Pos.BOTTOM_LEFT);
		horizBox.getChildren().addAll(horizSlider, horizLabel);
		HBox root = new HBox(30);
		
		root.setPadding(new Insets(10, 10, 10, 10));
		root.getChildren().addAll(horizBox, vertBox);
		Scene scene = new Scene(root, 460, 350);
		stage.setScene(scene);
		stage.setTitle("Slider Example");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}