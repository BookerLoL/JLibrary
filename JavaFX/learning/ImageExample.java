import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageExample extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Image image = new Image("https://wallpapercave.com/wp/wp2551790.jpg");
		PixelReader pixelReader = image.getPixelReader();

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage wImage = new WritableImage(width, height);
		PixelWriter writer = wImage.getPixelWriter();

		// grayscaling the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = pixelReader.getColor(x, y);
				writer.setColor(x, y, color.grayscale());
			}
		}

		ImageView imageContainer = new ImageView(wImage);
		VBox vb = new VBox();
		vb.getChildren().add(imageContainer);

		Scene scene = new Scene(vb);
		stage.setScene(scene);
		stage.setTitle("Grayscaling Image Example");
		stage.show();
	}

}
