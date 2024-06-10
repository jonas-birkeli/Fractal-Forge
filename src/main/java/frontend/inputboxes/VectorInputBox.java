package frontend.inputboxes;

import backend.geometry.Vector;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents input fields for the vector elements containing the chances of each transform
 * in the chaos game.
 * Used to represent the values of a vector.
 *
 * @version 1.0
 * @author jonas
 * @since 19.05.2024
 */
public class VectorInputBox {
  private final Vector vector;
  private final Consumer<Void> updateFunction;

  /**
   * Constructs a new input box for vector elements.
   *
   * @param vector the vector that needs to be displayed
   * @param updateFunction the update function to call when a value changes
   * @since 1.0
   */
  public VectorInputBox(Vector vector, Consumer<Void> updateFunction) {
    this.vector = vector;
    this.updateFunction = updateFunction;
  }

  /**
   * Constructs the HBox with its contained information and editable values.
   * The HBox contains a text field for each vector element.
   *
   * @return the HBox containing the vector elements
   * @since 1.0
   */
  public VBox constructBox() {
    List<Double> vectorElements = new java.util.ArrayList<>(vector.getAllElements().toList());
    VBox vbox = new VBox();
    vbox.getChildren().add(new Label("Chance for each transform\nExample: 50, 75, 100"));

    for (int i = 0; i < vectorElements.size(); i++) {
      TextField textField = constructTextFieldFromVector(vectorElements, i, vector);
      vbox.getChildren().add(textField);
    }
    return vbox;
  }


  /**
   * Constructs a text field for the vector elements.
   * Adds a listener to the text field to update the vector elements.
   *
   * @param vectorElements the vector elements to update
   * @param i the index of the vector element
   * @param vector the vector to update
   * @return the text field for the vector elements
   * @since 1.1
   */
  private TextField constructTextFieldFromVector(
      List<Double> vectorElements, int i, Vector vector
  ) {

    TextField textField = new TextField("" + vectorElements.get(i));
    textField.setId("vectorTextField");

    // Mnemonic parsing
    Label label = new Label("" + vectorElements.get(i));
    label.setMnemonicParsing(true);
    label.setLabelFor(textField);

    textField.setOnAction(e -> {
      if (textField.getText().replaceAll("[.\\-\\s]", "").isEmpty()) {
        return;
      }
      vectorElements.set(i, Double.parseDouble(textField.getText()));
      vector.setElement(i, Double.parseDouble(textField.getText()));

      // Loop through all elements of the vector and check if they are all zero

      updateFunction.accept(null);
    });
    textField.focusedProperty().addListener((obs, wasFocues, isNowFocused) -> {
      if (textField.getText().replaceAll("[.\\-\\s]", "").isEmpty()) {
        return;
      }
      vectorElements.set(i, Double.parseDouble(textField.getText()));
      vector.setElement(i, Double.parseDouble(textField.getText()));

      updateFunction.accept(null);
    });
    return textField;
  }
}
