/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo;

import java.util.Objects;
import javax.measure.quantity.Length;
import tech.units.indriya.ComparableQuantity;

/**
 * Wraps two coordinates with the distance between the first one and the second one, can be compared
 * by distance to another CoordinateDistance
 */
public class CoordinateDistance implements Comparable<CoordinateDistance> {
  private final org.locationtech.jts.geom.Point coordinateA;
  private final org.locationtech.jts.geom.Point coordinateB;
  private final ComparableQuantity<Length> distance;

  /**
   * Calculates the distance from the first to the second coordinate using {@link
   * GeoUtils#calcHaversine(double, double, double, double)}
   *
   * @param pointA The first coordinate
   * @param pointB The second coordinate
   */
  public CoordinateDistance(
      org.locationtech.jts.geom.Point pointA, org.locationtech.jts.geom.Point pointB) {
    this(pointA, pointB, GeoUtils.calcHaversine(pointA.getCoordinate(), pointB.getCoordinate()));
  }

  /**
   * @param coordinateA The first coordinate
   * @param coordinateB The second coordinate
   * @param distance The distance from A to B
   */
  private CoordinateDistance(
      org.locationtech.jts.geom.Point coordinateA,
      org.locationtech.jts.geom.Point coordinateB,
      ComparableQuantity<Length> distance) {
    this.coordinateA = coordinateA;
    this.coordinateB = coordinateB;
    this.distance = distance;
  }

  /** Returns the first coordinate. */
  public org.locationtech.jts.geom.Point getCoordinateA() {
    return coordinateA;
  }

  /** Returns the second coordinate. */
  public org.locationtech.jts.geom.Point getCoordinateB() {
    return coordinateB;
  }

  /** Returns the distance from the first coordinate to the second coordinate in km. */
  public ComparableQuantity<Length> getDistance() {
    return distance;
  }

  /**
   * Compares two coordinate distances on the length of the distance alone, thus having a natural
   * ordering that is inconsistent with equals
   *
   * @param that the distance to compare
   * @return a number lower than 0 if this has a lower distance than that, 0 if they are the same, a
   *     number higher than 0 if that has a lower distance
   */
  @Override
  public int compareTo(CoordinateDistance that) {
    return this.distance.compareTo(that.distance);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CoordinateDistance)) return false;
    CoordinateDistance that = (CoordinateDistance) o;
    return coordinateA.equals(that.coordinateA)
        && coordinateB.equals(that.coordinateB)
        && distance.equals(that.distance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinateA, coordinateB, distance);
  }

  @Override
  public String toString() {
    return "CoordinateDistance{"
        + "coordinateA="
        + coordinateA
        + ", coordinateB="
        + coordinateB
        + ", distance="
        + distance
        + '}';
  }
}
