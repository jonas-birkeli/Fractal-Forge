package backend.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComplexTest {
  Complex complex;

  @BeforeEach
  void setUp() {
    complex = new Complex(1, 2);
  }

  @AfterEach
  void tearDown() {
    complex = null;
  }

  @Test
  void nthRoot() {
    Complex nthRoot = complex.nthRoot(2);
    assertEquals(1.272, nthRoot.getElement(0), 0.001, "The real part of the nth root is incorrect");
    assertEquals(0.786, nthRoot.getElement(1), 0.001, "The imaginary part of the nth root is incorrect");
  }

  @Test
  void add() {
    Vector v = new Vector(2, 0);
    Vector add = complex.add(v);
    assertEquals(3, add.getElement(0), 0.001, "The real part of the sum is incorrect");
    assertEquals(2, add.getElement(1), 0.001, "The imaginary part of the sum is incorrect");
  }

  @Test
  void mul() {
    Complex mul = complex.mul(2);
    assertEquals(2, mul.getElement(0), 0.001, "The real part of the product is incorrect");
    assertEquals(4, mul.getElement(1), 0.001, "The imaginary part of the product is incorrect");
  }
}