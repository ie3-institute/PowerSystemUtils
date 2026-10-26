/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import squants.*
import squants.motion.{Pascals, Pressure}

import scala.util.Try

/** Represents the dynamic viscosity, in Pa*s. */
final class DynamicViscosity private (
    val value: Double,
    val unit: ViscosityUnit
) extends Quantity[DynamicViscosity] {

  def dimension: DynamicViscosity.type = DynamicViscosity

  def /(that: Time): Pressure = Pascals(toPascalSeconds * that.toSeconds)

  def toPascalSeconds: Double = to(PascalSeconds)
}

object DynamicViscosity extends Dimension[DynamicViscosity] {
  private[quantities] def apply[A](n: A, unit: ViscosityUnit)(using
      num: Numeric[A]
  ) = new DynamicViscosity(num.toDouble(n), unit)
  def apply(value: Any): Try[DynamicViscosity] = parse(value)
  def name = "DynamicViscosity"
  def primaryUnit: PascalSeconds.type = PascalSeconds
  def siUnit: PascalSeconds.type = PascalSeconds
  def units: Set[UnitOfMeasure[DynamicViscosity]] = Set(
    PascalSeconds
  )
}

trait ViscosityUnit extends UnitOfMeasure[DynamicViscosity] with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): DynamicViscosity =
    DynamicViscosity(n, this)
}

object PascalSeconds extends ViscosityUnit with PrimaryUnit with SiUnit {
  val symbol: String = "Pa*s"
}
