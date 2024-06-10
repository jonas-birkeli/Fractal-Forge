package config;

/**
 * Represents the configuration of the data.
 * Contains the paths to the default affine and julia presets, and the path to the presets folder.
 *
 * @version 1.0
 * @author proggang
 * @since 13.05.2024
 */
public class DataConfig {
  public static final String PRESET_FILE_PATH = "./data/presets/";
  public static final String DEFAULT_AFFINE_FILE_PATH = PRESET_FILE_PATH + "barnsley-fern.txt";
  public static final String DEFAULT_JULIA_FILE_PATH = PRESET_FILE_PATH + "julia.txt";

  public static final String MENU_FILE_PATH = "/menu.fxml";
  public static final String MAIN_FILE_PATH = "/main.fxml";
  public static final String SETTINGS_FILE_PATH = "/settings.fxml";

  public static final String AFFINE_NAME = "Affine";
  public static final String JULIA_NAME = "Julia";
  public static final String SELECT_TYPE_CONSTANT = "Select type";

  // Hide the constructor
  private DataConfig() {
    // do nothing
  }
}
