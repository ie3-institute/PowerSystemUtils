/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.quantities
import edu.ie3.util.quantities.PowerSystemUnits.*
import edu.ie3.util.quantities.electro.*
import edu.ie3.util.quantities.interfaces.*
import edu.ie3.util.quantities.prices.*
import edu.ie3.util.quantities.energy.*
import edu.ie3.util.quantities.radio.*
import edu.ie3.util.quantities.thermal.{
  SpecificHeatCapacity,
  ThermalConductance,
  *
}
import squants.*
import squants.electro.{ElectricPotential, *}
import squants.thermal.*
import squants.energy.{Energy, Power, *}
import squants.market.*
import squants.radio.{Irradiance, *}
import squants.mass.{Density, KilogramsPerCubicMeter}
import squants.space.{Angle, Volume, *}
import squants.time.*
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.*

import javax.measure
import javax.measure.quantity.*
import javax.measure.{Quantity, Unit}
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object QuantityUtils {

  /** Extension to enrich the [[Double]] with [[ComparableQuantity]] and
    * [[squants.Quantity]] conversion capabilities.
    */
  extension (value: Double) {

    /* javax measure units */

    def asPercent: ComparableQuantity[Dimensionless] = as(PERCENT)

    def percent: squants.Dimensionless = squants.Percent(value)

    /* indriya units */

    def asSquareMetre: ComparableQuantity[Area] = as(SQUARE_METRE)

    def squareMeter: squants.Area = SquareMeters(value)

    def asVolt: ComparableQuantity[ElectricPotential] = as(VOLT)

    def volt: squants.electro.ElectricPotential = Volts(value)

    def asAmpere: ComparableQuantity[ElectricCurrent] = as(AMPERE)

    def ampere: squants.ElectricCurrent = Amperes(value)

    def asKiloAmpere: ComparableQuantity[ElectricCurrent] = as(
      KILOAMPERE
    )

    def kiloAmpere: squants.ElectricCurrent = KiloAmperes(value)

    def asNanoSiemens: ComparableQuantity[ElectricConductance] = as(
      NANOSIEMENS
    )

    def nanoSiemens: ElectricalConductance = NanoSiemens(value)

    def asSiemens: ComparableQuantity[ElectricConductance] = as(SIEMENS)

    def siemens: ElectricalConductance = Siemens(value)

    def asMilliOhm: ComparableQuantity[ElectricResistance] = as(MILLIOHM)

    def milliOhm: ElectricalResistance = Milliohms(value)

    def asOhm: ComparableQuantity[ElectricResistance] = as(OHM)

    def ohm: ElectricalResistance = Ohms(value)

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    def asMetre: ComparableQuantity[Length] = as(METRE)

    def meter: squants.Length = Meters(value)

    def asKilometre: ComparableQuantity[Length] = as(KILOMETRE)

    def kilometer: squants.Length = Kilometers(value)

    def asMillimetre: ComparableQuantity[Length] = as(MILLIMETRE)

    def milliMeter: squants.Length = Millimeters(value)

    def asMillisecond: ComparableQuantity[javax.measure.quantity.Time] = as(
      MILLISECOND
    )

    def milliSecond: squants.Time = Milliseconds(value)

    def asSecond: ComparableQuantity[javax.measure.quantity.Time] = as(SECOND)

    def second: Any = Seconds(value)

    def asMinute: ComparableQuantity[javax.measure.quantity.Time] = as(MINUTE)

    def minute: Time = Minutes(value)

    def asHour: ComparableQuantity[javax.measure.quantity.Time] = as(HOUR)

    def hour: Time = Hours(value)

    def asPu: ComparableQuantity[javax.measure.quantity.Dimensionless] = as(PU)

    def pu: squants.Dimensionless = Each(value)

    def asEuro: ComparableQuantity[Currency] = as(EURO)

    def euro: squants.market.Money = EUR(value)

    def asEuroPerKilometre: ComparableQuantity[PricePerLength] = as(
      EURO_PER_KILOMETRE
    )

    def euroPerKilometer: prices.PricePerLength = EuroPerKilometers(value)

    def asEuroPerWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_WATTHOUR
    )

    def euroPerWattHour: EnergyPrice = EuroPerWattHours(value)

    def asEuroPerKiloWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_KILOWATTHOUR
    )

    def euroPerKiloWattHour: EnergyPrice = EuroPerKilowattHours(value)

    def asEuroPerMegaWattHour: ComparableQuantity[EnergyPrice] = as(
      EURO_PER_MEGAWATTHOUR
    )

    def euroPerMegaWattHour: EnergyPrice = EuroPerMegawattHours(value)

    def asDegreeGeom: ComparableQuantity[Angle] = as(DEGREE_GEOM)

    def degreeGeom: Angle = Degrees(value)

    def asKilogramPerCubicMetre: ComparableQuantity[Density] = as(
      KILOGRAM_PER_CUBIC_METRE
    )

    def kilogramPerCubicMeter: Density = KilogramsPerCubicMeter(value)

    /* ==== Energy ==== */

    def asWattHour: ComparableQuantity[Energy] = as(WATTHOUR)

    def wattHour: Energy = WattHours(value)

    def asKiloWattHour: ComparableQuantity[Energy] = as(KILOWATTHOUR)

    def kilowattHours: Energy = KilowattHours(value)

    def asMegaWattHour: ComparableQuantity[Energy] = as(MEGAWATTHOUR)

    def megawattHour: Energy = MegawattHours(value)

    def asVarHour: ComparableQuantity[Energy] = as(VARHOUR)

    def varHours: Energy = VarHours(value)

    def asKiloVarHour: ComparableQuantity[Energy] = as(KILOVARHOUR)

    def kilovarHour: Energy = KilovarHours(value)

    def asMegaVarHour: ComparableQuantity[Energy] = as(MEGAVARHOUR)

    def megavarHour: Energy = MegavarHours(value)

    def asWattHourPerMetre: ComparableQuantity[SpecificEnergy] = as(
      WATTHOUR_PER_METRE
    )

    def asKiloWattHourPerKiloMetre: ComparableQuantity[SpecificEnergy] = as(
      KILOWATTHOUR_PER_KILOMETRE
    )

    def asWattHourPerSquareMetre: ComparableQuantity[Irradiation] = as(
      WATTHOUR_PER_SQUAREMETRE
    )

    def wattHourPerSquareMeter: Irradiation = WattHoursPerSquareMeter(value)

    def asKiloWattHourPerSquareMetre: ComparableQuantity[Irradiation] = as(
      KILOWATTHOUR_PER_SQUAREMETRE
    )

    def kilowattHourPerSquareMeter: Irradiation = KilowattHoursPerSquareMeter(
      value
    )

    /* ==== Power ==== */

    def asVoltAmpere: ComparableQuantity[Power] = as(VOLTAMPERE)

    def voltampere: ApparentPower = Voltamperes(value)

    def asKiloVoltAmpere: ComparableQuantity[Power] = as(KILOVOLTAMPERE)

    def kilovoltampere: ApparentPower = Kilovoltamperes(value)

    def asMegaVoltAmpere: ComparableQuantity[Power] = as(MEGAVOLTAMPERE)

    def megavoltampere: ApparentPower = Megavoltamperes(value)

    def asVar: ComparableQuantity[Power] = as(VAR)

    def `var`: ReactivePower = Vars(value)

    def asKiloVar: ComparableQuantity[Power] = as(KILOVAR)

    def kilovar: ReactivePower = Kilovars(value)

    def asMegaVar: ComparableQuantity[Power] = as(MEGAVAR)

    def megavar: ReactivePower = Megavars(value)

    def asWatt: ComparableQuantity[Power] = as(WATT)

    def watt: Power = Watts(value)

    def asKiloWatt: ComparableQuantity[Power] = as(KILOWATT)

    def kilowatt: Power = Kilowatts(value)

    def asMegaWatt: ComparableQuantity[Power] = as(MEGAWATT)

    def megawatt: Power = Megawatts(value)

    def asWattPerSquareMetre: ComparableQuantity[interfaces.Irradiance] = as(
      WATT_PER_SQUAREMETRE
    )

    def wattPerSquareMeter: Irradiance = WattsPerSquareMeter(value)

    def asKiloWattPerSquareMetre: ComparableQuantity[interfaces.Irradiance] =
      as(
        KILOWATT_PER_SQUAREMETRE
      )

    def kilowattPerSquareMeter: Irradiance = KilowattsPerSquareMeter(value)

    /* ==== Composed units ==== */

    def asPercentPerHour: ComparableQuantity[DimensionlessRate] = as(
      PERCENT_PER_HOUR
    )

    def percentPerHour: quantities.DimensionlessRate = PercentPerHours(value)

    def asPuPerHour: ComparableQuantity[DimensionlessRate] = as(PU_PER_HOUR)

    def puPerHour: quantities.DimensionlessRate = PuPerHours(value)

    /* ==== Basic electric units ==== */

    def asKiloVolt: ComparableQuantity[ElectricPotential] = as(KILOVOLT)

    def kilovolt: ElectricPotential = Kilovolts(value)

    def asMegaVolt: ComparableQuantity[ElectricPotential] = as(MEGAVOLT)

    def megavolt: ElectricPotential = Megavolts(value)

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

    def kelvin: squants.Temperature = Kelvin(value)

    def asDegreeCelsius: ComparableQuantity[Temperature] = as(
      CELSIUS
    )

    def celsius: squants.Temperature = Celsius(value)

    def asKiloWattPerKelvin: ComparableQuantity[interfaces.ThermalConductance] =
      as(
        KILOWATT_PER_KELVIN
      )

    def kilowattPerKelvin: ThermalConductance = KilowattsPerKelvin(value)

    def asKiloWattHourPerKelvin: ComparableQuantity[HeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN)

    def kilowattHourPerKelvin: ThermalCapacity = KilowattHourPerKelvin(value)

    def asKiloWattHourPerKelvinTimesCubicMetre
        : ComparableQuantity[interfaces.SpecificHeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE)

    def kiloWattHourPerKelvinTimesCubicMeter: SpecificHeatCapacity =
      KilowattHoursPerKelvinCubicMeters(value)

    /* ==== Volume ==== */

    def asCubicMetre: ComparableQuantity[Volume] =
      as(CUBIC_METRE)

    def cubicMeter: Volume = CubicMeters(value)

    def asLitre: ComparableQuantity[Volume] =
      as(LITRE)

    def litre: Volume = Litres(value)

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

  extension [Q <: Quantity[Q]](q: ComparableQuantity[Q]) {

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

  extension [Q <: Quantity[Q]](unit: measure.Unit[Q]) {

    /** Transform some power unit to given unit with the same prefix
      * @param targetUnit
      *   the target system unit
      * @return
      *   this unit converted to given
      */
    def toEquivalentIn(targetUnit: measure.Unit[Q]): measure.Unit[Q] =
      targetUnit.transform(unit.getConverterTo(unit.getSystemUnit))
  }

  extension (energyDensity: EnergyDensity) {
    def toKilowattHoursPerCubicMeter: Double =
      energyDensity.to(KilowattHoursPerCubicMeter)
  }

  extension (energy: Energy) {
    def calcVolume(that: EnergyDensity): Volume = CubicMeters(
      energy.toKilowattHours / that.toKilowattHoursPerCubicMeter
    )
  }

  extension (power: squants.Power) {
    def /(that: ReactivePower): Dimensionless = Each(
      power.toWatts / that.toVars
    )
  }

  extension (
      electricPotential: ElectricPotential
  ) {
    def multiplyWithDimensionless(
        that: Dimensionless
    ): ElectricPotential = Volts(
      electricPotential.toVolts * that.toEach
    )

  }

  extension (
      thermalCapacity: ThermalCapacity
  ) {
    def toWattHoursPerKelvin: Double =
      thermalCapacity.toJoulesPerKelvin / 3600
    def toWattSecondsPerKelvin: Double =
      thermalCapacity.toJoulesPerKelvin // Joule == Ws
  }

  extension (
      irradiance: Irradiance
  ) {
    def *(that: Time): Irradiation = WattHoursPerSquareMeter(
      irradiance.toWattsPerSquareMeter * that.toSeconds / Hours(1).toSeconds
    )
  }

}
