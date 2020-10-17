package main.javafx.toggleswitches;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import main.collections.Pair.MutablePair;
import main.collections.Pair.Pair;
import main.javafx.shapes.Square;

/**
 * A class designed to create a customized toggle-style switch.
 * 
 * You are able to control most of the features along with using predefined
 * popular looking styles.
 * 
 * If you are unsatisfied with any of the styles, you can modify the objects
 * directly and check with the selected index value. If you do modify them,
 * please be make sure to test that the toggle still works. For example you
 * could add a background image to the toggle or create more animations.
 * 
 * There is a check to ensure that people can't spam the button while it's
 * animations are running.
 * 
 * This class does use a lot of memory compared to other toggles however this
 * allows a lot of flexibility.
 * 
 * <p>
 * Important: The width will always have the higher value than height, so if you
 * update height to have a higher value then width then they will simply swap
 * values. If you want a toggle to have more height than width then simply
 * rotate the object.
 * 
 * <p>
 * I highly recommend playing around with the settings to see what you can do. A
 * simple way of testing is to create an application that tests each function
 * using sliders and calling the available functions.
 * 
 * <p>
 * TODO: Need to add default styles that users can pick from if they don't feel
 * like making one. -
 * https://assets.justinmind.com/wp-content/uploads/2020/05/toggle-switches-ui-kits.png
 * -
 * https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/48029d70302039.5ba15c1f0af95.png
 * - https://speckyboy.com/wp-content/uploads/2020/03/switch-th.jpg
 * 
 * @author Ethan
 * @version 1.0
 */
public class SliderToggle extends Parent {
	public static enum Component {
		SLIDER, TOGGLE;
	}

	public static enum SliderStyle {
		ROUNDED, CORNERS;
	}

	public static enum ToggleStyle {
		SQUARE, CIRCLE;
	}

	private static enum Content {
		SLIDER(0), TOGGLE(1), TEXT1(2), TEXT2(3);

		int position;

		Content(int position) {
			this.position = position;
		}
	}

	private class ComponentsModel {
		private Shape slider;
		private Shape toggle;
		private SliderStyle sliderStyle;
		private ToggleStyle toggleStyle;

		public double getSliderHeight() {
			return slider.getLayoutBounds().getHeight();
		}

		public double getSliderWidth() {
			return slider.getLayoutBounds().getWidth();
		}

		public double getToggleWidth() {
			return toggle.getLayoutBounds().getWidth();
		}
	}

	private class TransitionModel {
		private ParallelTransition transitions;
		private FillTransition sliderFT;
		private FillTransition toggleFT;
		private TranslateTransition toggleTT;
		private Pair<Color, Color> toggleFillColors;
		private Pair<Color, Color> sliderFillColors;

		public TransitionModel() {
			toggleFillColors = new MutablePair<>(Color.DODGERBLUE, Color.WHITE);
			sliderFillColors = new MutablePair<>(Color.SKYBLUE, Color.DARKGRAY);
		}

		public Color getToggleColor(boolean key) {
			return key ? toggleFillColors.getKey() : toggleFillColors.getValue();
		}

		public Color getSliderColor(boolean key) {
			return key ? sliderFillColors.getKey() : sliderFillColors.getValue();
		}
	}

	private class StateModel {
		private boolean isRunning;
		private double width;
		private double height;
		private double baseSize;
		private SimpleBooleanProperty isSelected;

		public StateModel(double width, double height) {
			this.width = width;
			this.height = height;
			swapSizes();
			baseSize = Math.min(width, height);
			isSelected = new SimpleBooleanProperty(false);
		}

		public void setWidth(double newWidth) {
			width = newWidth;
			swapSizes();
			baseSize = Math.min(width, height);
		}

		public void setHeight(double newHeight) {
			height = newHeight;
			swapSizes();
			baseSize = Math.min(width, height);
		}

		/*
		 * Technically we don't need to use Math.min if we swap sizes first but in case
		 * this ever changes, this won't break anything.
		 */
		private void swapSizes() {
			if (height > width) {
				double temp = height;
				height = width;
				width = temp;
			}
		}
	}

	public static final double DEFAULT_WIDTH = 100.0;
	public static final double SECONDS = 0.5;

	// We are using models to simply break up the code to make it more readable.
	private ComponentsModel model;
	private TransitionModel tModel;
	private StateModel state;

