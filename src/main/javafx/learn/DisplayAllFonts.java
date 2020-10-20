package main.javafx.learn;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DisplayAllFonts extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox mainView = new HBox(20);

		final int initialFontSize = 12;

		ScrollPane sp = new ScrollPane();
		sp.setMaxWidth(550);
		GridPane fontContent = new GridPane();
		List<String> fontFamilies = Font.getFamilies();
		for (int i = 0; i < Font.getFamilies().size(); i++) {
			Label familyLabel = new Label(fontFamilies.get(i));
			familyLabel.setFont(Font.font(initialFontSize));
			Label userInputLabel = new Label();
			userInputLabel.setFont(Font.font(fontFamilies.get(i), initialFontSize));

			fontContent.add(familyLabel, 0, i);
			fontContent.add(userInputLabel, 1, i);
		}
		sp.setContent(fontContent);

		HBox inputContent = new HBox(10);
		Label inputLabel = new Label("Input: ");
		TextField inputTF = new TextField();
		inputTF.textProperty().addListener((obs, old, newV) -> {
			for (Node child : fontContent.getChildren()) {
				if (GridPane.getColumnIndex(child) == 1) {
					Label userInputLabel = (Label) child;
					userInputLabel.setText(newV);
				}
			}
		});
		inputContent.getChildren().addAll(inputLabel, inputTF);

		HBox fontSizeContent = new HBox(10);
		Label fontSizeLabel = new Label("Font Size");
		Slider fontSlider = new Slider(1, 72, initialFontSize);
		fontSlider.setSnapToTicks(true);
		fontSlider.setShowTickLabels(true);
		fontSlider.setShowTickMarks(true);
		fontSlider.setMinorTickCount(17);
		fontSlider.valueChangingProperty().addListener((obs, oldV, newV) -> {
			if (!newV) {
				fontContent.getChildren().forEach(child -> {
					Label label = (Label) child;
					label.setFont(Font.font(label.getFont().getFamily(), fontSlider.getValue()));
				});
			}
		});
		fontSizeContent.getChildren().addAll(fontSizeLabel, fontSlider);

		VBox userInputContent = new VBox(10);
		userInputContent.getChildren().addAll(inputContent, fontSizeContent);
		mainView.getChildren().addAll(sp, userInputContent);

		stage.setTitle("Font Families");
		stage.setScene(new Scene(mainView, 800, 600));
		stage.show();
	}

}
