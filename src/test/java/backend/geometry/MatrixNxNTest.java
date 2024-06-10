package backend.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatrixNxNTest {
  MatrixNxN m;

  @BeforeEach
  void setUp() {
    m = new MatrixNxN(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0);
  }

  @AfterEach
  void tearDown() {
    m = null;
  }

  @Test
  void getElement() {
    // middle of matrix
    assertEquals(5.0, m.getElement(1, 1), "Value should be 5.0");
  }

  @Test
  void getElementThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> m.getElement(3,3), "One or both of the indexes are out of bounds of matrix.");
  }

  @Test
  void setElement() {
    m.setElement(2,2, 10.0);
    assertEquals(10.0, m.getElement(2, 2), "Value should be 10.0");
  }

  @Test
  void setElementThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> m.setElement(3,3, 10.0), "One or both of the indexes are out of bounds of matrix.");
  }

  @Test
  void getSize() {
    assertEquals(3, m.getSize(), "Size should be 3");
  }

  @Test
  void multiply() {
    Vector v = new Vector(1.0,2.0,3.0);
    Vector res = m.multiply(v);

    assertEquals(14.0, res.getElement(0), "Value should be 6.0");
    assertEquals(32.0, res.getElement(1), "Value should be 32.0");
    assertEquals(50.0, res.getElement(2), "Value should be 50.0");
    // 14, 32, 50
    // https://matrix.reshish.com/multCalculation.php
  }

  @Test
  void multiplyThrowsException() {
    Vector v = new Vector(1.0,2.0);
    assertThrows(IllegalArgumentException.class, () -> m.multiply(v), "Vector must be of same dimension of matrix!");
  }
}