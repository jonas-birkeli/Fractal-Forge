package backend.core;

import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.JuliaTransform;
import backend.transforms.Transform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Represents a chaos game.
 * Supports drawing the chaos game, and updating the current point.
 *
 * @version 1.4
 * @author proggang
 * @since 20.02.2024
 */
public class ChaosGame implements ChaosGameObserver {
  private ChaosCanvas canvas;
  private final ChaosGameDescription description;
  private Vector currentPoint;
  private final Random random;
  private int width;
  private int height;
  private final List<Vector> pointStack;

  private final List<ChaosGameObserver> observers;

  /**
   * Constructs a new chaos game with the given description, width, and height.
   * The current point is initially at the origin.
   *
   * @param description the description of the chaos game
   * @param width the width of the canvas
   * @param height the height of the canvas
   * @since 1.0
   */
  public ChaosGame(ChaosGameDescription description, int width, int height) {
    this.description = description;
    this.width = width;
    this.height = height;

    this.canvas = new ChaosCanvas(
        width, height, description.getMinCoords(), description.getMaxCoords()
    );

    this.currentPoint = new Vector(0.0, 0.0);
    this.random = new Random();
    this.pointStack = new ArrayList<>();

    this.observers = new ArrayList<>();
  }

  /**
   * Returns the canvas of this chaos game.
   *
   * @return the canvas of this chaos game
   * @since 1.0
   */
  public ChaosCanvas getCanvas() {
    return canvas;
  }

  /**
   * Returns the description of this chaos game.
   *
   * @return the description of this chaos game
   * @since 1.2
   */
  public ChaosGameDescription getDescription() {
    return description;
  }

  /**
   * Runs the game for the given number of steps.
   *
   * @param steps the number of steps to run
   * @since 1.0
   */
  public void runSteps(int steps) {
    /* * * * * * * * * * * * * *
     * Initializing variables. *
     * * * * * * * * * * * * * */
    // Reset the point stack and canvas
    pointStack.clear();
    canvas.clear();
    currentPoint = new Vector(0.0, 0.0);

    pointStack.add(currentPoint);  // Add the initial point to the point stack

    // Generating the transforms and cumulative probabilities
    List<Transform> transforms = new ArrayList<>();
    List<Integer> cumulativeProbabilities = new ArrayList<>();
    if (isAffine()) {
      // Directly move the transforms from the description, if affine
      transforms = description.getTransforms();

      for (int i = 0; i < description.getTransforms().size(); i++) {
        cumulativeProbabilities.add((int) description.getProbability().getElement(i));
        System.out.println(description.getProbability().getElement(i));
        // Adding each transform into the cumulative probabilities
      }
    } else {
      // Add the transforms twice for complex transforms, positive and negative
      for (Transform transform : description.getTransforms()) {
        JuliaTransform juliaTransform = (JuliaTransform) transform;
        transforms.add(new JuliaTransform(juliaTransform.getPoint(), 1));
        transforms.add(new JuliaTransform(juliaTransform.getPoint(), -1));

        // Equal probabilities for each transform
        cumulativeProbabilities.add(50);
        cumulativeProbabilities.add(100);
      }
    }

    int transformSize = transforms.size();
    int randomNum;
    int transformIndex;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Run the chaos game for the given number of steps. *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */
    for (int i = 0; i < steps; i++) {
      // Branch every point to the next point
      for (Transform transform : transforms) {
        Vector newPoint = transform.transform(currentPoint);
        pointStack.add(newPoint);
      }

      // Fast as fuck binary search probability finder
      randomNum = random.nextInt(100);
      transformIndex = Collections.binarySearch(cumulativeProbabilities, randomNum);

      // Binary search return negative when searching < (left), need to invert and adjust with -1
      if (transformIndex < 0) {
        transformIndex = -transformIndex - 1;
      }

      currentPoint = pointStack.get(pointStack.size() - transformSize + transformIndex);
    }
    pointStack.parallelStream().forEach(canvas::putPixel);
  }

  /**
   * Runs the inverse julia transform,
   * checking if the point is convergent within the given number of iterations.
   *
   * @since 1.3
   */
  public void runInverse() throws IllegalAccessException {
    if (description.getTransforms().size() != 1) {
      throw new IllegalAccessException("Julia set can only be run with one transform");
    }
    for (Transform transform : description.getTransforms()) {
      if (!(transform instanceof JuliaTransform)) {
        throw new IllegalAccessException("Julia set can only be run with JuliaTransform");
      }
    }
    pointStack.clear();
    canvas.clear();

    // Get the julia transform
    JuliaTransform transform = (JuliaTransform) description.getTransforms().get(0);

    Vector point;
    double widthOverTwo = width >> 1;
    double heightOverTwo = height >> 1;

    // LEAVE BE FOR NOW 🤓
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        double zx = 1.5 * (x - widthOverTwo) / (widthOverTwo);
        double zy = (y - heightOverTwo) / (heightOverTwo);

        point = new Vector(zx, zy);
        int iteration = transform.inverseTransform(point);

        if (iteration == 0) {
          pointStack.add(point);
        }
      }
    }

    pointStack.parallelStream().forEach(canvas::putPixel);
  }

  /**
   * Updates all subscribers of this chaos game.
   * If there are no subscribers, nothing happens.
   */
  @Override
  public void update() {
    observers.forEach(ChaosGameObserver::update);
  }

  /**
   * Adds the given observer to this chaos game.
   * If the given observer is null, does nothing.
   *
   * @param observer the observer to add
   * @since 1.1
   */
  public void addObserver(ChaosGameObserver observer) {
    observers.add(observer);
  }

  /**
   * Removes the given observer from this chaos game.
   * If the given observer is null, does nothing.
   *
   * @param observer the observer to remove
   * @since 1.1
   */
  public void removeObserver(ChaosGameObserver observer) {
    observers.remove(observer);
  }

  /**
   * Updates the chaos canvas with new parameters.
   * Used when updating the description.
   *
   * @since 1.2
   */
  public void updateCanvas() {
    canvas = new ChaosCanvas(width, height, description.getMinCoords(), description.getMaxCoords());
    update();
  }

  /**
   * Sets the size of the chaos game.
   * The size is the same for both width and height.
   *
   * @param size the size of the chaos game
   * @since 1.3
   */
  public void setSize(int size) {
    width = size;
    height = size;
    updateCanvas();
  }

  /**
   * Returns whether the chaos game is affine or not,
   * meaning it has only affine transforms or complex transforms.
   *
   * @return true if the chaos game is affine, false otherwise
   * @since 1.3
   */
  public boolean isAffine() {
    return description.getTransforms().get(0) instanceof AffineTransform;
  }

  /**
   * Removes the given transform from the chaos game.
   * If the given transform is not found, does nothing.
   *
   * @param transform the transform to remove
   * @since 1.3
   */
  public void removeTransform(Transform transform) {

    int index = 0;
    boolean found = false;
    for (int i = 0; i < description.getTransforms().size() && !found; i++) {
      if (description.getTransforms().get(i).equals(transform)) {
        index = i;
        found = true;
      }
    }

    // Even if the transform is not found, we still need to update the canvas
    if (found) {
      try {
        description.getTransforms().get(index);
        description.removeTransform(index);
      } catch (IndexOutOfBoundsException e) {
        Logger.getLogger(ChaosGame.class.getName())
            .warning("Index out VBox containing values for transform of bounds");
      }
    }
    updateCanvas();
  }
}
