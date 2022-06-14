package just.curiosity.soft_body_simulation;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import just.curiosity.soft_body_simulation.core.SoftBodyProcessor;
import just.curiosity.soft_body_simulation.core.body.SoftBody;
import just.curiosity.soft_body_simulation.core.body.StaticBody;
import just.curiosity.soft_body_simulation.core.constants.Const;
import just.curiosity.soft_body_simulation.core.spring.Spring;
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
  private static final SoftBodyProcessor softBodyProcessor;
  private static boolean isRunning;

  static {
    width = 1000;
    height = 700;
    keyboard = new Keyboard();
    mouse = new Mouse();
    isRunning = true;

    final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    pixelBuffer = ((DataBufferInt) bufferedImage.getRaster()
      .getDataBuffer())
      .getData();

    window = new Window(bufferedImage, keyboard, mouse);

    // Creating soft body.
    final double softBodyWidth = 150;
    final double softBodyHeight = 150;
    final SoftBody softBody = new SoftBody(
      width / 2d - softBodyWidth / 2, 0,
      (int) softBodyWidth, (int) softBodyHeight,
      25);

    final List<StaticBody> staticBodies = new ArrayList<>();
    createStaticBodies(staticBodies);

    softBodyProcessor = new SoftBodyProcessor(softBody, staticBodies);
    softBodyProcessor.onUpdate(Main::renderSoftBody);
  }

  private static void createStaticBodies(List<StaticBody> staticBodies) {
    // Creating base bottom platform.
    final double platformWidth = 400;
    final double platformHeight = 50;
    final double platformLeftTopX = width / 2d - platformWidth / 2;
    final double platformLeftTopY = height - platformHeight;

    staticBodies.add(new StaticBody(List.of(
      new Vector(platformLeftTopX, platformLeftTopY),
      new Vector(platformLeftTopX + platformWidth, platformLeftTopY),
      new Vector(platformLeftTopX + platformWidth, platformLeftTopY + platformHeight),
      new Vector(platformLeftTopX, platformLeftTopY + platformHeight))));
  }

  // This method is responsible for tracking interactions (keystrokes,
  // mouse click/movement).
  private static void interactionControl() {
    // Pressing escape will stop the application.
    if (keyboard.getCurrentKeyCode() == KeyEvent.VK_ESCAPE) {
      stop();
    }
  }

  private static boolean inRange(double x, double y) {
    return (x >= 0 && x < width) && (y >= 0 && y < height);
  }

  private static double dist(Vector v1, Vector v2) {
    final double diffX = v2.x() - v1.x();
    final double diffY = v2.y() - v1.y();
    return Math.sqrt((diffX * diffX) + (diffY * diffY));
  }

  private static void drawLine(Vector v1, Vector v2, Color color) {
    final double dist = dist(v1, v2);
    final double xStep = (v2.x() - v1.x()) / dist;
    final double yStep = (v2.y() - v1.y()) / dist;

    double x = v1.x();
    double y = v1.y();

    for (int i = 0; i < dist; i++, x += xStep, y += yStep) {
      final int iX = (int) x;
      final int iY = (int) y;
      if (inRange(iX, iY)) {
        pixelBuffer[iY * width + iX] = color.getRGB();
      }
    }
  }

  private static void renderSoftBody(SoftBody softBody, List<StaticBody> staticBodies) {
    final List<Spring> springs = softBody.springs();
    springs.parallelStream()
      .forEach(s -> drawLine(
        s.first().location(),
        s.second().location(),
        Color.RED));

    staticBodies.parallelStream()
      .forEach(staticBody -> {
        final List<Vector> points = staticBody.points();
        for (int i = 1; i < points.size(); i++) {
          drawLine(points.get(i - 1), points.get(i), Color.WHITE);
          if (i == points.size() - 1) {
            drawLine(points.get(i), points.get(0), Color.WHITE);
          }
        }
      });
  }

  // This method is called on every frame.
  private static void updateAndDraw(double deltaTime) {
    interactionControl();

    Arrays.fill(pixelBuffer, Const.BACKGROUND_COLOR_RGB);

    softBodyProcessor.update(deltaTime);

    window.draw();
  }

  // Main loop.
  public static void start() {
    int frames = 0;
    long start = System.currentTimeMillis();
    long time0 = System.nanoTime();
    double time;
    double lastTime = 0;
    double deltaTime;

    while (isRunning) {
      time = (System.nanoTime() - time0) / 1E9;
      deltaTime = time - lastTime;
      lastTime = time;

      updateAndDraw(deltaTime);

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
