package backend.geometry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents an N-dimensional Vector. Supports addition and subtraction.
 *
 * @version 1.2
 * @author proggang
 * @since 24.03.2024
 */
public class Vector {
  double[] elements;

  /**
   * Constructs a new vector of dimensions equal to the length of elements supplied.
   *
   * @param elem the elements supplied
   * @since 1.0
   */
  public Vector(double... elem) {
    elements = elem;
  }

  /**
   * Returns the size of the vector.
   *
   * @return the size of the vector.
   * @since 1.0
   */
  public int getSize() {
    return elements.length;
  }

  /**
   * Returns the element positioned at index j in the vector.
   * If the index is outside the vector, an exception is thrown.
   *
   * @param j the index
   * @return the element positioned at index
   * @throws IllegalArgumentException if the index is outside the vector
   * @since 1.0
   */
  public double getElement(int j) throws IllegalArgumentException {
    if (j >= elements.length) {
      throw new IllegalArgumentException("Index is larger than the dimension of the vector.");
    }
    return elements[j];
  }

  /**
   * Sets the element positioned at index j in the vector.
   * If the index is outside the vector, an exception is thrown.
   *
   * @param j the index
   * @param value the value to set
   * @throws IllegalArgumentException if the index is outside the vector
   * @since 1.0
   */
  public void setElement(int j, double value) throws IllegalArgumentException {
    if (j >= elements.length) {
      throw new IllegalArgumentException("Index is larger than the dimension of the vector.");
    }

    elements[j] = value;
  }

  /**
   * Returns the result of adding this vector with another vector.
   * Sets this vector as a result.
   * If vectors are not of similar dimension, an exception is thrown.
   *
   * @param v the other vector.
   * @return the result of adding both vectors.
   * @throws IllegalArgumentException if the vectors are not of similar dimension
   * @since 1.0
   */
  public Vector add(Vector v) throws IllegalArgumentException {
    if (elements.length != v.getSize()) {
      throw new IllegalArgumentException(
          "Input vector is not the same size as the original vector"
      );
    }

    double[] result = new double[elements.length];
    for (int j = 0; j < elements.length; j++) {
      result[j] = elements[j] + v.getElement(j);
    }

    elements = result;
    return new Vector(result);
  }

  /**
   * Returns the result of subtract this vector with another vector.
   * Sets this vector as a result.
   * If vectors are not of similar dimension, an exception is thrown.
   *
   * @param v the other vector.
   * @return the result of adding both vectors.
   * @throws IllegalArgumentException if the vectors are not of similar dimension
   * @since 1.0
   */
  public Vector sub(Vector v) throws IllegalArgumentException {
    if (elements.length != v.getSize()) {
      throw new IllegalArgumentException(
          "Input vector is not the same size as the original vector"
      );
    }

    double[] result = new double[elements.length];
    for (int j = 0; j < elements.length; j++) {
      result[j] = elements[j] - v.getElement(j);
    }

    elements = result;
    return new Vector(result);
  }

  /**
   * Returns whether the vector is a zero vector or not.
   *
   * @return true if the vector is a zero vector, false otherwise.
   * @since 1.2
   */
  public boolean isZero() {
    for (double element : elements) {
      if (element != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a stream of all elements in the vector.
   *
   * @return a stream of all elements in the vector.
   * @since 1.1
   */
  public Stream<Double> getAllElements() {
    return Arrays.stream(elements).boxed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vector vector = (Vector) o;
    return Objects.deepEquals(elements, vector.elements);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(elements);
  }
}
