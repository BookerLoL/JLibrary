package main.collections.NoCategoryYet;

import java.awt.Color;
import java.util.List;

public class ZBuffer {
	public static class Polygon {
		private int zDepth;
		private Color[][] pixels;

		public Polygon(Color[][] pixels, int zDepth) {
			this.zDepth = zDepth;
			this.pixels = pixels;
		}
	}
	
	private static class Pixel {
		Color color;
		int depth;
		
		public Pixel(Color color, int depth) {
			this.color = color;
			this.depth = depth;
		}
	}

	private Pixel[][] viewMatrix;
	private Pixel defaultPixel;

	public ZBuffer(int height, int width) {
		this(height, width, Color.WHITE);
	}

	public ZBuffer(int height, int width, Color defaultBackgroundColor) {
		viewMatrix = new Pixel[height][width];
		defaultPixel = new Pixel(defaultBackgroundColor, Integer.MAX_VALUE);
	}

	//Return the current state of the matrix after adding the polygons, will not clear the buffer until clear is called
	public void add(List<Polygon> visiblePolygons) {
		for (Polygon p : visiblePolygons) {
			int pZDepth = p.zDepth;
			for (int row = 0; row < viewMatrix.length; row++) {
				for (int col = 0; col < viewMatrix[0].length; col++) {
					if (pZDepth < viewMatrix[row][col].depth) {
						viewMatrix[row][col].depth = pZDepth;
						viewMatrix[row][col].color = p.pixels[row][col];
					}
				}
			}
		}
	}
	
	public Color[][] getMatrix() {
		Color[][] resultMatrix = new Color[viewMatrix.length][viewMatrix[0].length];
		for (int row = 0; row < viewMatrix.length; row++) {
			for (int col = 0; col < viewMatrix[0].length; col++) {
				resultMatrix[row][col] = viewMatrix[row][col].color;
			}
		}
		return resultMatrix;
	}
	
	public void clear() {
		for (int row = 0; row < viewMatrix.length; row++) {
			for (int col = 0; col < viewMatrix[0].length; col++) {
				viewMatrix[row][col] = defaultPixel;
			}
		}
	}
}
