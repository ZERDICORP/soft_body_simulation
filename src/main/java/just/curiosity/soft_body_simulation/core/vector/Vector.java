package just.curiosity.soft_body_simulation.core.vector;

/**
 * @author zerdicorp
 * @project soft_body_simulation
 * @created 14/06/2022 - 8:31 AM
 */

public class Vector {
  private double x;
  private double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void add(Vector v) {
    x += v.x;
    y += v.y;
  }

  public Vector copy() {
    return new Vector(x, y);
  }

  public void multiply(double n) {
    x *= n;
    y *= n;
  }

  public void subtract(Vector v) {
    x -= v.x;
    y -= v.y;
  }

  public void update(Vector v) {
    x = v.x;
    y = v.y;
  }

  @Override
  public String toString() {
    return "Vector{" +
      "x=" + x +
      ", y=" + y +
      '}';
  }
}
