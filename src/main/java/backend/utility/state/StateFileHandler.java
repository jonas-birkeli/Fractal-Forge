package backend.utility.state;

import static config.StateConfig.DEFAULT_SAVE_STATE_PATH;

import backend.core.ChaosGameFileHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Class that handles loading and saving the state of the application into a file.
 * The state consists of the current chaos game description and the path to the css file.
 *
 * @version 1.0
 * @author proggang
 * @since 04.05.2024
 */
public class StateFileHandler extends ChaosGameFileHandler {
  private State state;

  /**
   * Constructor for the StateFileHandler.
   *
   * @since 1.0
   */
  public StateFileHandler() {
    super();
    state = null;
  }

  /**
   * Returns the state of the application.
   *
   * @return the state of the application
   */
  public State getState() {
    return state;
  }

  /**
   * Sets the state of the application.
   *
   * @param state the state to set
   * @since 1.0
   */
  public void setState(State state) {
    this.state = state;
  }

  /**
   * Sets the state of the application.
   * Saves the currently set state to the file.
   * Set a new state by setting the state to the given state.
   *
   * @see #setState(State)
   * @since 1.0
   */
  public void saveState() {
    if (state.getChaosGameDescription() == null) {
      Logger.getLogger(StateFileHandler.class.getName())
          .warning("Chaos game description is null. Not saving state to file.");
      return;
    }

    try {
      writeToFile(state.getChaosGameDescription(), DEFAULT_SAVE_STATE_PATH);
    } catch (IOException e) {
      Logger.getLogger(StateFileHandler.class.getName())
          .warning("Error saving state to file " + DEFAULT_SAVE_STATE_PATH + ". " + e);
    }
    try (BufferedWriter writer = new BufferedWriter(
        new FileWriter(DEFAULT_SAVE_STATE_PATH, true)
    )) {
      // CSS
      writer.newLine();
      writer.write("# CSS file path");
      writer.newLine();
      writer.write(state.getCssPath());
      writer.newLine();

      // FXML view
      writer.write("# View file path");
      writer.newLine();
      writer.write(state.getViewPath());
      writer.newLine();
    } catch (Exception e) {
      Logger.getLogger(StateFileHandler.class.getName())
          .warning("Error saving state to file: " + DEFAULT_SAVE_STATE_PATH + ". " + e);
    }

  }

  /**
   * Loads the state of the application from the file.
   * Sets the loaded state as the current state.
   * If the file is not found, a warning is logged and the state is not set.
   * Returns null if a file is made earlier.
   *
   * @return the loaded state. Might be null if a file is not found.
   * @see State
   * @since 1.0
   */
  public State loadState() {
    State.Builder builder = new State.Builder();
    String path = DEFAULT_SAVE_STATE_PATH;

    try (Scanner scanner = new Scanner(new File(path))) {
      if (builder.chaosGameDescription(readFromFile(path)) == null) {
        Logger.getLogger(StateFileHandler.class.getName())
            .warning("Error reading chaos game description from file: " + path);
        return null;
      }

      // Skipping the transform and going to the css path
      boolean end = false;
      while (scanner.hasNextLine() && !end) {
        String line = scanner.nextLine().trim();
        if (line.equals("# CSS file path")) {
          end = true;
        }
      }

      String cssPath = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
      builder = builder.cssPath(cssPath);

      scanner.nextLine();  // Skip line
      String viewPath = scanner.nextLine().replaceAll("\\s*#.*", "").trim();
      builder  = builder.viewPath(viewPath);

      state = builder.build();  // Save builder
      return state;

    } catch (FileNotFoundException e) {
      Logger.getLogger(StateFileHandler.class.getName())
          .warning("File not found: " + path + ". " + e);
    }
    return null;
  }
}
