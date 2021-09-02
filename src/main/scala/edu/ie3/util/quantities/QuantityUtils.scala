package edu.ie3.util.quantities

import edu.ie3.util.quantities.PowerSystemUnits._
import edu.ie3.util.quantities.interfaces.{Currency, Density, DimensionlessRate, EnergyPrice, HeatCapacity, Irradiance, Irradiation, PricePerLength, SpecificCapacitance, SpecificConductance, SpecificEnergy, SpecificHeatCapacity, SpecificResistance, ThermalConductance}
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.{AMPERE, OHM, PERCENT, SIEMENS, VOLT, WATT}

import javax.measure.MetricPrefix
import javax.measure.quantity.{Angle, Dimensionless, ElectricConductance, ElectricCurrent, ElectricPotential, ElectricResistance, Energy, Length, Power, Time}

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

    /* indriya units */

    def toVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, VOLT)

    def toAmpere: ComparableQuantity[ElectricCurrent] =
      Quantities.getQuantity(value, AMPERE)

    def toKiloAmpere: ComparableQuantity[ElectricCurrent] =
      Quantities.getQuantity(value, MetricPrefix.KILO(AMPERE))

    def toNanoSiemens: ComparableQuantity[ElectricConductance] =
      Quantities.getQuantity(value, MetricPrefix.NANO(SIEMENS))

    def toSiemens: ComparableQuantity[ElectricConductance] =
      Quantities.getQuantity(value, SIEMENS)

    def toMilliOhm: ComparableQuantity[ElectricResistance] =
      Quantities.getQuantity(value, MetricPrefix.MILLI(OHM))

    def toOhm: ComparableQuantity[ElectricResistance] =
      Quantities.getQuantity(value, OHM)

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    def toKilometre: ComparableQuantity[Length] =
      Quantities.getQuantity(value, KILOMETRE)

    def toMillisecond: ComparableQuantity[Time] =
      Quantities.getQuantity(value, MILLISECOND)

    def toPu: ComparableQuantity[Dimensionless] =
      Quantities.getQuantity(value, PU)

    def toEuro: ComparableQuantity[Currency] =
      Quantities.getQuantity(value, EURO)

    def toEuroPerKilometre: ComparableQuantity[PricePerLength] =
      Quantities.getQuantity(value, EURO_PER_KILOMETRE)

    def toEuroPerWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_WATTHOUR)

    def toEuroPerKiloWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_KILOWATTHOUR)

    def toEuroPerMegaWattHour: ComparableQuantity[EnergyPrice] =
      Quantities.getQuantity(value, EURO_PER_MEGAWATTHOUR)

    def toDegreeGeom: ComparableQuantity[Angle] =
      Quantities.getQuantity(value, DEGREE_GEOM)

    def toKilogramPerCubicMetre: ComparableQuantity[Density] =
      Quantities.getQuantity(value, KILOGRAM_PER_CUBIC_METRE)

    /* ==== Energy ==== */

    def toWattHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, WATTHOUR)

    def toKiloWattHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, KILOWATTHOUR)

    def toVarHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, VARHOUR)

    def toKiloVarHour: ComparableQuantity[Energy] =
      Quantities.getQuantity(value, KILOVARHOUR)

    def toWattHourPerMetre: ComparableQuantity[SpecificEnergy] =
      Quantities.getQuantity(value, WATTHOUR_PER_METRE)

    def toKiloWattHourPerKiloMetre: ComparableQuantity[SpecificEnergy] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KILOMETRE)

    def toWattHourPerSquareMetre: ComparableQuantity[Irradiation] =
      Quantities.getQuantity(value, WATTHOUR_PER_SQUAREMETRE)

    def toKiloWattHourPerSquareMetre: ComparableQuantity[Irradiation] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_SQUAREMETRE)

    /* ==== Power ==== */

    def toVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, VOLTAMPERE)

    def toKiloVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOVOLTAMPERE)

    def toMegaVoltAmpere: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAVOLTAMPERE)

    def toVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, VAR)

    def toKiloVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOVAR)

    def toMegaVar: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAVAR)

    def toWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, WATT)

    def toKiloWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, KILOWATT)

    def toMegaWatt: ComparableQuantity[Power] =
      Quantities.getQuantity(value, MEGAWATT)

    def toWattPerSquareMetre: ComparableQuantity[Irradiance] =
      Quantities.getQuantity(value, WATT_PER_SQUAREMETRE)

    def toKiloWattPerSquareMetre: ComparableQuantity[Irradiance] =
      Quantities.getQuantity(value, KILOWATT_PER_SQUAREMETRE)

    /* ==== Composed units ==== */

    def toPercentPerHour: ComparableQuantity[DimensionlessRate] =
      Quantities.getQuantity( value, PERCENT_PER_HOUR)

    def toPuPerHour: ComparableQuantity[DimensionlessRate] =
      Quantities.getQuantity(value, PU_PER_HOUR)

    /* ==== Basic electric units ==== */

    def toKiloVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, KILOVOLT)

    def toMegaVolt: ComparableQuantity[ElectricPotential] =
      Quantities.getQuantity(value, MEGAVOLT)

    def toOhmPerKilometre: ComparableQuantity[SpecificResistance] =
      Quantities.getQuantity(value, OHM_PER_KILOMETRE)

    def toSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      Quantities.getQuantity(value, SIEMENS_PER_KILOMETRE)

    def toMicroSiemensPerKilometre: ComparableQuantity[SpecificConductance] =
      Quantities.getQuantity(value, MetricPrefix.MICRO(SIEMENS_PER_KILOMETRE))

    def toFarradPerMetre: ComparableQuantity[SpecificCapacitance] =
      Quantities.getQuantity(value, FARAD_PER_METRE)

    def toMicroFarradPerKilometre: ComparableQuantity[SpecificCapacitance] =
      Quantities.getQuantity(value, MICROFARAD_PER_KILOMETRE)

    def toKiloWattHourPerKelvin: ComparableQuantity[HeatCapacity] = {
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN)
    }

    def toKiloWattHourPerKelvinTimesCubicMetre: ComparableQuantity[SpecificHeatCapacity] =
      Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE)

    /* ==== Thermal Conductance ==== */

    def toKiloWattPerKelvin: ComparableQuantity[ThermalConductance] =
      Quantities.getQuantity(value, KILOWATT_PER_KELVIN)

  }
}
