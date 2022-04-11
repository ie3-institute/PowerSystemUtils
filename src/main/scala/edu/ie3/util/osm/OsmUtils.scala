/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm

import com.typesafe.scalalogging.LazyLogging
import edu.ie3.util.exceptions.OsmException
import edu.ie3.util.geo.GeoUtils
import edu.ie3.util.geo.RichGeometries.RichCoordinate
import edu.ie3.util.osm.model.CommonOsmKey.{Building, Highway, Landuse}
import edu.ie3.util.osm.model.OsmEntity
import edu.ie3.util.osm.model.OsmEntity.Node
import edu.ie3.util.osm.model.OsmEntity.Way.{ClosedWay, OpenWay}
import org.locationtech.jts.geom.{Coordinate, LineString, Point, Polygon}

import scala.collection.parallel.immutable.ParSeq
import scala.util.{Failure, Success, Try}

object OsmUtils extends LazyLogging {

  val par: ParOsmUtil.type = ParOsmUtil

  object ParOsmUtil {

    def extractBuildings(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Building.toString, specificTagValues)

    def extractLanduses(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Landuse.toString, specificTagValues)

    def extractHighways(
        entities: ParSeq[OsmEntity],
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      entitiesByKey(entities, Highway.toString, specificTagValues)

    def entitiesByKey(
        entities: ParSeq[OsmEntity],
        osmKey: String,
        specificTagValues: Option[Set[String]] = None
    ): ParSeq[OsmEntity] =
      specificTagValues match {
        case Some(tagValues) =>
          entities.filter(_.hasKeyValuesPairOr(osmKey, tagValues))
        case None =>
          entities.filter(_.hasKey(osmKey))
      }

  }

  def extractBuildings(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Building.toString, specificTagValues)

  def extractLanduses(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Landuse.toString, specificTagValues)

  def extractHighways(
      entities: Seq[OsmEntity],
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    entitiesByKey(entities, Highway.toString, specificTagValues)

  def entitiesByKey(
      entities: Seq[OsmEntity],
      osmKey: String,
      specificTagValues: Option[Set[String]] = None
  ): Seq[OsmEntity] =
    specificTagValues match {
      case Some(tagValues) =>
        entities.filter(_.hasKeyValuesPairOr(osmKey, tagValues))
      case None =>
        entities.filter(_.hasKey(osmKey))
    }
  object GeometryUtils {

    /** Build a polygon from a closed way.
      *
      * @param closedWay
      *   the closed way
      * @param nodesMap
      *   the mapping from node id to node
      * @return
      */
    def buildPolygon(
        closedWay: ClosedWay,
        nodesMap: Map[Long, Node]
    ): Try[Polygon] = {
      val coordinates = closedWay.nodes.map(nodeId => {
        val node = nodesMap.getOrElse(
          nodeId,
          return Failure(
            OsmException(
              s"Couldn't convert the closed way since node with id $nodeId wasn't found."
            )
          )
        )
        new Coordinate(node.longitude, node.latitude)
      })
      Success(GeoUtils.buildPolygon(coordinates.toArray))
    }

    /** Builds a polygon from a sequence of Nodes
      *
      * @param nodes
      *   the nodes
      * @return
      *   the corresponding polygon
      */
    def buildPolygon(nodes: Seq[Node]): Polygon =
      GeoUtils.buildPolygon(
        nodes
          .map(node => new Coordinate(node.longitude, node.latitude))
          .toArray
      )

  }
}
