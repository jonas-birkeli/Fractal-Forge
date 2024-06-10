package frontend.inputboxes;

import backend.transforms.JuliaTransform;
import backend.transforms.Transform;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Represents a Julia input VBox.
 * Used to represent the values of a Julia transform.
 *
 * @version 1.0
 * @author jonas
 * @since 15.05.2024
 */
public class JuliaInputBox extends AbstractInputBox {

  /**
   * Constructs a new input box for julia transforms.
   * Consist of two values.
   *
   * @param transform The transform that needs to be displayed
   * @param updateFunction The update function to call when a value changes.
   * @param removeFunction The remove function to call when the remove button is clicked.
   * @since 1.0
   */
  public JuliaInputBox(
      Transform transform, Consumer<Void> updateFunction, Consumer<Transform> removeFunction
  ) {
    super(transform, updateFunction, removeFunction);
    construct();
  }

  @Override
  public void construct() {
    JuliaTransform julia = (JuliaTransform) transform;
    List<Double> pointElements = new java.util.ArrayList<>(
        julia.getPoint().getAllElements().toList()
    );

    HBox hbox = new HBox();  // We want to display the content horizontally.

    for (int i = 0; i < 2; i++) {
      TextField textField = constructTextFieldFromVector(pointElements, i, julia.getPoint());
      hbox.getChildren().add(textField);
    }
    this.getChildren().add(hbox);
  }
}
