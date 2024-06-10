package backend.geometry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents an NxN matrix. Supports addition, subtraction, and multiplication.
 *
 * @version 1.2
 * @author proggang
 * @since 24.04.2024
 */
public class MatrixNxN {
  private double[][] elements;

  /**
   * Constructs the matrix based on the input.
   * The size of the array is chosen based on the square root of the size of input.
   * If the size of input sf not square, an exception is thrown.
   *
   * @param elements The input array of elements
   * @since 1.0
   */
  public MatrixNxN(double... elements) {
    setElements(elements);
  }

  private void setElements(double... elements) {
    if (elements.length < 1) {
      throw new IllegalArgumentException("Matrix must have at least one element.");
    }
    int dimension = (int) Math.sqrt(elements.length);

    // Defining the size of the matrix.
    this.elements = new double[dimension][dimension];

    for (int i = 0; i < dimension; i++) {
      System.arraycopy(elements, i * dimension, this.elements[i], 0, dimension);
    }
  }

  /**
   * Returns the element positioned at row (i, j) in the matrix.
   * If the value is outside the bounds, an exception is thrown.
   *
   * @param i the column index
   * @param j the row index
   * @return the element positioned at (i, j)
   * @since 1.0
   */
  public double getElement(int i, int j) {
    if (i >= elements.length || j >= elements[i].length) {
      throw new IllegalArgumentException("One or both of the indexes are out of bounds of matrix.");
    }
    return elements[i][j];
  }

  /**
   * Sets the value at position (i, j) in the matrix.
   * If the value is outside the bounds, an exception is thrown.
   *
   * @param i the column index
   * @param j the row index
   * @param value the value to be set
   * @since 1.0
   */
  public void setElement(int i, int j, double value) {
    if (i >= elements.length || j >= elements[i].length) {
      throw new IllegalArgumentException("One or both of the indexes are out of bounds of matrix.");
    }
    elements[i][j] = value;
  }

  /**
   * Sets the value at the index in the matrix.
   * The index is calculated as i * size + j.
   *
   * @param index the index of the element
   * @param value the value to be set
   * @since 1.1
   */
  public void setElement(int index, double value) {
    int i = index / elements.length;
    int j = index % elements.length;
    setElement(i, j, value);
  }

  /**
   * Returns the size of the matrix.
   *
   * @return the dimension of the matrix
   * @since 1.0
   */
  public int getSize() {
    return elements.length;
  }

  /**
   * Multiplies the vector parameter with the matrix.
   *
   * @param v the vector to multiply
   * @return the result vector of the multiplication
   * @since 1.0
   */
  public Vector multiply(Vector v) {
    if (elements.length != v.getSize()) {
      throw new IllegalArgumentException("Vector must be of same dimension of matrix!");
    }

    double[] result = new double[v.getSize()];
    double[] vectorElements = v.getAllElements().mapToDouble(Double::doubleValue).toArray();

    for (int i = 0; i < elements.length; i++) {
      double sum = 0;
      for (int j = 0; j < elements[i].length; j++) {
        sum += elements[i][j] * vectorElements[j];
      }
      result[i] = sum;
    }

    return new Vector(result);
  }

  /**
   * Checks whether all elements in the matrix are zero.
   * If all elements are zero, returns true.
   *
   * @return true if all elements are zero, false otherwise
   * @since 1.2
   */
  public boolean isZero() {
    // Looping through all elements and checking if they are zero
    for (double[] row : elements) {
      for (double element : row) {
        if (element != 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Returns a stream of all elements in the matrix.
   *
   * @return a stream of all elements in the matrix
   * @since 1.1
   */
  public Stream<Double> getAllElements() {
    return Arrays.stream(elements).flatMapToDouble(Arrays::stream).boxed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatrixNxN matrixNxN = (MatrixNxN) o;
    return Objects.deepEquals(elements, matrixNxN.elements);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(elements);
  }
}
