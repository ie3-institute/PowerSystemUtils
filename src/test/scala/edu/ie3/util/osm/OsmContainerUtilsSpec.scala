/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}
import edu.ie3.util.quantities.QuantityMatchers
import org.scalatest.Inside.inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.immutable.ParMap
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}

class OsmContainerUtilsSpec
    extends Matchers
    with QuantityMatchers
    with AnyWordSpecLike
    with SimpleOsmTestData {

  "The OsmContainerUtils" should {

    "reduce a given SeqOsmContainer instance correctly" in {
      val incompleteNodes = Seq(n1, n2)
      val seqContainer = SeqOsmContainer(incompleteNodes, ways, relations)

      val expectedWays = Map(
        100L ->
          ClosedWay(
            100L,
            Seq(n1.id, n2.id, n1.id),
            Map.empty,
            None,
            Some(1)
          ),
        11L -> OpenWay(11L, Seq(n1.id, n2.id), Map.empty, None, Some(1))
      )

      inside(OsmContainerUtils.reduce(seqContainer)) {
        case SeqOsmContainer(nodes, ways, relations) =>
          nodes shouldBe seqContainer.nodes
          ways shouldBe expectedWays
          relations shouldBe seqContainer.relations
      }

    }

    "reduce a given ParOsmContainer instance correctly" in {
      val incompleteNodes = Seq(n1, n2)
      val parContainer =
        ParOsmContainer(incompleteNodes.par, ways.par, relations.par)

      val expectedWays = ParMap(
        100L ->
          ClosedWay(
            100L,
            Seq(n1.id, n2.id, n1.id),
            Map.empty,
            None,
            Some(1)
          ),
        11L -> OpenWay(11L, Seq(n1.id, n2.id), Map.empty, None, Some(1))
      )

      inside(OsmContainerUtils.reduce(parContainer)) {
        case ParOsmContainer(nodes, ways, relations) =>
          nodes shouldBe parContainer.nodes
          ways shouldBe expectedWays
          relations shouldBe parContainer.relations
      }

    }

    "parReduce a given SeqOsmContainer instance correctly" in {
      val incompleteNodes = Seq(n1, n2)
      val seqContainer = SeqOsmContainer(incompleteNodes, ways, relations)

      val expectedWays = Map(
        100L ->
          ClosedWay(
            100L,
            Seq(n1.id, n2.id, n1.id),
            Map.empty,
            None,
            Some(1)
          ),
        11L -> OpenWay(11L, Seq(n1.id, n2.id), Map.empty, None, Some(1))
      )

      inside(
        Await.result(
          OsmContainerUtils.reducePar(seqContainer),
          Duration(10, SECONDS)
        )
      ) { case SeqOsmContainer(nodes, ways, relations) =>
        nodes shouldBe seqContainer.nodes
        ways shouldBe expectedWays
        relations shouldBe seqContainer.relations
      }

    }

    "parReduce a given ParOsmContainer instance correctly" in {
      val incompleteNodes = Seq(n1, n2)
      val parContainer =
        ParOsmContainer(incompleteNodes.par, ways.par, relations.par)

      val expectedWays = ParMap(
        100L ->
          ClosedWay(
            100L,
            Seq(n1.id, n2.id, n1.id),
            Map.empty,
            None,
            Some(1)
          ),
        11L -> OpenWay(11L, Seq(n1.id, n2.id), Map.empty, None, Some(1))
      )

      inside(
        Await.result(
          OsmContainerUtils.reducePar(parContainer),
          Duration(10, SECONDS)
        )
      ) { case ParOsmContainer(nodes, ways, relations) =>
        nodes shouldBe parContainer.nodes
        ways shouldBe expectedWays
        relations shouldBe parContainer.relations
      }

    }

  }

}
