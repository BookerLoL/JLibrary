package main.collections.NoCategoryYet;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

/*
 * This is merely an example of the painters algorithm.
 * 
 * Includes the reverse painter method.
 * 
 * In this case, we are going to assume the user will provide the same pixel sizes for each polygon based on the buffer size
 * in order to simplify the code
 */
public class PaintersBuffer {
	public static class Polygon {
		private int zDepth;
		private Color[][] pixels;

		public Polygon(Color[][] pixels, int zDepth) {
			this.zDepth = zDepth;
			this.pixels = pixels;
		}
	}

	private Color[][] viewMatrix;
	private Color defaultBackgroundColor;
	private boolean reversePainter;

	public PaintersBuffer(int height, int width) {
		this(height, width, Color.WHITE, false);
	}

	public PaintersBuffer(int height, int width, boolean reversePainter) {
		this(height, width, Color.WHITE, reversePainter);
	}

	public PaintersBuffer(int height, int width, Color defaultBackgroundColor, boolean reversePainter) {
		viewMatrix = new Color[height][width];
		this.defaultBackgroundColor = defaultBackgroundColor;
		this.reversePainter = reversePainter;
	}

	//Return the current state of the matrix after adding the polygons, will not clear the buffer until clear is called
	public Color[][] add(List<Polygon> visiblePolygons) {
		if (!reversePainter) {
			//furthest depth first
			Collections.sort(visiblePolygons, (p1, p2) -> p2.zDepth - p1.zDepth);
			for (Polygon p : visiblePolygons) {
				for (int row = 0; row < viewMatrix.length; row++) {
					for (int col = 0; col < viewMatrix[0].length; col++) {
						if (p.pixels[row][col] != defaultBackgroundColor) {
							viewMatrix[row][col] = p.pixels[row][col];
						}
					}
				}
			}
		} else {
			//closest depth first
			Collections.sort(visiblePolygons, (p1, p2) -> p1.zDepth - p2.zDepth);
			for (Polygon p : visiblePolygons) {
				for (int row = 0; row < viewMatrix.length; row++) {
					for (int col = 0; col < viewMatrix[0].length; col++) {
						//Only update if it's the default color and the polygon has a different value
						if (viewMatrix[row][col] == defaultBackgroundColor && p.pixels[row][col] != defaultBackgroundColor) {
							viewMatrix[row][col] = p.pixels[row][col];
						}
					}
				}
			}
		}
		
		return viewMatrix;
	}
	
	public void clear() {
		for (int row = 0; row < viewMatrix.length; row++) {
			for (int col = 0; col < viewMatrix[0].length; col++) {
				viewMatrix[row][col] = defaultBackgroundColor;
			}
		}
	}
}
