package just.curiosity.soft_body_simulation.core;

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

  public SoftBody(double x, double y, int width, int height, int springRestLength) {
    final int cols = width / springRestLength;
    final int rows = height / springRestLength;
    final double squaredLength = Math.pow(springRestLength, 2);
    final double diagonalSpringRestLength = Math.sqrt(squaredLength + squaredLength);

    // We generate particles and springs connecting them. The final
    // body is limited by the shape of a rectangle/square.
    for (int j = 0; j < rows; j++) {
      for (int i = 0; i < cols; i++) {
        particles.add(
          new Particle(new Vector(x + i * springRestLength, y + j * springRestLength)));

        if (j == 0 && i > 0) {
          springs.add(new Spring(
            particles.get(i - 1),
            particles.get(i),
            springRestLength));
        }

        if (i == 0 && j > 0) {
          springs.add(new Spring(
            particles.get((j - 1) * cols),
            particles.get(j * cols),
            springRestLength));
        }

        if (j > 0 && i > 0) {
          springs.add(new Spring(
            particles.get(j * cols + (i - 1)),
            particles.get((j - 1) * cols + i),
            diagonalSpringRestLength));

          springs.add(new Spring(
            particles.get(j * cols + i),
            particles.get((j - 1) * cols + (i - 1)),
            diagonalSpringRestLength));

          springs.add(new Spring(
            particles.get(j * cols + i),
            particles.get(j * cols + (i - 1)),
            springRestLength));

          springs.add(new Spring(
            particles.get(j * cols + i),
            particles.get((j - 1) * cols + i),
            springRestLength));
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
