package edu.ie3.util.quantities.electro

import squants.MetricSystem
import squants.electro.ElectricCurrentUnit

object KiloAmperes extends ElectricCurrentUnit {
  val symbol = "kA"
  val conversionFactor: Double = MetricSystem.Kilo
}

