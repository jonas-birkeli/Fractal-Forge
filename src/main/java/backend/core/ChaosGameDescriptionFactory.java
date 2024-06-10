package backend.core;

import static config.DataConfig.DEFAULT_AFFINE_FILE_PATH;
import static config.DataConfig.DEFAULT_JULIA_FILE_PATH;
import static config.DataConfig.JULIA_NAME;

import backend.geometry.Complex;
import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.JuliaTransform;

/**
 * Represents a chaos game description factory.
 * Builds predefined chaos game descriptions.
 *
 * @version 1.0
 * @author proggang
 * @since 20.02.2024
 */
public class ChaosGameDescriptionFactory {

  /**
   * Returns a predefined chaos game description.
   * Predefined chaos games: "julia", "mandelbrot", "sierpinski".
   * If the type is not recognized, returns the Sierpinski chaos game description.
   *
   * @param type the type of the predefined chaos game description
   * @return the predefined chaos game description
   * @since 1.0
   */
  public ChaosGameDescription getPredefinedChaosGame(String type) {
    if (type.equalsIgnoreCase(JULIA_NAME)) {
      return ChaosGameFileHandler.readFromFile(DEFAULT_JULIA_FILE_PATH);
    }
    return ChaosGameFileHandler.readFromFile(DEFAULT_AFFINE_FILE_PATH);
  }


  /**
   * Returns the default chaos game, which is the affine chaos game containing the Barnsley fern.
   * The default chaos game is 800x800 pixels.
   *
   * @return the default chaos game
   * @since 1.0
   */
  public ChaosGame getDefaultChaosGame() {
    MatrixNxN m1 = new MatrixNxN(0, 0, 0, 0.16, .2);
    MatrixNxN m2 = new MatrixNxN(.85, .04, -.04, .85);
    MatrixNxN m3 = new MatrixNxN(.2, -.26, .23, .22);
    MatrixNxN m4 = new MatrixNxN(-.15, .28, .26, .24);
    Vector v1 = new Vector(0, 0);
    Vector v2 = new Vector(0, 1.6);
    Vector v3 = new Vector(0, 1.6);
    Vector v4 = new Vector(0, .44);

    Vector probability = new Vector(1, 86, 93, 100);

    ChaosGameDescription chaosGameDescription =
        new ChaosGameDescription.ChaosGameDescriptionBuilder()
        .minCoords(new Vector(-2.65, 0))
        .maxCoords(new Vector(2.65, 10))
        .addTransform(new AffineTransform(m1, v1))
        .addTransform(new AffineTransform(m2, v2))
        .addTransform(new AffineTransform(m3, v3))
        .addTransform(new AffineTransform(m4, v4))
        .probability(probability)
        .build();

    return new ChaosGame(chaosGameDescription, 800, 800);
  }

  /**
   * Generates a Julia set chaos game description with the given complex number.
   * The Julia set is constrained to the area between negative two and two on both axes.
   *
   * @param complex the complex number of the Julia set
   * @return the Julia set chaos game description
   */
  public ChaosGameDescription generateJuliaSet(Vector complex) {
    return new ChaosGameDescription.ChaosGameDescriptionBuilder()
        .minCoords(new Vector(-2.0, -2.0))
        .maxCoords(new Vector(2.0, 2.0))
        .addTransform(new JuliaTransform((Complex) complex, 1))
        .build();
  }
}