package edu.ie3.util.quantities

import edu.ie3.util.quantities.PowerSystemUnits.{
  DEGREE_GEOM,
  KILOVOLT,
  OHM_PER_KILOMETRE,
  PU,
  SIEMENS_PER_KILOMETRE,
  VOLTAMPERE
}
import edu.ie3.util.quantities.interfaces.{
  SpecificConductance,
  SpecificResistance
}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.{AMPERE, OHM, PERCENT, SIEMENS, VOLT}

import javax.measure.MetricPrefix
import javax.measure.quantity.{
  Angle,
  Dimensionless,
  ElectricConductance,
  ElectricCurrent,
  ElectricPotential,
  ElectricResistance,
  Power
}

object QuantityUtils {

  /** Implicit class to enrich the [[Double]] with [[ComparableQuantity]]
   * conversion capabilities
   *
   * @param value
   *   the actual double value
   */
  implicit class RichQuantityDouble(value: Double) {

    /* javax measure units */

    def toPercent: ComparableQuantity[Dimensionless] =
    Quantities.getQuantity(value, PERCENT)

    def toDegreeGeom: ComparableQuantity[Angle] =
    Quantities.getQuantity(value, DEGREE_GEOM)

    /* indriya units */

    def toVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, VOLT)

    def toKiloVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(
        value,
        KILOVOLT
      )

    def toAmpere: ComparableQuantity[ElectricCurrent] =
    Quantities.getQuantity(
    value,
    AMPERE
    )

    def toKiloAmpere: ComparableQuantity[ElectricCurrent] =
    Quantities.getQuantity(
    value,
    MetricPrefix.KILO(AMPERE)
    )

    def toNanoSiemens: ComparableQuantity[ElectricConductance] =
      Quantities.getQuantity(value, MetricPrefix.NANO(SIEMENS))

    def toSiemens: ComparableQuantity[ElectricConductance] =
    Quantities.getQuantity(value, SIEMENS)

    def toMilliOhm: ComparableQuantity[ElectricResistance] =
      Quantities.getQuantity(value, MetricPrefix.MILLI(OHM))

    def toOhm: ComparableQuantity[ElectricResistance] = Quantities.getQuantity(
    value,
    OHM
    )

    /* PowerSystemUnits*/

    def toPu: ComparableQuantity[Dimensionless] =
    Quantities.getQuantity(value, PU)


    def toMicroSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
    Quantities.getQuantity(
    value,
    MetricPrefix.MICRO(SIEMENS_PER_KILOMETRE)
    )

    def toOhmPerKilometre: ComparableQuantity[SpecificResistance] =
    Quantities.getQuantity(
    value,
    OHM_PER_KILOMETRE
    )

    def toMegaVoltAmpere: ComparableQuantity[Power] = Quantities.getQuantity(
    value,
    MetricPrefix.MEGA(VOLTAMPERE)
    )
  }
}
