/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import org.locationtech.jts.geom.Point

import java.util.Objects
import javax.measure.quantity.Length
import tech.units.indriya.ComparableQuantity

/** Wraps two coordinates with the distance between the first one and the second
  * one, can be compared by distance to another CoordinateDistance
  * @param coordinateA
  *   The first coordinate
  * @param coordinateB
  *   The second coordinate
  * @param distance
  *   The distance from A to B
  */
class CoordinateDistance private (
    val coordinateA: Point,
    val coordinateB: Point,
    val distance: ComparableQuantity[Length]
) extends Comparable[CoordinateDistance] {

  /** Calculates the distance from the first to the second coordinate using
    * [[GeoUtils.calcHaversine(double, double, double, double)]]
    *
    * @param coordinateA
    *   The first coordinate
    * @param coordinateB
    *   The second coordinate
    */
  def this(coordinateA: Point, coordinateB: Point) = {
    this(
      coordinateA,
      coordinateB,
      GeoUtils.calcHaversine(
        coordinateA.getY,
        coordinateA.getX,
        coordinateB.getY,
        coordinateB.getX
      )
    )
  }

  /** @return The first coordinate */
  def getCoordinateA: Point = {
    coordinateA
  }

  /** @return The second coordinate */
  def getCoordinateB: Point = {
    coordinateB
  }

  /** @return
    *   The distance from the first coordinate to the second coordinate in km
    */
  def getDistance: ComparableQuantity[Length] = {
    distance
  }

  /** Compares two coordinate distances on the length of the distance alone,
    * thus having a natural ordering that is inconsistent with equals
    *
    * @param that
    *   the distance to compare
    * @return
    *   a number lower than 0 if this has a lower distance than that, 0 if they
    *   are the same, a number higher than 0 if that has a lower distance
    */
  override def compareTo(that: CoordinateDistance): Int = {
    this.distance.compareTo(that.distance)
  }

  override def equals(other: Any): Boolean = other match {
    case other: CoordinateDistance if this == other => true
    case other: CoordinateDistance =>
      coordinateA.equals(other.coordinateA) && coordinateB.equals(
        other.coordinateB
      ) && distance == other.distance
    case _ => false
  }

  override def hashCode: Int = {
    Objects.hash(coordinateA, coordinateB, distance)
  }

  override def toString: String = {
    "CoordinateDistance{" + "coordinateA=" + coordinateA + ", coordinateB=" + coordinateB + ", distance=" + distance + '}'
  }
}
