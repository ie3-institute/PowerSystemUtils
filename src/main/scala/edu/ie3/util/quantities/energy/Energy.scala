package edu.ie3.util.quantities.energy

import squants.{MetricSystem, PrimaryUnit}
import squants.energy.{EnergyUnit, Watts}

object VarHours extends EnergyUnit {
  val symbol = "VArh"
}

object KilovarHours extends EnergyUnit {
  val conversionFactor: Double = Watts.conversionFactor * MetricSystem.Kilo
  val symbol = "kVArh"
}

object MegavarHours extends EnergyUnit {
  val conversionFactor: Double = Watts.conversionFactor * MetricSystem.Mega
  val symbol = "MVArh"
}