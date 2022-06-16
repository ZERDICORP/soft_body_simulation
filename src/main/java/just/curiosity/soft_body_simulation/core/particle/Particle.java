package just.curiosity.soft_body_simulation.core.particle;

import java.util.Objects;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:44 AM
 */

public class Particle {
  private final Vector location;
  private final Vector velocity;

  {
    velocity = new Vector(0, 0);
  }

  public Particle(Vector location) {
    this.location = location;
  }

  public Vector location() {
    return location;
  }

  public Vector velocity() {
    return velocity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Particle particle = (Particle) o;
    return Objects.equals(location, particle.location) && Objects.equals(velocity, particle.velocity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, velocity);
  }
}
