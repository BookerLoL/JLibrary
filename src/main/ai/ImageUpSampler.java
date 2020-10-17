package main.ai;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

//https://www.pixilart.com/draw?ref=home-page#, very nice for creating simple images
public class ImageUpSampler {
	public static Image nearestNeighborInterpolation(Image image, int scale) throws IllegalArgumentException {
		return nearestNeighborInterpolation(image, scale, scale);
	}

	public static Image nearestNeighborInterpolation(Image image, int scaleWidth, int scaleHeight)
			throws IllegalArgumentException {
		checkValidScales(scaleWidth, scaleHeight);

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage wImage = new WritableImage(width * scaleWidth, height * scaleHeight);
		PixelWriter writer = wImage.getPixelWriter();
		PixelReader reader = image.getPixelReader();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = reader.getColor(x, y);
				for (int row = y * scaleHeight; row < (y * scaleHeight) + scaleHeight; row++) {
					for (int col = x * scaleWidth; col < (x * scaleWidth) + scaleWidth; col++) {
						writer.setColor(col, row, color);
						System.out.println(color);
					}
				}
			}
		}

		return new ImageView(wImage).getImage();
	}

	private static void checkValidScales(int scaleWidth, int scaleHeight) throws IllegalArgumentException {
		if (scaleWidth <= 0 || scaleHeight <= 0) {
			throw new IllegalArgumentException("Scale value should not be less than 1, width scale: " + scaleWidth
					+ ", height scale: " + scaleHeight);
		}
	}

	public static Image BilinearInterpolation(Image image, int scale) throws IllegalArgumentException {
		return BilinearInterpolation(image, scale);
	}

	/*
	 * Incomplete, need to finish later.
	 */
	public static Image BilinearInterpolation(Image image, int scaleWidth, int scaleHeight)
			throws IllegalArgumentException {
		Image neighbors = nearestNeighborInterpolation(image, scaleWidth, scaleHeight);

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage wImage = new WritableImage(width, height);
		PixelWriter writer = wImage.getPixelWriter();
		PixelReader reader = neighbors.getPixelReader();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
			}
		}

		return null;
	}
}
