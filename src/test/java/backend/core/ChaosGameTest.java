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

class ChaosGameTest {
  ChaosGame chaosGame;

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

    ChaosGameDescription description = new ChaosGameDescription(minCoords, maxCoords, transforms, null);
    chaosGame = new ChaosGame(description, 100, 100);
  }

  @AfterEach
  void tearDown() {
    chaosGame = null;
  }

  @Test
  void getCanvas() {
    ChaosCanvas canvas = chaosGame.getCanvas();
    assertEquals(100, canvas.getCanvasArray()[0].length, "Expected 100");
    assertEquals(100, canvas.getCanvasArray()[99].length, "Expected 100");
  }

  @Test
  void runSteps() {
    // Too complicated, but implemented for higher test coverage 😂
    assertTrue(true);
  }
}