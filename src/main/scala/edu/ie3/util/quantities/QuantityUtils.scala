/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.quantities
import edu.ie3.util.quantities.PowerSystemUnits.*
import edu.ie3.util.quantities.electro.*
import edu.ie3.util.quantities.energy.*
import edu.ie3.util.quantities.prices.*
import edu.ie3.util.quantities.radio.*
import edu.ie3.util.quantities.thermal.*
import squants.*
import squants.electro.*
import squants.energy.*
import squants.market.EUR
import squants.mass.KilogramsPerCubicMeter
import squants.radio.{Irradiance, WattsPerSquareMeter}
import squants.space.*
import squants.thermal.{Celsius, ThermalCapacity}
import squants.time.{Hours, Milliseconds, Minutes}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.*

import javax.measure.{Quantity, Unit, quantity as jq}
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object QuantityUtils {

  /** Extension to enrich the [[Double]] with [[ComparableQuantity]] and
    * [[squants.Quantity]] conversion capabilities.
    */
  extension (value: Double) {

    /* javax measure units */

    def asPercent: ComparableQuantity[jq.Dimensionless] = as(PERCENT)

    def percent: Dimensionless = Percent(value)

    /* indriya units */

    def asSquareMetre: ComparableQuantity[jq.Area] = as(SQUARE_METRE)

    def squareMeter: Area = SquareMeters(value)

    def asVolt: ComparableQuantity[jq.ElectricPotential] = as(VOLT)

    def volt: ElectricPotential = Volts(value)

    def asAmpere: ComparableQuantity[jq.ElectricCurrent] = as(AMPERE)

    def ampere: ElectricCurrent = Amperes(value)

    def asKiloAmpere: ComparableQuantity[jq.ElectricCurrent] = as(
      KILOAMPERE
    )

    def kiloAmpere: ElectricCurrent = KiloAmperes(value)

    def asNanoSiemens: ComparableQuantity[jq.ElectricConductance] = as(
      NANOSIEMENS
    )

    def nanoSiemens: ElectricalConductance = NanoSiemens(value)

    def asSiemens: ComparableQuantity[jq.ElectricConductance] = as(SIEMENS)

    def siemens: ElectricalConductance = Siemens(value)

    def asMilliOhm: ComparableQuantity[jq.ElectricResistance] = as(MILLIOHM)

    def milliOhm: ElectricalResistance = Milliohms(value)

    def asOhm: ComparableQuantity[jq.ElectricResistance] = as(OHM)

    def ohm: ElectricalResistance = Ohms(value)

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    def asMetre: ComparableQuantity[jq.Length] = as(METRE)

    def meter: Length = Meters(value)

    def asKilometre: ComparableQuantity[jq.Length] = as(KILOMETRE)

    def kilometer: Length = Kilometers(value)

    def asMillimetre: ComparableQuantity[jq.Length] = as(MILLIMETRE)

    def milliMeter: Length = Millimeters(value)

    def asMillisecond: ComparableQuantity[jq.Time] = as(MILLISECOND)

    def milliSecond: Time = Milliseconds(value)

    def asSecond: ComparableQuantity[jq.Time] = as(SECOND)

    def second: Time = Seconds(value)

    def asMinute: ComparableQuantity[jq.Time] = as(MINUTE)

    def minute: Time = Minutes(value)

    def asHour: ComparableQuantity[jq.Time] = as(HOUR)

    def hour: Time = Hours(value)

    def asPu: ComparableQuantity[jq.Dimensionless] = as(PU)

    def pu: Dimensionless = Each(value)

    def asEuro: ComparableQuantity[interfaces.Currency] = as(EURO)

    def euro: Money = EUR(value)

    def asEuroPerKilometre: ComparableQuantity[interfaces.PricePerLength] = as(
      EURO_PER_KILOMETRE
    )

    def euroPerKilometer: PricePerLength = EuroPerKilometers(value)

    def asEuroPerWattHour: ComparableQuantity[interfaces.EnergyPrice] = as(
      EURO_PER_WATTHOUR
    )

    def euroPerWattHour: EnergyPrice = EuroPerWattHours(value)

    def asEuroPerKiloWattHour: ComparableQuantity[interfaces.EnergyPrice] = as(
      EURO_PER_KILOWATTHOUR
    )

    def euroPerKiloWattHour: EnergyPrice = EuroPerKilowattHours(value)

    def asEuroPerMegaWattHour: ComparableQuantity[interfaces.EnergyPrice] = as(
      EURO_PER_MEGAWATTHOUR
    )

    def euroPerMegaWattHour: EnergyPrice = EuroPerMegawattHours(value)

    def asDegreeGeom: ComparableQuantity[jq.Angle] = as(DEGREE_GEOM)

    def degreeGeom: Angle = Degrees(value)

    def asKilogramPerCubicMetre: ComparableQuantity[interfaces.Density] = as(
      KILOGRAM_PER_CUBIC_METRE
    )

    def kilogramPerCubicMeter: Density = KilogramsPerCubicMeter(value)

    /* ==== Energy ==== */

    def asWattHour: ComparableQuantity[jq.Energy] = as(WATTHOUR)

    def wattHour: Energy = WattHours(value)

    def asKiloWattHour: ComparableQuantity[jq.Energy] = as(KILOWATTHOUR)

    def kilowattHours: Energy = KilowattHours(value)

    def asMegaWattHour: ComparableQuantity[jq.Energy] = as(MEGAWATTHOUR)

    def megawattHour: Energy = MegawattHours(value)

    def asVarHour: ComparableQuantity[jq.Energy] = as(VARHOUR)

    def varHours: Energy = VarHours(value)

    def asKiloVarHour: ComparableQuantity[jq.Energy] = as(KILOVARHOUR)

    def kilovarHour: Energy = KilovarHours(value)

    def asMegaVarHour: ComparableQuantity[jq.Energy] = as(MEGAVARHOUR)

    def megavarHour: Energy = MegavarHours(value)

    def asWattHourPerMetre: ComparableQuantity[interfaces.SpecificEnergy] = as(
      WATTHOUR_PER_METRE
    )

    def asKiloWattHourPerKiloMetre
        : ComparableQuantity[interfaces.SpecificEnergy] = as(
      KILOWATTHOUR_PER_KILOMETRE
    )

    def asWattHourPerSquareMetre: ComparableQuantity[interfaces.Irradiation] =
      as(
        WATTHOUR_PER_SQUAREMETRE
      )

    def wattHourPerSquareMeter: Irradiation = WattHoursPerSquareMeter(value)

    def asKiloWattHourPerSquareMetre
        : ComparableQuantity[interfaces.Irradiation] = as(
      KILOWATTHOUR_PER_SQUAREMETRE
    )

    def kilowattHourPerSquareMeter: Irradiation = KilowattHoursPerSquareMeter(
      value
    )

    /* ==== Power ==== */

    def asVoltAmpere: ComparableQuantity[jq.Power] = as(VOLTAMPERE)

    def voltampere: ApparentPower = Voltamperes(value)

    def asKiloVoltAmpere: ComparableQuantity[jq.Power] = as(KILOVOLTAMPERE)

    def kilovoltampere: ApparentPower = Kilovoltamperes(value)

    def asMegaVoltAmpere: ComparableQuantity[jq.Power] = as(MEGAVOLTAMPERE)

    def megavoltampere: ApparentPower = Megavoltamperes(value)

    def asVar: ComparableQuantity[jq.Power] = as(VAR)

    def `var`: ReactivePower = Vars(value)

    def asKiloVar: ComparableQuantity[jq.Power] = as(KILOVAR)

    def kilovar: ReactivePower = Kilovars(value)

    def asMegaVar: ComparableQuantity[jq.Power] = as(MEGAVAR)

    def megavar: ReactivePower = Megavars(value)

    def asWatt: ComparableQuantity[jq.Power] = as(WATT)

    def watt: Power = Watts(value)

    def asKiloWatt: ComparableQuantity[jq.Power] = as(KILOWATT)

    def kilowatt: Power = Kilowatts(value)

    def asMegaWatt: ComparableQuantity[jq.Power] = as(MEGAWATT)

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

    def asPercentPerHour: ComparableQuantity[interfaces.DimensionlessRate] = as(
      PERCENT_PER_HOUR
    )

    def percentPerHour: quantities.DimensionlessRate = PercentPerHours(value)

    def asPuPerHour: ComparableQuantity[interfaces.DimensionlessRate] = as(
      PU_PER_HOUR
    )

    def puPerHour: quantities.DimensionlessRate = PuPerHours(value)

    /* ==== Basic electric units ==== */

    def asKiloVolt: ComparableQuantity[jq.ElectricPotential] = as(KILOVOLT)

    def kilovolt: ElectricPotential = Kilovolts(value)

    def asMegaVolt: ComparableQuantity[jq.ElectricPotential] = as(MEGAVOLT)

    def megavolt: ElectricPotential = Megavolts(value)

    def asOhmPerKilometre: ComparableQuantity[interfaces.SpecificResistance] =
      as(OHM_PER_KILOMETRE)

    def asSiemensPerKilometre
        : ComparableQuantity[interfaces.SpecificConductance] =
      as(SIEMENS_PER_KILOMETRE)

    def asMicroSiemensPerKilometre
        : ComparableQuantity[interfaces.SpecificConductance] =
      as(MICRO_SIEMENS_PER_KILOMETRE)

    def asFarradPerMetre: ComparableQuantity[interfaces.SpecificCapacitance] =
      as(FARAD_PER_METRE)

    def asMicroFarradPerKilometre
        : ComparableQuantity[interfaces.SpecificCapacitance] =
      as(MICROFARAD_PER_KILOMETRE)

    /* ==== Thermal ==== */

    def asKelvin: ComparableQuantity[jq.Temperature] = as(
      KELVIN
    )

    def kelvin: Temperature = Kelvin(value)

    def asDegreeCelsius: ComparableQuantity[jq.Temperature] = as(
      CELSIUS
    )

    def celsius: Temperature = Celsius(value)

    def asKiloWattPerKelvin: ComparableQuantity[interfaces.ThermalConductance] =
      as(
        KILOWATT_PER_KELVIN
      )

    def kilowattPerKelvin: ThermalConductance = KilowattsPerKelvin(value)

    def asKiloWattHourPerKelvin: ComparableQuantity[interfaces.HeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN)

    def kilowattHourPerKelvin: ThermalCapacity = KilowattHourPerKelvin(value)

    def asKiloWattHourPerKelvinTimesCubicMetre
        : ComparableQuantity[interfaces.SpecificHeatCapacity] =
      as(KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE)

    def kiloWattHourPerKelvinTimesCubicMeter: SpecificHeatCapacity =
      KilowattHoursPerKelvinCubicMeters(value)

    /* ==== Volume ==== */

    def asCubicMetre: ComparableQuantity[jq.Volume] =
      as(CUBIC_METRE)

    def cubicMeter: Volume = CubicMeters(value)

    def asLitre: ComparableQuantity[jq.Volume] =
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

  extension [Q <: Quantity[Q]](unit: javax.measure.Unit[Q]) {

    /** Transform some power unit to given unit with the same prefix
      * @param targetUnit
      *   the target system unit
      * @return
      *   this unit converted to given
      */
    def toEquivalentIn(
        targetUnit: javax.measure.Unit[Q]
    ): javax.measure.Unit[Q] =
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

  extension (power: Power) {
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
