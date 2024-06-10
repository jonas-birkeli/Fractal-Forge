package backend.utility.state;


import backend.core.ChaosGameDescription;

/**
 * Singleton class that handles the state of the application.
 * Items stored is the current chaos game description and the current chaos game with its
 * corresponding canvas.
 * The class is a singleton to ensure that the state is consistent throughout the application.
 *
 * @version 1.1
 * @author proggang
 * @since 04.05.2024
 */
public class StateHandler {
  private static StateHandler instance;
  private final StateFileHandler stateFileHandler;
  private State state;
  private State.Builder builder;

  /**
   * Constructor for the StateHandler.
   * Initializes the StateHandler with the StateFileHandler.
   *
   * @since 1.0
   */
  private StateHandler() {
    // Prevent instantiation
    this.stateFileHandler = new StateFileHandler();
    loadState();
    builder = new State.Builder();
  }

  /**
   * Returns the instance of the StateHandler.
   *
   * @return the instance of the StateHandler
   */
  public static StateHandler getInstance() {
    if (instance == null) {
      instance = new StateHandler();
    }
    return instance;
  }

  /**
   * Loads the state of the application from the file. If no state is saved in the file, the state
   * is null.
   *
   * @since 1.0
   */
  private void loadState() {
    state = stateFileHandler.loadState();
    // Load the state from the file
  }

  /**
   * Saves the state of the application to the file.
   * The state consists of the current chaos game description and the path to the css file.
   *
   * @since 1.0
   */
  public void saveState() {
    state = builder.build();
    stateFileHandler.setState(state);
    stateFileHandler.saveState();
  }

  /**
   * Gets the state of the application from the StateHandler.
   * The state consists of the current chaos game description and the path to the css file.
   * If no state is saved in the file, the state is null.
   *
   * @return the state of the application, null if no state is saved
   * @since 1.0
   */
  public State getState() {
    return state;
  }

  /**
   * Gets the chaos game description of the application from the StateHandler.
   *
   * @return the chaos game description of the application.
   *     Null if no chaos game description is set to the state.
   * @since 1.0
   */
  public ChaosGameDescription getChaosGameDescription() {
    if (state == null) {
      return null;
    }
    return state.getChaosGameDescription();
  }

  /**
   * Sets the chaos game description of the application into the StateHandler.
   * The state consists of the current chaos game description and the path to the css file, but the
   * path to the css file is not set.
   *
   * @see State
   * @see #setCssPath(String)
   * @param description the chaos game description@
   * @since 1.1
   */
  public void setDescription(ChaosGameDescription description) {
    if (state != null) {
      state.setChaosGameDescription(description);
    }
    builder = builder.chaosGameDescription(description);
  }

  /**
   * Sets the path to the css file of the application into the StateHandler.
   * The state consists of the current chaos game description and the path to the css file, but the
   * current chaos game description is not set.
   *
   * @see State
   * @see #setDescription(ChaosGameDescription)
   * @param cssPath the path to the css file
   * @since 1.1
   */
  public void setCssPath(String cssPath) {
    builder.cssPath(cssPath);
  }

  /**
   * Checks whether the state is null or not.
   * If the state is null, the state is not saved in the file.
   *
   * @return true if the state is null, false if the state is not null
   * @since 1.1
   */
  public boolean isStateNull() {
    return state == null;
  }

  /**
   * Sets the path to the view file of the application into the StateHandler.
   * The state consists of the current chaos game description and the path to the css file, but the
   * path to the css file is not set.
   *
   * @param viewPath the path to the view file
   * @since 1.1
   */
  public void setViewPath(String viewPath) {
    builder.viewPath(viewPath);
  }
}
