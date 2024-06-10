package backend.core;

import static config.DisplayConfig.PIXEL_HIT_INCREMENTER;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;

/**
 * Represents a canvas for drawing chaos game fractals.
 * Supports getting and putting pixels.
 *
 * @version 1.1
 * @author proggang
 * @since 20.02.2024
 */
public class ChaosCanvas {
  private double[][] canvas;
  private final int width;
  private final int height;
  private final Vector minCoords;
  private final Vector maxCoords;
  private AffineTransform transformCoordsToIndices;

  /**
   * Constructs a new canvas with the given width, height, minimum coordinates,
   * and maximum coordinates.
   *
   * @param width the width of the canvas
   * @param height the height of the canvas
   * @param minCoords the minimum coordinates of the canvas
   * @param maxCoords the maximum coordinates of the canvas
   * @since 1.0
   */
  public ChaosCanvas(int width, int height, Vector minCoords, Vector maxCoords) {
    this.height = height;
    this.width = width;
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;

    canvas = new double[height][width];

    updateCoordsToIndices();
  }

  /**
   * Updates the transformation from coordinates to indices.
   * The transformation is calculated based on the width, height, minimum coordinates,
   * and maximum coordinates of the canvas.
   *
   * @since 1.0
   */
  public void updateCoordsToIndices() {
    int m = width;
    int n = height;

    MatrixNxN matrix = new MatrixNxN(
            0.0,
        (m - 1) / (minCoords.getElement(1) - maxCoords.getElement(1)),
            (n - 1) / (maxCoords.getElement(0) - minCoords.getElement(0)),
        0.0
    );
    Vector vector = new Vector(
        ((m - 1) * maxCoords.getElement(1)) / (maxCoords.getElement(1) - minCoords.getElement(1)),
        ((n - 1) * minCoords.getElement(0)) / (minCoords.getElement(0) - maxCoords.getElement(0))
    );
    transformCoordsToIndices = new AffineTransform(matrix, vector);
  }

  /**
   * Sets the transformation from coordinates to indices if the given transformation is not null.
   * Otherwise, does nothing.
   *
   * @since 1.0
   */
  public double getPixel(Vector point) {
    if (point == null) {
      throw new IllegalArgumentException("point cannot be null");
    }
    point = transformCoordsToIndices.transform(point);

    int i = (int) point.getElement(0);
    int j = (int) point.getElement(1);

    if (i < 0 || i >= height || j < 0 || j >= width) {
      throw new IllegalArgumentException("point is out of bounds");
    }
    return canvas[i][j];
  }

  /**
   * Puts a pixel at the given point.
   * If the given point is null, does nothing.
   *
   * @param point the point at which to put the pixel
   * @since 1.0
   */
  public void putPixel(Vector point) {
    if (point == null) {
      throw new IllegalArgumentException("point cannot be null");
    }

    point = transformCoordsToIndices.transform(
        new Vector(point.getElement(0), point.getElement(1))
    );

    // (y, x) = (i, j)
    int i = (int) point.getElement(0);
    int j = (int) point.getElement(1);

    // Checking for out of bounds when zooming
    boolean outOfBounds = i < 0 || i >= height || j < 0 || j >= width;

    if (outOfBounds) {
      return;
    }

    if (point.getSize() == 2) {
      canvas[i][j] += PIXEL_HIT_INCREMENTER;
    } else if (point.getSize() == 3) {
      canvas[i][j] = point.getElement(2);
    }
  }

  /**
   * Returns the canvas array.
   *
   * @return the canvas array
   * @since 1.0
   */
  public double[][] getCanvasArray() {
    return canvas;
  }

  /**
   * Clears the canvas by filling the canvas with zeros.
   *
   * @since 1.0
   */
  public void clear() {
    canvas = new double[height][width];
  }
}
