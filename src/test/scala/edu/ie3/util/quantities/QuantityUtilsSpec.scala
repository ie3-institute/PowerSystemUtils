/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.quantities

import edu.ie3.util.quantities.PowerSystemUnits._
import edu.ie3.util.quantities.QuantityMatchers.equalWithTolerance
import edu.ie3.util.quantities.QuantityUtils.RichQuantityDouble
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units._

import javax.measure.MetricPrefix

class QuantityUtilsSpec extends Matchers with AnyWordSpecLike {
  "A rich quantity util" should {
    implicit val quantityTolerance: Double = 1e-9
    val value = 10.123154122

    /* javax measure units */

    "convert a double to a percent quantity quantity" in {
      value.asPercent should equalWithTolerance(
        Quantities.getQuantity(value, PERCENT)
      )
    }

    /* indriya units */

    "convert a double to a volt quantity" in {
      value.asVolt should equalWithTolerance(
        Quantities.getQuantity(value, VOLT)
      )
    }
    
    "convert a double to ampere quantity" in {
      value.asAmpere should equalWithTolerance(
          Quantities.getQuantity(
            value,
            AMPERE
      )
      )
    }

    "convert a double to a kilo ampere quantity" in {
      value.asKiloAmpere should equalWithTolerance(
          Quantities.getQuantity(
            value,
            MetricPrefix.KILO(AMPERE)
      )
      )
    }

    "convert a double to a nano siemens quantity" in {
      value.asNanoSiemens should equalWithTolerance(
        Quantities.getQuantity(value, MetricPrefix.NANO(SIEMENS))
      )
    }

    "convert a double to a siemens quantity" in {
      value.asSiemens should equalWithTolerance(
          Quantities.getQuantity(value, SIEMENS)
      )
    }

    "convert a double to a milli ohm quantity" in {
      value.asMilliOhm should equalWithTolerance(
        Quantities.getQuantity(value, MetricPrefix.MILLI(OHM))
      )
    }

    "convert a double to an ohm quantity" in {
      value.asOhm should equalWithTolerance(
          Quantities.getQuantity(
            value,
            OHM
      )
      )
    }

    /* PowerSystemUnits */

    /* ==== Basic non electric units ==== */

    "convert a double to a kilometre quantity" in {
      value.asKilometre should equalWithTolerance(Quantities.getQuantity(value, KILOMETRE))
    }

    "convert a double to a millisecond quantity" in {
      value.asMillisecond should equalWithTolerance(Quantities.getQuantity(value, MILLISECOND))
    }
    
    "convert a double to a pu quantity" in {
      value.asPu should equalWithTolerance(Quantities.getQuantity(value, PU))
    }
    
    "convert a double to a euro quantity" in {
      value.asEuro should equalWithTolerance(Quantities.getQuantity(value, EURO))
    }

    "convert a double to a euro per kilometre quantity" in {
      value.asEuroPerKilometre should equalWithTolerance(Quantities.getQuantity(value, EURO_PER_KILOMETRE))
    }

    "convert a double to a euro per watt hour quantity" in {
      value.asEuroPerWattHour should equalWithTolerance(Quantities.getQuantity(value, EURO_PER_WATTHOUR))
    }

    "convert a double to a euro per kilo watt hour quantity" in {
      value.asEuroPerKiloWattHour should equalWithTolerance(Quantities.getQuantity(value, EURO_PER_KILOWATTHOUR))
    }

    "convert a double to a euro per megawatt quantity" in {
      value.asEuroPerMegaWattHour should equalWithTolerance(Quantities.getQuantity(value, EURO_PER_MEGAWATTHOUR))
    }

    "convert a double to a degree geom quantity" in {
      value.asDegreeGeom should equalWithTolerance(
        Quantities.getQuantity(value, DEGREE_GEOM)
      )
    }

    "convert a double to a kilogram per cubic meter quantity" in {
      value.asKilogramPerCubicMetre should equalWithTolerance(Quantities.getQuantity(value, KILOGRAM_PER_CUBIC_METRE))
    }

    /* ==== Energy ==== */

    "convert a double to a watt hour quantity" in {
      value.asWattHour should equalWithTolerance(Quantities.getQuantity(value, WATTHOUR))
    }

    "convert a double to a kilowatt-hour quantity" in {
      value.asKiloWattHour should equalWithTolerance(Quantities.getQuantity(value, KILOWATTHOUR))
    }

    "convert a double to a var-hour quantity" in {
      value.asVarHour should equalWithTolerance(Quantities.getQuantity(value, VARHOUR))
    }

    "convert a double to a kilovar-hour quantity" in {
      value.asKiloVarHour should equalWithTolerance(Quantities.getQuantity(value, KILOVARHOUR))
    }

    "convert a double to a watt-hour per square metre quantity" in {
      value.asWattHourPerSquareMetre should equalWithTolerance(Quantities.getQuantity(value, WATTHOUR_PER_SQUAREMETRE))
    }

    "convert a double to a kilowatt-hour per square metre quantity" in {
      value.asKiloWattHourPerSquareMetre should equalWithTolerance(Quantities.getQuantity(value, KILOWATTHOUR_PER_SQUAREMETRE))
    }

    /* ==== Power ==== */

    "convert a double to a volt-ampere quantity" in {
      value.asVoltAmpere should equalWithTolerance(Quantities.getQuantity(value, VOLTAMPERE))
    }

    "convert a double to a kilovolt-ampere quantity" in {
      value.asKiloVoltAmpere should equalWithTolerance(Quantities.getQuantity(value, KILOVOLTAMPERE))
    }

    "convert a double to a megavolt-ampere quantity" in {
      value.asMegaVoltAmpere should equalWithTolerance(Quantities.getQuantity(value, MEGAVOLTAMPERE))
    }

    "convert a double to a var quantity" in {
      value.asVar should equalWithTolerance(Quantities.getQuantity(value, VAR))
    }

    "convert a double to a kilovar quantity" in {
      value.asKiloVar should equalWithTolerance(Quantities.getQuantity(value, KILOVAR))
    }

    "convert a double to a megavar quantity" in {
      value.asMegaVar should equalWithTolerance(Quantities.getQuantity(value, MEGAVAR))
    }

    "convert a double to a watt quantity" in {
      value.asWatt should equalWithTolerance(Quantities.getQuantity(value, WATT))
    }

    "convert a double to a kilowatt quantity" in {
      value.asKiloWatt should equalWithTolerance(Quantities.getQuantity(value, KILOWATT))
    }

    "convert a double to a megawatt quantity" in {
      value.asMegaWatt should equalWithTolerance(Quantities.getQuantity(value, MEGAWATT))
    }

    "convert a double to a watt per square metre quantity" in {
      value.asWattPerSquareMetre should equalWithTolerance(Quantities.getQuantity(value, WATT_PER_SQUAREMETRE))
    }

    "convert a double to a kilowatt per square metre quantity" in {
      value.asKiloWattPerSquareMetre should equalWithTolerance(Quantities.getQuantity(value, KILOWATT_PER_SQUAREMETRE))
    }

    /* ==== Composed units ==== */

    "convert a double to a percent per hour quantity" in {
      value.asPercentPerHour should equalWithTolerance(Quantities.getQuantity(value, PERCENT_PER_HOUR))
    }

    "convert a double to a pu per hour quantity" in {
      value.asPuPerHour should equalWithTolerance(Quantities.getQuantity(value, PU_PER_HOUR))
    }

    /* ==== Basic electric units ==== */

    "convert a double to a kilovolt quantity" in {
      value.asKiloVolt should equalWithTolerance(Quantities.getQuantity(value, KILOVOLT))
    }

    "convert a double to a megavolt quantity" in {
      value.asMegaVolt should equalWithTolerance(Quantities.getQuantity(value, MEGAVOLT))
    }

    "convert a double to a ohm per kilometre quantity" in {
      value.asOhmPerKilometre should equalWithTolerance(Quantities.getQuantity(value, OHM_PER_KILOMETRE))
    }

    "convert a double to a siemens per kilometre quantity" in {
      value.asSiemensPerKilometre should equalWithTolerance(Quantities.getQuantity(value, SIEMENS_PER_KILOMETRE))
    }

    "convert a double to a microsiemens per kilometre quantity" in {
      value.asMicroSiemensPerKilometre should equalWithTolerance(Quantities.getQuantity(value, MICRO_SIEMENS_PER_KILOMETRE))
    }

    "convert a double to a farrad per metre quantity" in {
      value.asFarradPerMetre should equalWithTolerance(Quantities.getQuantity(value, FARAD_PER_METRE))
    }

    "convert a double to a microfarrad per kilometre quantity" in {
      value.asMicroFarradPerKilometre should equalWithTolerance(Quantities.getQuantity(value, MICROFARAD_PER_KILOMETRE))
    }

    "convert a double to a kilowatthour per kelvin quantity" in {
      value.asKiloWattHourPerKelvin should equalWithTolerance(Quantities.getQuantity(value, KILOWATTHOUR_PER_KELVIN))
    }

    /* ==== Thermal Conductance ==== */

    "convert a double to a kilowatt per kelvin quantity" in {
      value.asKiloWattPerKelvin should equalWithTolerance(Quantities.getQuantity(value, KILOWATT_PER_KELVIN))
    }

  }
}
