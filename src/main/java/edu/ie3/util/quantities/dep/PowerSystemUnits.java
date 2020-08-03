/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities.dep;

import edu.ie3.util.TimeUtil;
import edu.ie3.util.quantities.interfaces.*;
import java.util.HashSet;
import java.util.logging.Level;
import javax.measure.Unit;
import javax.measure.quantity.*;
import tec.uom.se.format.SimpleUnitFormat;
import tec.uom.se.function.MultiplyConverter;
import tec.uom.se.quantity.QuantityDimension;
import tec.uom.se.unit.*;

/**
 * Defines the physical units used in the simulation. They can be used like this: private
 * Quantity&lt;Power&gt; activePower = Quantities.getQuantity(5.0, Units.MW); When defining
 * interfaces via Quantities.getQuanitity make sure to always use a double ( e.g. 5.0, not just 5)
 * when needed or else calculations will always result in a integer. E.g.: 0.0 MW + 0.2 MW = 0.2 MW
 * but 0 MW + 0.2 MW = 0 MW
 *
 * @author roemer
 * @version 0.1
 * @since 17.07.2017
 */

/** @deprecated As of release 1.4, replaced by {@link edu.ie3.util.quantities.PowerSystemUnits} */
@Deprecated
public class PowerSystemUnits extends Units {

  /* ==== Basic non electric units ==== */
  /** Kilometre */
  public static final Unit<Length> KILOMETRE = MetricPrefix.KILO(METRE);

  /** Millisecond */
  public static final Unit<Time> MILLISECOND = MetricPrefix.MILLI(SECOND);

  /** Per Unit */
  public static final Unit<Dimensionless> PU =
      new TransformedUnit<>("p.u.", PERCENT, new MultiplyConverter(100));

  /** Euro */
  public static final Unit<Currency> EURO = new BaseUnit<>("€", QuantityDimension.NONE);

  /** Euro / km */
  public static final Unit<PricePerLength> EURO_PER_KILOMETRE =
      new ProductUnit<>(EURO.divide(MetricPrefix.KILO(METRE)));

  /** Euro / Wh */
  public static final Unit<EnergyPrice> EURO_PER_WATTHOUR =
      new ProductUnit<>(EURO.divide(Units.WATT.multiply(Units.HOUR)));

  /** Euro / kWh */
  public static final Unit<EnergyPrice> EURO_PER_KILOWATTHOUR =
      MetricPrefix.MILLI(EURO_PER_WATTHOUR);

  /** Euro / MWh */
  public static final Unit<EnergyPrice> EURO_PER_MEGAWATTHOUR =
      MetricPrefix.MICRO(EURO_PER_WATTHOUR);

  /** Degree */
  public static final Unit<Angle> DEGREE_GEOM = new BaseUnit<>("°");

  /* ==== Energy ==== */

  /** Watthour */
  public static final Unit<Energy> WATTHOUR =
      new TransformedUnit<>("Wh", JOULE, new MultiplyConverter(3600));

  public static final Unit<Energy> VARHOUR =
      new TransformedUnit<>("VArh", JOULE, new MultiplyConverter(3600));

  /** Kilowatthour */
  public static final Unit<Energy> KILOWATTHOUR = MetricPrefix.KILO(WATTHOUR);

  public static final Unit<Energy> KILOVARHOUR = MetricPrefix.KILO(VARHOUR);

  /** Watthour per metre */
  public static final Unit<SpecificEnergy> WATTHOUR_PER_METRE = new BaseUnit<>("Wh/m");

  /** Kilowatthour per Kilometre */
  public static final Unit<SpecificEnergy> KILOWATTHOUR_PER_KILOMETRE =
      new AlternateUnit<>(WATTHOUR_PER_METRE, "kWh/km");

  /** Watthour per squaremetre */
  public static final Unit<Irradiation> WATTHOUR_PER_SQUAREMETRE =
      new ProductUnit<>(WATT.multiply(HOUR).divide(SQUARE_METRE));

  /** Kilowatthour per squaremetre */
  public static final Unit<Irradiation> KILOWATTHOUR_PER_SQUAREMETRE =
      MetricPrefix.KILO(WATTHOUR_PER_SQUAREMETRE);

  /* ==== Power ==== */

  /** Volt ampere */
  public static final Unit<Power> VOLTAMPERE = new AlternateUnit<>(WATT, "VA");

  /** Kilovoltampere */
  public static final Unit<Power> KILOVOLTAMPERE = MetricPrefix.KILO(VOLTAMPERE);

  /** Megavoltampere */
  public static final Unit<Power> MEGAVOLTAMPERE = MetricPrefix.MEGA(VOLTAMPERE);

  /** Volt ampere reactive */
  public static final Unit<Power> VAR = new AlternateUnit<>(WATT, "VAr");

  /** Megavar */
  public static final Unit<Power> MEGAVAR = MetricPrefix.MEGA(VAR);

  /** Kilovar */
  public static final Unit<Power> KILOVAR = MetricPrefix.KILO(VAR);

  /** Kilowatt */
  public static final Unit<Power> KILOWATT = MetricPrefix.KILO(WATT);

