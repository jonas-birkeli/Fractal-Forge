package backend.transforms;

import backend.geometry.Complex;
import backend.geometry.Vector;
import java.util.Objects;

/**
 * Represents a 2-dimensional transformation.
 * Supports transformation of vectors.
 *
 * @version 1.3
 * @author proggang
 * @since 10.03.2024
 */
public class JuliaTransform implements Transform {

  private Complex point;
  private int sign;
  private int power;

  /**
   * Constructs a new Julia transformation with the given point and sign.
   *
   * @param point the point of this transformation
   * @param sign  the sign of this transformation
   * @since 1.0
   */
  public JuliaTransform(Complex point, int sign, int... power) {
    setPoint(point);
    setSign(sign);
    setPower(power);
  }

  /**
   * Sets the power of this transformation. If the given power is empty, sets the power to 2.
   *
   * @param power the power to set
   * @since 1.4
   */
  public void setPower(int[] power) {
    if (power.length == 0) {
      this.power = 2;
    } else {
      this.power = power[0];
    }
  }

  /**
   * Sets the sign of this transformation if the given sign is 1 or -1. Otherwise, sets the sign to
   * 1.
   *
   * @param sign the sign to set 1 for positive sign -1 for negative sign otherwise, 1
   * @since 1.0
   */
  private void setSign(int sign) {
    if (sign == -1) {
      this.sign = sign;
    } else {
      this.sign = 1;
    }
  }

  /**
   * Sets the point of this transformation if the given point is not null. Otherwise, does nothing.
   *
   * @param point the point to set
   * @since 1.0
   */
  private void setPoint(Complex point) {
    if (point != null) {
      this.point = point;
    }
  }

  /**
   * Transforms the given vector. The transformation is based on the formula: v = sign * sqrt(v -
   * point)
   *
   * @param v the vector to transform
   * @return the transformed vector
   * @since 1.0
   */
  public Vector transform(Vector v) {
    if (v == null) {
      throw new IllegalArgumentException("Vector cannot be null");
    }

    Complex z = new Complex(v.getElement(0), v.getElement(1));

    Vector resV = z.sub(point);
    Complex res = new Complex(resV.getElement(0), resV.getElement(1));
    res = res.nthRoot(power);
    res = res.mul(sign);

    return res;
  }

  /**
   * Checks if the given vector is in the Julia set. The check is based on the formula: z = z^2 +
   * point. 3
   * The method returns the number of iterations before the point diverges. If the point is inside
   * the Julia set, the method returns 0.
   *
   * @param v the vector to transform
   * @return the number if iterations;
   * @since 1.0
   */
  public int inverseTransform(Vector v) {
    if (v == null) {
      throw new IllegalArgumentException("Vector cannot be null");
    }

    double realPart = v.getElement(0);
    double imagPart = v.getElement(1);

    final int maxIterations = 100;

    // Apply the inverse Julia iteration formula
    for (int i = 0; i < maxIterations; i++) {
      // z = z^2 + point
      double realSquared = Math.pow(realPart, power);
      double imagSquared = Math.pow(imagPart, power);

      // Check if the point diverges
      if (realSquared + imagSquared > 4) {
        return i;  // loop exit condition
      }

      // Apply the inverse Julia iteration formula
      double newReal = realSquared - imagSquared + point.getElement(0);
      double newImag = 2 * realPart * imagPart + point.getElement(1);

      realPart = newReal;
      imagPart = newImag;
    }

    // Return the maximum iteration count if the point is inside the Julia set
    return 0;
  }

  /**
   * Returns the sign of this transformation.
   *
   * @return the sign of this transformation
   * @since 1.3
   */
  public int getSign() {
    return sign;
  }

  /**
   * Returns the details of this transformation. The details include the point and the sign of this
   * transformation.
   *
   * @return the details of this transformation
   * @since 1.1
   */
  @Override
  public String getDetailsAsString() {
    StringBuilder result = new StringBuilder();

    for (int j = 0; j < point.getSize(); j++) {
      result.append(point.getElement(j))
          .append(", ");
    }
    // Delete last ", "
    result.delete(result.length() - 2, result.length());
    return String.valueOf(result);
  }

  /**
   * Returns the point of this transformation.
   *
   * @return the point of this transformation
   * @since 1.2
   */
  public Complex getPoint() {
    return point;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JuliaTransform that = (JuliaTransform) o;
    return sign == that.sign && Objects.equals(getPoint(), that.getPoint());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPoint(), sign);
  }
}