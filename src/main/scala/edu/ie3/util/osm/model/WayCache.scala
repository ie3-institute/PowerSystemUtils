/*
 * © 2022. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.osm.model

import edu.ie3.util.osm.model.OsmEntity.{Node, Way}
import cats.implicits.*
import org.locationtech.jts.geom.Polygon

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.util.Try

private[model] trait WayCache {

  protected def _getNode: Long => Option[Node]

  protected def _getWay: Long => Option[Way]

  implicit class RichConcurrentMap[K, V](concurrentMap: ConcurrentMap[K, V]) {

    def safeGet(key: K): Option[V] =
      Try(Option(concurrentMap.get(key))).toOption.flatten

  }

  type WayId = Long

  private lazy val _wayNodeCache =
    java.util.concurrent.ConcurrentHashMap[WayId, Seq[Node]]()

  protected def wayNodes(way: Way): Option[Seq[Node]] =
    _wayNodeCache
      .safeGet(way.id)
      .orElse(
        way.nodes
          .collect(_getNode(_))
          .traverse(identity)
          .map(nodes => {
            _wayNodeCache.putIfAbsent(way.id, nodes)
            nodes
          })
      )
}
