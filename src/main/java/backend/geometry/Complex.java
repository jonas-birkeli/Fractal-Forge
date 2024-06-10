package backend.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complex number. Supports square root.
 *
 * @version 1.7
 * @author proggang
 * @since 10.03.2024
 */
public class Complex extends Vector {

  /**
   * Constructs a new complex number with the given components.
   *
   * @param elements the components of the complex number
   *                 as an array of doubles with size 2.
   * @since 1.0
   */
  public Complex(double... elements) {
    super(elements);
  }

  /**
   * Returns the square root of this complex number.
   * The calculation is based on the formula:
   * sqrt(a + b*i) = sqrt((1/2) * (sqrt(a^2 + b^2) + a))
   * + i*sign * sqrt((1/2) * (sqrt(a^2 + b^2) - a)) * i
   * If the complex number has more than two elements, only the first 2 are accounted for.
   *
   * @return the square root of this complex number as a new complex number with 2 elements.
   * @since 1.0
   */
  public Complex sqrt() {
    double real = getElement(0);
    double imag = getElement(1);

    double magnitude = Math.sqrt(real * real + imag * imag);
    double t = Math.atan2(imag, real);

    double sqrtMagnitude = Math.sqrt(magnitude);
    double sqrtReal = sqrtMagnitude * Math.cos(t  / 2);
    double sqrtImag = sqrtMagnitude * Math.sin(t /  2);

    return new Complex(sqrtReal, sqrtImag);
  }

  /**
   * Returns the n-th root of this complex number.
   * The calculation is based on the formula:
   * (a + b*i)^(1/n) = (r^(1/n) * (cos((t + 2*k*pi) / n) + i * sin((t + 2*k*pi) / n))
   * where r = sqrt(a^2 + b^2) and t = atan2(b, a)
   *
   * @param n the root to calculate
   * @return the n-th root of this complex number
   * @since 1.7
   */
  public Complex nthRoot(int n) {
    double real = getElement(0);
    double imag = getElement(1);

    // Calculate magnitude and argument
    double magnitude = Math.sqrt(real * real + imag * imag);
    double argument = Math.atan2(imag, real);

    // Calculate the nth roots
    double rootMagnitude = Math.pow(magnitude, 1.0 / n);

    double angle = (argument + 2 * Math.PI * n) / n;
    double realPart = rootMagnitude * Math.cos(angle);
    double imaginaryPart = rootMagnitude * Math.sin(angle);

    return new Complex(realPart, imaginaryPart);
  }

  /**
   * Returns the product of this complex number and the scalar.
   *
   * @param scalar the scalar to multiply with
   * @return the product of this complex number and the scalar
   * @since 1.6
   */
  public Complex mul(double scalar) {
    return new Complex(getElement(0) * scalar, getElement(1) * scalar);
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
