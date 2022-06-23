package just.curiosity.soft_body_simulation.core.spring;

import just.curiosity.soft_body_simulation.core.particle.Particle;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:52 AM
 */

public record Spring(Particle first, Particle second, double restLength) {
}
