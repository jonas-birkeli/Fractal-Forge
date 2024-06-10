package backend.core;

import static org.junit.jupiter.api.Assertions.*;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.Transform;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChaosGameDescriptionTest {
  ChaosGameDescription description;

  @BeforeEach
  void setUp() {
    Vector minCoords = new Vector(0.0, 1.0);
    Vector maxCoords = new Vector(100.0, 101.0);

    MatrixNxN m1 = new MatrixNxN(1.0,2.0,3.0,4.0);
    Vector v1 = new Vector(1.0,2.0);

    MatrixNxN m2 = new MatrixNxN(5.0,6.0,7.0,8.0);
    Vector v2 = new Vector(3.0,4.0);

    MatrixNxN m3 = new MatrixNxN(9.0,10.0,11.0,12.0);
    Vector v3 = new Vector(5.0,6.0);

    List<Transform> transforms = List.of(
        new AffineTransform(m1, v1),
        new AffineTransform(m2, v2),
        new AffineTransform(m3, v3)
    );

    description = new ChaosGameDescription(minCoords, maxCoords, transforms, null);
  }

  @AfterEach
  void tearDown() {
    description = null;
  }

  @Test
  void getTransforms() {
    List<Transform> transforms = description.getTransforms();
    assertEquals(3, transforms.size(), "Expected 3 transforms");
  }

  @Test
  void getMinCoords() {
    Vector minCoords = description.getMinCoords();
    assertEquals(0, minCoords.getElement(0), "Expected 0 as x0");
    assertEquals(1, minCoords.getElement(1), "Expected 1 as x1");
  }

  @Test
  void getMaxCoords() {
    Vector maxCoords = description.getMaxCoords();
    assertEquals(100, maxCoords.getElement(0), "Expected 100 as x0");
    assertEquals(101, maxCoords.getElement(1), "Expected 101 as x1");
  }
}