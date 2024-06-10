package frontend.controllers;

import static config.DataConfig.AFFINE_NAME;
import static config.DataConfig.JULIA_NAME;
import static config.DataConfig.MENU_FILE_PATH;
import static config.DataConfig.SELECT_TYPE_CONSTANT;
import static config.DisplayConfig.DEFAULT_DECIMAL_FORMAT;

import backend.core.ChaosGame;
import backend.core.ChaosGameDescriptionFactory;
import backend.models.MainModel;
import backend.utility.state.StateHandler;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The controller for the interactive part for the chaos game.
 * Handles the choice box and the sliders.
 *
 * @version 1.2
 * @author proggang
 * @since 25.04.2024
 */
public class MainController {

  private MainModel model;
  @FXML
  private ChoiceBox<String> choiceBox;
  @FXML
  private Canvas canvas;
  @FXML
  private VBox transformContainer;
  @FXML
  private SplitPane root;
  @FXML
  private Label canvasSizeValueLabel;
  @FXML
  private Slider canvasSizeSlider;
  @FXML
  private Label iterationValueLabel;
  @FXML
  private Slider iterationSlider;
  @FXML
  private CheckBox heatmapCheckbox;
  @FXML
  private Button backButton;
  @FXML
  private CheckBox inverseCheckbox;
  @FXML
  private CheckBox trackCursorPosCheckbox;
  @FXML
  private TextField saveToFile;
  @FXML
  private TextField rootNumberField;

  /**
   * Initializes the choice box and adds listeners to the canvas size.
   *
   * @since 1.0
   */
  public void initialize() {
    model = new MainModel(this);
    choiceBox.getItems().addAll(AFFINE_NAME, JULIA_NAME);

    root.widthProperty().addListener((observable, oldValue, newValue) -> {
      canvas.setWidth(newValue.doubleValue() - 200);  // 200 to account for the sidebar
      model.update();
    });
    root.heightProperty().addListener((observable, oldValue, newValue) -> {
      canvas.setHeight(newValue.doubleValue());
      model.update();
    });


    // ************************************
    // Initializing text fields for sliders
    // ************************************
    DecimalFormat formatter = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);

    String formattedNumber = formatter.format(canvas.getWidth());
    canvasSizeSlider.setValue(canvas.getWidth());
    canvasSizeValueLabel.setText(formattedNumber + "x" + formattedNumber); // Resolution 600x600

    iterationSlider.setValue(model.getSteps());
    iterationValueLabel.setText(formatter.format(model.getSteps() / 1000) + "k");  // 50k

    choiceBox.setValue(SELECT_TYPE_CONSTANT);

