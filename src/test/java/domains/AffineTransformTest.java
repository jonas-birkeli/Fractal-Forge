package domains;

import static org.junit.jupiter.api.Assertions.*;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffineTransformTest {
  AffineTransform affineTransform;

  @BeforeEach
  void setUp() {
    MatrixNxN m = new MatrixNxN(1.0,2.0,3.0,4.0);
    Vector v = new Vector(1.0,2.0);
    affineTransform = new AffineTransform(m, v);
  }

  @AfterEach
  void tearDown() {
    affineTransform = null;
  }

  @Test
  void transformTest() {
    Vector v = new Vector(1.0,2.0);
    Vector v2 = affineTransform.transform(v);

    // [1, 2]
    // [3, 4] * [1, 2] = [5, 11]

    // (5, 11) + (1, 2) = (6, 13)
    assertEquals(6, v2.getElement(0), "Expected 6");
    assertEquals(13, v2.getElement(1), "Expected 13");

  }
}