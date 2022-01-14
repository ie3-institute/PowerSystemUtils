/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.exceptions.OsmException
import edu.ie3.util.osm.model.OsmEntity.Relation.{
  RelationMember,
  RelationMemberType
}
import edu.ie3.util.osm.model.OsmEntity.{Node, Relation, Way}

/** Trait to be used for [[OsmContainer]] instances to add support for
  * [[RelationEntities]] provision
  */
trait RelationEntitiesSupport extends WayCache with LazyLogging {

  protected def _getRelation: Long => Option[Relation]

  type RelationId = Long

  private val _relationEntityCache =
    java.util.concurrent.ConcurrentHashMap[RelationId, RelationEntities]()

  /** Class holding all instances of [[Node]] s, [[Way]] s, and [[Relation]] s
    * inside a specific [[Relation]]
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

  /** Tries to create a [[RelationEntities]] instance based on the provided
    * identifier of the requested [[Relation]]
    *
    * @param relationId
    *   the identifier of the relation of interest
    * @return
    *   an optional instance of [[RelationEntities]]
    */
  def relationEntities(relationId: Long): Option[RelationEntities] = {

    def _relationEntities(relation: RelationMember) =
      relationEntities(relation.id)

    def _wayEntity(way: RelationMember) =
      _getWay(way.id)

    def _nodeEntity(node: RelationMember) =
      _getNode(node.id)

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
                  _nodeEntity(relationMember) match {
                    case Some(node) =>
                      (nodes + (node.id -> node), ways, relations)
                    case None =>
                      (nodes, ways, relations)
                  }
                case RelationMemberType.Way =>
                  _wayEntity(relationMember) match {
                    case Some(way) =>
                      (nodes, ways + (way.id -> way), relations)
                    case None =>
                      (nodes, ways, relations)
                  }

                case RelationMemberType.Relation =>
                  _relationEntities(relationMember) match {
                    case Some(entities) =>
                      (
                        nodes ++ entities.nodes,
                        ways ++ entities.ways,
                        relations ++ entities.relations
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
