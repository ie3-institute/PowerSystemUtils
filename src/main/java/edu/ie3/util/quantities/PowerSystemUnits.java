/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities;

import static javax.measure.MetricPrefix.*;
import static tech.units.indriya.AbstractUnit.ONE;

import edu.ie3.util.quantities.interfaces.*;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.quantity.*;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.*;
import tech.units.indriya.unit.Units;

/**
 * Defines the physical units used in the simulation. They can be used like this: private
 * Quantity&lt;Power&gt; activePower = Quantities.getQuantity(5.0, Units.MW); When defining
 * interfaces via Quantities.getQuantity make sure to always use a double ( e.g. 5.0, not just 5)
 * when needed or else calculations will always result in an integer. E.g.: 0.0 MW + 0.2 MW = 0.2 MW
 * but 0 MW + 0.2 MW = 0 MW
 *
 * @author roemer
 * @version 0.1
 * @since 17.07.2017
 */
public class PowerSystemUnits extends Units {

  private static final Logger logger = Logger.getLogger(PowerSystemUnits.class.getName());

  /* ==== Basic non electric units ==== */
  /** Kilometre */
  public static final Unit<Length> KILOMETRE = MetricPrefixDouble.prefix(KILO, METRE);

  /** Millisecond */
  public static final Unit<Time> MILLISECOND = MetricPrefixDouble.prefix(MILLI, SECOND);

  /** Per Unit */
  public static final Unit<Dimensionless> PU = new AlternateUnit<>(ONE, "p.u.");

  /** Euro */
  public static final Unit<Currency> EURO = new BaseUnit<>("€", UnitDimension.NONE);

  /** Euro / km */
  public static final Unit<PricePerLength> EURO_PER_KILOMETRE =
      new ProductUnit<>(EURO.divide(KILOMETRE));

  /** Euro / Wh */
  public static final Unit<EnergyPrice> EURO_PER_WATTHOUR =
      new ProductUnit<>(EURO.divide(WATT.multiply(HOUR)));

  /** Euro / kWh */
  public static final Unit<EnergyPrice> EURO_PER_KILOWATTHOUR =
      new TransformedUnit<>("€/kWh", EURO_PER_WATTHOUR, MultiplyConverter.of(1E-3));

  /** Euro / MWh */
  public static final Unit<EnergyPrice> EURO_PER_MEGAWATTHOUR =
      new TransformedUnit<>("€/MWh", EURO_PER_WATTHOUR, MultiplyConverter.of(1E-6));

  /** Degree */
  public static final Unit<Angle> DEGREE_GEOM =
      new TransformedUnit<>("°", RADIAN, MultiplyConverter.of(Math.toRadians(1.0)));

  /** Density */
  public static final Unit<Density> KILOGRAM_PER_CUBIC_METRE =
      new ProductUnit<>(KILOGRAM.divide(CUBIC_METRE));

  /* ==== Energy ==== */

  /** Watthour */
  public static final Unit<Energy> WATTHOUR =
      new TransformedUnit<>("Wh", JOULE, MultiplyConverter.of(3600));

  public static final Unit<Energy> VARHOUR =
      new TransformedUnit<>("varh", JOULE, MultiplyConverter.of(3600));

  /** Kilowatthour */
  public static final Unit<Energy> KILOWATTHOUR = MetricPrefixDouble.prefix(KILO, WATTHOUR);

  public static final Unit<Energy> KILOVARHOUR = MetricPrefixDouble.prefix(KILO, VARHOUR);

  /** Megawatthour */
  public static final Unit<Energy> MEGAWATTHOUR = MetricPrefixDouble.prefix(MEGA, WATTHOUR);

  public static final Unit<Energy> MEGAVARHOUR = MetricPrefixDouble.prefix(MEGA, VARHOUR);

  /** Watthour per metre */
  public static final Unit<SpecificEnergy> WATTHOUR_PER_METRE =
      new ProductUnit<>(WATTHOUR.divide(METRE));

  /** Kilowatthour per Kilometre */
  public static final Unit<SpecificEnergy> KILOWATTHOUR_PER_KILOMETRE =
      new TransformedUnit<>("kWh/km", WATTHOUR_PER_METRE, MultiplyConverter.of(1d));

  /** Watthour per squaremetre */
  public static final Unit<Irradiation> WATTHOUR_PER_SQUAREMETRE =
      new ProductUnit<>(WATT.multiply(HOUR).divide(SQUARE_METRE));

  /** Kilowatthour per squaremetre */
  public static final Unit<Irradiation> KILOWATTHOUR_PER_SQUAREMETRE =
      MetricPrefixDouble.prefix(KILO, WATTHOUR_PER_SQUAREMETRE);

  /* ==== Power ==== */

  /** Volt ampere */
  public static final Unit<Power> VOLTAMPERE = new AlternateUnit<>(WATT, "VA");

  /** Kilovoltampere */
  public static final Unit<Power> KILOVOLTAMPERE = MetricPrefixDouble.prefix(KILO, VOLTAMPERE);

  /** Megavoltampere */
  public static final Unit<Power> MEGAVOLTAMPERE = MetricPrefixDouble.prefix(MEGA, VOLTAMPERE);

  /** Voltampere reactive */
  public static final Unit<Power> VAR = new AlternateUnit<>(WATT, "var");

  /** Megavar */
  public static final Unit<Power> MEGAVAR = MetricPrefixDouble.prefix(MEGA, VAR);

