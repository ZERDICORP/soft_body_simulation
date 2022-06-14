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

  public SoftBodyProcessor(SoftBody softBody, List<StaticBody> staticBodies) {
    this.softBody = softBody;
    this.staticBodies = staticBodies;
  }

  public void onUpdate(BiConsumer<SoftBody, List<StaticBody>> onUpdate) {
    this.onUpdate = onUpdate;
  }

  public void update(double deltaTime) {
    softBody.particles().parallelStream()
      .forEach(particle -> {
        final Vector location = particle.location();
        final Vector velocity = particle.velocity();

        final Vector gravityForce = new Vector(0, Const.ACCELERATION * deltaTime);
        velocity.add(gravityForce);

        final Vector locationOffset = velocity.copy();
        locationOffset.multiply(0.5 * deltaTime);
        location.add(locationOffset);

        detectCollisions(particle);
      });

    onUpdate.accept(softBody, staticBodies);
  }

  private void detectCollisions(Particle particle) {
    // prev = particle.location - particle.velocity
    // p = intersection(vector(prev, particle.location), STATIC_BODIES_BORDERS)
    // if p != null: stop particle
  }
}
