/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.exceptions.OsmException
import edu.ie3.util.osm.model.OsmEntity
import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}
import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.{Relation, Way}
import edu.ie3.util.osm.model.OsmEntity.Node

import scala.collection.parallel.immutable.{ParMap, ParSeq}
import scala.util.{Failure, Success, Try}
import scala.collection.parallel.CollectionConverters.*

sealed trait OsmContainer {

  def node(nodeId: Long): Option[Node]

  def nodes(nodeIds: Seq[Long]): Seq[Option[Node]]

  def way(wayId: Long): Option[Way]

  def ways(wayIds: Seq[Long]): Seq[Option[Way]]

  def relation(relationId: Long): Option[Relation]

  def relations(relationIds: Seq[Long]): Seq[Option[Relation]]

  def par(): ParOsmContainer

  def seq(): SeqOsmContainer

}

object OsmContainer {

  final case class SeqOsmContainer(
      nodes: Map[Long, Node],
      ways: Map[Long, Way],
      relations: Map[Long, Relation]
  ) extends OsmContainer
      with RichClosedWaySupport
      with RelationEntitiesSupport {

    override def node(nodeId: Long): Option[Node] =
      nodes.get(nodeId)

    override def nodes(nodeIds: Seq[Long]): Seq[Option[Node]] =
      nodeIds.map(nodes.get)

    override def way(wayId: Long): Option[Way] = ways.get(wayId)

    override def ways(wayIds: Seq[Long]): Seq[Option[Way]] =
      wayIds.map(ways.get)

    override def relation(relationId: Long): Option[Relation] =
      relations.get(relationId)

    override def relations(relationIds: Seq[Long]): Seq[Option[Relation]] =
      relationIds.map(relations.get)

    override def par(): ParOsmContainer =
      ParOsmContainer(nodes.par, ways.par, relations.par)

    override def seq(): SeqOsmContainer = this

    override protected def _getNode: Long => Option[Node] = (nodeId: Long) =>
      nodes.get(nodeId)

    override protected def _getWay: Long => Option[Way] = (nodeId: Long) =>
      ways.get(nodeId)

    override protected def _getRelation: Long => Option[Relation] =
      (nodeId: Long) => relations.get(nodeId)

  }

  object SeqOsmContainer {
    def apply(
        nodes: Seq[Node],
        ways: Seq[Way],
        relations: Seq[Relation]
    ): SeqOsmContainer =
      new SeqOsmContainer(
        nodes.map(node => (node.id, node)).toMap,
        ways.map(way => (way.id, way)).toMap,
        relations.map(relation => (relation.id, relation)).toMap
      )
  }

  final case class ParOsmContainer(
      nodes: ParMap[Long, Node],
      ways: ParMap[Long, Way],
      relations: ParMap[Long, Relation]
  ) extends OsmContainer
      with RichClosedWaySupport
      with RelationEntitiesSupport {

    override def node(nodeId: Long): Option[Node] =
      nodes.get(nodeId)

    override def nodes(nodeIds: Seq[Long]): Seq[Option[Node]] =
      nodeIds.map(nodes.get)

    override def way(wayId: Long): Option[Way] = ways.get(wayId)

    override def ways(wayIds: Seq[Long]): Seq[Option[Way]] =
      wayIds.map(ways.get)

    override def relation(relationId: Long): Option[Relation] =
      relations.get(relationId)

    override def relations(relationIds: Seq[Long]): Seq[Option[Relation]] =
      relationIds.map(relations.get)

    override def par(): ParOsmContainer =
      this

    override def seq(): SeqOsmContainer =
      SeqOsmContainer(nodes.seq, ways.seq, relations.seq)

    override protected def _getNode: Long => Option[Node] = (nodeId: Long) =>
      nodes.get(nodeId)

    override protected def _getWay: Long => Option[Way] = (nodeId: Long) =>
      ways.get(nodeId)

    override protected def _getRelation: Long => Option[Relation] =
      (nodeId: Long) => relations.get(nodeId)

  }

  object ParOsmContainer {

    def apply(
        nodes: ParSeq[Node],
        ways: ParSeq[Way],
        relations: ParSeq[Relation]
    ): ParOsmContainer = new ParOsmContainer(
      nodes.map(node => (node.id, node)).toMap,
      ways.map(way => (way.id, way)).toMap,
      relations.map(relation => (relation.id, relation)).toMap
    )

  }

}
