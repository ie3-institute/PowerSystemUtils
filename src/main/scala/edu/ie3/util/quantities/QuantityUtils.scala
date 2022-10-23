/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.quantities.PowerSystemUnits._
import edu.ie3.util.quantities.interfaces.{
  Currency,
  Density,
  DimensionlessRate,
  EnergyPrice,
  HeatCapacity,
  Irradiance,
  Irradiation,
  PricePerLength,
  SpecificCapacitance,
  SpecificConductance,
  SpecificEnergy,
  SpecificHeatCapacity,
  SpecificResistance,
  ThermalConductance
}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units._

import javax.measure
import javax.measure.{MetricPrefix, Quantity}
import javax.measure.quantity.{
  Angle,
  Area,
  Dimensionless,
  ElectricConductance,
  ElectricCurrent,
  ElectricPotential,
  ElectricResistance,
  Energy,
  Length,
  Power,
  Time
}
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object QuantityUtils {

  /** Implicit class to enrich the [[Double]] with [[ComparableQuantity]]
    * conversion capabilities
    *
    * @param value
    *   the actual double value
    */
  implicit class RichQuantityDouble(value: Double) {

    /* javax measure units */

    def asPercent: ComparableQuantity[Dimensionless] =
      Quantities.getQuantity(value, PERCENT)

    /* indriya units */

    def asSquareMetre: ComparableQuantity[Area] =
      Quantities.getQuantity(value, SQUARE_METRE)

    def asVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, VOLT)

    def asAmpere: ComparableQuantity[ElectricCurrent] =
      Quantities.getQuantity(value, AMPERE)

    def asKiloAmpere: ComparableQuantity[ElectricCurrent] =
      Quantities.getQuantity(value, MetricPrefix.KILO(AMPERE))

    def asNanoSiemens: ComparableQuantity[ElectricConductance] =
      Quantities.getQuantity(value, MetricPrefix.NANO(SIEMENS))

    def asSiemens: ComparableQuantity[ElectricConductance] =
      Quantities.getQuantity(value, SIEMENS)

    def asMilliOhm: ComparableQuantity[ElectricResistance] =
      Quantities.getQuantity(value, MetricPrefix.MILLI(OHM))

    def asOhm: ComparableQuantity[ElectricResistance] =
      Quantities.getQuantity(value, OHM)

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    def asKilometre: ComparableQuantity[Length] =
      Quantities.getQuantity(value, KILOMETRE)

    def asMillisecond: ComparableQuantity[Time] =
      Quantities.getQuantity(value, MILLISECOND)

    def asPu: ComparableQuantity[Dimensionless] =
      Quantities.getQuantity(value, PU)

    def asEuro: ComparableQuantity[Currency] =
      Quantities.getQuantity(value, EURO)

    def asEuroPerKilometre: ComparableQuantity[PricePerLength] =
      Quantities.getQuantity(value, EURO_PER_KILOMETRE)

    def asEuroPerWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_WATTHOUR)

    def asEuroPerKiloWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_KILOWATTHOUR)

    def asEuroPerMegaWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_MEGAWATTHOUR)

    def asDegreeGeom: ComparableQuantity[Angle] =
      Quantities.getQuantity(value, DEGREE_GEOM)

    def asKilogramPerCubicMetre: ComparableQuantity[Density] =
      Quantities.getQuantity(value, KILOGRAM_PER_CUBIC_METRE)

    /* ==== Energy ==== */

    def asWattHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, WATTHOUR)

    def asKiloWattHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, KILOWATTHOUR)

    def asVarHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, VARHOUR)

    def asKiloVarHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, KILOVARHOUR)

    def asWattHourPerMetre: ComparableQuantity[SpecificEnergy] =
      Quantities.getQuantity(value, WATTHOUR_PER_METRE)

    def asKiloWattHourPerKiloMetre: ComparableQuantity[SpecificEnergy] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KILOMETRE)

    def asWattHourPerSquareMetre: ComparableQuantity[Irradiation] =
      Quantities.getQuantity(value, WATTHOUR_PER_SQUAREMETRE)

    def asKiloWattHourPerSquareMetre: ComparableQuantity[Irradiation] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_SQUAREMETRE)

    /* ==== Power ==== */

    def asVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, VOLTAMPERE)

    def asKiloVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOVOLTAMPERE)

    def asMegaVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAVOLTAMPERE)

    def asVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, VAR)

    def asKiloVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOVAR)

    def asMegaVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAVAR)

    def asWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, WATT)

    def asKiloWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOWATT)

    def asMegaWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAWATT)

    def asWattPerSquareMetre: ComparableQuantity[Irradiance] =
      Quantities.getQuantity(value, WATT_PER_SQUAREMETRE)

    def asKiloWattPerSquareMetre: ComparableQuantity[Irradiance] =
      Quantities.getQuantity(value, KILOWATT_PER_SQUAREMETRE)

    /* ==== Composed units ==== */

    def asPercentPerHour: ComparableQuantity[DimensionlessRate] =
      Quantities.getQuantity(value, PERCENT_PER_HOUR)

    def asPuPerHour: ComparableQuantity[DimensionlessRate] =
      Quantities.getQuantity(value, PU_PER_HOUR)

    /* ==== Basic electric units ==== */

    def asKiloVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, KILOVOLT)

    def asMegaVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, MEGAVOLT)

    def asOhmPerKilometre: ComparableQuantity[SpecificResistance] =
      Quantities.getQuantity(value, OHM_PER_KILOMETRE)

    def asSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      Quantities.getQuantity(value, SIEMENS_PER_KILOMETRE)

    def asMicroSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      Quantities.getQuantity(value, MICRO_SIEMENS_PER_KILOMETRE)

    def asFarradPerMetre: ComparableQuantity[SpecificCapacitance] =
      Quantities.getQuantity(value, FARAD_PER_METRE)

    def asMicroFarradPerKilometre: ComparableQuantity[SpecificCapacitance] =
      Quantities.getQuantity(value, MICROFARAD_PER_KILOMETRE)

    def asKiloWattHourPerKelvin: ComparableQuantity[HeatCapacity] = {
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN)
    }

    def asKiloWattHourPerKelvinTimesCubicMetre
        : ComparableQuantity[SpecificHeatCapacity] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE)

    /* ==== Thermal Conductance ==== */

    def asKiloWattPerKelvin: ComparableQuantity[ThermalConductance] =
      Quantities.getQuantity(value, KILOWATT_PER_KELVIN)

  }

  implicit class RichQuantity[Q <: Quantity[Q]](
      private val q: ComparableQuantity[Q]
  ) extends AnyVal {

    /** Returns the smaller of two Quantities
      *
      * @param other
      *   the other Quantity
      * @return
      *   the smaller of both Quantities
      */
    def min(other: ComparableQuantity[Q]): ComparableQuantity[Q] = {
      if (q.isLessThanOrEqualTo(other)) q else other
    }

    /** Returns the bigger of two Quantities
      *
      * @param other
      *   the other Quantity
      * @return
      *   the bigger of both Quantities
      */
    def max(other: ComparableQuantity[Q]): ComparableQuantity[Q] = {
      if (q.isGreaterThan(other)) q else other
    }

    /** Rounds a quantity given a specified rounding mode after a specified
      * decimal.
      *
      * @param decimals
      *   how many decimals to consider
      * @param roundingMode
      *   the rounding mode to use
      * @tparam Q
      *   type of the quantity
      * @return
      *   the rounded quantity
      */
    def round(
        decimals: Int,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): ComparableQuantity[Q] = {
      if (decimals < 0)
        throw new IllegalArgumentException(
          "You can not round to negative decimal places."
        )
      val rounded = BigDecimal
        .valueOf(q.getValue.doubleValue())
        .setScale(decimals, roundingMode)
        .doubleValue
      Quantities.getQuantity(rounded, q.getUnit)
    }
  }

  implicit class RichUnit[Q <: Quantity[Q]](
      private val unit: measure.Unit[Q]
  ) extends AnyVal {

    /** Transform some power unit to given unit with the same prefix
      * @param targetUnit
      *   the target system unit
      * @return
      *   this unit converted to given
      */
    def toEquivalentIn(targetUnit: measure.Unit[Q]): measure.Unit[Q] =
      targetUnit.transform(unit.getConverterTo(unit.getSystemUnit))
  }

}
