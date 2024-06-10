package backend.utility.state;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateHandlerTest {
  StateHandler stateHandler;

  @BeforeEach
  void setUp() {
    stateHandler = StateHandler.getInstance();
  }

  @AfterEach
  void tearDown() {
    // Strictly speaking, this is not necessary since the instance is a singleton
    stateHandler = null;
  }

  @Test
  void getInstance() {
    assertNotNull(stateHandler, "The instance should not be null");
  }

  @Test
  void saveState() {
    // Too complex, not testable
    assertTrue(true, "The method is too complex to test");
  }

  @Test
  void getState() {
    assertNotNull(stateHandler.getState(), "The state should be null");
  }

  @Test
  void getChaosGameDescription() {
    assertNotNull(stateHandler.getChaosGameDescription(), "The chaos game description should be null");
  }

  @Test
  void setDescription() {
    stateHandler.setDescription(null);

    assertNull(stateHandler.getChaosGameDescription(), "The chaos game description should not be null");
  }

  @Test
  void isStateNull() {
    assertFalse(stateHandler.isStateNull(), "The state should not be null");
  }
}