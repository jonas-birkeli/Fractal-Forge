package backend.utility.state;

import static org.junit.jupiter.api.Assertions.*;

import backend.core.ChaosGameDescription;
import backend.geometry.Vector;
import backend.transforms.Transform;
import backend.utility.state.State.Builder;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateTest {
  State state;

  @BeforeEach
  void setUp() {
    State.Builder builder = new State.Builder();
    builder.chaosGameDescription(null);
    builder.cssPath("");
    builder.viewPath("");

    state = builder.build();
  }

  @AfterEach
  void tearDown() {
    state = null;
  }

  @Test
  void setChaosGameDescriptionPositive() {
    ChaosGameDescription chaosGameDescription = new ChaosGameDescription(null, null, null, null);

    state.setChaosGameDescription(chaosGameDescription);
    assertNotNull(state.getChaosGameDescription(), "The chaos game description should not be null");
  }

  @Test
  void getChaosGameDescriptionNullCheck() {
    assertNull(state.getChaosGameDescription(), "The chaos game description should be null");
  }

  @Test
  void getCssPath() {
    assertEquals("", state.getCssPath(), "The css path should be empty");
  }

  @Test
  void getViewPath() {
    assertEquals("", state.getViewPath(), "The view path should be empty");
  }
}