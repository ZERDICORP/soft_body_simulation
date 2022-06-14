package just.curiosity.soft_body_simulation.gui;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import just.curiosity.soft_body_simulation.gui.interaction.Keyboard;
import just.curiosity.soft_body_simulation.gui.interaction.Mouse;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public final class Window extends JFrame {
  private final BufferedImage bufferedImage;

  public Window(BufferedImage bufferedImage, Keyboard keyboard, Mouse mouse) {
    this.bufferedImage = bufferedImage;

    addMouseListener(mouse);
    addMouseMotionListener(mouse);
    addMouseWheelListener(mouse);
    addKeyListener(keyboard);

    setTitle("Soft Body Simulation");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
    setLocationRelativeTo(null);
    setResizable(false);
    setFocusable(true);
    setVisible(true);
  }

  public void draw() {
    BufferStrategy bufferStrategy = this.getBufferStrategy();
    if (bufferStrategy == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics graphics = bufferStrategy.getDrawGraphics();

    graphics.drawImage(bufferedImage, 0, 0, null);
    bufferStrategy.show();
    graphics.dispose();
  }
}
