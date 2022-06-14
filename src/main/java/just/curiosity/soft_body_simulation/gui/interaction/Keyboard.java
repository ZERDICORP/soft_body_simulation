package just.curiosity.soft_body_simulation.gui.interaction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public final class Keyboard implements KeyListener {
  private static int keyCode;

  public int getCurrentKeyCode() {
    return keyCode;
  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    keyCode = keyEvent.getKeyCode();
  }

  @Override
  public void keyReleased(KeyEvent keyEvent) {
    keyCode = -1;
  }

  @Override
  public void keyTyped(KeyEvent keyEvent) {
  }
}
