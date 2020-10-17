package main.javafx.toggleswitches;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.javafx.toggleswitches.SliderToggle.SliderStyle;
import main.javafx.toggleswitches.SliderToggle.ToggleStyle;

public class Tester extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane content = new Pane();
		content.setPrefSize(1280, 800);

		VBox buttonDisplay = new VBox();
		SliderToggle toggle1 = new SliderToggle();
		// toggle1.addTranslationTransition();
		// toggle1.addFillTransition(Component.SLIDER, Color.YELLOW, Color.BLUE);
		Button b = new Button("Testing");

		buttonDisplay.getChildren().addAll(b, toggle1);
		buttonDisplay.setTranslateX(content.getPrefWidth() / 2);
		buttonDisplay.setTranslateY(content.getPrefHeight() / 2);

		VBox sliders = new VBox(5);
		Text t = new Text("Slider Type");
		Slider s = new Slider(0, 1, 0);
		s.setShowTickLabels(true);
		s.setShowTickMarks(true);
		s.valueProperty().addListener((obs, old, newV) -> {
			System.out.println(newV);
			toggle1.setSliderStyle(SliderStyle.values()[newV.intValue()]);
		});
		Text t0 = new Text("Toggle Type");
		Slider s0 = new Slider(0, 1, 0);
		s0.setShowTickLabels(true);
		s0.setShowTickMarks(true);
		s0.valueProperty().addListener((obs, old, newV) -> {
			System.out.println(newV);
			toggle1.setToggleStyle(ToggleStyle.values()[newV.intValue()]);
		});

		Text t1 = new Text("Toggle Size");
		Slider s1 = new Slider(-100, 100, 0);
		s1.setShowTickMarks(true);
		s1.setShowTickLabels(true);
		s1.valueProperty().addListener((obs, old, newV) -> {
			toggle1.setToggleSize(newV.doubleValue());
		});

		Text t2 = new Text("Slider Width");
		Slider s2 = new Slider(1, 300, 0);
		s2.setShowTickMarks(true);
		s2.setShowTickLabels(true);
		s2.valueProperty().addListener((obs, old, newV) -> {
			toggle1.setSliderHeight(newV.doubleValue());
		});

		Text t3 = new Text("Slider Height");
		Slider s3 = new Slider(1, 300, 0);
		s3.setShowTickMarks(true);
		s3.setShowTickLabels(true);
		s3.valueProperty().addListener((obs, old, newV) -> {
			toggle1.setSliderWidth(newV.doubleValue());
		});

		sliders.getChildren().addAll(t, s, t0, s0, t1, s1, t2, s2, t3, s3);

		Rectangle rect = new Rectangle(100, 40, 100, 100);
		rect.setArcHeight(50);
		rect.setArcWidth(50);
		rect.setFill(Color.VIOLET);

		TranslateTransition tt = new TranslateTransition(Duration.millis(2000), rect);
		tt.setToX(200);
		tt.setDuration(Duration.seconds(3));

		content.getChildren().addAll(sliders, buttonDisplay, rect);

		Scene scene = new Scene(content);
		stage.setScene(scene);
		stage.setTitle("Toggle Switch Demo");
		stage.show();
		tt.play();
	}

}
