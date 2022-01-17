/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.Relation.{
  RelationMember,
  RelationMemberType
}
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}
import edu.ie3.util.osm.model.OsmEntity.{Node, Relation, Way}
import edu.ie3.util.quantities.{PowerSystemUnits, QuantityMatchers}
import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units
import org.locationtech.jts.geom.Point
import edu.ie3.util.geo.RichGeometries.*
import edu.ie3.util.osm.model.RelationEntitiesSupport.RelationEntities
import org.scalatest.matchers.dsl.MatcherWords.contain

import scala.collection.parallel.CollectionConverters.*
import edu.ie3.util.osm.SimpleOsmTestData

class OsmContainerSpec
    extends Matchers
    with QuantityMatchers
    with AnyWordSpecLike
    with SimpleOsmTestData {

  "A SeqOsmContainer" should {

    "instantiate correctly from sequences" in {

      val container = SeqOsmContainer(nodes, ways, relations)

      inside(container) { case SeqOsmContainer(nodes, ways, relations) =>
        nodes.size shouldBe 3
        nodes.values.toList shouldBe this.nodes

        ways.size shouldBe 2
        ways.values.toList shouldBe this.ways

        relations.size shouldBe 2
        relations.values.toList shouldBe this.relations
      }

    }

    "provide additional way information correctly" in {

      val container = SeqOsmContainer(nodes, ways, relations)

      container
        .wayAreaOnEarth(closedWay)
        .foreach(quantity =>
          quantity should equalWithTolerance(
            Quantities.getQuantity(0.0027641020519695303, Units.SQUARE_METRE)
          )
        )
      container.wayCentroid(closedWay) shouldBe Some(
        GeoUtils.buildCoordinate(49.378921000000005, 6.597183).toPoint
      )
      container.wayAreaCovers(n2.coordinate, closedWay) shouldBe true
      container.wayPolygon(closedWay) shouldBe Some(
        GeoUtils.buildPolygon(
          Seq(n1, n2, n3, n1).map { case Node(_, lat, long, _, _) =>
            GeoUtils.buildCoordinate(lat, long)
          }.toArray
        )
      )

      container.wayAreaOnEarth(openWay.id) shouldBe None
      container.wayCentroid(openWay.id) shouldBe None
      container.wayAreaCovers(n2.coordinate, openWay.id) shouldBe false
      container.wayPolygon(openWay.id) shouldBe None
    }

    "provide relation entities correctly" in {

      val container = SeqOsmContainer(nodes, ways, relations)

      inside(container.relationEntities(r2.id)) {
        case Some(RelationEntities(relationId, nodes, ways, relations)) =>
          relationId shouldBe r2.id
          nodes should contain theSameElementsAs Map(n2.id -> n2, n1.id -> n1)
          ways should contain theSameElementsAs Map(closedWay.id -> closedWay)
          relations should contain theSameElementsAs Map(r1.id -> r1)
      }
    }

    "transform to a ParOsmContainer correctly" in {

      inside(ParOsmContainer(nodes.par, ways.par, relations.par).par()) {
        case ParOsmContainer(_, _, _) =>
          succeed
      }

    }

  }

  "A ParOsmContainer" should {

    "instantiate correctly from sequences" in {

      val container = ParOsmContainer(nodes.par, ways.par, relations.par)

      inside(container) { case ParOsmContainer(nodes, ways, relations) =>
        nodes.size shouldBe 3
        nodes.values.toList should contain theSameElementsAs this.nodes

        ways.size shouldBe 2
        ways.values.toList should contain theSameElementsAs this.ways

        relations.size shouldBe 2
        relations.values.toList should contain theSameElementsAs this.relations
      }

    }

    "provide additional way information correctly" in {

      val container = ParOsmContainer(nodes.par, ways.par, relations.par)

      container
        .wayAreaOnEarth(closedWay)
        .foreach(quantity =>
          quantity should equalWithTolerance(
            Quantities.getQuantity(0.0027641020519695303, Units.SQUARE_METRE)
          )
        )
      container.wayCentroid(closedWay) shouldBe Some(
        GeoUtils.buildCoordinate(49.378921000000005, 6.597183).toPoint
      )
      container.wayAreaCovers(n2.coordinate, closedWay) shouldBe true
      container.wayPolygon(closedWay) shouldBe Some(
        GeoUtils.buildPolygon(
          Seq(n1, n2, n3, n1).map { case Node(_, lat, long, _, _) =>
            GeoUtils.buildCoordinate(lat, long)
          }.toArray
        )
      )

      container.wayAreaOnEarth(openWay.id) shouldBe None
      container.wayCentroid(openWay.id) shouldBe None
      container.wayAreaCovers(n2.coordinate, openWay.id) shouldBe false
      container.wayPolygon(openWay.id) shouldBe None
    }

    "provide relation entities correctly" in {

      val container = ParOsmContainer(nodes.par, ways.par, relations.par)

      inside(container.relationEntities(r2.id)) {
        case Some(RelationEntities(relationId, nodes, ways, relations)) =>
          relationId shouldBe r2.id
          nodes should contain theSameElementsAs Map(n2.id -> n2, n1.id -> n1)
          ways should contain theSameElementsAs Map(closedWay.id -> closedWay)
          relations should contain theSameElementsAs Map(r1.id -> r1)
      }
    }

    "transform to a SeqOsmContainer correctly" in {

      inside(ParOsmContainer(nodes.par, ways.par, relations.par).seq()) {
        case SeqOsmContainer(_, _, _) =>
          succeed
      }

    }

  }

}
