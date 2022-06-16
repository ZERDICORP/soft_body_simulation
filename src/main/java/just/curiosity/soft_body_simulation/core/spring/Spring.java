package just.curiosity.soft_body_simulation.core.spring;

import just.curiosity.soft_body_simulation.core.mass_point.MassPoint;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:52 AM
 */

public class Spring {
  private final MassPoint first;
  private final MassPoint second;

  public Spring(MassPoint first, MassPoint second) {
    this.first = first;
    this.second = second;
  }

  public MassPoint first() {
    return first;
  }

  public MassPoint second() {
    return second;
  }
}
