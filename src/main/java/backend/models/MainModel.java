package backend.models;


import static config.DataConfig.PRESET_FILE_PATH;
import static config.DisplayConfig.END_COLOR;
import static config.DisplayConfig.START_COLOR;

import backend.core.ChaosGame;
import backend.core.ChaosGameDescriptionFactory;
import backend.core.ChaosGameFileHandler;
import backend.core.ChaosGameObserver;
import backend.geometry.Complex;
import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.JuliaTransform;
import backend.transforms.Transform;
import backend.utility.state.StateHandler;
import frontend.controllers.MainController;
import frontend.inputboxes.AffineInputBox;
import frontend.inputboxes.JuliaInputBox;
import frontend.inputboxes.VectorInputBox;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Class for interacting with the view-model of the chaos game.
 *
 * @version 1.1
 * @author proggang
 * @since 04.04.2024
 */
public class MainModel implements ChaosGameObserver {
  private ChaosGame chaosGame;
  private final MainController controller;
  private int steps;
  private boolean fancyColoring;
  private boolean inverseTransform;

  /**
   * Constructs a new model with the given chaos game.
   *
   * @since 1.0
   */
  public MainModel(MainController controller) {
    this.controller = controller;
    inverseTransform = false;

    // If the state is not null, set the chaos game description to the state
    if (StateHandler.getInstance().getChaosGameDescription() != null) {
      chaosGame = new ChaosGame(
          StateHandler
              .getInstance()
              .getChaosGameDescription(),
          (int) controller.getCanvas().getWidth(),
          (int) controller.getCanvas().getHeight()
      );
    } else {
      chaosGame = new ChaosGameDescriptionFactory().getDefaultChaosGame();
      StateHandler.getInstance().setDescription(chaosGame.getDescription());
    }

    update();
    steps = 10000;
  }

  /**
   * Creates text fields for the transforms of the chaos game.
   *
   * @param container the container to add the text fields to
   * @since 1.0
   */
  public void createTextFields(VBox container) {
    // Clearing the container for new text fields
    container.getChildren().clear();

    for (int i = 0; i < chaosGame.getDescription().getTransforms().size(); i++) {
      Transform transform = chaosGame.getDescription().getTransforms().get(i);

      Consumer<Void> updateFunction = unused -> chaosGame.updateCanvas();
      Consumer<Transform> removeFunction = unused -> chaosGame.removeTransform(transform);

      if (transform instanceof AffineTransform transformFromDesc) {
        container.getChildren().add(
            new AffineInputBox(transformFromDesc, updateFunction, removeFunction)
        );
      } else {
        container.getChildren().add(
            new JuliaInputBox(transform, updateFunction, removeFunction)
        );
      }
    }

    Consumer<Void> updateFunction = unused -> chaosGame.updateCanvas();

    VectorInputBox vectorInputBox =
        new VectorInputBox(chaosGame.getDescription().getProbability(), updateFunction);
    container.getChildren().add(vectorInputBox.constructBox());
  }

  /**
   * Sets the chaos game of this model.
   *
   * @param chaosGame the chaos game of this model
   * @since 1.0
   */
  public void setChaosGame(ChaosGame chaosGame) {
    this.chaosGame = chaosGame;
    chaosGame.addObserver(this);
  }

  /**
   * Runs the game for the given number of steps. If the chaos game is null, does nothing.
   *
   * @since 1.0
   */
  public void runIterations() {
    chaosGame.getCanvas().clear();

    if (inverseTransform) {
      try {
        chaosGame.runInverse();
      } catch (IllegalAccessException e) {
        Logger.getLogger(MainModel.class.getName())
            .severe("Inverse Julia set can only be run with JuliaTransform. " + e);
      }
    } else {
      chaosGame.runSteps(steps);
    }
  }

