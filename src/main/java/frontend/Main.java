package frontend;

import static config.StateConfig.DEFAULT_CSS_PATH;

import backend.utility.state.StateHandler;
import config.DisplayConfig;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main GUI application class.
 *
 * @version 1.1
 * @author Maven
 * @since 1.0
 */
public class Main extends Application {

  /**
   * Starts the application.
   *
   * @param primaryStage the primary stage
   * @throws IOException if a file reading error occurs
   * @since 1.0
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    loadFromState(primaryStage);
    /*
    FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
    Parent menuRoot = menuLoader.load();

    // Scene size and title setting
    Scene scene = new Scene(
        menuRoot,
        DisplayConfig.PREF_SCREEN_WIDTH,
        DisplayConfig.PREF_SCREEN_HEIGHT
    );
    primaryStage.setTitle(DisplayConfig.APPLICATION_TITLE);
    primaryStage.setMinWidth(DisplayConfig.MIN_SCREEN_WIDTH);
    primaryStage.setMinHeight(DisplayConfig.MIN_SCREEN_HEIGHT);

    primaryStage.setScene(scene);
    primaryStage.show();
     */
  }

  /**
   * Loads the view from the state.
   * If the state is null, the default view is loaded.
   *
   * @param primaryStage the primary stage
   * @throws IOException if a file reading error occurs
   * @since 1.1
   */
  public void loadFromState(Stage primaryStage) throws IOException {
    String viewPath;

    if (StateHandler.getInstance().isStateNull()) {
      Logger.getLogger(Main.class.getName())
          .warning("View path is null. Loading default view.");
      viewPath = "/menu.fxml";

      // Setting some default values for the state
      StateHandler.getInstance().setViewPath(viewPath);
      StateHandler.getInstance().setCssPath(DEFAULT_CSS_PATH);
    } else {
      // State is not null, meaning that the view path is saved in the state
      viewPath = StateHandler.getInstance().getState().getViewPath();
      StateHandler.getInstance().setViewPath(viewPath);
    }

    if (viewPath.isEmpty()) {
      Logger.getLogger(Main.class.getName())
          .warning("View path is empty. Loading default view.");
      viewPath = "/menu.fxml";

      // Setting some default values for the state
      StateHandler.getInstance().setViewPath(viewPath);
      StateHandler.getInstance().setCssPath(DEFAULT_CSS_PATH);
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
    Parent root = loader.load();

    Scene scene;
    scene = new Scene(root, DisplayConfig.PREF_SCREEN_WIDTH, DisplayConfig.PREF_SCREEN_HEIGHT);

    primaryStage.setTitle(DisplayConfig.APPLICATION_TITLE);
    primaryStage.setMinWidth(DisplayConfig.MIN_SCREEN_WIDTH);
    primaryStage.setMinHeight(DisplayConfig.MIN_SCREEN_HEIGHT);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Stops the application.
   *
   * @since 1.0
   */
  @Override
  public void stop() {
    Logger.getLogger(Main.class.getName()).info("Saving state...");
    StateHandler.getInstance().saveState();
    Logger.getLogger(Main.class.getName()).info("Application closing...");
    System.exit(0);
  }

  /**
   * Launches the application.
   *
   * @param args the command line arguments
   * @since 1.0
   */
  public static void main(String[] args) {
    launch();
  }
}