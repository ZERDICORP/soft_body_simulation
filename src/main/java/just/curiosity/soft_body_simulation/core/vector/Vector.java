package just.curiosity.soft_body_simulation.core.vector;

import java.util.Objects;

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

  public void divide(double n) {
    x /= n;
    y /= n;
  }

  public void update(Vector v) {
    x = v.x;
    y = v.y;
  }

  public double theta() {
    return Math.atan2(-y, -x);
  }

  public void normalize() {
    double magnitude = magnitude();
    if (magnitude > 0) {
      x /= magnitude;
      y /= magnitude;
    }
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  public double dotProduct(Vector v) {
    return x * v.x() + y * v.y();
  }

  @Override
  public String toString() {
    return "Vector{" +
      "x=" + x +
      ", y=" + y +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vector vector = (Vector) o;
    return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
