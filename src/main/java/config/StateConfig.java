package config;

/**
 * Represents final variables for the state configuration.
 * Includes default path variables.
 *
 * @version 1.0
 * @author jonas
 * @since 22.05.2024
 */
public class StateConfig {
  public static final String DEFAULT_SAVE_STATE_PATH = "data/state/save_state.txt";

  public static final String DEFAULT_CSS_PATH = "css/default_theme.css";
  public static final String DEFAULT_VIEW_PATH = "/menu.fxml";

  // Hide the constructor
  private StateConfig() {
    // do nothing
  }
}
