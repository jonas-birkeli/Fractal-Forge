package frontend.inputboxes;

import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.JuliaTransform;
import backend.transforms.Transform;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Represents the building blocks for an input box field,
 * used to display the values used in a transformation.
 *
 * @version 1.0
 * @author jonas
 * @since 15.05.2024
 */
public abstract class AbstractInputBox extends VBox {
  protected Transform transform;
  protected Consumer<Void> updateFunction;
  protected Consumer<Transform> removeFunction;
  protected boolean hasValue;

  /**
   * Constructs the VBox with its contained information and editable values.
   *
   * @param transform the transformation to be displayed
   * @param updateFunction the function to call when a value changes
   * @since 1.0
   */
  AbstractInputBox(
      Transform transform, Consumer<Void> updateFunction, Consumer<Transform> removeFunction
  ) {
    this.transform = transform;
    this.updateFunction = updateFunction;
    this.removeFunction = removeFunction;
    hasValue = false;
  }

  /**
   * Checks if the transformation has any values, not including 0.
   * Updated the hasValue variable respectively.
   * Calling the Remove Function if the transformation has no values.
   *
   * @since 1.1
   */
  protected void checkValue() {
    hasValue = false;
    // Loop thorough matrix and vector elements and check if they are all zero

    if (transform instanceof AffineTransform affineTransform) {
      hasValue = affineTransform.getMatrix().isZero() && affineTransform.getVector().isZero();
    } else {
      JuliaTransform juliaTransform = (JuliaTransform) transform;
      hasValue = juliaTransform.getPoint().isZero();
    }

    if (hasValue) {
      removeFunction.accept(transform);

      this.getChildren().clear();
    }
  }

  /**
   * Constructs the VBox consisting of all values.
   *
   * @since 1.0
   */
  protected abstract void construct();

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
  protected TextField constructTextFieldFromVector(
      List<Double> vectorElements, int i, Vector vector
  ) {
    if (vectorElements.get(i) != 0.0) {
      hasValue = true;
    }

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

      // Check if the transformation has any values, remove the input box if it does
      checkValue();
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
