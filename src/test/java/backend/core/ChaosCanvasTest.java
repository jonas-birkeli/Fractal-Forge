package backend.core;

import static config.DisplayConfig.PIXEL_HIT_INCREMENTER;
import static org.junit.jupiter.api.Assertions.*;

import backend.geometry.Vector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChaosCanvasTest {
  ChaosCanvas canvas;

  @BeforeEach
  void setUp() {
    Vector minCoords = new Vector(0.0,0.0);
    Vector maxCoords = new Vector(100.0,100.0);
    canvas = new ChaosCanvas(100, 100, minCoords, maxCoords);
  }

  @AfterEach
  void tearDown() {
    canvas = null;
  }

  @Test
  void GetPixelTest() {
    Vector point = new Vector(50.0, 50.0);
    assertEquals(0, canvas.getPixel(point), "Expected 0");

    // Paint the pixel, increments the hue value by some arbitrary value
    canvas.putPixel(point);
    assertEquals(PIXEL_HIT_INCREMENTER, canvas.getPixel(point), "Expected " + PIXEL_HIT_INCREMENTER);
  }

  @Test
  void putPixel() {
    Vector point = new Vector(50.0, 50.0);
    canvas.putPixel(point);
    assertEquals(PIXEL_HIT_INCREMENTER, canvas.getPixel(point), "Expected " + PIXEL_HIT_INCREMENTER);
  }

  @Test
  void putPixelThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> canvas.putPixel(null), "point cannot be null");

    Vector pointOutOfBounds = new Vector(000.0, 1000.0);  // 1000 > 100
    assertThrows(IllegalArgumentException.class, () -> canvas.getPixel(pointOutOfBounds), "point cannot be null");
  }

  @Test
  void getCanvasArray() {
    assertEquals(new int[100][100].length, canvas.getCanvasArray().length, "Expected true");
  }

  @Test
  void clear() {
    Vector point = new Vector(50.0,50.0);
    canvas.putPixel(point);
    // Painting the pixel
    assertEquals(PIXEL_HIT_INCREMENTER, canvas.getPixel(point), "Expected " + PIXEL_HIT_INCREMENTER);

    // Clearing the canvas, the pixel should be cleared
    canvas.clear();
    assertEquals(0, canvas.getPixel(point), "Expected 0");
  }
}