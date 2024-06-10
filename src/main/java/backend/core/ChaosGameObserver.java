package backend.core;

/**
 * Interface for notifying observers of a chaos game canvas.
 *
 * @version 1.0
 * @author proggang
 * @since 04.04.2024
 */
public interface ChaosGameObserver  {

  /**
   * Sends an update notification to the observer from the chaos game.
   *
   * @since 1.0
   */
  void update();
}