  /** Megawatt */
  public static final Unit<Power> MEGAWATT = MetricPrefix.MEGA(WATT);

  /** Watt per square metre */
  public static final Unit<PowerDensity> WATT_PER_SQUAREMETRE =
      new ProductUnit<>(WATT.divide(SQUARE_METRE));

  /* ==== Composed units ==== */
  public static final Unit<DimensionlessRate> PERCENT_PER_HOUR =
      new ProductUnit<>(PERCENT.divide(HOUR));

  public static final Unit<DimensionlessRate> PU_PER_HOUR =
      new TransformedUnit<>("p.u./h", PERCENT_PER_HOUR, new MultiplyConverter(100));

  /* ==== Basic electric units ==== */

  /** Kilovolt */
  public static final Unit<ElectricPotential> KILOVOLT = MetricPrefix.KILO(VOLT);

  /** Megavolt */
  public static final Unit<ElectricPotential> MEGAVOLT = MetricPrefix.MEGA(VOLT);

  /** Ohm per kilometre */
  public static final Unit<SpecificResistance> OHM_PER_KILOMETRE =
      new ProductUnit<>(OHM.divide(KILOMETRE));

  /** Siemens per kilometre */
  public static final Unit<SpecificConductance> SIEMENS_PER_KILOMETRE =
      new ProductUnit<>(SIEMENS.divide(KILOMETRE));

  /** Micro Siemens per kilometre */
  public static final Unit<SpecificConductance> MICRO_SIEMENS_PER_KILOMETRE =
      new ProductUnit<>(MetricPrefix.MICRO(SIEMENS).divide(KILOMETRE));

  /** Farad per metre */
  public static final Unit<SpecificCapacitance> FARAD_PER_METRE = new BaseUnit<>("F/m");

  /** F / km */
  public static final Unit<SpecificCapacitance> FARAD_PER_KILOMETRE =
      new TransformedUnit<>("F/km", FARAD_PER_METRE, new MultiplyConverter(1 / 1E3));

  /** µF / km */
  public static final Unit<SpecificCapacitance> MICROFARAD_PER_KILOMETRE =
      new TransformedUnit<>("µF/km", FARAD_PER_KILOMETRE, new MultiplyConverter(1 / 1E6));

  /* ==== Heat Capacity ==== */
  /** kWh/K */
  public static final Unit<HeatCapacity> KILOWATTHOUR_PER_KELVIN =
      new ProductUnit<>(KILOWATTHOUR.divide(KELVIN));

  /** kWh/K*m³ */
  public static final Unit<SpecificHeatCapacity> KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE =
      new ProductUnit<>(KILOWATTHOUR_PER_KELVIN.divide(CUBIC_METRE));

  /* ==== Thermal Conductance ==== */

  /** kW/K */
  public static final Unit<ThermalConductance> KILOWATT_PER_KELVIN = new BaseUnit<>("kW/K");

  private static HashSet<String> registeredLabels = new HashSet<>();

  static {
    addUnit(WATTHOUR, "Wh");
    addUnit(WATTHOUR_PER_METRE, "Wh/m");
    addUnit(KILOWATTHOUR_PER_KILOMETRE, "kWh/km");
    addUnit(KILOWATTHOUR, "kWh");
    addUnit(OHM_PER_KILOMETRE, "Ω/km");
    addUnit(SIEMENS_PER_KILOMETRE, "S/km");
    addUnit(VOLTAMPERE, "VA");
    addUnit(KILOVOLTAMPERE, "kVA");
    addUnit(MEGAVOLTAMPERE, "MVA");
    addUnit(WATT_PER_SQUAREMETRE, "W/m²");
    addUnit(PERCENT_PER_HOUR, "%/h");
    addUnit(PU_PER_HOUR, "p.u./h");
    addUnit(VAR, "VAr");
    addUnit(KILOVAR, "kVAr");
    addUnit(MEGAVAR, "MVAr");
    addUnit(PU, "PU");
    addUnit(EURO, "EUR");
    addUnit(EURO_PER_KILOMETRE, "EUR/km");
    addUnit(EURO_PER_WATTHOUR, "EUR/Wh");
    addUnit(EURO_PER_KILOWATTHOUR, "EUR/kWh");
    addUnit(EURO_PER_MEGAWATTHOUR, "EUR/MWh");
    addUnit(FARAD_PER_METRE, "F/m");
    addUnit(MICROFARAD_PER_KILOMETRE, "µF/km");
    addUnit(FARAD_PER_KILOMETRE, "F/km");
    addUnit(DEGREE_GEOM, "°");
    addUnit(KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE, "kWh/K*m³");
    addUnit(KILOWATT_PER_KELVIN, "kW/K");
  }

  /**
   * Units must be registered via this method or they cannot be serialized/deserialized! If the
   * return-value is null, the unit was already registered
   */
  private static Unit<?> addUnit(Unit<?> unit, String label) {
    if (registeredLabels.contains(label)) {
      logger.log(Level.FINE, "Label {} is already registered. Ignoring", label);
      return null;
    }

    SimpleUnitFormat.getInstance().label(unit, label);
    return unit;
  }
}
