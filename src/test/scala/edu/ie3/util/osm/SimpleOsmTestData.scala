/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.model.OsmEntity
import edu.ie3.util.osm.model.OsmEntity.{Node, Relation}
import edu.ie3.util.osm.model.OsmEntity.Relation.{
  RelationMember,
  RelationMemberType
}
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}

trait SimpleOsmTestData {

  protected val n1: Node = Node(1L, 49.38, 6.5971830, Map.empty)
  protected val n2: Node = Node(2L, 49.378, 6.5971830, Map.empty)
  protected val n3: Node = Node(3L, 49.377842, 6.5971830, Map.empty)

  protected val closedWay: ClosedWay = ClosedWay(
    100L,
    Seq(n1.id, n2.id, n3.id, n1.id),
    Map.empty,
    None
  )

  protected val openWay: OpenWay =
    OpenWay(11L, Seq(n1.id, n2.id, n3.id), Map.empty, None)

  protected val nodes = Seq(
    n1,
    n2,
    n3
  )

  protected val nodesMap: Map[Long, Node] = Map(
    n1.id -> n1,
    n2.id -> n2,
    n3.id -> n3
  )

  protected val ways: Seq[OsmEntity.Way] = Seq(closedWay, openWay)

  protected val r1: Relation = Relation(
    1000L,
    Seq(
      RelationMember(
        n1.id,
        RelationMemberType(n1),
        ""
      ),
      RelationMember(
        closedWay.id,
        RelationMemberType(closedWay),
        ""
      )
    ),
    Map.empty,
    None
  )

  protected val r2: Relation = Relation(
    1001L,
    Seq(
      RelationMember(
        n2.id,
        RelationMemberType(n2),
        ""
      ),
      RelationMember(
        r1.id,
        RelationMemberType(r1),
        ""
      )
    ),
    Map.empty,
    None
  )

  protected val relations: Seq[Relation] = Seq(r1, r2)

}
