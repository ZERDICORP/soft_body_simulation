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

  public Vector copy() {
    return new Vector(x, y);
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public Vector update(Vector v) {
    x = v.x;
    y = v.y;
    return this;
  }

  public Vector add(Vector v) {
    x += v.x;
    y += v.y;
    return this;
  }

  public Vector subtract(Vector v) {
    x -= v.x;
    y -= v.y;
    return this;
  }

  public Vector multiply(Vector v) {
    x *= v.x();
    y *= v.y();
    return this;
  }

  public Vector divide(double n) {
    x /= n;
    y /= n;
    return this;
  }

  public Vector multiply(double n) {
    x *= n;
    y *= n;
    return this;
  }

  public Vector addX(double n) {
    x += n;
    return this;
  }

  public Vector addY(double n) {
    y += n;
    return this;
  }

  public Vector normalize() {
    double magnitude = magnitude();
    if (magnitude > 0) {
      x /= magnitude;
      y /= magnitude;
    }
    return this;
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  public double dotProduct(Vector v) {
    return x * v.x() + y * v.y();
  }

  public double theta() {
    return Math.atan2(-y, -x);
  }

  public double distTo(Vector v) {
    final double diffX = v.x() - x;
    final double diffY = v.y() - y;
    return Math.sqrt((diffX * diffX) + (diffY * diffY));
  }

  @Override
  public String toString() {
    return "Vector{" +
      "x=" + x +
      ", y=" + y +
      '}';
  }
}
