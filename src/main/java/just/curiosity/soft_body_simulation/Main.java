package just.curiosity.soft_body_simulation;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import just.curiosity.soft_body_simulation.core.SoftBody;
import just.curiosity.soft_body_simulation.core.SoftBodyProcessor;
import just.curiosity.soft_body_simulation.core.boundary.Boundary;
import just.curiosity.soft_body_simulation.core.constants.Const;
import just.curiosity.soft_body_simulation.core.vector.Vector;
import just.curiosity.soft_body_simulation.gui.Window;
import just.curiosity.soft_body_simulation.gui.interaction.Keyboard;
import just.curiosity.soft_body_simulation.gui.interaction.Mouse;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public class Main {
  private static final int width;
  private static final int height;
  private static final Keyboard keyboard;
  private static final Mouse mouse;
  private static final int[] pixelBuffer;
  private static final Window window;
  private static final SoftBody softBody;
  private static final SoftBodyProcessor softBodyProcessor;
  private static final List<Boundary> boundaries;
  private static boolean isRunning;

  static {
    width = 1300;
    height = 900;
    keyboard = new Keyboard();
    mouse = new Mouse();
    isRunning = true;

    final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    pixelBuffer = ((DataBufferInt) bufferedImage.getRaster()
      .getDataBuffer())
      .getData();

    window = new Window(bufferedImage, keyboard, mouse);
    softBody = new SoftBody(100, -60, 280, 180, 40);
    boundaries = new ArrayList<>();
    softBodyProcessor = new SoftBodyProcessor(softBody, boundaries);

    initBoundaries();
  }

  private static void initBoundaries() {
    boundaries.add(new Boundary(
      new Vector(0, 280),
      new Vector(140, 280)));

    boundaries.add(new Boundary(
      new Vector(140, 280),
      new Vector(300, 450)));

    boundaries.add(new Boundary(
      new Vector(300, 450),
      new Vector(400, 450)));

    boundaries.add(new Boundary(
      new Vector(400, 450),
      new Vector(500, 550)));

    boundaries.add(new Boundary(
      new Vector(500, 550),
      new Vector(600, 550)));

    boundaries.add(new Boundary(
      new Vector(600, 550),
      new Vector(700, 700)));

    boundaries.add(new Boundary(
      new Vector(700, 700),
      new Vector(900, 850)));

    boundaries.add(new Boundary(
      new Vector(900, 850),
      new Vector(1200, 850)));

    boundaries.add(new Boundary(
      new Vector(1200, 850),
      new Vector(width + Const.MASS_POINT_RADIUS, 500)));
  }

  private static boolean inRange(double x, double y) {
    return (x >= 0 && x < width) && (y >= 0 && y < height);
  }

  private static void drawPixel(double x, double y, int RGB) {
    final int iX = (int) x;
    final int iY = (int) y;
    if (inRange(iX, iY)) {
      pixelBuffer[iY * width + iX] = RGB;
    }
  }

  private static void drawOval(Vector v, int radius, Color color) {
    for (int i = 0; i < radius; i++) {
      final double rays = 4 * (i + 1);
      final double thetaStep = Const.DOUBLE_PI / rays;
      for (int j = 0; j < rays; j++) {
        final double theta = thetaStep * j;
        final double x = v.x() + Math.cos(theta) * i;
        final double y = v.y() + Math.sin(theta) * i;

        drawPixel(x, y, color.getRGB());
      }
    }
  }

  private static void drawLine(Vector start, Vector end, Color color) {
    final double dist = start.distTo(end);
    final Vector unit = end.copy()
      .subtract(start)
      .divide(dist);

    final double theta = unit.theta();
    for (int j = 0; j < Const.MASS_POINT_RADIUS; j++) {
      final double cos = Math.cos(theta) * j;
      final double sin = Math.sin(theta) * j;

      double x = start.x();
      double y = start.y();
      for (int i = 0; i < dist; i++, x += unit.x(), y += unit.y()) {
        drawPixel(x, y, color.getRGB());

        drawPixel(x + sin, y - cos, color.getRGB());
        drawPixel(x - sin, y + cos, color.getRGB());

        if (i == 0 || i == dist - 1) {
          drawOval(new Vector(x, y), (int) Const.MASS_POINT_RADIUS, Color.WHITE);
        }
      }
    }
  }

  private static void interactionControl() {
    // Pressing escape will stop the application.
    if (keyboard.getCurrentKeyCode() == KeyEvent.VK_ESCAPE) {
      stop();
    }
  }

  // This method is called on every frame.
  private static void updateAndDraw(double deltaTime) {
    interactionControl();

    Arrays.fill(pixelBuffer, Const.BACKGROUND_COLOR_RGB);

    // Update soft body state.
    softBodyProcessor.update(deltaTime);

    // Drawing points, or rather connect them with lines.
    softBody.springs().parallelStream()
      .forEach(spring -> {
        drawLine(spring.second().location(), spring.first().location(), Const.SOFT_BODY_COLOR);
        drawOval(spring.second().location(), (int) Const.MASS_POINT_RADIUS, Const.SOFT_BODY_COLOR);
        drawOval(spring.first().location(), (int) Const.MASS_POINT_RADIUS, Const.SOFT_BODY_COLOR);
      });

    // Drawing boundaries.
    boundaries.parallelStream()
      .forEach(boundary -> drawLine(boundary.start(), boundary.end(), Color.WHITE));

    window.draw();
  }

  // Main loop.
  public static void start() {
    int frames = 0;
    long start = System.currentTimeMillis();

    while (isRunning) {
      updateAndDraw(Const.DELTA_TIME_MOCK);

      // Timing for FPS display.
      long end = System.currentTimeMillis();
      if (end - start >= 1000) {
        System.out.println("FPS: " + frames);
        frames = 0;
        start = end;
      }
      frames++;
    }

    window.dispose();
  }

  private static void stop() {
    isRunning = false;
  }

  public static void main(String[] args) {
    start();
  }
}
