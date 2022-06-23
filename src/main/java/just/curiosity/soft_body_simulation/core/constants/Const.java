package just.curiosity.soft_body_simulation.core.constants;

import java.awt.Color;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:28 AM
 */

public interface Const {
  int BACKGROUND_COLOR_RGB = 0x000000; // BLACK
  int COLLISION_RAYS = 360;
  double DOUBLE_PI = Math.PI * 2;
  double COLLISION_RAY_STEP = DOUBLE_PI / COLLISION_RAYS;
  double GRAVITY = 300;
  double MASS_POINT_RADIUS = 10;
  double POINT_MASS = 0.1;
  double SPRING_STIFFNESS = 250;
  double DAMPING_FACTOR = 0.8;
  double DELTA_TIME_MOCK = 0.01;
  Color SOFT_BODY_COLOR = new Color(114, 206, 0);
}
