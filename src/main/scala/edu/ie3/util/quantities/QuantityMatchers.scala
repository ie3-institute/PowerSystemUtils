/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.MatcherUtils

import javax.measure.Quantity
import org.scalatest.matchers.{MatchResult, Matcher}

/** Trait, to simplify test coding, that is reliant on [[Quantity]] s
  */
trait QuantityMatchers {
  class QuantityMatcher[Q <: Quantity[Q]](right: Quantity[Q], tolerance: Double)
      extends Matcher[Quantity[Q]]
      with QuantityMatchers
      with MatcherUtils {
    override def apply(left: Quantity[Q]): MatchResult = MatchResult(
      QuantityUtil.equals(left, right, tolerance),
      assembleRawFailureMessage(left, right, tolerance, "quantity"),
      assembleNegatedFailureMessage(left, right, tolerance, "quantity")
    )
  }

  def equalWithTolerance[Q <: Quantity[Q]](
      right: Quantity[Q]
  )(implicit quantityTolerance: Double = 1e-10) =
    new QuantityMatcher(right, quantityTolerance)
}
