package frontend.inputboxes;

import backend.geometry.MatrixNxN;
import backend.geometry.Vector;
import backend.transforms.AffineTransform;
import backend.transforms.Transform;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Represents an affine input VBox.
 * Used to represent the values of an affine transform.
 *
 * @since 1.1
 */
public class AffineInputBox extends AbstractInputBox {

  /**
   * Constructs a new input box for affine transforms.
   * Consists of six values.
   *
   * @param transform the transform that needs to be displayed
   * @param updateFunction the update function to call when a value changes
   * @param removeFunction the remove function to call when the remove button is clicked
   * @since 1.1
   */
  public AffineInputBox(
      Transform transform, Consumer<Void> updateFunction, Consumer<Transform> removeFunction
  ) {
    super(transform, updateFunction, removeFunction);
    hasValue = true;
    construct();
  }

  @Override
  public void construct() {
    AffineTransform affineTransform = (AffineTransform) transform;
    MatrixNxN matrix = affineTransform.getMatrix();
    Vector vector = affineTransform.getVector();

    List<Double> matrixElements = new java.util.ArrayList<>(matrix.getAllElements().toList());
    List<Double> vectorElements = new java.util.ArrayList<>(vector.getAllElements().toList());

    for (int i = 0; i < matrix.getSize(); i++) {
      //this.getChildren().add(new Label("Matrix         Vector"));
      this.getChildren().add(new HBox());
    }

    for (int i = 0; i < matrixElements.size(); i++) {
      TextField textField = constructTextFieldFromMatrix(matrixElements, i, matrix);

      HBox box = (HBox) this.getChildren().get(i % (int) Math.sqrt(matrixElements.size()));
      box.getChildren().add(textField);
    }

    for (int i = 0; i < vectorElements.size(); i++) {
      TextField textField = constructTextFieldFromVector(vectorElements, i, vector);

      HBox box = (HBox) this.getChildren().get(i);
      box.getChildren().add(textField);
    }
  }

  /**
   * Constructs a text field for the matrix elements.
   * Adds a listener to the text field to update the matrix elements.
   *
   * @param matrixElements the matrix elements to update
   * @param i the index of the matrix element
   * @param matrix the matrix to update
   * @return the text field for the matrix elements
   * @since 1.1
   */
  protected TextField constructTextFieldFromMatrix(
      List<Double> matrixElements, int i, MatrixNxN matrix
  ) {
    TextField textField = new TextField("" + matrixElements.get(i));
    textField.setStyle("-fx-background-color: #616161");

    // Mnemonic parsing
    Label label = new Label("" + matrixElements.get(i));
    label.setMnemonicParsing(true);
    label.setLabelFor(textField);

    textField.setOnAction(e -> {
      if (textField.getText().replaceAll("[.\\-\\s]", "").isEmpty()) {
        // Regex replaces all dots, dashes, and spaces with nothin
        return;
      }
      matrixElements.set(i, Double.parseDouble(textField.getText()));
      matrix.setElement(i, Double.parseDouble(textField.getText()));

      updateFunction.accept(null);

      // Check if the transformation has any values, remove the input box if it does
      checkValue();
    });

    textField.focusedProperty().addListener((obs, wasFocues, isNowFocused) -> {
      if (textField.getText().replaceAll("[.\\-\\s]", "").isEmpty()) {
        // Regex replaces all dots, dashes, and spaces with nothin
        return;
      }
      matrixElements.set(i, Double.parseDouble(textField.getText()));
      matrix.setElement(i, Double.parseDouble(textField.getText()));

      updateFunction.accept(null);
    });
    return textField;
  }
}
