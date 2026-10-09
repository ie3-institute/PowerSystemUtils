/*
 * © 2026. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util

/** Some common utils for matchers.
  */
trait MatcherUtils {
  protected def assembleRawFailureMessage[Q, T](
      lhs: Q,
      rhs: Q,
      tolerance: T,
      str: String
  ) = s"The $str $lhs and $rhs differ more than $tolerance in value"

  protected def assembleNegatedFailureMessage[Q, T](
      lhs: Q,
      rhs: Q,
      tolerance: T,
      str: String
  ) = s"The $str $lhs and $rhs differ less than $tolerance in value"
}
