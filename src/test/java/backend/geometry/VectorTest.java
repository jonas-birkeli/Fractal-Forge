package backend.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorTest {
  Vector v;

  @BeforeEach
  void setUp() {
    v = new Vector(1.0, 2.0, 3.0);
  }

  @AfterEach
  void tearDown() {
    v = null;
  }

  @Test
  void getSizePositive() {
    assertEquals(3, v.getSize());
  }

  @Test
  void getElementPositive() {
    assertEquals(1.0, v.getElement(0));
    assertEquals(2.0, v.getElement(1));
    assertEquals(3.0, v.getElement(2));
  }

  @Test
  void getElementNegative() {
    assertNotEquals(2.0, v.getElement(0));
    assertNotEquals(3.0, v.getElement(1));
    assertNotEquals(1.0, v.getElement(2));
  }

  @Test
  void getElementThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> v.getElement(3), "Index is larger than the dimension of the vector.");
  }

  @Test
  void setElement() {
    v.setElement(0, 4.0);
    assertEquals(4.0, v.getElement(0), "Element at index 0 should be 4.0.");
  }

  @Test
  void setElementThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> v.setElement(3, 4.0), "Index is larger than the dimension of the vector.");
  }

  @Test
  void add() {
    Vector v2 = new Vector(4.0, 5.0, 6.0);
    Vector result = v.add(v2);
    assertEquals(5.0, result.getElement(0), "Element at index 0 should be 5.0.");
    assertEquals(7.0, result.getElement(1), "Element at index 1 should be 7.0.");
    assertEquals(9.0, result.getElement(2), "Element at index 2 should be 9.0.");
  }

  @Test
  void addThrowsException() {
    Vector v2 = new Vector(4.0, 5.0);
    assertThrows(IllegalArgumentException.class, () -> v.add(v2), "Input vector is not the same size as the original vector");
  }

  @Test
  void subPositive() {
    Vector v2 = new Vector(4.0, 5.0, 6.0);
    Vector result = v.sub(v2);
    assertEquals(-3.0, result.getElement(0), "Element at index 0 should be -3.0.");
    assertEquals(-3.0, result.getElement(1), "Element at index 1 should be -3.0.");
    assertEquals(-3.0, result.getElement(2), "Element at index 2 should be -3.0.");
  }

  @Test
  void subNegative() {
    Vector v2 = new Vector(4.0, 5.0, 6.0);
    Vector result = v.sub(v2);
    assertNotEquals(3.0, result.getElement(0), "Element at index 0 should not be 3.0.");
    assertNotEquals(1.0, result.getElement(1), "Element at index 1 should not be 3.0.");
    assertNotEquals(0.0, result.getElement(2), "Element at index 2 should not be 3.0.");
  }

  @Test
  void subThrowsException() {
    Vector v2 = new Vector(4.0, 5.0);
    assertThrows(IllegalArgumentException.class, () -> v.sub(v2), "Input vector is not the same size as the original vector");
  }
}