  /** Kilovar */
  public static final Unit<Power> KILOVAR = MetricPrefixDouble.prefix(KILO, VAR);

  /** Kilowatt */
  public static final Unit<Power> KILOWATT = MetricPrefixDouble.prefix(KILO, WATT);

  /** Megawatt */
  public static final Unit<Power> MEGAWATT = MetricPrefixDouble.prefix(MEGA, WATT);

  /** Watt per square metre */
  public static final Unit<Irradiance> WATT_PER_SQUAREMETRE =
      new ProductUnit<>(WATT.divide(SQUARE_METRE));

  /** Kilowatt per square metre */
  public static final Unit<Irradiance> KILOWATT_PER_SQUAREMETRE =
      new ProductUnit<>(KILOWATT.divide(SQUARE_METRE));

  /* ==== Composed units ==== */
  public static final Unit<DimensionlessRate> PERCENT_PER_HOUR =
      new ProductUnit<>(PERCENT.divide(HOUR));

  public static final Unit<DimensionlessRate> PU_PER_HOUR =
      new TransformedUnit<>("p.u./h", PERCENT_PER_HOUR, MultiplyConverter.of(100));

  /* ==== Basic electric units ==== */

  /** Kilovolt */
  public static final Unit<ElectricPotential> KILOVOLT = MetricPrefixDouble.prefix(KILO, VOLT);

  /** Megavolt */
  public static final Unit<ElectricPotential> MEGAVOLT = MetricPrefixDouble.prefix(MEGA, VOLT);

  /** Ohm per kilometre */
  public static final Unit<SpecificResistance> OHM_PER_KILOMETRE =
      new ProductUnit<>(OHM.divide(KILOMETRE));

  /** Siemens per kilometre */
  public static final Unit<SpecificConductance> SIEMENS_PER_KILOMETRE =
      new ProductUnit<>(SIEMENS.divide(KILOMETRE));

  /** Micro Siemens per kilometre */
  public static final Unit<SpecificConductance> MICRO_SIEMENS_PER_KILOMETRE =
      new ProductUnit<>(MetricPrefixDouble.prefix(MICRO, SIEMENS).divide(KILOMETRE));

  /** Farad per metre */
  public static final Unit<SpecificCapacitance> FARAD_PER_METRE =
      new ProductUnit<>(FARAD.divide(METRE));

  /** F / km */
  public static final Unit<SpecificCapacitance> FARAD_PER_KILOMETRE =
      new TransformedUnit<>("F/km", FARAD_PER_METRE, MultiplyConverter.of(1 / 1E3));

  /** µF / km */
  public static final Unit<SpecificCapacitance> MICROFARAD_PER_KILOMETRE =
      new TransformedUnit<>("µF/km", FARAD_PER_KILOMETRE, MultiplyConverter.of(1 / 1E6));

  /* ==== Heat Capacity ==== */
  /** kWh/K */
  public static final Unit<HeatCapacity> KILOWATTHOUR_PER_KELVIN =
      new ProductUnit<>(KILOWATTHOUR.divide(KELVIN));

  /** kWh/K*m³ */
  public static final Unit<SpecificHeatCapacity> KILOWATTHOUR_PER_KELVIN_TIMES_CUBICMETRE =
      new ProductUnit<>(KILOWATTHOUR_PER_KELVIN.divide(CUBIC_METRE));

  /* ==== Thermal Conductance ==== */

  /** kW/K */
  public static final Unit<ThermalConductance> KILOWATT_PER_KELVIN =
      new ProductUnit<>(KILOWATT.divide(KELVIN));

  private static final HashSet<String> REGISTERED_LABELS = new HashSet<>();

  static {
    addUnit(KILOWATT, "kW");
    addUnit(WATTHOUR, "Wh");
    addUnit(WATTHOUR_PER_METRE, "Wh/m");
    addUnit(KILOWATTHOUR_PER_KILOMETRE, "kWh/km");
    addUnit(KILOWATTHOUR, "kWh");
    addUnit(MEGAWATTHOUR, "MWh");
    addUnit(OHM_PER_KILOMETRE, "Ω/km");
    addUnit(SIEMENS_PER_KILOMETRE, "S/km");
    addUnit(VOLTAMPERE, "VA");
    addUnit(KILOVOLTAMPERE, "kVA");
    addUnit(MEGAVOLTAMPERE, "MVA");
    addUnit(WATT_PER_SQUAREMETRE, "W/m²");
    addUnit(PERCENT_PER_HOUR, "%/h");
    addUnit(PU_PER_HOUR, "p.u./h");
    addUnit(VAR, "var");
    addUnit(KILOVAR, "kvar");
    addUnit(MEGAVAR, "Mvar");
    addUnit(VARHOUR, "varh");
    addUnit(KILOVARHOUR, "kvarh");
    addUnit(MEGAVARHOUR, "Mvarh");
    addUnit(PU, "p.u.");
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
    addUnit(KILOGRAM_PER_CUBIC_METRE, "kg/m³");
  }

  /**
   * Units must be registered via this method or they cannot be serialized/deserialized! If the
   * return-value is null, the unit was already registered
   */
  private static void addUnit(Unit<?> unit, String label) {
    if (REGISTERED_LABELS.contains(label)) {
      logger.log(Level.FINE, "Label {} is already registered. Ignoring", label);
    }

    SimpleUnitFormat.getInstance().label(unit, label);
    REGISTERED_LABELS.add(label);
  }
}
