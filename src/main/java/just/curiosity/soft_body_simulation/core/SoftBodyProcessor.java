package just.curiosity.soft_body_simulation.core;

import java.util.List;
import java.util.function.BiConsumer;
import just.curiosity.soft_body_simulation.core.body.SoftBody;
import just.curiosity.soft_body_simulation.core.body.StaticBody;
import just.curiosity.soft_body_simulation.core.constants.Const;
import just.curiosity.soft_body_simulation.core.particle.Particle;
import just.curiosity.soft_body_simulation.core.vector.Vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:33 AM
 */

public class SoftBodyProcessor {
  private final SoftBody softBody;
  private final List<StaticBody> staticBodies;
  private BiConsumer<SoftBody, List<StaticBody>> onUpdate;
  private BiConsumer<Vector, Vector> onIntersect;

  public SoftBodyProcessor(SoftBody softBody, List<StaticBody> staticBodies) {
    this.softBody = softBody;
    this.staticBodies = staticBodies;
  }

  public void onUpdate(BiConsumer<SoftBody, List<StaticBody>> onUpdate) {
    this.onUpdate = onUpdate;
  }

  public void onIntersect(BiConsumer<Vector, Vector> onIntersect) {
    this.onIntersect = onIntersect;
  }

  public void update(double deltaTime) {
    softBody.particles().parallelStream()
      .forEach(particle -> {
        final Vector acceleration = new Vector(0, Const.ACCELERATION * deltaTime);
        particle.velocity().add(acceleration);

        final Vector locationOffset = applyVelocity(particle, deltaTime);

        detectCollisionsWithStaticBodies(particle, locationOffset, deltaTime);
      });

    if (onUpdate != null) {
      onUpdate.accept(softBody, staticBodies);
    }
  }

  private void detectCollisionsWithStaticBodies(Particle particle, Vector locationOffset, double deltaTime) {
    final Vector previousLocation = particle.location().copy();
    previousLocation.subtract(locationOffset);

    final List<Vector> staticBodyPoints = staticBodies.stream()
      .reduce((b1, b2) -> {
        b2.addPoints(b1.points());
        return b2;
      })
      .orElseThrow()
      .points();

    for (int i = 1; i < staticBodyPoints.size(); i++) {
      final Vector intersection = findIntersection(previousLocation, particle.location(),
        staticBodyPoints.get(i - 1), staticBodyPoints.get(i));

      if (intersection != null) {
        particle.location().update(intersection);

        // Bouncing.
        particle.velocity().multiply(-0.5);
        applyVelocity(particle, deltaTime);

        if (onIntersect != null) {
          onIntersect.accept(previousLocation, intersection);
        }

        break;
      }
    }
  }

  private Vector applyVelocity(Particle particle, double deltaTime) {
    final Vector offset = particle.velocity().copy();
    offset.multiply(0.5 * deltaTime);
    particle.location().add(offset);
    return offset;
  }

  private double determinant(Vector v1, Vector v2, Vector v3) {
    return (v2.x() - v1.x()) * (v3.y() - v1.y()) - (v2.y() - v1.y()) * (v3.x() - v1.x());
  }

  private Vector findIntersection(Vector v1, Vector v2, Vector v3, Vector v4) {
    final double fZ1 = determinant(v1, v2, v3);
    final double fZ2 = determinant(v1, v2, v4);

    if (Math.signum(fZ1) == Math.signum(fZ2))
      return null;

    final double iZ3 = determinant(v3, v4, v1);
    final double iZ4 = determinant(v3, v4, v2);

    if (Math.signum(iZ3) == Math.signum(iZ4))
      return null;

    final double x = v3.x() + (v4.x() - v3.x()) * Math.abs(fZ1) / Math.abs(fZ2 - fZ1);
    final double y = v3.y() + (v4.y() - v3.y()) * Math.abs(fZ1) / Math.abs(fZ2 - fZ1);

    return new Vector(x, y);
  }
}