	public SliderToggle() {
		this(DEFAULT_WIDTH);
	}

	public SliderToggle(ToggleStyle toggleStyle, SliderStyle sliderStyle) {
		this(DEFAULT_WIDTH, DEFAULT_WIDTH, toggleStyle, sliderStyle);
	}

	public SliderToggle(double width) {
		this(width, width / 2);
	}

	public SliderToggle(double width, double height) {
		this(width, height, ToggleStyle.CIRCLE, SliderStyle.ROUNDED);
	}

	public SliderToggle(double width, double height, ToggleStyle toggleStyle, SliderStyle sliderStyle) {
		state = new StateModel(width, height);
		model = new ComponentsModel();
		tModel = new TransitionModel();

		model.sliderStyle = sliderStyle;
		model.toggleStyle = toggleStyle;
		model.slider = createSlider(width, height, sliderStyle);
		model.toggle = createToggle(toggleStyle);
		this.getChildren().addAll(model.slider, model.toggle);

		tModel.sliderFT = new FillTransition(Duration.seconds(SECONDS), model.slider);
		tModel.toggleFT = new FillTransition(Duration.seconds(SECONDS), model.toggle);
		tModel.toggleTT = new TranslateTransition(Duration.seconds(SECONDS), model.toggle);
		tModel.transitions = new ParallelTransition(tModel.toggleTT, tModel.toggleFT, tModel.sliderFT);

		state.isSelected.addListener((obs, oldV, newV) -> {
			if (!state.isRunning) {
				state.isRunning = true;
				tModel.toggleTT.setToX(getToggleTranslationX());
				tModel.toggleFT
						.setToValue(newV ? tModel.toggleFillColors.getKey() : tModel.toggleFillColors.getValue());
				tModel.toggleFT
						.setFromValue(newV ? tModel.toggleFillColors.getValue() : tModel.toggleFillColors.getKey());
				tModel.sliderFT
						.setToValue(newV ? tModel.sliderFillColors.getKey() : tModel.sliderFillColors.getValue());
				tModel.sliderFT
						.setFromValue(newV ? tModel.sliderFillColors.getValue() : tModel.sliderFillColors.getKey());
				tModel.transitions.play();
			}
		});

		tModel.transitions.setOnFinished(event -> {
			state.isRunning = false;
		});

		this.setOnMouseClicked(event -> {
			if (!state.isRunning) {
				toggle();
			}
		});
	}

	/*
	 * This function has extra checks because additional styles will be added later.
	 */
	private Shape createSlider(double width, double height, SliderStyle sliderStyle) {
		Shape slider = null;
		if (sliderStyle == SliderStyle.ROUNDED || sliderStyle == SliderStyle.CORNERS) {
			Rectangle rectangle = new Rectangle(width, height);
			if (sliderStyle == SliderStyle.ROUNDED) {
				double ratio = width / height;
				rectangle.setArcHeight(height);
				rectangle.setArcWidth(width / ratio);
			}
			slider = rectangle;
		}
		slider.setFill(tModel.getSliderColor(isSelected()));
		return slider;
	}

	public void setSliderWidth(double newWidth) {
		if (newWidth != state.width) {
			state.setWidth(newWidth);
			updateNewSlider();
		}
	}

	public void setSliderHeight(double newHeight) {
		if (newHeight != state.height) {
			state.setHeight(newHeight);
			updateNewSlider();
		}
	}

	private void updateNewSlider() {
		Shape newSlider = createSlider(state.width, state.height, model.sliderStyle);
		getChildren().remove(Content.SLIDER.position);
		getChildren().add(Content.SLIDER.position, newSlider);
		tModel.sliderFT.setShape(newSlider);
		model.slider = newSlider;

		if (model.toggleStyle == ToggleStyle.SQUARE) {
			Square sq = (Square) model.toggle;
			double diff = state.baseSize - sq.getWidth();
			sq.setTranslateY(diff / 2);
		} else { // Defautl is a circle
			Circle circle = (Circle) model.toggle;
			circle.setCenterX(state.baseSize / 2);
			circle.setCenterY(state.baseSize / 2);
		}
		moveToggle();
	}

