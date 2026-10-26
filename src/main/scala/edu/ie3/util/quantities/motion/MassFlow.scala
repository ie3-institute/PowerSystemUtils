/*
 * © 2023. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.motion

import squants.*

import scala.util.Try

/** Represents the mass flow, in kg*s. */
final class MassFlow private (
    val value: Double,
    val unit: MassFlowUnit
) extends Quantity[MassFlow] {

  def dimension: MassFlow.type = MassFlow

  def *(that: Time): Mass = Kilograms(toKilogramsPerSecond * that.toSeconds)

  def toKilogramsPerSecond: Double = to(KilogramsPerSecond)
}

object MassFlow extends Dimension[MassFlow] {
  private[motion] def apply[A](n: A, unit: MassFlowUnit)(using
      num: Numeric[A]
  ) = new MassFlow(num.toDouble(n), unit)
  def apply(value: Any): Try[MassFlow] = parse(value)
  def name = "MassFlow"
  def primaryUnit: KilogramsPerSecond.type = KilogramsPerSecond
  def siUnit: KilogramsPerSecond.type = KilogramsPerSecond
  def units: Set[UnitOfMeasure[MassFlow]] = Set(
    KilogramsPerSecond
  )
}

trait MassFlowUnit extends UnitOfMeasure[MassFlow] with UnitConverter {
  def apply[A](n: A)(using num: Numeric[A]): MassFlow =
    MassFlow(n, this)
}

object KilogramsPerSecond extends MassFlowUnit with PrimaryUnit with SiUnit {
  val symbol: String = "kg/s"
}
