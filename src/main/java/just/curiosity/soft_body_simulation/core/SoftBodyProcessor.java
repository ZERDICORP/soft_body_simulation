package just.curiosity.soft_body_simulation.core;

import java.util.List;
import just.curiosity.soft_body_simulation.core.boundary.Boundary;
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
  private final List<Boundary> boundaries;

  public SoftBodyProcessor(SoftBody softBody, List<Boundary> boundaries) {
    this.softBody = softBody;
    this.boundaries = boundaries;
  }

  public void update(double deltaTime) {
    // Spring mass system realization.
    softBody.springs()
      .forEach(spring -> {
        final Particle first = spring.first();
        final Particle second = spring.second();

        final Vector normalVector = second.location().copy()
          .subtract(first.location())
          .normalize();

        final Vector velocityDifference = second.velocity().copy()
          .subtract(first.velocity());

        final double dist = first.location().distTo(second.location());
        final double force1 = (dist - spring.restLength()) * Const.SPRING_STIFFNESS;
        final double force2 = normalVector.dotProduct(velocityDifference) * Const.DAMPING_FACTOR;
        final double finalForce = force1 + force2;

        // Moving the first particle.
        first.force()
          .add(normalVector
            .multiply(finalForce));

        // Moving the second particle.
        second.force()
          .add(first.location().copy()
            .subtract(second.location())
            .normalize()
            .multiply(finalForce));
      });

    // For each particle, we apply a number of forces (gravity,
    // reflection of velocity, etc).
    softBody.particles()
      .forEach(particle -> {
        gravity(particle, deltaTime);
        selfCollision(particle, deltaTime);
        boundariesCollision(particle, deltaTime);
      });
  }

  private void applyVelocity(Particle particle, double deltaTime) {
    particle.location().add(particle.velocity().copy()
      .multiply(0.5 * deltaTime));
  }

  private void applyForce(Particle particle, double deltaTime) {
    particle.velocity().add(particle.force()
      .multiply(deltaTime)
      .divide(Const.POINT_MASS));

    applyVelocity(particle, deltaTime);
    // Reset force for the next iteration.
    particle.force().multiply(0);
  }

  private void gravity(Particle particle, double deltaTime) {
    final Vector gravity = new Vector(0, Const.GRAVITY * Const.POINT_MASS);
    particle.force().add(gravity);
    applyForce(particle, deltaTime);
  }

  private void selfCollision(Particle particle, double deltaTime) {
    softBody.particles().stream()
      // We need all particles, except for the one for which we
      // will determine the collision (I think it's obvious why).
      .filter(target -> !target.equals(particle))
      // For each particle, it is necessary to determine the
      // collision with the current particle, and in which case,
      // reflect the velocities of both particles, and then move
      // them to the intersection.
      .forEach(target -> {
        final double dist = particle.location().distTo(target.location());
        final double diff = dist - Const.MASS_POINT_RADIUS * 2;
        // If the distance between the particles is greater than
        // the diameter, there is no collision. Let's skip it.
        if (diff > 0) {
          return;
        }

        final Vector locationOffset = particle.location().copy()
          .subtract(target.location())
          .multiply(diff * 0.5)
          .divide(dist);

        final Vector tangentVector = target.location().copy()
          .subtract(particle.location())
          .divide(dist)
          .multiply(new Vector(0, -1));

        // Reflection of particle velocities.
        particle.velocity().update(tangentVector.copy()
          .multiply(tangentVector.dotProduct(particle.velocity())));
        target.velocity().update(tangentVector.copy()
          .multiply(tangentVector.dotProduct(target.velocity())));

        // Displacement of particles to the intersection.
        particle.location().subtract(locationOffset);
        target.location().add(locationOffset);

        // Apply particle velocities.
        applyVelocity(particle, deltaTime);
        applyVelocity(target, deltaTime);
      });
  }

  private void boundariesCollision(Particle particle, double deltaTime) {
    if (boundaries.size() == 0) {
      return;
    }

    Vector nearestIntersection = null;
    double minDist = Const.MASS_POINT_RADIUS;
    for (int j = 0; j < Const.COLLISION_RAYS; j++) {
      final double theta = Const.COLLISION_RAY_STEP * j;
      final Vector rayEnd = particle.location().copy()
        .addX(Math.cos(theta) * Const.MASS_POINT_RADIUS)
        .addY(Math.sin(theta) * Const.MASS_POINT_RADIUS);

      // For each boundary, it is necessary to find the intersection
      // with the ray. As a result, the nearest intersection will be
      // taken as the basis for calculations.
      for (Boundary boundary : boundaries) {
        final Vector intersection = findIntersection(particle.location(), rayEnd, boundary.start(), boundary.end());
        if (intersection == null) {
          continue;
        }

        final double dist = particle.location().distTo(intersection);
        if (dist < minDist) {
          minDist = dist;
          nearestIntersection = intersection;
        }
      }
    }

    if (nearestIntersection == null) {
      return;
    }

    final Vector pushVector = particle.location().copy()
      .subtract(nearestIntersection)
      .normalize()
      .multiply(minDist - Const.MASS_POINT_RADIUS);

    final Vector normalVector = pushVector.copy()
      .normalize();

    // Move the particle to the intersection.
    particle.location().add(pushVector
      .multiply(-1));

    // Velocity reflection.
    particle.velocity()
      .subtract(normalVector
        .multiply(particle.velocity().dotProduct(normalVector) * 2));

    applyVelocity(particle, deltaTime);
  }

  // Function for finding the intersection of two segments.
  private Vector findIntersection(Vector v1, Vector v2, Vector v3, Vector v4) {
    final double f1 = determinant(v1, v2, v3);
    final double f2 = determinant(v1, v2, v4);

    if (Math.signum(f1) == Math.signum(f2))
      return null;

    final double f3 = determinant(v3, v4, v1);
    final double f4 = determinant(v3, v4, v2);

    if (Math.signum(f3) == Math.signum(f4))
      return null;

    final double x = v3.x() + (v4.x() - v3.x()) * Math.abs(f1) / Math.abs(f2 - f1);
    final double y = v3.y() + (v4.y() - v3.y()) * Math.abs(f1) / Math.abs(f2 - f1);

    return new Vector(x, y);
  }

  private double determinant(Vector v1, Vector v2, Vector v3) {
    return (v2.x() - v1.x()) * (v3.y() - v1.y()) - (v2.y() - v1.y()) * (v3.x() - v1.x());
  }
}
