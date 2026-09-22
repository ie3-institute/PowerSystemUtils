/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.quantities.PowerSystemUnits.*
import edu.ie3.util.quantities.QuantityMatchers.equalWithTolerance
import edu.ie3.util.quantities.QuantityUtils.*
import edu.ie3.util.quantities.electro.*
import edu.ie3.util.quantities.energy.*
import edu.ie3.util.quantities.prices.*
import edu.ie3.util.quantities.radio.{
  KilowattHoursPerSquareMeter,
  WattHoursPerSquareMeter
}
import edu.ie3.util.quantities.thermal.{
  KilowattHoursPerKelvin,
  KilowattHoursPerKelvinTimesCubicMeters,
  KilowattsPerKelvin
}
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpecLike
import squants.*
import squants.electro.*
import squants.energy.*
import squants.market.EUR
import squants.mass.KilogramsPerCubicMeter
import squants.radio.WattsPerSquareMeter
import squants.space.*
import squants.thermal.Celsius
import squants.time.{Hours, Milliseconds, Minutes}
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.*

import scala.math.BigDecimal.RoundingMode

class QuantityUtilsSpec
    extends Matchers
    with SquantsMatchers
    with AnyWordSpecLike
    with TableDrivenPropertyChecks {
  "The quantity utils" when {
    given quantityTolerance: Double = 1e-9

    "rounding a quantity" should {
      val qty = 10.1245.asAmpere

      "round a quantity half up by default" in {
        qty.round(2) should equalWithTolerance(10.12.asAmpere)
        qty.round(3) should equalWithTolerance(10.125.asAmpere)
      }

      "round a quantity with the given rounding mode" in {
        qty.round(3, RoundingMode.HALF_DOWN) should equalWithTolerance(
          10.124.asAmpere
        )
      }
    }

    "converting a double" should {
      val value = 10.123154122

      /* javax measure units */

      "convert a double to a percent quantity quantity" in {
        value.asPercent should equalWithTolerance(
          Quantities.getQuantity(value, PERCENT)
        )

        value.percent should approximate(Percent(value))(using
          Percent(quantityTolerance)
        )
      }

      /* indriya units */

      "convert a double to a volt quantity" in {
        value.asVolt should equalWithTolerance(
          Quantities.getQuantity(value, VOLT)
        )

        value.volt should approximate(Volts(value))(using
          Volts(quantityTolerance)
        )
      }

      "convert a double to ampere quantity" in {
        value.asAmpere should equalWithTolerance(
          Quantities.getQuantity(
            value,
            AMPERE
          )
        )

        value.ampere should approximate(Amperes(value))(using
          Amperes(quantityTolerance)
        )
      }

      "convert a double to a kilo ampere quantity" in {
        value.asKiloAmpere should equalWithTolerance(
          Quantities.getQuantity(value, KILOAMPERE)
        )

        value.kiloAmpere should approximate(Kiloamperes(value))(using
          Kiloamperes(quantityTolerance)
        )
      }

      "convert a double to a nano siemens quantity" in {
        value.asNanoSiemens should equalWithTolerance(
          Quantities.getQuantity(value, NANOSIEMENS)
        )

        value.nanoSiemens should approximate(Nanosiemens(value))(using
          Nanosiemens(quantityTolerance)
        )
      }

      "convert a double to a siemens quantity" in {
        value.asSiemens should equalWithTolerance(
          Quantities.getQuantity(value, SIEMENS)
        )

        value.siemens should approximate(Siemens(value))(using
          Siemens(quantityTolerance)
        )
      }

      "convert a double to a milli ohm quantity" in {
        value.asMilliOhm should equalWithTolerance(
          Quantities.getQuantity(value, MILLIOHM)
        )

        value.milliOhm should approximate(Milliohms(value))(using
          Milliohms(quantityTolerance)
        )
      }

      "convert a double to an ohm quantity" in {
        value.asOhm should equalWithTolerance(
          Quantities.getQuantity(value, OHM)
        )

        value.ohm should approximate(Ohms(value))(using Ohms(quantityTolerance))
      }

      /* PowerSystemUnits */

      /* ==== Basic non-electric units ==== */

      "convert a double to a metre quantity" in {
        value.asMetre should equalWithTolerance(
          Quantities.getQuantity(value, METRE)
        )

        value.meter should approximate(Meters(value))(using
          Meters(quantityTolerance)
        )
      }

      "convert a double to a kilometre quantity" in {
        value.asKilometre should equalWithTolerance(
          Quantities.getQuantity(value, KILOMETRE)
        )

        value.kilometer should approximate(Kilometers(value))(using
          Kilometers(quantityTolerance)
        )
      }

      "convert a double to a millimetre quantity" in {
        value.asMillimetre should equalWithTolerance(
          Quantities.getQuantity(value, MILLIMETRE)
        )

        value.milliMeter should approximate(Millimeters(value))(using
          Millimeters(quantityTolerance)
        )
      }

      "convert a double to a millisecond quantity" in {
        value.asMillisecond should equalWithTolerance(
          Quantities.getQuantity(value, MILLISECOND)
        )

        value.milliSecond should approximate(Milliseconds(value))(using
          Milliseconds(quantityTolerance)
        )
      }

      "convert a double to a second quantity" in {
        value.asSecond should equalWithTolerance(
          Quantities.getQuantity(value, SECOND)
        )

        value.second should approximate(Seconds(value))(using
          Seconds(quantityTolerance)
        )
      }

      "convert a double to a minute quantity" in {
        value.asMinute should equalWithTolerance(
          Quantities.getQuantity(value, MINUTE)
        )

        value.minute should approximate(Minutes(value))(using
          Minutes(quantityTolerance)
        )
      }

      "convert a double to an hour quantity" in {
        value.asHour should equalWithTolerance(
          Quantities.getQuantity(value, HOUR)
        )

        value.hour should approximate(Hours(value))(using
          Hours(quantityTolerance)
        )
      }

      "convert a double to a pu quantity" in {
        value.asPu should equalWithTolerance(Quantities.getQuantity(value, PU))

        value.pu should approximate(Each(value))(using Each(quantityTolerance))
      }

      "convert a double to a euro quantity" in {
        value.asEuro should equalWithTolerance(
          Quantities.getQuantity(value, EURO)
        )

        value.euro should approximate(EUR(value))(using EUR(quantityTolerance))
      }

      "convert a double to a euro per kilometre quantity" in {
        value.asEuroPerKilometre should equalWithTolerance(
          Quantities.getQuantity(value, EURO_PER_KILOMETRE)
        )

        value.euroPerKilometer should approximate(EuroPerKilometers(value))(
          using EuroPerKilometers(quantityTolerance)
        )
      }

      "convert a double to a euro per watt hour quantity" in {
        value.asEuroPerWattHour should equalWithTolerance(
          Quantities.getQuantity(value, EURO_PER_WATTHOUR)
        )

        value.euroPerWattHour should approximate(EuroPerWattHours(value))(using
          EuroPerWattHours(quantityTolerance)
        )
      }

      "convert a double to a euro per kilo watt hour quantity" in {
        value.asEuroPerKiloWattHour should equalWithTolerance(
          Quantities.getQuantity(value, EURO_PER_KILOWATTHOUR)
        )

        value.euroPerKiloWattHour should approximate(
          EuroPerKilowattHours(value)
        )(using EuroPerKilowattHours(quantityTolerance))
      }

      "convert a double to a euro per megawatt quantity" in {
        value.asEuroPerMegaWattHour should equalWithTolerance(
          Quantities.getQuantity(value, EURO_PER_MEGAWATTHOUR)
        )

        value.euroPerMegaWattHour should approximate(
          EuroPerMegawattHours(value)
        )(using EuroPerMegawattHours(quantityTolerance))
      }

      "convert a double to a degree geom quantity" in {
        value.asDegreeGeom should equalWithTolerance(
          Quantities.getQuantity(value, DEGREE_GEOM)
        )

        value.degreeGeom should approximate(Degrees(value))(using
          Degrees(quantityTolerance)
        )
      }

      "convert a double to a kilogram per cubic meter quantity" in {
        value.asKilogramPerCubicMetre should equalWithTolerance(
          Quantities.getQuantity(value, KILOGRAM_PER_CUBIC_METRE)
        )

        value.kilogramPerCubicMeter should approximate(
          KilogramsPerCubicMeter(value)
        )(using KilogramsPerCubicMeter(quantityTolerance))
      }

      /* ==== Energy ==== */

      "convert a double to a watt hour quantity" in {
        value.asWattHour should equalWithTolerance(
          Quantities.getQuantity(value, WATTHOUR)
        )

        value.wattHour should approximate(WattHours(value))(using
          WattHours(quantityTolerance)
        )
      }

      "convert a double to a kilowatt-hour quantity" in {
        value.asKiloWattHour should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATTHOUR)
        )

        value.kilowattHours should approximate(KilowattHours(value))(using
          KilowattHours(quantityTolerance)
        )
      }

      "convert a double to a megawatt-hour quantity" in {
        value.asMegaWattHour should equalWithTolerance(
          Quantities.getQuantity(value, MEGAWATTHOUR)
        )

        value.megawattHour should approximate(MegawattHours(value))(using
          MegawattHours(quantityTolerance)
        )
      }

      "convert a double to a var-hour quantity" in {
        value.asVarHour should equalWithTolerance(
          Quantities.getQuantity(value, VARHOUR)
        )

        value.varHour should approximate(VarHours(value))(using
          VarHours(quantityTolerance)
        )
      }

      "convert a double to a kilovar-hour quantity" in {
        value.asKiloVarHour should equalWithTolerance(
          Quantities.getQuantity(value, KILOVARHOUR)
        )

        value.kilovarHour should approximate(KilovarHours(value))(using
          KilovarHours(quantityTolerance)
        )
      }

      "convert a double to a megavar-hour quantity" in {
        value.asMegaVarHour should equalWithTolerance(
          Quantities.getQuantity(value, MEGAVARHOUR)
        )

        value.megavarHour should approximate(MegavarHours(value))(using
          MegavarHours(quantityTolerance)
        )
      }

      "convert a double to a watt-hour per metre quantity" in {
        value.asWattHourPerMetre should equalWithTolerance(
          Quantities.getQuantity(value, WATTHOUR_PER_METRE)
        )
      }

      "convert a double to a kilowatt-hour per kilometre quantity" in {
        value.asKiloWattHourPerKiloMetre should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATTHOUR_PER_KILOMETRE)
        )
      }

      "convert a double to a watt-hour per square metre quantity" in {
        value.asWattHourPerSquareMetre should equalWithTolerance(
          Quantities.getQuantity(value, WATTHOUR_PER_SQUAREMETRE)
        )

        value.wattHourPerSquareMeter should approximate(
          WattHoursPerSquareMeter(value)
        )(using WattHoursPerSquareMeter(quantityTolerance))
      }

      "convert a double to a kilowatt-hour per square metre quantity" in {
        value.asKiloWattHourPerSquareMetre should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATTHOUR_PER_SQUAREMETRE)
        )

        value.kilowattHourPerSquareMeter should approximate(
          KilowattHoursPerSquareMeter(value)
        )(using KilowattHoursPerSquareMeter(quantityTolerance))
      }

      /* ==== Power ==== */

      "convert a double to a volt-ampere quantity" in {
        value.asVoltAmpere should equalWithTolerance(
          Quantities.getQuantity(value, VOLTAMPERE)
        )

        value.voltampere should approximate(Voltamperes(value))(using
          Voltamperes(quantityTolerance)
        )
      }

      "convert a double to a kilovolt-ampere quantity" in {
        value.asKiloVoltAmpere should equalWithTolerance(
          Quantities.getQuantity(value, KILOVOLTAMPERE)
        )

        value.kilovoltampere should approximate(Kilovoltamperes(value))(using
          Kilovoltamperes(quantityTolerance)
        )
      }

      "convert a double to a megavolt-ampere quantity" in {
        value.asMegaVoltAmpere should equalWithTolerance(
          Quantities.getQuantity(value, MEGAVOLTAMPERE)
        )

        value.megavoltampere should approximate(Megavoltamperes(value))(using
          Megavoltamperes(quantityTolerance)
        )
      }

      "convert a double to a var quantity" in {
        value.asVar should equalWithTolerance(
          Quantities.getQuantity(value, VAR)
        )

        value.`var` should approximate(Vars(value))(using
          Vars(quantityTolerance)
        )
      }

      "convert a double to a kilovar quantity" in {
        value.asKiloVar should equalWithTolerance(
          Quantities.getQuantity(value, KILOVAR)
        )

        value.kilovar should approximate(Kilovars(value))(using
          Kilovars(quantityTolerance)
        )
      }

      "convert a double to a megavar quantity" in {
        value.asMegaVar should equalWithTolerance(
          Quantities.getQuantity(value, MEGAVAR)
        )

        value.megavar should approximate(Megavars(value))(using
          Megavars(quantityTolerance)
        )
      }

      "convert a double to a watt quantity" in {
        value.asWatt should equalWithTolerance(
          Quantities.getQuantity(value, WATT)
        )

        value.watt should approximate(Watts(value))(using
          Watts(quantityTolerance)
        )
      }

      "convert a double to a kilowatt quantity" in {
        value.asKiloWatt should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATT)
        )

        value.kilowatt should approximate(Kilowatts(value))(using
          Kilowatts(quantityTolerance)
        )
      }

      "convert a double to a megawatt quantity" in {
        value.asMegaWatt should equalWithTolerance(
          Quantities.getQuantity(value, MEGAWATT)
        )

        value.megawatt should approximate(Megawatts(value))(using
          Megawatts(quantityTolerance)
        )
      }

      "convert a double to a watt per square metre quantity" in {
        value.asWattPerSquareMetre should equalWithTolerance(
          Quantities.getQuantity(value, WATT_PER_SQUAREMETRE)
        )

        value.wattPerSquareMeter should approximate(WattsPerSquareMeter(value))(
          using WattsPerSquareMeter(quantityTolerance)
        )
      }

      "convert a double to a kilowatt per square metre quantity" in {
        value.asKiloWattPerSquareMetre should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATT_PER_SQUAREMETRE)
        )

        value.kilowattHourPerSquareMeter should approximate(
          KilowattHoursPerSquareMeter(value)
        )(using KilowattHoursPerSquareMeter(quantityTolerance))
      }

      /* ==== Composed units ==== */

      "convert a double to a percent per hour quantity" in {
        value.asPercentPerHour should equalWithTolerance(
          Quantities.getQuantity(value, PERCENT_PER_HOUR)
        )

        value.percentPerHour should approximate(PercentsPerHour(value))(using
          PercentsPerHour(quantityTolerance)
        )
      }

      "convert a double to a pu per hour quantity" in {
        value.asPuPerHour should equalWithTolerance(
          Quantities.getQuantity(value, PU_PER_HOUR)
        )

        value.puPerHour should approximate(PuPerHour(value))(using
          PuPerHour(quantityTolerance)
        )
      }

      /* ==== Basic electric units ==== */

      "convert a double to a kilovolt quantity" in {
        value.asKiloVolt should equalWithTolerance(
          Quantities.getQuantity(value, KILOVOLT)
        )

        value.kilovolt should approximate(Kilovolts(value))(using
          Kilovolts(quantityTolerance)
        )
      }

      "convert a double to a megavolt quantity" in {
        value.asMegaVolt should equalWithTolerance(
          Quantities.getQuantity(value, MEGAVOLT)
        )

        value.megavolt should approximate(Megavolts(value))(using
          Megavolts(quantityTolerance)
        )
      }

      "convert a double to a ohm per kilometre quantity" in {
        value.asOhmPerKilometre should equalWithTolerance(
          Quantities.getQuantity(value, OHM_PER_KILOMETRE)
        )

        value.ohmPerKilometer should approximate(OhmsPerKilometer(value))(using
          OhmsPerKilometer(quantityTolerance)
        )
      }

      "convert a double to a siemens per kilometre quantity" in {
        value.asSiemensPerKilometre should equalWithTolerance(
          Quantities.getQuantity(value, SIEMENS_PER_KILOMETRE)
        )

        value.siemensPerKilometer should approximate(
          SiemensPerKilometer(value)
        )(using SiemensPerKilometer(quantityTolerance))
      }

      "convert a double to a microsiemens per kilometre quantity" in {
        value.asMicroSiemensPerKilometre should equalWithTolerance(
          Quantities.getQuantity(value, MICRO_SIEMENS_PER_KILOMETRE)
        )

        value.microsiemensPerKilometer should approximate(
          MicrosiemensPerKilometer(value)
        )(using MicrosiemensPerKilometer(quantityTolerance))
      }

      "convert a double to a farrad per metre quantity" in {
        value.asFarradPerMetre should equalWithTolerance(
          Quantities.getQuantity(value, FARAD_PER_METRE)
        )

        value.faradPerMeter should approximate(FaradPerMeter(value))(
          using FaradPerMeter(quantityTolerance)
        )
      }

      "convert a double to a microfarrad per kilometre quantity" in {
        value.asMicroFarradPerKilometre should equalWithTolerance(
          Quantities.getQuantity(value, MICROFARAD_PER_KILOMETRE)
        )

        value.microfaradsPerKilometer should approximate(
          MicrofaradsPerKilometer(value)
        )(using MicrofaradsPerKilometer(quantityTolerance))
      }

      /* ==== Thermal ==== */

      "convert a double to kelvin quantity" in {
        value.asKelvin should equalWithTolerance(
          Quantities.getQuantity(
            value,
            KELVIN
          )
        )

        value.kelvin should approximate(Kelvin(value))(using
          Kelvin(quantityTolerance)
        )
      }

      "convert a double to a degree celsius quantity" in {
        value.asDegreeCelsius should equalWithTolerance(
          Quantities.getQuantity(
            value,
            CELSIUS
          )
        )

        value.celsius should approximate(Celsius(value))(using
          Celsius(quantityTolerance)
        )
      }

      "convert a double to a kilowatt per kelvin quantity" in {
        value.asKiloWattPerKelvin should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATT_PER_KELVIN)
        )

        value.kilowattPerKelvin should approximate(KilowattsPerKelvin(value))(
          using KilowattsPerKelvin(quantityTolerance)
        )
      }

      "convert a double to a kilowatthour per kelvin quantity" in {
        value.asKiloWattHourPerKelvin should equalWithTolerance(
          Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN)
        )

        value.kilowattHourPerKelvin should approximate(
          KilowattHoursPerKelvin(value)
        )(using KilowattHoursPerKelvin(quantityTolerance))
      }

      "convert a double to a kilowatthour per kelvin times cubic metre quantity" in {
        value.asKiloWattHourPerKelvinTimesCubicMetre should equalWithTolerance(
          Quantities.getQuantity(
            value,
            KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE
          )
        )

        value.kiloWattHourPerKelvinTimesCubicMeter should approximate(
          KilowattHoursPerKelvinTimesCubicMeters(value)
        )(using KilowattHoursPerKelvinTimesCubicMeters(quantityTolerance))
      }

      /* ==== Volume ==== */

      "convert a double to a cubic metre quantity" in {
        value.asCubicMetre should equalWithTolerance(
          Quantities.getQuantity(
            value,
            CUBIC_METRE
          )
        )

        value.cubicMeter should approximate(CubicMeters(value))(using
          CubicMeters(quantityTolerance)
        )
      }

      "convert a double to a litre quantity" in {
        value.asLitre should equalWithTolerance(
          Quantities.getQuantity(
            value,
            LITRE
          )
        )

        value.litre should approximate(Litres(value))(using
          Litres(quantityTolerance)
        )
      }
    }

    "extend the quantity functionality" should {

      "return min and max of quantities" in {

        val smaller = 1.asAmpere
        val bigger = 2.asAmpere

        smaller.min(bigger) shouldBe smaller
        bigger.min(smaller) shouldBe smaller
        smaller.max(bigger) shouldBe bigger
        bigger.max(smaller) shouldBe bigger

      }

    }

    "converting units to alternative units" should {
      "succeed if units are compatible" in {
        val cases = Table(
          ("sourceUnit", "targetUnit", "expectedUnit"),
          (VOLTAMPERE, WATT, WATT),
          (KILOVOLTAMPERE, WATT, KILOWATT),
          (MEGAVOLTAMPERE, WATT, MEGAWATT),
          (VAR, WATT, WATT),
          (KILOVAR, WATT, KILOWATT),
          (MEGAVAR, WATT, MEGAWATT),
          (VOLTAMPERE, VAR, VAR),
          (KILOVOLTAMPERE, VAR, KILOVAR),
          (MEGAVOLTAMPERE, VAR, MEGAVAR),
          (WATT, VAR, VAR),
          (KILOWATT, VAR, KILOVAR),
          (MEGAWATT, VAR, MEGAVAR),
          (VAR, VOLTAMPERE, VOLTAMPERE),
          (KILOVAR, VOLTAMPERE, KILOVOLTAMPERE),
          (MEGAVAR, VOLTAMPERE, MEGAVOLTAMPERE),
          (WATT, VOLTAMPERE, VOLTAMPERE),
          (KILOWATT, VOLTAMPERE, KILOVOLTAMPERE),
          (MEGAWATT, VOLTAMPERE, MEGAVOLTAMPERE)
        )

        forAll(cases) { (sourceUnit, targetUnit, expectedUnit) =>
          sourceUnit.toEquivalentIn(targetUnit) shouldBe expectedUnit
        }
      }
    }
  }
}
