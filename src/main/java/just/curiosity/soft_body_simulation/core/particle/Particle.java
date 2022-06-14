package just.curiosity.soft_body_simulation.core.particle;

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
}
