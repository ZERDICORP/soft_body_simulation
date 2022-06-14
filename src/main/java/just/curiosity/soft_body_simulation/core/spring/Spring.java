package just.curiosity.soft_body_simulation.core.spring;

import just.curiosity.soft_body_simulation.core.particle.Particle;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:52 AM
 */

public class Spring {
  private final Particle first;
  private final Particle second;

  public Spring(Particle first, Particle second) {
    this.first = first;
    this.second = second;
  }

  public Particle first() {
    return first;
  }

  public Particle second() {
    return second;
  }
}
