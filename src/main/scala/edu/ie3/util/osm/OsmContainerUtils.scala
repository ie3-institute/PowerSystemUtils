/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.{Relation, Way}
import edu.ie3.util.osm.model.{
  OsmContainer,
  OsmEntity,
  RelationEntitiesSupport,
  RichClosedWaySupport
}

import scala.collection.parallel.immutable.{ParMap, ParSeq}
import scala.concurrent.Future
import scala.collection.parallel.CollectionConverters._

object OsmContainerUtils {

  def reduce(osmContainer: OsmContainer): OsmContainer = {
    osmContainer match {
      case container @ SeqOsmContainer(nodes, ways, relations) =>
        val updatedWays: Map[Long, Way] = ways.values
          .map(updateWays(_, (nodeId: Long) => nodes.contains(nodeId)))
          .toMap

        val allEntities: Map[Long, OsmEntity] = ways ++ nodes ++ relations
        val updatedRelations: Map[Long, Relation] =
          relations.values
            .map(
              updateRelations(
                _,
                (entityId: Long) => allEntities.contains(entityId)
              )
            )
            .toMap

        container.copy(ways = updatedWays, relations = updatedRelations)
      case container @ ParOsmContainer(nodes, ways, relations) =>
        val updatedWays: ParMap[Long, Way] = ways.values
          .map(updateWays(_, (nodeId: Long) => nodes.contains(nodeId)))
          .toMap
        val allEntities: ParMap[Long, OsmEntity] = ways ++ nodes ++ relations
        val updatedRelations: ParMap[Long, Relation] =
          relations.values
            .map(
              updateRelations(
                _,
                (entityId: Long) => allEntities.contains(entityId)
              )
            )
            .toMap

        container.copy(ways = updatedWays, relations = updatedRelations)
    }
  }

  def reducePar(osmContainer: OsmContainer): Future[OsmContainer] = {
    import concurrent.ExecutionContext.Implicits.global

    def futWayUpdates(ways: Iterable[Way], contains: Long => Boolean) =
      Future
        .traverse(ways)(wayEntity => Future(updateWays(wayEntity, contains)))
        .map(_.toMap)

    def futRelUpdates(
        relations: Iterable[Relation],
        contains: Long => Boolean
    ) =
      Future
        .traverse(relations)(relationEntity =>
          Future(updateRelations(relationEntity, contains))
        )
        .map(_.toMap)

    osmContainer match {
      case container @ SeqOsmContainer(nodes, ways, relations) =>
        val updatedWays: Future[Map[Long, Way]] =
          futWayUpdates(ways.values, (nodeId: Long) => nodes.contains(nodeId))
        val allEntities: Map[Long, OsmEntity] = ways ++ nodes ++ relations
        val updatedRelations: Future[Map[Long, Relation]] = futRelUpdates(
          relations.values,
          (entityId: Long) => allEntities.contains(entityId)
        )

        updatedWays.zip(updatedRelations).map {
          case (updatedWays, updatedRelations) =>
            container.copy(ways = updatedWays, relations = updatedRelations)
        }
      case container @ ParOsmContainer(nodes, ways, relations) =>
        val updatedWays: Future[ParMap[Long, Way]] = futWayUpdates(
          ways.values.seq,
          (nodeId: Long) => nodes.contains(nodeId)
        ).map(_.par)

        val allEntities: ParMap[Long, OsmEntity] = ways ++ nodes ++ relations
        val updatedRelations: Future[ParMap[Long, Relation]] = futRelUpdates(
          relations.values.seq,
          (entityId: Long) => allEntities.contains(entityId)
        ).map(_.par)

        updatedWays.zip(updatedRelations).map {
          case (updatedWays, updatedRelations) =>
            container.copy(ways = updatedWays, relations = updatedRelations)

        }
    }
  }

  private def updateRelations(
      relation: Relation,
      contains: Long => Boolean
  ): (Long, Relation) =
    val availableMemberRelationEntityIds: Seq[Long] =
      relation.members.map(_.id).filter(contains)
    val relationMemberEntityMap: Map[Long, Relation.RelationMember] =
      relation.members.map(member => member.id -> member).toMap
    relation.id -> relation.copy(members =
      availableMemberRelationEntityIds.flatMap(relationMemberEntityMap.get)
    )

  private def updateWays(way: Way, contains: Long => Boolean) = {
    val availableNodes = way.nodes.filter(contains)
    way match {
      case openWay: Way.OpenWay =>
        openWay.id -> openWay.copy(nodes = availableNodes)
      case closedWay: Way.ClosedWay =>
        closedWay.id -> closedWay.copy(nodes = availableNodes)
    }
  }
}