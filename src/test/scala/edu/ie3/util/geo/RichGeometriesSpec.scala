/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.geo

import edu.ie3.util.geo.GeoUtils.DEFAULT_GEOMETRY_FACTORY
import edu.ie3.util.geo.RichGeometries.GeoPolygon
import edu.ie3.util.quantities.QuantityUtils.RichQuantityDouble
import org.locationtech.jts.geom.Coordinate
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class RichGeometriesSpec extends Matchers with AnyWordSpecLike {

  "A rich polygon" should {

    "calculate area on earth correctly" in {
      val coordinateA = new Coordinate(8.748497631269068, 51.72341137638795)
      val coordinateB = new Coordinate(8.76167264195022, 51.723225286136866)
      val coordinateC = new Coordinate(8.76240220280227, 51.715568337479546)
      val coordinateD = new Coordinate(8.74832596989211, 51.71546198184121)
      val polygon = DEFAULT_GEOMETRY_FACTORY.createPolygon(
        Array(coordinateA, coordinateB, coordinateC, coordinateD, coordinateA)
      )
      val actual = 813431.49.asSquareMetre
      polygon.calcAreaOnEarth
        .divide(actual)
        .getValue
        .doubleValue() shouldBe 1d +- 0.01
    }

  }

}
