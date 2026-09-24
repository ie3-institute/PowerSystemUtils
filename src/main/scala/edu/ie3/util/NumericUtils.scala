/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util

/** Provides scala [[Numeric]] to Java.
  */
object NumericUtils {

  val BigDecimalF: Numeric[BigDecimal] = Numeric.BigDecimalIsFractional
  val BigDecimalI: Numeric[BigDecimal] = Numeric.BigDecimalAsIfIntegral
  val BigInt: Numeric[BigInt] = Numeric.BigIntIsIntegral
  val Byte: Numeric[Byte] = Numeric.ByteIsIntegral
  val Char: Numeric[Char] = Numeric.CharIsIntegral
  val Double: Numeric[Double] = Numeric.DoubleIsFractional
  val Float: Numeric[Float] = Numeric.FloatIsFractional
  val Int: Numeric[Int] = Numeric.IntIsIntegral
  val Long: Numeric[Long] = Numeric.LongIsIntegral
  val Short: Numeric[Short] = Numeric.ShortIsIntegral
}
