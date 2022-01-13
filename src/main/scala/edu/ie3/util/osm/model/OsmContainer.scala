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
      nodes: Seq[Node],
      ways: Seq[Way],
      relations: Seq[Relation]
  ) extends OsmContainer
      with LazyLogging
      with RichWaySupport {

    lazy val nodesMap: Map[Long, Node] =
      nodes.map(node => (node.id, node)).toMap

    lazy val waysMap: Map[Long, Way] =
      ways.map(way => (way.id, way)).toMap

    lazy val relationsMap: Map[Long, Relation] =
      relations.map(relation => (relation.id, relation)).toMap

    override def node(nodeId: Long): Option[Node] =
      nodesMap.get(nodeId)

    override def nodes(nodeIds: Seq[Long]): Seq[Option[Node]] =
      nodeIds.map(nodesMap.get)

    override def way(wayId: Long): Option[Way] = waysMap.get(wayId)

    override def ways(wayIds: Seq[Long]): Seq[Option[Way]] =
      wayIds.map(waysMap.get)

    override def relation(relationId: Long): Option[Relation] =
      relationsMap.get(relationId)

    override def relations(relationIds: Seq[Long]): Seq[Option[Relation]] =
      relationIds.map(relationsMap.get)

    override def par(): ParOsmContainer =
      ParOsmContainer(nodes.par, ways.par, relations.par)

    override def seq(): SeqOsmContainer = this

    override protected def _getNode: Long => Option[Node] = (nodeId: Long) =>
      nodesMap.get(nodeId)

    override protected def _getWay: Long => Option[Way] = (nodeId: Long) =>
      waysMap.get(nodeId)

  }

  final case class ParOsmContainer(
      nodes: ParSeq[Node],
      ways: ParSeq[Way],
      relations: ParSeq[Relation]
  ) extends OsmContainer
      with RichWaySupport {

    lazy val nodesMap: ParMap[Long, Node] =
      nodes.map(node => (node.id, node)).toMap

    lazy val waysMap: ParMap[Long, Way] =
      ways.map(way => (way.id, way)).toMap

    lazy val relationsMap: ParMap[Long, Relation] =
      relations.map(relation => (relation.id, relation)).toMap

    override def node(nodeId: Long): Option[Node] =
      nodesMap.get(nodeId)

    override def nodes(nodeIds: Seq[Long]): Seq[Option[Node]] =
      nodeIds.map(nodesMap.get)

    override def way(wayId: Long): Option[Way] = waysMap.get(wayId)

    override def ways(wayIds: Seq[Long]): Seq[Option[Way]] =
      wayIds.map(waysMap.get)

    override def relation(relationId: Long): Option[Relation] =
      relationsMap.get(relationId)

    override def relations(relationIds: Seq[Long]): Seq[Option[Relation]] =
      relationIds.map(relationsMap.get)

    override def par(): ParOsmContainer =
      this

    override def seq(): SeqOsmContainer =
      SeqOsmContainer(nodes.seq, ways.seq, relations.seq)

    override protected def _getNode: Long => Option[Node] = (nodeId: Long) =>
      nodesMap.get(nodeId)

    override protected def _getWay: Long => Option[Way] = (nodeId: Long) =>
      waysMap.get(nodeId)

  }

}
