/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.OsmEntity
import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}
//import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.{Way, Relation}
import edu.ie3.util.osm.model.OsmEntity.Node

import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.{ParMap, ParSeq}

sealed trait OsmContainer {
  def nodeFromId(osmId: Long): Option[Node]

  def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]]

//  def par: ParOsmContainer
//
//  def seq: SeqOsmContainer
}

object OsmContainer {

//  final case class SeqOsmContainer(
//      nodes: Seq[Node],
//      ways: Seq[Way],
//      relations: Seq[Relation]
//  ) extends OsmContainer {
//
//    lazy val nodesMap: Map[Long, Node] =
//      nodes.map(node => (node.id, node)).toMap
//
//    lazy val waysMap: Map[Long, Way] =
//      ways.map(way => (way.id, way)).toMap
//
//    lazy val relationsMap: Map[Long, Way] =
//      ways.map(way => (way.id, way)).toMap
//
//    override def nodeFromId(osmId: Long): Option[Node] =
//      nodesMap.get(osmId)
//
//    override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
//      osmIds.map(nodesMap.get)
//
//    override def par: ParOsmContainer =
//      ParOsmContainer(nodes.par, ways.par, relations.par)
//
//    override def seq: SeqOsmContainer = this
//
//  }
//
//  final case class ParOsmContainer(
//      nodes: ParSeq[Node],
//      ways: ParSeq[Way],
//      relations: ParSeq[Relation]
//  ) extends OsmContainer {
//
//    lazy val nodesMap: ParMap[Long, Node] =
//      nodes.map(node => (node.id, node)).toMap.par
//
//    lazy val waysMap: ParMap[Long, Way] =
//      ways.map(way => (way.id, way)).toMap.par
//
//    lazy val relationsMap: ParMap[Long, Way] =
//      ways.map(way => (way.id, way)).toMap.par
//
//    override def nodeFromId(osmId: Long): Option[Node] =
//      nodesMap.get(osmId)
//
//    override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
//      osmIds.map(nodesMap.get)
//
//    override def par: ParOsmContainer = this
//
//    override def seq: SeqOsmContainer =
//      SeqOsmContainer(nodes.seq, ways.seq, relations.seq)
//  }
}
