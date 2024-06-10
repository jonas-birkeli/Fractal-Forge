package frontend.controllers;

import static config.DataConfig.MAIN_FILE_PATH;
import static config.DataConfig.PRESET_FILE_PATH;
import static config.DataConfig.SETTINGS_FILE_PATH;

import backend.core.ChaosGameFileHandler;
import backend.utility.state.StateHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Represents the controller for the menu.fxml file.
 * Handles the start, load, exit and settings buttons.
 * Handles the dark mode toggle button.
 *
 * @version 1.0
 * @author jonas
 * @since 21.05.2024
 */
public class MenuController {
  @FXML
  public HBox titleContainer;
  @FXML
  public Label title;
  @FXML
  public HBox menuButtons;
  @FXML
  public Button loadButton;
  @FXML
  public HBox exitContainer;
  @FXML
  public Button exit;
  @FXML
  private Button startButton;
  @FXML
  private CheckBox darkModeButton;

  /**
   * Initializes the controller.
   * Does nothing.
   *
   * @since 1.0
   */
  public void initialize() {
    // empty
  }

  /**
   * Handles the start button. Loads the main.fxml file.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  private void handleStartButton(ActionEvent actionEvent) {
    try {
      FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(MAIN_FILE_PATH));
      Parent root = mainLoader.load();

      // Set the view path to main.fxml when the file is actually loaded
      StateHandler.getInstance().setViewPath(MAIN_FILE_PATH);

      Stage stage = (Stage) startButton.getScene().getWindow();


      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      Logger.getLogger(MenuController.class.getName()).severe("Failed to load main.fxml");
    }
  }

  /**
   * Handles the load button. Opens a file chooser dialog to load a file.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  private void handleLoadButton(ActionEvent actionEvent)  {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");

    // Set initial directory (optional)
    fileChooser.setInitialDirectory(new File("" + Paths.get(PRESET_FILE_PATH)));

    // Set file filters (optional)
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
        "Text files (*.txt)", "*.txt"
    );
    fileChooser.getExtensionFilters().add(extFilter);

    // Show open file dialog
    File file = fileChooser.showOpenDialog(new Stage());

    if (file == null) {
      // Process the selected file (if any)
      Logger.getLogger(MenuController.class.getName()).info("No file selected");
      return;
    }

    Parent root = null;

    // Load main.fxml
    // try to load the correct fractal from file using its path
    try {
      // Updates the state description for persistence
      StateHandler.getInstance().setDescription(
          ChaosGameFileHandler.readFromFile(file.getAbsolutePath())
      );

      if (StateHandler.getInstance().getChaosGameDescription() == null) {
        Logger.getLogger(MenuController.class.getName())
            .severe("Failed to load chaos game description from file");
        return;
      }

      Logger.getLogger(MenuController.class.getName()).info("Loading main stage");
      FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(MAIN_FILE_PATH));
      root = mainLoader.load();

      // Set the view path to main.fxml
      StateHandler.getInstance().setViewPath(MAIN_FILE_PATH);

    } catch (FileNotFoundException e) {
      Logger.getLogger(MenuController.class.getName())
            .severe("Failed to load file " + file.getAbsolutePath() + ". " + e);
    } catch (IOException e) {
      Logger.getLogger(MenuController.class.getName())
          .severe("Failed to load main.fxml " + e);
    }

    Stage stage = (Stage) startButton.getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Handles the exit button. Closes the stage.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  public void handleExitButton(ActionEvent actionEvent) {
    Stage stage = (Stage) startButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Handles the dark mode button. Sets the css path to darkMode.css if the button is selected.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  public void handleDarkModeButton(ActionEvent actionEvent) {
    if (darkModeButton.isSelected()) {
      StateHandler.getInstance().setCssPath("/css/darkMode.css");
    } else {
      StateHandler.getInstance().setCssPath("");  // empty string means no css
    }
  }

  /**
   * Handles the settings button. Loads the settings.fxml file.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.1
   */
  @FXML
  public void handleSettingsButton(ActionEvent actionEvent) {
    try {
      FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(SETTINGS_FILE_PATH));
      Parent root = mainLoader.load();

      // Set the view path to main.fxml when the file is actually loaded
      StateHandler.getInstance().setViewPath(SETTINGS_FILE_PATH);

      Stage stage = (Stage) startButton.getScene().getWindow();


      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      Logger.getLogger(MenuController.class.getName()).severe("Failed to load settings.fxml");
    }
  }
}
