package backend.core;

import static org.junit.jupiter.api.Assertions.*;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.Transform;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChaosGameFileHandlerTest {
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
  void readWriteFromFile() {
    try {
      ChaosGameFileHandler.writeToFile(description, "test.txt");
    } catch (IOException e) {
      fail("Expected file to exist");
    }
    ChaosGameDescription readDescription = ChaosGameFileHandler.readFromFile("test.txt");
    assertNotNull(readDescription, "Expected file to exist");



    Vector minCoordsDescription = readDescription.getMinCoords();
    Vector maxCoordsDescription = readDescription.getMaxCoords();

    assertEquals(0, minCoordsDescription.getElement(0), "Expected 0 as x0");
    assertEquals(1, minCoordsDescription.getElement(1), "Expected 1 as x1");
    assertEquals(100, maxCoordsDescription.getElement(0), "Expected 100 as x0");
    assertEquals(101, maxCoordsDescription.getElement(1), "Expected 101 as x1");

    for (int i = 0; i < description.getTransforms().size(); i++) {
      AffineTransform expectedTransform = (AffineTransform) description.getTransforms().get(i);
      AffineTransform readTransform = (AffineTransform) readDescription.getTransforms().get(i);

      MatrixNxN expectedMatrix = expectedTransform.getMatrix();
      MatrixNxN readMatrix = readTransform.getMatrix();
      for (int y = 0; y < expectedMatrix.getSize(); y++) {
        for (int x = 0; x < expectedMatrix.getSize(); x++) {
          assertEquals(expectedMatrix.getElement(x, y), readMatrix.getElement(x, y), "Expected equal matrix element");
        }
      }

      Vector expectedVector = expectedTransform.getVector();
      Vector readVector = readTransform.getVector();

      for (int j = 0; j < expectedVector.getSize(); j++) {
        assertEquals(expectedVector.getElement(j), readVector.getElement(j), "Expected equal vector element");
      }
    }
  }

  @Test
  void ReadFromNegative() {
    assertNull(ChaosGameFileHandler.readFromFile("nonexistent.txt"), "Expected file to not exist");
  }
}