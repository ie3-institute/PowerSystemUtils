/*
 * © 2024. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.quantities

import edu.ie3.util.MatcherUtils
import org.scalatest.matchers.{MatchResult, Matcher}
import squants.Quantity

/** Trait, to simplify test coding, that is reliant on squants */
trait SquantsMatchers {
  class SquantsMatcher[Q <: Quantity[Q]](right: Q)(using tolerance: Q)
      extends Matcher[Quantity[Q]]
      with MatcherUtils {
    override def apply(left: Quantity[Q]): MatchResult = MatchResult(
      left =~ right,
      assembleRawFailureMessage(left, right, tolerance, "quantity"),
      assembleNegatedFailureMessage(left, right, tolerance, "quantity")
    )
  }

  class OptionalSquantsMatcher[Q <: Quantity[Q]](
      right: Option[Q]
  )(using
      tolerance: Q
  ) extends Matcher[Option[Q]]
      with MatcherUtils {
    override def apply(left: Option[Q]): MatchResult = {
      (left, right) match {
        case (Some(leftValue), Some(rightValue)) =>
          MatchResult(
            leftValue =~ rightValue,
            assembleRawFailureMessage(left, right, tolerance, "quantity"),
            assembleNegatedFailureMessage(left, right, tolerance, "quantity")
          )
        case (None, _) =>
          MatchResult(
            false,
            s"Expected $right but got None",
            s"Got None when a value was expected"
          )
        case (Some(v), None) =>
          MatchResult(
            false,
            s"Expected None but got Some($v)",
            s"Got a value when None was expected"
          )
      }
    }
  }

  def approximate[Q <: Quantity[Q]](right: Q)(using tolerance: Q): Matcher[Q] =
    new SquantsMatcher(right)

  def approximate[Q <: Quantity[Q]](right: Option[Q])(using
      tolerance: Q
  ): Matcher[Option[Q]] = new OptionalSquantsMatcher(right)
}