  /**
   * Draws the canvas of the chaos game.
   * Supports fancy coloring.
   *
   * @since 1.0
   */
  public void drawCanvas() {
    GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();
    gc.clearRect(0, 0, controller.getCanvas().getWidth(), controller.getCanvas().getHeight());

    double[][] canvasArray = chaosGame.getCanvas().getCanvasArray();

    double pixelWidth = controller.getCanvas().getWidth() / canvasArray.length;
    double pixelHeight = controller.getCanvas().getHeight() / canvasArray[0].length;

    if (!fancyColoring) {
      // Skip loading the same color thousands of times, faster to load once
      gc.setFill(Color.BLACK);
    }

    for (int y = 0; y < canvasArray.length; y++) {
      for (int x = 0; x < canvasArray.length; x++) {
        double freq = canvasArray[y][x];
        if (freq == 0) {
          continue;
        }
        if (fancyColoring) {
          gc.setFill(calculateColor(freq));
        }
        gc.fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);
      }
    }
  }

  // ChatGPT generated code, no trivial effect
  private Color calculateColor(double freq) {
    // Define the range of colors

    // Map the value to a range between 0 and 1
    double normalizedValue = Math.min(Math.max(freq, 0.0), 1.0);

    // Interpolate between startColor and endColor based on the normalized value
    double hue = START_COLOR.getHue()
        + (END_COLOR.getHue() - START_COLOR.getHue()) * normalizedValue;

    double saturation = START_COLOR.getSaturation()
        + (END_COLOR.getSaturation() - START_COLOR.getSaturation()) * normalizedValue;

    double brightness = START_COLOR.getBrightness()
        + (END_COLOR.getBrightness() - START_COLOR.getBrightness()) * normalizedValue;

    // Create and return the interpolated color
    return Color.hsb(hue, saturation, brightness);
  }

  /**
   * Updates the model by rerunning the iterations and redrawing the canvas.
   *
   * @since 1.0
   */
  @Override
  public void update() {
    runIterations();
    drawCanvas();
    StateHandler.getInstance().setDescription(chaosGame.getDescription());
  }

  /**
   * Zooms the chaos game by the given value.
   * The value should be between -10 and 10.
   *
   * @param value the value to zoom by
   */
  public void zoom(double value) {
    Vector scaleVector = new Vector(value, value);

    chaosGame.getDescription().getMaxCoords().sub(scaleVector);
    chaosGame.getDescription().getMinCoords().add(scaleVector);

    chaosGame.updateCanvas();
    update();
  }

  /**
   * Sets the number of steps in the chaos game.
   * The number of steps should be between 1 and 10000000.
   *
   * @param steps the number of steps in the chaos game
   * @since 1.1
   */
  public void setSteps(int steps) {
    this.steps = steps;
    drawCanvas();
  }

  /**
   * Returns the number of steps in the chaos game.
   *
   * @return the number of steps in the chaos game
   * @since 1.1
   */
  public int getSteps() {
    return steps;
  }

  /**
   * Toggles whether to use heatmap tracking when drawing the chaos game.
   *
   * @param selected if the heatmap should be used
   * @since 1.1
   */
  public void toggleHeatmap(boolean selected) {
    fancyColoring = selected;
    update();
  }

  /**
   * Updates the canvas size of the chaos game.
   * The value should be between 100 and 1000 for performance reasons.
   * Updates the chaos game and redraws the canvas.
   *
   * @param value the new size of the canvas
   * @since 1.1
   */
  public void updateCanvasSize(double value) {
    chaosGame.setSize((int) value);
  }

  /**
   * Sets the file path of the chaos game, and updates the canvas.
   * If the file is not found, logs an error message.
   *
   * @param filePath the file path of the chaos game
   * @since 1.1
   */
  public void setFilePath(String filePath) {
    if (chaosGame != null) {
      chaosGame.removeObserver(this);
    }


    chaosGame = new ChaosGame(
        ChaosGameFileHandler.readFromFile(filePath),
        (int) controller.getCanvas().getWidth(),
        (int) controller.getCanvas().getHeight()
    );

    if (chaosGame.getDescription() == null) {
      String errorMessage = "File not found: " + filePath + ". ";
      Logger.getLogger(MainModel.class.getName())
          .severe(errorMessage);

      // Default config, boring
      chaosGame = new ChaosGameDescriptionFactory().getDefaultChaosGame();
    }
    chaosGame.addObserver(this);
    chaosGame.updateCanvas();
  }

  /**
   * Returns the chaos game of this model.
   *
   * @return the chaos game of this model
   * @since 1.1
   */
  public ChaosGame getChaosGame() {
    return chaosGame;
  }

  /**
   * Toggles whether to use the inverse transform or not.
   * If the inverse transform is used, the chaos game will run the inverse of the Julia set.
   *
   * @since 1.2
   */
  public void enableInverseTransform(boolean value) {
    inverseTransform = value;
    update();
  }

  /**
   * Updates the cursor position of the Julia set.
   * The cursor position is used to calculate the Julia set.
   * The x and y values should be between -1 and 1.
   *
   * @param x the x-coordinate of the cursor
   * @param y the y-coordinate of the cursor
   * @since 1.2
   */
  public void updateCursorPos(double x, double y) {
    JuliaTransform transform = (JuliaTransform) chaosGame.getDescription().getTransforms().get(0);
    transform.getPoint().setElement(0, x);
    transform.getPoint().setElement(1, y);

    update();

  }

  /**
   * Saves the chaos game to a file with the given file name.
   * The file name should not contain the file extension.
   *
   * @param fileName the name of the file to save the chaos game to
   * @throws IOException if the file cannot be saved
   * @since 1.2
   */
  public void saveToFile(String fileName) throws IOException {
    String path = PRESET_FILE_PATH + fileName + ".txt";
    ChaosGameFileHandler.writeToFile(chaosGame.getDescription(), path);
  }

  /**
   * Sets the root number of the Julia set.
   *
   * @param number the number to set the root to
   * @throws IllegalAccessException if the Julia set cannot be run with the given transform
   * @since 1.2
   */
  public void setRootNumber(int number) throws IllegalAccessException {
    try {
      JuliaTransform juliaTransform = (JuliaTransform) chaosGame.getDescription().getTransforms()
          .get(0);
      juliaTransform.setPower(new int[]{number});
      update();
    } catch (ClassCastException e) {
      throw new IllegalAccessException("Julia set can only be run with JuliaTransform");
    }

  }

  /**
   * Adds a transform to the chaos game.
   * The transform consists of only zeros.
   *
   * @since 1.2
   */
  public void addTransform() {
    if (chaosGame.isAffine()) {
      AffineTransform transform =
          (AffineTransform) chaosGame.getDescription().getTransforms().get(0);
      int size = transform.getMatrix().getSize();

      MatrixNxN matrix = new MatrixNxN(new double[size * size]);
      Vector vector = new Vector(new double[size]);

      chaosGame.getDescription().getTransforms().add(new AffineTransform(matrix, vector));
    } else {
      chaosGame.getDescription().getTransforms().add(new JuliaTransform(new Complex(0, 0), 1));
    }
    chaosGame.getDescription().clearProbabilities();
  }
}