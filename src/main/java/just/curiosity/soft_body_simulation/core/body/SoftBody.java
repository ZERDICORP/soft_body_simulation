package just.curiosity.soft_body_simulation.core.body;

import java.util.ArrayList;
import java.util.List;
import just.curiosity.soft_body_simulation.core.mass_point.MassPoint;
import just.curiosity.soft_body_simulation.core.spring.Spring;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public class SoftBody {
  private final List<MassPoint> massPoints;
  private final List<Spring> springs;
  private final double restLength;

  {
    massPoints = new ArrayList<>();
    springs = new ArrayList<>();
  }

  public SoftBody(double x, double y, int width, int height, int particleGap) {
    final int cols = width / particleGap;
    final int rows = height / particleGap;

    final double widthStep = (double) width / cols;
    final double heightStep = (double) height / rows;

    restLength = Math.sqrt(widthStep * widthStep + heightStep * heightStep);

    for (int j = 0; j < rows; j++) {
      for (int i = 0; i < cols; i++) {
        massPoints.add(
          new MassPoint(new Vector(x + i * widthStep, y + j * heightStep)));

        if (j == 0 && i > 0) {
          springs.add(new Spring(
            massPoints.get(i - 1),
            massPoints.get(i)));
        }

        if (i == 0 && j > 0) {
          springs.add(new Spring(
            massPoints.get((j - 1) * cols),
            massPoints.get(j * cols)));
        }

        if (j > 0 && i > 0) {
          springs.add(new Spring(
            massPoints.get(j * cols + (i - 1)),
            massPoints.get((j - 1) * cols + i)));

          springs.add(new Spring(
            massPoints.get(j * cols + i),
            massPoints.get((j - 1) * cols + (i - 1))));

          springs.add(new Spring(
            massPoints.get(j * cols + i),
            massPoints.get(j * cols + (i - 1))));

          springs.add(new Spring(
            massPoints.get(j * cols + i),
            massPoints.get((j - 1) * cols + i)));
        }
      }
    }
  }

  public List<MassPoint> massPoints() {
    return massPoints;
  }

  public List<Spring> springs() {
    return springs;
  }

  public double restLength() {
    return restLength;
  }
}
