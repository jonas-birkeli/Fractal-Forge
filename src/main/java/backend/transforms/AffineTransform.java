package backend.transforms;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import java.util.Objects;

/**
 * Represents a 2-dimensional affine transformation.
 * Supports transformation of vectors.
 *
 * @version 1.3
 * @author proggang
 * @since 10.03.2024
 */
public class AffineTransform implements Transform {
  private MatrixNxN matrix;
  private Vector vector;

  /**
   * Constructs a new affine transformation with the given matrix and vector.
   * If the given matrix or vector is null, does nothing.
   *
   * @param matrix the matrix
   * @param vector the vector
   */
  public AffineTransform(MatrixNxN matrix, Vector vector) {
    setMatrix(matrix);
    setVector(vector);
  }

  /**
   * Sets the vector of this transformation if the given vector is not null.
   * Otherwise, does nothing.
   *
   * @param vector the vector to set
   * @since 1.0
   */
  private void setVector(Vector vector) {
    if (vector != null) {
      this.vector = vector;
    }
  }

  /**
   * Sets the matrix of this transformation if the given matrix is not null.
   * Otherwise, does nothing.
   *
   * @param matrix the matrix to set
   * @since 1.0
   */
  private void setMatrix(MatrixNxN matrix) {
    if (matrix != null) {
      this.matrix = matrix;
    }
  }

  /**
   * Transforms the given vector.
   *
   * @param v the vector to transform
   * @return the transformed vector
   * @since 1.0
   */
  public Vector transform(Vector v) {
    if (v == null) {
      return null;
    }
    return matrix.multiply(v).add(vector);
  }

  /**
   * Inverse transform of affine.
   * DO NOT USE!
   *
   * @param v the vector to transform
   * @return 0
   * @deprecated DO NOT USE!
   */
  @Deprecated
  public int inverseTransform(Vector v) {
    return 0;
  }

  /**
   * Returns the details of this transformation.
   * The details include all the values of the matrix and the vector.
   *
   * @return A string representation of the details of this transformation
   * @since 1.1
   */
  public String getDetailsAsString() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < matrix.getSize(); i++) {
      for (int j = 0; j < matrix.getSize(); j++) {
        result.append(matrix.getElement(i, j))
            .append(", ");
      }
    }

    for (int i = 0; i < vector.getSize(); i++) {
      result.append(vector.getElement(i))
          .append(", ");
    }
    // Delete last ", "
    result.delete(result.length() - 2, result.length());

    return String.valueOf(result);
  }


  /**
   * Returns the matrix of this transformation.
   *
   * @return the matrix of this transformation
   * @since 1.3
   */
  public MatrixNxN getMatrix() {
    return matrix;
  }

  /**
   * Returns the vector of this transformation.
   *
   * @return the vector of this transformation
   * @since 1.3
   */
  public Vector getVector() {
    return vector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AffineTransform that = (AffineTransform) o;
    return Objects.equals(getMatrix(), that.getMatrix()) && Objects.equals(
        getVector(), that.getVector());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMatrix(), getVector());
  }
}
