package backend.utility.state;

import backend.core.ChaosGameDescription;

/**
 * Class that represents the state of the application.
 * The state consists of the current chaos game description and the path to the css file.
 * Builder pattern is used to create the state.
 *
 * @version 1.1
 * @author proggang
 * @since 04.05.2024
 */
public class State {
  private ChaosGameDescription chaosGameDescription;
  private final String cssPath;
  private final String viewPath;

  /**
   * Constructor for the state.
   *
   * @param builder the builder
   * @since 1.0
   */
  private State(Builder builder) {
    this.chaosGameDescription = builder.chaosGameDescription;
    this.cssPath = builder.cssPath;
    this.viewPath = builder.viewPath;
  }

  public void setChaosGameDescription(ChaosGameDescription description) {
    this.chaosGameDescription = description;
  }

  /**
   * Builder class for the state.
   * Used to create a state with the desired chaos game description and css path.
   *
   * @version 1.0
   * @author proggang
   * @since 04.05.2024
   */
  public static class Builder {
    private ChaosGameDescription chaosGameDescription = null;
    private String cssPath = "";
    private String viewPath = "";

    /**
     * Sets the chaos game description.
     *
     * @param chaosGameDescription the chaos game description
     * @return the builder
     * @since 1.0
     */
    public Builder chaosGameDescription(ChaosGameDescription chaosGameDescription) {
      this.chaosGameDescription = chaosGameDescription;
      return this;
    }

    /**
     * Sets the path to the css file.
     *
     * @param cssPath the path to the css file
     * @return the builder
     * @since 1.0
     */
    public Builder cssPath(String cssPath) {
      this.cssPath = cssPath;
      return this;
    }

    /**
     * Sets the path to the view file.
     *
     * @param viewPath the path to the view file
     * @return the builder
     * @since 1.1
     */
    public Builder viewPath(String viewPath) {
      this.viewPath = viewPath;
      return this;
    }

    /**
     * Builds the state.
     *
     * @return the state
     * @since 1.0
     */
    public State build() {
      return new State(this);
    }
  }

  /**
   * Returns the chaos game description.
   *
   * @return the chaos game description
   * @since 1.0
   */
  public ChaosGameDescription getChaosGameDescription() {
    return chaosGameDescription;
  }

  /**
   * Returns the path to the css file.
   *
   * @return the path to the css file
   * @since 1.0
   */
  public String getCssPath() {
    return cssPath;
  }

  /**
   * Returns the path to the view file.
   *
   * @return the path to the view file
   * @since 1.1
   */
  public String getViewPath() {
    return viewPath;
  }
}
