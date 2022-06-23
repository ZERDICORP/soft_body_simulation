package just.curiosity.soft_body_simulation.core.particle;

import java.util.Objects;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:44 AM
 */

public class Particle {
  public static int staticGlobalId;
  public final int id;
  private final Vector location;
  private final Vector velocity;
  private final Vector force;

  {
    velocity = new Vector(0, 0);
    force = new Vector(0, 0);

    // Each particle needs an identifier so that when calculating
    // the collision, we do not check if the particle collides
    // with itself.
    id = ++staticGlobalId;
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

  public Vector force() {
    return force;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return ((Particle) o).id == id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