    if (choiceBox.getValue() != null && choiceBox.getValue().equals(SELECT_TYPE_CONSTANT)) {
      model.setChaosGame(new ChaosGame(
          StateHandler.getInstance().getChaosGameDescription(),
          (int) canvasSizeSlider.getValue(),
          (int) canvasSizeSlider.getValue()
      ));

      choiceBox.setValue(model.getChaosGame().isAffine() ? AFFINE_NAME : JULIA_NAME);
      model.update();
    }
  }

  /**
   * Constructor, initializes the model.
   * Not used directly.
   *
   * @see #initialize()
   * @since 1.0
   */
  public MainController() {
    model = null;
  }

  /**
   * Handles the choice box action.
   * Updates the chaos game based on the selected value.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  public void handleChoiceBoxAction(ActionEvent actionEvent) {
    // if the chaos game is affine, and the choice box is julia, change to standard julia
    // if the chaos game is julia, and the choice box is affine, change to standard affine

    if (model.getChaosGame().isAffine()) {
      if (choiceBox.getValue().equals(JULIA_NAME)) {
        model.setChaosGame(new ChaosGame(
            new ChaosGameDescriptionFactory().getPredefinedChaosGame(JULIA_NAME),
            (int) canvasSizeSlider.getValue(),
            (int) canvasSizeSlider.getValue()
        ));
      }
    } else {
      // Game is not affine
      if (choiceBox.getValue().equals(AFFINE_NAME)) {
        // Meaning we changed from julia to affine
        model.setChaosGame(new ChaosGame(
            new ChaosGameDescriptionFactory().getPredefinedChaosGame(AFFINE_NAME),
            (int) canvasSizeSlider.getValue(),
            (int) canvasSizeSlider.getValue()
        ));

        heatmapCheckbox.setDisable(false);
      }
    }

    updateUsableItems();

    transformContainer.getChildren().removeAll(transformContainer.getChildren());
    model.createTextFields(transformContainer);
    model.update();
  }

  /**
   * Updates the different items by checking what type of fractal is showing.
   * Disabling sertain features like iterations when a julia transform is showing.
   *
   * @since 1.2
   */
  public void updateUsableItems() {
    if (model.getChaosGame().isAffine()) {
      choiceBox.setValue("Affine");

      iterationSlider.setDisable(false); // Enable iteration slider
      inverseCheckbox.setDisable(true);  // Disable inverse possibility
      trackCursorPosCheckbox.setDisable(true);  // Disable tracking cursor position
      trackCursorPosCheckbox.setSelected(false);
      toggleTrackCursorPos(null); // Update the tracking cursor position

      model.enableInverseTransform(false);  // Disable inverse transform on backend
    } else {
      choiceBox.setValue(JULIA_NAME);
      inverseCheckbox.setDisable(false); // Enable inverse possibility
      trackCursorPosCheckbox.setDisable(false);  // Enable tracking cursor position
      updateInverseTransform(null);  // Update whether to use inverse or not

      // If inverse is selected, disable iteration slider
      if (inverseCheckbox.isSelected()) {
        iterationSlider.setDisable(true);  // disable iteration slider
        updateInverseTransform(null);
      } else {
        iterationSlider.setDisable(false);
        // Enable iteration slider because inverse is not selected
      }
    }
  }

  /**
   * Updates the iterations of the chaos game.
   *
   * @param mouseEvent the action event, disregarded
   * @since 1.0
   */
  @FXML
  public void updateIterations(MouseEvent mouseEvent) {
    int steps = (int) iterationSlider.getValue();
    DecimalFormat formatter = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);
    String formattedNumber = formatter.format(steps / 1000);
    iterationValueLabel.setText(formattedNumber + "k");

    model.setSteps(steps);
    model.update();
  }

  /**
   * Toggles whether to use heatmap tracking or not when drawing the canvas.
   *
   * @param actionEvent the mouse event, disregarded
   * @since 1.1
   */
  @FXML
  public void toggleHeatmap(ActionEvent actionEvent) {
    model.toggleHeatmap(heatmapCheckbox.isSelected());
  }

  /**
   * Updates the canvas size based on the slider value.
   * The canvas size is updated by setting the width and height of the canvas.
   *
   * @param mouseEvent the mouse event, disregarded
   * @since 1.1
   */
  @FXML
  public void updateCanvasSize(MouseEvent mouseEvent) {
    model.updateCanvasSize(canvasSizeSlider.getValue());
    DecimalFormat formatter = new DecimalFormat(DEFAULT_DECIMAL_FORMAT);
    String formattedNumber = formatter.format(canvasSizeSlider.getValue());
    canvasSizeValueLabel.setText(formattedNumber + "x" + formattedNumber);
  }

  /**
   * Handles the back button.
   * Loads the menu, and saves the state of the application.
   *
   * @param actionEvent the action event, disregarded
   * @throws IOException if a file reading error occurs
   * @since 1.1
   */
  public void handleBackButton(ActionEvent actionEvent) throws IOException {
    StateHandler.getInstance().setViewPath(MENU_FILE_PATH);
    FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU_FILE_PATH));
    Parent root = loader.load();

    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Toggles whether to use the inverse transform or not.
   * The inverse transform is used to create the affine chaos game.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void updateInverseTransform(ActionEvent actionEvent) {
    iterationSlider.setDisable(inverseCheckbox.isSelected());
    // Disable iteration slider if inverse is selected

    if (!inverseCheckbox.isSelected()) {
      rootNumberField.setText("Enable Inverse");
      rootNumberField.setDisable(true);

    } else {
      rootNumberField.setText("");
      rootNumberField.setDisable(false);
    }

    model.enableInverseTransform(inverseCheckbox.isSelected());
    heatmapCheckbox.setDisable(inverseCheckbox.isSelected());
    heatmapCheckbox.setSelected(false);

    // Disable heatmap if inverse is selected
  }

  /**
   * Toggles whether to use the cursor position for the julia transform values.
   * If enabled, the cursor position is used to update the julia transform values.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void toggleTrackCursorPos(ActionEvent actionEvent) {
    if (trackCursorPosCheckbox.isSelected()) {
      canvas.setOnMouseMoved(event -> {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double contentX = (mouseX / canvas.getWidth()) * 2 - 1;
        double contentY = (mouseY / canvas.getHeight()) * 2 - 1;

        model.createTextFields(transformContainer);
        model.updateCursorPos(contentX, contentY);
      });
    } else {
      canvas.setOnMouseMoved(null);
    }
  }

  /**
   * Handles the save to file text field. When a text enters the area and enter is hit, the chaos
   * game is saved to the file. The file is stored at the default destination with .txt as the file
   * extension.
   * If there occurs an error, the text field is marked with red.
   * If the save is successful, the text field is marked with green.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void handleSaveToFile(ActionEvent actionEvent) {
    String filePath = saveToFile.getText();
    if (filePath.isEmpty()) {
      saveToFile.setStyle("-fx-border-color: red; -fx-border-width: 3px");
      Logger.getLogger(MainController.class.getName()).severe("No file path given");
      return;
    }

    try {
      model.saveToFile(filePath);
      saveToFile.setStyle("-fx-border-color: #01fa01; -fx-border-width: 3px");
    } catch (IOException e) {
      saveToFile.setStyle("-fx-border-color: red; -fx-border-width: 3px");
      Logger.getLogger(MainController.class.getName()).severe("Error saving to file");
    }
  }

  /**
   * Updates the root number of the chaos game.
   * Used in the julia transforms to update the root number.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void updateRootNumber(ActionEvent actionEvent) {
    String rootNumber = rootNumberField.getText();
    if (rootNumber.isEmpty()) {
      rootNumberField.setStyle("-fx-border-color: red; -fx-border-width: 3px");
    }
    try {
      int number = Integer.parseInt(rootNumber);
      model.setRootNumber(number);
      rootNumberField.setStyle("-fx-border-color: #01fa01; -fx-border-width: 3px");
    } catch (NumberFormatException | IllegalAccessException e) {
      rootNumberField.setStyle("-fx-border-color: red; -fx-border-width: 3px");
    }
  }

  /**
   * Zooms the canvas in.
   * The zoom is done by increase the minimum and maximum coordinates of the canvas.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void zoomDown(ActionEvent actionEvent) {
    model.zoom(-1);
  }

  /**
   * Zooms the canvas out.
   * The zoom is done by decreasing the minimum and maximum coordinates of the canvas.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void zoomUp(ActionEvent actionEvent) {
    model.zoom(1);
  }

  /**
   * Adds a transform to the chaos game.
   * The transform is added to the chaos game description.
   *
   * @param actionEvent the action event, disregarded
   * @since 1.2
   */
  public void addTransform(ActionEvent actionEvent) {
    model.addTransform();
    model.createTextFields(transformContainer);
    model.update();
  }

  /**
   * Returns the canvas element of the scene.
   *
   * @return the canvas element of the scene
   * @since 1.2
   */
  public Canvas getCanvas() {
    return canvas;
  }
}
