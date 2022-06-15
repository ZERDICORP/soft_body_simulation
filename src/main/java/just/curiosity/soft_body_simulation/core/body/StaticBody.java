package just.curiosity.soft_body_simulation.core.body;

import java.util.List;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:38 AM
 */

public class StaticBody {
  private final List<Vector> points;

  public StaticBody(List<Vector> points) {
    this.points = points;
  }

  public List<Vector> points() {
    return points;
  }

  public void addPoints(List<Vector> points) {
    this.points.addAll(points);
  }
}
