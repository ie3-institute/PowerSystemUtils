package edu.ie3.util.quantities.thermal

import squants.{MetricSystem, PrimaryUnit, SiUnit}
import squants.thermal.ThermalCapacityUnit
import squants.time.Time

object KilowattHourPerKelvin extends ThermalCapacityUnit {
  val symbol = "J/K"
  val conversionFactor: Double = Time.MillisecondsPerHour
}

