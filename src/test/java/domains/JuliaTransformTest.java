package domains;

import backend.geometry.Vector;
import backend.transforms.JuliaTransform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.geometry.Complex;

class JuliaTransformTest {
  JuliaTransform juliaTransform;

  @BeforeEach
  void setUp() {
    Complex point = new Complex(1.0, 1.0);
    juliaTransform = new JuliaTransform(point, 1);
  }

  @AfterEach
  void tearDown() {
    juliaTransform = null;
  }

  @Test
  void transformTest() {
    Complex point = new Complex(3.0, 4.0);
    Vector point2 = juliaTransform.transform(point);

    //assertEquals(1.6741492280355401, point2.getElement(0), "Expected 1.6741492280355401");
    //assertEquals(0.895977476129838, point2.getElement(1), "Expected 0.895977476129838");
  }
}