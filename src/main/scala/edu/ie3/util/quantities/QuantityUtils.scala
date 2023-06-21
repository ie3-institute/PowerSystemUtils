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
import tech.units.indriya.function.Calculus
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units._

import javax.measure
import javax.measure.{Quantity, Unit}
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
  Temperature,
  Time,
  Volume
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

    def asPercent: ComparableQuantity[Dimensionless] = as(PERCENT)

    /* indriya units */

    def asSquareMetre: ComparableQuantity[Area] = as(SQUARE_METRE)

    def asVolt: ComparableQuantity[ElectricPotential] = as(VOLT)

    def asAmpere: ComparableQuantity[ElectricCurrent] = as(AMPERE)

    def asKiloAmpere: ComparableQuantity[ElectricCurrent] = as(
      KILOAMPERE
    )

    def asNanoSiemens: ComparableQuantity[ElectricConductance] = as(
      NANOSIEMENS
    )

    def asSiemens: ComparableQuantity[ElectricConductance] = as(SIEMENS)

    def asMilliOhm: ComparableQuantity[ElectricResistance] = as(MILLIOHM)

    def asOhm: ComparableQuantity[ElectricResistance] = as(OHM)

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    def asMetre: ComparableQuantity[Length] = as(METRE)

    def asKilometre: ComparableQuantity[Length] = as(KILOMETRE)

    def asMillisecond: ComparableQuantity[Time] = as(MILLISECOND)

    def asSecond: ComparableQuantity[Time] = as(SECOND)

    def asMinute: ComparableQuantity[Time] = as(MINUTE)

    def asHour: ComparableQuantity[Time] = as(HOUR)

    def asPu: ComparableQuantity[Dimensionless] = as(PU)

    def asEuro: ComparableQuantity[Currency] = as(EURO)

    def asEuroPerKilometre: ComparableQuantity[PricePerLength] = as(
      EURO_PER_KILOMETRE
    )

    def asEuroPerWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_WATTHOUR
    )

    def asEuroPerKiloWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_KILOWATTHOUR
    )

    def asEuroPerMegaWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_MEGAWATTHOUR
    )

    def asDegreeGeom: ComparableQuantity[Angle] = as(DEGREE_GEOM)

    def asKilogramPerCubicMetre: ComparableQuantity[Density] = as(
      KILOGRAM_PER_CUBIC_METRE
    )

    /* ==== Energy ==== */

    def asWattHour: ComparableQuantity[Energy] = as(WATTHOUR)

    def asKiloWattHour: ComparableQuantity[Energy] = as(KILOWATTHOUR)

    def asMegaWattHour: ComparableQuantity[Energy] = as(MEGAWATTHOUR)

    def asVarHour: ComparableQuantity[Energy] = as(VARHOUR)

    def asKiloVarHour: ComparableQuantity[Energy] = as(KILOVARHOUR)

    def asMegaVarHour: ComparableQuantity[Energy] = as(MEGAVARHOUR)

    def asWattHourPerMetre: ComparableQuantity[SpecificEnergy] = as(
      WATTHOUR_PER_METRE
    )

    def asKiloWattHourPerKiloMetre: ComparableQuantity[SpecificEnergy] = as(
      KILOWATTHOUR_PER_KILOMETRE
    )

    def asWattHourPerSquareMetre: ComparableQuantity[Irradiation] = as(
      WATTHOUR_PER_SQUAREMETRE
    )

    def asKiloWattHourPerSquareMetre: ComparableQuantity[Irradiation] = as(
      KILOWATTHOUR_PER_SQUAREMETRE
    )

    /* ==== Power ==== */

    def asVoltAmpere: ComparableQuantity[Power] = as(VOLTAMPERE)

    def asKiloVoltAmpere: ComparableQuantity[Power] = as(KILOVOLTAMPERE)

    def asMegaVoltAmpere: ComparableQuantity[Power] = as(MEGAVOLTAMPERE)

    def asVar: ComparableQuantity[Power] = as(VAR)

    def asKiloVar: ComparableQuantity[Power] = as(KILOVAR)

    def asMegaVar: ComparableQuantity[Power] = as(MEGAVAR)

    def asWatt: ComparableQuantity[Power] = as(WATT)

    def asKiloWatt: ComparableQuantity[Power] = as(KILOWATT)

    def asMegaWatt: ComparableQuantity[Power] = as(MEGAWATT)

    def asWattPerSquareMetre: ComparableQuantity[Irradiance] = as(
      WATT_PER_SQUAREMETRE
    )

    def asKiloWattPerSquareMetre: ComparableQuantity[Irradiance] = as(
      KILOWATT_PER_SQUAREMETRE
    )

    /* ==== Composed units ==== */

    def asPercentPerHour: ComparableQuantity[DimensionlessRate] = as(
      PERCENT_PER_HOUR
    )

    def asPuPerHour: ComparableQuantity[DimensionlessRate] = as(PU_PER_HOUR)

    /* ==== Basic electric units ==== */

    def asKiloVolt: ComparableQuantity[ElectricPotential] = as(KILOVOLT)

    def asMegaVolt: ComparableQuantity[ElectricPotential] = as(MEGAVOLT)

    def asOhmPerKilometre: ComparableQuantity[SpecificResistance] =
      as(OHM_PER_KILOMETRE)

    def asSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      as(SIEMENS_PER_KILOMETRE)

    def asMicroSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      as(MICRO_SIEMENS_PER_KILOMETRE)

    def asFarradPerMetre: ComparableQuantity[SpecificCapacitance] =
      as(FARAD_PER_METRE)

    def asMicroFarradPerKilometre: ComparableQuantity[SpecificCapacitance] =
      as(MICROFARAD_PER_KILOMETRE)

    /* ==== Thermal ==== */

    def asKelvin: ComparableQuantity[Temperature] = as(
      KELVIN
    )

    def asDegreeCelsius: ComparableQuantity[Temperature] = as(
      CELSIUS
    )

    def asKiloWattPerKelvin: ComparableQuantity[ThermalConductance] = as(
      KILOWATT_PER_KELVIN
    )

    def asKiloWattHourPerKelvin: ComparableQuantity[HeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN)

    def asKiloWattHourPerKelvinTimesCubicMetre
        : ComparableQuantity[SpecificHeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE)

    /* ==== Volume ==== */

    def asCubicMetre: ComparableQuantity[Volume] =
      as(CUBIC_METRE)

    def asLitre: ComparableQuantity[Volume] =
      as(LITRE)

    /** Create a quantity from the double with given unit
      * @param unit
      *   the unit to create the quantity with
      * @tparam Q
      *   the quantity type of given unit
      * @return
      *   a quantity of given unit
      */
    def as[Q <: Quantity[Q]](unit: Unit[Q]): ComparableQuantity[Q] =
      Quantities.getQuantity(value, unit)

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

  /** The [[tech.units.indriya.function.DefaultNumberSystem]] is only covering
    * java [[Number]] children. As [[BigDecimal]] is not related to
    * [[java.math.BigDecimal]], this causes issues, why the
    * [[tech.units.indriya.spi.NumberSystem]] has to be to be used has to be
    * specified to something, that actually is able to handle the scala number
    * system.
    */
  def adjustNumberSystem() =
    Calculus.setCurrentNumberSystem(
      Calculus.getNumberSystem("edu.ie3.util.quantities.ScalaNumberSystem")
    )
}
