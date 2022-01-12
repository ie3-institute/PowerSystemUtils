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
import edu.ie3.util.osm.model.OsmContainer.ParOsmContainer.{
  ExtendedParOsmContainer,
  SimpleParOsmContainer
}
import edu.ie3.util.osm.model.OsmContainer.SeqOsmContainer.{
  ExtendedSeqOsmContainer,
  SimpleSeqOsmContainer
}
import edu.ie3.util.osm.model.OsmContainer.{ParOsmContainer, SeqOsmContainer}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.RelationMember.ExtendedRelationMember
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Relation.{
  ExtendedRelation,
  RelationMemberType,
  SimpleRelation
}
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.Way.ExtendedWay
import edu.ie3.util.osm.model.OsmEntity.ComposedEntity.{ExtendedWay, SimpleWay}
import edu.ie3.util.osm.model.OsmEntity.{
  ComposedEntity,
  ExtendedOsmEntity,
  Node
}

import scala.collection.mutable
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.{ParMap, ParSeq}
import scala.util.{Failure, Success, Try}

sealed trait OsmContainer {

  def nodeFromId(osmId: Long): Option[Node]

  def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]]

}

object OsmContainer {

  sealed trait SeqOsmContainer {
    val nodes: Seq[Node]
  }

  sealed trait ParOsmContainer {
    val nodes: ParSeq[Node]
  }

  sealed trait SimpleContainer extends OsmContainer {

    def par(): SimpleContainer with ParOsmContainer

    def seq(): SimpleContainer with SeqOsmContainer

  }

  sealed trait ExtendedContainer extends OsmContainer {

    def par(): ExtendedContainer with ParOsmContainer

    def seq(): ExtendedContainer with SeqOsmContainer

  }

  object SeqOsmContainer {

    // todo JH: conversion method to transfer simple to extended container

    final case class SimpleSeqOsmContainer(
        override val nodes: Seq[Node],
        ways: Seq[SimpleWay],
        relations: Seq[SimpleRelation],
        override protected val _relCache: mutable.Map[Long, ExtendedRelation] =
          mutable.Map.empty,
        override protected val _wayCache: mutable.Map[Long, ExtendedWay] =
          mutable.Map.empty
    ) extends SimpleContainer
        with SeqOsmContainer
        with LazyLogging
        with ContainerCache {

      lazy val nodesMap: Map[Long, Node] =
        nodes.map(node => (node.id, node)).toMap

      lazy val waysMap: Map[Long, SimpleWay] =
        ways.map(way => (way.id, way)).toMap

      lazy val relationsMap: Map[Long, SimpleRelation] =
        relations.map(relation => (relation.id, relation)).toMap

      override protected def _node: Long => Option[Node] = id =>
        nodesMap.get(id)

      override protected def _simpleWay: Long => Option[SimpleWay] = id =>
        waysMap.get(id)

      override protected def _simpleRelation: Long => Option[SimpleRelation] =
        id => relationsMap.get(id)

      override def nodeFromId(osmId: Long): Option[Node] =
        nodesMap.get(osmId)

      override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
        osmIds.map(nodesMap.get)

      override def par(): SimpleContainer with ParOsmContainer =
        SimpleParOsmContainer(nodes.par, ways.par, relations.par)

      override def seq(): SimpleContainer with SeqOsmContainer = this

    }

    final case class ExtendedSeqOsmContainer(
        override val nodes: Seq[Node],
        ways: Seq[ExtendedWay],
        relations: Seq[ExtendedRelation]
    ) extends ExtendedContainer
        with SeqOsmContainer {

      lazy val nodesMap: Map[Long, Node] =
        nodes.map(node => (node.id, node)).toMap

      lazy val waysMap: Map[Long, ExtendedWay] =
        ways.map(way => (way.id, way)).toMap

      lazy val relationsMap: Map[Long, ExtendedRelation] =
        relations.map(relations => (relations.id, relations)).toMap

      override def nodeFromId(osmId: Long): Option[Node] =
        nodesMap.get(osmId)

      override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
        osmIds.map(nodesMap.get)

      override def par(): ExtendedContainer with ParOsmContainer =
        ExtendedParOsmContainer(nodes.par, ways.par, relations.par)

      override def seq(): ExtendedContainer with SeqOsmContainer = this

    }

  }

  object ParOsmContainer {

    final case class SimpleParOsmContainer(
        override val nodes: ParSeq[Node],
        ways: ParSeq[SimpleWay],
        relations: ParSeq[SimpleRelation],
        override protected val _relCache: mutable.Map[Long, ExtendedRelation] =
          mutable.Map.empty,
        override protected val _wayCache: mutable.Map[Long, ExtendedWay] =
          mutable.Map.empty
    ) extends SimpleContainer
        with ParOsmContainer
        with ContainerCache {

      lazy val nodesMap: ParMap[Long, Node] =
        nodes.map(node => (node.id, node)).toMap

      lazy val waysMap: ParMap[Long, SimpleWay] =
        ways.map(way => (way.id, way)).toMap

      lazy val relationsMap: ParMap[Long, SimpleRelation] =
        relations.map(relation => (relation.id, relation)).toMap

      override protected def _node: Long => Option[Node] = id =>
        nodesMap.get(id)

      override protected def _simpleWay: Long => Option[SimpleWay] = id =>
        waysMap.get(id)

      override protected def _simpleRelation: Long => Option[SimpleRelation] =
        id => relationsMap.get(id)

      override def nodeFromId(osmId: Long): Option[Node] =
        nodesMap.get(osmId)

      override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
        osmIds.map(nodesMap.get)

      override def par(): SimpleContainer with ParOsmContainer =
        this

      override def seq(): SimpleContainer with SeqOsmContainer =
        SimpleSeqOsmContainer(nodes.seq, ways.seq, relations.seq)

    }

    final case class ExtendedParOsmContainer(
        override val nodes: ParSeq[Node],
        ways: ParSeq[ExtendedWay],
        relations: ParSeq[ExtendedRelation]
    ) extends ExtendedContainer
        with ParOsmContainer {

      lazy val nodesMap: ParMap[Long, Node] =
        nodes.map(node => (node.id, node)).toMap

      lazy val waysMap: ParMap[Long, ExtendedWay] =
        ways.map(way => (way.id, way)).toMap

      lazy val relationsMap: ParMap[Long, ExtendedRelation] =
        relations.map(relation => (relation.id, relation)).toMap

      override def nodeFromId(osmId: Long): Option[Node] =
        nodesMap.get(osmId)

      override def nodesFromIds(osmIds: Seq[Long]): Seq[Option[Node]] =
        osmIds.map(nodesMap.get)

      override def par(): ExtendedContainer with ParOsmContainer =
        this

      override def seq(): ExtendedContainer with SeqOsmContainer =
        ExtendedSeqOsmContainer(nodes.seq, ways.seq, relations.seq)

    }

  }

}
