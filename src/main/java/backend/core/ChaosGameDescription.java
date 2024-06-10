package backend.core;

import backend.geometry.Vector;
import backend.transforms.Transform;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.util.Builder;

/**
 * Represents a chaos game description. Supports getting the minimum coordinates, maximum
 * coordinates, and transforms.
 *
 * @author proggang
 * @version 1.2
 * @since 20.02.2024
 */
public final class ChaosGameDescription {
  private final Vector minCoords;
  private final Vector maxCoords;
  private final List<Transform> transforms;
  private Vector probability;

  /**
   * Constructs a new chaos game description with the given minimum coordinates,
   * maximum coordinates, and transformations.
   * The probabilities are set to equal probabilities for each transformation,
   * unless specified otherwise.
   *
   * @param minCoords  the minimum coordinates of the chaos game description
   * @param maxCoords  the maximum coordinates of the chaos game description
   * @param transforms the transformations of the chaos game description
   * @param probability the probabilities of the chaos game description (Optional)
   * @since 1.0
   */
  public ChaosGameDescription(Vector minCoords, Vector maxCoords,
      List<Transform> transforms, Vector probability) {
    this.minCoords = minCoords;
    this.maxCoords = maxCoords;
    this.transforms = transforms;
    this.probability = probability;
  }

  /**
   * Returns the transformations of this chaos game description.
   *
   * @return the transformations of this chaos game description
   * @since 1.0
   */
  public List<Transform> getTransforms() {
    return transforms;
  }

  /**
   * Returns the minimum coordinates of this chaos game description.
   *
   * @return the minimum coordinates of this chaos game description
   * @since 1.0
   */
  public Vector getMinCoords() {
    return minCoords;
  }

  /**
   * Returns the maximum coordinates of this chaos game description.
   *
   * @return the maximum coordinates of this chaos game description
   * @since 1.0
   */
  public Vector getMaxCoords() {
    return maxCoords;
  }

  /**
   * Returns the probabilities of this chaos game description. Probability is an array of integers,
   * where the i-th element represents the probability of the i-th transformation.
   * Probabilities are generated of equal probability for each transformation if not set.
   *
   * @return the probabilities of this chaos game description
   * @since 1.2
   */
  public Vector getProbability() {
    if (probability == null) {
      // Generating equal probabilities for each transformation if not set
      int size = transforms.size();
      double[] newProbability = new double[size];
      int equalProbability = 100 / size;
      int cumulativeProbability = 0;
      for (int i = 0; i < size; i++) {
        cumulativeProbability += equalProbability;
        newProbability[i] = cumulativeProbability;
      }

      return new Vector(newProbability);
    }

    return probability;
  }

  /**
   * Constructor for chaos game descriptions. Needs a builder to create a chaos game description.
   *
   * @since 1.1
   */
  public ChaosGameDescription(ChaosGameDescriptionBuilder builder) {
    this(builder.minCoords, builder.maxCoords, builder.transforms, builder.probability);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (ChaosGameDescription) obj;
    return Objects.equals(this.minCoords, that.minCoords)
        && Objects.equals(this.maxCoords, that.maxCoords)
        && Objects.equals(this.transforms, that.transforms)
        && Objects.equals(this.probability, that.probability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minCoords, maxCoords, transforms, probability);
  }

  /**
   * Removes the transformation at the given index.
   * The index must be within the bounds of the list.
   * The probability of the removed transformation is also removed.
   *
   * @param i the index of the transformation to remove
   * @since 1.2
   */
  public void removeTransform(int i) {
    transforms.remove(i);
    probability = null;  // Reset probabilities
  }

  /**
   * Clears the probabilities of the chaos game description.
   * The probabilities are set to equal probabilities for each transformation.
   *
   * @since 1.2
   */
  public void clearProbabilities() {
    probability = null;
  }


  /**
   * Represents a builder for chaos game descriptions. Supports setting the minimum coordinates,
   * maximum coordinates, and adding transformations.
   *
   * @author proggang
   * @version 1.0
   * @since 11.05.2024
   */
  public static class ChaosGameDescriptionBuilder implements Builder<ChaosGameDescription> {

    private Vector minCoords;
    private Vector maxCoords;
    private final List<Transform> transforms = new ArrayList<>();
    private Vector probability = null;
    // Only adding transformations is supported, so the list needs to be initialized here.

    /**
     * Sets the minimum coordinates of the chaos game description.
     *
     * @param minCoords the minimum coordinates of the chaos game description
     * @return updated builder instance
     * @since 1.0
     */
    public ChaosGameDescriptionBuilder minCoords(Vector minCoords) {
      this.minCoords = minCoords;
      return this;
    }

    /**
     * Sets the maximum coordinates of the chaos game description.
     *
     * @param maxCoords the maximum coordinates of the chaos game description
     * @return updated builder instance
     * @since 1.0
     */
    public ChaosGameDescriptionBuilder maxCoords(Vector maxCoords) {
      this.maxCoords = maxCoords;
      return this;
    }

    /**
     * Adds a transformation to the chaos game description.
     *
     * @param transform the transformation to add
     * @return updated builder instance
     * @since 1.0
     */
    public ChaosGameDescriptionBuilder addTransform(Transform transform) {
      this.transforms.add(transform);
      return this;
    }

    /**
     * Adds probabilities to the chaos game description. Probability is a vector of integers, where
     * the i-th element represents the probability of the i-th transformation.
     * The vector must have the same size as the number of transformations.
     *
     * @param probability the probabilities to set
     * @return updated builder instance
     * @since 1.2
     */
    public ChaosGameDescriptionBuilder probability(Vector probability) {
      this.probability = probability;
      return this;
    }

    /**
     * Builds a chaos game description with the given parameters. The minimum coordinates, maximum
     * coordinates, and at least one transformation are required.
     *
     * @return the built chaos game description
     */
    public ChaosGameDescription build() {
      return new ChaosGameDescription(this);
    }
  }
}
