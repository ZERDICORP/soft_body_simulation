package just.curiosity.soft_body_simulation.core.body;

import java.util.ArrayList;
import java.util.List;
import just.curiosity.soft_body_simulation.core.particle.Particle;
import just.curiosity.soft_body_simulation.core.spring.Spring;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public class SoftBody {
  private final List<Particle> particles;
  private final List<Spring> springs;

  {
    particles = new ArrayList<>();
    springs = new ArrayList<>();
  }

  public SoftBody(double x, double y, int width, int height, int particleGap) {
    final int cols = width / particleGap;
    final int rows = height / particleGap;

    final double widthStep = (double) width / cols;
    final double heightStep = (double) height / rows;

    for (int j = 0; j < rows; j++) {
      for (int i = 0; i < cols; i++) {
        particles.add(
          new Particle(new Vector(x + i * widthStep, y + j * heightStep)));

        if (j > 0 && i > 0) {
          springs.add(new Spring(
            particles.get(j * cols + (i - 1)),
            particles.get((j - 1) * cols + i)));

          springs.add(new Spring(
            particles.get(j * cols + i),
            particles.get((j - 1) * cols + (i - 1))));
        }
      }
    }
  }

  public List<Particle> particles() {
    return particles;
  }

  public List<Spring> springs() {
    return springs;
  }
}