	private Shape createToggle(ToggleStyle toggleStyle) {
		Shape toggle = null;
		if (toggleStyle == ToggleStyle.SQUARE) {
			toggle = new Square(state.baseSize);
		} else { // Default is a circle
			double halfBaseSize = state.baseSize / 2;
			toggle = new Circle(halfBaseSize, halfBaseSize, halfBaseSize);
		}
		toggle.setFill(tModel.getToggleColor(isSelected()));
		return toggle;
	}

	public void toggle() {
		state.isSelected.set(!state.isSelected.get());
	}

	private void moveToggle() {
		model.toggle.setTranslateX(getToggleTranslationX());
	}

	private double getToggleTranslationX() {
		double transX = 0.0;
		if (state.isSelected.get()) {
			if (model.toggleStyle == ToggleStyle.SQUARE) {
				transX = model.getSliderWidth() - state.baseSize + ((state.baseSize - model.getToggleWidth()) / 2);
			} else if (model.toggleStyle == ToggleStyle.CIRCLE) {
				transX = model.getSliderWidth() - state.baseSize;
			}
		} else {
			if (model.toggleStyle == ToggleStyle.SQUARE) {
				transX = (state.baseSize - model.getToggleWidth()) / 2;
			} else if (model.toggleStyle == ToggleStyle.CIRCLE) {
				transX = 0.0;
			}
		}
		return transX;
	}

	public void setSliderStyle(SliderStyle sliderStyle) {
		if (sliderStyle != model.sliderStyle) {
			model.sliderStyle = sliderStyle;
			getChildren().remove(Content.SLIDER.position);
			model.slider = createSlider(model.getSliderWidth(), model.getSliderHeight(), sliderStyle);
			tModel.sliderFT.setShape(model.slider);
			getChildren().add(Content.SLIDER.position, model.slider);
		}
	}

	public void setToggleStyle(ToggleStyle toggleStyle) {
		if (toggleStyle != model.toggleStyle) {
			model.toggleStyle = toggleStyle;
			getChildren().remove(Content.TOGGLE.position);
			model.toggle = createToggle(toggleStyle);
			getChildren().add(Content.TOGGLE.position, model.toggle);
			tModel.toggleFT.setShape(model.toggle);
			tModel.toggleTT.setNode(model.toggle);
			moveToggle();
		}
	}

	public void setToggleSize(double additionalSize) {
		if (model.toggleStyle == ToggleStyle.SQUARE) {
			Square sq = (Square) model.toggle;
			sq.setWidth(state.baseSize + additionalSize);
			double diff = state.baseSize - sq.getWidth();
			sq.setTranslateY(diff / 2);
		} else { // Defautl is a circle
			Circle circle = (Circle) model.toggle;
			circle.setRadius(state.baseSize / 2 + additionalSize);
		}
		moveToggle();
	}

	public void updateFillTransition(Component component, Color color, boolean isSelected) {
		if (component == Component.SLIDER) {
			if (isSelected) {
				tModel.sliderFillColors.setKey(color);
			} else {
				tModel.sliderFillColors.setValue(color);
			}
		} else {
			if (isSelected) {
				tModel.toggleFillColors.setKey(color);
			} else {
				tModel.toggleFillColors.setValue(color);
			}
		}
	}

	public void updateFillTransition(Component component, Color toggleColor, Color untoggleColor) {
		addFillTransition(component, toggleColor, untoggleColor,
				component == Component.SLIDER ? tModel.sliderFT.getDuration() : tModel.toggleFT.getDuration());
	}

	public void addFillTransition(Component component, Color toggleColor, Color untoggleColor, Duration duration) {
		if (component == Component.SLIDER) {
			tModel.sliderFillColors = new MutablePair<>(toggleColor, untoggleColor);
			tModel.sliderFT.setDuration(duration);
		} else {
			tModel.toggleFillColors = new MutablePair<>(toggleColor, untoggleColor);
			tModel.toggleFT.setDuration(duration);
		}
	}

	public void addTranslationTransition(Duration duration) {
		TranslateTransition tt = new TranslateTransition(duration, model.toggle);
		tt.setToX(getToggleTranslationX());
		tModel.transitions.getChildren().add(tt);
	}

	public boolean isSelected() {
		return state.isSelected.get();
	}

	public int getisSelected() {
		return state.isSelected.get() ? 1 : 0;
	}

	public Shape get(Component component) {
		if (component == Component.SLIDER) {
			return model.slider;
		} else {
			return model.toggle;
		}
	}
}
