/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.exceptions.OsmException
import edu.ie3.util.osm.model.OsmContainer.ParOsmContainer.SimpleParOsmContainer
import edu.ie3.util.osm.model.OsmContainer.SeqOsmContainer.SimpleSeqOsmContainer
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.{ExtendedWay, SimpleWay}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.{
  ExtendedRelation,
  RelationMemberType,
  SimpleRelation
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember.ExtendedRelationMember
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.{
  ExtendedWay,
  SimpleWay
}
import edu.ie3.util.osm.model.OsmEntity.{
  ExtendedOsmEntity,
  Node,
  SimpleOsmEntity
}

import scala.collection.immutable.{MapOps, OldHashMap}
import scala.collection.{GenMap, mutable}
import scala.collection.parallel.immutable.{ParMap, ParMapLike}
import scala.util.{Failure, Success, Try}

trait ContainerCache extends LazyLogging {

  protected def _simpleWay: Long => Option[SimpleWay]

  protected def _simpleRelation: Long => Option[SimpleRelation]

  protected def _node: Long => Option[Node]

  private val _wayCache: mutable.Map[Long, ExtendedWay] =
    scala.collection.mutable.Map.empty

  private val _relCache: mutable.Map[Long, ExtendedRelation] =
    scala.collection.mutable.Map.empty

  /** WARNING: METHOD IS NOT THREAD SAFE AND MAY CAUSE OVERHEAD IN GENERATING
    * THE SAME WAY MULTIPLE TIMES
    *
    * @param id
    * @return
    */
  def extendedWay(id: Long): Option[ExtendedWay] =
    _wayCache.get(id) match {
      case cachedWay @ Some(_) => cachedWay
      case None =>
        _simpleWay(id)
          .map(simpleWay =>
            ExtendedWay(
              simpleWay.id,
              simpleWay.nodes.collect(_node(_)).flatten,
              simpleWay.tags,
              simpleWay.metaInformation
            )
          )
          .map(extendedWay => {
            _wayCache.addOne(id, extendedWay)
            extendedWay
          })
    }

  /** WARNING: METHOD IS NOT THREAD SAFE AND MAY CAUSE OVERHEAD IN GENERATING
    * THE SAME WAY MULTIPLE TIMES
    *
    * @param id
    * @return
    */
  def extendedRelation(id: Long): Option[ExtendedRelation] =
    _relCache.get(id) match {
      case cachedRel @ Some(_) => cachedRel
      case None =>
        _simpleRelation(id)
          .flatMap(simpleRelation => {
            Try {
              simpleRelation.members.flatMap(simpleRelationMember => {
                simpleRelationMember.relationType match {
                  case RelationMemberType.Node =>
                    _node(simpleRelationMember.id) match {
                      case Some(node) =>
                        Some(ExtendedRelationMember(node))
                      case None =>
                        throw new OsmException(
                          s"Cannot build ExtendedNode for relation with '$id'. " +
                            s"Node with id '${simpleRelationMember.id} not found!'"
                        )

                    }
                  case RelationMemberType.Way =>
                    extendedWay(simpleRelationMember.id) match {
                      case Some(extendedWay) =>
                        Some(ExtendedRelationMember(extendedWay))
                      case None =>
                        throw new OsmException(
                          s"Cannot build ExtendedWay for relation with '$id'. " +
                            s"Way with id '${simpleRelationMember.id} not found!'"
                        )

                    }
                  case RelationMemberType.Relation =>
                    extendedRelation(simpleRelationMember.id) match {
                      case Some(extendedRel) =>
                        Some(ExtendedRelationMember(extendedRel))
                      case None =>
                        throw new OsmException(
                          s"Cannot build ExtendedRelation for relation with '$id'. " +
                            s"Relation with id '${simpleRelationMember.id} not found!'"
                        )

                    }
                  case RelationMemberType.Unrecognized =>
                    logger.warn(
                      s"SimpleRelationMember $simpleRelationMember has type 'Unrecognized'."
                    )
                    None
                }
              })
            } match {
              case Failure(exception) =>
                logger.error(
                  s"Cannot build extended Relation. Exception $exception"
                )
                None
              case Success(extendedRelationMembers) =>
                Some(
                  ExtendedRelation(
                    simpleRelation.id,
                    extendedRelationMembers,
                    simpleRelation.tags,
                    simpleRelation.metaInformation
                  )
                )
            }
          })
          .map(extendedRelation => {
            _relCache.addOne(id, extendedRelation)
            extendedRelation
          })
    }

}
