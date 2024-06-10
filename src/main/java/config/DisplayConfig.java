package config;

import javafx.scene.paint.Color;

/**
 * Represents the configuration of the screen.
 * Contains the minimum and preferred screen width and height.
 *
 * @version 1.0
 * @author proggang
 * @since 20.04.2024
 */
public class DisplayConfig {
  public static final String APPLICATION_TITLE = "Fractal Forge";

  public static final double MIN_SCREEN_WIDTH = 600.0;
  public static final double MIN_SCREEN_HEIGHT = 400.0;

  public static final double PREF_SCREEN_WIDTH = 1200.0;
  public static final double PREF_SCREEN_HEIGHT = 800.0;

  public static final int CANVAS_WIDTH = 200;
  public static final int CANVAS_HEIGHT = 200;

  // For each time a pixel is hit by a transformation, this value increments the pixel value
  public static final double PIXEL_HIT_INCREMENTER = 0.5;
  public static final Color START_COLOR = Color.RED;
  public static final Color END_COLOR = Color.PURPLE;

  // Used by the DecimalFormat class to format the numbers
  public static final String DEFAULT_DECIMAL_FORMAT = "#,###";

  // Hide the constructor
  private DisplayConfig() {
    // do nothing
  }
}

