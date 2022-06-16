package just.curiosity.soft_body_simulation.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import just.curiosity.soft_body_simulation.Main;
import just.curiosity.soft_body_simulation.core.body.SoftBody;
import just.curiosity.soft_body_simulation.core.body.StaticBody;
import just.curiosity.soft_body_simulation.core.constants.Const;
import just.curiosity.soft_body_simulation.core.mass_point.MassPoint;
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
    // Apply gravity force for each point.
    softBody.massPoints().parallelStream()
      .forEach(massPoint -> {
        final Vector gravity = new Vector(0, Const.GRAVITY);
        gravity.multiply(Const.POINT_MASS);

        massPoint.force().add(gravity);
      });

    // Apply spring force for each point.
    softBody.springs().parallelStream()
      .forEach(spring -> {
        final double dist = Main.dist(spring.first().location(), spring.second().location());
        final double force1 = (dist - softBody.restLength()) * Const.SPRING_STIFFNESS;

        final Vector difference1 = spring.second().location().copy();
        difference1.subtract(spring.first().location());
        difference1.normalize();

        final Vector direction = spring.second().velocity().copy();
        direction.subtract(spring.first().velocity());

        final double dotProduct = difference1.dotProduct(direction);

        final double force2 = dotProduct * Const.DAMPING_FACTOR;
        final double force3 = force1 + force2;

        // Move first mass point.
        difference1.multiply(force3);

        spring.first().force().add(difference1);

        // Move second mass point.
        final Vector difference2 = spring.first().location().copy();
        difference2.subtract(spring.second().location());
        difference2.normalize();
        difference2.multiply(force3);

        spring.second().force().add(difference2);
      });

    // Apply final force for each point.
    softBody.massPoints().parallelStream()
      .forEach(massPoint -> {
        massPoint.force().multiply(deltaTime);
        massPoint.force().divide(Const.POINT_MASS);

        massPoint.velocity().add(massPoint.force());

        applyVelocity(massPoint, deltaTime);

        massPoint.force().multiply(0);
      });

    // Apply collision reflect force for each point.
    softBody.massPoints().parallelStream()
      .forEach(massPoint -> {
        final Vector offset = massPoint.velocity().copy();
        offset.multiply(0.5 * deltaTime);

        detectCollisionsWithStaticBodies(massPoint, offset);
      });

    if (onUpdate != null) {
      onUpdate.accept(softBody, staticBodies);
    }
  }

  private void applyVelocity(MassPoint massPoint, double deltaTime) {
    final Vector offset = massPoint.velocity().copy();
    offset.multiply(0.5 * deltaTime);
    massPoint.location().add(offset);
  }

  private void detectCollisionsWithStaticBodies(MassPoint massPoint, Vector locationOffset) {
    if (staticBodies.size() == 0) {
      return;
    }

    final Vector previousLocation = massPoint.location().copy();
    previousLocation.subtract(locationOffset);

    final List<Vector> staticBodyPoints = new ArrayList<>();
    for (StaticBody staticBody : staticBodies) {
      staticBodyPoints.addAll(staticBody.points());
    }

    // TODO: implement Ray Casting algorithm for each mass point
    for (int i = 1; i < staticBodyPoints.size(); i++) {
      final Vector intersection = findIntersection(previousLocation, massPoint.location(),
        staticBodyPoints.get(i - 1), staticBodyPoints.get(i));

      if (intersection != null) {
        massPoint.location().update(intersection);

        // TODO: velocity vector reflection

        if (onIntersect != null) {
          onIntersect.accept(previousLocation, intersection);
        }

        break;
      }
    }
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
