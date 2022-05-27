/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.osm.model.OsmEntity.Relation.RelationMemberType
import edu.ie3.util.osm.model.OsmEntity.{Node, Relation, Way}
import edu.ie3.util.osm.model.RelationEntitiesSupport.RelationEntities

/** Trait to be used for [[OsmContainer]] instances to add support for
  * [[RelationEntities]] provision
  */
trait RelationEntitiesSupport extends WayCache with LazyLogging {

  protected def _getRelation: Long => Option[Relation]

  type RelationId = Long

  private val _relationEntityCache =
    new java.util.concurrent.ConcurrentHashMap[RelationId, RelationEntities]()

  /** Tries to create a [[RelationEntities]] instance based on the provided
    * identifier of the requested [[Relation]]
    *
    * @param relationId
    *   the identifier of the relation of interest
    * @return
    *   an optional instance of [[RelationEntities]]
    */
  def relationEntities(relationId: Long): Option[RelationEntities] = {

    _relationEntityCache.safeGet(relationId) match {
      case relationEntities @ Some(_) =>
        relationEntities
      case None =>
        _getRelation(relationId)
          .map(relation => {
            val (nodes, ways, relations) = relation.members.foldLeft(
              Map.empty[Long, Node],
              Map.empty[Long, Way],
              Map.empty[Long, Relation]
            ) { case ((nodes, ways, relations), relationMember) =>
              relationMember.relationType match {
                case RelationMemberType.Node =>
                  _getNode(relationMember.id) match {
                    case Some(node) =>
                      (nodes + (node.id -> node), ways, relations)
                    case None =>
                      (nodes, ways, relations)
                  }
                case RelationMemberType.Way =>
                  _getWay(relationMember.id) match {
                    case Some(way) =>
                      (nodes, ways + (way.id -> way), relations)
                    case None =>
                      (nodes, ways, relations)
                  }

                case RelationMemberType.Relation =>
                  relationEntities(relationMember.id)
                    .zip(_getRelation(relationMember.id)) match {
                    case Some((entities, relation)) =>
                      (
                        nodes ++ entities.nodes,
                        ways ++ entities.ways,
                        relations ++ entities.relations + (relation.id -> relation)
                      )
                    case None =>
                      (nodes, ways, relations)
                  }
                case RelationMemberType.Unrecognized =>
                  logger.warn(
                    s"RelationMember '$relationMember' has type 'Unrecognized'."
                  )
                  (nodes, ways, relations)
              }
            }
            RelationEntities(relationId, nodes, ways, relations)
          })
          .map(relationEntities => {
            _relationEntityCache.putIfAbsent(relationId, relationEntities)
            relationEntities
          })
    }
  }
}

object RelationEntitiesSupport {

  /** Class holding all instances of [[Node]] s, [[Way]] s, and [[Relation]] s
    * inside a specific [[Relation]]. This class is intended to be recursive and
    * comprehensive. That means, if it holds [[Relation]] instances, all
    * [[Node]] and [[Way]] instances of all downstream relations are also
    * included comparable to a deep instance.
    *
    * If specific entity instances are not part of the container, they are
    * (obviously) not included!
    *
    * @param relationId
    *   the identifier of the [[Relation]] this class holds all entities for
    * @param nodes
    *   nodes inside the specific relation
    * @param ways
    *   ways inside the specific relation
    * @param relations
    *   relations inside the specific relation
    */
  final case class RelationEntities(
      relationId: Long,
      nodes: Map[Long, Node],
      ways: Map[Long, Way],
      relations: Map[Long, Relation]
  )
}
