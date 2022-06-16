package just.curiosity.soft_body_simulation.core.mass_point;

import java.util.Objects;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:44 AM
 */

public class MassPoint {
  private final Vector location;
  private final Vector velocity;
  private final Vector force;

  {
    velocity = new Vector(0, 0);
    force = new Vector(0, 0);
  }

  public MassPoint(Vector location) {
    this.location = location;
  }

  public Vector location() {
    return location;
  }

  public Vector velocity() {
    return velocity;
  }

  public Vector force() {
    return force;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MassPoint particle = (MassPoint) o;
    return Objects.equals(location, particle.location) && Objects.equals(velocity, particle.velocity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, velocity);
  }
}
