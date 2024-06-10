package backend.transforms;

import backend.geometry.Vector;

/**
 * Represents an N-dimensional transformation.
 * Supports transformation of vectors.
 *
 * @version 1.0
 * @author proggang
 * @since 10.03.2024
 */
public interface Transform {

  /**
   * Transforms the given vector.
   *
   * @param v the vector to transform
   * @return the transformed vector
   * @since 1.0
   */
  Vector transform(Vector v);

  int inverseTransform(Vector v);

  String getDetailsAsString();
}
